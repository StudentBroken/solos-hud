package io.fabric.sdk.android.services.events;

/* JADX INFO: loaded from: classes66.dex */
public interface EventsManager<T> {
    void deleteAllEvents();

    void recordEvent(T t);

    void sendEvents();
}
