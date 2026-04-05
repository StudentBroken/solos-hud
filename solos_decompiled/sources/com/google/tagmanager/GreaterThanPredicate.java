package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class GreaterThanPredicate extends NumberPredicate {
    private static final String ID = FunctionType.GREATER_THAN.toString();

    public static String getFunctionId() {
        return ID;
    }

    public GreaterThanPredicate() {
        super(ID);
    }

    @Override // com.google.tagmanager.NumberPredicate
    protected boolean evaluateNumber(TypedNumber arg0, TypedNumber arg1, Map<String, TypeSystem.Value> parameters) {
        return arg0.compareTo(arg1) > 0;
    }
}
