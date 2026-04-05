package com.kopin.solos.wear;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.BaseFragmentActivity;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.wear.WatchInfoPageFragment;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class WatchInfoPagerActivity extends BaseFragmentActivity {
    private static final int NUM_PAGES = WatchInfoPageFragment.WatchInfoPages.values().length;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<ImageView> pageIndicatorImages;

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getTheme(LiveRide.getCurrentSport()));
        setContentView(com.kopin.solos.R.layout.activity_page_viewer);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mPager = (ViewPager) findViewById(com.kopin.solos.R.id.pager);
        this.mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.mPager.setAdapter(this.mPagerAdapter);
        addPageDots();
        selectPageIndicator(this.mPager.getCurrentItem());
        this.mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.kopin.solos.wear.WatchInfoPagerActivity.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                WatchInfoPagerActivity.this.selectPageIndicator(position);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void addPageDots() {
        this.pageIndicatorImages = new ArrayList();
        LinearLayout tabLayout = (LinearLayout) findViewById(com.kopin.solos.R.id.tabLayout);
        for (int i = 0; i < NUM_PAGES; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(com.kopin.solos.R.drawable.tab_selector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.width = (int) TypedValue.applyDimension(1, 10.0f, getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(1, 10.0f, getResources().getDisplayMetrics());
            params.rightMargin = (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics());
            tabLayout.addView(dot, params);
            this.pageIndicatorImages.add(dot);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectPageIndicator(int position) {
        int i = 0;
        while (i < NUM_PAGES) {
            this.pageIndicatorImages.get(i).setSelected(position == i);
            i++;
        }
    }

    public void onClosePageClick(View view) {
        finish();
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override // android.support.v4.app.FragmentStatePagerAdapter
        public Fragment getItem(int position) {
            WatchInfoPageFragment pageFragment = new WatchInfoPageFragment();
            Bundle args = new Bundle();
            args.putInt("ordinal", position);
            pageFragment.setArguments(args);
            return pageFragment;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return WatchInfoPagerActivity.NUM_PAGES;
        }
    }
}
