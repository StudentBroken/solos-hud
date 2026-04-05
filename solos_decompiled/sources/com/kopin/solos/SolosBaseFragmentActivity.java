package com.kopin.solos;

import android.os.Bundle;
import com.kopin.solos.common.BaseFragmentActivity;
import com.kopin.solos.storage.LiveRide;

/* JADX INFO: loaded from: classes24.dex */
public class SolosBaseFragmentActivity extends BaseFragmentActivity {
    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getTheme(LiveRide.getCurrentSport()));
    }

    public CharSequence getStringFromTheme(int attr) {
        return ThemeUtil.getString(this, attr);
    }
}
