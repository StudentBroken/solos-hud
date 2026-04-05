package com.ua.oss.org.apache.http.entity.mime;

/* JADX INFO: loaded from: classes65.dex */
public class MinimalField {
    private final String name;
    private final String value;

    public MinimalField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return this.value;
    }

    public String toString() {
        return this.name + ": " + this.value;
    }
}
