package android.support.wearable.view;

import android.annotation.TargetApi;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public abstract class GridPagerAdapter {
    public static final int OPTION_DISABLE_PARALLAX = 1;
    public static final int PAGE_DEFAULT_OPTIONS = 0;
    private final DataSetObservable mObservable = new DataSetObservable();
    private OnBackgroundChangeListener mOnBackgroundChangeListener;
    public static final Drawable BACKGROUND_NONE = new NoOpDrawable();
    public static final Point POSITION_NONE = new Point(-1, -1);
    public static final Point POSITION_UNCHANGED = new Point(-2, -2);

    interface OnBackgroundChangeListener {
        void onPageBackgroundChanged(int i, int i2);

        void onRowBackgroundChanged(int i);
    }

    public abstract void destroyItem(ViewGroup viewGroup, int i, int i2, Object obj);

    public abstract int getColumnCount(int i);

    public abstract int getRowCount();

    public abstract Object instantiateItem(ViewGroup viewGroup, int i, int i2);

    public abstract boolean isViewFromObject(View view, Object obj);

    public int getCurrentColumnForRow(int row, int currentColumn) {
        return 0;
    }

    public void setCurrentColumnForRow(int row, int currentColumn) {
    }

    public void startUpdate(ViewGroup container) {
    }

    public void finishUpdate(ViewGroup container) {
    }

    public Drawable getBackgroundForRow(int row) {
        return BACKGROUND_NONE;
    }

    public Drawable getBackgroundForPage(int row, int column) {
        return BACKGROUND_NONE;
    }

    public int getOptionsForPage(int row, int column) {
        return 0;
    }

    public void notifyPageBackgroundChanged(int row, int column) {
        if (this.mOnBackgroundChangeListener != null) {
            this.mOnBackgroundChangeListener.onPageBackgroundChanged(row, column);
        }
    }

    public void notifyRowBackgroundChanged(int row) {
        if (this.mOnBackgroundChangeListener != null) {
            this.mOnBackgroundChangeListener.onRowBackgroundChanged(row);
        }
    }

    void setOnBackgroundChangeListener(OnBackgroundChangeListener listener) {
        this.mOnBackgroundChangeListener = listener;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        this.mObservable.notifyChanged();
    }

    public Point getItemPosition(Object object) {
        return POSITION_NONE;
    }

    protected void applyItemPosition(Object object, Point position) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable savedState, ClassLoader classLoader) {
    }

    private static final class NoOpDrawable extends Drawable {
        private NoOpDrawable() {
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 0;
        }
    }
}
