package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class NoopValueBuilder implements ValueBuilder {
    NoopValueBuilder() {
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getListItem(int index) {
        return new NoopValueBuilder();
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getMapKey(int index) {
        return new NoopValueBuilder();
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getMapValue(int index) {
        return new NoopValueBuilder();
    }

    @Override // com.google.tagmanager.ValueBuilder
    public ValueBuilder getTemplateToken(int index) {
        return new NoopValueBuilder();
    }

    @Override // com.google.tagmanager.ValueBuilder
    public MacroEvaluationInfoBuilder createValueMacroEvaluationInfoExtension() {
        return new NoopMacroEvaluationInfoBuilder();
    }
}
