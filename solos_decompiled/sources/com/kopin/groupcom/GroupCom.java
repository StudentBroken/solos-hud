package com.kopin.groupcom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.kopin.peloton.Failure;
import com.kopin.peloton.groupcom.ChatGroup;
import com.kopin.peloton.groupcom.GroupComAppServerHelper;
import com.kopin.peloton.groupcom.SessionInfo;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes64.dex */
public class GroupCom {
    private static final int MAX_NUM_SUBSCRIBERS = 15;
    private static final String OPEN_TOK_API_KEY = "45869532";
    private static Context sContext;
    private static ChatGroup sCurrentChatGroup;
    private static Handler sHandlerChatTime;
    private static Publisher sPublisher;
    private static Session sSession;
    private static SessionInfo sSessionInfo;
    private static String sUserName;
    private static final String LOG_TAG = GroupCom.class.getSimpleName();
    private static HashMap<Stream, Subscriber> sSubscriberStreams = new HashMap<>();
    private static List<GroupComListener> sGroupComListeners = new ArrayList();
    private static List<ChatGroup> sChatGroups = new ArrayList();
    private static long timeStampGroupFetch = 0;
    private static boolean isGroupFetchInProgress = false;
    private static long sSessionCreatedTime = 0;
    private static long MIN_TIME_INTERVAL_GROUP_FETCH = 30000;
    private static final GroupComAppServerHelper.JoinSessionResponse sSessionTokenResponse = new GroupComAppServerHelper.JoinSessionResponse() { // from class: com.kopin.groupcom.GroupCom.1
        @Override // com.kopin.peloton.groupcom.GroupComAppServerHelper.JoinSessionResponse
        public void onJoinSessionResponse(SessionInfo sessionInfo) {
            if (GroupCom.sContext != null) {
                SessionInfo unused = GroupCom.sSessionInfo = sessionInfo;
                GroupCom.initializeSession();
                GroupCom.initializePublisher();
                return;
            }
            GroupCom.disconnectSession();
        }

        @Override // com.kopin.peloton.groupcom.GroupComAppServerHelper.AppServerResponse
        public void onFailure(Failure failure, int i, String s) {
        }
    };
    private static final GroupComAppServerHelper.FetchMyChatGroupsResponse sFetchMyChatGroupResponse = new GroupComAppServerHelper.FetchMyChatGroupsResponse() { // from class: com.kopin.groupcom.GroupCom.2
        @Override // com.kopin.peloton.groupcom.GroupComAppServerHelper.AppServerResponse
        public void onFailure(Failure failure, int i, String s) {
            Log.d(GroupCom.LOG_TAG, "FetchMyChatGroupsResponse::Failure");
            boolean unused = GroupCom.isGroupFetchInProgress = false;
        }

        @Override // com.kopin.peloton.groupcom.GroupComAppServerHelper.FetchMyChatGroupsResponse
        public void onFetchMyGroupsResponse(List<ChatGroup> list) {
            if (GroupCom.sCurrentChatGroup != null) {
                for (ChatGroup group : list) {
                    if (group.GroupId.contentEquals(GroupCom.sCurrentChatGroup.GroupId)) {
                        group.sessionState = GroupCom.sCurrentChatGroup.sessionState;
                        group.sessionTime = GroupCom.getSessionTime();
                        ChatGroup unused = GroupCom.sCurrentChatGroup = group;
                    }
                }
            }
            List unused2 = GroupCom.sChatGroups = list;
            GroupCom.sendChatGroupsChanged();
            boolean unused3 = GroupCom.isGroupFetchInProgress = false;
        }
    };
    private static final SessionListener sSessionListener = new SessionListener();
    private static final PublisherKit.PublisherListener sPublisherListener = new PublisherKit.PublisherListener() { // from class: com.kopin.groupcom.GroupCom.3
        @Override // com.opentok.android.PublisherKit.PublisherListener
        public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
            Log.i(GroupCom.LOG_TAG, "Publisher Stream Created");
        }

