package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutRef extends LinkEntityRef<Workout> {
    public static final Parcelable.Creator<WorkoutRef> CREATOR = new Parcelable.Creator<WorkoutRef>() { // from class: com.ua.sdk.workout.WorkoutRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutRef createFromParcel(Parcel in) {
            return new WorkoutRef(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutRef[] newArray(int size) {
            return new WorkoutRef[size];
        }
    };

    public WorkoutRef(String href) {
        super(href);
    }

    public WorkoutRef(String id, String href) {
        super(id, href);
    }

    public WorkoutRef(String id, long localId, String href) {
        super(id, localId, href);
    }

    public WorkoutRef(String id, long localId, String href, int options) {
        super(id, localId, href, options);
    }

    private WorkoutRef(FieldBuilder init) {
        super(init.id, init.getHref());
    }

    private WorkoutRef(Builder init) {
        super(init.id, init.getHref());
    }

    public static FieldBuilder getFieldBuilder(WorkoutRef ref) {
        return new FieldBuilder();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class FieldBuilder extends BaseReferenceBuilder {
        private String id;

        private FieldBuilder(WorkoutRef ref) {
            super(ref.getHref());
            this.id = ref.getId();
        }

        public FieldBuilder setTimeSeriesField(boolean fieldSet) {
            if (fieldSet) {
                setParam("field_set", "time_series");
            } else {
                removeParam("field_set");
            }
            return this;
        }

        public WorkoutRef build() {
            return new WorkoutRef(this);
        }
    }

    public static class Builder extends BaseReferenceBuilder {
        protected String id;

        public Builder() {
            super("/v7.0/workout/{id}/");
        }

        public Builder setId(String id) {
            this.id = id;
            setParam("id", id);
            return this;
        }

        public WorkoutRef build() {
            if (this.id == null) {
                throw new IllegalArgumentException("Id must be set to build workout reference");
            }
            return new WorkoutRef(this);
        }
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private WorkoutRef(Parcel in) {
        super(in);
    }
}
