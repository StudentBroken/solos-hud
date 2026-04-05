package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class InstallReferrerMacro extends FunctionCallImplementation {
    private final Context context;
    private static final String ID = FunctionType.INSTALL_REFERRER.toString();
    private static final String COMPONENT = Key.COMPONENT.toString();

    public static String getFunctionId() {
        return ID;
    }

    public InstallReferrerMacro(Context context) {
        super(ID, new String[0]);
        this.context = context;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        TypeSystem.Value componentValue = parameters.get(COMPONENT);
        String component = componentValue != null ? Types.valueToString(parameters.get(COMPONENT)) : null;
        String referrer = InstallReferrerUtil.getInstallReferrer(this.context, component);
        return referrer != null ? Types.objectToValue(referrer) : Types.getDefaultValue();
    }
}
