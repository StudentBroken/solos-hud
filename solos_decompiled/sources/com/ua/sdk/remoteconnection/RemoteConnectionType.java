package com.ua.sdk.remoteconnection;

import com.ua.sdk.Entity;

/* JADX INFO: loaded from: classes65.dex */
public interface RemoteConnectionType extends Entity {
    String getDisconnectCancelStr();

    String getDisconnectConfirmStr();

    String getDisconnectStr();

    String getIntroBodyStr();

    String getIntroHeadingStr();

    String getLogoLink();

    String getLogoLinkLight();

    String getName();

    String getOAuthLink();

    String getRecorderTypeKey();

    String getType();

    void setDisconnectCancelStr(String str);

    void setDisconnectConfirmStr(String str);

    void setDisconnectStr(String str);

    void setIntroBodyStr(String str);

    void setIntroHeadingStr(String str);

    void setLogoLink(String str);

    void setLogoLinkLight(String str);

    void setName(String str);

    void setOAuthLink(String str);

    void setRecorderTypeKey(String str);

    void setType(String str);
}
