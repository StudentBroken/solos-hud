package com.twitter.sdk.android.tweetcomposer;

import com.twitter.sdk.android.tweetcomposer.internal.CardData;

/* JADX INFO: loaded from: classes29.dex */
class CardDataFactory {
    static final String APP_CARD_CTA_KEY = "open";
    static final String APP_CARD_TYPE = "promo_image_app";
    private static final String MEDIA_SCHEME = "media://";

    CardDataFactory() {
    }

    static CardData createAppCardData(Card card, Long mediaId, String advertisingId) {
        return new CardData.Builder().card("promo_image_app").image(getCardMedia(mediaId)).appIPhoneId(card.appIPhoneId).appIPadId(card.appIPadId).appGooglePlayId(card.appGooglePlayId).cardData("{}").ctaKey(APP_CARD_CTA_KEY).deviceId(advertisingId).build();
    }

    static String getCardMedia(Long mediaId) {
        return MEDIA_SCHEME + Long.toString(mediaId.longValue());
    }
}
