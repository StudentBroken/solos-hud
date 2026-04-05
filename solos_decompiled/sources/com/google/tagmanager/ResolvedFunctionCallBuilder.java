package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;

/* JADX INFO: loaded from: classes49.dex */
interface ResolvedFunctionCallBuilder {
    ResolvedPropertyBuilder createResolvedPropertyBuilder(String str);

    void setFunctionResult(TypeSystem.Value value);
}
