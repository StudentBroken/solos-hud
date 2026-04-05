package com.kopin.pupil.util;

import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes25.dex */
public class XmlParserPackage {
    private String mPageName;
    private XmlPullParser mParser;
    private String mTemplate;
    private int mVersion;

    public XmlParserPackage(XmlPullParser parser, int version, String pageName, String template) {
        this.mParser = parser;
        this.mVersion = version;
        this.mPageName = pageName;
        this.mTemplate = template;
    }

    public XmlPullParser getParser() {
        return this.mParser;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public String getPageName() {
        return this.mPageName;
    }

    public String getTemplate() {
        return this.mTemplate;
    }
}
