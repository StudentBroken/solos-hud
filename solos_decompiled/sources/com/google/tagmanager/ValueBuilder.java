package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
interface ValueBuilder {
    MacroEvaluationInfoBuilder createValueMacroEvaluationInfoExtension();

    ValueBuilder getListItem(int i);

    ValueBuilder getMapKey(int i);

    ValueBuilder getMapValue(int i);

    ValueBuilder getTemplateToken(int i);
}
