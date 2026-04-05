package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug;
import com.google.analytics.midtier.proto.containertag.MutableTypeSystem;
import com.google.analytics.midtier.proto.containertag.TypeSystem;

/* JADX INFO: loaded from: classes49.dex */
class DebugValueBuilder implements ValueBuilder {
    private MutableTypeSystem.Value value;

    public DebugValueBuilder(MutableTypeSystem.Value value) {
        this.value = value;
    }

    private void validateType(MutableTypeSystem.Value.Type expected, MutableTypeSystem.Value.Type actual, String message) {
        if (!expected.equals(actual)) {
            throw new TypeMismatchException(message, actual);
        }
    }

    public static MutableTypeSystem.Value copyImmutableValue(TypeSystem.Value value) {
        MutableTypeSystem.Value result = MutableTypeSystem.Value.newMessage();
        if (!result.mergeFrom(value.toByteArray())) {
            Log.e("Failed to copy runtime value into debug value");
        }
        return result;
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getListItem(int index) {
        validateType(MutableTypeSystem.Value.Type.LIST, this.value.getType(), "add new list item");
        DebugValueBuilder listItemBuilder = new DebugValueBuilder(this.value.getListItem(index));
        return listItemBuilder;
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getMapKey(int index) {
        validateType(MutableTypeSystem.Value.Type.MAP, this.value.getType(), "add new map key");
        DebugValueBuilder mapKeyValueBuilder = new DebugValueBuilder(this.value.getMapKey(index));
        return mapKeyValueBuilder;
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getMapValue(int index) {
        validateType(MutableTypeSystem.Value.Type.MAP, this.value.getType(), "add new map value");
        DebugValueBuilder mapValueBuilder = new DebugValueBuilder(this.value.getMapValue(index));
        return mapValueBuilder;
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getTemplateToken(int index) {
        validateType(MutableTypeSystem.Value.Type.TEMPLATE, this.value.getType(), "add template token");
        DebugValueBuilder tokenValueBuilder = new DebugValueBuilder(this.value.getTemplateToken(index));
        return tokenValueBuilder;
    }

    @Override // com.google.tagmanager.ValueBuilder
    public MacroEvaluationInfoBuilder createValueMacroEvaluationInfoExtension() {
        validateType(MutableTypeSystem.Value.Type.MACRO_REFERENCE, this.value.getType(), "set macro evaluation extension");
        MacroEvaluationInfoBuilder macroEvaluationExtension = new DebugMacroEvaluationInfoBuilder((MutableDebug.MacroEvaluationInfo) this.value.getMutableExtension(MutableDebug.MacroEvaluationInfo.macro));
        return macroEvaluationExtension;
    }

    private static class TypeMismatchException extends IllegalStateException {
        public TypeMismatchException(String operation, MutableTypeSystem.Value.Type t) {
            super("Attempted operation: " + operation + " on object of type: " + t);
        }
    }
}
