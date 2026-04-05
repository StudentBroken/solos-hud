package com.ua.sdk.net.json;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.UaException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes65.dex */
public class Iso8601PeriodFormat implements Parcelable {
    public static Parcelable.Creator<Iso8601PeriodFormat> CREATOR = new Parcelable.Creator<Iso8601PeriodFormat>() { // from class: com.ua.sdk.net.json.Iso8601PeriodFormat.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Iso8601PeriodFormat createFromParcel(Parcel source) {
            return new Iso8601PeriodFormat(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Iso8601PeriodFormat[] newArray(int size) {
            return new Iso8601PeriodFormat[size];
        }
    };
    private static final String PERIOD = "P";
    final List<Duration> durations;

    private Iso8601PeriodFormat(Builder init) {
        this.durations = new ArrayList();
        this.durations.addAll(init.durations);
    }

    private Iso8601PeriodFormat(Parcel in) {
        this.durations = new ArrayList();
        this.durations.addAll(in.readArrayList(Duration.class.getClassLoader()));
    }

    public static Iso8601PeriodFormat parse(String period) throws UaException {
        try {
            String period2 = period.toUpperCase();
            String[] buckets = period2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            if (buckets.length == 0 || !buckets[0].equalsIgnoreCase(PERIOD) || buckets.length % 2 == 0) {
                throw new UaException("Unable to parse '" + period2 + "'");
            }
            Builder builder = new Builder();
            for (int i = 1; i < buckets.length; i += 2) {
                builder.addDuration(new Duration(Integer.valueOf(buckets[i]).intValue(), Interval.parse(buckets[i + 1])));
            }
            return builder.build();
        } catch (Exception e) {
            throw new UaException("Unable to parse '" + period + "'", e);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.durations);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PERIOD);
        for (Duration duration : this.durations) {
            sb.append(duration.toString());
        }
        return sb.toString().toUpperCase();
    }

    static class Builder {
        final Set<Interval> intervals = Collections.synchronizedSet(EnumSet.noneOf(Interval.class));
        final List<Duration> durations = new ArrayList();

        public Builder addDuration(Duration duration) {
            if (this.intervals.add(duration.interval)) {
                this.durations.add(duration);
            }
            return this;
        }

        public Iso8601PeriodFormat build() {
            Collections.sort(this.durations, new Comparator<Duration>() { // from class: com.ua.sdk.net.json.Iso8601PeriodFormat.Builder.1
                @Override // java.util.Comparator
                public int compare(Duration lhs, Duration rhs) {
                    if (rhs.interval.weight > lhs.interval.weight) {
                        return 1;
                    }
                    return rhs.interval.weight == lhs.interval.weight ? 0 : -1;
                }
            });
            return new Iso8601PeriodFormat(this);
        }
    }

    static class Duration implements Parcelable {
        public static Parcelable.Creator<Duration> CREATOR = new Parcelable.Creator<Duration>() { // from class: com.ua.sdk.net.json.Iso8601PeriodFormat.Duration.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Duration createFromParcel(Parcel source) {
                return new Duration(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Duration[] newArray(int size) {
                return new Duration[size];
            }
        };
        final int elapsed;
        final Interval interval;

        public Duration(int elapsed, Interval interval) {
            this.elapsed = elapsed;
            this.interval = interval;
        }

        private Duration(Parcel in) {
            this.elapsed = in.readInt();
            this.interval = (Interval) in.readValue(Interval.class.getClassLoader());
        }

        public String toString() {
            return this.elapsed + this.interval.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.elapsed);
            dest.writeValue(this.interval);
        }
    }

    enum Interval {
        DAYS("D", 0),
        WEEKS("W", 1),
        MONTHS("M", 2),
        YEARS("Y", 3);

        String name;
        int weight;

        Interval(String name, int weight) {
            this.name = name;
            this.weight = weight;
        }

        public static Interval parse(String value) throws UaException {
            Interval[] arr$ = values();
            for (Interval interval : arr$) {
                if (interval.name.equalsIgnoreCase(value)) {
                    return interval;
                }
            }
            throw new UaException(value + " is currently not supported!");
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }
}
