package com.twitter.sdk.android.tweetui;

import android.content.Context;
import android.database.DataSetObserver;
import android.widget.BaseAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Identifiable;
import com.twitter.sdk.android.tweetui.internal.TimelineDelegate;

/* JADX INFO: loaded from: classes9.dex */
abstract class TimelineListAdapter<T extends Identifiable> extends BaseAdapter {
    protected final Context context;
    protected final TimelineDelegate<T> delegate;

    public TimelineListAdapter(Context context, Timeline<T> timeline) {
        this(context, new TimelineDelegate(timeline));
    }

    TimelineListAdapter(Context context, TimelineDelegate<T> delegate) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        this.context = context;
        this.delegate = delegate;
        delegate.refresh(null);
    }

    public void refresh(Callback<TimelineResult<T>> cb) {
        this.delegate.refresh(cb);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.delegate.getCount();
    }

    @Override // android.widget.Adapter
    public T getItem(int i) {
        return (T) this.delegate.getItem(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return this.delegate.getItemId(position);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public void registerDataSetObserver(DataSetObserver observer) {
        this.delegate.registerDataSetObserver(observer);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.delegate.unregisterDataSetObserver(observer);
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        this.delegate.notifyDataSetChanged();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetInvalidated() {
        this.delegate.notifyDataSetInvalidated();
    }
}
