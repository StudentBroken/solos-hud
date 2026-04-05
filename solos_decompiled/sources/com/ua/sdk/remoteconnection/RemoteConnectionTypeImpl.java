package com.ua.sdk.remoteconnection;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeImpl extends ApiTransferObject implements RemoteConnectionType, Parcelable {
    public static Parcelable.Creator<RemoteConnectionTypeImpl> CREATOR = new Parcelable.Creator<RemoteConnectionTypeImpl>() { // from class: com.ua.sdk.remoteconnection.RemoteConnectionTypeImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionTypeImpl createFromParcel(Parcel source) {
            return new RemoteConnectionTypeImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteConnectionTypeImpl[] newArray(int size) {
            return new RemoteConnectionTypeImpl[size];
        }
    };
    private String disconnectCancelStr;
    private String disconnectConfirmStr;
    private String disconnectStr;
    private String introBodyStr;
    private String introHeadingStr;
    private String logoLink;
    private String logoLinkLight;
    private String name;
    private String oAuthLink;
    private String recorderTypeKey;
    private String type;

    protected RemoteConnectionTypeImpl() {
    }

    protected RemoteConnectionTypeImpl(RemoteConnectionType remoteConnectionType) {
        Precondition.isNotNull(remoteConnectionType, "remoteConnectionType");
        this.type = remoteConnectionType.getType();
        this.recorderTypeKey = remoteConnectionType.getRecorderTypeKey();
        this.name = remoteConnectionType.getName();
        this.introHeadingStr = remoteConnectionType.getIntroHeadingStr();
        this.introBodyStr = remoteConnectionType.getIntroBodyStr();
        this.disconnectStr = remoteConnectionType.getDisconnectStr();
        this.disconnectCancelStr = remoteConnectionType.getDisconnectCancelStr();
        this.disconnectConfirmStr = remoteConnectionType.getDisconnectConfirmStr();
        this.logoLink = remoteConnectionType.getLogoLink();
        this.logoLinkLight = remoteConnectionType.getLogoLinkLight();
        this.oAuthLink = remoteConnectionType.getOAuthLink();
        if (remoteConnectionType instanceof RemoteConnectionTypeImpl) {
            copyLinkMap(((RemoteConnectionTypeImpl) remoteConnectionType).getLinkMap());
        }
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<RemoteConnectionType> getRef() {
        List<Link> selfLinks = getLinks("self");
        if (selfLinks == null || selfLinks.isEmpty()) {
            return null;
        }
        return new LinkEntityRef(selfLinks.get(0).getId(), selfLinks.get(0).getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getType() {
        return this.type;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getRecorderTypeKey() {
        return this.recorderTypeKey;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getIntroHeadingStr() {
        return this.introHeadingStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getIntroBodyStr() {
        return this.introBodyStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getDisconnectStr() {
        return this.disconnectStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getDisconnectCancelStr() {
        return this.disconnectCancelStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getDisconnectConfirmStr() {
        return this.disconnectConfirmStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getLogoLink() {
        return this.logoLink;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getLogoLinkLight() {
        return this.logoLinkLight;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public String getOAuthLink() {
        return this.oAuthLink;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setType(String type) {
        this.type = type;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setRecorderTypeKey(String recorderTypeKey) {
        this.recorderTypeKey = recorderTypeKey;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setName(String name) {
        this.name = name;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setIntroHeadingStr(String introHeadingStr) {
        this.introHeadingStr = introHeadingStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setIntroBodyStr(String introBodyStr) {
        this.introBodyStr = introBodyStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setDisconnectStr(String disconnectStr) {
        this.disconnectStr = disconnectStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setDisconnectCancelStr(String disconnectCancelStr) {
        this.disconnectCancelStr = disconnectCancelStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setDisconnectConfirmStr(String disconnectConfirmStr) {
        this.disconnectConfirmStr = disconnectConfirmStr;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setLogoLinkLight(String logoLinkLight) {
        this.logoLinkLight = logoLinkLight;
    }

    @Override // com.ua.sdk.remoteconnection.RemoteConnectionType
    public void setOAuthLink(String oAuthLink) {
        this.oAuthLink = oAuthLink;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.type);
        dest.writeString(this.recorderTypeKey);
        dest.writeString(this.name);
        dest.writeString(this.introHeadingStr);
        dest.writeString(this.introBodyStr);
        dest.writeString(this.disconnectStr);
        dest.writeString(this.disconnectCancelStr);
        dest.writeString(this.disconnectConfirmStr);
        dest.writeString(this.logoLink);
        dest.writeString(this.logoLinkLight);
        dest.writeString(this.oAuthLink);
    }

    private RemoteConnectionTypeImpl(Parcel in) {
        super(in);
        this.type = in.readString();
        this.recorderTypeKey = in.readString();
        this.name = in.readString();
        this.introHeadingStr = in.readString();
        this.introBodyStr = in.readString();
        this.disconnectStr = in.readString();
        this.disconnectCancelStr = in.readString();
        this.disconnectConfirmStr = in.readString();
        this.logoLink = in.readString();
        this.logoLinkLight = in.readString();
        this.oAuthLink = in.readString();
    }
}
