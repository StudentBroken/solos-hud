package android.support.wearable.internal.view.drawer;

import android.support.annotation.MainThread;
import android.support.wearable.view.drawer.WearableNavigationDrawer;

/* JADX INFO: loaded from: classes33.dex */
public interface WearableNavigationDrawerPresenter {
    @MainThread
    void onDataSetChanged();

    @MainThread
    boolean onDrawerTapped();

    @MainThread
    void onNewAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter wearableNavigationDrawerAdapter);

    @MainThread
    void onSelected(int i);

    @MainThread
    void onSetCurrentItemRequested(int i, boolean z);
}
