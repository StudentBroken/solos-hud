package android.support.v7.widget;

import android.content.Context;
import android.view.View;

/* JADX INFO: loaded from: classes19.dex */
class CardViewApi21 implements CardViewImpl {
    CardViewApi21() {
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void initialize(CardViewDelegate cardView, Context context, int backgroundColor, float radius, float elevation, float maxElevation) {
        RoundRectDrawable background = new RoundRectDrawable(backgroundColor, radius);
        cardView.setCardBackground(background);
        View view = cardView.getCardView();
        view.setClipToOutline(true);
        view.setElevation(elevation);
        setMaxElevation(cardView, maxElevation);
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void setRadius(CardViewDelegate cardView, float radius) {
        getCardBackground(cardView).setRadius(radius);
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void initStatic() {
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        getCardBackground(cardView).setPadding(maxElevation, cardView.getUseCompatPadding(), cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    @Override // android.support.v7.widget.CardViewImpl
    public float getMaxElevation(CardViewDelegate cardView) {
        return getCardBackground(cardView).getPadding();
    }

    @Override // android.support.v7.widget.CardViewImpl
    public float getMinWidth(CardViewDelegate cardView) {
        return getRadius(cardView) * 2.0f;
    }

    @Override // android.support.v7.widget.CardViewImpl
    public float getMinHeight(CardViewDelegate cardView) {
        return getRadius(cardView) * 2.0f;
    }

    @Override // android.support.v7.widget.CardViewImpl
    public float getRadius(CardViewDelegate cardView) {
        return getCardBackground(cardView).getRadius();
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void setElevation(CardViewDelegate cardView, float elevation) {
        cardView.getCardView().setElevation(elevation);
    }

    @Override // android.support.v7.widget.CardViewImpl
    public float getElevation(CardViewDelegate cardView) {
        return cardView.getCardView().getElevation();
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void updatePadding(CardViewDelegate cardView) {
        if (!cardView.getUseCompatPadding()) {
            cardView.setShadowPadding(0, 0, 0, 0);
            return;
        }
        float elevation = getMaxElevation(cardView);
        float radius = getRadius(cardView);
        int hPadding = (int) Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
        int vPadding = (int) Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
        cardView.setShadowPadding(hPadding, vPadding, hPadding, vPadding);
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void onCompatPaddingChanged(CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    @Override // android.support.v7.widget.CardViewImpl
    public void setBackgroundColor(CardViewDelegate cardView, int color) {
        getCardBackground(cardView).setColor(color);
    }

    private RoundRectDrawable getCardBackground(CardViewDelegate cardView) {
        return (RoundRectDrawable) cardView.getCardBackground();
    }
}
