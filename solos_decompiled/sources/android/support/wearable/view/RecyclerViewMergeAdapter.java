package android.support.wearable.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes33.dex */
public final class RecyclerViewMergeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final long CHILD_ID_RANGE = 10000000000000000L;
    private static final int MAX_ADAPTER_ID = 922;
    private static final String TAG = "MergeAdapter";
    private final List<AdapterHolder> mAdapters = new ArrayList();
    private int mItemCount;
    private int mNextAdapterId;
    private int mNextViewTypeId;

    @Nullable
    private RecyclerView mRecyclerView;

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView = null;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void setHasStableIds(boolean hasStableIds) {
        if (hasStableIds) {
            int len = this.mAdapters.size();
            for (int i = 0; i < len; i++) {
                if (!this.mAdapters.get(i).adapter.hasStableIds()) {
                    throw new IllegalStateException("All child adapters must have stable IDs when hasStableIds=true");
                }
            }
        }
        super.setHasStableIds(hasStableIds);
    }

    public void addAdapter(RecyclerView.Adapter adapter) {
        addAdapter(this.mAdapters.size(), adapter);
    }

    public void addAdapter(int adapterPosition, RecyclerView.Adapter<?> adapter) {
        if (this.mNextAdapterId == MAX_ADAPTER_ID) {
            throw new IllegalStateException("addAdapter cannot be called more than 922 times");
        }
        if (hasStableIds() && !adapter.hasStableIds()) {
            throw new IllegalStateException("All child adapters must have stable IDs when hasStableIds=true");
        }
        AdapterHolder adapterHolder = new AdapterHolder(this.mNextAdapterId, adapter);
        this.mNextAdapterId++;
        adapterHolder.observer = new ForwardingDataSetObserver(this, adapterHolder);
        adapterHolder.adapterPosition = adapterPosition;
        this.mAdapters.add(adapterPosition, adapterHolder);
        updateItemPositionOffsets(adapterPosition);
        adapter.registerAdapterDataObserver(adapterHolder.observer);
        notifyItemRangeInserted(adapterHolder.itemPositionOffset, adapterHolder.adapter.getItemCount());
    }

    public int getAdapterPosition(@Nullable RecyclerView.Adapter<?> adapter) {
        if (adapter != null) {
            int len = this.mAdapters.size();
            for (int i = 0; i < len; i++) {
                if (this.mAdapters.get(i).adapter == adapter) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void removeAdapter(RecyclerView.Adapter<?> adapter) {
        int pos = getAdapterPosition(adapter);
        if (pos >= 0) {
            removeAdapter(pos);
        }
    }

    public void moveAdapter(int newPosition, RecyclerView.Adapter<?> adapter) {
        if (newPosition < 0) {
            throw new IllegalArgumentException("newPosition cannot be < 0");
        }
        if (getAdapterPosition(adapter) < 0) {
            throw new IllegalStateException("adapter must already be added");
        }
        int previousPosition = getAdapterPosition(adapter);
        if (previousPosition != newPosition) {
            AdapterHolder holder = this.mAdapters.remove(previousPosition);
            notifyItemRangeRemoved(holder.itemPositionOffset, holder.adapter.getItemCount());
            this.mAdapters.add(newPosition, holder);
            if (previousPosition < newPosition) {
                updateItemPositionOffsets(previousPosition);
            } else {
                updateItemPositionOffsets(newPosition);
            }
            notifyItemRangeInserted(holder.itemPositionOffset, holder.adapter.getItemCount());
        }
    }

    public void removeAdapter(int adapterPosition) {
        if (adapterPosition < 0 || adapterPosition >= this.mAdapters.size()) {
            Log.w(TAG, new StringBuilder(50).append("removeAdapter(").append(adapterPosition).append("): position out of range!").toString());
            return;
        }
        AdapterHolder holder = this.mAdapters.remove(adapterPosition);
        updateItemPositionOffsets(adapterPosition);
        holder.adapter.unregisterAdapterDataObserver(holder.observer);
        if (this.mRecyclerView != null && holder.viewTypes != null) {
            int size = holder.viewTypes.size();
            for (int i = 0; i < size; i++) {
                int viewType = holder.viewTypes.keyAt(i);
                RecyclerView.RecycledViewPool pool = this.mRecyclerView.getRecycledViewPool();
                pool.setMaxRecycledViews(viewType, 0);
            }
        }
        notifyItemRangeRemoved(holder.itemPositionOffset, holder.adapter.getItemCount());
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mItemCount;
    }

    @NonNull
    public RecyclerView.Adapter<?> getAdapterForPosition(int position) {
        return getAdapterHolderForPosition(position).adapter;
    }

    public long getChildItemId(int position) {
        AdapterHolder adapterHolder = getAdapterHolderForPosition(position);
        int adapterItemOffset = adapterHolder.itemPositionOffset;
        int childPosition = position - adapterItemOffset;
        return adapterHolder.adapter.getItemId(childPosition);
    }

    public int getParentPosition(RecyclerView.Adapter childAdapter, int childPosition) {
        for (int i = 0; i < this.mAdapters.size(); i++) {
            if (this.mAdapters.get(i).adapter == childAdapter) {
                return this.mAdapters.get(i).itemPositionOffset + childPosition;
            }
        }
        return -1;
    }

    public int getChildPosition(int position) {
        AdapterHolder adapterHolder = getAdapterHolderForPosition(position);
        int adapterItemOffset = adapterHolder.itemPositionOffset;
        return position - adapterItemOffset;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        AdapterHolder adapterHolder = getAdapterHolderForPosition(position);
        int adapterItemOffset = adapterHolder.itemPositionOffset;
        int localViewType = adapterHolder.adapter.getItemViewType(position - adapterItemOffset);
        if (adapterHolder.viewTypes == null) {
            adapterHolder.viewTypes = new SparseIntArray(1);
        } else {
            int keyIndex = adapterHolder.viewTypes.indexOfValue(localViewType);
            if (keyIndex != -1) {
                return adapterHolder.viewTypes.keyAt(keyIndex);
            }
        }
        int viewType = this.mNextViewTypeId;
        this.mNextViewTypeId++;
        adapterHolder.viewTypes.put(viewType, localViewType);
        return viewType;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int localViewType;
        int len = this.mAdapters.size();
        for (int i = 0; i < len; i++) {
            AdapterHolder adapterHolder = this.mAdapters.get(i);
            if (adapterHolder.viewTypes != null && (localViewType = adapterHolder.viewTypes.get(viewType, -1)) != -1) {
                return adapterHolder.adapter.onCreateViewHolder(viewGroup, localViewType);
            }
        }
        Log.w(TAG, new StringBuilder(66).append("onCreateViewHolder: No child adapters handle viewType: ").append(viewType).toString());
        return null;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AdapterHolder adapterHolder = getAdapterHolderForPosition(position);
        int start = adapterHolder.itemPositionOffset;
        int localPosition = position - start;
        adapterHolder.adapter.onBindViewHolder(viewHolder, localPosition);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        AdapterHolder adapterHolder;
        int viewType = holder.getItemViewType();
        if (viewType != -1 && (adapterHolder = findAdapterHolderForViewType(viewType)) != null) {
            adapterHolder.adapter.onViewRecycled(holder);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        AdapterHolder adapterHolder;
        int viewType = holder.getItemViewType();
        if (viewType == -1 || (adapterHolder = findAdapterHolderForViewType(viewType)) == null) {
            return true;
        }
        return adapterHolder.adapter.onFailedToRecycleView(holder);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        AdapterHolder adapterHolder;
        int viewType = holder.getItemViewType();
        if (viewType != -1 && (adapterHolder = findAdapterHolderForViewType(viewType)) != null) {
            adapterHolder.adapter.onViewAttachedToWindow(holder);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        AdapterHolder adapterHolder;
        int viewType = holder.getItemViewType();
        if (viewType != -1 && (adapterHolder = findAdapterHolderForViewType(viewType)) != null) {
            adapterHolder.adapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public long getItemId(int position) {
        if (!hasStableIds()) {
            return -1L;
        }
        int adapterIndex = getAdapterIndexForPosition(position);
        int adapterId = this.mAdapters.get(adapterIndex).adapterId;
        return createItemId(adapterId, getChildItemId(position));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItemPositionOffsets(int startingAdapterIndex) {
        int nextOffset = 0;
        if (startingAdapterIndex > 0) {
            AdapterHolder holder = this.mAdapters.get(startingAdapterIndex - 1);
            int nextOffset2 = holder.itemPositionOffset;
            nextOffset = nextOffset2 + holder.adapter.getItemCount();
        }
        int len = this.mAdapters.size();
        for (int i = startingAdapterIndex; i < len; i++) {
            AdapterHolder adapterHolder = this.mAdapters.get(i);
            adapterHolder.itemPositionOffset = nextOffset;
            adapterHolder.adapterPosition = i;
            nextOffset += adapterHolder.adapter.getItemCount();
        }
        this.mItemCount = nextOffset;
    }

    @Nullable
    private AdapterHolder findAdapterHolderForViewType(int viewType) {
        int adapterIndex = getAdapterIndexForViewType(viewType);
        if (adapterIndex == -1) {
            return null;
        }
        return this.mAdapters.get(adapterIndex);
    }

    @NonNull
    private AdapterHolder getAdapterHolderForPosition(int position) {
        int adapterIndex = getAdapterIndexForPosition(position);
        return this.mAdapters.get(adapterIndex);
    }

    private int getAdapterIndexForPosition(int position) {
        int len = this.mAdapters.size();
        for (int i = 0; i < len; i++) {
            AdapterHolder adapterHolder = this.mAdapters.get(i);
            int start = adapterHolder.itemPositionOffset;
            int count = adapterHolder.adapter.getItemCount();
            if (position >= start && position < start + count) {
                return i;
            }
        }
        throw new IllegalStateException(new StringBuilder(46).append("No adapter appears to own position ").append(position).toString());
    }

    private int getAdapterIndexForViewType(int viewType) {
        int len = this.mAdapters.size();
        for (int i = 0; i < len; i++) {
            AdapterHolder adapterHolder = this.mAdapters.get(i);
            if (adapterHolder.viewTypes != null) {
                int index = adapterHolder.viewTypes.indexOfKey(viewType);
                if (index >= 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static long createItemId(int adapterId, long childItemId) {
        return (CHILD_ID_RANGE * ((long) adapterId)) + childItemId;
    }

    private static final class AdapterHolder {
        final RecyclerView.Adapter adapter;
        final int adapterId;
        int adapterPosition;
        int itemPositionOffset;
        ForwardingDataSetObserver observer;
        SparseIntArray viewTypes;

        public AdapterHolder(int id, RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            this.adapterId = id;
        }
    }

    private static final class ForwardingDataSetObserver extends RecyclerView.AdapterDataObserver {
        private final AdapterHolder mAdapterHolder;
        private final RecyclerViewMergeAdapter mMergedAdapter;

        public ForwardingDataSetObserver(RecyclerViewMergeAdapter parent, AdapterHolder holder) {
            this.mAdapterHolder = holder;
            this.mMergedAdapter = parent;
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            this.mMergedAdapter.updateItemPositionOffsets(0);
            this.mMergedAdapter.notifyDataSetChanged();
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int positionStart, int itemCount) {
            this.mMergedAdapter.notifyItemRangeChanged(this.mAdapterHolder.itemPositionOffset + positionStart, itemCount);
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int positionStart, int itemCount) {
            this.mMergedAdapter.updateItemPositionOffsets(this.mAdapterHolder.adapterPosition);
            this.mMergedAdapter.notifyItemRangeInserted(this.mAdapterHolder.itemPositionOffset + positionStart, itemCount);
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            this.mMergedAdapter.updateItemPositionOffsets(this.mAdapterHolder.adapterPosition);
            this.mMergedAdapter.notifyItemRangeRemoved(this.mAdapterHolder.itemPositionOffset + positionStart, itemCount);
        }
    }
}
