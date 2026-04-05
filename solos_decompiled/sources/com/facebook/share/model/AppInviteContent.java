package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes41.dex */
public final class AppInviteContent implements ShareModel {
    public static final Parcelable.Creator<AppInviteContent> CREATOR = new Parcelable.Creator<AppInviteContent>() { // from class: com.facebook.share.model.AppInviteContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppInviteContent createFromParcel(Parcel in) {
            return new AppInviteContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppInviteContent[] newArray(int size) {
            return new AppInviteContent[size];
        }
    };
    private final String applinkUrl;
    private final Builder.Destination destination;
    private final String previewImageUrl;
    private final String promoCode;
    private final String promoText;

    private AppInviteContent(Builder builder) {
        this.applinkUrl = builder.applinkUrl;
        this.previewImageUrl = builder.previewImageUrl;
        this.promoCode = builder.promoCode;
        this.promoText = builder.promoText;
        this.destination = builder.destination;
    }

    AppInviteContent(Parcel in) {
        this.applinkUrl = in.readString();
        this.previewImageUrl = in.readString();
        this.promoText = in.readString();
        this.promoCode = in.readString();
        String destinationString = in.readString();
        if (destinationString.length() > 0) {
            this.destination = Builder.Destination.valueOf(destinationString);
        } else {
            this.destination = Builder.Destination.FACEBOOK;
        }
    }

    public String getApplinkUrl() {
        return this.applinkUrl;
    }

    public String getPreviewImageUrl() {
        return this.previewImageUrl;
    }

    public String getPromotionCode() {
        return this.promoCode;
    }

    public String getPromotionText() {
        return this.promoText;
    }

    public Builder.Destination getDestination() {
        return this.destination != null ? this.destination : Builder.Destination.FACEBOOK;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.applinkUrl);
        out.writeString(this.previewImageUrl);
        out.writeString(this.promoText);
        out.writeString(this.promoCode);
        out.writeString(this.destination.toString());
    }

    public static class Builder implements ShareModelBuilder<AppInviteContent, Builder> {
        private String applinkUrl;
        private Destination destination;
        private String previewImageUrl;
        private String promoCode;
        private String promoText;

        public enum Destination {
            FACEBOOK("facebook"),
            MESSENGER("messenger");

            private final String name;

            Destination(String s) {
                this.name = s;
            }

            public boolean equalsName(String otherName) {
                if (otherName == null) {
                    return false;
                }
                return this.name.equals(otherName);
            }

            @Override // java.lang.Enum
            public String toString() {
                return this.name;
            }
        }

        public Builder setApplinkUrl(String applinkUrl) {
            this.applinkUrl = applinkUrl;
            return this;
        }

        public Builder setPreviewImageUrl(String previewImageUrl) {
            this.previewImageUrl = previewImageUrl;
            return this;
        }

        public Builder setPromotionDetails(String promotionText, String promotionCode) {
            if (!TextUtils.isEmpty(promotionText)) {
                if (promotionText.length() > 80) {
                    throw new IllegalArgumentException("Invalid promotion text, promotionText needs to be between1 and 80 characters long");
                }
                if (!isAlphanumericWithSpaces(promotionText)) {
                    throw new IllegalArgumentException("Invalid promotion text, promotionText can only contain alphanumericcharacters and spaces.");
                }
                if (!TextUtils.isEmpty(promotionCode)) {
                    if (promotionCode.length() > 10) {
                        throw new IllegalArgumentException("Invalid promotion code, promotionCode can be between1 and 10 characters long");
                    }
                    if (!isAlphanumericWithSpaces(promotionCode)) {
                        throw new IllegalArgumentException("Invalid promotion code, promotionCode can only contain alphanumeric characters and spaces.");
                    }
                }
            } else if (!TextUtils.isEmpty(promotionCode)) {
                throw new IllegalArgumentException("promotionCode cannot be specified without a valid promotionText");
            }
            this.promoCode = promotionCode;
            this.promoText = promotionText;
            return this;
        }

        public Builder setDestination(Destination destination) {
            this.destination = destination;
            return this;
        }

        @Override // com.facebook.share.ShareBuilder
        public AppInviteContent build() {
            return new AppInviteContent(this);
        }

        @Override // com.facebook.share.model.ShareModelBuilder
        public Builder readFrom(AppInviteContent content) {
            return content == null ? this : setApplinkUrl(content.getApplinkUrl()).setPreviewImageUrl(content.getPreviewImageUrl()).setPromotionDetails(content.getPromotionText(), content.getPromotionCode()).setDestination(content.getDestination());
        }

        private boolean isAlphanumericWithSpaces(String str) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (!Character.isDigit(c) && !Character.isLetter(c) && !Character.isSpaceChar(c)) {
                    return false;
                }
            }
            return true;
        }
    }
}
