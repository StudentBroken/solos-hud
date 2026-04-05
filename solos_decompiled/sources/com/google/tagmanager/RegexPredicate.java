package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: loaded from: classes49.dex */
class RegexPredicate extends StringPredicate {
    private static final String ID = FunctionType.REGEX.toString();
    private static final String IGNORE_CASE = Key.IGNORE_CASE.toString();

    public static String getFunctionId() {
        return ID;
    }

    public static String getIgnoreCaseKey() {
        return IGNORE_CASE;
    }

    public RegexPredicate() {
        super(ID);
    }

    @Override // com.google.tagmanager.StringPredicate
    protected boolean evaluateString(String arg0, String arg1, Map<String, TypeSystem.Value> parameters) {
        int flags = 64;
        if (Types.valueToBoolean(parameters.get(IGNORE_CASE)).booleanValue()) {
            flags = 64 | 2;
        }
        try {
            return Pattern.compile(arg1, flags).matcher(arg0).find();
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}
