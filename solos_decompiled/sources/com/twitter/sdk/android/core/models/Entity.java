package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
class Entity implements Serializable {
    private static final int END_INDEX = 1;
    private static final int START_INDEX = 0;

    @SerializedName("indices")
    public final List<Integer> indices;

    public Entity(int start, int end) {
        List<Integer> temp = new ArrayList<>(2);
        temp.add(0, Integer.valueOf(start));
        temp.add(1, Integer.valueOf(end));
        this.indices = Collections.unmodifiableList(temp);
    }

    public int getStart() {
        return this.indices.get(0).intValue();
    }

    public int getEnd() {
        return this.indices.get(1).intValue();
    }
}
