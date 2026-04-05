package com.google.tagmanager;

import android.os.Build;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class DeviceNameMacro extends FunctionCallImplementation {
    private static final String ID = FunctionType.DEVICE_NAME.toString();

    public static String getFunctionId() {
        return ID;
    }

    public DeviceNameMacro() {
        super(ID, new String[0]);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (!model.startsWith(manufacturer) && !manufacturer.equals("unknown")) {
            model = manufacturer + " " + model;
        }
        return Types.objectToValue(model);
    }
}
