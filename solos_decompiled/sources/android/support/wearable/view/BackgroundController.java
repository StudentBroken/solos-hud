package android.support.wearable.view;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.View;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
class BackgroundController implements GridViewPager.OnPageChangeListener, GridViewPager.OnAdapterChangeListener, GridPagerAdapter.OnBackgroundChangeListener {
    private GridPagerAdapter mAdapter;
    private float mBaseXPos;
    private int mBaseXSteps;
    private float mBaseYPos;
    private int mBaseYSteps;
    private float mCrossfadeXPos;
    private float mCrossfadeYPos;
    private int mFadeXSteps;
    private int mFadeYSteps;
    private float mScrollRelativeX;
    private float mScrollRelativeY;
    private boolean mUsingCrossfadeLayer;
    private Direction mDirection = Direction.NONE;
    private final Point mCurrentPage = new Point();
    private final Point mLastSelectedPage = new Point();
    private final LruCache<Integer, Drawable> mRowBackgrounds = new LruCache<Integer, Drawable>(3) { // from class: android.support.wearable.view.BackgroundController.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.v4.util.LruCache
        public Drawable create(Integer key) {
            return BackgroundController.this.mAdapter.getBackgroundForRow(key.intValue()).mutate();
        }
    };
    private final LruCache<Integer, Drawable> mPageBackgrounds = new LruCache<Integer, Drawable>(5) { // from class: android.support.wearable.view.BackgroundController.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.v4.util.LruCache
        public Drawable create(Integer key) {
            int col = BackgroundController.unpackX(key.intValue());
            int row = BackgroundController.unpackY(key.intValue());
            return BackgroundController.this.mAdapter.getBackgroundForPage(row, col).mutate();
        }
    };
    private final ViewportDrawable mBaseLayer = new ViewportDrawable();
    private final ViewportDrawable mCrossfadeLayer = new ViewportDrawable();
    private final CrossfadeDrawable mBackground = new CrossfadeDrawable();
    private final Point mLastPageScrolled = new Point();
    private final Point mFadeSourcePage = new Point();
    private final Point mBaseSourcePage = new Point();

    private static int pack(int x, int y) {
        return (y << 16) | (65535 & x);
    }

    private static int pack(Point p) {
        return pack(p.x, p.y);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int unpackX(int key) {
        return 65535 & key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int unpackY(int key) {
        return key >>> 16;
    }

    private enum Direction {
        LEFT(-1, 0),
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        NONE(0, 0);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isVertical() {
            return this.y != 0;
        }

        boolean isHorizontal() {
            return this.x != 0;
        }

        static Direction fromOffset(float x, float y) {
            if (y != 0.0f) {
                return y > 0.0f ? DOWN : UP;
            }
            if (x != 0.0f) {
                return x > 0.0f ? RIGHT : LEFT;
            }
            return NONE;
        }
    }

    public BackgroundController() {
        this.mBackground.setFilterBitmap(true);
        this.mCrossfadeLayer.setFilterBitmap(true);
        this.mBaseLayer.setFilterBitmap(true);
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void attachTo(View v) {
        v.setBackground(this.mBackground);
    }

    @Override // android.support.wearable.view.GridViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            this.mDirection = Direction.NONE;
        }
    }

    @Override // android.support.wearable.view.GridViewPager.OnPageChangeListener
    public void onPageScrolled(int row, int column, float rowOffset, float colOffset, int rowOffsetPx, int colOffsetPx) {
        float relX;
        float relY;
        if (this.mDirection == Direction.NONE || !this.mCurrentPage.equals(this.mLastSelectedPage) || !this.mLastPageScrolled.equals(column, row)) {
            this.mLastPageScrolled.set(column, row);
            this.mCurrentPage.set(this.mLastSelectedPage.x, this.mLastSelectedPage.y);
            relX = 0.0f;
            relY = Func.clamp(row - this.mCurrentPage.y, -1, 0) + rowOffset;
            if (relY == 0.0f) {
                relX = Func.clamp(column - this.mCurrentPage.x, -1, 0) + colOffset;
            }
            this.mDirection = Direction.fromOffset(relX, relY);
            updateBackgrounds(this.mCurrentPage, this.mLastPageScrolled, this.mDirection, relX, relY);
        } else if (this.mDirection.isVertical()) {
            relX = 0.0f;
            relY = Func.clamp(row - this.mCurrentPage.y, -1, 0) + rowOffset;
        } else {
            relX = Func.clamp(column - this.mCurrentPage.x, -1, 0) + colOffset;
            relY = 0.0f;
        }
        this.mScrollRelativeX = relX;
        this.mScrollRelativeY = relY;
        this.mBaseLayer.setPosition(this.mBaseXPos + relX, this.mBaseYPos + relY);
        if (this.mUsingCrossfadeLayer) {
            float progress = this.mDirection.isVertical() ? Math.abs(relY) : Math.abs(relX);
            this.mBackground.setProgress(progress);
            this.mCrossfadeLayer.setPosition(this.mCrossfadeXPos + relX, this.mCrossfadeYPos + relY);
        }
    }

    private void updateBackgrounds(Point current, Point scrolling, Direction dir, float relX, float relY) {
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            Drawable base = updateBaseLayer(current, relX, relY);
            boolean overScrolling = ((float) current.x) + relX < 0.0f || ((float) current.y) + relY < 0.0f || ((float) scrolling.x) + relX > ((float) (this.mAdapter.getColumnCount(current.y) + (-1))) || ((float) scrolling.y) + relY > ((float) (this.mAdapter.getRowCount() + (-1)));
            if (this.mDirection == Direction.NONE || overScrolling) {
                this.mUsingCrossfadeLayer = false;
                this.mCrossfadeLayer.setDrawable(null);
                this.mBackground.setProgress(0.0f);
                return;
            }
            updateFadingLayer(current, scrolling, dir, relX, relY, base);
            return;
        }
        this.mUsingCrossfadeLayer = false;
        this.mBaseLayer.setDrawable(null);
        this.mCrossfadeLayer.setDrawable(null);
    }

