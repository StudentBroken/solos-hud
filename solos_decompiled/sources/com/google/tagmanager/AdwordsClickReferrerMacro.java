package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class AdwordsClickReferrerMacro extends FunctionCallImplementation {
    private final Context context;
    private static final String ID = FunctionType.ADWORDS_CLICK_REFERRER.toString();
    private static final String COMPONENT = Key.COMPONENT.toString();
    private static final String CONVERSION_ID = Key.CONVERSION_ID.toString();

    public static String getFunctionId() {
        return ID;
    }

    public AdwordsClickReferrerMacro(Context context) {
        super(ID, CONVERSION_ID);
        this.context = context;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        TypeSystem.Value conversionIdValue = parameters.get(CONVERSION_ID);
        if (conversionIdValue == null) {
            return Types.getDefaultValue();
        }
        String conversionId = Types.valueToString(conversionIdValue);
        TypeSystem.Value componentValue = parameters.get(COMPONENT);
        String component = componentValue != null ? Types.valueToString(componentValue) : null;
        String referrer = InstallReferrerUtil.getClickReferrer(this.context, conversionId, component);
        return referrer != null ? Types.objectToValue(referrer) : Types.getDefaultValue();
    }
}
