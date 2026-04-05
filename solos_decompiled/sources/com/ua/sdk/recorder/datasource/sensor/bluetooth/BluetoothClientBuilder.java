package com.ua.sdk.recorder.datasource.sensor.bluetooth;

import com.ua.sdk.recorder.data.BluetoothServiceType;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothArmour39Service;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothBatteryService;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothCscService;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothCyclingPowerService;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothHeartRateService;
import com.ua.sdk.recorder.datasource.sensor.bluetooth.services.BluetoothRscService;
import java.util.Set;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes65.dex */
public class BluetoothClientBuilder {
    private Set<BluetoothServiceType> serviceTypes;

    public BluetoothClientBuilder(Set<BluetoothServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public BluetoothClient build() {
        Callable<BluetoothClient> clientCallable = createClientCallable(this.serviceTypes);
        try {
            return clientCallable.call();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create a new bluetooth client!", e);
        }
    }

    private Callable<BluetoothClient> createClientCallable(final Set<BluetoothServiceType> types) {
        return new Callable<BluetoothClient>() { // from class: com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClientBuilder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public BluetoothClient call() throws Exception {
                BaseGattCallback baseGattCallback = new BaseGattCallback();
                baseGattCallback.addGattCallbackListener(new BluetoothBatteryService());
                for (BluetoothServiceType type : types) {
                    switch (AnonymousClass2.$SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType[type.ordinal()]) {
                        case 1:
                            baseGattCallback.addGattCallbackListener(new BluetoothHeartRateService());
                            break;
                        case 2:
                            baseGattCallback.addGattCallbackListener(new BluetoothRscService());
                            break;
                        case 3:
                            baseGattCallback.addGattCallbackListener(new BluetoothCscService());
                            break;
                        case 4:
                            baseGattCallback.addGattCallbackListener(new BluetoothCyclingPowerService());
                            break;
                        case 5:
                            baseGattCallback.addGattCallbackListener(new BluetoothArmour39Service());
                            break;
                    }
                }
                return new BluetoothConnection(baseGattCallback);
            }
        };
    }

    /* JADX INFO: renamed from: com.ua.sdk.recorder.datasource.sensor.bluetooth.BluetoothClientBuilder$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType = new int[BluetoothServiceType.values().length];

        static {
            try {
                $SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType[BluetoothServiceType.HEART_RATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType[BluetoothServiceType.RUN_SPEED_CADENCE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType[BluetoothServiceType.BIKE_SPEED_CADENCE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType[BluetoothServiceType.BIKE_POWER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$ua$sdk$recorder$data$BluetoothServiceType[BluetoothServiceType.ARMOUR_39.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }
}
