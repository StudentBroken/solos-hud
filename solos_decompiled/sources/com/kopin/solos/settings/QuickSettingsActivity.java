package com.kopin.solos.settings;

import android.R;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.wear.WatchInfoPagerActivity;
import com.kopin.solos.wear.WatchModeActivity;

/* JADX INFO: loaded from: classes24.dex */
public class QuickSettingsActivity extends ThemeActivity {
    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getFragmentManager().beginTransaction().replace(R.id.content, new QuickSettingsFragment()).commit();
    }

    public void onDismissQuickSettingsClick(View view) {
        finish();
    }

    public void onWatchModeHintClick(View view) {
        startActivity(new Intent(this, (Class<?>) WatchInfoPagerActivity.class));
    }

    public void onSwitchToWatchModeClick(View view) {
        Intent intent = new Intent(this, (Class<?>) WatchModeActivity.class);
        intent.putExtra(WatchModeActivity.MODE_PHONE, false);
        startActivity(intent);
        finish();
    }
}
