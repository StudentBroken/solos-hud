package android.support.wearable.preference;

import android.R;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.CallSuper;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class WearablePreferenceActivity extends WearableActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback {
    @Override // android.support.wearable.activity.WearableActivity, android.app.Activity
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String initialFragment = intent.getStringExtra(":android:show_fragment");
            Bundle initialArguments = intent.getBundleExtra(":android:show_fragment_args");
            if (initialFragment != null) {
                startPreferenceFragment(Fragment.instantiate(this, initialFragment, initialArguments), false);
            }
        }
    }

    @Override // android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback
    public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
        startActivity(onBuildStartFragmentIntent(pref.getFragment(), pref.getExtras(), 0));
        return true;
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof PreferenceFragment) {
            CharSequence title = ((PreferenceFragment) fragment).getPreferenceScreen().getTitle();
            if (!TextUtils.isEmpty(title) && !TextUtils.equals(title, getTitle())) {
                setTitle(title);
            }
        }
    }

    public void startPreferenceFragment(Fragment fragment, boolean push) {
        if (push) {
            startActivity(onBuildStartFragmentIntent(fragment.getClass().getName(), fragment.getArguments(), 0));
            return;
        }
        getFragmentManager().popBackStack((String) null, 1);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
    }

    public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args, int titleRes) {
        return new Intent("android.intent.action.MAIN").setClass(this, WearablePreferenceActivity.class).putExtra(":android:show_fragment", fragmentName).putExtra(":android:show_fragment_args", args).putExtra(":android:show_fragment_title", titleRes).putExtra(":android:no_headers", true);
    }
}
