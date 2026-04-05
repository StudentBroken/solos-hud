package com.ua.sdk.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ua.sdk.UaException;
import com.ua.sdk.net.json.Iso8601PeriodFormat;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class Period implements Parcelable {
    final String period;
    public static final Period ONE_DAY = new Period("P1D");
    public static final Period ONE_WEEK = new Period("P1W");
    public static final Period ONE_MONTH = new Period("P1M");
    public static final Period ONE_YEAR = new Period("P1Y");
    public static Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>() { // from class: com.ua.sdk.internal.Period.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Period createFromParcel(Parcel source) {
            return new Period(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };

    private Period(String period) {
        this.period = period;
    }

    private Period(Iso8601PeriodFormat format) {
        this(format.toString());
    }

    private Period(Parcel in) {
        this.period = in.readString();
    }

    public boolean isValid(Period... periods) {
        if (periods == null) {
            return true;
        }
        for (Period period : periods) {
            if (period.toString().equalsIgnoreCase(this.period)) {
                return true;
            }
        }
        return false;
    }

    public static Period parse(String period) throws UaException {
        return new Period(Iso8601PeriodFormat.parse(period));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.period);
    }

    public String toString() {
        return this.period;
    }

    public static class PeriodAdapter implements JsonSerializer<Period>, JsonDeserializer<Period> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public Period deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new Period(Iso8601PeriodFormat.parse(json.getAsString()));
            } catch (UaException e) {
                return null;
            }
        }

        @Override // com.google.gson.JsonSerializer
        public JsonElement serialize(Period src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.period);
        }
    }
}
