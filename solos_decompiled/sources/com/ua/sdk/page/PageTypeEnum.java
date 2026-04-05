package com.ua.sdk.page;

/* JADX INFO: loaded from: classes65.dex */
public enum PageTypeEnum {
    PERSONAL("personal"),
    PUBLIC_ENTITY("public_entity"),
    PUBLIC_FIGURE("public_figure");

    private final String name;

    PageTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static PageTypeEnum getById(String id) {
        PageTypeEnum[] arr$ = values();
        for (PageTypeEnum type : arr$) {
            if (type.toString().toLowerCase().equals(id)) {
                return type;
            }
        }
        return null;
    }
}
