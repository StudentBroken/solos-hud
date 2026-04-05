package com.kopin.solos.customisableridemetrics;

import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class LabelManager {
    private List<TextView> textViewList = new ArrayList();

    public void add(TextView textView) {
        this.textViewList.add(textView);
    }

    public void setText(String text) {
        for (TextView textView : this.textViewList) {
            if (textView != null) {
                textView.setText(text);
            }
        }
        this.textViewList.remove((Object) null);
    }

    public void setText(int textRes) {
        for (TextView textView : this.textViewList) {
            if (textView != null) {
                textView.setText(textRes);
            }
        }
        this.textViewList.remove((Object) null);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        for (TextView textView : this.textViewList) {
            if (textView != null) {
                textView.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            }
        }
    }

    public boolean contains(TextView textView) {
        return this.textViewList.contains(textView);
    }
}
