package org.jstrava.entities.activity;

/* JADX INFO: loaded from: classes68.dex */
public class Polyline {
    private String id;
    private String polyline;
    private String resource_state;
    private String summary_polyline;

    public String toString() {
        return this.id;
    }

    public Polyline(String id) {
        this.id = id;
    }

    public Polyline() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolyline() {
        return this.polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public String getSummary_polyline() {
        return this.summary_polyline;
    }

    public void setSummary_polyline(String summary_polyline) {
        this.summary_polyline = summary_polyline;
    }

    public String getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(String resource_state) {
        this.resource_state = resource_state;
    }
}
