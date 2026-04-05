package org.jstrava.entities.segment;

import org.jstrava.entities.activity.Polyline;

/* JADX INFO: loaded from: classes68.dex */
public class Segment {
    private boolean PRIVATE;
    private String activity_type;
    private int athlete_count;
    private float average_grade;
    private String city;
    private int climb_category;
    private String climb_category_desc;
    private String created_at;
    private float distance;
    private int effort_count;
    private float elevation_high;
    private float elevation_low;
    private String[] end_latlng;
    private boolean hazardous;
    private long id;
    private Polyline map;
    private float maximum_grade;
    private String name;
    private float pr_distance;
    private int pr_time;
    private int resource_state;
    private boolean starred;
    private String[] start_latlng;
    private String state;
    private float total_elevation_gain;
    private String updated_at;

    public Segment(long id) {
        this.id = id;
    }

    public Segment() {
    }

    public String toString() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity_type() {
        return this.activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAverage_grade() {
        return this.average_grade;
    }

    public void setAverage_grade(float average_grade) {
        this.average_grade = average_grade;
    }

    public float getMaximum_grade() {
        return this.maximum_grade;
    }

    public void setMaximum_grade(float maximum_grade) {
        this.maximum_grade = maximum_grade;
    }

    public float getElevation_high() {
        return this.elevation_high;
    }

    public void setElevation_high(float elevation_high) {
        this.elevation_high = elevation_high;
    }

    public float getElevation_low() {
        return this.elevation_low;
    }

    public void setElevation_low(float elevation_low) {
        this.elevation_low = elevation_low;
    }

    public String[] getStart_latlng() {
        return this.start_latlng;
    }

    public void setStart_latlng(String[] start_latlng) {
        this.start_latlng = start_latlng;
    }

    public String[] getEnd_latlng() {
        return this.end_latlng;
    }

    public void setEnd_latlng(String[] end_latlng) {
        this.end_latlng = end_latlng;
    }

    public int getClimb_category() {
        return this.climb_category;
    }

    public void setClimb_category(int climb_category) {
        this.climb_category = climb_category;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean getPRIVATE() {
        return this.PRIVATE;
    }

    public void setPRIVATE(boolean PRIVATE) {
        this.PRIVATE = PRIVATE;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public float getTotal_elevation_gain() {
        return this.total_elevation_gain;
    }

    public void setTotal_elevation_gain(float total_elevation_gain) {
        this.total_elevation_gain = total_elevation_gain;
    }

    public Polyline getMap() {
        return this.map;
    }

    public void setMap(Polyline map) {
        this.map = map;
    }

    public int getEffort_count() {
        return this.effort_count;
    }

    public void setEffort_count(int effort_count) {
        this.effort_count = effort_count;
    }

    public int getAthlete_count() {
        return this.athlete_count;
    }

    public void setAthlete_count(int athlete_count) {
        this.athlete_count = athlete_count;
    }

    public boolean getHazardous() {
        return this.hazardous;
    }

    public void setHazardous(boolean hazardous) {
        this.hazardous = hazardous;
    }

    public int getPr_time() {
        return this.pr_time;
    }

    public void setPr_time(int pr_time) {
        this.pr_time = pr_time;
    }

    public float getPr_distance() {
        return this.pr_distance;
    }

    public void setPr_distance(float pr_distance) {
        this.pr_distance = pr_distance;
    }

    public boolean getStarred() {
        return this.starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getClimb_category_desc() {
        return this.climb_category_desc;
    }

    public void setClimb_category_desc(String climb_category_desc) {
        this.climb_category_desc = climb_category_desc;
    }
}
