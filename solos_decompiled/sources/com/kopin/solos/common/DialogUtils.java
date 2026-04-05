package com.kopin.solos.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;

/* JADX INFO: loaded from: classes52.dex */
public class DialogUtils {
    public static AlertDialog createDialog(Context context, int title, int message, int buttonMsg, Runnable action) {
        return createDialog(context, context.getString(title), context.getString(message), context.getString(buttonMsg), action);
    }

    public static AlertDialog createDialog(Context context, String title, String message, String buttonMsg, final Runnable action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.VCAlertDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(buttonMsg, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.common.DialogUtils.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (action != null) {
                    action.run();
                }
            }
        });
        return builder.create();
    }

    public static void setDialogTitleDivider(Dialog dialog) {
        Resources resources = dialog.getContext().getResources();
        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
        if (titleDividerId != 0) {
            View divider = dialog.findViewById(titleDividerId);
            divider.setBackgroundColor(resources.getColor(R.color.app_actionbar_divider));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0046, code lost:
    
        r7.setAccessible(true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004a, code lost:
    
        r7.set(r0, new android.graphics.drawable.ColorDrawable(r16.getContext().getResources().getColor(com.kopin.solos.common.R.color.solos_orange)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0063, code lost:
    
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0064, code lost:
    
        r3.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0086, code lost:
    
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0087, code lost:
    
        r3.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x008b, code lost:
    
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008c, code lost:
    
        r3.printStackTrace();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void setDialogSeparatorDivider(android.app.DatePickerDialog r16) {
        /*
            r12 = 0
            int r11 = android.os.Build.VERSION.SDK_INT
            r13 = 21
            if (r11 > r13) goto L85
            android.widget.DatePicker r2 = r16.getDatePicker()     // Catch: java.lang.ClassCastException -> L68
            r11 = 0
            android.view.View r5 = r2.getChildAt(r11)     // Catch: java.lang.ClassCastException -> L68
            android.widget.LinearLayout r5 = (android.widget.LinearLayout) r5     // Catch: java.lang.ClassCastException -> L68
            r11 = 0
            android.view.View r6 = r5.getChildAt(r11)     // Catch: java.lang.ClassCastException -> L68
            android.widget.LinearLayout r6 = (android.widget.LinearLayout) r6     // Catch: java.lang.ClassCastException -> L68
            r4 = 0
        L1a:
            int r11 = r6.getChildCount()     // Catch: java.lang.ClassCastException -> L68
            if (r4 >= r11) goto L85
            android.view.View r10 = r6.getChildAt(r4)     // Catch: java.lang.ClassCastException -> L68
            if (r10 == 0) goto L60
            boolean r11 = r10 instanceof android.widget.NumberPicker     // Catch: java.lang.ClassCastException -> L68
            if (r11 == 0) goto L60
            r0 = r10
            android.widget.NumberPicker r0 = (android.widget.NumberPicker) r0     // Catch: java.lang.ClassCastException -> L68
            r8 = r0
            java.lang.Class<android.widget.NumberPicker> r11 = android.widget.NumberPicker.class
            java.lang.reflect.Field[] r9 = r11.getDeclaredFields()     // Catch: java.lang.ClassCastException -> L68
            int r13 = r9.length     // Catch: java.lang.ClassCastException -> L68
            r11 = r12
        L36:
            if (r11 >= r13) goto L60
            r7 = r9[r11]     // Catch: java.lang.ClassCastException -> L68
            java.lang.String r14 = r7.getName()     // Catch: java.lang.ClassCastException -> L68
            java.lang.String r15 = "mSelectionDivider"
            boolean r14 = r14.equals(r15)     // Catch: java.lang.ClassCastException -> L68
            if (r14 == 0) goto L90
            r11 = 1
            r7.setAccessible(r11)     // Catch: java.lang.ClassCastException -> L68
            android.graphics.drawable.ColorDrawable r11 = new android.graphics.drawable.ColorDrawable     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
            android.content.Context r13 = r16.getContext()     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
            android.content.res.Resources r13 = r13.getResources()     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
            int r14 = com.kopin.solos.common.R.color.solos_orange     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
            int r13 = r13.getColor(r14)     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
            r11.<init>(r13)     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
            r7.set(r8, r11)     // Catch: java.lang.IllegalArgumentException -> L63 java.lang.ClassCastException -> L68 android.content.res.Resources.NotFoundException -> L86 java.lang.IllegalAccessException -> L8b
        L60:
            int r4 = r4 + 1
            goto L1a
        L63:
            r3 = move-exception
            r3.printStackTrace()     // Catch: java.lang.ClassCastException -> L68
            goto L60
        L68:
            r1 = move-exception
            java.lang.String r11 = "DialogUtil"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "Problem setting dividers on DatePickerDialog "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = r1.getMessage()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.w(r11, r12)
        L85:
            return
        L86:
            r3 = move-exception
            r3.printStackTrace()     // Catch: java.lang.ClassCastException -> L68
            goto L60
        L8b:
            r3 = move-exception
            r3.printStackTrace()     // Catch: java.lang.ClassCastException -> L68
            goto L60
        L90:
            int r11 = r11 + 1
            goto L36
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kopin.solos.common.DialogUtils.setDialogSeparatorDivider(android.app.DatePickerDialog):void");
    }

    public static void showNoNetworkDialog(Activity activity) {
        showNoNetworkDialog(activity, R.string.no_internet_msg_sharing);
    }

    public static void showNoNetworkDialog(Activity activity, int messageResource) {
        AlertDialog dialog = createDialog(activity, R.string.menu_command_log_error, messageResource, android.R.string.ok, (Runnable) null);
        dialog.show();
        setDialogTitleDivider(dialog);
    }
}
