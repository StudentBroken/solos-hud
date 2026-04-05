package com.google.analytics.tracking.android;

import android.content.Context;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/* JADX INFO: loaded from: classes49.dex */
class ClientIdDefaultProvider implements DefaultProvider {
    private static ClientIdDefaultProvider sInstance;
    private static final Object sInstanceLock = new Object();
    private String mClientId;
    private boolean mClientIdLoaded = false;
    private final Object mClientIdLock = new Object();
    private final Context mContext;

    public static void initializeProvider(Context c) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new ClientIdDefaultProvider(c);
            }
        }
    }

    @VisibleForTesting
    static void dropInstance() {
        synchronized (sInstanceLock) {
            sInstance = null;
        }
    }

    public static ClientIdDefaultProvider getProvider() {
        ClientIdDefaultProvider clientIdDefaultProvider;
        synchronized (sInstanceLock) {
            clientIdDefaultProvider = sInstance;
        }
        return clientIdDefaultProvider;
    }

    protected ClientIdDefaultProvider(Context c) {
        this.mContext = c;
        asyncInitializeClientId();
    }

    @Override // com.google.analytics.tracking.android.DefaultProvider
    public boolean providesField(String field) {
        return Fields.CLIENT_ID.equals(field);
    }

    @Override // com.google.analytics.tracking.android.DefaultProvider
    public String getValue(String field) {
        if (Fields.CLIENT_ID.equals(field)) {
            return blockingGetClientId();
        }
        return null;
    }

    private String blockingGetClientId() {
        if (!this.mClientIdLoaded) {
            synchronized (this.mClientIdLock) {
                if (!this.mClientIdLoaded) {
                    Log.v("Waiting for clientId to load");
                    do {
                        try {
                            this.mClientIdLock.wait();
                        } catch (InterruptedException e) {
                            Log.e("Exception while waiting for clientId: " + e);
                        }
                    } while (!this.mClientIdLoaded);
                }
            }
        }
        Log.v("Loaded clientId");
        return this.mClientId;
    }

    private boolean storeClientId(String clientId) {
        try {
            Log.v("Storing clientId.");
            FileOutputStream fos = this.mContext.openFileOutput("gaClientId", 0);
            fos.write(clientId.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("Error creating clientId file.");
            return false;
        } catch (IOException e2) {
            Log.e("Error writing to clientId file.");
            return false;
        }
    }

    protected String generateClientId() {
        String result = UUID.randomUUID().toString().toLowerCase();
        if (!storeClientId(result)) {
            return "0";
        }
        return result;
    }

    private void asyncInitializeClientId() {
        Thread clientIdFetcherThread = new Thread("client_id_fetcher") { // from class: com.google.analytics.tracking.android.ClientIdDefaultProvider.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (ClientIdDefaultProvider.this.mClientIdLock) {
                    ClientIdDefaultProvider.this.mClientId = ClientIdDefaultProvider.this.initializeClientId();
                    ClientIdDefaultProvider.this.mClientIdLoaded = true;
                    ClientIdDefaultProvider.this.mClientIdLock.notifyAll();
                }
            }
        };
        clientIdFetcherThread.start();
    }

    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002b  */
    @com.google.android.gms.common.util.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.lang.String initializeClientId() {
        /*
            r8 = this;
            r4 = 0
            android.content.Context r6 = r8.mContext     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            java.lang.String r7 = "gaClientId"
            java.io.FileInputStream r2 = r6.openFileInput(r7)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            r6 = 128(0x80, float:1.794E-43)
            byte[] r0 = new byte[r6]     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            r6 = 0
            r7 = 128(0x80, float:1.794E-43)
            int r3 = r2.read(r0, r6, r7)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            int r6 = r2.available()     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            if (r6 <= 0) goto L30
            java.lang.String r6 = "clientId file seems corrupted, deleting it."
            com.google.analytics.tracking.android.Log.e(r6)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            r2.close()     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            android.content.Context r6 = r8.mContext     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            java.lang.String r7 = "gaClientId"
            r6.deleteFile(r7)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
        L29:
            if (r4 != 0) goto L2f
            java.lang.String r4 = r8.generateClientId()
        L2f:
            return r4
        L30:
            if (r3 > 0) goto L44
            java.lang.String r6 = "clientId file seems empty, deleting it."
            com.google.analytics.tracking.android.Log.e(r6)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            r2.close()     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            android.content.Context r6 = r8.mContext     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            java.lang.String r7 = "gaClientId"
            r6.deleteFile(r7)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            goto L29
        L42:
            r6 = move-exception
            goto L29
        L44:
            java.lang.String r5 = new java.lang.String     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            r6 = 0
            r5.<init>(r0, r6, r3)     // Catch: java.io.FileNotFoundException -> L42 java.io.IOException -> L4f
            r2.close()     // Catch: java.io.IOException -> L5d java.io.FileNotFoundException -> L60
            r4 = r5
            goto L29
        L4f:
            r1 = move-exception
        L50:
            java.lang.String r6 = "Error reading clientId file, deleting it."
            com.google.analytics.tracking.android.Log.e(r6)
            android.content.Context r6 = r8.mContext
            java.lang.String r7 = "gaClientId"
            r6.deleteFile(r7)
            goto L29
        L5d:
            r1 = move-exception
            r4 = r5
            goto L50
        L60:
            r6 = move-exception
            r4 = r5
            goto L29
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.analytics.tracking.android.ClientIdDefaultProvider.initializeClientId():java.lang.String");
    }
}
