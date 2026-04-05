package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
class WearableActionDrawerMenu implements Menu {
    private final Context mContext;
    private final WearableActionDrawerMenuListener mListener;
    private final List<WearableActionDrawerMenuItem> mItems = new ArrayList();
    private final WearableActionDrawerMenuItem.MenuItemChangedListener mItemChangedListener = new WearableActionDrawerMenuItem.MenuItemChangedListener() { // from class: android.support.wearable.view.drawer.WearableActionDrawerMenu.1
        @Override // android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuItem.MenuItemChangedListener
        public void itemChanged(WearableActionDrawerMenuItem item) {
            for (int i = 0; i < WearableActionDrawerMenu.this.mItems.size(); i++) {
                if (WearableActionDrawerMenu.this.mItems.get(i) == item) {
                    WearableActionDrawerMenu.this.mListener.menuItemChanged(i);
                }
            }
        }
    };

    interface WearableActionDrawerMenuListener {
        void menuChanged();

        void menuItemAdded(int i);

        void menuItemChanged(int i);

        void menuItemRemoved(int i);
    }

    public WearableActionDrawerMenu(Context context, WearableActionDrawerMenuListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence title) {
        return add(0, 0, 0, title);
    }

    @Override // android.view.Menu
    public MenuItem add(int titleRes) {
        return add(0, 0, 0, titleRes);
    }

