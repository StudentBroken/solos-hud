package android.support.wearable.internal.view.drawer;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.R;
import android.support.wearable.internal.view.drawer.MultiPagePresenter;
import android.support.wearable.view.drawer.PageIndicatorView;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes33.dex */
public class MultiPageUi implements MultiPagePresenter.Ui {
    private static final String TAG = "MultiPageUi";

    @Nullable
    private ViewPager mNavigationPager;

    @Nullable
    private PageIndicatorView mPageIndicatorView;
    private WearableNavigationDrawerPresenter mPresenter;

    @Override // android.support.wearable.internal.view.drawer.MultiPagePresenter.Ui
    public void initialize(WearableNavigationDrawer drawer, WearableNavigationDrawerPresenter presenter) {
        if (drawer == null) {
            throw new IllegalArgumentException("Received null drawer.");
        }
        if (presenter == null) {
            throw new IllegalArgumentException("Received null presenter.");
        }
        this.mPresenter = presenter;
        LayoutInflater inflater = LayoutInflater.from(drawer.getContext());
        View content = inflater.inflate(R.layout.navigation_drawer_view, (ViewGroup) drawer, false);
        this.mNavigationPager = (ViewPager) content.findViewById(R.id.wearable_support_navigation_drawer_view_pager);
        this.mPageIndicatorView = (PageIndicatorView) content.findViewById(R.id.wearable_support_navigation_drawer_page_indicator);
        drawer.setDrawerContent(content);
    }

    @Override // android.support.wearable.internal.view.drawer.MultiPagePresenter.Ui
    public void setNavigationPagerAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter adapter) {
        if (this.mNavigationPager == null || this.mPageIndicatorView == null) {
            Log.w(TAG, "setNavigationPagerAdapter was called before initialize.");
            return;
        }
        NavigationPagerAdapter navigationPagerAdapter = new NavigationPagerAdapter(adapter);
        this.mNavigationPager.setAdapter(navigationPagerAdapter);
        this.mNavigationPager.clearOnPageChangeListeners();
        this.mNavigationPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() { // from class: android.support.wearable.internal.view.drawer.MultiPageUi.1
            @Override // android.support.v4.view.ViewPager.SimpleOnPageChangeListener, android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                MultiPageUi.this.mPresenter.onSelected(position);
            }
        });
        this.mPageIndicatorView.setPager(this.mNavigationPager);
    }

    @Override // android.support.wearable.internal.view.drawer.MultiPagePresenter.Ui
    public void notifyPageIndicatorDataChanged() {
        if (this.mPageIndicatorView != null) {
            this.mPageIndicatorView.notifyDataSetChanged();
        }
    }

    @Override // android.support.wearable.internal.view.drawer.MultiPagePresenter.Ui
    public void notifyNavigationPagerAdapterDataChanged() {
        PagerAdapter adapter;
        if (this.mNavigationPager != null && (adapter = this.mNavigationPager.getAdapter()) != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override // android.support.wearable.internal.view.drawer.MultiPagePresenter.Ui
    public void setNavigationPagerSelectedItem(int index, boolean smoothScrollTo) {
        if (this.mNavigationPager != null) {
            this.mNavigationPager.setCurrentItem(index, smoothScrollTo);
        }
    }

    private static final class NavigationPagerAdapter extends PagerAdapter {
        private final WearableNavigationDrawer.WearableNavigationDrawerAdapter mAdapter;

        NavigationPagerAdapter(WearableNavigationDrawer.WearableNavigationDrawerAdapter adapter) {
            this.mAdapter = adapter;
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.navigation_drawer_item_view, container, false);
            container.addView(view);
            ImageView iconView = (ImageView) view.findViewById(R.id.wearable_support_navigation_drawer_item_icon);
            TextView textView = (TextView) view.findViewById(R.id.wearable_support_navigation_drawer_item_text);
            iconView.setImageDrawable(this.mAdapter.getItemDrawable(position));
            textView.setText(this.mAdapter.getItemText(position));
            return view;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.mAdapter.getCount();
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getItemPosition(Object object) {
            return -2;
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
