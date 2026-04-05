package com.twitter.sdk.android.tweetui;

import android.app.Activity;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.tweetui.internal.MultiTouchImageView;

/* JADX INFO: loaded from: classes9.dex */
public class GalleryActivity extends Activity {
    static final String MEDIA_ENTITY = "MEDIA_ENTITY";
    static final String TWEET_ID = "TWEET_ID";

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tw__gallery_activity);
        MediaEntity entity = (MediaEntity) getIntent().getSerializableExtra(MEDIA_ENTITY);
        MultiTouchImageView imageView = (MultiTouchImageView) findViewById(R.id.image_view);
        Picasso.with(this).load(entity.mediaUrlHttps).into(imageView);
    }
}
