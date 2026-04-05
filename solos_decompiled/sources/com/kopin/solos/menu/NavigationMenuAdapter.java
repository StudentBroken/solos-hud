package com.kopin.solos.menu;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;

/* JADX INFO: loaded from: classes24.dex */
public class NavigationMenuAdapter extends CustomMenuAdapter<NavigationMenuItem> {
    private LayoutInflater mInflater;

    public NavigationMenuAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void addMenuItem(NavigationMenuItem menuItem) {
        menuItem.mMenuAdapter = this;
        super.addMenuItem(menuItem);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void doCleanUp() {
        super.doCleanUp();
        for (int i = 0; i < getCount(); i++) {
            getMenuItem(i).mMenuAdapter = null;
        }
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        NavigationMenuItem item = getMenuItem(position);
        return getViewItem(item, convertView, parent, position);
    }

    private View getViewItem(NavigationMenuItem item, View convertView, ViewGroup parent, int position) {
        if (convertView == null) {
            View convertView2 = this.mInflater.inflate(R.layout.navigation_menu_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView2.findViewById(R.id.navigation_item_description);
            viewHolder.imageView = (ImageView) convertView2.findViewById(R.id.menu_icon);
            viewHolder.imageView2 = (ImageView) convertView2.findViewById(R.id.indicator);
            convertView2.setTag(viewHolder);
            if (item.getDrawableId() != 0) {
                viewHolder.imageView.setVisibility(0);
                viewHolder.imageView.setColorFilter(getContext().getResources().getColor(R.color.solos_white), PorterDuff.Mode.SRC_ATOP);
                viewHolder.imageView.setImageResource(item.getDrawableId());
            } else {
                viewHolder.imageView.setVisibility(8);
            }
            if (item.getIndicatorDrawable() != 0) {
                viewHolder.imageView2.setVisibility(0);
                viewHolder.imageView2.setImageResource(item.getIndicatorDrawable());
                viewHolder.title.setTextColor(getContext().getColor(R.color.solos_orange));
                viewHolder.imageView.setColorFilter(getContext().getColor(R.color.solos_orange), PorterDuff.Mode.SRC_ATOP);
                viewHolder.imageView2.setColorFilter(getContext().getColor(R.color.solos_orange), PorterDuff.Mode.SRC_ATOP);
            } else {
                viewHolder.imageView2.setVisibility(8);
            }
            viewHolder.setEnabled(item.isEnabled());
            viewHolder.title.setText(item.getTitle());
            return convertView2;
        }
        return getViewItem(item, null, parent, position);
    }

    private static class ViewHolder {
        ImageView imageView;
        ImageView imageView2;
        TextView title;

        private ViewHolder() {
        }

        public void setEnabled(boolean enabled) {
            this.title.setEnabled(enabled);
        }
    }

    public static class NavigationMenuItem extends CustomMenuItem {
        private int mIndicatorDrawable;
        private NavigationMenuAdapter mMenuAdapter;

        public NavigationMenuItem(String title, int drawableId, int id) {
            super(title, drawableId, id);
        }

        public NavigationMenuItem(String title, int drawableId, int id, int indicatorDrawable) {
            super(title, drawableId, id);
            this.mIndicatorDrawable = indicatorDrawable;
        }

        @Override // com.kopin.solos.menu.CustomMenuItem
        public void onDataChanged() {
            if (this.mMenuAdapter != null) {
                this.mMenuAdapter.notifyDataSetChanged();
            }
        }

        int getIndicatorDrawable() {
            return this.mIndicatorDrawable;
        }
    }
}
