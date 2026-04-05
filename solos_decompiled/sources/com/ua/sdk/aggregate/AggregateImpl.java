package com.ua.sdk.aggregate;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Period;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class AggregateImpl extends ApiTransferObject implements Aggregate {
    public static Parcelable.Creator<AggregateImpl> CREATOR = new Parcelable.Creator<AggregateImpl>() { // from class: com.ua.sdk.aggregate.AggregateImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateImpl createFromParcel(Parcel source) {
            return new AggregateImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateImpl[] newArray(int size) {
            return new AggregateImpl[size];
        }
    };
    protected static final String LINK_DATA_TYPE = "data_type";
    protected static final String LINK_USER = "user";
    transient EntityRef<DataType> dataTypeRef;

    @SerializedName("period")
    protected Period period;

    @SerializedName("periods")
    protected List<AggregateSummary> periods;

    @SerializedName("summary")
    protected AggregateSummary summary;
    transient EntityRef<User> userRef;

    public AggregateImpl() {
    }

    protected AggregateImpl(Parcel in) {
        super(in);
        this.periods = new ArrayList();
        this.summary = (AggregateSummary) in.readValue(AggregateSummaryImpl.class.getClassLoader());
        in.readList(this.periods, AggregateSummary.class.getClassLoader());
        this.period = (Period) in.readValue(Period.class.getClassLoader());
    }

    @Override // com.ua.sdk.aggregate.Aggregate
    public AggregateSummary getSummary() {
        return this.summary;
    }

    public void setSummary(AggregateSummary summary) {
        this.summary = summary;
    }

    @Override // com.ua.sdk.aggregate.Aggregate
    public List<AggregateSummary> getPeriods() {
        return this.periods;
    }

    public void setPeriods(List<AggregateSummary> list) {
        List arrayList = list;
        if (list == null) {
            arrayList = new ArrayList();
        }
        arrayList.clear();
        arrayList.addAll(arrayList);
    }

    @Override // com.ua.sdk.aggregate.Aggregate
    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override // com.ua.sdk.aggregate.Aggregate
    public EntityRef<User> getUserRef() {
        Link user;
        if (this.userRef == null && (user = getLink("user")) != null) {
            this.userRef = new LinkEntityRef(user.getId(), user.getHref());
        }
        return this.userRef;
    }

    public void setUserRef(EntityRef<User> userRef) {
        if (userRef != null) {
            this.userRef = userRef;
            addLink("user", new Link(userRef.getHref(), userRef.getId()));
        }
    }

    @Override // com.ua.sdk.aggregate.Aggregate
    public EntityRef<DataType> getDataTypeRef() {
        Link dataType;
        if (this.dataTypeRef == null && (dataType = getLink(LINK_DATA_TYPE)) != null) {
            this.dataTypeRef = new LinkEntityRef(dataType.getId(), dataType.getHref());
        }
        return this.dataTypeRef;
    }

    public void setDataTypeRef(EntityRef<DataType> dataTypeRef) {
        if (dataTypeRef != null) {
            this.dataTypeRef = dataTypeRef;
            addLink(LINK_DATA_TYPE, new Link(dataTypeRef.getHref(), dataTypeRef.getId()));
        }
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<Aggregate> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (this.periods == null) {
            this.periods = new ArrayList();
        }
        dest.writeValue(this.summary);
        dest.writeList(this.periods);
        dest.writeValue(this.period);
    }
}
