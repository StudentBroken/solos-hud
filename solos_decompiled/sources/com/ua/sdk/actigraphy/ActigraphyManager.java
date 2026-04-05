package com.ua.sdk.actigraphy;

import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface ActigraphyManager {
    EntityList<Actigraphy> fetchActigraphy(ActigraphyListRef actigraphyListRef) throws UaException;

    Request fetchActigraphy(ActigraphyListRef actigraphyListRef, FetchCallback<EntityList<Actigraphy>> fetchCallback);
}
