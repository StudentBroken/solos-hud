package com.google.android.gms.maps.model;

/* JADX INFO: loaded from: classes10.dex */
public final class Dash extends PatternItem {
    public final float length;

    public Dash(float f) {
        super(0, Float.valueOf(Math.max(f, 0.0f)));
        this.length = Math.max(f, 0.0f);
    }

    @Override // com.google.android.gms.maps.model.PatternItem
    public final String toString() {
        return new StringBuilder(30).append("[Dash: length=").append(this.length).append("]").toString();
    }
}
