package com.facebook.appevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: loaded from: classes41.dex */
class AppEventQueue {
    private static final int FLUSH_PERIOD_IN_SECONDS = 15;
    private static final int NUM_LOG_EVENTS_TO_TRY_TO_FLUSH_AFTER = 100;
    private static ScheduledFuture scheduledFuture;
    private static final String TAG = AppEventQueue.class.getName();
    private static volatile AppEventCollection appEventCollection = new AppEventCollection();
    private static final ScheduledExecutorService singleThreadExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final Runnable flushRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventQueue.1
        @Override // java.lang.Runnable
        public void run() {
            ScheduledFuture unused = AppEventQueue.scheduledFuture = null;
            if (AppEventsLogger.getFlushBehavior() != AppEventsLogger.FlushBehavior.EXPLICIT_ONLY) {
                AppEventQueue.flushAndWait(FlushReason.TIMER);
            }
        }
    };

    AppEventQueue() {
    }

    public static void persistToDisk() {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.2
            @Override // java.lang.Runnable
            public void run() {
                AppEventStore.persistEvents(AppEventQueue.appEventCollection);
                AppEventCollection unused = AppEventQueue.appEventCollection = new AppEventCollection();
            }
        });
    }

    public static void flush(final FlushReason reason) {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.3
            @Override // java.lang.Runnable
            public void run() {
                AppEventQueue.flushAndWait(reason);
            }
        });
    }

    public static void add(final AccessTokenAppIdPair accessTokenAppId, final AppEvent appEvent) {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.4
            @Override // java.lang.Runnable
            public void run() {
                AppEventQueue.appEventCollection.addEvent(accessTokenAppId, appEvent);
                if (AppEventsLogger.getFlushBehavior() == AppEventsLogger.FlushBehavior.EXPLICIT_ONLY || AppEventQueue.appEventCollection.getEventCount() <= 100) {
                    if (AppEventQueue.scheduledFuture == null) {
                        ScheduledFuture unused = AppEventQueue.scheduledFuture = AppEventQueue.singleThreadExecutor.schedule(AppEventQueue.flushRunnable, 15L, TimeUnit.SECONDS);
                        return;
                    }
                    return;
                }
                AppEventQueue.flushAndWait(FlushReason.EVENT_THRESHOLD);
            }
        });
    }

    public static Set<AccessTokenAppIdPair> getKeySet() {
        return appEventCollection.keySet();
    }

    static void flushAndWait(FlushReason reason) {
        PersistedEvents result = AppEventStore.readAndClearStore();
        appEventCollection.addPersistedEvents(result);
        try {
            FlushStatistics flushResults = sendEventsToServer(reason, appEventCollection);
            if (flushResults != null) {
                Intent intent = new Intent(AppEventsLogger.ACTION_APP_EVENTS_FLUSHED);
                intent.putExtra(AppEventsLogger.APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED, flushResults.numEvents);
                intent.putExtra(AppEventsLogger.APP_EVENTS_EXTRA_FLUSH_RESULT, flushResults.result);
                Context context = FacebookSdk.getApplicationContext();
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        } catch (Exception e) {
            Log.w(TAG, "Caught unexpected exception while flushing app events: ", e);
        }
    }

    private static FlushStatistics sendEventsToServer(FlushReason reason, AppEventCollection appEventCollection2) {
        FlushStatistics flushResults = new FlushStatistics();
        Context context = FacebookSdk.getApplicationContext();
        boolean limitEventUsage = FacebookSdk.getLimitEventAndDataUsage(context);
        List<GraphRequest> requestsToExecute = new ArrayList<>();
        for (AccessTokenAppIdPair accessTokenAppId : appEventCollection2.keySet()) {
            GraphRequest request = buildRequestForSession(accessTokenAppId, appEventCollection2.get(accessTokenAppId), limitEventUsage, flushResults);
            if (request != null) {
                requestsToExecute.add(request);
            }
        }
        if (requestsToExecute.size() > 0) {
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flushing %d events due to %s.", Integer.valueOf(flushResults.numEvents), reason.toString());
            Iterator<GraphRequest> it = requestsToExecute.iterator();
            while (it.hasNext()) {
                it.next().executeAndWait();
            }
            return flushResults;
        }
        return null;
    }

    private static GraphRequest buildRequestForSession(final AccessTokenAppIdPair accessTokenAppId, final SessionEventsState appEvents, boolean limitEventUsage, final FlushStatistics flushState) {
        int numEvents;
        String applicationId = accessTokenAppId.getApplicationId();
        Utility.FetchedAppSettings fetchedAppSettings = Utility.queryAppSettings(applicationId, false);
        final GraphRequest postRequest = GraphRequest.newPostRequest(null, String.format("%s/activities", applicationId), null, null);
        Bundle requestParameters = postRequest.getParameters();
        if (requestParameters == null) {
            requestParameters = new Bundle();
        }
        requestParameters.putString("access_token", accessTokenAppId.getAccessTokenString());
        String pushNotificationsRegistrationId = AppEventsLogger.getPushNotificationsRegistrationId();
        if (pushNotificationsRegistrationId != null) {
            requestParameters.putString("device_token", pushNotificationsRegistrationId);
        }
        postRequest.setParameters(requestParameters);
        if (fetchedAppSettings != null && (numEvents = appEvents.populateRequest(postRequest, FacebookSdk.getApplicationContext(), fetchedAppSettings.supportsImplicitLogging(), limitEventUsage)) != 0) {
            flushState.numEvents += numEvents;
            postRequest.setCallback(new GraphRequest.Callback() { // from class: com.facebook.appevents.AppEventQueue.5
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) {
                    AppEventQueue.handleResponse(accessTokenAppId, postRequest, response, appEvents, flushState);
                }
            });
            return postRequest;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleResponse(final AccessTokenAppIdPair accessTokenAppId, GraphRequest request, GraphResponse response, final SessionEventsState appEvents, FlushStatistics flushState) {
        String prettyPrintedEvents;
        FacebookRequestError error = response.getError();
        String resultDescription = "Success";
        FlushResult flushResult = FlushResult.SUCCESS;
        if (error != null) {
            if (error.getErrorCode() == -1) {
                resultDescription = "Failed: No Connectivity";
                flushResult = FlushResult.NO_CONNECTIVITY;
            } else {
                resultDescription = String.format("Failed:\n  Response: %s\n  Error %s", response.toString(), error.toString());
                flushResult = FlushResult.SERVER_ERROR;
            }
        }
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.APP_EVENTS)) {
            String eventsJsonString = (String) request.getTag();
            try {
                JSONArray jsonArray = new JSONArray(eventsJsonString);
                prettyPrintedEvents = jsonArray.toString(2);
            } catch (JSONException e) {
                prettyPrintedEvents = "<Can't encode events for debug logging>";
            }
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flush completed\nParams: %s\n  Result: %s\n  Events JSON: %s", request.getGraphObject().toString(), resultDescription, prettyPrintedEvents);
        }
        appEvents.clearInFlightAndStats(error != null);
        if (flushResult == FlushResult.NO_CONNECTIVITY) {
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.6
                @Override // java.lang.Runnable
                public void run() {
                    AppEventStore.persistEvents(accessTokenAppId, appEvents);
                }
            });
        }
        if (flushResult != FlushResult.SUCCESS && flushState.result != FlushResult.NO_CONNECTIVITY) {
            flushState.result = flushResult;
        }
    }
}
