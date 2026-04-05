package com.kopin.solos.menu;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.view.InputDeviceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.util.PaceUtil;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class SensorMenuAdapter extends CustomMenuAdapter<SensorMenuItem> {
    private LayoutInflater mInflater;

    public enum SensorMenuItemType {
        SENSOR,
        OPTION
    }

    public SensorMenuAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void addMenuItem(SensorMenuItem menuItem) {
        menuItem.mMenuAdapter = this;
        super.addMenuItem(menuItem);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void doCleanUp() {
        for (int i = 0; i < getCount(); i++) {
            getMenuItem(i).mMenuAdapter = null;
        }
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SensorMenuItem item = getMenuItem(position);
        switch (item.getType()) {
            case SENSOR:
                return getViewSensor(item, convertView, parent);
            case OPTION:
                return getViewMenu(item, convertView, parent);
            default:
                return null;
        }
    }

    private View getViewSensor(SensorMenuItem item, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.sensor_menu_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageSensor = (ImageView) convertView.findViewById(R.id.imageSensor);
            viewHolder.imageConnectionState = (ImageView) convertView.findViewById(R.id.imageConnectionState);
            viewHolder.textSensorData = (TextView) convertView.findViewById(R.id.textSensorData);
            viewHolder.textSensorName = (TextView) convertView.findViewById(R.id.textSensorName);
            viewHolder.textSensorType = (TextView) convertView.findViewById(R.id.textSensorType);
            viewHolder.type = item.getType();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.type != item.getType()) {
                return getViewSensor(item, null, parent);
            }
        }
        viewHolder.setEnabled(item.isEnabled());
        viewHolder.imageSensor.setImageResource(getImageResource(item.mSensorType));
        String extra = item.getExtra();
        if (extra == null) {
            viewHolder.textSensorName.setText(R.string.sensor_not_connected);
        } else {
            viewHolder.textSensorName.setText(extra);
        }
        viewHolder.textSensorType.setText(item.getTitle());
        String value = "---";
        if (item.getState() == Sensor.ConnectionState.CONNECTED && item.getValue() != Integer.MIN_VALUE) {
            value = item.getSensorDataType() == Sensor.DataType.PACE ? PaceUtil.formatPace(item.getValue()) : String.valueOf(item.getValue());
        }
        viewHolder.textSensorData.setText(String.format(Locale.US, "%s %s", value, item.getUnit()));
        switch (item.getState()) {
            case CONNECTED:
                viewHolder.imageConnectionState.setColorFilter(-16711936, PorterDuff.Mode.MULTIPLY);
                viewHolder.textSensorName.setTextColor(-16711936);
                break;
            case CONNECTING:
                viewHolder.imageConnectionState.setColorFilter(InputDeviceCompat.SOURCE_ANY, PorterDuff.Mode.MULTIPLY);
                viewHolder.textSensorName.setTextColor(InputDeviceCompat.SOURCE_ANY);
                break;
            case DISCONNECTED:
                viewHolder.imageConnectionState.setColorFilter(-7829368, PorterDuff.Mode.MULTIPLY);
                viewHolder.textSensorName.setTextColor(-7829368);
                break;
            case DISCONNECTING:
                viewHolder.imageConnectionState.setColorFilter(InputDeviceCompat.SOURCE_ANY, PorterDuff.Mode.MULTIPLY);
                viewHolder.textSensorName.setTextColor(InputDeviceCompat.SOURCE_ANY);
                break;
            case RECONNECTING:
                viewHolder.imageConnectionState.setColorFilter(InputDeviceCompat.SOURCE_ANY, PorterDuff.Mode.MULTIPLY);
                viewHolder.textSensorName.setTextColor(InputDeviceCompat.SOURCE_ANY);
                break;
            default:
                viewHolder.imageConnectionState.setColorFilter(-7829368, PorterDuff.Mode.MULTIPLY);
                viewHolder.textSensorName.setTextColor(-7829368);
                break;
        }
        return convertView;
    }

    private int getImageResource(Sensor.DataType type) {
        switch (type) {
            case POWER:
                return R.drawable.ic_power_small;
            case SPEED:
                return R.drawable.ic_ic_speed_icon;
            case PACE:
                return R.drawable.ic_pace;
            case HEARTRATE:
                return R.drawable.ic_heart_rate_small;
            case CADENCE:
                return R.drawable.ic_cadence_small;
            case STEP:
                return R.drawable.ic_cadence_run;
            case STRIDE:
                return R.drawable.ic_stride_small;
            case KICK:
                return R.drawable.ic_run_power_small;
            default:
                return 0;
        }
    }

    private View getViewMenu(SensorMenuItem item, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.sensor_menu_item_option, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textSensorName = (TextView) convertView.findViewById(R.id.item_device_description);
            viewHolder.type = item.getType();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.type != item.getType()) {
                return getViewMenu(item, null, parent);
            }
        }
        viewHolder.setEnabled(item.isEnabled());
        viewHolder.textSensorName.setText(item.getTitle());
        return convertView;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return getMenuItem(position).getType().ordinal();
    }

    private static class ViewHolder {
        ImageView imageConnectionState;
        ImageView imageSensor;
        TextView textSensorData;
        TextView textSensorName;
        TextView textSensorType;
        SensorMenuItemType type;

        private ViewHolder() {
        }

        void setEnabled(boolean enabled) {
            if (this.imageConnectionState != null) {
                this.imageConnectionState.setEnabled(enabled);
            }
            if (this.textSensorName != null) {
                this.textSensorName.setEnabled(enabled);
            }
            if (this.textSensorData != null) {
                this.textSensorData.setEnabled(enabled);
            }
            if (this.textSensorType != null) {
                this.textSensorType.setEnabled(enabled);
            }
        }
    }

    public static class SensorMenuItem extends CustomMenuItem {
        private String mExtra;
        private SensorMenuAdapter mMenuAdapter;
        private Sensor.DataType mSensorType;
        private Sensor.ConnectionState mState;
        private SensorMenuItemType mType;
        private String mUnit;
        private int mValue;

        public SensorMenuItem(String title, int id, String unit) {
            this(Sensor.DataType.UNKOWN, title, id, unit);
        }

        public SensorMenuItem(Sensor.DataType type, String title, int id, String unit) {
            super(title, 0, id);
            this.mState = Sensor.ConnectionState.DISCONNECTED;
            this.mType = SensorMenuItemType.SENSOR;
            this.mUnit = unit;
            this.mSensorType = type;
        }

        @Override // com.kopin.solos.menu.CustomMenuItem
        public void onDataChanged() {
            if (this.mMenuAdapter != null) {
                this.mMenuAdapter.notifyDataSetChanged();
            }
        }

        public String getUnit() {
            return this.mUnit;
        }

        public int getValue() {
            return this.mValue;
        }

        public String getExtra() {
            return this.mExtra;
        }

        public void setValue(int value) {
            if (this.mValue != value) {
                this.mValue = value;
                onDataChanged();
            }
        }

        public void setUnit(String unit) {
            this.mUnit = unit;
        }

        public Sensor.ConnectionState getState() {
            return this.mState;
        }

        public void setState(Sensor.ConnectionState state) {
            if (this.mState != state) {
                this.mState = state;
                onDataChanged();
            }
        }

        public SensorMenuItemType getType() {
            return this.mType;
        }

        public Sensor.DataType getSensorDataType() {
            return this.mSensorType;
        }

        public void setType(SensorMenuItemType type) {
            this.mType = type;
        }

        public void update(String title, String unit, String extra, Sensor.ConnectionState state) {
            setTitle(title);
            this.mUnit = unit;
            this.mExtra = extra;
            this.mState = state;
            onDataChanged();
        }

        public boolean isSensorType(Sensor.DataType type) {
            return this.mSensorType.equals(type);
        }

        public static SensorMenuItem createSensorItem(Sensor.DataType type, int id, String title, String unit, String extra) {
            SensorMenuItem ret = new SensorMenuItem(title, id, unit);
            ret.mSensorType = type;
            ret.mExtra = extra;
            return ret;
        }
    }
}
