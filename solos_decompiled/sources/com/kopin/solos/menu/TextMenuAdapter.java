package com.kopin.solos.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class TextMenuAdapter extends CustomMenuAdapter<TextMenuItem> {
    private LayoutInflater mInflater;
    private MenuCallback mMenuCallback;

    public interface MenuCallback {
        void closingMenu();

        void openingMenu();
    }

    public enum TextMenuType {
        MEDIUM,
        SMALL
    }

    public TextMenuAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void addMenuItem(TextMenuItem menuItem) {
        menuItem.mMenuAdapter = this;
        super.addMenuItem(menuItem);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void doCleanUp() {
        super.doCleanUp();
        for (int i = 0; i < getCount(); i++) {
            getMenuItem(i).mMenuAdapter = null;
        }
        if (this.mMenuCallback != null) {
            this.mMenuCallback.closingMenu();
        }
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        TextMenuItem item = getMenuItem(position);
        if (item.getType() == TextMenuType.MEDIUM) {
            view = getViewMedium(item, convertView, parent);
        } else if (item.getType() == TextMenuType.SMALL) {
            view = getViewSmall(item, convertView, parent);
        }
        if (this.mMenuCallback != null && view != null) {
            this.mMenuCallback.openingMenu();
        }
        return view;
    }

    private View getViewSmall(TextMenuItem item, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.menu_warning, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_message);
            viewHolder.type = item.getType();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.type != item.getType()) {
                return getViewSmall(item, null, parent);
            }
        }
        viewHolder.setEnabled(item.isEnabled());
        viewHolder.title.setText(item.getTitle());
        return convertView;
    }

    private View getViewMedium(TextMenuItem item, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.sensor_menu_item_message, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_message);
            viewHolder.type = item.getType();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.type != item.getType()) {
                return getViewMedium(item, null, parent);
            }
        }
        viewHolder.setEnabled(item.isEnabled());
        viewHolder.title.setText(item.getTitle());
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextMenuType type;

        private ViewHolder() {
        }

        public void setEnabled(boolean enabled) {
            this.title.setEnabled(enabled);
        }
    }

    public static class TextMenuItem extends CustomMenuItem {
        private TextMenuAdapter mMenuAdapter;
        private TextMenuType mType;

        public TextMenuItem(String title, int id, TextMenuType type) {
            super(title, 0, id);
            this.mType = type;
        }

        public TextMenuType getType() {
            return this.mType;
        }

        @Override // com.kopin.solos.menu.CustomMenuItem
        public void onDataChanged() {
            if (this.mMenuAdapter != null) {
                this.mMenuAdapter.notifyDataSetChanged();
            }
        }

        public int hashCode() {
            return getId();
        }

        public boolean equals(Object o) {
            return o != null && (o == this || ((o instanceof TextMenuItem) && ((TextMenuItem) o).getId() == getId()));
        }
    }

    public void addCallback(MenuCallback menuCallback) {
        this.mMenuCallback = menuCallback;
    }
}
