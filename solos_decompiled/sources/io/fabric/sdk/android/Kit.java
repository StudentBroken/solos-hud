package io.fabric.sdk.android;

import android.content.Context;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.concurrency.Task;
import java.io.File;
import java.util.Collection;

/* JADX INFO: loaded from: classes66.dex */
public abstract class Kit<Result> implements Comparable<Kit> {
    Context context;
    Fabric fabric;
    IdManager idManager;
    InitializationCallback<Result> initializationCallback;
    InitializationTask<Result> initializationTask = new InitializationTask<>(this);

    protected abstract Result doInBackground();

    public abstract String getIdentifier();

    public abstract String getVersion();

    void injectParameters(Context context, Fabric fabric, InitializationCallback<Result> callback, IdManager idManager) {
        this.fabric = fabric;
        this.context = new FabricContext(context, getIdentifier(), getPath());
        this.initializationCallback = callback;
        this.idManager = idManager;
    }

    final void initialize() {
        this.initializationTask.executeOnExecutor(this.fabric.getExecutorService(), (Object[]) new Void[]{(Void) null});
    }

    protected boolean onPreExecute() {
        return true;
    }

    protected void onPostExecute(Result result) {
    }

    protected void onCancelled(Result result) {
    }

    protected IdManager getIdManager() {
        return this.idManager;
    }

    public Context getContext() {
        return this.context;
    }

    public Fabric getFabric() {
        return this.fabric;
    }

    public String getPath() {
        return ".Fabric" + File.separator + getIdentifier();
    }

    @Override // java.lang.Comparable
    public int compareTo(Kit another) {
        if (containsAnnotatedDependency(another)) {
            return 1;
        }
        if (another.containsAnnotatedDependency(this)) {
            return -1;
        }
        if (!hasAnnotatedDependency() || another.hasAnnotatedDependency()) {
            return (hasAnnotatedDependency() || !another.hasAnnotatedDependency()) ? 0 : -1;
        }
        return 1;
    }

    boolean containsAnnotatedDependency(Kit target) {
        DependsOn dependsOn = (DependsOn) getClass().getAnnotation(DependsOn.class);
        if (dependsOn != null) {
            Class<?>[] deps = dependsOn.value();
            for (Class<?> dep : deps) {
                if (dep.equals(target.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean hasAnnotatedDependency() {
        DependsOn dependsOn = (DependsOn) getClass().getAnnotation(DependsOn.class);
        return dependsOn != null;
    }

    protected Collection<Task> getDependencies() {
        return this.initializationTask.getDependencies();
    }
}
