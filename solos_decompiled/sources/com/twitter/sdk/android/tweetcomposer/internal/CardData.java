package com.twitter.sdk.android.tweetcomposer.internal;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes29.dex */
public class CardData {
    private static Serializer serializer;

    @SerializedName("twitter:app:country")
    public final String appCountry;

    @SerializedName("twitter:app:id:googleplay")
    public final String appGooglePlayId;

    @SerializedName("twitter:app:id:ipad")
    public final String appIPadId;

    @SerializedName("twitter:app:id:iphone")
    public final String appIPhoneId;

    @SerializedName("twitter:text:cta")
    public final String callToAction;

    @SerializedName("twitter:card")
    public final String card;

    @SerializedName("twitter:card_data")
    public final String cardData;

    @SerializedName("twitter:cta_key")
    public final String ctaKey;

    @SerializedName("twitter:description")
    public final String description;

    @SerializedName("twitter:text:did_value")
    public final String deviceId;

    @SerializedName("twitter:image")
    public final String image;

    @SerializedName("twitter:site")
    public final String site;

    private CardData(String card, String image, String site, String description, String cardData, String callToAction, String ctaKey, String deviceId, String appIPhoneId, String appIPadId, String appGooglePlayId, String appCountry) {
        this.card = card;
        this.image = image;
        this.site = site;
        this.description = description;
        this.cardData = cardData;
        this.callToAction = callToAction;
        this.ctaKey = ctaKey;
        this.deviceId = deviceId;
        this.appIPhoneId = appIPhoneId;
        this.appIPadId = appIPadId;
        this.appGooglePlayId = appGooglePlayId;
        this.appCountry = appCountry;
    }

    Serializer getSerializer() {
        if (serializer == null) {
            serializer = new Serializer();
        }
        return serializer;
    }

    public String toString() {
        return getSerializer().serialize(this);
    }

    static class Serializer {
        private final Gson gson = new Gson();

        Serializer() {
        }

        String serialize(CardData data) {
            return this.gson.toJson(data);
        }
    }

    public static class Builder {
        private String appCountry;
        private String appGooglePlayId;
        private String appIPadId;
        private String appIPhoneId;
        private String callToAction;
        private String card;
        private String cardData;
        private String ctaKey;
        private String description;
        private String deviceId;
        private String image;
        private String site;

        public Builder card(String card) {
            this.card = card;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder cardData(String data) {
            this.cardData = data;
            return this;
        }

        public Builder callToAction(String callToAction) {
            this.callToAction = callToAction;
            return this;
        }

        public Builder ctaKey(String ctaKey) {
            this.ctaKey = ctaKey;
            return this;
        }

        public Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder appIPhoneId(String appIPhoneId) {
            this.appIPhoneId = appIPhoneId;
            return this;
        }

        public Builder appIPadId(String appIPadId) {
            this.appIPadId = appIPadId;
            return this;
        }

        public Builder appGooglePlayId(String appGooglePlayId) {
            this.appGooglePlayId = appGooglePlayId;
            return this;
        }

        public Builder appCountry(String appCountry) {
            this.appCountry = appCountry;
            return this;
        }

        public CardData build() {
            return new CardData(this.card, this.image, this.site, this.description, this.cardData, this.callToAction, this.ctaKey, this.deviceId, this.appIPhoneId, this.appIPadId, this.appGooglePlayId, this.appCountry);
        }
    }
}
