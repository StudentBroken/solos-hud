package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.proto.Serving;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.CustomFunctionCall;
import com.google.tagmanager.LoadCallback;
import com.google.tagmanager.PreviewManager;
import com.google.tagmanager.ResourceUtil;
import com.google.tagmanager.TagManager;
import com.google.tagmanager.proto.Resource;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
public class Container {
    static final boolean ENABLE_CONTAINER_DEBUG_MODE = false;

    @VisibleForTesting
    static final int MAX_NUMBER_OF_TOKENS = 30;
    static final int MINIMUM_REFRESH_PERIOD_BURST_MODE_MS = 5000;
    static final long MINIMUM_REFRESH_PERIOD_MS = 900000;
    static final long REFRESH_PERIOD_ON_FAILURE_MS = 3600000;
    static final long REFRESH_PERIOD_ON_SUCCESS_MS = 43200000;
    private Clock mClock;
    private final String mContainerId;
    private final Context mContext;
    private CtfeHost mCtfeHost;
    private volatile String mCtfeServerAddress;
    private volatile String mCtfeUrlPathAndQuery;

    @VisibleForTesting
    LoadCallback<Resource.ResourceWithMetadata> mDiskLoadCallback;
    private Map<String, FunctionCallMacroHandler> mFunctionCallMacroHandlers;
    private Map<String, FunctionCallTagHandler> mFunctionCallTagHandlers;
    private volatile Serving.Resource mLastLoadedResource;
    private volatile long mLastRefreshMethodCalledTime;
    private volatile long mLastRefreshTime;

    @VisibleForTesting
    LoadCallback<Serving.Resource> mNetworkLoadCallback;
    private ResourceLoaderScheduler mNetworkLoadScheduler;
    private volatile int mNumTokens;
    private volatile int mResourceFormatVersion;
    private ResourceStorage mResourceStorage;
    private volatile String mResourceVersion;
    private Runtime mRuntime;
    private final TagManager mTagManager;
    private Callback mUserCallback;

    public interface Callback {
        void containerRefreshBegin(Container container, RefreshType refreshType);

        void containerRefreshFailure(Container container, RefreshType refreshType, RefreshFailure refreshFailure);

        void containerRefreshSuccess(Container container, RefreshType refreshType);
    }

    public interface FunctionCallMacroHandler {
        Object getValue(String str, Map<String, Object> map);
    }

    public interface FunctionCallTagHandler {
        void execute(String str, Map<String, Object> map);
    }

    public enum RefreshFailure {
        NO_SAVED_CONTAINER,
        IO_ERROR,
        NO_NETWORK,
        NETWORK_ERROR,
        SERVER_ERROR,
        UNKNOWN_ERROR
    }

    public enum RefreshType {
        SAVED,
        NETWORK
    }

    interface ResourceLoaderScheduler {
        void close();

        void loadAfterDelay(long j, String str);

        void setCtfeURLPathAndQuery(String str);

        void setLoadCallback(LoadCallback<Serving.Resource> loadCallback);
    }

    interface ResourceStorage {
        void close();

        ResourceUtil.ExpandedResource loadExpandedResourceFromJsonAsset(String str);

        Serving.Resource loadResourceFromContainerAsset(String str);

        void loadResourceFromDiskInBackground();

        void saveResourceToDiskInBackground(Resource.ResourceWithMetadata resourceWithMetadata);

        void setLoadCallback(LoadCallback<Resource.ResourceWithMetadata> loadCallback);
    }

    private class FunctionCallMacroHandlerAdapter implements CustomFunctionCall.CustomEvaluator {
        private FunctionCallMacroHandlerAdapter() {
        }

        @Override // com.google.tagmanager.CustomFunctionCall.CustomEvaluator
        public Object evaluate(String key, Map<String, Object> parameters) {
            FunctionCallMacroHandler handler = Container.this.getFunctionCallMacroHandler(key);
            if (handler == null) {
                return null;
            }
            return handler.getValue(key, parameters);
        }
    }

    private class FunctionCallTagHandlerAdapter implements CustomFunctionCall.CustomEvaluator {
        private FunctionCallTagHandlerAdapter() {
        }

