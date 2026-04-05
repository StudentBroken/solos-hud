package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kopin.pupil.update.util.FirmwareFlash;
import com.kopin.solos.BuildConfig;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Config;
import java.util.Calendar;
import java.util.Date;

/* JADX INFO: loaded from: classes24.dex */
public class AboutPreference extends Preference {
    private int touchCount;

    public AboutPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AboutPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AboutPreference(Context context) {
        super(context);
    }

    @Override // android.preference.Preference
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        this.touchCount = 0;
        View view = View.inflate(getContext(), R.layout.layout_about, null);
        view.setClickable(true);
        TextView versionText = (TextView) view.findViewById(R.id.textVersionName);
        versionText.setText(getContext().getString(R.string.text_version_name, BuildConfig.VERSION_NAME));
        Date buildDate = com.kopin.solos.core.BuildConfig.BUILD_DATE != null ? com.kopin.solos.core.BuildConfig.BUILD_DATE : new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(buildDate);
        String line = getContext().getResources().getString(R.string.text_copyright, String.valueOf(cal.get(1)));
        ((TextView) view.findViewById(R.id.txtCopyright)).setText(line);
        view.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.AboutPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AboutPreference.this.touchCount = (AboutPreference.this.touchCount + 1) % 5;
                if (AboutPreference.this.touchCount == 0) {
                    AboutPreference.this.showDebugInfo();
                }
            }
        });
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDebugInfo() {
        String serverStr = Config.CLOUD_LIVE ? "LIVE" : "DEV";
        String safeFWVersion = FirmwareFlash.safeVersion();
        String debugInfo = "Server: " + serverStr + "\nSafe FW Version: " + safeFWVersion;
        AlertDialog dialog = DialogUtils.createDialog(getContext(), "Debug Info", debugInfo, getContext().getString(android.R.string.ok), (Runnable) null);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }
}
