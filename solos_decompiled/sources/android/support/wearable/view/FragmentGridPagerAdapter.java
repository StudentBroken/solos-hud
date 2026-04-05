package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentCallbacks2;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.GridPageOptions;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
@Deprecated
public abstract class FragmentGridPagerAdapter extends GridPagerAdapter {
    private static final int MAX_ROWS = 65535;
    private static final GridPageOptions.BackgroundListener NOOP_BACKGROUND_OBSERVER = new GridPageOptions.BackgroundListener() { // from class: android.support.wearable.view.FragmentGridPagerAdapter.1
        @Override // android.support.wearable.view.GridPageOptions.BackgroundListener
        public void notifyBackgroundChanged() {
        }
    };
    private FragmentTransaction mCurTransaction;
    private final FragmentManager mFragmentManager;
    private final Map<String, Point> mFragmentPositions = new HashMap();
    private final Map<Point, String> mFragmentTags = new HashMap();

    public abstract Fragment getFragment(int i, int i2);

    public FragmentGridPagerAdapter(FragmentManager fm) {
        this.mFragmentManager = fm;
    }

    private static String makeFragmentName(int viewId, long id) {
        return new StringBuilder(49).append("android:switcher:").append(viewId).append(":").append(id).toString();
    }

    public long getFragmentId(int row, int column) {
        return (65535 * column) + row;
    }

    @Override // android.support.wearable.view.GridPagerAdapter
    public Fragment instantiateItem(ViewGroup container, int row, int column) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        long itemId = getFragmentId(row, column);
        String tag = makeFragmentName(container.getId(), itemId);
        Fragment fragment = this.mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = getFragment(row, column);
            this.mCurTransaction.add(container.getId(), fragment, tag);
        } else {
            restoreFragment(fragment, this.mCurTransaction);
        }
        Point position = new Point(column, row);
        this.mFragmentTags.put(position, tag);
        this.mFragmentPositions.put(tag, position);
        if (fragment instanceof GridPageOptions) {
            GridPageOptions backgroundProvider = (GridPageOptions) fragment;
            backgroundProvider.setBackgroundListener(new BackgroundObserver(tag));
        }
        return fragment;
    }

    protected void restoreFragment(Fragment fragment, FragmentTransaction transaction) {
    }

    private class BackgroundObserver implements GridPageOptions.BackgroundListener {
        private final String mTag;

        private BackgroundObserver(String tag) {
            this.mTag = tag;
        }

        @Override // android.support.wearable.view.GridPageOptions.BackgroundListener
        public void notifyBackgroundChanged() {
            Point pos = (Point) FragmentGridPagerAdapter.this.mFragmentPositions.get(this.mTag);
            if (pos != null) {
                FragmentGridPagerAdapter.this.notifyPageBackgroundChanged(pos.y, pos.x);
            }
        }
    }

    @Override // android.support.wearable.view.GridPagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == ((Fragment) object).getView();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.wearable.view.GridPagerAdapter
    public void destroyItem(ViewGroup container, int row, int column, Object object) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        Fragment fragment = (Fragment) object;
        if (fragment instanceof GridPageOptions) {
            ((GridPageOptions) fragment).setBackgroundListener(NOOP_BACKGROUND_OBSERVER);
        }
        removeFragment(fragment, this.mCurTransaction);
    }

    protected void removeFragment(Fragment fragment, FragmentTransaction transaction) {
        transaction.remove(fragment);
    }

    @Override // android.support.wearable.view.GridPagerAdapter
    protected void applyItemPosition(Object object, Point position) {
        if (position != GridPagerAdapter.POSITION_UNCHANGED) {
            Fragment fragment = (Fragment) object;
            if (fragment.getTag().equals(this.mFragmentTags.get(position))) {
                this.mFragmentTags.remove(position);
            }
            if (position == GridPagerAdapter.POSITION_NONE) {
                this.mFragmentPositions.remove(fragment.getTag());
            } else {
                this.mFragmentPositions.put(fragment.getTag(), position);
                this.mFragmentTags.put(position, fragment.getTag());
            }
        }
    }

    public final Drawable getFragmentBackground(int row, int column) {
        String tag = this.mFragmentTags.get(new Point(column, row));
        ComponentCallbacks2 componentCallbacks2FindFragmentByTag = this.mFragmentManager.findFragmentByTag(tag);
        Drawable bg = BACKGROUND_NONE;
        if (componentCallbacks2FindFragmentByTag instanceof GridPageOptions) {
            Drawable bg2 = ((GridPageOptions) componentCallbacks2FindFragmentByTag).getBackground();
            return bg2;
        }
        return bg;
    }

    @Override // android.support.wearable.view.GridPagerAdapter
    public Drawable getBackgroundForPage(int row, int column) {
        return getFragmentBackground(row, column);
    }

    @Override // android.support.wearable.view.GridPagerAdapter
    public void finishUpdate(ViewGroup container) {
        if (this.mFragmentManager.isDestroyed()) {
            this.mCurTransaction = null;
        } else if (this.mCurTransaction != null) {
            this.mCurTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }
    }

    public Fragment findExistingFragment(int row, int column) {
        String tag = this.mFragmentTags.get(new Point(column, row));
        if (tag != null) {
            return this.mFragmentManager.findFragmentByTag(tag);
        }
        return null;
    }
}
