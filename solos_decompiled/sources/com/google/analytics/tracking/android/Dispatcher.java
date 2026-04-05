package com.google.analytics.tracking.android;

import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
interface Dispatcher {
    void close();

    int dispatchHits(List<Hit> list);

    boolean okToDispatch();

    void overrideHostUrl(String str);
}
