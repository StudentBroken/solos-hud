package com.twitter.sdk.android.tweetui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
@Deprecated
public class TweetViewAdapter<T extends BaseTweetView> extends BaseAdapter {
    protected final Context context;
    protected List<Tweet> tweets;

    public TweetViewAdapter(Context context) {
        this.context = context;
        this.tweets = new ArrayList();
    }

    public TweetViewAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    public T getTweetView(Context context, Tweet tweet) {
        return new CompactTweetView(context, tweet);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.tweets == null) {
            return 0;
        }
        return this.tweets.size();
    }

    @Override // android.widget.Adapter
    public Tweet getItem(int position) {
        return this.tweets.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    public void setTweetById(Tweet tweet) {
        for (int i = 0; i < this.tweets.size(); i++) {
            if (tweet.getId() == this.tweets.get(i).getId()) {
                this.tweets.set(i, tweet);
            }
        }
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            View rowView = getTweetView(this.context, tweet);
            return rowView;
        }
        ((BaseTweetView) convertView).setTweet(tweet);
        return convertView;
    }

    public List<Tweet> getTweets() {
        return this.tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        if (tweets == null) {
            this.tweets = new ArrayList();
        } else {
            this.tweets = tweets;
        }
        notifyDataSetChanged();
    }
}
