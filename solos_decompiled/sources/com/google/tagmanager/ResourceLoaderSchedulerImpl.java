package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.proto.Serving;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.Container;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes49.dex */
class ResourceLoaderSchedulerImpl implements Container.ResourceLoaderScheduler {
    private static final boolean MAY_INTERRUPT_IF_RUNNING = true;
    private LoadCallback<Serving.Resource> mCallback;
    private boolean mClosed;
    private final String mContainerId;
    private final Context mContext;
    private CtfeHost mCtfeHost;
    private String mCtfeUrlPathAndQuery;
    private final ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mFuture;
    private final ResourceLoaderFactory mResourceLoaderFactory;

    interface ResourceLoaderFactory {
        ResourceLoader createResourceLoader(CtfeHost ctfeHost);
    }

    interface ScheduledExecutorServiceFactory {
        ScheduledExecutorService createExecutorService();
    }

    public ResourceLoaderSchedulerImpl(Context context, String containerId, CtfeHost ctfeHost) {
        this(context, containerId, ctfeHost, null, null);
    }

    @VisibleForTesting
    ResourceLoaderSchedulerImpl(Context context, String containerId, CtfeHost ctfeHost, ScheduledExecutorServiceFactory executorServiceFactory, ResourceLoaderFactory resourceLoaderFactory) {
        this.mCtfeHost = ctfeHost;
        this.mContext = context;
        this.mContainerId = containerId;
        this.mExecutor = (executorServiceFactory == null ? new ScheduledExecutorServiceFactory() { // from class: com.google.tagmanager.ResourceLoaderSchedulerImpl.1
            @Override // com.google.tagmanager.ResourceLoaderSchedulerImpl.ScheduledExecutorServiceFactory
            public ScheduledExecutorService createExecutorService() {
                return Executors.newSingleThreadScheduledExecutor();
            }
        } : executorServiceFactory).createExecutorService();
        if (resourceLoaderFactory == null) {
            this.mResourceLoaderFactory = new ResourceLoaderFactory() { // from class: com.google.tagmanager.ResourceLoaderSchedulerImpl.2
                @Override // com.google.tagmanager.ResourceLoaderSchedulerImpl.ResourceLoaderFactory
                public ResourceLoader createResourceLoader(CtfeHost ctfeHost2) {
                    return new ResourceLoader(ResourceLoaderSchedulerImpl.this.mContext, ResourceLoaderSchedulerImpl.this.mContainerId, ctfeHost2);
                }
            };
        } else {
            this.mResourceLoaderFactory = resourceLoaderFactory;
        }
    }

    @Override // com.google.tagmanager.Container.ResourceLoaderScheduler
    public synchronized void close() {
        ensureNotClosed();
        if (this.mFuture != null) {
            this.mFuture.cancel(false);
        }
        this.mExecutor.shutdown();
        this.mClosed = true;
    }

    @Override // com.google.tagmanager.Container.ResourceLoaderScheduler
    public synchronized void setLoadCallback(LoadCallback<Serving.Resource> callback) {
        ensureNotClosed();
        this.mCallback = callback;
    }

    @Override // com.google.tagmanager.Container.ResourceLoaderScheduler
    public synchronized void loadAfterDelay(long delayInMillis, String previousVersion) {
        Log.v("loadAfterDelay: containerId=" + this.mContainerId + " delay=" + delayInMillis);
        ensureNotClosed();
        if (this.mCallback == null) {
            throw new IllegalStateException("callback must be set before loadAfterDelay() is called.");
        }
        if (this.mFuture != null) {
            this.mFuture.cancel(true);
        }
        this.mFuture = this.mExecutor.schedule(createResourceLoader(previousVersion), delayInMillis, TimeUnit.MILLISECONDS);
    }

    @Override // com.google.tagmanager.Container.ResourceLoaderScheduler
    public synchronized void setCtfeURLPathAndQuery(String urlPathAndQuery) {
        ensureNotClosed();
        this.mCtfeUrlPathAndQuery = urlPathAndQuery;
    }

    private synchronized void ensureNotClosed() {
        if (this.mClosed) {
            throw new IllegalStateException("called method after closed");
        }
    }

    private ResourceLoader createResourceLoader(String previousVersion) {
        ResourceLoader resourceLoader = this.mResourceLoaderFactory.createResourceLoader(this.mCtfeHost);
        resourceLoader.setLoadCallback(this.mCallback);
        resourceLoader.setCtfeURLPathAndQuery(this.mCtfeUrlPathAndQuery);
        resourceLoader.setPreviousVersion(previousVersion);
        return resourceLoader;
    }
}
