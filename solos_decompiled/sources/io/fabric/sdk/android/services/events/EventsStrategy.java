package io.fabric.sdk.android.services.events;

/* JADX INFO: loaded from: classes66.dex */
public interface EventsStrategy<T> extends FileRollOverManager, EventsManager<T> {
    FilesSender getFilesSender();
}