        @Override // com.opentok.android.PublisherKit.PublisherListener
        public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
            Log.i(GroupCom.LOG_TAG, "Publisher Stream Destroyed");
        }

        @Override // com.opentok.android.PublisherKit.PublisherListener
        public void onError(PublisherKit publisherKit, OpentokError opentokError) {
            GroupCom.handleOpentokError(opentokError);
        }
    };
    private static Runnable sTimerTask = new Runnable() { // from class: com.kopin.groupcom.GroupCom.4
        @Override // java.lang.Runnable
        public void run() {
            try {
                GroupCom.sendSessionTime();
            } finally {
                if (GroupCom.sHandlerChatTime != null) {
                    GroupCom.sHandlerChatTime.postDelayed(GroupCom.sTimerTask, 1000L);
                }
            }
        }
    };

    public interface GroupComListener {
        void onChatGroupsChanged(List<ChatGroup> list);

        void onGroupComError(GroupComError groupComError);

        void onSessionStateChange(ChatGroup chatGroup);

        void onSessionTimerTick(ChatGroup chatGroup);

        void onSubscriberConnected(String str, int i);

        void onSubscriberDropped(String str, int i);
    }

    public enum GroupComError {
        NoNetwork,
        OpentokError;

        String errorMessage;

        public String getErrorMessage() {
            return this.errorMessage;
        }
    }

    public static void init(Context context) {
        sContext = context;
        sHandlerChatTime = new Handler(Looper.getMainLooper());
    }

    public static void fetchMyChatGroups() {
        long currentTimestamp = System.currentTimeMillis();
        if (!isGroupFetchInProgress) {
            Log.d(LOG_TAG, "Server fetchMyChatGroups");
            isGroupFetchInProgress = true;
            timeStampGroupFetch = currentTimestamp;
            GroupComAppServerHelper.fetchMyChatGroups(sFetchMyChatGroupResponse);
            return;
        }
        sendChatGroupsChanged();
    }

    public static void connectSession(ChatGroup chatGroup, String userName) {
        sCurrentChatGroup = chatGroup;
        sCurrentChatGroup.sessionState = ChatGroup.SessionState.CONNECTING;
        sUserName = userName;
        sendSessionStateChanged();
        GroupComAppServerHelper.joinChatSession(sCurrentChatGroup.GroupId, sSessionTokenResponse);
    }

    public static void disconnectSession() {
        Log.d(LOG_TAG, "Disconnection Requested");
        stopChatTimer();
        if (sSession != null) {
            if (sSubscriberStreams.size() > 0) {
                for (Subscriber subscriber : sSubscriberStreams.values()) {
                    if (subscriber != null) {
                        sSession.unsubscribe(subscriber);
                        subscriber.destroy();
                    }
                }
                sSubscriberStreams.clear();
            }
            if (sPublisher != null) {
                sSession.unpublish(sPublisher);
                sPublisher.destroy();
                sPublisher = null;
            }
            sSession.disconnect();
        }
    }

    public static void addListener(GroupComListener listener) {
        sGroupComListeners.add(listener);
    }

    public static void removeListener(GroupComListener listener) {
        sGroupComListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void deinit() {
        sSession = null;
        sCurrentChatGroup = null;
        sUserName = null;
    }

    public static boolean isSpeaking() {
        if (sPublisher != null) {
            return sPublisher.getPublishAudio();
        }
        return false;
    }

    public static void startSpeaking() {
        if (sPublisher != null) {
            sPublisher.setPublishAudio(true);
        }
    }

    public static void stopSpeaking() {
        if (sPublisher != null) {
            sPublisher.setPublishAudio(false);
        }
    }

    public static void toggleSpeaking() {
        if (sPublisher != null) {
            boolean isSpeaking = sPublisher.getPublishAudio();
            sPublisher.setPublishAudio(!isSpeaking);
        }
    }

    public static boolean isInSession() {
        return sSession != null;
    }

    public static int getOtherParticipantCount() {
        return sSubscriberStreams.size();
    }

    public static ChatGroup getCurrentChatGroup() {
        return sCurrentChatGroup;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendChatGroupsChanged() {
        if (sGroupComListeners != null) {
            for (GroupComListener listener : sGroupComListeners) {
                listener.onChatGroupsChanged(sChatGroups);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendSessionStateChanged() {
        if (sGroupComListeners != null) {
            for (GroupComListener listener : sGroupComListeners) {
                listener.onSessionStateChange(sCurrentChatGroup);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendSessionTime() {
        if (sGroupComListeners != null) {
            for (GroupComListener listener : sGroupComListeners) {
                sCurrentChatGroup.sessionTime = getSessionTime();
                listener.onSessionTimerTick(sCurrentChatGroup);
            }
        }
    }

    private static void sendError(GroupComError error) {
        if (sGroupComListeners != null) {
            for (GroupComListener listener : sGroupComListeners) {
                listener.onGroupComError(error);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initializeSession() {
        sSession = new Session.Builder(sContext, OPEN_TOK_API_KEY, sSessionInfo.SessionId).build();
        sSession.setSessionListener(sSessionListener);
        sSession.setReconnectionListener(sSessionListener);
        sSession.connect(sSessionInfo.Token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initializePublisher() {
        Log.e(LOG_TAG, "Token: " + sSessionInfo.Token);
        sPublisher = new Publisher.Builder(sContext).name(sUserName).videoTrack(false).build();
        sPublisher.setPublisherListener(sPublisherListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleOpentokError(OpentokError opentokError) {
        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
        if (opentokError.getErrorCode() == OpentokError.ErrorCode.ConnectionDropped || opentokError.getErrorCode() == OpentokError.ErrorCode.ConnectionFailed) {
            sendError(GroupComError.NoNetwork);
            return;
        }
        GroupComError.OpentokError.errorMessage = opentokError.getMessage();
        sendError(GroupComError.OpentokError);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sContext.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static class SessionListener implements Session.ReconnectionListener, Session.SessionListener {
        private SessionListener() {
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onConnected(Session session) {
            Log.i(GroupCom.LOG_TAG, "Session Connected");
            GroupCom.startChatTimer();
            if (GroupCom.sPublisher != null) {
                GroupCom.sSession.publish(GroupCom.sPublisher);
                GroupCom.sPublisher.setPublishAudio(false);
            }
            GroupCom.sCurrentChatGroup.sessionState = ChatGroup.SessionState.CONNECTED;
            GroupCom.sendSessionStateChanged();
            Toast.makeText(GroupCom.sContext, "Session Connected", 1).show();
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onDisconnected(Session session) {
            Log.i(GroupCom.LOG_TAG, "Session Disconnected");
            GroupCom.sCurrentChatGroup.sessionState = ChatGroup.SessionState.DISCONNECTED;
            GroupCom.sendSessionStateChanged();
            GroupCom.deinit();
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onStreamReceived(Session session, Stream stream) {
            Log.i(GroupCom.LOG_TAG, "Stream Received" + stream.getStreamId() + " in session " + session.getSessionId());
            if (GroupCom.sSubscriberStreams.size() + 1 > 15) {
                Toast.makeText(GroupCom.sContext, "New subscriber ignored. MAX_NUM_SUBSCRIBERS limit reached.", 1).show();
                return;
            }
            Subscriber subscriber = new Subscriber.Builder(GroupCom.sContext, stream).build();
            GroupCom.sSession.subscribe(subscriber);
            GroupCom.sSubscriberStreams.put(stream, subscriber);
            for (GroupComListener listener : GroupCom.sGroupComListeners) {
                listener.onSubscriberConnected(stream.getName(), GroupCom.sSubscriberStreams.size());
            }
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onStreamDropped(Session session, Stream stream) {
            Log.i(GroupCom.LOG_TAG, "Stream Dropped" + stream.getStreamId() + " in session " + session.getSessionId());
            Subscriber subscriber = (Subscriber) GroupCom.sSubscriberStreams.get(stream);
            if (subscriber != null) {
                GroupCom.sSubscriberStreams.remove(stream);
                for (GroupComListener listener : GroupCom.sGroupComListeners) {
                    listener.onSubscriberDropped(stream.getName(), GroupCom.sSubscriberStreams.size());
                }
            }
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onError(Session session, OpentokError opentokError) {
            GroupCom.handleOpentokError(opentokError);
        }

        @Override // com.opentok.android.Session.ReconnectionListener
        public void onReconnecting(Session session) {
            if (GroupCom.sSession != null && GroupCom.sCurrentChatGroup != null) {
                GroupCom.sCurrentChatGroup.sessionState = ChatGroup.SessionState.RECONNECTING;
                GroupCom.sendSessionStateChanged();
            }
        }

        @Override // com.opentok.android.Session.ReconnectionListener
        public void onReconnected(Session session) {
            if (GroupCom.sSession != null && GroupCom.sCurrentChatGroup != null) {
                GroupCom.sCurrentChatGroup.sessionState = ChatGroup.SessionState.CONNECTED;
                GroupCom.sendSessionStateChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startChatTimer() {
        sSessionCreatedTime = System.currentTimeMillis();
        sTimerTask.run();
    }

    private static void stopChatTimer() {
        sSessionCreatedTime = 0L;
        if (sHandlerChatTime != null) {
            sHandlerChatTime.removeCallbacks(sTimerTask);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getSessionTime() {
        if (sSession == null || sSession.getConnection() == null || sSessionCreatedTime <= 0) {
            return 0L;
        }
        return System.currentTimeMillis() - sSessionCreatedTime;
    }
}
