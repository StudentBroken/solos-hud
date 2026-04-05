package com.twitter.sdk.android.tweetcomposer;

import android.content.Context;
import android.net.Uri;
import java.io.Serializable;

/* JADX INFO: loaded from: classes29.dex */
public class Card implements Serializable {
    public static final String APP_CARD_TYPE = "promo_image_app";
    final String appGooglePlayId;
    final String appIPadId;
    final String appIPhoneId;
    final String appName;
    final String cardType;
    final String imageUri;

    Card(String cardType, String imageUri, String appName, String appIPhoneId, String appIPadId, String appGooglePlayId) {
        this.cardType = cardType;
        this.imageUri = imageUri;
        this.appName = appName;
        this.appIPadId = appIPadId;
        this.appIPhoneId = appIPhoneId;
        this.appGooglePlayId = appGooglePlayId;
    }

    public String getCardType() {
        return this.cardType;
    }

    static boolean isAppCard(Card card) {
        return (card == null || card.getCardType() == null || !card.getCardType().equals(APP_CARD_TYPE)) ? false : true;
    }

    public static class AppCardBuilder {
        private String appGooglePlayId;
        private String appIPadId;
        private String appIPhoneId;
        private String appName;
        private Uri imageUri;

        public AppCardBuilder(Context context) {
            this.appName = Card.getApplicationName(context);
            this.appGooglePlayId = Card.getPackageName(context);
        }

        public AppCardBuilder imageUri(Uri imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public AppCardBuilder iPhoneId(String appIPhoneId) {
            this.appIPhoneId = appIPhoneId;
            return this;
        }

        public AppCardBuilder iPadId(String appIPadId) {
            this.appIPadId = appIPadId;
            return this;
        }

        public AppCardBuilder googlePlayId(String appGooglePlayId) {
            this.appGooglePlayId = appGooglePlayId;
            return this;
        }

        public Card build() {
            if (this.imageUri == null) {
                throw new IllegalStateException("App Card requires a non-null imageUri");
            }
            return new Card(Card.APP_CARD_TYPE, this.imageUri.toString(), this.appName, this.appIPhoneId, this.appIPadId, this.appGooglePlayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }
}
