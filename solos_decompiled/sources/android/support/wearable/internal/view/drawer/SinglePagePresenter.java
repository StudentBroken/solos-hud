package android.support.wearable.internal.view.drawer;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.wearable.view.drawer.WearableNavigationDrawer;

/* JADX INFO: loaded from: classes33.dex */
public class SinglePagePresenter implements WearableNavigationDrawerPresenter {
    private static final long DRAWER_CLOSE_DELAY_MS = 500;

    @Nullable
    private WearableNavigationDrawer.WearableNavigationDrawerAdapter mAdapter;
    private final boolean mIsAccessibilityEnabled;
    private final Ui mUi;
    private int mCount = 0;
    private int mSelected = 0;

    public interface Ui {
        void closeDrawerDelayed(long j);

        void deselectItem(int i);

        void initialize(int i);

        void peekDrawer();

        void selectItem(int i);

        void setIcon(int i, Drawable drawable, String str);

        void setPresenter(WearableNavigationDrawerPresenter wearableNavigationDrawerPresenter);

        void setText(String str, boolean z);
    }

    public SinglePagePresenter(Ui ui, boolean isAccessibilityEnabled) {
        if (ui == null) {
            throw new IllegalArgumentException("Received null ui.");
        }
        this.mIsAccessibilityEnabled = isAccessibilityEnabled;
        this.mUi = ui;
        this.mUi.setPresenter(this);
        onDataSetChanged();
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onDataSetChanged() {
        if (this.mAdapter != null) {
            int count = this.mAdapter.getCount();
            if (this.mCount != count) {
                this.mCount = count;
                this.mSelected = Math.min(this.mSelected, count - 1);
                this.mUi.initialize(count);
            }
            for (int i = 0; i < count; i++) {
                this.mUi.setIcon(i, this.mAdapter.getItemDrawable(i), this.mAdapter.getItemText(i));
            }
            this.mUi.setText(this.mAdapter.getItemText(this.mSelected), false);
            this.mUi.selectItem(this.mSelected);
        }
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onNewAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("Received null adapter.");
        }
        this.mAdapter = adapter;
        this.mAdapter.setPresenter(this);
        onDataSetChanged();
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onSelected(int index) {
        this.mUi.deselectItem(this.mSelected);
        this.mUi.selectItem(index);
        this.mSelected = index;
        if (this.mIsAccessibilityEnabled) {
            this.mUi.peekDrawer();
        } else {
            this.mUi.closeDrawerDelayed(DRAWER_CLOSE_DELAY_MS);
        }
        if (this.mAdapter != null) {
            this.mUi.setText(this.mAdapter.getItemText(index), true);
            this.mAdapter.onItemSelected(index);
        }
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public void onSetCurrentItemRequested(int index, boolean smoothScrollTo) {
        this.mUi.deselectItem(this.mSelected);
        this.mUi.selectItem(index);
        this.mSelected = index;
        if (this.mAdapter != null) {
            this.mUi.setText(this.mAdapter.getItemText(index), false);
            this.mAdapter.onItemSelected(index);
        }
    }

    @Override // android.support.wearable.internal.view.drawer.WearableNavigationDrawerPresenter
    public boolean onDrawerTapped() {
        return false;
    }
}
