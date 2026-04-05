package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.storage.util.Utility;
import java.lang.reflect.Field;
import java.util.Set;

/* JADX INFO: loaded from: classes24.dex */
public class CustomMultiSelectPreference extends MultiSelectListPreference {
    private static final String TAG = "CustomMultiSelect";
    private String mCheckAllEntry;
    private String mEmptySummary;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private Set<String> mNewValues;
    private Set<String> mValues;

    public CustomMultiSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomMultiSelectPreference);
        this.mCheckAllEntry = a.getString(0);
        this.mEmptySummary = a.getString(1);
        a.recycle();
        if (this.mCheckAllEntry == null) {
            this.mCheckAllEntry = "All";
        }
        if (this.mEmptySummary == null) {
            this.mEmptySummary = "None";
        }
        this.mEntries = getEntries();
        this.mEntryValues = getEntryValues();
        this.mValues = getValues();
        try {
            Field field = MultiSelectListPreference.class.getDeclaredField("mNewValues");
            field.setAccessible(true);
            this.mNewValues = (Set) field.get(this);
            field.setAccessible(false);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    @Override // android.preference.MultiSelectListPreference, android.preference.DialogPreference
    protected void onPrepareDialogBuilder(@NonNull AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        if (this.mNewValues != null) {
            if (this.mEntries == null || this.mEntryValues == null) {
                throw new IllegalStateException("MultiSelectListPreference requires an entries array and an entryValues array.");
            }
            boolean[] checkedItems = getSelectedItems();
            final boolean[] checkedItems2 = new boolean[checkedItems.length + 1];
            checkedItems2[0] = Utility.areAllTrue(checkedItems);
            System.arraycopy(checkedItems, 0, checkedItems2, 1, checkedItems.length);
            CharSequence[] entries = new CharSequence[this.mEntries.length + 1];
            entries[0] = this.mCheckAllEntry;
            System.arraycopy(this.mEntries, 0, entries, 1, this.mEntries.length);
            builder.setMultiChoiceItems(entries, checkedItems2, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.kopin.solos.settings.CustomMultiSelectPreference.1
                @Override // android.content.DialogInterface.OnMultiChoiceClickListener
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    boolean z = true;
                    if (which == 0) {
                        CustomMultiSelectPreference.this.checkAll(dialog, isChecked, checkedItems2);
                        return;
                    }
                    if (isChecked) {
                        boolean added = CustomMultiSelectPreference.this.mNewValues.add(CustomMultiSelectPreference.this.mEntryValues[which - 1].toString());
                        CustomMultiSelectPreference.this.setPreferenceChanged(CustomMultiSelectPreference.this.isPreferenceChanged() || added);
                        if (Utility.areAllTrue(checkedItems2, 1, checkedItems2.length)) {
                            ListView lv = ((AlertDialog) dialog).getListView();
                            lv.setItemChecked(0, true);
                            checkedItems2[0] = true;
                            return;
                        }
                        return;
                    }
                    boolean removed = CustomMultiSelectPreference.this.mNewValues.remove(CustomMultiSelectPreference.this.mEntryValues[which - 1].toString());
                    CustomMultiSelectPreference customMultiSelectPreference = CustomMultiSelectPreference.this;
                    if (!CustomMultiSelectPreference.this.isPreferenceChanged() && !removed) {
                        z = false;
                    }
                    customMultiSelectPreference.setPreferenceChanged(z);
                    ListView lv2 = ((AlertDialog) dialog).getListView();
                    lv2.setItemChecked(0, false);
                    checkedItems2[0] = false;
                }
            });
            this.mValues.remove("#ALL#");
            this.mNewValues.clear();
            this.mNewValues.addAll(this.mValues);
        }
    }

    @Override // android.preference.DialogPreference
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        DialogUtils.setDialogTitleDivider(getDialog());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAll(DialogInterface dialog, boolean isChecked, boolean[] items) {
        ListView lv = ((AlertDialog) dialog).getListView();
        items[0] = isChecked;
        for (int i = 1; i < lv.getCount(); i++) {
            lv.setItemChecked(i, isChecked);
            if (isChecked) {
                this.mNewValues.add(this.mEntryValues[i - 1].toString());
            } else {
                this.mNewValues.remove(this.mEntryValues[i - 1].toString());
            }
            items[i] = isChecked;
        }
        setPreferenceChanged(true);
    }

    @Override // android.preference.MultiSelectListPreference
    public void setEntries(CharSequence[] entries) {
        super.setEntries(entries);
        this.mEntries = entries;
    }

    @Override // android.preference.MultiSelectListPreference
    public void setEntryValues(CharSequence[] entryValues) {
        super.setEntryValues(entryValues);
        this.mEntryValues = entryValues;
    }

    private boolean[] getSelectedItems() {
        this.mValues.remove("#ALL#");
        CharSequence[] entries = this.mEntryValues;
        int entryCount = entries.length;
        Set<String> values = this.mValues;
        boolean[] result = new boolean[entryCount];
        for (int i = 0; i < entryCount; i++) {
            result[i] = values.contains(entries[i].toString());
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPreferenceChanged() {
        boolean value = true;
        try {
            Field field = MultiSelectListPreference.class.getDeclaredField("mPreferenceChanged");
            field.setAccessible(true);
            value = ((Boolean) field.get(this)).booleanValue();
            field.setAccessible(false);
            return value;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPreferenceChanged(boolean value) {
        try {
            Field field = MultiSelectListPreference.class.getDeclaredField("mPreferenceChanged");
            field.setAccessible(true);
            field.setBoolean(this, value);
            field.setAccessible(false);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public String createSummary(Set<?> selectedValues) {
        if (selectedValues == null) {
            return this.mEmptySummary;
        }
        selectedValues.remove("#ALL#");
        if (selectedValues.isEmpty()) {
            return this.mEmptySummary;
        }
        CharSequence[] entries = getEntries();
        if (selectedValues.size() >= entries.length) {
            return this.mCheckAllEntry;
        }
        StringBuilder sb = new StringBuilder();
        boolean addComa = false;
        for (Object string : selectedValues) {
            int pos = Utility.search(getEntryValues(), string);
            if (pos >= 0 && pos < entries.length) {
                if (addComa) {
                    sb.append(", ");
                }
                sb.append(entries[pos].toString());
                addComa = true;
            }
        }
        return sb.toString();
    }
}
