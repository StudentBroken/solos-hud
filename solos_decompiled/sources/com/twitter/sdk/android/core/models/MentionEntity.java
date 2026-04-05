package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class MentionEntity extends Entity {

    @SerializedName("id")
    public final long id;

    @SerializedName("id_str")
    public final String idStr;

    @SerializedName("name")
    public final String name;

    @SerializedName("screen_name")
    public final String screenName;

    @Override // com.twitter.sdk.android.core.models.Entity
    public /* bridge */ /* synthetic */ int getEnd() {
        return super.getEnd();
    }

    @Override // com.twitter.sdk.android.core.models.Entity
    public /* bridge */ /* synthetic */ int getStart() {
        return super.getStart();
    }

    public MentionEntity(long id, String idStr, String name, String screenName, int start, int end) {
        super(start, end);
        this.id = id;
        this.idStr = idStr;
        this.name = name;
        this.screenName = screenName;
    }
}
