package android.support.wearable.internal.view.drawer;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.wearable.R;
import android.support.wearable.internal.view.drawer.SinglePagePresenter;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/* JADX INFO: loaded from: classes33.dex */
public class SinglePageUi implements SinglePagePresenter.Ui {

    @IdRes
    private static final int[] SINGLE_PAGE_BUTTON_IDS = {R.id.wearable_support_nav_drawer_icon_0, R.id.wearable_support_nav_drawer_icon_1, R.id.wearable_support_nav_drawer_icon_2, R.id.wearable_support_nav_drawer_icon_3, R.id.wearable_support_nav_drawer_icon_4, R.id.wearable_support_nav_drawer_icon_5, R.id.wearable_support_nav_drawer_icon_6};

    @LayoutRes
    private static final int[] SINGLE_PAGE_LAYOUT_RES = {0, R.layout.single_page_nav_drawer_1_item, R.layout.single_page_nav_drawer_2_item, R.layout.single_page_nav_drawer_3_item, R.layout.single_page_nav_drawer_4_item, R.layout.single_page_nav_drawer_5_item, R.layout.single_page_nav_drawer_6_item, R.layout.single_page_nav_drawer_7_item};
    private final WearableNavigationDrawer mDrawer;
    private WearableNavigationDrawerPresenter mPresenter;
    private CircledImageView[] mSinglePageImageViews;

    @Nullable
    private TextView mTextView;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final Runnable mCloseDrawerRunnable = new Runnable() { // from class: android.support.wearable.internal.view.drawer.SinglePageUi.1
        @Override // java.lang.Runnable
        public void run() {
            SinglePageUi.this.mDrawer.closeDrawer();
        }
    };

    public SinglePageUi(WearableNavigationDrawer navigationDrawer) {
        if (navigationDrawer == null) {
            throw new IllegalArgumentException("Received null navigationDrawer.");
        }
        this.mDrawer = navigationDrawer;
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void setPresenter(WearableNavigationDrawerPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void initialize(int count) {
        if (count < 0 || count >= SINGLE_PAGE_LAYOUT_RES.length || SINGLE_PAGE_LAYOUT_RES[count] == 0) {
            this.mDrawer.setDrawerContent(null);
            return;
        }
        int layoutRes = SINGLE_PAGE_LAYOUT_RES[count];
        LayoutInflater inflater = LayoutInflater.from(this.mDrawer.getContext());
        View content = inflater.inflate(layoutRes, (ViewGroup) this.mDrawer, false);
        View peek = inflater.inflate(R.layout.single_page_nav_drawer_peek_view, (ViewGroup) this.mDrawer, false);
        this.mTextView = (TextView) content.findViewById(R.id.wearable_support_nav_drawer_text);
        this.mSinglePageImageViews = new CircledImageView[count];
        for (int i = 0; i < count; i++) {
            this.mSinglePageImageViews[i] = (CircledImageView) content.findViewById(SINGLE_PAGE_BUTTON_IDS[i]);
            this.mSinglePageImageViews[i].setOnClickListener(new OnSelectedClickHandler(i, this.mPresenter));
            this.mSinglePageImageViews[i].setCircleHidden(true);
        }
        this.mDrawer.setDrawerContent(content);
        this.mDrawer.setPeekContent(peek);
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void setIcon(int index, Drawable drawable, String contentDescription) {
        this.mSinglePageImageViews[index].setImageDrawable(drawable);
        this.mSinglePageImageViews[index].setContentDescription(contentDescription);
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void setText(String itemText, boolean showToastIfNoTextView) {
        if (this.mTextView != null) {
            this.mTextView.setText(itemText);
        } else if (showToastIfNoTextView) {
            Toast toast = Toast.makeText(this.mDrawer.getContext(), itemText, 0);
            toast.setGravity(17, 0, 0);
            toast.show();
        }
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void selectItem(int index) {
        this.mSinglePageImageViews[index].setCircleHidden(false);
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void deselectItem(int index) {
        this.mSinglePageImageViews[index].setCircleHidden(true);
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void closeDrawerDelayed(long delayMs) {
        this.mMainThreadHandler.removeCallbacks(this.mCloseDrawerRunnable);
        this.mMainThreadHandler.postDelayed(this.mCloseDrawerRunnable, delayMs);
    }

    @Override // android.support.wearable.internal.view.drawer.SinglePagePresenter.Ui
    public void peekDrawer() {
        this.mDrawer.peekDrawer();
    }

    private static class OnSelectedClickHandler implements View.OnClickListener {
        private final int mIndex;
        private final WearableNavigationDrawerPresenter mPresenter;

        private OnSelectedClickHandler(int index, WearableNavigationDrawerPresenter presenter) {
            this.mIndex = index;
            this.mPresenter = presenter;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            this.mPresenter.onSelected(this.mIndex);
        }
    }
}
