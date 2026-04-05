package com.kopin.peloton.ride;

import com.kopin.peloton.debug.LogEvent;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class Activity {
    private static final String format = "id %s, name %s, description %s";
    public String Description;
    public ArrayList<LogEvent> EventLog;
    public String Id;
    public String Name;

    public Activity() {
        this.Id = "";
        this.Name = "";
        this.Description = "";
        this.EventLog = new ArrayList<>();
    }

    public Activity(String name, String description) {
        this.Id = "";
        this.Name = "";
        this.Description = "";
        this.EventLog = new ArrayList<>();
        this.Name = name;
        this.Description = description;
    }

    public String toString() {
        return String.format(Locale.US, format, this.Id, this.Name, this.Description);
    }
}
