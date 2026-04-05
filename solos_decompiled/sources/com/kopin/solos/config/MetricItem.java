package com.kopin.solos.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import com.facebook.appevents.AppEventsConstants;
import com.kopin.solos.metrics.MetricResource;
import java.util.concurrent.ThreadLocalRandom;

/* JADX INFO: loaded from: classes37.dex */
class MetricItem {
    private static final String PREF_FILE = "config_metric_prefs";
    private static final String PREK_KEY = "metric_item_";
    private static final String SEPARATOR = ";";
    private final CheckBox checkBox;
    private final EditText edMax;
    private final EditText edMin;
    private final String key;
    private Double max;
    private Double min;
    private final SharedPreferences preferences;

    public MetricItem(Context context, MetricResource metricResource, CheckBox checkBox, EditText edMin, EditText edMax) {
        this.preferences = context.getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        this.key = PREK_KEY + metricResource.name();
        this.checkBox = checkBox;
        this.edMin = edMin;
        this.edMax = edMax;
        deserialize();
        checkBox.setText(metricResource.name().toLowerCase());
    }

    public MetricItem(Context context, MetricResource metricResource) {
        this.preferences = context.getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        this.key = PREK_KEY + metricResource.name();
        this.checkBox = null;
        this.edMin = null;
        this.edMax = null;
        deserialize();
    }

    String deserialize() {
        String data = this.preferences.getString(this.key, "");
        fromString(data);
        prepareValue();
        return data;
    }

    void serialize() {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(this.key, toString()).apply();
        prepareValue();
    }

    void serialize(String data) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(this.key, data).apply();
        prepareValue();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append((this.checkBox == null || !this.checkBox.isChecked()) ? 0 : 1).append(SEPARATOR).append((CharSequence) this.edMin.getText()).append(SEPARATOR).append((CharSequence) this.edMax.getText()).append(SEPARATOR);
        Log.d(this.key, "to " + builder.toString());
        return builder.toString();
    }

    private void fromString(String s) {
        Log.d(this.key, "from " + s);
        if (s != null && s.contains(SEPARATOR) && this.checkBox != null) {
            String[] parts = s.split(SEPARATOR);
            this.checkBox.setChecked(parts[0].equals(AppEventsConstants.EVENT_PARAM_VALUE_YES));
            this.edMin.setText(parts.length > 1 ? parts[1] : "");
            this.edMax.setText(parts.length > 2 ? parts[2] : "");
        }
    }

    private void prepareValue() {
        this.min = null;
        try {
            this.min = Double.valueOf(Double.parseDouble(this.edMin.getText().toString()));
        } catch (Exception e) {
        }
        this.max = null;
        try {
            this.max = Double.valueOf(Double.parseDouble(this.edMax.getText().toString()));
        } catch (Exception e2) {
        }
        if (this.min != null && this.max != null) {
            this.min = Double.valueOf(Math.min(this.min.doubleValue(), this.max.doubleValue()));
            this.max = Double.valueOf(Math.max(this.min.doubleValue(), this.max.doubleValue()));
        }
    }

    public Double getValue() {
        if (this.checkBox == null || !this.checkBox.isChecked() || (this.min == null && this.max == null)) {
            return null;
        }
        if (this.min.equals(this.max) || (this.min != null && this.max == null)) {
            return this.min;
        }
        if (this.min == null && this.max != null) {
            return this.max;
        }
        return Double.valueOf(ThreadLocalRandom.current().nextDouble(this.min.doubleValue(), this.max.doubleValue()));
    }
}
