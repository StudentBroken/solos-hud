package com.facebook.appevents;

import android.content.Context;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AccessTokenAppIdPair;
import com.facebook.appevents.AppEvent;
import com.facebook.internal.Utility;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

/* JADX INFO: loaded from: classes41.dex */
class AppEventStore {
    private static final String PERSISTED_EVENTS_FILENAME = "AppEventsLogger.persistedevents";
    private static final String TAG = AppEventStore.class.getName();

    AppEventStore() {
    }

    public static synchronized void persistEvents(AccessTokenAppIdPair accessTokenAppIdPair, SessionEventsState appEvents) {
        assertIsNotMainThread();
        PersistedEvents persistedEvents = readAndClearStore();
        if (persistedEvents.containsKey(accessTokenAppIdPair)) {
            persistedEvents.get(accessTokenAppIdPair).addAll(appEvents.getEventsToPersist());
        } else {
            persistedEvents.addEvents(accessTokenAppIdPair, appEvents.getEventsToPersist());
        }
        saveEventsToDisk(persistedEvents);
    }

    public static synchronized void persistEvents(AppEventCollection eventsToPersist) {
        assertIsNotMainThread();
        PersistedEvents persistedEvents = readAndClearStore();
        for (AccessTokenAppIdPair accessTokenAppIdPair : eventsToPersist.keySet()) {
            SessionEventsState sessionEventsState = eventsToPersist.get(accessTokenAppIdPair);
            persistedEvents.addEvents(accessTokenAppIdPair, sessionEventsState.getEventsToPersist());
        }
        saveEventsToDisk(persistedEvents);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0033 A[Catch: all -> 0x005b, TRY_ENTER, TRY_LEAVE, TryCatch #7 {, blocks: (B:4:0x0003, B:7:0x0024, B:8:0x0027, B:11:0x0033, B:15:0x003b, B:34:0x007d, B:35:0x0080, B:36:0x0089, B:38:0x008b, B:28:0x0066, B:29:0x0069, B:32:0x0074, B:17:0x0045, B:18:0x0048, B:21:0x0053), top: B:50:0x0003, inners: #3, #4, #9, #10 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized com.facebook.appevents.PersistedEvents readAndClearStore() {
        /*
            java.lang.Class<com.facebook.appevents.AppEventStore> r9 = com.facebook.appevents.AppEventStore.class
            monitor-enter(r9)
            assertIsNotMainThread()     // Catch: java.lang.Throwable -> L5b
            r5 = 0
            r7 = 0
            android.content.Context r1 = com.facebook.FacebookSdk.getApplicationContext()     // Catch: java.lang.Throwable -> L5b
            java.lang.String r8 = "AppEventsLogger.persistedevents"
            java.io.FileInputStream r4 = r1.openFileInput(r8)     // Catch: java.io.FileNotFoundException -> L44 java.lang.Exception -> L5e java.lang.Throwable -> L7c
            com.facebook.appevents.AppEventStore$MovedClassObjectInputStream r6 = new com.facebook.appevents.AppEventStore$MovedClassObjectInputStream     // Catch: java.io.FileNotFoundException -> L44 java.lang.Exception -> L5e java.lang.Throwable -> L7c
            java.io.BufferedInputStream r8 = new java.io.BufferedInputStream     // Catch: java.io.FileNotFoundException -> L44 java.lang.Exception -> L5e java.lang.Throwable -> L7c
            r8.<init>(r4)     // Catch: java.io.FileNotFoundException -> L44 java.lang.Exception -> L5e java.lang.Throwable -> L7c
            r6.<init>(r8)     // Catch: java.io.FileNotFoundException -> L44 java.lang.Exception -> L5e java.lang.Throwable -> L7c
            java.lang.Object r8 = r6.readObject()     // Catch: java.lang.Throwable -> L93 java.lang.Exception -> L96 java.io.FileNotFoundException -> L99
            r0 = r8
            com.facebook.appevents.PersistedEvents r0 = (com.facebook.appevents.PersistedEvents) r0     // Catch: java.lang.Throwable -> L93 java.lang.Exception -> L96 java.io.FileNotFoundException -> L99
            r7 = r0
            com.facebook.internal.Utility.closeQuietly(r6)     // Catch: java.lang.Throwable -> L5b
            java.lang.String r8 = "AppEventsLogger.persistedevents"
            java.io.File r8 = r1.getFileStreamPath(r8)     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L5b
            r8.delete()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L5b
            r5 = r6
        L31:
            if (r7 != 0) goto L38
            com.facebook.appevents.PersistedEvents r7 = new com.facebook.appevents.PersistedEvents     // Catch: java.lang.Throwable -> L5b
            r7.<init>()     // Catch: java.lang.Throwable -> L5b
        L38:
            monitor-exit(r9)
            return r7
        L3a:
            r3 = move-exception
            java.lang.String r8 = com.facebook.appevents.AppEventStore.TAG     // Catch: java.lang.Throwable -> L5b
            java.lang.String r10 = "Got unexpected exception when removing events file: "
            android.util.Log.w(r8, r10, r3)     // Catch: java.lang.Throwable -> L5b
            r5 = r6
            goto L31
        L44:
            r8 = move-exception
        L45:
            com.facebook.internal.Utility.closeQuietly(r5)     // Catch: java.lang.Throwable -> L5b
            java.lang.String r8 = "AppEventsLogger.persistedevents"
            java.io.File r8 = r1.getFileStreamPath(r8)     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L5b
            r8.delete()     // Catch: java.lang.Exception -> L52 java.lang.Throwable -> L5b
            goto L31
        L52:
            r3 = move-exception
            java.lang.String r8 = com.facebook.appevents.AppEventStore.TAG     // Catch: java.lang.Throwable -> L5b
            java.lang.String r10 = "Got unexpected exception when removing events file: "
            android.util.Log.w(r8, r10, r3)     // Catch: java.lang.Throwable -> L5b
            goto L31
        L5b:
            r8 = move-exception
            monitor-exit(r9)
            throw r8
        L5e:
            r2 = move-exception
        L5f:
            java.lang.String r8 = com.facebook.appevents.AppEventStore.TAG     // Catch: java.lang.Throwable -> L7c
            java.lang.String r10 = "Got unexpected exception while reading events: "
            android.util.Log.w(r8, r10, r2)     // Catch: java.lang.Throwable -> L7c
            com.facebook.internal.Utility.closeQuietly(r5)     // Catch: java.lang.Throwable -> L5b
            java.lang.String r8 = "AppEventsLogger.persistedevents"
            java.io.File r8 = r1.getFileStreamPath(r8)     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L73
            r8.delete()     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L73
            goto L31
        L73:
            r3 = move-exception
            java.lang.String r8 = com.facebook.appevents.AppEventStore.TAG     // Catch: java.lang.Throwable -> L5b
            java.lang.String r10 = "Got unexpected exception when removing events file: "
            android.util.Log.w(r8, r10, r3)     // Catch: java.lang.Throwable -> L5b
            goto L31
        L7c:
            r8 = move-exception
        L7d:
            com.facebook.internal.Utility.closeQuietly(r5)     // Catch: java.lang.Throwable -> L5b
            java.lang.String r10 = "AppEventsLogger.persistedevents"
            java.io.File r10 = r1.getFileStreamPath(r10)     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L8a
            r10.delete()     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L8a
        L89:
            throw r8     // Catch: java.lang.Throwable -> L5b
        L8a:
            r3 = move-exception
            java.lang.String r10 = com.facebook.appevents.AppEventStore.TAG     // Catch: java.lang.Throwable -> L5b
            java.lang.String r11 = "Got unexpected exception when removing events file: "
            android.util.Log.w(r10, r11, r3)     // Catch: java.lang.Throwable -> L5b
            goto L89
        L93:
            r8 = move-exception
            r5 = r6
            goto L7d
        L96:
            r2 = move-exception
            r5 = r6
            goto L5f
        L99:
            r8 = move-exception
            r5 = r6
            goto L45
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.appevents.AppEventStore.readAndClearStore():com.facebook.appevents.PersistedEvents");
    }

    private static void saveEventsToDisk(PersistedEvents eventsToPersist) throws Throwable {
        ObjectOutputStream oos;
        ObjectOutputStream oos2 = null;
        Context context = FacebookSdk.getApplicationContext();
        try {
            try {
                oos = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(PERSISTED_EVENTS_FILENAME, 0)));
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            oos.writeObject(eventsToPersist);
            Utility.closeQuietly(oos);
        } catch (Exception e2) {
            e = e2;
            oos2 = oos;
            Log.w(TAG, "Got unexpected exception while persisting events: ", e);
            try {
                context.getFileStreamPath(PERSISTED_EVENTS_FILENAME).delete();
            } catch (Exception e3) {
            }
            Utility.closeQuietly(oos2);
        } catch (Throwable th2) {
            th = th2;
            oos2 = oos;
            Utility.closeQuietly(oos2);
            throw th;
        }
    }

    private static void assertIsNotMainThread() {
    }

    private static class MovedClassObjectInputStream extends ObjectInputStream {
        private static final String ACCESS_TOKEN_APP_ID_PAIR_SERIALIZATION_PROXY_V1_CLASS_NAME = "com.facebook.appevents.AppEventsLogger$AccessTokenAppIdPair$SerializationProxyV1";
        private static final String APP_EVENT_SERIALIZATION_PROXY_V1_CLASS_NAME = "com.facebook.appevents.AppEventsLogger$AppEvent$SerializationProxyV1";

        public MovedClassObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override // java.io.ObjectInputStream
        protected ObjectStreamClass readClassDescriptor() throws ClassNotFoundException, IOException {
            ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();
            if (resultClassDescriptor.getName().equals(ACCESS_TOKEN_APP_ID_PAIR_SERIALIZATION_PROXY_V1_CLASS_NAME)) {
                return ObjectStreamClass.lookup(AccessTokenAppIdPair.SerializationProxyV1.class);
            }
            if (resultClassDescriptor.getName().equals(APP_EVENT_SERIALIZATION_PROXY_V1_CLASS_NAME)) {
                return ObjectStreamClass.lookup(AppEvent.SerializationProxyV1.class);
            }
            return resultClassDescriptor;
        }
    }
}
