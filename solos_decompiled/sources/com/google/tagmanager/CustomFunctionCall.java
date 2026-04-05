package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class CustomFunctionCall extends FunctionCallImplementation {
    private final CustomEvaluator mFunctionCallEvaluator;
    private static final String ID = FunctionType.FUNCTION_CALL.toString();
    private static final String FUNCTION_CALL_NAME = Key.FUNCTION_CALL_NAME.toString();
    private static final String ADDITIONAL_PARAMS = Key.ADDITIONAL_PARAMS.toString();

    public interface CustomEvaluator {
        Object evaluate(String str, Map<String, Object> map);
    }

    public static String getFunctionId() {
        return ID;
    }

    public static String getFunctionCallNameKey() {
        return FUNCTION_CALL_NAME;
    }

    public static String getAdditionalParamsKey() {
        return ADDITIONAL_PARAMS;
    }

    public CustomFunctionCall(CustomEvaluator functionCallEvaluator) {
        super(ID, FUNCTION_CALL_NAME);
        this.mFunctionCallEvaluator = functionCallEvaluator;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return false;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        String functionCallName = Types.valueToString(parameters.get(FUNCTION_CALL_NAME));
        Map<String, Object> objectParams = new HashMap<>();
        TypeSystem.Value additionalParamsValue = parameters.get(ADDITIONAL_PARAMS);
        if (additionalParamsValue != null) {
            Object additionalParamsObject = Types.valueToObject(additionalParamsValue);
            if (!(additionalParamsObject instanceof Map)) {
                Log.w("FunctionCallMacro: expected ADDITIONAL_PARAMS to be a map.");
                return Types.getDefaultValue();
            }
            Map<Object, Object> additionalParams = (Map) additionalParamsObject;
            for (Map.Entry<Object, Object> entry : additionalParams.entrySet()) {
                objectParams.put(entry.getKey().toString(), entry.getValue());
            }
        }
        try {
            return Types.objectToValue(this.mFunctionCallEvaluator.evaluate(functionCallName, objectParams));
        } catch (Exception e) {
            Log.w("Custom macro/tag " + functionCallName + " threw exception " + e.getMessage());
            return Types.getDefaultValue();
        }
    }
}
