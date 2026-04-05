package com.kopin.solos.common;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.kopin.solos.common.log.LogEvent;
import com.kopin.solos.common.permission.Permission;

/* JADX INFO: loaded from: classes52.dex */
public class BaseActivity extends Activity {
    private int mFragmentContainerId = 0;
    private Permission[] mStartPermissions;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseUtil.event(LogEvent.CREATE, this);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        BaseUtil.onStart(this, this.mStartPermissions);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        BaseUtil.event(LogEvent.RESUME, this);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        BaseUtil.event(LogEvent.PAUSE, this);
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        BaseUtil.event(LogEvent.STOP, this);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        BaseUtil.event(LogEvent.DESTROY, this);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        BaseUtil.event(LogEvent.BACK_PRESSED, this);
        super.onBackPressed();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseUtil.event(LogEvent.ACTIVITY_RESULT, this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        return isFinishing();
    }

    public void initPermissions(Permission... startPermissions) {
        initPermissions(0, startPermissions);
    }

    public void initPermissions(int fragmentContainerId, Permission... startPermissions) {
        this.mFragmentContainerId = fragmentContainerId;
        this.mStartPermissions = new Permission[startPermissions.length];
        int length = startPermissions.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            Permission permission = startPermissions[i];
            this.mStartPermissions[i2] = permission;
            i++;
            i2++;
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!isFinishing()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            Fragment fragment = BaseUtil.getVisibleFragment(this, this.mFragmentContainerId);
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
