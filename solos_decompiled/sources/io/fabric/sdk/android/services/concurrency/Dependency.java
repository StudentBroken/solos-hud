package io.fabric.sdk.android.services.concurrency;

import java.util.Collection;

/* JADX INFO: loaded from: classes66.dex */
public interface Dependency<T> {
    void addDependency(T t);

    boolean areDependenciesMet();

    Collection<T> getDependencies();
}
