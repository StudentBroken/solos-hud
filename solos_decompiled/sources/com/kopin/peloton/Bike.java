package com.kopin.peloton;

import android.content.ContentValues;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class Bike {
    public String BikeId;
    public String Name;
    public long TimeStamp;
    public double Weight;
    public double WheelCircumference;

    public Bike() {
        this.BikeId = "";
        this.Name = "";
        this.WheelCircumference = 0.0d;
        this.Weight = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
    }

    public Bike(String name, double weight, double wheelCircumference) {
        this.BikeId = "";
        this.Name = "";
        this.WheelCircumference = 0.0d;
        this.Weight = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        this.Name = name;
        this.Weight = weight;
        this.WheelCircumference = wheelCircumference;
    }

    public Bike(ContentValues values, String keyName, String keyWheel, String keyWeight) {
        this.BikeId = "";
        this.Name = "";
        this.WheelCircumference = 0.0d;
        this.Weight = 0.0d;
        this.TimeStamp = System.currentTimeMillis();
        if (values.containsKey(keyName)) {
            this.Name = values.getAsString(keyName);
        }
        if (values.containsKey(keyWheel)) {
            this.WheelCircumference = values.getAsInteger(keyWheel).intValue();
        }
        if (values.containsKey(keyWeight)) {
            this.Weight = values.getAsDouble(keyWeight).doubleValue();
        }
    }

    public String toString() {
        return String.format(Locale.US, "bikeId %s\nname %s, wheel %f, weight %f", this.BikeId, this.Name, Double.valueOf(this.WheelCircumference), Double.valueOf(this.Weight));
    }

    public ContentValues toContentValues(String keyName, String keyWheel, String keyWeight) {
        ContentValues values = new ContentValues();
        values.put(keyName, this.Name);
        values.put(keyWheel, Double.valueOf(this.WheelCircumference));
        values.put(keyWeight, Double.valueOf(this.Weight));
        return values;
    }
}