        @Override // com.google.tagmanager.CustomFunctionCall.CustomEvaluator
        public Object evaluate(String key, Map<String, Object> parameters) {
            FunctionCallTagHandler handler = Container.this.getFunctionCallTagHandler(key);
            handler.execute(key, parameters);
            return Types.getDefaultString();
        }
    }

    Container(Context context, String containerId, TagManager tagManager) {
        this(context, containerId, tagManager, new ResourceStorageImpl(context, containerId));
    }

    @VisibleForTesting
    Container(Context context, String containerId, TagManager tagManager, ResourceStorage resourceStorage) {
        this.mResourceVersion = "";
        this.mResourceFormatVersion = 0;
        this.mCtfeHost = new CtfeHost();
        this.mContext = context;
        this.mContainerId = containerId;
        this.mTagManager = tagManager;
        this.mResourceStorage = resourceStorage;
        this.mNumTokens = 30;
        this.mFunctionCallMacroHandlers = new HashMap();
        this.mFunctionCallTagHandlers = new HashMap();
        createInitialContainer();
    }

    public String getContainerId() {
        return this.mContainerId;
    }

    public boolean getBoolean(String key) {
        Runtime runtime = getRuntime();
        if (runtime == null) {
            Log.e("getBoolean called for closed container.");
            return Types.getDefaultBoolean().booleanValue();
        }
        try {
            return Types.valueToBoolean(runtime.evaluateMacroReference(key).getObject()).booleanValue();
        } catch (Exception e) {
            Log.e("Calling getBoolean() threw an exception: " + e.getMessage() + " Returning default value.");
            return Types.getDefaultBoolean().booleanValue();
        }
    }

    public double getDouble(String key) {
        Runtime runtime = getRuntime();
        if (runtime == null) {
            Log.e("getDouble called for closed container.");
            return Types.getDefaultDouble().doubleValue();
        }
        try {
            return Types.valueToDouble(runtime.evaluateMacroReference(key).getObject()).doubleValue();
        } catch (Exception e) {
            Log.e("Calling getDouble() threw an exception: " + e.getMessage() + " Returning default value.");
            return Types.getDefaultDouble().doubleValue();
        }
    }

    public long getLong(String key) {
        Runtime runtime = getRuntime();
        if (runtime == null) {
            Log.e("getLong called for closed container.");
            return Types.getDefaultInt64().longValue();
        }
        try {
            return Types.valueToInt64(runtime.evaluateMacroReference(key).getObject()).longValue();
        } catch (Exception e) {
            Log.e("Calling getLong() threw an exception: " + e.getMessage() + " Returning default value.");
            return Types.getDefaultInt64().longValue();
        }
    }

    public String getString(String key) {
        Runtime runtime = getRuntime();
        if (runtime == null) {
            Log.e("getString called for closed container.");
            return Types.getDefaultString();
        }
        try {
            return Types.valueToString(runtime.evaluateMacroReference(key).getObject());
        } catch (Exception e) {
            Log.e("Calling getString() threw an exception: " + e.getMessage() + " Returning default value.");
            return Types.getDefaultString();
        }
    }

    public long getLastRefreshTime() {
        return this.mLastRefreshTime;
    }

    public synchronized void refresh() {
        if (getRuntime() == null) {
            Log.w("refresh called for closed container");
        } else {
            try {
                if (isDefaultContainerRefreshMode()) {
                    Log.w("Container is in DEFAULT_CONTAINER mode. Refresh request is ignored.");
                } else {
                    long currentTime = this.mClock.currentTimeMillis();
                    if (useAvailableToken(currentTime)) {
                        Log.v("Container refresh requested");
                        loadAfterDelay(0L);
                        this.mLastRefreshMethodCalledTime = currentTime;
                    } else {
                        Log.v("Container refresh was called too often. Ignored.");
                    }
                }
            } catch (Exception e) {
                Log.e("Calling refresh() throws an exception: " + e.getMessage());
            }
        }
    }

    public synchronized void close() {
        try {
            if (this.mNetworkLoadScheduler != null) {
                this.mNetworkLoadScheduler.close();
            }
            this.mNetworkLoadScheduler = null;
            if (this.mResourceStorage != null) {
                this.mResourceStorage.close();
            }
            this.mResourceStorage = null;
            this.mUserCallback = null;
            this.mTagManager.removeContainer(this.mContainerId);
        } catch (Exception e) {
            Log.e("Calling close() threw an exception: " + e.getMessage());
        }
        this.mRuntime = null;
    }

