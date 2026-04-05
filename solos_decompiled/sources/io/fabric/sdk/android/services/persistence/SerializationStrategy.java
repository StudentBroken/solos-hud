package io.fabric.sdk.android.services.persistence;

/* JADX INFO: loaded from: classes66.dex */
public interface SerializationStrategy<T> {
    T deserialize(String str);

    String serialize(T t);
}
