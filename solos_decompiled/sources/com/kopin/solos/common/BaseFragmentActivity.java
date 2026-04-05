package com.kopin.solos.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import com.kopin.solos.common.log.LogEvent;
import com.kopin.solos.common.permission.Permission;
import java.util.List;

/* JADX INFO: loaded from: classes52.dex */
public class BaseFragmentActivity extends FragmentActivity {
    private int mFragmentContainerId = 0;
    private Permission[] mStartPermissions;

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseUtil.event(LogEvent.CREATE, this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        BaseUtil.onStart(this, this.mStartPermissions);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        BaseUtil.event(LogEvent.RESUME, this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        BaseUtil.event(LogEvent.PAUSE, this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        BaseUtil.event(LogEvent.STOP, this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        BaseUtil.event(LogEvent.DESTROY, this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        BaseUtil.event(LogEvent.BACK_PRESSED, this);
        super.onBackPressed();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseUtil.event(LogEvent.ACTIVITY_RESULT, this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        return isFinishing();
    }

    public void onServiceDisconnected() {
    }

    protected Fragment findFragment(String tag) {
        if (isFinishing()) {
            return null;
        }
        return getFragmentManager().findFragmentByTag(tag);
    }

    protected void popToFragment(String tag) {
        if (!isFinishing()) {
            getFragmentManager().popBackStack(tag, 1);
        }
    }

    protected void clearBackStack() {
        if (!isFinishing()) {
            new Handler().post(new Runnable() { // from class: com.kopin.solos.common.BaseFragmentActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!BaseFragmentActivity.this.isFinishing()) {
                        FragmentManager manager = BaseFragmentActivity.this.getFragmentManager();
                        if (manager.getBackStackEntryCount() > 0) {
                            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                            manager.popBackStack(first.getId(), 1);
                        }
                    }
                }
            });
        }
    }

    public void fragmentAddBackStack(int container, Fragment fragment) {
        fragmentAddBackStack(container, fragment, null);
    }

    public void fragmentAddBackStack(final int container, final Fragment fragment, final String tag) {
        if (!isFinishing()) {
            new Handler().post(new Runnable() { // from class: com.kopin.solos.common.BaseFragmentActivity.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!BaseFragmentActivity.this.isFinishing()) {
                        BaseFragmentActivity.this.getFragmentManager().beginTransaction().add(container, fragment, tag).addToBackStack(null).commitAllowingStateLoss();
                    }
                }
            });
        }
    }

    public void fragmentReplace(final int container, final Fragment fragment) {
        if (!isFinishing()) {
            new Handler().post(new Runnable() { // from class: com.kopin.solos.common.BaseFragmentActivity.3
                @Override // java.lang.Runnable
                public void run() {
                    if (!BaseFragmentActivity.this.isFinishing()) {
                        BaseFragmentActivity.this.getFragmentManager().beginTransaction().replace(container, fragment).commitAllowingStateLoss();
                    }
                }
            });
        }
    }

    public void fragmentReplace(final int container, final Fragment fragment, final String tag) {
        if (!isFinishing()) {
            new Handler().post(new Runnable() { // from class: com.kopin.solos.common.BaseFragmentActivity.4
                @Override // java.lang.Runnable
                public void run() {
                    if (!BaseFragmentActivity.this.isFinishing()) {
                        BaseFragmentActivity.this.getFragmentManager().beginTransaction().replace(container, fragment, tag).commitAllowingStateLoss();
                    }
                }
            });
        }
    }

    public void fragmentReplaceBackStack(final int container, final Fragment fragment) {
        if (!isFinishing()) {
            new Handler().post(new Runnable() { // from class: com.kopin.solos.common.BaseFragmentActivity.5
                @Override // java.lang.Runnable
                public void run() {
                    if (!BaseFragmentActivity.this.isFinishing()) {
                        BaseFragmentActivity.this.getFragmentManager().beginTransaction().replace(container, fragment).addToBackStack(null).commitAllowingStateLoss();
                    }
                }
            });
        }
    }

    public void fragmentReplaceBackStack(final int container, final Fragment fragment, final String tag) {
        if (!isFinishing()) {
            new Handler().post(new Runnable() { // from class: com.kopin.solos.common.BaseFragmentActivity.6
                @Override // java.lang.Runnable
                public void run() {
                    if (!BaseFragmentActivity.this.isFinishing()) {
                        BaseFragmentActivity.this.getFragmentManager().beginTransaction().replace(container, fragment, tag).addToBackStack(null).commitAllowingStateLoss();
                    }
                }
            });
        }
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

    public void initPermissions(int fragmentContainerId, List<Permission> startPermissions) {
        this.mFragmentContainerId = fragmentContainerId;
        this.mStartPermissions = (Permission[]) startPermissions.toArray(new Permission[startPermissions.size()]);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!isFinishing()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            android.support.v4.app.Fragment frag = BaseUtil.getVisibleSupportFragment(this);
            if (frag != null) {
                frag.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            Fragment fragment = BaseUtil.getVisibleFragment(this, this.mFragmentContainerId);
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
