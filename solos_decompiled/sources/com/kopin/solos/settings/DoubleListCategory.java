package com.kopin.solos.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import com.kopin.solos.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes24.dex */
public class DoubleListCategory extends PreferenceCategory {
    private boolean isEnabled;
    private final Preference.OnPreferenceChangeListener mListChanged;
    private final ArrayList<ExclusiveListPreference> mLists;

    public DoubleListCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLists = new ArrayList<>();
        this.mListChanged = new Preference.OnPreferenceChangeListener() { // from class: com.kopin.solos.settings.DoubleListCategory.1
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                DoubleListCategory.this.setLists((ExclusiveListPreference) preference, (String) newValue);
                DoubleListCategory.this.updateEnabled((ExclusiveListPreference) preference, (CharSequence) newValue);
                return DoubleListCategory.this.callChangeListener(newValue);
            }
        };
    }

    @Override // android.preference.PreferenceCategory, android.preference.PreferenceGroup
    protected boolean onPrepareAddPreference(Preference preference) {
        if (preference instanceof ExclusiveListPreference) {
            ExclusiveListPreference listPref = (ExclusiveListPreference) preference;
            for (ListPreference pref : this.mLists) {
                String otherVal = pref.getValue();
                if (otherVal != null) {
                    listPref.hideValue(otherVal);
                }
            }
            this.mLists.add((ExclusiveListPreference) preference);
            preference.setOnPreferenceChangeListener(this.mListChanged);
            boolean isFirst = isFirstPref(listPref);
            CharSequence[] ents = listPref.getEntries();
            ents[0] = getContext().getString(isFirst ? R.string.multi_display_screen_none : R.string.multi_display_entry_none);
            listPref.setEntries(ents);
            if (!isFirst) {
                listPref.hideValue("");
            }
            CharSequence value = listPref.getValue();
            CharSequence title = ((ListPreference) preference).getEntry();
            preference.setTitle(title);
            setLists((ExclusiveListPreference) preference, (String) value);
            updateEnabled(listPref, value);
        }
        return super.onPrepareAddPreference(preference);
    }

    public void holdFirstScreen() {
        if (!this.mLists.isEmpty()) {
            this.mLists.get(0).setEnabled(false);
        }
    }

    public boolean updateEnabled() {
        for (ExclusiveListPreference pref : this.mLists) {
            updateEnabled(pref, pref.getValue());
        }
        return this.isEnabled;
    }

    private boolean isFirstPref(ExclusiveListPreference pref) {
        return !this.mLists.isEmpty() && this.mLists.get(0) == pref;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnabled(ExclusiveListPreference preference, CharSequence value) {
        boolean isFirst = isFirstPref(preference);
        if (isFirst) {
            this.isEnabled = (value == null || ((String) value).isEmpty()) ? false : true;
            for (ExclusiveListPreference pref : this.mLists) {
                if (pref != preference) {
                    pref.setEnabled(this.isEnabled);
                }
            }
            return;
        }
        preference.setEnabled(this.isEnabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLists(ExclusiveListPreference preference, String newValue) {
        for (ExclusiveListPreference pref : this.mLists) {
            if (pref != preference) {
                if (newValue == null) {
                    pref.restoreList();
                } else {
                    pref.hideValue(newValue.toString());
                }
            }
        }
    }
}
