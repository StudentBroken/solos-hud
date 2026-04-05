package com.kopin.solos.settings;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

/* JADX INFO: loaded from: classes52.dex */
public class CustomSwitchPreference extends SwitchPreference {
    private BindViewCallback bindViewCallback;
    private boolean mAllowToggle;
    private Switch mSwitch;

    public interface BindViewCallback {
        void onBindView(Switch r1);
    }

    public void setBindViewCallback(BindViewCallback callback) {
        this.bindViewCallback = callback;
    }

    public CustomSwitchPreference(Context context) {
        super(context, null);
        this.mAllowToggle = true;
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAllowToggle = true;
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAllowToggle = true;
    }

    private View findSwitchView(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View test = root.getChildAt(i);
            if (test instanceof ViewGroup) {
                test = findSwitchView((ViewGroup) test);
            }
            if (test instanceof Switch) {
                return test;
            }
        }
        return null;
    }

    @Override // android.preference.SwitchPreference, android.preference.Preference
    protected void onBindView(View view) {
        clearListenerInViewGroup(view instanceof ViewGroup ? (ViewGroup) view : null);
        super.onBindView(view);
        if (view instanceof Switch) {
            this.mSwitch = (Switch) view;
        } else if (view instanceof ViewGroup) {
            this.mSwitch = (Switch) findSwitchView((ViewGroup) view);
        }
        if (this.mSwitch == null) {
            Log.e(CustomSwitchPreference.class.getSimpleName(), "Couldn't find a Switch view to customise!");
            return;
        }
        Boolean initVal = Boolean.valueOf(getPersistedBoolean(false));
        this.mSwitch.setChecked(initVal.booleanValue());
        this.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.kopin.solos.settings.CustomSwitchPreference.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!CustomSwitchPreference.this.mAllowToggle || !CustomSwitchPreference.this.callChangeListener(Boolean.valueOf(isChecked)) || !CustomSwitchPreference.this.isEnabled()) {
                    buttonView.setChecked(!isChecked);
                } else {
                    CustomSwitchPreference.this.setChecked(isChecked);
                }
            }
        });
        allowToggle(this.mAllowToggle);
        if (this.bindViewCallback != null) {
            this.bindViewCallback.onBindView(this.mSwitch);
        }
    }

    public boolean allowToggle() {
        return this.mAllowToggle;
    }

    public void allowToggle(boolean onOrOff) {
        this.mAllowToggle = onOrOff;
        if (this.mSwitch != null) {
            this.mSwitch.setEnabled(onOrOff);
        }
    }

    private void clearListenerInViewGroup(ViewGroup viewGroup) {
        if (viewGroup != null) {
            int count = viewGroup.getChildCount();
            for (int n = 0; n < count; n++) {
                View childView = viewGroup.getChildAt(n);
                if (childView instanceof Switch) {
                    Switch switchView = (Switch) childView;
                    switchView.setOnCheckedChangeListener(null);
                    return;
                } else {
                    if (childView instanceof ViewGroup) {
                        ViewGroup childGroup = (ViewGroup) childView;
                        clearListenerInViewGroup(childGroup);
                    }
                }
            }
        }
    }
}
