package com.kopin.pupil.util;

import android.os.Bundle;

/* JADX INFO: loaded from: classes25.dex */
public class Action {
    private Type mActionType;
    private String mArg;
    private Bundle mBundle;
    private String mName;
    private String mPageID;
    private String mTemplateID;

    public enum Type {
        CHANGING_PAGE,
        CHANGED_PAGE,
        BUTTON_PRESS_SHORT,
        BUTTON_PRESS_LONG
    }

    public Action() {
        this.mActionType = null;
        this.mPageID = "";
        this.mTemplateID = "";
        this.mName = null;
        this.mArg = null;
        this.mBundle = new Bundle();
    }

    public Action(Action source) {
        this.mActionType = null;
        this.mPageID = "";
        this.mTemplateID = "";
        this.mName = null;
        this.mArg = null;
        this.mBundle = new Bundle();
        this.mActionType = source.mActionType;
        this.mPageID = source.mPageID;
        this.mTemplateID = source.mTemplateID;
        this.mName = source.mName;
        this.mArg = source.mArg;
        if (source.mBundle != null) {
            this.mBundle = new Bundle(source.mBundle);
        }
    }

    public void set(Action action) {
        if (action != null) {
            this.mPageID = action.mPageID;
            this.mBundle = action.mBundle;
            this.mArg = action.mArg;
            this.mName = action.mName;
        }
    }

    public Type getType() {
        return this.mActionType;
    }

    public void setType(Type type) {
        this.mActionType = type;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPageID() {
        return this.mPageID;
    }

    public void setPageID(String pageID) {
        this.mPageID = pageID;
    }

    public String getTemplateID() {
        return this.mTemplateID;
    }

    public void setTemplateID(String templateID) {
        this.mTemplateID = templateID;
    }

    public Bundle getExtra() {
        return this.mBundle == null ? new Bundle() : this.mBundle;
    }

    public void setExtra(Bundle bundle) {
        this.mBundle = bundle;
    }

    public String getArg() {
        return this.mArg;
    }

    public void setArg(String arg) {
        this.mArg = arg;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Action [type: ").append(this.mActionType);
        sb.append(" ; pageID: ").append(this.mPageID);
        sb.append(" ; extra: ").append(this.mBundle);
        sb.append(" ; arg: ").append(this.mArg);
        return sb.append("]").toString();
    }
}
