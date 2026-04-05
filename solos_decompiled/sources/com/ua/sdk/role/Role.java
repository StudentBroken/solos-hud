package com.ua.sdk.role;

import com.ua.sdk.Resource;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface Role extends Resource {

    public enum Permission {
        EDIT,
        CREATE,
        MODERATE,
        ADMINISTER
    }

    String getDescription();

    Type getName();

    List<Permission> getPermissions();

    void setDescription(String str);

    void setName(Type type);

    void setPermissions(List<Permission> list);

    public enum Type {
        CREATOR("CREATOR"),
        MODERATOR("MODERATOR"),
        EDITOR("EDITOR"),
        ADMINISTRATOR("ADMINISTRATOR");

        String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static Type parse(String value) {
            Type[] arr$ = values();
            for (Type type : arr$) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Value passed is not known!");
        }
    }
}