    public boolean isDefault() {
        return getLastRefreshTime() == 0;
    }

    void load(Callback callback) {
        Clock systemClock = new Clock() { // from class: com.google.tagmanager.Container.1
            @Override // com.google.tagmanager.Clock
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        load(callback, new ResourceLoaderSchedulerImpl(this.mContext, this.mContainerId, this.mCtfeHost), systemClock);
    }

    public synchronized void registerFunctionCallMacroHandler(String customMacroName, FunctionCallMacroHandler customMacroHandler) {
        this.mFunctionCallMacroHandlers.put(customMacroName, customMacroHandler);
    }

    public synchronized FunctionCallMacroHandler getFunctionCallMacroHandler(String customMacroName) {
        return this.mFunctionCallMacroHandlers.get(customMacroName);
    }

    public synchronized void registerFunctionCallTagHandler(String customTagName, FunctionCallTagHandler customTagHandler) {
        this.mFunctionCallTagHandlers.put(customTagName, customTagHandler);
    }

    public synchronized FunctionCallTagHandler getFunctionCallTagHandler(String customTagName) {
        return this.mFunctionCallTagHandlers.get(customTagName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void callRefreshSuccess(RefreshType refreshType) {
        Log.v("calling containerRefreshSuccess(" + refreshType + "): mUserCallback = " + this.mUserCallback);
        if (this.mUserCallback != null) {
            this.mUserCallback.containerRefreshSuccess(this, refreshType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void callRefreshFailure(RefreshType refreshType, RefreshFailure refreshFailure) {
        if (this.mUserCallback != null) {
            this.mUserCallback.containerRefreshFailure(this, refreshType, refreshFailure);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void callRefreshBegin(RefreshType refreshType) {
        if (this.mUserCallback != null) {
            this.mUserCallback.containerRefreshBegin(this, refreshType);
        }
    }

    @VisibleForTesting
    void evaluateTags(String currentEventName) {
        getRuntime().evaluateTags(currentEventName);
    }

    @VisibleForTesting
    synchronized void load(Callback callback, ResourceLoaderScheduler scheduler, final Clock clock) {
        if (this.mDiskLoadCallback != null) {
            throw new RuntimeException("Container already loaded: container ID: " + this.mContainerId);
        }
        this.mClock = clock;
        this.mUserCallback = callback;
        this.mDiskLoadCallback = new LoadCallback<Resource.ResourceWithMetadata>() { // from class: com.google.tagmanager.Container.2
            @Override // com.google.tagmanager.LoadCallback
            public void startLoad() {
                Container.this.callRefreshBegin(RefreshType.SAVED);
            }

            @Override // com.google.tagmanager.LoadCallback
            public void onSuccess(Resource.ResourceWithMetadata resource) {
                if (Container.this.isDefault()) {
                    Container.this.initEvaluators(resource.getResource(), false);
                    Log.v("setting refresh time to saved time: " + resource.getTimeStamp());
                    Container.this.mLastRefreshTime = resource.getTimeStamp();
                    Container.this.loadAfterDelay(Math.max(0L, Math.min(Container.REFRESH_PERIOD_ON_SUCCESS_MS, (Container.this.mLastRefreshTime + Container.REFRESH_PERIOD_ON_SUCCESS_MS) - clock.currentTimeMillis())));
                }
                Container.this.callRefreshSuccess(RefreshType.SAVED);
            }

            @Override // com.google.tagmanager.LoadCallback
            public void onFailure(LoadCallback.Failure failure) {
                Container.this.callRefreshFailure(RefreshType.SAVED, failureToRefreshFailure(failure));
                if (Container.this.isDefault()) {
                    Container.this.loadAfterDelay(0L);
                }
            }

            private RefreshFailure failureToRefreshFailure(LoadCallback.Failure failure) {
                switch (AnonymousClass4.$SwitchMap$com$google$tagmanager$LoadCallback$Failure[failure.ordinal()]) {
                    case 1:
                        return RefreshFailure.NO_SAVED_CONTAINER;
                    case 2:
                        return RefreshFailure.IO_ERROR;
                    case 3:
                        return RefreshFailure.SERVER_ERROR;
                    default:
                        return RefreshFailure.UNKNOWN_ERROR;
                }
            }
        };
        if (isDefaultContainerRefreshMode()) {
            Log.i("Container is in DEFAULT_CONTAINER mode. Use default container.");
        } else {
            this.mResourceStorage.setLoadCallback(this.mDiskLoadCallback);
            this.mNetworkLoadCallback = new LoadCallback<Serving.Resource>() { // from class: com.google.tagmanager.Container.3
                @Override // com.google.tagmanager.LoadCallback
                public void startLoad() {
                    Container.this.callRefreshBegin(RefreshType.NETWORK);
                }

                @Override // com.google.tagmanager.LoadCallback
                public void onSuccess(Serving.Resource resource) {
                    synchronized (Container.this) {
                        if (resource != null) {
                            Container.this.initEvaluators(resource, false);
                        } else if (Container.this.mLastLoadedResource != null) {
                            resource = Container.this.mLastLoadedResource;
                        } else {
                            onFailure(LoadCallback.Failure.SERVER_ERROR);
                            return;
                        }
                        Container.this.mLastRefreshTime = clock.currentTimeMillis();
                        Log.v("setting refresh time to current time: " + Container.this.mLastRefreshTime);
                        if (!Container.this.isContainerPreview()) {
                            Container.this.saveResourceToDisk(resource);
                        }
                        Container.this.loadAfterDelay(Container.REFRESH_PERIOD_ON_SUCCESS_MS);
                        Container.this.callRefreshSuccess(RefreshType.NETWORK);
                    }
                }

                @Override // com.google.tagmanager.LoadCallback
                public void onFailure(LoadCallback.Failure failure) {
                    Container.this.loadAfterDelay(3600000L);
                    Container.this.callRefreshFailure(RefreshType.NETWORK, failureToRefreshFailure(failure));
                }

                private RefreshFailure failureToRefreshFailure(LoadCallback.Failure failure) {
                    switch (AnonymousClass4.$SwitchMap$com$google$tagmanager$LoadCallback$Failure[failure.ordinal()]) {
                        case 1:
                            return RefreshFailure.NO_NETWORK;
                        case 2:
                            return RefreshFailure.NETWORK_ERROR;
                        case 3:
                            return RefreshFailure.SERVER_ERROR;
                        default:
                            return RefreshFailure.UNKNOWN_ERROR;
                    }
                }
            };
            scheduler.setLoadCallback(this.mNetworkLoadCallback);
            if (isContainerPreview()) {
                this.mCtfeUrlPathAndQuery = PreviewManager.getInstance().getCTFEUrlPath();
                scheduler.setCtfeURLPathAndQuery(this.mCtfeUrlPathAndQuery);
            }
            if (this.mCtfeServerAddress != null) {
                this.mCtfeHost.setCtfeServerAddress(this.mCtfeServerAddress);
            }
            this.mNetworkLoadScheduler = scheduler;
            this.mResourceStorage.loadResourceFromDiskInBackground();
        }
    }

    /* JADX INFO: renamed from: com.google.tagmanager.Container$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$google$tagmanager$LoadCallback$Failure = new int[LoadCallback.Failure.values().length];

        static {
            try {
                $SwitchMap$com$google$tagmanager$LoadCallback$Failure[LoadCallback.Failure.NOT_AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$tagmanager$LoadCallback$Failure[LoadCallback.Failure.IO_ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$tagmanager$LoadCallback$Failure[LoadCallback.Failure.SERVER_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    @VisibleForTesting
    String getResourceVersion() {
        return this.mResourceVersion;
    }

    @VisibleForTesting
    synchronized void loadAfterDelay(long delay) {
        if (this.mNetworkLoadScheduler != null && !isDefaultContainerRefreshMode()) {
            String previousVersion = this.mLastLoadedResource == null ? null : this.mLastLoadedResource.getVersion();
            this.mNetworkLoadScheduler.loadAfterDelay(delay, previousVersion);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void saveResourceToDisk(Serving.Resource resource) {
        if (this.mResourceStorage != null) {
            this.mResourceStorage.saveResourceToDiskInBackground(Resource.ResourceWithMetadata.newBuilder().setTimeStamp(getLastRefreshTime()).setResource(resource).build());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initEvaluators(Serving.Resource resource, boolean isDefaultResource) {
        try {
            ResourceUtil.ExpandedResource expandedResource = ResourceUtil.getExpandedResource(resource);
            if (!isDefaultResource) {
                this.mLastLoadedResource = resource;
            }
            initEvaluatorsWithExpandedResource(expandedResource);
        } catch (ResourceUtil.InvalidResourceException err) {
            Log.e("Not loading resource: " + resource + " because it is invalid: " + err.toString());
        }
    }

    private void initEvaluatorsWithExpandedResource(ResourceUtil.ExpandedResource expandedResource) {
        this.mResourceVersion = expandedResource.getVersion();
        this.mResourceFormatVersion = expandedResource.getResourceFormatVersion();
        EventInfoDistributor eventInfoDistributor = createEventInfoDistributor(this.mResourceVersion);
        Runtime runtime = new Runtime(this.mContext, expandedResource, this.mTagManager.getDataLayer(), new FunctionCallMacroHandlerAdapter(), new FunctionCallTagHandlerAdapter(), eventInfoDistributor);
        setRuntime(runtime);
    }

    @VisibleForTesting
    EventInfoDistributor createEventInfoDistributor(String resourceVersion) {
        if (PreviewManager.getInstance().getPreviewMode().equals(PreviewManager.PreviewMode.CONTAINER_DEBUG)) {
        }
        return new NoopEventInfoDistributor();
    }

    private synchronized void setRuntime(Runtime runtime) {
        this.mRuntime = runtime;
    }

    private synchronized Runtime getRuntime() {
        return this.mRuntime;
    }

    @VisibleForTesting
    synchronized void setCtfeServerAddress(String addr) {
        this.mCtfeServerAddress = addr;
        if (addr != null) {
            this.mCtfeHost.setCtfeServerAddress(addr);
        }
    }

    @VisibleForTesting
    synchronized void setCtfeUrlPathAndQuery(String ctfeUrlPathAndQuery) {
        this.mCtfeUrlPathAndQuery = ctfeUrlPathAndQuery;
        if (this.mNetworkLoadScheduler != null) {
            this.mNetworkLoadScheduler.setCtfeURLPathAndQuery(ctfeUrlPathAndQuery);
        }
    }

    String getCtfeUrlPathAndQuery() {
        return this.mCtfeUrlPathAndQuery;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isContainerPreview() {
        PreviewManager previewManager = PreviewManager.getInstance();
        return (previewManager.getPreviewMode() == PreviewManager.PreviewMode.CONTAINER || previewManager.getPreviewMode() == PreviewManager.PreviewMode.CONTAINER_DEBUG) && this.mContainerId.equals(previewManager.getContainerId());
    }

    private void createInitialContainer() {
        String containerFile = "tagmanager/" + this.mContainerId;
        Serving.Resource resource = this.mResourceStorage.loadResourceFromContainerAsset(containerFile);
        if (resource != null) {
            initEvaluators(resource, true);
            return;
        }
        ResourceUtil.ExpandedResource expandedResource = this.mResourceStorage.loadExpandedResourceFromJsonAsset(containerFile + ".json");
        if (expandedResource == null) {
            Log.w("No default container found; creating an empty container.");
            expandedResource = ResourceUtil.ExpandedResource.newBuilder().build();
        }
        initEvaluatorsWithExpandedResource(expandedResource);
    }

    private boolean isDefaultContainerRefreshMode() {
        return this.mTagManager.getRefreshMode() == TagManager.RefreshMode.DEFAULT_CONTAINER;
    }

    private boolean useAvailableToken(long currentTime) {
        if (this.mLastRefreshMethodCalledTime == 0) {
            this.mNumTokens--;
            return true;
        }
        long timeElapsed = currentTime - this.mLastRefreshMethodCalledTime;
        if (timeElapsed < 5000) {
            return false;
        }
        if (this.mNumTokens < 30) {
            int numAddedTokens = (int) Math.floor(timeElapsed / MINIMUM_REFRESH_PERIOD_MS);
            this.mNumTokens = Math.min(30, this.mNumTokens + numAddedTokens);
        }
        if (this.mNumTokens <= 0) {
            return false;
        }
        this.mNumTokens--;
        return true;
    }
}
