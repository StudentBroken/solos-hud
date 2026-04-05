package com.ua.sdk.group.objective;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Period;
import com.ua.sdk.net.json.Iso8601PeriodFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class GroupObjectiveImpl extends ApiTransferObject implements GroupObjective {
    public static Parcelable.Creator<GroupObjectiveImpl> CREATOR = new Parcelable.Creator<GroupObjectiveImpl>() { // from class: com.ua.sdk.group.objective.GroupObjectiveImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupObjectiveImpl createFromParcel(Parcel source) {
            return new GroupObjectiveImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupObjectiveImpl[] newArray(int size) {
            return new GroupObjectiveImpl[size];
        }
    };
    private static final String LINK_DATA_TYPE = "data_type";

    @SerializedName("criteria")
    Criteria criteria;
    transient DataField dataField;
    transient DataType dataType;

    @SerializedName("data_type_field")
    String dataTypeField;
    transient EntityRef<DataType> dataTypeRef;

    @SerializedName("end_datetime")
    Date endDate;

    @SerializedName("iteration")
    Integer iteration;

    @SerializedName("iteration_end_datetime")
    Date iterationEndDate;

    @SerializedName("iteration_start_datetime")
    Date iterationStartDate;

    @SerializedName("name")
    String name;

    @SerializedName("period")
    Period period;

    @SerializedName("start_datetime")
    Date startDate;

    public GroupObjectiveImpl() {
    }

    private GroupObjectiveImpl(Parcel in) {
        super(in);
        this.period = (Period) in.readValue(Iso8601PeriodFormat.class.getClassLoader());
        this.startDate = (Date) in.readValue(Date.class.getClassLoader());
        this.endDate = (Date) in.readValue(Date.class.getClassLoader());
        this.iterationStartDate = (Date) in.readValue(Date.class.getClassLoader());
        this.iterationEndDate = (Date) in.readValue(Date.class.getClassLoader());
        this.iteration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dataTypeField = in.readString();
        this.name = in.readString();
        this.criteria = (Criteria) in.readParcelable(Criteria.class.getClassLoader());
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Period getPeriod() {
        return this.period;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Date getStartDate() {
        return this.startDate;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Date getEndDate() {
        return this.endDate;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Date getIterationStartDate() {
        return this.iterationStartDate;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Date getIterationEndDate() {
        return this.iterationEndDate;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Integer getIteration() {
        return this.iteration;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public EntityRef<DataType> getDataTypeRef() {
        Link dataType;
        if (this.dataTypeRef == null && (dataType = getLink(LINK_DATA_TYPE)) != null) {
            this.dataTypeRef = new LinkEntityRef(dataType.getId(), dataType.getHref());
        }
        return this.dataTypeRef;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public DataType getDataType() {
        if (this.dataType == null && getDataTypeRef() != null) {
            this.dataType = BaseDataTypes.ALL_BASE_TYPE_MAP.get(this.dataTypeRef.getId());
        }
        return this.dataType;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public DataField getDataField() {
        if (this.dataField == null && this.dataTypeField != null) {
            this.dataField = BaseDataTypes.findDataField(this.dataTypeField, getDataType());
        }
        return this.dataField;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setIterationStartDate(Date iterationStartDate) {
        this.iterationStartDate = iterationStartDate;
    }

    public void setIterationEndDate(Date iterationEndDate) {
        this.iterationEndDate = iterationEndDate;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public void setDataField(DataField dataField) {
        if (dataField != null) {
            this.dataField = BaseDataTypes.findDataField(dataField.getId(), getDataType());
        }
    }

    public void setDataTypeField(String dataTypeField) {
        this.dataTypeField = dataTypeField;
    }

    public void setDataTypeRef(EntityRef<DataType> dataTypeRef) {
        this.dataTypeRef = dataTypeRef;
        addLink(LINK_DATA_TYPE, new Link(dataTypeRef.getHref(), dataTypeRef.getId()));
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public Criteria getCriteria() {
        return this.criteria;
    }

    @Override // com.ua.sdk.group.objective.GroupObjective
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<GroupObjective> getRef() {
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
        dest.writeValue(this.period);
        dest.writeValue(this.startDate);
        dest.writeValue(this.endDate);
        dest.writeValue(this.iterationStartDate);
        dest.writeValue(this.iterationEndDate);
        dest.writeValue(this.iteration);
        dest.writeString(this.dataTypeField);
        dest.writeString(this.name);
        dest.writeParcelable(this.criteria, flags);
    }
}
