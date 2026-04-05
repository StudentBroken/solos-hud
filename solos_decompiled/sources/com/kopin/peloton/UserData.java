package com.kopin.peloton;

import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class UserData {
    public ArrayList<Bike> Bikes;
    public ArrayList<FTP> Ftps;
    public ArrayList<PHR> Phrs;
    public ArrayList<RFTP> Rtps;
    public Profile UserProfile;

    public String toString() {
        return String.format(Locale.US, "name %s, dob %d, weight %f, gender %s, numBikes %d", this.UserProfile.Name, Long.valueOf(this.UserProfile.DateOfBirth), Double.valueOf(this.UserProfile.Weight), this.UserProfile.Gender, Integer.valueOf(this.Bikes.size()));
    }
}
