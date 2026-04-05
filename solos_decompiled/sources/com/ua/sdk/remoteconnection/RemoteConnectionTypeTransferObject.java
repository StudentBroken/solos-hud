package com.ua.sdk.remoteconnection;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeTransferObject extends ApiTransferObject {

    @SerializedName("disconnect_cancel")
    String disconnectCancel;

    @SerializedName("disconnect_confirm")
    String disconnectConfirm;

    @SerializedName("disconnect_copy")
    String disconnectCopy;

    @SerializedName("intro_copy_body")
    String introCopyBody;

    @SerializedName("intro_copy_heading")
    String introCopyHeading;

    @SerializedName("logo_link")
    String logoLink;

    @SerializedName("logo_link_light")
    String logoLinkLight;

    @SerializedName("name")
    String name;

    @SerializedName("oauth_connect_link")
    String oauthConnectLink;

    @SerializedName("recorder_type_key")
    String recorderTypeKey;

    @SerializedName(ShareConstants.MEDIA_TYPE)
    String type;

    public static RemoteConnectionTypeTransferObject fromRemoteConnectionType(RemoteConnectionType rct) {
        if (rct == null) {
            return null;
        }
        RemoteConnectionTypeTransferObject to = new RemoteConnectionTypeTransferObject();
        to.type = rct.getType();
        to.recorderTypeKey = rct.getRecorderTypeKey();
        to.name = rct.getName();
        to.introCopyHeading = rct.getIntroHeadingStr();
        to.introCopyBody = rct.getIntroBodyStr();
        to.disconnectCopy = rct.getDisconnectStr();
        to.disconnectCancel = rct.getDisconnectCancelStr();
        to.disconnectConfirm = rct.getDisconnectConfirmStr();
        to.logoLink = rct.getLogoLink();
        to.logoLinkLight = rct.getLogoLinkLight();
        to.oauthConnectLink = rct.getOAuthLink();
        if (rct instanceof RemoteConnectionTypeImpl) {
            to.setLinkMap(((RemoteConnectionTypeImpl) rct).getLinkMap());
            return to;
        }
        return to;
    }

    public static RemoteConnectionTypeImpl toRemoteConnectionTypeImpl(RemoteConnectionTypeTransferObject obj) throws UaException {
        if (obj == null) {
            return null;
        }
        RemoteConnectionTypeImpl rci = new RemoteConnectionTypeImpl();
        rci.setType(obj.type);
        rci.setRecorderTypeKey(obj.recorderTypeKey);
        rci.setName(obj.name);
        rci.setIntroHeadingStr(obj.introCopyHeading);
        rci.setIntroBodyStr(obj.introCopyBody);
        rci.setDisconnectStr(obj.disconnectCopy);
        rci.setDisconnectCancelStr(obj.disconnectCancel);
        rci.setDisconnectConfirmStr(obj.disconnectConfirm);
        rci.setLogoLink(obj.logoLink);
        rci.setLogoLinkLight(obj.logoLink);
        rci.setOAuthLink(obj.oauthConnectLink);
        for (String key : obj.getLinkKeys()) {
            rci.setLinksForRelation(key, obj.getLinks(key));
        }
        return rci;
    }
}
