package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.settings.TargetDialog;
import com.kopin.solos.settings.ValidatedEditTextPreference;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.TargetPreference;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class DualCustomListPreference extends CustomListPreference {
    private static final TargetPreference[] TIME_TARGETS = {TargetPreference.TIME, TargetPreference.AVERAGE_PACE};
    private static final List<TargetPreference> TIME_TARGETS_LIST = new ArrayList(Arrays.asList(TIME_TARGETS));
    private static boolean listDialogVisible = false;
    private boolean allowToggle1;
    private String def1;
    private String def2;
    private ListType focusedList;
    private String key1;
    private String key2;
    private TextView list1;
    private TextView list2;
    private final ArrayList<String> mExcludeVals1;
    private final ArrayList<String> mExcludeVals2;
    private CharSequence[] mOriginalEnts;
    private CharSequence[] mOriginalVals;
    private View mRowDivider;
    private boolean mShowDivider;
    private final TargetDialog.TargetChangedListener mTargetChangeListener;
    private ValidatedEditTextPreference.UnitSystemChangeListener mUnitSystemChangeListener;
    private TextView target1;
    private TextView target2;
    private TargetPreference targetType1;
    private TargetPreference targetType2;
    private String targetValue1;
    private String targetValue2;

    private enum ListType {
        FIRST,
        SECOND
    }

    public DualCustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.focusedList = ListType.FIRST;
        this.def1 = "";
        this.def2 = "";
        this.targetValue1 = "";
        this.targetValue2 = "";
        this.allowToggle1 = true;
        this.mExcludeVals1 = new ArrayList<>();
        this.mExcludeVals2 = new ArrayList<>();
        this.mShowDivider = true;
        this.mTargetChangeListener = new TargetDialog.TargetChangedListener() { // from class: com.kopin.solos.settings.DualCustomListPreference.5
            @Override // com.kopin.solos.settings.TargetDialog.TargetChangedListener
            public void onTargetValueChanged(String key, String newValue) {
                DualCustomListPreference.this.setCustomValue(key, newValue);
                DualCustomListPreference.this.setCustomSummary(DualCustomListPreference.this.focusedList);
                DualCustomListPreference.this.callChangeListener(newValue);
            }

            @Override // com.kopin.solos.settings.TargetDialog.TargetChangedListener
            public void onTargetValueChanged(String key, long newValue) {
                DualCustomListPreference.this.setCustomLong(key, newValue);
                DualCustomListPreference.this.setCustomSummary(DualCustomListPreference.this.focusedList);
                DualCustomListPreference.this.callChangeListener(Long.valueOf(newValue));
            }
        };
        parseAttributes(context, attrs);
    }

    public DualCustomListPreference(Context context) {
        super(context);
        this.focusedList = ListType.FIRST;
        this.def1 = "";
        this.def2 = "";
        this.targetValue1 = "";
        this.targetValue2 = "";
        this.allowToggle1 = true;
        this.mExcludeVals1 = new ArrayList<>();
        this.mExcludeVals2 = new ArrayList<>();
        this.mShowDivider = true;
        this.mTargetChangeListener = new TargetDialog.TargetChangedListener() { // from class: com.kopin.solos.settings.DualCustomListPreference.5
            @Override // com.kopin.solos.settings.TargetDialog.TargetChangedListener
            public void onTargetValueChanged(String key, String newValue) {
                DualCustomListPreference.this.setCustomValue(key, newValue);
                DualCustomListPreference.this.setCustomSummary(DualCustomListPreference.this.focusedList);
                DualCustomListPreference.this.callChangeListener(newValue);
            }

            @Override // com.kopin.solos.settings.TargetDialog.TargetChangedListener
            public void onTargetValueChanged(String key, long newValue) {
                DualCustomListPreference.this.setCustomLong(key, newValue);
                DualCustomListPreference.this.setCustomSummary(DualCustomListPreference.this.focusedList);
                DualCustomListPreference.this.callChangeListener(Long.valueOf(newValue));
            }
        };
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DualCustomListPreference);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    this.key1 = a.getString(attr);
                    break;
                case 1:
                    this.key2 = a.getString(attr);
                    break;
                case 2:
                    this.def1 = a.getString(attr);
                    break;
                case 3:
                    this.def2 = a.getString(attr);
                    break;
                case 4:
                    this.mShowDivider = a.getBoolean(attr, true);
                    break;
            }
        }
        a.recycle();
    }

    @Override // android.preference.ListPreference, android.preference.Preference
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        this.mOriginalEnts = getEntries();
        this.mOriginalVals = getEntryValues();
        this.mExcludeVals1.add("");
        this.mExcludeVals2.add("");
        if (getPersistedValue(this.key1).isEmpty()) {
            setCustomValue(this.key1, this.def1);
        }
        if (getPersistedValue(this.key2).isEmpty()) {
            setCustomValue(this.key2, this.def2);
        }
        fixDefault(this.key1);
        fixDefault(this.key2);
    }

    private void fixDefault(String key) {
        String value = getPersistedValue(key);
        TemplateManager.DataType dataType = TemplateManager.DataType.fromString(value);
        MetricDataType metricDataType = MetricDataType.fromString(value);
        if (!dataType.isSupported() || (metricDataType != null && !ConfigMetrics.isHeadsetMetricEnabled(metricDataType))) {
            setCustomValue(key, "");
        }
    }

    @Override // android.preference.ListPreference
    public void setEntries(CharSequence[] entries) {
        super.setEntries(entries);
        this.mOriginalEnts = entries;
    }

    @Override // android.preference.ListPreference
    public void setEntryValues(CharSequence[] entryValues) {
        super.setEntryValues(entryValues);
        this.mOriginalVals = entryValues;
    }

    @Override // android.preference.Preference
    protected void onBindView(View viewRoot) {
        super.onBindView(viewRoot);
        this.mRowDivider = viewRoot.findViewById(R.id.rowDivider);
        if (!this.mShowDivider) {
            this.mRowDivider.setVisibility(8);
        }
        this.list1 = (TextView) viewRoot.findViewById(R.id.listSection1);
        this.list2 = (TextView) viewRoot.findViewById(R.id.listSection2);
        this.target1 = (TextView) viewRoot.findViewById(R.id.targetText1);
        this.target2 = (TextView) viewRoot.findViewById(R.id.targetText2);
        setCustomSummary(ListType.FIRST);
        setCustomSummary(ListType.SECOND);
        this.list1.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.DualCustomListPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!DualCustomListPreference.listDialogVisible) {
                    boolean unused = DualCustomListPreference.listDialogVisible = true;
                    DualCustomListPreference.this.focusedList = ListType.FIRST;
                    DualCustomListPreference.this.showDialog(null);
                }
            }
        });
        allowToggle(this.allowToggle1);
        this.list2.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.DualCustomListPreference.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!DualCustomListPreference.listDialogVisible) {
                    boolean unused = DualCustomListPreference.listDialogVisible = true;
                    DualCustomListPreference.this.focusedList = ListType.SECOND;
                    DualCustomListPreference.this.showDialog(null);
                }
            }
        });
        this.target1.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.DualCustomListPreference.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DualCustomListPreference.this.focusedList = ListType.FIRST;
                DualCustomListPreference.this.showTargetDialog(v, DualCustomListPreference.this.targetValue1, DualCustomListPreference.this.targetType1);
            }
        });
        this.target2.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.DualCustomListPreference.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DualCustomListPreference.this.focusedList = ListType.SECOND;
                DualCustomListPreference.this.showTargetDialog(v, DualCustomListPreference.this.targetValue2, DualCustomListPreference.this.targetType2);
            }
        });
    }

    public void allowToggle(boolean onOrOff) {
        this.allowToggle1 = onOrOff;
        if (this.list1 != null) {
            this.list1.setEnabled(onOrOff && isEnabled());
        }
    }

    public void refresh() {
        setCustomSummary(ListType.FIRST);
        setCustomSummary(ListType.SECOND);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTargetDialog(View v, String value, TargetPreference targetType) {
        String title;
        if (TIME_TARGETS_LIST.contains(targetType)) {
            long curVal = getSharedPreferences().getLong(targetType.getKey(), 3600000L);
            TargetTimeDialog.show(getContext(), targetType, curVal, this.mTargetChangeListener);
        } else if (targetType != TargetPreference.NONE) {
            String unit = (String) v.getTag();
            if (unit == null || unit.isEmpty()) {
                title = getContext().getResources().getString(R.string.pref_target_summary);
            } else {
                title = getContext().getResources().getString(R.string.pref_target_summary_format, unit);
            }
            TargetDialog.show(getContext(), title, value, targetType, this.mTargetChangeListener, targetType.getAllowDecimals());
        }
    }

    private TargetPreference getTargetKey(ListType fromList) {
        return TargetPreference.getTarget("" + getPersistedValue(fromList == ListType.FIRST ? this.key1 : this.key2));
    }

    @Override // android.preference.ListPreference, android.preference.DialogPreference
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        if (this.focusedList == ListType.FIRST) {
            restoreList(this.mExcludeVals1);
            hideValue(this.mExcludeVals1, getPersistedValue(this.key2));
            updateEntries(this.mExcludeVals1);
            setValue(getPersistedValue(this.key1));
        } else {
            restoreList(this.mExcludeVals2);
            hideValue(this.mExcludeVals2, getPersistedValue(this.key1));
            updateEntries(this.mExcludeVals2);
            setValue(getPersistedValue(this.key2));
        }
        super.onPrepareDialogBuilder(builder);
    }

    private void updateEntries(ArrayList<String> excludeVals) {
        if (this.mOriginalEnts != null) {
            int count = this.mOriginalEnts.length - excludeVals.size();
            CharSequence[] ents = new CharSequence[count];
            CharSequence[] vals = new CharSequence[count];
            int j = 0;
            for (int i = 0; i < this.mOriginalVals.length; i++) {
                if (!excludeVals.contains(this.mOriginalVals[i])) {
                    ents[j] = this.mOriginalEnts[i];
                    vals[j] = this.mOriginalVals[i];
                    j++;
                }
            }
            super.setEntries(ents);
            super.setEntryValues(vals);
        }
    }

    void hideValue(ArrayList<String> excludeVals, String newValue) {
        if (!excludeVals.contains(newValue)) {
            excludeVals.add(newValue);
        }
    }

    void restoreList(ArrayList<String> excludeVals) {
        boolean hasEmpty = excludeVals.contains("");
        excludeVals.clear();
        if (hasEmpty) {
            excludeVals.add("");
        }
    }

    @Override // android.preference.ListPreference, android.preference.DialogPreference
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            if (this.focusedList == ListType.FIRST) {
                setValue1(getValue());
            } else if (this.focusedList == ListType.SECOND) {
                setValue2(getValue());
            }
            callChangeListener(null);
        }
        listDialogVisible = false;
        this.list1.setEnabled(this.allowToggle1);
        this.list2.setEnabled(true);
    }

    public void setValue1(String value) {
        setValue(value);
        setCustomValue(this.key1, value);
        setCustomSummary(ListType.FIRST);
    }

    public void setValue2(String value) {
        setValue(value);
        setCustomValue(this.key2, value);
        setCustomSummary(ListType.SECOND);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCustomValue(String key, String value) {
        SharedPreferences preferences = getSharedPreferences();
        preferences.edit().putString(key, value).commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCustomLong(String key, long value) {
        SharedPreferences preferences = getSharedPreferences();
        preferences.edit().putLong(key, value).commit();
    }

    private String getPersistedValue(String key) {
        return getSharedPreferences().getString(key, "");
    }

    private String getPersistedValueExtra(String key) {
        if (key == null || key.isEmpty()) {
            return "";
        }
        Map<String, ?> prefs = getSharedPreferences().getAll();
        Object o = prefs.get(key);
        if (o != null) {
            String val = o instanceof Float ? "" + getSharedPreferences().getFloat(key, 0.0f) : "";
            if (o instanceof Integer) {
                val = val + getSharedPreferences().getInt(key, 0);
            }
            if (o instanceof Long) {
                val = val + getSharedPreferences().getLong(key, 0L);
            }
            if (o instanceof String) {
                return val + getSharedPreferences().getString(key, "");
            }
            return val;
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCustomSummary(ListType listType) {
        TargetPreference targetPreference = getTargetKey(listType);
        String targetValue = getPersistedValueExtra(targetPreference.getKey());
        String unit = targetPreference.getUnitSuffix(getContext());
        if (this.mUnitSystemChangeListener != null && TargetPreference.LOCALISABLE_TARGETS().contains(targetPreference)) {
            targetValue = this.mUnitSystemChangeListener.onValueFetched(targetValue);
        }
        if (TIME_TARGETS_LIST.contains(targetPreference)) {
            targetValue = TimeHelper.timeToString(getSharedPreferences().getLong(targetPreference.getKey(), 0L));
            if (targetPreference == TargetPreference.AVERAGE_PACE) {
                targetValue = PaceUtil.formatPace(Conversion.paceForLocale(Prefs.getTargetAveragePaceValue()), 5);
            }
        }
        if (listType == ListType.FIRST) {
            this.targetType1 = targetPreference;
            this.targetValue1 = targetValue;
            setListDisplay(this.list1, getCustomEntry(getPersistedValue(this.key1)), this.target1, targetValue, targetPreference != TargetPreference.NONE, unit);
        } else {
            this.targetType2 = targetPreference;
            this.targetValue2 = targetValue;
            setListDisplay(this.list2, getCustomEntry(getPersistedValue(this.key2)), this.target2, targetValue, targetPreference != TargetPreference.NONE, unit);
        }
    }

    private void setListDisplay(TextView list, CharSequence listValue, TextView target, String targetValue, boolean isTarget, String unit) {
        String targetValue2 = Utility.trimEmptyDecimal(targetValue);
        if (list != null) {
            list.setText(listValue);
        }
        if (target != null) {
            if (!unit.isEmpty()) {
                targetValue2 = targetValue2 + " " + unit;
            }
            target.setText(targetValue2);
            target.setTag(unit);
            target.setBackgroundResource(!isTarget ? R.color.pref_background : R.drawable.background_edittext_prefs);
        }
    }

    public int findIndexOfOriginalValue(String value) {
        if (value != null && this.mOriginalVals != null) {
            for (int i = 0; i < this.mOriginalVals.length; i++) {
                CharSequence s = this.mOriginalVals[i];
                if (s != null && value.contentEquals(s)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    private CharSequence getCustomEntry(String value) {
        int index = findIndexOfOriginalValue(value);
        return (index <= 0 || index >= this.mOriginalEnts.length) ? getContext().getString(R.string.tap_to_add) : this.mOriginalEnts[index];
    }

    public void setUnitSystemChangeListener(ValidatedEditTextPreference.UnitSystemChangeListener listener) {
        this.mUnitSystemChangeListener = listener;
    }
}
