package com.google.tagmanager;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.analytics.containertag.proto.Serving;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.Container;
import com.google.tagmanager.LoadCallback;
import com.google.tagmanager.PreviewManager;
import com.google.tagmanager.ResourceUtil;
import com.google.tagmanager.proto.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;

/* JADX INFO: loaded from: classes49.dex */
class ResourceStorageImpl implements Container.ResourceStorage {
    private static final String SAVED_RESOURCE_FILENAME_PREFIX = "resource_";
    private static final String SAVED_RESOURCE_SUB_DIR = "google_tagmanager";
    private LoadCallback<Resource.ResourceWithMetadata> mCallback;
    private final String mContainerId;
    private final Context mContext;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    ResourceStorageImpl(Context context, String containerId) {
        this.mContext = context;
        this.mContainerId = containerId;
    }

    @Override // com.google.tagmanager.Container.ResourceStorage
    public void setLoadCallback(LoadCallback<Resource.ResourceWithMetadata> callback) {
        this.mCallback = callback;
    }

    @Override // com.google.tagmanager.Container.ResourceStorage
    public void loadResourceFromDiskInBackground() {
        this.mExecutor.execute(new Runnable() { // from class: com.google.tagmanager.ResourceStorageImpl.1
            @Override // java.lang.Runnable
            public void run() {
                ResourceStorageImpl.this.loadResourceFromDisk();
            }
        });
    }

    @VisibleForTesting
    void loadResourceFromDisk() {
        if (this.mCallback == null) {
            throw new IllegalStateException("callback must be set before execute");
        }
        this.mCallback.startLoad();
        Log.v("Start loading resource from disk ...");
        if ((PreviewManager.getInstance().getPreviewMode() == PreviewManager.PreviewMode.CONTAINER || PreviewManager.getInstance().getPreviewMode() == PreviewManager.PreviewMode.CONTAINER_DEBUG) && this.mContainerId.equals(PreviewManager.getInstance().getContainerId())) {
            this.mCallback.onFailure(LoadCallback.Failure.NOT_AVAILABLE);
            return;
        }
        try {
            FileInputStream stream = new FileInputStream(getResourceFile());
            try {
                try {
                    this.mCallback.onSuccess(Resource.ResourceWithMetadata.parseFrom(stream, ProtoExtensionRegistry.getRegistry()));
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.w("error closing stream for reading resource from disk");
                    }
                }
            } catch (IOException e2) {
                Log.w("error reading resource from disk");
                this.mCallback.onFailure(LoadCallback.Failure.IO_ERROR);
                try {
                    stream.close();
                } catch (IOException e3) {
                    Log.w("error closing stream for reading resource from disk");
                }
            }
            Log.v("Load resource from disk finished.");
        } catch (FileNotFoundException e4) {
            Log.d("resource not on disk");
            this.mCallback.onFailure(LoadCallback.Failure.NOT_AVAILABLE);
        }
    }

    @Override // com.google.tagmanager.Container.ResourceStorage
    public void saveResourceToDiskInBackground(final Resource.ResourceWithMetadata resource) {
        this.mExecutor.execute(new Runnable() { // from class: com.google.tagmanager.ResourceStorageImpl.2
            @Override // java.lang.Runnable
            public void run() {
                ResourceStorageImpl.this.saveResourceToDisk(resource);
            }
        });
    }

    @Override // com.google.tagmanager.Container.ResourceStorage
    public Serving.Resource loadResourceFromContainerAsset(String assetFile) {
        Serving.Resource result;
        Log.v("Loading default container from " + assetFile);
        AssetManager assets = this.mContext.getAssets();
        if (assets == null) {
            Log.e("No assets found in package");
            return null;
        }
        try {
            InputStream is = assets.open(assetFile);
            try {
                try {
                    result = Serving.Resource.parseFrom(is, ProtoExtensionRegistry.getRegistry());
                    Log.v("Parsed default container: " + result);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
                Log.w("Error when parsing: " + assetFile);
                try {
                    is.close();
                } catch (IOException e3) {
                }
                result = null;
            }
            return result;
        } catch (IOException e4) {
            Log.w("No asset file: " + assetFile + " found.");
            return null;
        }
    }

    @Override // com.google.tagmanager.Container.ResourceStorage
    public ResourceUtil.ExpandedResource loadExpandedResourceFromJsonAsset(String assetFile) {
        ResourceUtil.ExpandedResource expandedResourceExpandedResourceFromJsonString = null;
        Log.v("loading default container from " + assetFile);
        AssetManager assets = this.mContext.getAssets();
        if (assets == null) {
            Log.w("Looking for default JSON container in package, but no assets were found.");
        } else {
            InputStream is = null;
            try {
                try {
                    is = assets.open(assetFile);
                    expandedResourceExpandedResourceFromJsonString = JsonUtils.expandedResourceFromJsonString(stringFromInputStream(is));
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (Throwable th) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e3) {
                Log.w("No asset file: " + assetFile + " found (or errors reading it).");
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
            } catch (JSONException e5) {
                Log.w("Error parsing JSON file" + assetFile + " : " + e5);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e6) {
                    }
                }
            }
        }
        return expandedResourceExpandedResourceFromJsonString;
    }

    @Override // com.google.tagmanager.Container.ResourceStorage
    public synchronized void close() {
        this.mExecutor.shutdown();
    }

    @VisibleForTesting
    boolean saveResourceToDisk(Resource.ResourceWithMetadata resource) {
        FileOutputStream stream;
        boolean z = false;
        File file = getResourceFile();
        try {
            try {
                stream = new FileOutputStream(file);
                try {
                    resource.writeTo(stream);
                    z = true;
                } catch (IOException e) {
                    Log.w("Error writing resource to disk. Removing resource from disk.");
                    file.delete();
                    try {
                        stream.close();
                    } catch (IOException e2) {
                        Log.w("error closing stream for writing resource to disk");
                    }
                }
            } catch (FileNotFoundException e3) {
                Log.e("Error opening resource file for writing");
            }
            return z;
        } finally {
            try {
                stream.close();
            } catch (IOException e4) {
                Log.w("error closing stream for writing resource to disk");
            }
        }
    }

    @VisibleForTesting
    File getResourceFile() {
        String fileName = SAVED_RESOURCE_FILENAME_PREFIX + this.mContainerId;
        File dir = this.mContext.getDir(SAVED_RESOURCE_SUB_DIR, 0);
        return new File(dir, fileName);
    }

    private String stringFromInputStream(InputStream is) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while (true) {
            int n = reader.read(buffer);
            if (n != -1) {
                writer.write(buffer, 0, n);
            } else {
                return writer.toString();
            }
        }
    }
}
