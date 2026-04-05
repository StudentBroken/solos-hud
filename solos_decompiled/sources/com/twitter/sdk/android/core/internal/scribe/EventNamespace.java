package com.twitter.sdk.android.core.internal.scribe;

import android.support.wearable.watchface.WatchFaceStyle;
import com.facebook.internal.NativeProtocol;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class EventNamespace {

    @SerializedName(NativeProtocol.WEB_DIALOG_ACTION)
    public final String action;

    @SerializedName("client")
    public final String client;

    @SerializedName(WatchFaceStyle.KEY_COMPONENT)
    public final String component;

    @SerializedName("element")
    public final String element;

    @SerializedName("page")
    public final String page;

    @SerializedName("section")
    public final String section;

    public EventNamespace(String client, String page, String section, String component, String element, String action) {
        this.client = client;
        this.page = page;
        this.section = section;
        this.component = component;
        this.element = element;
        this.action = action;
    }

    public String toString() {
        return "client=" + this.client + ", page=" + this.page + ", section=" + this.section + ", component=" + this.component + ", element=" + this.element + ", action=" + this.action;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventNamespace that = (EventNamespace) o;
        if (this.action == null ? that.action != null : !this.action.equals(that.action)) {
            return false;
        }
        if (this.client == null ? that.client != null : !this.client.equals(that.client)) {
            return false;
        }
        if (this.component == null ? that.component != null : !this.component.equals(that.component)) {
            return false;
        }
        if (this.element == null ? that.element != null : !this.element.equals(that.element)) {
            return false;
        }
        if (this.page == null ? that.page != null : !this.page.equals(that.page)) {
            return false;
        }
        if (this.section != null) {
            if (this.section.equals(that.section)) {
                return true;
            }
        } else if (that.section == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.client != null ? this.client.hashCode() : 0;
        return (((((((((result * 31) + (this.page != null ? this.page.hashCode() : 0)) * 31) + (this.section != null ? this.section.hashCode() : 0)) * 31) + (this.component != null ? this.component.hashCode() : 0)) * 31) + (this.element != null ? this.element.hashCode() : 0)) * 31) + (this.action != null ? this.action.hashCode() : 0);
    }

    public static class Builder {
        private String action;
        private String client;
        private String component;
        private String element;
        private String page;
        private String section;

        public Builder setClient(String client) {
            this.client = client;
            return this;
        }

        public Builder setPage(String page) {
            this.page = page;
            return this;
        }

        public Builder setSection(String section) {
            this.section = section;
            return this;
        }

        public Builder setComponent(String component) {
            this.component = component;
            return this;
        }

        public Builder setElement(String element) {
            this.element = element;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public EventNamespace builder() {
            return new EventNamespace(this.client, this.page, this.section, this.component, this.element, this.action);
        }
    }
}
