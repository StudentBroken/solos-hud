package com.ua.sdk.internal;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;
import com.ua.sdk.cache.EntityDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public abstract class AbstractEntityList<T extends Resource> extends ApiTransferObject implements EntityList<T> {
    private transient ArrayList<T> mElements;

    @SerializedName("_embedded")
    Map<String, ArrayList<T>> mEmbedded;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    Integer mTotalCount;

    protected abstract String getListKey();

    public AbstractEntityList() {
    }

    @Override // com.ua.sdk.EntityList
    public int getSize() {
        return getElements().size();
    }

    @Override // com.ua.sdk.EntityList
    public T get(int index) {
        Precondition.isValidIndex(getElements(), index);
        return getElements().get(index);
    }

    public ArrayList<T> getElements() {
        if (this.mElements == null) {
            if (this.mEmbedded == null) {
                this.mEmbedded = new HashMap(1);
            }
            this.mElements = this.mEmbedded.get(getListKey());
            if (this.mElements == null) {
                this.mElements = new ArrayList<>(0);
                this.mEmbedded.put(getListKey(), this.mElements);
            }
        }
        return this.mElements;
    }

    @Override // com.ua.sdk.EntityList
    public List<T> getAll() {
        return Collections.unmodifiableList(getElements());
    }

    @Override // com.ua.sdk.EntityList
    public boolean isEmpty() {
        return getElements() == null || getElements().size() == 0;
    }

    @Override // com.ua.sdk.EntityList
    public boolean hasPrevious() {
        return getLink("prev") != null;
    }

    @Override // com.ua.sdk.EntityList
    public boolean hasNext() {
        return getLink("next") != null;
    }

    @Override // com.ua.sdk.EntityList
    public int getTotalCount() {
        return this.mTotalCount == null ? getSize() : this.mTotalCount.intValue();
    }

    public void setTotalCount(int totalCount) {
        this.mTotalCount = Integer.valueOf(totalCount);
    }

    public void add(T item) {
        getElements().add(item);
    }

    @Override // com.ua.sdk.EntityList
    public T remove(int index) {
        if (index < 0 || index >= getElements().size()) {
            return null;
        }
        T item = getElements().remove(index);
        if (item != null && this.mTotalCount != null) {
            this.mTotalCount = Integer.valueOf(this.mTotalCount.intValue() - 1);
            return item;
        }
        return item;
    }

    @Override // com.ua.sdk.EntityList
    public EntityListRef<T> getPreviousPage() {
        Link prevLink = getLink("prev");
        if (prevLink == null) {
            return null;
        }
        return new LinkListRef(prevLink.getHref());
    }

    @Override // com.ua.sdk.EntityList
    public EntityListRef<T> getNextPage() {
        Link nextLink = getLink("next");
        if (nextLink == null) {
            return null;
        }
        return new LinkListRef(nextLink.getHref());
    }

    @Override // com.ua.sdk.Resource
    public EntityListRef<T> getRef() {
        Link selfLink = getLink("self");
        if (selfLink == null) {
            return null;
        }
        return new LinkListRef(selfLink.getHref());
    }

    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.mTotalCount);
        dest.writeList(getElements());
    }

    protected AbstractEntityList(Parcel in) {
        super(in);
        this.mTotalCount = (Integer) in.readValue(Long.class.getClassLoader());
        this.mElements = new ArrayList<>();
        in.readList(this.mElements, Resource.class.getClassLoader());
        this.mEmbedded = new HashMap(1);
        this.mEmbedded.put(getListKey(), this.mElements);
    }

    @Override // com.ua.sdk.EntityList
    public void set(int index, T object) {
        getElements().set(index, object);
    }

    public boolean preparePartials(Reference ref) {
        return false;
    }
}