    @Override // android.view.Menu
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return add(groupId, itemId, order, this.mContext.getResources().getString(titleRes));
    }

    @Override // android.view.Menu
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        WearableActionDrawerMenuItem item = new WearableActionDrawerMenuItem(this.mContext, itemId, title, this.mItemChangedListener);
        this.mItems.add(item);
        this.mListener.menuItemAdded(this.mItems.size() - 1);
        return item;
    }

    @Override // android.view.Menu
    public void clear() {
        this.mItems.clear();
        this.mListener.menuChanged();
    }

    @Override // android.view.Menu
    public void removeItem(int id) {
        int index = findItemIndex(id);
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.remove(index);
            this.mListener.menuItemRemoved(index);
        }
    }

    @Override // android.view.Menu
    public MenuItem findItem(int id) {
        int index = findItemIndex(id);
        if (index < 0 || index >= this.mItems.size()) {
            return null;
        }
        return this.mItems.get(index);
    }

    @Override // android.view.Menu
    public int size() {
        return this.mItems.size();
    }

    @Override // android.view.Menu
    @Nullable
    public MenuItem getItem(int index) {
        if (index < 0 || index >= this.mItems.size()) {
            return null;
        }
        return this.mItems.get(index);
    }

    private int findItemIndex(int id) {
        List<WearableActionDrawerMenuItem> items = this.mItems;
        int itemCount = items.size();
        for (int i = 0; i < itemCount; i++) {
            if (items.get(i).getItemId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override // android.view.Menu
    public void close() {
        throw new UnsupportedOperationException("close is not implemented");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence title) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int titleRes) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        throw new UnsupportedOperationException("addSubMenu is not implemented");
    }

    @Override // android.view.Menu
    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        throw new UnsupportedOperationException("addIntentOptions is not implemented");
    }

    @Override // android.view.Menu
    public void removeGroup(int groupId) {
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        throw new UnsupportedOperationException("setGroupCheckable is not implemented");
    }

    @Override // android.view.Menu
    public void setGroupVisible(int group, boolean visible) {
        throw new UnsupportedOperationException("setGroupVisible is not implemented");
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int group, boolean enabled) {
        throw new UnsupportedOperationException("setGroupEnabled is not implemented");
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        return false;
    }

    @Override // android.view.Menu
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        throw new UnsupportedOperationException("performShortcut is not implemented");
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return false;
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int id, int flags) {
        throw new UnsupportedOperationException("performIdentifierAction is not implemented");
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean isQwerty) {
    }

    public static final class WearableActionDrawerMenuItem implements MenuItem {
        private MenuItem.OnMenuItemClickListener mClickListener;
        private final Context mContext;
        private Drawable mIconDrawable;
        private final int mId;
        private final MenuItemChangedListener mItemChangedListener;
        private CharSequence mTitle;

        private interface MenuItemChangedListener {
            void itemChanged(WearableActionDrawerMenuItem wearableActionDrawerMenuItem);
        }

        public WearableActionDrawerMenuItem(Context context, int id, CharSequence title, MenuItemChangedListener listener) {
            this.mContext = context;
            this.mId = id;
            this.mTitle = title;
            this.mItemChangedListener = listener;
        }

        @Override // android.view.MenuItem
        public int getItemId() {
            return this.mId;
        }

        @Override // android.view.MenuItem
        public MenuItem setTitle(CharSequence title) {
            this.mTitle = title;
            if (this.mItemChangedListener != null) {
                this.mItemChangedListener.itemChanged(this);
            }
            return this;
        }

        @Override // android.view.MenuItem
        public MenuItem setTitle(int title) {
            return setTitle(this.mContext.getResources().getString(title));
        }

        @Override // android.view.MenuItem
        public CharSequence getTitle() {
            return this.mTitle;
        }

        @Override // android.view.MenuItem
        public MenuItem setIcon(Drawable icon) {
            this.mIconDrawable = icon;
            if (this.mItemChangedListener != null) {
                this.mItemChangedListener.itemChanged(this);
            }
            return this;
        }

        @Override // android.view.MenuItem
        public MenuItem setIcon(int iconRes) {
            return setIcon(this.mContext.getResources().getDrawable(iconRes));
        }

        @Override // android.view.MenuItem
        public Drawable getIcon() {
            return this.mIconDrawable;
        }

        @Override // android.view.MenuItem
        public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener menuItemClickListener) {
            this.mClickListener = menuItemClickListener;
            return this;
        }

        @Override // android.view.MenuItem
        public int getGroupId() {
            return 0;
        }

        @Override // android.view.MenuItem
        public int getOrder() {
            return 0;
        }

        @Override // android.view.MenuItem
        public MenuItem setTitleCondensed(CharSequence title) {
            return this;
        }

        @Override // android.view.MenuItem
        public CharSequence getTitleCondensed() {
            return null;
        }

        @Override // android.view.MenuItem
        public MenuItem setIntent(Intent intent) {
            throw new UnsupportedOperationException("setIntent is not implemented");
        }

        @Override // android.view.MenuItem
        public Intent getIntent() {
            return null;
        }

        @Override // android.view.MenuItem
        public MenuItem setShortcut(char numericChar, char alphaChar) {
            throw new UnsupportedOperationException("setShortcut is not implemented");
        }

        @Override // android.view.MenuItem
        public MenuItem setNumericShortcut(char numericChar) {
            return this;
        }

        @Override // android.view.MenuItem
        public char getNumericShortcut() {
            return (char) 0;
        }

        @Override // android.view.MenuItem
        public MenuItem setAlphabeticShortcut(char alphaChar) {
            return this;
        }

        @Override // android.view.MenuItem
        public char getAlphabeticShortcut() {
            return (char) 0;
        }

        @Override // android.view.MenuItem
        public MenuItem setCheckable(boolean checkable) {
            return this;
        }

        @Override // android.view.MenuItem
        public boolean isCheckable() {
            return false;
        }

        @Override // android.view.MenuItem
        public MenuItem setChecked(boolean checked) {
            return this;
        }

        @Override // android.view.MenuItem
        public boolean isChecked() {
            return false;
        }

        @Override // android.view.MenuItem
        public MenuItem setVisible(boolean visible) {
            return this;
        }

        @Override // android.view.MenuItem
        public boolean isVisible() {
            return false;
        }

        @Override // android.view.MenuItem
        public MenuItem setEnabled(boolean enabled) {
            return this;
        }

        @Override // android.view.MenuItem
        public boolean isEnabled() {
            return false;
        }

        @Override // android.view.MenuItem
        public boolean hasSubMenu() {
            return false;
        }

        @Override // android.view.MenuItem
        public SubMenu getSubMenu() {
            return null;
        }

        @Override // android.view.MenuItem
        public ContextMenu.ContextMenuInfo getMenuInfo() {
            return null;
        }

        @Override // android.view.MenuItem
        public void setShowAsAction(int actionEnum) {
            throw new UnsupportedOperationException("setShowAsAction is not implemented");
        }

        @Override // android.view.MenuItem
        public MenuItem setShowAsActionFlags(int actionEnum) {
            throw new UnsupportedOperationException("setShowAsActionFlags is not implemented");
        }

        @Override // android.view.MenuItem
        public MenuItem setActionView(View view) {
            throw new UnsupportedOperationException("setActionView is not implemented");
        }

        @Override // android.view.MenuItem
        public MenuItem setActionView(int resId) {
            throw new UnsupportedOperationException("setActionView is not implemented");
        }

        @Override // android.view.MenuItem
        public View getActionView() {
            return null;
        }

        @Override // android.view.MenuItem
        public MenuItem setActionProvider(ActionProvider actionProvider) {
            throw new UnsupportedOperationException("setActionProvider is not implemented");
        }

        @Override // android.view.MenuItem
        public ActionProvider getActionProvider() {
            return null;
        }

        @Override // android.view.MenuItem
        public boolean expandActionView() {
            throw new UnsupportedOperationException("expandActionView is not implemented");
        }

        @Override // android.view.MenuItem
        public boolean collapseActionView() {
            throw new UnsupportedOperationException("collapseActionView is not implemented");
        }

        @Override // android.view.MenuItem
        public boolean isActionViewExpanded() {
            throw new UnsupportedOperationException("isActionViewExpanded is not implemented");
        }

        @Override // android.view.MenuItem
        public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener listener) {
            throw new UnsupportedOperationException("setOnActionExpandListener is not implemented");
        }

        boolean invoke() {
            return this.mClickListener != null && this.mClickListener.onMenuItemClick(this);
        }
    }
}
