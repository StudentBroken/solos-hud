package com.kopin.pupil.util;

/* JADX INFO: loaded from: classes25.dex */
public class UpdateElementValues {
    public String attribute;
    public String elementId;
    public Object value;

    public UpdateElementValues(String elementId, String attribute, Object value) {
        this.elementId = "";
        this.value = null;
        this.elementId = elementId;
        this.attribute = attribute;
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof UpdateElementValues)) {
            return false;
        }
        return this.elementId.equals(((UpdateElementValues) o).elementId);
    }
}
