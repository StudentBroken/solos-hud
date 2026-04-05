package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class LessEqualsPredicate extends NumberPredicate {
    private static final String ID = FunctionType.LESS_EQUALS.toString();

    public static String getFunctionId() {
        return ID;
    }

    public LessEqualsPredicate() {
        super(ID);
    }

    @Override // com.google.tagmanager.NumberPredicate
    protected boolean evaluateNumber(TypedNumber arg0, TypedNumber arg1, Map<String, TypeSystem.Value> parameters) {
        return arg0.compareTo(arg1) <= 0;
    }
}
