package com.kopin.solos.customisableridemetrics;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.customisableridemetrics.CustomiseableRideMetrics;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class MetricsPagerAdapter extends PagerAdapter {
    public static final int NUM_OVERFLOW_PAGES = 4;
    private List<Integer> itemList = new ArrayList();
    private LayoutInflater layoutInflater;
    private MultiLabelManager multiRideMetricLabelManager;

    public MetricsPagerAdapter(Context context, MultiLabelManager multiRideMetricLabelManager) {
        this.layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.multiRideMetricLabelManager = multiRideMetricLabelManager;
    }

    public MetricsPagerAdapter(Context context, MultiLabelManager multiRideMetricLabelManager, Integer... items) {
        this.layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.multiRideMetricLabelManager = multiRideMetricLabelManager;
        for (Integer item : items) {
            this.itemList.add(item);
        }
    }

    public MetricsPagerAdapter(Context context, MultiLabelManager multiRideMetricLabelManager, List<Integer> items) {
        this.layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.multiRideMetricLabelManager = multiRideMetricLabelManager;
        this.itemList.addAll(items);
    }

    public void setData(Integer... items) {
        this.itemList.clear();
        for (Integer item : items) {
            this.itemList.add(item);
        }
        notifyDataSetChanged();
    }

    public void setData(List<Integer> items) {
        this.itemList.clear();
        this.itemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getItemPosition(Object object) {
        if (object instanceof Integer) {
            return this.itemList.indexOf(object);
        }
        return -2;
    }

    @Override // android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        View pageView = null;
        if (position < this.itemList.size() + 4) {
            pageView = this.layoutInflater.inflate(R.layout.layout_record_screen_metric, (ViewGroup) null);
            TextView txtMetricValue = (TextView) pageView.findViewById(R.id.txtMetricValue);
            TextView txtUnit = (TextView) pageView.findViewById(R.id.txtUnit);
            TextView txtMetricCaption = (TextView) pageView.findViewById(R.id.txtMetricCaption);
            if (position >= this.itemList.size()) {
                position -= this.itemList.size();
            }
            int index = this.itemList.get(position).intValue();
            pageView.setTag(Integer.valueOf(index));
            txtMetricValue.setText(CustomiseableRideMetrics.RideScreenMetric.isFloatType(index) ? R.string.placeholder_float : R.string.placeholder_int);
            if (!this.multiRideMetricLabelManager.get(index).contains(txtMetricValue)) {
                this.multiRideMetricLabelManager.get(index).add(txtMetricValue);
                int titleLabelID = CustomiseableRideMetrics.RideScreenMetric.getTitleLabelId(index);
                int unitLabelId = CustomiseableRideMetrics.RideScreenMetric.getUnitLabelId(index);
                this.multiRideMetricLabelManager.get(unitLabelId).add(txtUnit);
                this.multiRideMetricLabelManager.get(titleLabelID).add(txtMetricCaption);
            }
            int captionRes = CustomiseableRideMetrics.RideScreenMetric.getTitleResource(index);
            if (captionRes > 0) {
                txtMetricCaption.setText(captionRes);
            }
            int iconRes = CustomiseableRideMetrics.RideScreenMetric.getIconResource(index);
            if (iconRes > 0) {
                txtMetricCaption.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0);
            }
            CustomiseableRideMetrics.setLabels();
            CustomiseableRideMetrics.resetRideMetricTexts();
            container.addView(pageView);
        }
        return pageView;
    }

    @Override // android.support.v4.view.PagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.itemList.size() <= 1 ? this.itemList.size() : this.itemList.size() + 4;
    }

    @Override // android.support.v4.view.PagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
