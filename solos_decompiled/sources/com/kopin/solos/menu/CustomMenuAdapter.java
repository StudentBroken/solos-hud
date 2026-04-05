package com.kopin.solos.menu;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import com.kopin.solos.menu.CustomMenuItem;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes24.dex */
public abstract class CustomMenuAdapter<T extends CustomMenuItem> extends BaseAdapter {
    private Context mContext;
    private ArrayList<T> mList = new ArrayList<>();

    @Override // android.widget.Adapter
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public CustomMenuAdapter(Context context) {
        this.mContext = context;
    }

    public void doCleanUp() {
    }

    public void addMenuItem(T menuItem) {
        this.mList.add(menuItem);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void removeMenuItem(T menuItem) {
        this.mList.remove(menuItem);
        notifyDataSetChanged();
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public T getMenuItem(int position) {
        return this.mList.get(position);
    }

    public Point getMeasurements(int sideMargins, int dividerHeight) {
        int maxWidth = 0;
        int height = -dividerHeight;
        View itemView = null;
        int itemType = 0;
        WindowManager windowManager = (WindowManager) getContext().getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(size.x - (sideMargins * 2), Integer.MIN_VALUE);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(size.y, Integer.MIN_VALUE);
        int count = getCount();
        FrameLayout parent = new FrameLayout(getContext());
        for (int i = 0; i < count; i++) {
            int positionType = getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            itemView = getView(i, itemView, parent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            int itemWidth = itemView.getMeasuredWidth();
            int itemHeight = itemView.getMeasuredHeight();
            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
            height += itemHeight + dividerHeight;
        }
        if (height < 0) {
            height = 0;
        }
        if (height > (((double) size.y) * 2.0d) / 3.0d) {
            height = (int) ((((double) size.y) * 2.0d) / 3.0d);
        }
        return new Point(maxWidth, height);
    }
}
