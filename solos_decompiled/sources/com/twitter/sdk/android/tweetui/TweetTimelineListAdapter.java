package com.twitter.sdk.android.tweetui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.internal.TimelineDelegate;

/* JADX INFO: loaded from: classes9.dex */
public class TweetTimelineListAdapter extends TimelineListAdapter<Tweet> {
    protected Callback<Tweet> actionCallback;
    protected final int styleResId;

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter, android.widget.Adapter
    public /* bridge */ /* synthetic */ int getCount() {
        return super.getCount();
    }

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter, android.widget.Adapter
    public /* bridge */ /* synthetic */ long getItemId(int x0) {
        return super.getItemId(x0);
    }

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter, android.widget.BaseAdapter
    public /* bridge */ /* synthetic */ void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter, android.widget.BaseAdapter
    public /* bridge */ /* synthetic */ void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter
    public /* bridge */ /* synthetic */ void refresh(Callback x0) {
        super.refresh(x0);
    }

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter, android.widget.BaseAdapter, android.widget.Adapter
    public /* bridge */ /* synthetic */ void registerDataSetObserver(DataSetObserver x0) {
        super.registerDataSetObserver(x0);
    }

    @Override // com.twitter.sdk.android.tweetui.TimelineListAdapter, android.widget.BaseAdapter, android.widget.Adapter
    public /* bridge */ /* synthetic */ void unregisterDataSetObserver(DataSetObserver x0) {
        super.unregisterDataSetObserver(x0);
    }

    public TweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
        this(context, timeline, R.style.tw__TweetLightStyle, (Callback<Tweet>) null);
    }

    TweetTimelineListAdapter(Context context, Timeline<Tweet> timeline, int styleResId, Callback<Tweet> cb) {
        this(context, (TimelineDelegate<Tweet>) new TimelineDelegate(timeline), styleResId, cb);
    }

    TweetTimelineListAdapter(Context context, TimelineDelegate<Tweet> delegate, int styleResId, Callback<Tweet> cb) {
        super(context, delegate);
        this.styleResId = styleResId;
        this.actionCallback = new ReplaceTweetCallback(delegate, cb);
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            BaseTweetView tv = new CompactTweetView(this.context, tweet, this.styleResId);
            tv.setOnActionCallback(this.actionCallback);
            return tv;
        }
        ((BaseTweetView) convertView).setTweet(tweet);
        return convertView;
    }

    static class ReplaceTweetCallback extends Callback<Tweet> {
        Callback<Tweet> cb;
        TimelineDelegate<Tweet> delegate;

        ReplaceTweetCallback(TimelineDelegate<Tweet> delegate, Callback<Tweet> cb) {
            this.delegate = delegate;
            this.cb = cb;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<Tweet> result) {
            this.delegate.setItemById(result.data);
            if (this.cb != null) {
                this.cb.success(result);
            }
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            if (this.cb != null) {
                this.cb.failure(exception);
            }
        }
    }

    public static class Builder {
        private Callback<Tweet> actionCallback;
        private Context context;
        private int styleResId = R.style.tw__TweetLightStyle;
        private Timeline<Tweet> timeline;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTimeline(Timeline<Tweet> timeline) {
            this.timeline = timeline;
            return this;
        }

        public Builder setViewStyle(int styleResId) {
            this.styleResId = styleResId;
            return this;
        }

        public Builder setOnActionCallback(Callback<Tweet> actionCallback) {
            this.actionCallback = actionCallback;
            return this;
        }

        public TweetTimelineListAdapter build() {
            return new TweetTimelineListAdapter(this.context, this.timeline, this.styleResId, this.actionCallback);
        }
    }
}
