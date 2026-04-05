package com.kopin.solos.sensors;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.sensors.Sensor;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes28.dex */
public class SensorListAdapter extends BaseAdapter {
    private final Context mContext;
    private List<Sensor> mList = new ArrayList();

    public SensorListAdapter(Activity ctx) {
        this.mContext = ctx;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public void setList(List<Sensor> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final Sensor params = this.mList.get(position);
        View view = View.inflate(this.mContext, R.layout.sensor_device_list_item, null);
        TextView name = (TextView) view.findViewById(R.id.device_name);
        TextView description = (TextView) view.findViewById(R.id.device_description);
        Button button = (Button) view.findViewById(R.id.btn_device_connect);
        ImageView icon = (ImageView) view.findViewById(R.id.device_sensor_icon);
        boolean hasConfig = params.hasConfig();
        ImageView iconSensorConfig = (ImageView) view.findViewById(R.id.sensor_config_icon);
        iconSensorConfig.setVisibility(hasConfig ? 0 : 8);
        iconSensorConfig.setOnClickListener(hasConfig ? new View.OnClickListener() { // from class: com.kopin.solos.sensors.SensorListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SensorsConnector.configureSensor(SensorListAdapter.this.mContext, params);
            }
        } : null);
        name.setText(params != null ? params.getName() : "N/A");
        description.setText(params != null ? params.getTypeName() : R.string.sensor_desc_unknown);
        Sensor.ConnectionState connectionState = params != null ? params.getConnectionState() : Sensor.ConnectionState.ERROR;
        switch (connectionState) {
            case CONNECTED:
                button.setVisibility(0);
                button.setEnabled(true);
                button.setText(R.string.forget);
                icon.setColorFilter(-16711936, PorterDuff.Mode.MULTIPLY);
                break;
            case CONNECTING:
                icon.setColorFilter(InputDeviceCompat.SOURCE_ANY, PorterDuff.Mode.MULTIPLY);
                button.setEnabled(false);
                button.setText(R.string.connect);
                break;
            case DISCONNECTED:
                icon.setColorFilter(SupportMenu.CATEGORY_MASK, PorterDuff.Mode.MULTIPLY);
                button.setEnabled(true);
                button.setText(R.string.connect);
                break;
            case DISCONNECTING:
                icon.setColorFilter(InputDeviceCompat.SOURCE_ANY, PorterDuff.Mode.MULTIPLY);
                button.setEnabled(false);
                button.setText(R.string.forget);
                break;
            case RECONNECTING:
                button.setVisibility(0);
                button.setEnabled(true);
                button.setText(R.string.forget);
                icon.setColorFilter(InputDeviceCompat.SOURCE_ANY, PorterDuff.Mode.MULTIPLY);
                break;
            default:
                icon.setColorFilter(-7829368, PorterDuff.Mode.MULTIPLY);
                button.setEnabled(false);
                button.setText("error");
                break;
        }
        if (params != null) {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.sensors.SensorListAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    switch (AnonymousClass3.$SwitchMap$com$kopin$solos$sensors$Sensor$ConnectionState[params.getConnectionState().ordinal()]) {
                        case 1:
                        case 5:
                            params.disconnect();
                            SensorList.forgetDevice(params);
                            break;
                        case 2:
                        case 4:
                        default:
                            return;
                        case 3:
                            params.connect();
                            SensorList.saveDevice(params);
                            break;
                    }
                    SensorListAdapter.this.notifyDataSetChanged();
                }
            });
        } else {
            button.setOnClickListener(null);
        }
        return view;
    }
}
