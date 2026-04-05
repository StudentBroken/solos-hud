package com.kopin.solos.menu;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.kopin.solos.R;
import com.kopin.solos.menu.CustomMenuItem;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class CustomActionProvider<T extends CustomMenuItem> extends ActionProvider {
    private ActionView mActionView;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<T> mList;
    private CustomMenuAdapter<T> mMenuAdapter;
    private OnItemClickListener mOnItemClickListener;
    private OnPrepareListener<T> mOnPrepareListener;
    private PopupWindow mPopupArrow;
    private PopupWindow mPopupWindow;

    public interface ActionView {
        View getView();

        void setActive(boolean z);

        void setActiveColor(int i);

        void setImageDrawable(Drawable drawable);

        void setInactiveColor(int i);
    }

    public interface OnItemClickListener {
        void onItemClick(int i, CustomMenuItem customMenuItem);
    }

    public interface OnPrepareListener<T extends CustomMenuItem> {
        void onPrepare(CustomActionProvider<T> customActionProvider);
    }

    public CustomActionProvider(Context context) {
        super(context);
        this.mList = new ArrayList();
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // android.view.ActionProvider
    public View onCreateActionView() {
        return null;
    }

    @Override // android.view.ActionProvider
    public View onCreateActionView(MenuItem forItem) {
        if (this.mActionView == null) {
            this.mActionView = new DefaultActionView(this.mContext);
        }
        this.mActionView.setImageDrawable(forItem.getIcon());
        final View view = this.mActionView.getView();
        view.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.menu.CustomActionProvider.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (CustomActionProvider.this.mPopupWindow == null) {
                    CustomActionProvider.this.showDropDownMenu(view);
                }
            }
        });
        return view;
    }

    public void addMenuItem(T menuItem) {
        if (!this.mList.contains(menuItem)) {
            this.mList.add(menuItem);
            if (this.mMenuAdapter != null) {
                this.mMenuAdapter.addMenuItem(menuItem);
            }
        }
    }

    public void clear() {
        this.mList.clear();
        if (this.mMenuAdapter != null) {
            this.mMenuAdapter.clear();
        }
    }

    public int getCount() {
        return this.mList.size();
    }

    public void removeMenuItem(T menuItem) {
        this.mList.remove(menuItem);
        if (this.mMenuAdapter != null) {
            this.mMenuAdapter.removeMenuItem(menuItem);
        }
    }

    public void setOnPrepareListener(OnPrepareListener<T> onPrepareListener) {
        this.mOnPrepareListener = onPrepareListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setMenuAdapter(CustomMenuAdapter<T> menuAdapter) {
        this.mMenuAdapter = menuAdapter;
        for (T menuItem : this.mList) {
            this.mMenuAdapter.addMenuItem(menuItem);
        }
    }

    public void setActionView(ActionView actionView) {
        this.mActionView = actionView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDropDownMenu(View anchor) {
        if (this.mMenuAdapter == null) {
            throw new NullPointerException("No menu adapter provided. Use setMenuAdapter() to specify one.");
        }
        if (this.mOnPrepareListener != null) {
            this.mOnPrepareListener.onPrepare(this);
        }
        if (!this.mMenuAdapter.isEmpty()) {
            ListView listView = (ListView) this.mInflater.inflate(R.layout.menu_list, (ViewGroup) null);
            listView.setScrollContainer(false);
            listView.setHorizontalScrollBarEnabled(false);
            listView.setAdapter((ListAdapter) this.mMenuAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.kopin.solos.menu.CustomActionProvider.2
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CustomMenuItem menuItem = CustomActionProvider.this.mMenuAdapter.getMenuItem(position);
                    if (CustomActionProvider.this.mOnItemClickListener != null && menuItem.isEnabled()) {
                        CustomActionProvider.this.mOnItemClickListener.onItemClick(position, menuItem);
                        if (menuItem.isDismissOnTap()) {
                            CustomActionProvider.this.mPopupWindow.dismiss();
                            CustomActionProvider.this.mPopupArrow.dismiss();
                        }
                    }
                }
            });
            int sideMargins = (int) Math.ceil(this.mContext.getResources().getDimension(R.dimen.menu_side_margins));
            int dividerHeight = (int) Math.ceil(this.mContext.getResources().getDimension(R.dimen.menu_divider_height));
            Point measurements = this.mMenuAdapter.getMeasurements(sideMargins, dividerHeight);
            int arrowBoxSize = (int) this.mContext.getResources().getDimension(R.dimen.menu_arrow_box_size);
            this.mPopupArrow = new PopupWindow(this.mInflater.inflate(R.layout.arrow_layout, (ViewGroup) null), arrowBoxSize, arrowBoxSize);
            this.mPopupArrow.setFocusable(false);
            this.mPopupArrow.setOutsideTouchable(false);
            this.mPopupWindow = new PopupWindow(listView, measurements.x, measurements.y);
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setOutsideTouchable(true);
            this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            this.mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.kopin.solos.menu.CustomActionProvider.3
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    CustomActionProvider.this.mPopupWindow = null;
                    CustomActionProvider.this.mPopupArrow.dismiss();
                    CustomActionProvider.this.mMenuAdapter.doCleanUp();
                }
            });
            int yOffsetArrow = -((int) (((double) (this.mContext.getResources().getDimension(R.dimen.menu_vertical_offset) + arrowBoxSize)) / 2.0d));
            int yOffsetList = (int) (((double) ((-this.mContext.getResources().getDimension(R.dimen.menu_vertical_offset)) + arrowBoxSize)) / 2.0d);
            this.mPopupArrow.showAsDropDown(anchor, (anchor.getWidth() - arrowBoxSize) / 2, yOffsetArrow);
            this.mPopupWindow.showAsDropDown(anchor, 0, yOffsetList);
        }
    }

    public static class DefaultActionView implements ActionView {
        private boolean isActive = false;
        private int mActiveColor;
        private ImageView mImageView;
        private int mInactiveColor;
        private View mView;

        public DefaultActionView(Context context) {
            int color = context.getResources().getColor(R.color.unfocused_grey);
            this.mInactiveColor = color;
            this.mActiveColor = color;
            this.mView = LayoutInflater.from(context).inflate(R.layout.default_action_view, (ViewGroup) null);
            this.mImageView = (ImageView) this.mView.findViewById(R.id.image);
        }

        @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
        public View getView() {
            return this.mView;
        }

        @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
        public void setImageDrawable(Drawable drawable) {
            this.mImageView.setImageDrawable(drawable);
        }

        @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
        public void setActiveColor(int color) {
            this.mActiveColor = color;
        }

        public void setAlpha(float alpha) {
            this.mImageView.setAlpha(alpha);
        }

        @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
        public void setInactiveColor(int color) {
            this.mInactiveColor = color;
        }

        @Override // com.kopin.solos.menu.CustomActionProvider.ActionView
        public void setActive(boolean active) {
            this.mImageView.setColorFilter(active ? this.mActiveColor : this.mInactiveColor);
            this.isActive = active;
        }

        public boolean isActive() {
            return this.isActive;
        }
    }
}
