package com.opentok.android;

import android.content.Context;
import com.opentok.android.SubscriberKit;

/* JADX INFO: loaded from: classes15.dex */
public class Subscriber extends SubscriberKit {

    public static class Builder extends SubscriberKit.Builder {
        public Builder(Context context, Stream stream) {
            super(context, stream);
        }

        @Override // com.opentok.android.SubscriberKit.Builder
        public Builder renderer(BaseVideoRenderer renderer) {
            this.renderer = renderer;
            return this;
        }

        @Override // com.opentok.android.SubscriberKit.Builder
        public Subscriber build() {
            return new Subscriber(this.context, this.stream, this.renderer);
        }
    }

    protected Subscriber(Context context, Stream stream, BaseVideoRenderer renderer) {
        super(context, stream, renderer);
        if (renderer == null) {
            this.renderer = VideoRenderFactory.constructRenderer(context);
        }
    }

    @Deprecated
    public Subscriber(Context context, Stream stream) {
        this(context, stream, null);
    }
}
