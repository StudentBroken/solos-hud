package com.kopin.pupil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import com.kopin.accessory.AudioCodec;
import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketReceivedListener;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.ActionType;
import com.kopin.accessory.packets.ButtonType;
import com.kopin.accessory.packets.base.IntPacketContent;
import com.kopin.pupil.PupilConnector;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.VCApplication;
import com.kopin.pupil.bluetooth.BluetoothConnector;
import com.kopin.pupil.bluetooth.DeviceLister;
import com.kopin.pupil.diags.Diagnostics;
import com.kopin.pupil.exception.NotFoundException;
import com.kopin.pupil.exception.XmlParserException;
import com.kopin.pupil.pagerenderer.GlassTheme;
import com.kopin.pupil.pagerenderer.PageRenderer;
import com.kopin.pupil.pagerenderer.Theme;
import com.kopin.pupil.remote.RemoteConnector;
import com.kopin.pupil.tts.IAudioCallback;
import com.kopin.pupil.tts.TTS;
import com.kopin.pupil.ui.BasePage;
import com.kopin.pupil.ui.PageHelper;
import com.kopin.pupil.ui.PageParser;
import com.kopin.pupil.ui.VCNotification;
import com.kopin.pupil.util.Action;
import com.kopin.pupil.util.MutableInt;
import com.kopin.pupil.util.XmlParserPackage;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes25.dex */
public class ConnectionManager {
    public static final String HEADSET_PAIRED = "headset_paired";
    private static final long NOTIFICATION_DISPLAY_TIME = 30000;
    public static final int NOTIFICATION_TTS = 8;
    private static final long TOAST_DISPLAY_TIME = 4000;
    private VCApplication mApplication;
    private ConnectionManagerListener mConnectionManagerListener;
    private BasePage mCurrentPage;
    private DeviceLister mDeviceLister;
    private VCNotification mNotification;
    private PageRenderer mPageRenderer;
    private Resolver mResolver;
    private ScreenWatcher mScreenWatcher;
    private boolean mSyncOutstanding;
    private TTS mTTS;
    private Theme mTheme;
    private VCContext mVCContext;
    private int mLastFrameId = 256;
    private int mSyncId = 0;
    private boolean mIsConnected = false;
    private TreeSet<VCNotification> mNotifications = new TreeSet<>();
    private HashMap<String, BasePage> mPageCache = new HashMap<>();
    private final PacketReceivedListener mUISyncListener = new PacketReceivedListener() { // from class: com.kopin.pupil.ConnectionManager.3
        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            int frameId = ((IntPacketContent) packet.getContent()).getValue();
            Log.d("ConnectionManager", "Frame drawn, UI Sync ID: " + frameId);
            if (frameId != 0) {
                if (ConnectionManager.this.mSyncId != frameId) {
                    Log.e("ConnectionManager", " ID doesn't match expected: " + ConnectionManager.this.mSyncId);
                    return;
                }
            } else {
                Log.d("ConnectionManager", " Received sync reset ID");
            }
            ConnectionManager.this.mSyncId = 0;
            if (ConnectionManager.this.mSyncOutstanding) {
                ConnectionManager.this.mSyncOutstanding = false;
                ConnectionManager.this.refresh(false);
            }
        }
    };
    private DeviceLister.ConnectionListener mConnectionListener = new DeviceLister.ConnectionListener() { // from class: com.kopin.pupil.ConnectionManager.4
        @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
        public void onAttemptConnection(BluetoothDevice device) {
        }

        @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
        public void onConnection(PupilConnector connector) {
            if (connector instanceof BluetoothConnector) {
                ConnectionManager.this.storeHeadset(((BluetoothConnector) connector).getDevice());
            }
            PupilDevice.connected(connector);
            PupilDevice.setResponseListener(ConnectionManager.this.mResponseListener);
            ConnectionManager.this.onConnection();
            connector.listenForPackets(PacketType.IMAGE_SYNC, ConnectionManager.this.mUISyncListener);
        }

        @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
        public void onConnectionFailed() {
        }
    };
    private DeviceLister.ConnectionListener mMirrorConnectionListener = new DeviceLister.ConnectionListener() { // from class: com.kopin.pupil.ConnectionManager.5
        @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
        public void onAttemptConnection(BluetoothDevice device) {
        }

        @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
        public void onConnection(PupilConnector connector) {
            PupilDevice.connectedMirror(connector);
        }

        @Override // com.kopin.pupil.bluetooth.DeviceLister.ConnectionListener
        public void onConnectionFailed() {
        }
    };
    private PupilConnector.Solos2ResponseListener mResponseListener = new PupilConnector.Solos2ResponseListener() { // from class: com.kopin.pupil.ConnectionManager.6
        @Override // com.kopin.pupil.PupilConnector.BaseResponseListener
        public void onDeviceInfo(PupilDevice.DeviceInfo devInfo) {
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onInfoChanged(devInfo);
            }
        }

        @Override // com.kopin.pupil.PupilConnector.BaseResponseListener
        public void onDeviceStatus(PupilDevice.DeviceStatus deviceStatus) {
            Diagnostics.onDeviceStatusChanged(deviceStatus);
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onStatusChanged(deviceStatus);
            }
        }

        @Override // com.kopin.pupil.PupilConnector.ResponseListener
        public void onButtonPress(boolean isShort) {
            ConnectionManager.this.sendButtonPress(isShort, ButtonType.MAIN);
        }

        @Override // com.kopin.pupil.PupilConnector.SolosResponseListener
        public void onButtonPress(boolean isShort, ButtonType button) {
            ConnectionManager.this.sendButtonPress(isShort, button);
        }

        @Override // com.kopin.pupil.PupilConnector.BaseResponseListener
        public void onDisconnected(boolean wasLoss) {
            ConnectionManager.this.mIsConnected = false;
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onExit();
                ConnectionManager.this.mApplication.onStateChanged(VCApplication.ConnectionListener.ConnectionState.DISCONNECTED);
                ConnectionManager.this.mApplication.onDisconnected();
            }
            PupilDevice.unregisterForPackets(PacketType.IMAGE_SYNC, ConnectionManager.this.mUISyncListener);
        }

        @Override // com.kopin.pupil.PupilConnector.BaseResponseListener
        public void onReconnection() {
            ConnectionManager.this.onConnection();
        }

        @Override // com.kopin.pupil.PupilConnector.ResponseListener
        public void onAudioEnd(int id) {
            if (id != ActionType.PLAY_CHIRP.getValue() && id != ActionType.PLAY_LISTENING_CHIME.getValue() && id != ActionType.PLAY_PROCESSING_CHIME.getValue()) {
                if (ConnectionManager.this.mApplication != null) {
                    ConnectionManager.this.mApplication.onTTSComplete(id);
                    return;
                }
                return;
            }
            Log.d("AudioEnd", "Ignoring AudioEnd for chirp: " + id);
        }

        @Override // com.kopin.pupil.PupilConnector.ResponseListener
        public void onAudioReceived(byte[] data) {
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onAudioReceived(data);
            }
        }

        @Override // com.kopin.pupil.PupilConnector.Solos2ResponseListener
        public void onTapEvent() {
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onTapEvent();
            }
        }

        @Override // com.kopin.pupil.PupilConnector.Solos2ResponseListener
        public void onVoiceDetected() {
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onVoiceDetected();
            }
        }

        @Override // com.kopin.pupil.PupilConnector.Solos2ResponseListener
        public void onSilenceDetected() {
            if (ConnectionManager.this.mApplication != null) {
                ConnectionManager.this.mApplication.onSilenceDetected();
            }
        }
    };
    private Handler mNotificationHandler = new Handler(Looper.getMainLooper());

    public interface ConnectionManagerListener {
        void onGarminRemoteButtonPress(int i);
    }

    public interface ScreenWatcher {
        void onScreenUpdated(Bitmap bitmap, Rect rect);
    }

    public ConnectionManager(Context context) {
        this.mTheme = new GlassTheme(context);
        this.mVCContext = new VCContext(this, context);
        this.mResolver = new Resolver(context);
        this.mPageRenderer = new PageRenderer(428, 240, context);
    }

    public boolean reconnectToLastHeadset(VCApplication app) {
        this.mApplication = app;
        String mac = PreferenceManager.getDefaultSharedPreferences(app).getString(HEADSET_PAIRED, null);
        if (mac != null) {
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
            if (device != null) {
                PupilDevice.tryConnect(device, this.mConnectionListener, true);
                Log.d("reconnectToLastHeadset", "attempting to reconnect to previous headset: " + mac);
                return true;
            }
            Log.e("reconnectToLastHeadset", "couldn't create BT device for previous headset");
        }
        return false;
    }

    public void disconnect(boolean andForget) {
        PupilDevice.disconnect();
    }

    public void connectToMirrorService(InetAddress addr) {
        new RemoteConnector(addr, this.mMirrorConnectionListener).start();
    }

    void initTTS() {
        if (this.mTTS != null) {
            Log.d("initTTS", "TTS already initialised");
            return;
        }
        try {
            this.mTTS = new TTS(this.mVCContext.getContext());
            this.mTTS.setAudioCallback(new IAudioCallback() { // from class: com.kopin.pupil.ConnectionManager.1
                @Override // com.kopin.pupil.tts.IAudioCallback
                public void onAudioStart(int id) {
                }

                @Override // com.kopin.pupil.tts.IAudioCallback
                public void onAudioData(byte[] data, int length, AudioCodec codec, int sampleRate) {
                    PupilDevice.sendAudio(data, length, codec, sampleRate);
                }

                @Override // com.kopin.pupil.tts.IAudioCallback
                public void onAudioEnd(int endID) {
                    PupilDevice.sendAudioEnd(endID);
                }
            });
        } catch (UnsatisfiedLinkError e) {
            Log.e("initTTS", "Couldn't initialise TTS engine", e);
        }
    }

    boolean hasTTS() {
        return this.mTTS != null;
    }

    public void setConnectionListener(ConnectionManagerListener connectionManagerListener) {
        this.mConnectionManagerListener = connectionManagerListener;
    }

    public void showConnectionDialog(Activity activity, VCApplication application) {
        PupilDevice.disconnect();
        this.mApplication = application;
        if (this.mDeviceLister != null) {
            this.mDeviceLister.dismiss();
            this.mDeviceLister = null;
        }
        this.mDeviceLister = new DeviceLister(activity, this.mConnectionListener);
        this.mDeviceLister.show();
    }

    public void initiateConnection(VCApplication application) {
        PupilDevice.disconnect();
        this.mApplication = application;
    }

    public boolean isConnected() {
        return PupilDevice.isConnected() || PupilDevice.isMirrorConnected();
    }

    public boolean isReconnecting() {
        return PupilDevice.isReconnecting();
    }

    protected void bitmapChanged(String id) {
        if (this.mCurrentPage != null) {
            this.mCurrentPage.bitmapChanged(id);
        }
    }

    void clearPageFromCache(String id) {
        synchronized (this.mPageCache) {
            this.mPageCache.remove(id);
        }
    }

    void resetPageCache() {
        synchronized (this.mPageCache) {
            this.mPageCache.clear();
        }
    }

    public void gotoPage(String templateID, String pageID, Bundle extra) {
        Action action = new Action();
        action.setExtra(extra);
        action.setPageID(pageID);
        action.setTemplateID(templateID);
        gotoPage(action);
    }

    public void gotoPage(String pageID, Bundle extra) {
        Action action = new Action();
        action.setExtra(extra);
        action.setPageID(pageID);
        gotoPage(action);
    }

    private void gotoPage(Action action) {
        if (this.mIsConnected) {
            String pageID = action.getPageID();
            if (pageID == null || pageID.isEmpty()) {
                Log.e("ConnectionManager", "Action contains no pageID " + pageID, new Exception("stack trace"));
                return;
            }
            if (this.mCurrentPage != null) {
                this.mCurrentPage.onStop();
            }
            synchronized (this.mPageCache) {
                this.mCurrentPage = this.mPageCache.get(pageID);
                if (this.mCurrentPage == null) {
                    XmlParserPackage parserPackage = this.mApplication.getPageXmlParser(pageID, action.getTemplateID());
                    if (parserPackage == null) {
                        Log.e("ConnectionManager", "gotoPage getPageXmlParser is null " + pageID + ", " + action.getTemplateID(), new Exception("stack trace"));
                        return;
                    }
                    try {
                        switch (parserPackage.getVersion()) {
                            case 2:
                                PageParser parser = new PageParser(this.mVCContext, this.mTheme, this.mResolver);
                                this.mCurrentPage = parser.parse(parserPackage.getParser(), this.mVCContext.getContext());
                                break;
                            default:
                                this.mCurrentPage = PageHelper.createPageFromXml(parserPackage.getParser(), this.mVCContext, this.mTheme, this.mResolver);
                                break;
                        }
                        String id = (action.getTemplateID() == null || action.getTemplateID().isEmpty()) ? pageID : action.getTemplateID();
                        this.mPageCache.put(id, this.mCurrentPage);
                    } catch (XmlParserException parserException) {
                        Log.e("", "", parserException);
                        if (this.mApplication != null) {
                            this.mApplication.throwXmlException(parserException);
                        }
                    } catch (Exception e) {
                        Log.e("", "", e);
                    }
                    if (parserPackage.getPageName() != null) {
                        this.mCurrentPage.setPageName(parserPackage.getPageName());
                        this.mCurrentPage.setTemplate(parserPackage.getTemplate());
                    }
                }
                action.setType(Action.Type.CHANGING_PAGE);
                this.mApplication.onAction(action);
                this.mCurrentPage.onStart();
                Action finalAction = new Action(action);
                finalAction.setType(Action.Type.CHANGED_PAGE);
                this.mApplication.onAction(finalAction);
                if (this.mSyncOutstanding && this.mSyncId != 0) {
                    Log.d("ConnectionManager", "Page changed, sync outstanding so resending, UI Sync ID: " + this.mSyncId);
                    PupilDevice.sendPacket(new Packet(PacketType.IMAGE_SYNC, new IntPacketContent(this.mSyncId)));
                }
                this.mCurrentPage.markReady(true, this.mSyncOutstanding);
            }
        }
    }

    public void sendDropDownNotification(Bitmap icon, Bundle extra, int duration, boolean ttsOn, Runnable runnable, VCNotification.Priority priority, VCNotification.TextPart... parts) {
        VCNotification notification = new VCNotification(VCNotification.VCNotificationType.DROP_DOWN, icon, parts);
        notification.setBundle(extra);
        notification.setRunnableAction(runnable);
        notification.setDuration(duration);
        notification.setPriority(priority);
        String appName = extra.getString("name");
        if (this.mNotification != null) {
            if (notification.getPriority().compareTo(this.mNotification.getPriority()) >= 1) {
                clearNotification();
                this.mNotification = notification;
                startNotificationTimer(notification.getDuration());
                refresh(true);
                speakNotification(ttsOn, appName, parts);
                return;
            }
            if (notification.getPriority().equals(this.mNotification.getPriority()) && this.mNotification.getDuration() <= 0) {
                clearNotification();
                this.mNotification = notification;
                startNotificationTimer(notification.getDuration());
                refresh(true);
                speakNotification(ttsOn, appName, parts);
                return;
            }
            this.mNotifications.add(notification);
            return;
        }
        clearNotification();
        this.mNotifications.add(notification);
        this.mNotification = this.mNotifications.pollLast();
        startNotificationTimer(this.mNotification.getDuration());
        refresh(true);
        speakNotification(ttsOn, appName, parts);
    }

    private void startNotificationTimer(long duration) {
        if (duration > 0) {
            this.mNotificationHandler.postDelayed(new Runnable() { // from class: com.kopin.pupil.ConnectionManager.2
                @Override // java.lang.Runnable
                public void run() {
                    ConnectionManager.this.getNextNotification();
                }
            }, duration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getNextNotification() {
        clearNotification();
        if (!this.mNotifications.isEmpty()) {
            this.mNotification = this.mNotifications.pollLast();
            if (this.mNotification != null) {
                startNotificationTimer(this.mNotification.getDuration());
            }
        }
        refresh(true);
    }

    protected void clearNotification() {
        this.mNotificationHandler.removeCallbacksAndMessages(null);
        this.mNotification = null;
    }

    public void sendButtonPress(boolean isShort, ButtonType button) {
        if (this.mApplication != null) {
            if (!this.mApplication.onButtonPress(button, !isShort)) {
                Action action = new Action();
                action.setType(isShort ? Action.Type.BUTTON_PRESS_SHORT : Action.Type.BUTTON_PRESS_LONG);
                if (this.mCurrentPage != null) {
                    action.setPageID(this.mCurrentPage.getPageName());
                    action.setTemplateID(this.mCurrentPage.getTemplate());
                }
                this.mApplication.onAction(action);
            }
        }
    }

    public boolean hasNotification() {
        return this.mNotification != null;
    }

    public VCNotification.VCNotificationType getNotificationType() {
        if (this.mNotification != null) {
            return this.mNotification.getNotificationType();
        }
        return null;
    }

    public boolean onAcceptNotification() {
        if (this.mNotification != null) {
            switch (this.mNotification.getNotificationType()) {
                case ACTIONABLE:
                    gotoPage(this.mNotification.getAction());
                    onDismissNotification();
                    break;
                case DROP_DOWN:
                    Runnable runnable = this.mNotification.getRunnableAction();
                    if (runnable != null) {
                        runnable.run();
                        break;
                    }
                default:
                    onDismissNotification();
                    break;
            }
            return true;
        }
        return false;
    }

    public void onDismissNotification() {
        clearNotification();
        getNextNotification();
    }

    public List<VCNotification.TextPart> getBrakedMessage(VCNotification.TextPart message) {
        return this.mPageRenderer.getBrakedMessage(message, this.mTheme);
    }

    public Bundle getElementProperties(String pageID, String elementID) {
        if (isCurrentPage(pageID)) {
            return this.mCurrentPage.getElementProperties(elementID);
        }
        return null;
    }

    public void updateElement(String templateID, String pageID, String elementID, String attribute, Object value, boolean refresh) {
        if (isCurrentPage(pageID) || (templateID != null && isCurrentPage(templateID))) {
            this.mCurrentPage.updateElement(elementID, attribute, value, refresh);
        }
    }

    public void updateElements(String pageID) {
        if (isCurrentPage(pageID)) {
            this.mCurrentPage.updateElements();
        }
    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean force) {
        if (!this.mSyncOutstanding) {
            if (this.mSyncId != 0) {
                this.mSyncOutstanding = true;
                return;
            }
            if (force) {
                this.mPageRenderer.reset(false);
            }
            if (this.mPageRenderer.renderPage(this.mCurrentPage, this.mTheme, this.mNotification)) {
                Rect rect = new Rect();
                MutableInt length = new MutableInt();
                if (PupilDevice.isLowColorMode()) {
                    byte[] data = this.mPageRenderer.getBytesRGB111(rect, length, false);
                    if (rect.width() != 0 && rect.height() != 0) {
                        PupilDevice.sendRGB111((short) rect.left, (short) rect.top, (short) rect.width(), (short) rect.height(), data, length.getValue());
                    }
                } else {
                    byte[] data2 = this.mPageRenderer.getBytesRLE565(rect, length, false);
                    if (rect.width() != 0 && rect.height() != 0) {
                        Log.d("ConnectionManager", "updateElements refresh: " + rect.left + "x" + rect.top + " - " + rect.width() + "x" + rect.height() + " force:" + force);
                        PupilDevice.sendRLE565((short) rect.left, (short) rect.top, (short) rect.width(), (short) rect.height(), data2, length.getValue());
                    }
                }
                Log.d("ConnectionManager", "Frame sent, UI Sync ID: " + this.mLastFrameId);
                this.mSyncId = this.mLastFrameId;
                PacketType packetType = PacketType.IMAGE_SYNC;
                int i = this.mLastFrameId;
                this.mLastFrameId = i + 1;
                PupilDevice.sendPacket(new Packet(packetType, new IntPacketContent(i)));
                if (this.mScreenWatcher != null) {
                    this.mScreenWatcher.onScreenUpdated(this.mPageRenderer.getBitmap(), rect);
                }
            }
        }
    }

    void setScreenWatcher(ScreenWatcher watcher) {
        this.mScreenWatcher = watcher;
        if (this.mScreenWatcher != null) {
            this.mScreenWatcher.onScreenUpdated(this.mPageRenderer.getBitmap(), null);
        }
    }

    public void speakText(int id, String text) {
        if (this.mTTS != null) {
            this.mTTS.speak(id, text);
        }
    }

    public void speakNotification(boolean ttsNotificationsOn, String appName, VCNotification.TextPart... parts) {
        if (ttsNotificationsOn) {
            StringBuffer buffer = new StringBuffer("Notification ");
            if (appName != null && !appName.isEmpty()) {
                buffer.append(" from " + appName + " , ");
            }
            for (VCNotification.TextPart textPart : parts) {
                buffer.append(textPart.getText());
            }
            speakText(8, buffer.toString());
        }
    }

    public void stopTTS() {
        PupilDevice.stopAudioSending();
        if (this.mTTS != null) {
            this.mTTS.stop();
        }
    }

    public boolean isCurrentPage(String pageID) {
        return this.mCurrentPage != null && (pageID.equals(this.mCurrentPage.getPageName()) || pageID.equals(this.mCurrentPage.getTemplate()));
    }

    public Bitmap getImageFromCache(String id) {
        if (this.mApplication != null) {
            return this.mApplication.getBitmap(id);
        }
        return null;
    }

    public void sendAnswerCall() {
        PupilDevice.sendAnswerCallPacket();
    }

    public void sendGotoSleep() {
        PupilDevice.gotoSleep();
    }

    public void sendWakeUp() {
        PupilDevice.wakeUp();
    }

    public void sendEndCall() {
        PupilDevice.sendEndCallPacket();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeHeadset(BluetoothDevice device) {
        if (this.mApplication != null && device != null) {
            PreferenceManager.getDefaultSharedPreferences(this.mApplication).edit().putString(HEADSET_PAIRED, device.getAddress()).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnection() {
        PupilDevice.requestStatus();
        this.mSyncOutstanding = false;
        this.mSyncId = 0;
        if (this.mApplication != null) {
            this.mApplication.onConnected();
            this.mApplication.onStateChanged(VCApplication.ConnectionListener.ConnectionState.CONNECTED);
            this.mApplication.onEnter();
        } else {
            Log.e("ConnectionManager", "onConnection called with no Application set!");
        }
        this.mIsConnected = true;
        if (this.mDeviceLister != null) {
            this.mDeviceLister.dismiss();
            this.mDeviceLister = null;
        }
    }

    public DeviceLister.ConnectionListener getConnectionListener() {
        return this.mConnectionListener;
    }

    private static class Resolver implements PageHelper.IResolver {
        private Context mContext;

        public Resolver(Context context) {
            this.mContext = context;
        }

        @Override // com.kopin.pupil.ui.PageHelper.IResolver
        public String resolveString(String id) throws NotFoundException {
            if (id.length() <= 1) {
                throw new NotFoundException();
            }
            Resources resources = this.mContext.getResources();
            try {
                String cleanID = id.substring(1);
                int intID = resources.getIdentifier(cleanID, null, this.mContext.getPackageName());
                if (intID == 0) {
                    throw new NotFoundException();
                }
                return resources.getString(intID);
            } catch (Resources.NotFoundException e) {
                throw new NotFoundException(e.getLocalizedMessage());
            }
        }

        @Override // com.kopin.pupil.ui.PageHelper.IResolver
        public int resolveColour(String id) throws NotFoundException {
            if (id.length() <= 1) {
                throw new NotFoundException();
            }
            Resources resources = this.mContext.getResources();
            try {
                String cleanID = id.substring(1);
                int intID = resources.getIdentifier(cleanID, null, this.mContext.getPackageName());
                if (intID == 0) {
                    throw new NotFoundException();
                }
                return resources.getColor(intID);
            } catch (Resources.NotFoundException e) {
                throw new NotFoundException(e.getLocalizedMessage());
            }
        }

        @Override // com.kopin.pupil.ui.PageHelper.IResolver
        public float resolveDimen(String id) throws NotFoundException {
            Resources resources = this.mContext.getResources();
            if (id.length() <= 1) {
                throw new NotFoundException();
            }
            try {
                String cleanID = id.substring(1);
                int intID = resources.getIdentifier(cleanID, null, this.mContext.getPackageName());
                if (intID == 0) {
                    throw new NotFoundException();
                }
                return resources.getDimension(intID);
            } catch (Resources.NotFoundException e) {
                throw new NotFoundException();
            }
        }
    }
}
