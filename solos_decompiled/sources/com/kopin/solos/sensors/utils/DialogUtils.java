package com.kopin.solos.sensors.utils;

import android.app.Dialog;
import android.content.res.Resources;
import android.view.View;
import com.kopin.solos.sensors.R;

/* JADX INFO: loaded from: classes28.dex */
public class DialogUtils {
    public static void setDialogTitleDivider(Dialog dialog) {
        Resources resources = dialog.getContext().getResources();
        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
        if (titleDividerId != 0) {
            View divider = dialog.findViewById(titleDividerId);
            divider.setBackgroundColor(resources.getColor(R.color.dialog_divider_color));
        }
    }
}
