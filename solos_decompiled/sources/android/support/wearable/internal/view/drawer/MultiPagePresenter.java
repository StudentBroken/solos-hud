package android.support.wearable.internal.view.drawer;

import android.support.annotation.Nullable;
import android.support.wearable.view.drawer.WearableNavigationDrawer;

/* JADX INFO: loaded from: classes33.dex */
public class MultiPagePresenter implements WearableNavigationDrawerPresenter {

    @Nullable
    private WearableNavigationDrawer.WearableNavigationDrawerAdapter mAdapter;
    private final WearableNavigationDrawer mDrawer;
    private final boolean mIsAccessibilityEnabled;
    private final Ui mUi;

    public interface Ui {
        void initialize(WearableNavigationDrawer wearableNavigationDrawer, WearableNavigationDrawerPresenter wearableNavigationDrawerPresenter);

        void notifyNavigationPagerAdapterDataChanged();

        void notifyPageIndicatorDataChanged();

        void setNavigationPagerAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter wearableNavigationDrawerAdapter);

        void setNavigationPagerSelectedItem(int i, boolean z);
    }

    public MultiPagePresenter(WearableNavigationDrawer drawer, Ui ui, boolean isAccessibilityEnabled) {
        if (drawer == null) {
            throw new IllegalArgumentException("Received null drawer.");
        }
        if (ui == null) {
            throw new IllegalArgumentException("Received null ui.");
        }
        this.mDrawer = drawer;
        this.mUi = ui;
        this.mUi.initialize(drawer, this);
        this.mIsAccessibilityEnabled = isAccessibilityEnabled;
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onDataSetChanged() {
        this.mUi.notifyNavigationPagerAdapterDataChanged();
        this.mUi.notifyPageIndicatorDataChanged();
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onNewAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("Received null adapter.");
        }
        this.mAdapter = adapter;
        this.mAdapter.setPresenter(this);
        this.mUi.setNavigationPagerAdapter(adapter);
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onSelected(int index) {
        if (this.mAdapter != null) {
            this.mAdapter.onItemSelected(index);
        }
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onSetCurrentItemRequested(int index, boolean smoothScrollTo) {
        this.mUi.setNavigationPagerSelectedItem(index, smoothScrollTo);
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public boolean onDrawerTapped() {
        if (this.mDrawer.isOpened()) {
            if (this.mIsAccessibilityEnabled) {
                this.mDrawer.peekDrawer();
            } else {
                this.mDrawer.closeDrawer();
            }
            return true;
        }
        return false;
    }
}
