package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

/* JADX INFO: loaded from: classes27.dex */
public final class MenuItemCompat {
    static final MenuVersionImpl IMPL;

    @Deprecated
    public static final int SHOW_AS_ACTION_ALWAYS = 2;

    @Deprecated
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;

    @Deprecated
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;

    @Deprecated
    public static final int SHOW_AS_ACTION_NEVER = 0;

    @Deprecated
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    interface MenuVersionImpl {
        int getAlphabeticModifiers(MenuItem menuItem);

        CharSequence getContentDescription(MenuItem menuItem);

        ColorStateList getIconTintList(MenuItem menuItem);

        PorterDuff.Mode getIconTintMode(MenuItem menuItem);

        int getNumericModifiers(MenuItem menuItem);

        CharSequence getTooltipText(MenuItem menuItem);

        void setAlphabeticShortcut(MenuItem menuItem, char c, int i);

        void setContentDescription(MenuItem menuItem, CharSequence charSequence);

        void setIconTintList(MenuItem menuItem, ColorStateList colorStateList);

        void setIconTintMode(MenuItem menuItem, PorterDuff.Mode mode);

        void setNumericShortcut(MenuItem menuItem, char c, int i);

        void setShortcut(MenuItem menuItem, char c, char c2, int i, int i2);

        void setTooltipText(MenuItem menuItem, CharSequence charSequence);
    }

