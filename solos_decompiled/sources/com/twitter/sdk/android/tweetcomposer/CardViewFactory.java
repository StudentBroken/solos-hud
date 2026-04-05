package com.twitter.sdk.android.tweetcomposer;

import android.content.Context;
import android.view.View;

/* JADX INFO: loaded from: classes29.dex */
class CardViewFactory {
    CardViewFactory() {
    }

    View createCard(Context context, Card card) {
        if (!card.cardType.equals(Card.APP_CARD_TYPE)) {
            return null;
        }
        AppCardView cardView = new AppCardView(context);
        cardView.setCard(card);
        return cardView;
    }
}
