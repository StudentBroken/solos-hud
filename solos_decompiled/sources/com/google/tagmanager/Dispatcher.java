package com.google.tagmanager;

import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
interface Dispatcher {
    void close();

    void dispatchHits(List<Hit> list);

    boolean okToDispatch();
}
