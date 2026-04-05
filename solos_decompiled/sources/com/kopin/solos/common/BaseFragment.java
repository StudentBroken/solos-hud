package com.kopin.solos.common;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.kopin.solos.common.log.LogEvent;
import com.kopin.solos.common.log.RemoteLog;

/* JADX INFO: loaded from: classes52.dex */
public class BaseFragment extends PreferenceFragment {
    protected Button mBtnStep;
    protected FrameLayout mContainerLayout;
    protected TextView mStepHeader;
    protected TextView mStepSubHeader;

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isFinishing()) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_setup_template, container, false);
        this.mContainerLayout = (FrameLayout) view.findViewById(R.id.setupContainer);
        this.mBtnStep = (Button) view.findViewById(R.id.btnStepSetup);
        this.mStepHeader = (TextView) view.findViewById(R.id.txtStepHeader);
        this.mStepSubHeader = (TextView) view.findViewById(R.id.txtSubHeader);
        return view;
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.CREATE, getClass().getSimpleName());
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.START, getClass().getSimpleName());
        }
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.RESUME, getClass().getSimpleName());
        }
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.PAUSE, getClass().getSimpleName());
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.STOP, getClass().getSimpleName());
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.DESTROY, getClass().getSimpleName());
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!isFinishing()) {
            RemoteLog.track(LogEvent.ACTIVITY_RESULT, getClass().getSimpleName());
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override // android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        return isFinishing();
    }

    protected boolean isStillRequired() {
        return (isDetached() || getActivity() == null || getActivity().isFinishing()) ? false : true;
    }

    protected boolean isFinishing() {
        return isDetached() || getActivity() == null || getActivity().isFinishing();
    }
}