    @Deprecated
    public interface OnActionExpandListener {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    static class MenuItemCompatBaseImpl implements MenuVersionImpl {
        MenuItemCompatBaseImpl() {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setContentDescription(MenuItem item, CharSequence contentDescription) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public CharSequence getContentDescription(MenuItem item) {
            return null;
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setTooltipText(MenuItem item, CharSequence tooltipText) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public CharSequence getTooltipText(MenuItem item) {
            return null;
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setShortcut(MenuItem item, char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setAlphabeticShortcut(MenuItem item, char alphaChar, int alphaModifiers) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public int getAlphabeticModifiers(MenuItem item) {
            return 0;
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setNumericShortcut(MenuItem item, char numericChar, int numericModifiers) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public int getNumericModifiers(MenuItem item) {
            return 0;
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setIconTintList(MenuItem item, ColorStateList tint) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public ColorStateList getIconTintList(MenuItem item) {
            return null;
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setIconTintMode(MenuItem item, PorterDuff.Mode tintMode) {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public PorterDuff.Mode getIconTintMode(MenuItem item) {
            return null;
        }
    }

    @RequiresApi(26)
    static class MenuItemCompatApi26Impl extends MenuItemCompatBaseImpl {
        MenuItemCompatApi26Impl() {
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setContentDescription(MenuItem item, CharSequence contentDescription) {
            item.setContentDescription(contentDescription);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public CharSequence getContentDescription(MenuItem item) {
            return item.getContentDescription();
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setTooltipText(MenuItem item, CharSequence tooltipText) {
            item.setTooltipText(tooltipText);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public CharSequence getTooltipText(MenuItem item) {
            return item.getTooltipText();
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setShortcut(MenuItem item, char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
            item.setShortcut(numericChar, alphaChar, numericModifiers, alphaModifiers);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setAlphabeticShortcut(MenuItem item, char alphaChar, int alphaModifiers) {
            item.setAlphabeticShortcut(alphaChar, alphaModifiers);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public int getAlphabeticModifiers(MenuItem item) {
            return item.getAlphabeticModifiers();
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setNumericShortcut(MenuItem item, char numericChar, int numericModifiers) {
            item.setNumericShortcut(numericChar, numericModifiers);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public int getNumericModifiers(MenuItem item) {
            return item.getNumericModifiers();
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setIconTintList(MenuItem item, ColorStateList tint) {
            item.setIconTintList(tint);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public ColorStateList getIconTintList(MenuItem item) {
            return item.getIconTintList();
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public void setIconTintMode(MenuItem item, PorterDuff.Mode tintMode) {
            item.setIconTintMode(tintMode);
        }

        @Override // android.support.v4.view.MenuItemCompat.MenuItemCompatBaseImpl, android.support.v4.view.MenuItemCompat.MenuVersionImpl
        public PorterDuff.Mode getIconTintMode(MenuItem item) {
            return item.getIconTintMode();
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 26) {
            IMPL = new MenuItemCompatApi26Impl();
        } else {
            IMPL = new MenuItemCompatBaseImpl();
        }
    }

    @Deprecated
    public static void setShowAsAction(MenuItem item, int actionEnum) {
        item.setShowAsAction(actionEnum);
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem item, View view) {
        return item.setActionView(view);
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem item, int resId) {
        return item.setActionView(resId);
    }

    @Deprecated
    public static View getActionView(MenuItem item) {
        return item.getActionView();
    }

    public static MenuItem setActionProvider(MenuItem item, ActionProvider provider) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).setSupportActionProvider(provider);
        }
        Log.w(TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return item;
    }

    public static ActionProvider getActionProvider(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getSupportActionProvider();
        }
        Log.w(TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }

    @Deprecated
    public static boolean expandActionView(MenuItem item) {
        return item.expandActionView();
    }

    @Deprecated
    public static boolean collapseActionView(MenuItem item) {
        return item.collapseActionView();
    }

    @Deprecated
    public static boolean isActionViewExpanded(MenuItem item) {
        return item.isActionViewExpanded();
    }

    @Deprecated
    public static MenuItem setOnActionExpandListener(MenuItem item, final OnActionExpandListener listener) {
        return item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() { // from class: android.support.v4.view.MenuItemCompat.1
            @Override // android.view.MenuItem.OnActionExpandListener
            public boolean onMenuItemActionExpand(MenuItem item2) {
                return listener.onMenuItemActionExpand(item2);
            }

            @Override // android.view.MenuItem.OnActionExpandListener
            public boolean onMenuItemActionCollapse(MenuItem item2) {
                return listener.onMenuItemActionCollapse(item2);
            }
        });
    }

    public static void setContentDescription(MenuItem item, CharSequence contentDescription) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setContentDescription(contentDescription);
        } else {
            IMPL.setContentDescription(item, contentDescription);
        }
    }

    public static CharSequence getContentDescription(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getContentDescription() : IMPL.getContentDescription(item);
    }

    public static void setTooltipText(MenuItem item, CharSequence tooltipText) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setTooltipText(tooltipText);
        } else {
            IMPL.setTooltipText(item, tooltipText);
        }
    }

    public static CharSequence getTooltipText(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getTooltipText() : IMPL.getTooltipText(item);
    }

    public static void setShortcut(MenuItem item, char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setShortcut(numericChar, alphaChar, numericModifiers, alphaModifiers);
        } else {
            IMPL.setShortcut(item, numericChar, alphaChar, numericModifiers, alphaModifiers);
        }
    }

    public static void setNumericShortcut(MenuItem item, char numericChar, int numericModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setNumericShortcut(numericChar, numericModifiers);
        } else {
            IMPL.setNumericShortcut(item, numericChar, numericModifiers);
        }
    }

    public static int getNumericModifiers(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getNumericModifiers() : IMPL.getNumericModifiers(item);
    }

    public static void setAlphabeticShortcut(MenuItem item, char alphaChar, int alphaModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setAlphabeticShortcut(alphaChar, alphaModifiers);
        } else {
            IMPL.setAlphabeticShortcut(item, alphaChar, alphaModifiers);
        }
    }

    public static int getAlphabeticModifiers(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getAlphabeticModifiers() : IMPL.getAlphabeticModifiers(item);
    }

    public static void setIconTintList(MenuItem item, ColorStateList tint) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setIconTintList(tint);
        } else {
            IMPL.setIconTintList(item, tint);
        }
    }

    public static ColorStateList getIconTintList(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getIconTintList() : IMPL.getIconTintList(item);
    }

    public static void setIconTintMode(MenuItem item, PorterDuff.Mode tintMode) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setIconTintMode(tintMode);
        } else {
            IMPL.setIconTintMode(item, tintMode);
        }
    }

    public static PorterDuff.Mode getIconTintMode(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getIconTintMode() : IMPL.getIconTintMode(item);
    }

    private MenuItemCompat() {
    }
}
