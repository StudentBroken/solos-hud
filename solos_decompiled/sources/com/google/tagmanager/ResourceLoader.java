package com.google.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.analytics.containertag.proto.Serving;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.LoadCallback;
import com.google.tagmanager.PreviewManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes49.dex */
class ResourceLoader implements Runnable {
    private static final String CTFE_URL_PREFIX = "/r?id=";
    private static final String CTFE_URL_SUFFIX = "&v=a50788154";
    private static final String PREVIOUS_CONTAINER_VERSION_QUERY_NAME = "pv";

    @VisibleForTesting
    static final String SDK_VERSION = "a50788154";
    private LoadCallback<Serving.Resource> mCallback;
    private final NetworkClientFactory mClientFactory;
    private final String mContainerId;
    private final Context mContext;
    private volatile CtfeHost mCtfeHost;
    private volatile String mCtfeUrlPathAndQuery;
    private final String mDefaultCtfeUrlPathAndQuery;
    private volatile String mPreviousVersion;

    public ResourceLoader(Context context, String containerId, CtfeHost ctfeHost) {
        this(context, containerId, new NetworkClientFactory(), ctfeHost);
    }

    @VisibleForTesting
    ResourceLoader(Context context, String containerId, NetworkClientFactory factory, CtfeHost ctfeHost) {
        this.mContext = context;
        this.mClientFactory = factory;
        this.mContainerId = containerId;
        this.mCtfeHost = ctfeHost;
        this.mDefaultCtfeUrlPathAndQuery = CTFE_URL_PREFIX + containerId;
        this.mCtfeUrlPathAndQuery = this.mDefaultCtfeUrlPathAndQuery;
        this.mPreviousVersion = null;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mCallback == null) {
            throw new IllegalStateException("callback must be set before execute");
        }
        this.mCallback.startLoad();
        loadResource();
    }

    private boolean okToLoad() {
        ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null && network.isConnected()) {
            return true;
        }
        Log.v("...no network connectivity");
        return false;
    }

    void setLoadCallback(LoadCallback<Serving.Resource> callback) {
        this.mCallback = callback;
    }

    private void loadResource() {
        if (!okToLoad()) {
            this.mCallback.onFailure(LoadCallback.Failure.NOT_AVAILABLE);
            return;
        }
        Log.v("Start loading resource from network ...");
        String url = getCtfeUrl();
        NetworkClient networkClient = this.mClientFactory.createNetworkClient();
        try {
            try {
                InputStream inputStream = networkClient.getInputStream(url);
                try {
                    Serving.OptionalResource resource = Serving.OptionalResource.parseFrom(inputStream, ProtoExtensionRegistry.getRegistry());
                    Log.v("Successfully loaded resource: " + resource);
                    if (!resource.hasResource()) {
                        Log.v("No change for container: " + this.mContainerId);
                    }
                    this.mCallback.onSuccess(resource.hasResource() ? resource.getResource() : null);
                    networkClient.close();
                    Log.v("Load resource from network finished.");
                } catch (IOException e) {
                    Log.w("Error when parsing downloaded resources from url: " + url + " " + e.getMessage(), e);
                    this.mCallback.onFailure(LoadCallback.Failure.SERVER_ERROR);
                    networkClient.close();
                }
            } catch (FileNotFoundException e2) {
                Log.w("No data is retrieved from the given url: " + url + ". Make sure container_id: " + this.mContainerId + " is correct.");
                this.mCallback.onFailure(LoadCallback.Failure.SERVER_ERROR);
                networkClient.close();
            } catch (IOException e3) {
                Log.w("Error when loading resources from url: " + url + " " + e3.getMessage(), e3);
                this.mCallback.onFailure(LoadCallback.Failure.IO_ERROR);
                networkClient.close();
            }
        } catch (Throwable th) {
            networkClient.close();
            throw th;
        }
    }

    @VisibleForTesting
    String getCtfeUrl() {
        String url = this.mCtfeHost.getCtfeServerAddress() + this.mCtfeUrlPathAndQuery + CTFE_URL_SUFFIX;
        if (this.mPreviousVersion != null && !this.mPreviousVersion.trim().equals("")) {
            url = url + "&pv=" + this.mPreviousVersion;
        }
        if (PreviewManager.getInstance().getPreviewMode().equals(PreviewManager.PreviewMode.CONTAINER_DEBUG)) {
            return url + "&gtm_debug=x";
        }
        return url;
    }

    @VisibleForTesting
    void setCtfeURLPathAndQuery(String urlPathAndQuery) {
        if (urlPathAndQuery == null) {
            this.mCtfeUrlPathAndQuery = this.mDefaultCtfeUrlPathAndQuery;
        } else {
            Log.d("Setting CTFE URL path: " + urlPathAndQuery);
            this.mCtfeUrlPathAndQuery = urlPathAndQuery;
        }
    }

    @VisibleForTesting
    void setPreviousVersion(String version) {
        Log.d("Setting previous container version: " + version);
        this.mPreviousVersion = version;
    }
}
