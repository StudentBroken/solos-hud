package com.twitter.sdk.android.tweetcomposer;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twitter.sdk.android.tweetcomposer.RoundedCornerTransformation;

/* JADX INFO: loaded from: classes29.dex */
public class AppCardView extends LinearLayout {
    ImageView appImageView;
    ViewGroup appInfoLayout;
    TextView appInstallButton;
    TextView appNameView;
    TextView appStoreNameView;

    public AppCardView(Context context) {
        this(context, null);
    }

    public AppCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(11)
    public AppCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context) {
        setOrientation(1);
        inflate(context, R.layout.tw__app_card, this);
        findSubviews();
        setButtonColor();
    }

    void findSubviews() {
        this.appImageView = (ImageView) findViewById(R.id.tw__app_image);
        this.appNameView = (TextView) findViewById(R.id.tw__app_name);
        this.appStoreNameView = (TextView) findViewById(R.id.tw__app_store_name);
        this.appInstallButton = (TextView) findViewById(R.id.tw__app_install_button);
        this.appInfoLayout = (ViewGroup) findViewById(R.id.tw__app_info_layout);
    }

    void setCard(Card card) {
        setImage(Uri.parse(card.imageUri));
        setAppName(card.appName);
    }

    void setImage(Uri uri) {
        int radius = getResources().getDimensionPixelSize(R.dimen.tw__card_radius_medium);
        Transformation transformation = new RoundedCornerTransformation.Builder().setRadii(radius, radius, 0, 0).build();
        Picasso.with(getContext()).load(uri).transform(transformation).fit().centerCrop().into(this.appImageView);
    }

    void setAppName(String name) {
        this.appNameView.setText(name);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = getResources().getDimensionPixelSize(R.dimen.tw__card_maximum_width);
        int measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        if (maxWidth > 0 && maxWidth < measuredWidth) {
            int measureMode = View.MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    void setButtonColor() {
        int buttonTextColor = getResources().getColor(R.color.tw__composer_blue_text);
        this.appInstallButton.setTextColor(buttonTextColor);
    }
}
