package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class DataLayerMacro extends FunctionCallImplementation {
    private final DataLayer mDataLayer;
    private static final String ID = FunctionType.CUSTOM_VAR.toString();
    private static final String NAME = Key.NAME.toString();
    private static final String DEFAULT_VALUE = Key.DEFAULT_VALUE.toString();

    public static String getFunctionId() {
        return ID;
    }

    public DataLayerMacro(DataLayer dataLayer) {
        super(ID, NAME);
        this.mDataLayer = dataLayer;
    }

    public static String getNameKey() {
        return NAME;
    }

    public static String getDefaultValueKey() {
        return DEFAULT_VALUE;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return false;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        Object dataLayerValue = this.mDataLayer.get(Types.valueToString(parameters.get(NAME)));
        if (dataLayerValue == null) {
            TypeSystem.Value defaultValue = parameters.get(DEFAULT_VALUE);
            if (defaultValue == null) {
                return Types.getDefaultValue();
            }
            return defaultValue;
        }
        return Types.objectToValue(dataLayerValue);
    }
}
