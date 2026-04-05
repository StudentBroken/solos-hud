package com.ua.sdk.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class UserCommunicationImpl implements UserCommunication, Parcelable {
    public static Parcelable.Creator<UserCommunicationImpl> CREATOR = new Parcelable.Creator<UserCommunicationImpl>() { // from class: com.ua.sdk.user.UserCommunicationImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserCommunicationImpl createFromParcel(Parcel source) {
            return new UserCommunicationImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserCommunicationImpl[] newArray(int size) {
            return new UserCommunicationImpl[size];
        }
    };

    @SerializedName("newsletter")
    Boolean newsletter;

    @SerializedName("promotions")
    Boolean promotions;

    @SerializedName("system_messages")
    Boolean systemMessages;

    public UserCommunicationImpl() {
    }

    @Override // com.ua.sdk.user.UserCommunication
    public boolean isPromotions() {
        return this.promotions != null && this.promotions.booleanValue();
    }

    @Override // com.ua.sdk.user.UserCommunication
    public Boolean getPromotions() {
        return this.promotions;
    }

    @Override // com.ua.sdk.user.UserCommunication
    public void setPromotions(Boolean promotions) {
        this.promotions = promotions;
    }

    @Override // com.ua.sdk.user.UserCommunication
    public boolean isNewsletter() {
        return this.newsletter != null && this.newsletter.booleanValue();
    }

    @Override // com.ua.sdk.user.UserCommunication
    public Boolean getNewsletter() {
        return this.newsletter;
    }

    @Override // com.ua.sdk.user.UserCommunication
    public void setNewsletter(Boolean newsletters) {
        this.newsletter = newsletters;
    }

    @Override // com.ua.sdk.user.UserCommunication
    public boolean isSystemMessages() {
        return this.systemMessages != null && this.systemMessages.booleanValue();
    }

    @Override // com.ua.sdk.user.UserCommunication
    public Boolean getSystemMessages() {
        return this.systemMessages;
    }

    @Override // com.ua.sdk.user.UserCommunication
    public void setSystemMessages(Boolean systemMessages) {
        this.systemMessages = systemMessages;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean newletters;
        private Boolean promotions;
        private Boolean systemMessages;

        public Builder setPromotions(Boolean promotions) {
            this.promotions = promotions;
            return this;
        }

        public Builder setNewletters(Boolean newletters) {
            this.newletters = newletters;
            return this;
        }

        public Builder setSystemMessages(Boolean systemMessages) {
            this.systemMessages = systemMessages;
            return this;
        }

        public UserCommunicationImpl build() {
            UserCommunicationImpl answer = new UserCommunicationImpl();
            answer.setPromotions(this.promotions);
            answer.setNewsletter(this.newletters);
            answer.setSystemMessages(this.systemMessages);
            return answer;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.promotions);
        dest.writeValue(this.newsletter);
        dest.writeValue(this.systemMessages);
    }

    private UserCommunicationImpl(Parcel in) {
        this.promotions = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.newsletter = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.systemMessages = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }
}