    private Drawable updateBaseLayer(Point current, float relX, float relY) {
        Drawable base = this.mPageBackgrounds.get(Integer.valueOf(pack(current)));
        this.mBaseSourcePage.set(current.x, current.y);
        if (base == GridPagerAdapter.BACKGROUND_NONE) {
            base = this.mRowBackgrounds.get(Integer.valueOf(current.y));
            this.mBaseXSteps = this.mAdapter.getColumnCount(current.y) + 2;
            this.mBaseXPos = current.x + 1;
        } else {
            this.mBaseXSteps = 3;
            this.mBaseXPos = 1.0f;
        }
        this.mBaseYSteps = 3;
        this.mBaseYPos = 1.0f;
        this.mBaseLayer.setDrawable(base);
        this.mBaseLayer.setStops(this.mBaseXSteps, this.mBaseYSteps);
        this.mBaseLayer.setPosition(this.mBaseXPos + relX, this.mBaseYPos + relY);
        this.mBackground.setBase(this.mBaseLayer);
        return base;
    }

    private void updateFadingLayer(Point current, Point scrolling, Direction dir, float relX, float relY, Drawable base) {
        int crossfadeY = scrolling.y + (dir == Direction.DOWN ? 1 : 0);
        int crossfadeX = scrolling.x + (dir == Direction.RIGHT ? 1 : 0);
        if (crossfadeY != this.mCurrentPage.y) {
            crossfadeX = this.mAdapter.getCurrentColumnForRow(crossfadeY, current.x);
        }
        Drawable fade = this.mPageBackgrounds.get(Integer.valueOf(pack(crossfadeX, crossfadeY)));
        this.mFadeSourcePage.set(crossfadeX, crossfadeY);
        boolean fadeIsRowBg = false;
        if (fade == GridPagerAdapter.BACKGROUND_NONE) {
            fade = this.mRowBackgrounds.get(Integer.valueOf(crossfadeY));
            fadeIsRowBg = true;
        }
        if (base == fade) {
            this.mUsingCrossfadeLayer = false;
            this.mCrossfadeLayer.setDrawable(null);
            this.mBackground.setFading(null);
            this.mBackground.setProgress(0.0f);
            return;
        }
        if (fadeIsRowBg) {
            int physRow = Func.clamp(crossfadeY, 0, this.mAdapter.getRowCount() - 1);
            this.mFadeXSteps = this.mAdapter.getColumnCount(physRow) + 2;
            if (dir.isHorizontal()) {
                this.mCrossfadeXPos = current.x + 1;
            } else {
                this.mCrossfadeXPos = crossfadeX + 1;
            }
        } else {
            this.mFadeXSteps = 3;
            this.mCrossfadeXPos = 1 - dir.x;
        }
        this.mFadeYSteps = 3;
        this.mCrossfadeYPos = 1 - dir.y;
        this.mUsingCrossfadeLayer = true;
        this.mCrossfadeLayer.setDrawable(fade);
        this.mCrossfadeLayer.setStops(this.mFadeXSteps, this.mFadeYSteps);
        this.mCrossfadeLayer.setPosition(this.mCrossfadeXPos + relX, this.mCrossfadeYPos + relY);
        this.mBackground.setFading(this.mCrossfadeLayer);
    }

    @Override // android.support.wearable.view.GridViewPager.OnPageChangeListener
    public void onPageSelected(int row, int column) {
        this.mLastSelectedPage.set(column, row);
    }

    @Override // android.support.wearable.view.GridPagerAdapter.OnBackgroundChangeListener
    public void onPageBackgroundChanged(int row, int column) {
        this.mPageBackgrounds.remove(Integer.valueOf(pack(column, row)));
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            updateBackgrounds(this.mCurrentPage, this.mCurrentPage, Direction.NONE, this.mScrollRelativeX, this.mScrollRelativeY);
        }
    }

    @Override // android.support.wearable.view.GridPagerAdapter.OnBackgroundChangeListener
    public void onRowBackgroundChanged(int row) {
        this.mRowBackgrounds.remove(Integer.valueOf(row));
        if (this.mAdapter != null && this.mAdapter.getRowCount() > 0) {
            updateBackgrounds(this.mCurrentPage, this.mCurrentPage, Direction.NONE, this.mScrollRelativeX, this.mScrollRelativeY);
        }
    }

    @Override // android.support.wearable.view.GridViewPager.OnAdapterChangeListener
    public void onAdapterChanged(GridPagerAdapter oldAdapter, GridPagerAdapter newAdapter) {
        reset();
        this.mLastSelectedPage.set(0, 0);
        this.mCurrentPage.set(0, 0);
        this.mAdapter = newAdapter;
    }

    @Override // android.support.wearable.view.GridViewPager.OnAdapterChangeListener
    public void onDataSetChanged() {
        reset();
    }

    private void reset() {
        this.mDirection = Direction.NONE;
        this.mPageBackgrounds.evictAll();
        this.mRowBackgrounds.evictAll();
        this.mCrossfadeLayer.setDrawable(null);
        this.mBaseLayer.setDrawable(null);
    }
}
