package org.jstrava.entities.segment;

/* JADX INFO: loaded from: classes68.dex */
public class LeaderBoardEntry {
    private int activity_id;
    private String athlete_gender;
    private int athlete_id;
    private String athlete_name;
    private String athlete_profile;
    private float average_hr;
    private float average_watts;
    private float distance;
    private long effort_id;
    private int elapsed_time;
    private int moving_time;
    private int rank;
    private String start_date;
    private String start_date_local;

    public String toString() {
        return this.athlete_name;
    }

    public String getAthlete_name() {
        return this.athlete_name;
    }

    public void setAthlete_name(String athlete_name) {
        this.athlete_name = athlete_name;
    }

    public int getAthlete_id() {
        return this.athlete_id;
    }

    public void setAthlete_id(int athlete_id) {
        this.athlete_id = athlete_id;
    }

    public String getAthlete_gender() {
        return this.athlete_gender;
    }

    public void setAthlete_gender(String athlete_gender) {
        this.athlete_gender = athlete_gender;
    }

    public float getAverage_hr() {
        return this.average_hr;
    }

    public void setAverage_hr(float average_hr) {
        this.average_hr = average_hr;
    }

    public float getAverage_watts() {
        return this.average_watts;
    }

    public void setAverage_watts(float average_watts) {
        this.average_watts = average_watts;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getElapsed_time() {
        return this.elapsed_time;
    }

    public void setElapsed_time(int elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public int getMoving_time() {
        return this.moving_time;
    }

    public void setMoving_time(int moving_time) {
        this.moving_time = moving_time;
    }

    public String getStart_date() {
        return this.start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_date_local() {
        return this.start_date_local;
    }

    public void setStart_date_local(String start_date_local) {
        this.start_date_local = start_date_local;
    }

    public int getActivity_id() {
        return this.activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public long getEffort_id() {
        return this.effort_id;
    }

    public void setEffort_id(long effort_id) {
        this.effort_id = effort_id;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getAthlete_profile() {
        return this.athlete_profile;
    }

    public void setAthlete_profile(String athlete_profile) {
        this.athlete_profile = athlete_profile;
    }
}
