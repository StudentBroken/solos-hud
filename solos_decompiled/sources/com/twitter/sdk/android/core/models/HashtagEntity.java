package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import com.kopin.pupil.ui.PageHelper;

/* JADX INFO: loaded from: classes62.dex */
public class HashtagEntity extends Entity {

    @SerializedName(PageHelper.TEXT_PART_TAG)
    public final String text;

    @Override // com.twitter.sdk.android.core.models.Entity
    public /* bridge */ /* synthetic */ int getEnd() {
        return super.getEnd();
    }

    @Override // com.twitter.sdk.android.core.models.Entity
    public /* bridge */ /* synthetic */ int getStart() {
        return super.getStart();
    }

    public HashtagEntity(String text, int start, int end) {
        super(start, end);
        this.text = text;
    }
}
