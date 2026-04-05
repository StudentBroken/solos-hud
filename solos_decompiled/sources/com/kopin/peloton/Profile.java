package com.kopin.peloton;

import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class Profile {
    public long DateOfBirth;
    public String Gender;
    public double Height;
    public String Name;
    public long TimeStamp;
    public int Units;
    public double Weight;

    public Profile() {
        this.Name = "";
        this.DateOfBirth = 0L;
        this.Gender = "";
        this.Weight = 0.0d;
        this.Height = 0.0d;
        this.Units = 0;
        this.TimeStamp = System.currentTimeMillis();
    }

    public Profile(String name, long dob, double weight) {
        this.Name = "";
        this.DateOfBirth = 0L;
        this.Gender = "";
        this.Weight = 0.0d;
        this.Height = 0.0d;
        this.Units = 0;
        this.TimeStamp = System.currentTimeMillis();
        this.Name = name;
        this.DateOfBirth = dob;
        this.Weight = weight;
    }

    public String toString() {
        return String.format(Locale.US, "name %s, dob %d, weight %f, gender %s", this.Name, Long.valueOf(this.DateOfBirth), Double.valueOf(this.Weight), this.Gender);
    }
}
