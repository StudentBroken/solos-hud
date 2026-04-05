package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class ContainsPredicate extends StringPredicate {
    private static final String ID = FunctionType.CONTAINS.toString();

    public static String getFunctionId() {
        return ID;
    }

    public ContainsPredicate() {
        super(ID);
    }

    @Override // com.google.tagmanager.StringPredicate
    protected boolean evaluateString(String arg0, String arg1, Map<String, TypeSystem.Value> parameters) {
        return arg0.contains(arg1);
    }
}
