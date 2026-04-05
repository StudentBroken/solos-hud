package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes24.dex */
public class ExclusiveListPreference extends CustomListPreference {
    private final ArrayList<String> mExcludeVals;
    private CharSequence[] mOriginalEnts;
    private CharSequence[] mOriginalVals;

    public ExclusiveListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mExcludeVals = new ArrayList<>();
    }

    @Override // android.preference.ListPreference, android.preference.DialogPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        updateEntries();
        super.onPrepareDialogBuilder(builder);
    }

    @Override // android.preference.ListPreference, android.preference.Preference
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        this.mOriginalEnts = getEntries();
        this.mOriginalVals = getEntryValues();
        if (getTitle() == null) {
            updateTitle(getValue());
        }
    }

    @Override // android.preference.Preference
    protected boolean callChangeListener(Object newValue) {
        boolean ret = super.callChangeListener(newValue);
        updateTitle((String) newValue);
        return ret;
    }

    private void updateTitle(String val) {
        if (val != null) {
            int pos = findIndexOfOriginalValue(val);
            if (pos == -1) {
                pos = 0;
            }
            if (this.mOriginalEnts != null && pos < this.mOriginalEnts.length) {
                setTitle(this.mOriginalEnts[pos]);
            }
        }
    }

    private int findIndexOfOriginalValue(String val) {
        if (val != null && this.mOriginalVals != null) {
            for (int i = 0; i < this.mOriginalVals.length; i++) {
                if (this.mOriginalVals[i] != null && val.contentEquals(this.mOriginalVals[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void updateEntries() {
        if (this.mOriginalEnts != null) {
            int count = this.mOriginalEnts.length - this.mExcludeVals.size();
            CharSequence[] ents = new CharSequence[count];
            CharSequence[] vals = new CharSequence[count];
            int j = 0;
            for (int i = 0; i < this.mOriginalVals.length; i++) {
                if (!this.mExcludeVals.contains(this.mOriginalVals[i])) {
                    ents[j] = this.mOriginalEnts[i];
                    vals[j] = this.mOriginalVals[i];
                    j++;
                }
            }
            setEntries(ents);
            setEntryValues(vals);
        }
    }

    void hideValue(String newValue) {
        if (!this.mExcludeVals.contains(newValue)) {
            this.mExcludeVals.add(newValue);
        }
    }

    void restoreList() {
        boolean hasEmpty = this.mExcludeVals.contains("");
        this.mExcludeVals.clear();
        if (hasEmpty) {
            this.mExcludeVals.add("");
        }
    }
}
