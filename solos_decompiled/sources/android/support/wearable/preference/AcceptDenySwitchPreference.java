package android.support.wearable.preference;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.wearable.R;
import android.support.wearable.view.AcceptDenyDialog;
import android.util.AttributeSet;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class AcceptDenySwitchPreference extends SwitchPreference implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private AcceptDenyDialog mDialog;
    private Drawable mDialogIcon;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private boolean mShowDialogWhenTurningOff;
    private boolean mShowDialogWhenTurningOn;
    private boolean mShowNegativeButton;
    private boolean mShowPositiveButton;
    private int mWhichButtonClicked;

    public AcceptDenySwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public AcceptDenySwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public AcceptDenySwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public AcceptDenySwitchPreference(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AcceptDenySwitchPreference, defStyleAttr, defStyleRes);
        this.mDialogTitle = a.getString(R.styleable.AcceptDenySwitchPreference_dialogTitle);
        if (this.mDialogTitle == null) {
            this.mDialogTitle = getTitle();
        }
        this.mDialogMessage = a.getString(R.styleable.AcceptDenySwitchPreference_dialogMessage);
        this.mDialogIcon = a.getDrawable(R.styleable.AcceptDenySwitchPreference_dialogIcon);
        this.mShowPositiveButton = a.getBoolean(R.styleable.AcceptDenySwitchPreference_showPositiveDialogButton, true);
        this.mShowNegativeButton = a.getBoolean(R.styleable.AcceptDenySwitchPreference_showNegativeDialogButton, true);
        this.mShowDialogWhenTurningOn = a.getBoolean(R.styleable.AcceptDenySwitchPreference_showDialogWhenTurningOn, true);
        this.mShowDialogWhenTurningOff = a.getBoolean(R.styleable.AcceptDenySwitchPreference_showDialogWhenTurningOff, false);
        a.recycle();
    }

    public void setDialogTitle(CharSequence dialogTitle) {
        this.mDialogTitle = dialogTitle;
    }

    public void setDialogTitle(@StringRes int dialogTitleResId) {
        setDialogTitle(getContext().getString(dialogTitleResId));
    }

    public CharSequence getDialogTitle() {
        return this.mDialogTitle;
    }

    public void setDialogMessage(CharSequence dialogMessage) {
        this.mDialogMessage = dialogMessage;
    }

    public void setDialogMessage(@StringRes int dialogMessageResId) {
        setDialogMessage(getContext().getString(dialogMessageResId));
    }

    public CharSequence getDialogMessage() {
        return this.mDialogMessage;
    }

    public void setDialogIcon(Drawable dialogIcon) {
        this.mDialogIcon = dialogIcon;
    }

    public void setDialogIcon(@DrawableRes int dialogIconRes) {
        this.mDialogIcon = getContext().getDrawable(dialogIconRes);
    }

    public Drawable getDialogIcon() {
        return this.mDialogIcon;
    }

    protected void onPrepareDialog(@NonNull AcceptDenyDialog dialog) {
    }

    @Override // android.preference.TwoStatePreference, android.preference.Preference
    protected void onClick() {
        if (this.mDialog == null || !this.mDialog.isShowing()) {
            boolean newValue = !isChecked();
            if ((this.mShowDialogWhenTurningOn && newValue) || (this.mShowDialogWhenTurningOff && !newValue)) {
                showDialog(null);
            } else if (callChangeListener(Boolean.valueOf(newValue))) {
                setChecked(newValue);
            }
        }
    }

    protected void showDialog(@Nullable Bundle state) {
        Context context = getContext();
        this.mWhichButtonClicked = -2;
        this.mDialog = new AcceptDenyDialog(context);
        this.mDialog.setTitle(this.mDialogTitle);
        this.mDialog.setIcon(this.mDialogIcon);
        this.mDialog.setMessage(this.mDialogMessage);
        if (this.mShowPositiveButton) {
            this.mDialog.setPositiveButton(this);
        }
        if (this.mShowNegativeButton) {
            this.mDialog.setNegativeButton(this);
        }
        onPrepareDialog(this.mDialog);
        if (state != null) {
            this.mDialog.onRestoreInstanceState(state);
        }
        this.mDialog.setOnDismissListener(this);
        this.mDialog.show();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        this.mWhichButtonClicked = which;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(@NonNull DialogInterface dialog) {
        this.mDialog = null;
        onDialogClosed(this.mWhichButtonClicked == -1);
    }

    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            boolean newValue = !isChecked();
            if (callChangeListener(Boolean.valueOf(newValue))) {
                setChecked(newValue);
            }
        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    @Override // android.preference.Preference
    public void onPrepareForRemoval() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    @Override // android.preference.TwoStatePreference, android.preference.Preference
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (this.mDialog == null || !this.mDialog.isShowing()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = this.mDialog.onSaveInstanceState();
        return myState;
    }

    @Override // android.preference.TwoStatePreference, android.preference.Preference
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    private static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.support.wearable.preference.AcceptDenySwitchPreference.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle dialogBundle;
        boolean isDialogShowing;

        public SavedState(Parcel source) {
            super(source);
            this.isDialogShowing = source.readInt() == 1;
            this.dialogBundle = source.readBundle();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.isDialogShowing ? 1 : 0);
            dest.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
