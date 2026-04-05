package com.ua.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/* JADX INFO: loaded from: classes65.dex */
public class LocalDate implements Parcelable {
    public static final int APRIL = 3;
    public static final int AUGUST = 7;
    public static final int DECEMBER = 11;
    public static final int FEBUARY = 1;
    public static final int JANUARY = 0;
    public static final int JULY = 6;
    public static final int JUNE = 5;
    public static final int MARCH = 2;
    public static final int MAY = 4;
    public static final int NOVEMBER = 10;
    public static final int OCTOBER = 9;
    public static final int SEPTEMPER = 8;
    private int dayOfMonth;
    private int month;
    private int year;
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() { // from class: com.ua.sdk.LocalDate.1
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat get() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static Parcelable.Creator<LocalDate> CREATOR = new Parcelable.Creator<LocalDate>() { // from class: com.ua.sdk.LocalDate.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocalDate createFromParcel(Parcel source) {
            return new LocalDate(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocalDate[] newArray(int size) {
            return new LocalDate[size];
        }
    };

    public LocalDate(int year, int month, int dayOfMonth) {
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException("month must be a value 0 - 11");
        }
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDayOfMonth() {
        return this.dayOfMonth;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.dayOfMonth);
    }

    private LocalDate(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.dayOfMonth = in.readInt();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(10);
        sb.append(this.year);
        sb.append('-');
        if (this.month < 9) {
            sb.append('0');
        }
        sb.append(this.month + 1);
        sb.append('-');
        if (this.dayOfMonth < 10) {
            sb.append('0');
        }
        sb.append(this.dayOfMonth);
        return sb.toString();
    }

    public static final LocalDate fromString(String string) {
        if (string == null) {
            return null;
        }
        try {
            SimpleDateFormat format = DATE_FORMAT.get();
            format.parse(string);
            Calendar cal = format.getCalendar();
            return fromCalendar(cal);
        } catch (ParseException e) {
            UaLog.debug("Error parsing LocalDate", (Throwable) e);
            return null;
        }
    }

    public static final LocalDate fromCalendar(Calendar cal) {
        if (cal == null) {
            return null;
        }
        int year = cal.get(1);
        int month = cal.get(2);
        int dayOfMonth = cal.get(5);
        return new LocalDate(year, month, dayOfMonth);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalDate)) {
            return false;
        }
        LocalDate localDate = (LocalDate) o;
        return this.dayOfMonth == localDate.dayOfMonth && this.month == localDate.month && this.year == localDate.year;
    }

    public int hashCode() {
        int result = this.year;
        return (((result * 31) + this.month) * 31) + this.dayOfMonth;
    }
}
