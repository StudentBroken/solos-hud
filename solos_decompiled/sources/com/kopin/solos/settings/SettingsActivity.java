package com.kopin.solos.settings;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import com.kopin.solos.R;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.LiveRide;

/* JADX INFO: loaded from: classes24.dex */
public class SettingsActivity extends PreferenceActivity {
    @Override // android.preference.PreferenceActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(LiveRide.getCurrentSport() == SportType.RIDE ? R.style.Theme_Ride : R.style.Theme_Run);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
