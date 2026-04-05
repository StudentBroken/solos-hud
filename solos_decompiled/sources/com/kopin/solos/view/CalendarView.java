package com.kopin.solos.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes48.dex */
public class CalendarView extends RelativeLayout {
    private static final SimpleDateFormat COMPACT_DATE = new SimpleDateFormat("dd/MM", Locale.US);
    private TextView mDate;
    private ImageView mImage;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.date_icon, this);
        this.mDate = (TextView) view.findViewById(R.id.icon_date);
        this.mImage = (ImageView) view.findViewById(R.id.icon_image);
    }

    public void setDate(long date) {
        this.mDate.setText(COMPACT_DATE.format(new Date(date)));
    }

    public void setTint(int color) {
        this.mDate.setTextColor(color);
        this.mImage.setColorFilter(color);
    }
}
