package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public final class GameRequestContent implements ShareModel {
    public static final Parcelable.Creator<GameRequestContent> CREATOR = new Parcelable.Creator<GameRequestContent>() { // from class: com.facebook.share.model.GameRequestContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GameRequestContent createFromParcel(Parcel in) {
            return new GameRequestContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GameRequestContent[] newArray(int size) {
            return new GameRequestContent[size];
        }
    };
    private final ActionType actionType;
    private final String data;
    private final Filters filters;
    private final String message;
    private final String objectId;
    private final List<String> recipients;
    private final List<String> suggestions;
    private final String title;

    public enum ActionType {
        SEND,
        ASKFOR,
        TURN
    }

    public enum Filters {
        APP_USERS,
        APP_NON_USERS
    }

    private GameRequestContent(Builder builder) {
        this.message = builder.message;
        this.recipients = builder.recipients;
        this.title = builder.title;
        this.data = builder.data;
        this.actionType = builder.actionType;
        this.objectId = builder.objectId;
        this.filters = builder.filters;
        this.suggestions = builder.suggestions;
    }

    GameRequestContent(Parcel in) {
        this.message = in.readString();
        this.recipients = in.createStringArrayList();
        this.title = in.readString();
        this.data = in.readString();
        this.actionType = (ActionType) in.readSerializable();
        this.objectId = in.readString();
        this.filters = (Filters) in.readSerializable();
        this.suggestions = in.createStringArrayList();
        in.readStringList(this.suggestions);
    }

    public String getMessage() {
        return this.message;
    }

    public String getTo() {
        if (getRecipients() != null) {
            return TextUtils.join(",", getRecipients());
        }
        return null;
    }

    public List<String> getRecipients() {
        return this.recipients;
    }

    public String getTitle() {
        return this.title;
    }

    public String getData() {
        return this.data;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public Filters getFilters() {
        return this.filters;
    }

    public List<String> getSuggestions() {
        return this.suggestions;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.message);
        out.writeStringList(this.recipients);
        out.writeString(this.title);
        out.writeString(this.data);
        out.writeSerializable(this.actionType);
        out.writeString(this.objectId);
        out.writeSerializable(this.filters);
        out.writeStringList(this.suggestions);
    }

    public static class Builder implements ShareModelBuilder<GameRequestContent, Builder> {
        private ActionType actionType;
        private String data;
        private Filters filters;
        private String message;
        private String objectId;
        private List<String> recipients;
        private List<String> suggestions;
        private String title;

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTo(String to) {
            if (to != null) {
                String[] recipientsArray = to.split(",");
                this.recipients = Arrays.asList(recipientsArray);
            }
            return this;
        }

        public Builder setRecipients(List<String> recipients) {
            this.recipients = recipients;
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setActionType(ActionType actionType) {
            this.actionType = actionType;
            return this;
        }

        public Builder setObjectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        public Builder setFilters(Filters filters) {
            this.filters = filters;
            return this;
        }

        public Builder setSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public GameRequestContent build() {
            return new GameRequestContent(this);
        }

        @Override // com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(GameRequestContent content) {
            return content == null ? this : setMessage(content.getMessage()).setRecipients(content.getRecipients()).setTitle(content.getTitle()).setData(content.getData()).setActionType(content.getActionType()).setObjectId(content.getObjectId()).setFilters(content.getFilters()).setSuggestions(content.getSuggestions());
        }

        Builder readFrom(Parcel parcel) {
            return readFrom((GameRequestContent) parcel.readParcelable(GameRequestContent.class.getClassLoader()));
        }
    }
}
