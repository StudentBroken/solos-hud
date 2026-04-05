package org.jstrava.entities.activity;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.jstrava.entities.athlete.Athlete;
import org.jstrava.entities.segment.SegmentEffort;
import org.jstrava.utility.MultipartUtility;

/* JADX INFO: loaded from: classes68.dex */
public class Activity {

    @SerializedName("private")
    private boolean PRIVATE;
    private int achievement_count;
    private Athlete athlete;
    private int athlete_count;
    private float average_cadence;
    private float average_heartrate;
    private float average_speed;
    private int average_temp;
    private float average_watts;
    private List<SegmentEffort> best_efforts;
    private float calories;
    private int comment_count;
    private boolean commute;
    private float distance;
    private int elapsed_time;
    private String[] end_latlng;
    private String external_id;
    private boolean flagged;
    private String gear_id;
    private boolean has_kudoed;
    private long id;
    private float kilojoules;
    private int kudos_count;
    private String location_city;
    private String location_state;
    private boolean manual;
    private Polyline map;
    private float max_heartrate;
    private float max_speed;
    private int moving_time;
    private String name;
    private int photo_count;
    private int resource_state;
    private MultipartUtility.ResponseType responseType = MultipartUtility.ResponseType.OK;
    private List<SegmentEffort> segment_efforts;
    private List<SplitsMetric> splits_metric;
    private List<SplitsStandard> splits_standard;
    private String start_date;
    private String start_date_local;
    private String[] start_latlng;
    private String time_zone;
    private float total_elevation_gain;
    private boolean trainer;
    private int truncated;
    private String type;
    private long upload_id;

    public String toString() {
        return "{" + this.name + ", " + this.id + ", uploadId: " + getUpload_id() + "}";
    }

    public Activity() {
    }

    public MultipartUtility.ResponseType getResponseType() {
        return this.responseType;
    }

    public void setResponseType(MultipartUtility.ResponseType responseType) {
        this.responseType = responseType;
    }

    public Activity(long id) {
        this.id = id;
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

    public String getExternal_id() {
        return this.external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public Athlete getAthlete() {
        return this.athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getMoving_time() {
        return this.moving_time;
    }

    public void setMoving_time(int moving_time) {
        this.moving_time = moving_time;
    }

    public int getElapsed_time() {
        return this.elapsed_time;
    }

    public void setElapsed_time(int elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public float getTotal_elevation_gain() {
        return this.total_elevation_gain;
    }

    public void setTotal_elevation_gain(float total_elevation_gain) {
        this.total_elevation_gain = total_elevation_gain;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart_date() {
        return this.start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public boolean isTrainer() {
        return this.trainer;
    }

    public boolean isCommute() {
        return this.commute;
    }

    public boolean isManual() {
        return this.manual;
    }

    public boolean isPRIVATE() {
        return this.PRIVATE;
    }

    public boolean isFlagged() {
        return this.flagged;
    }

    public boolean isHas_kudoed() {
        return this.has_kudoed;
    }

    public String getStart_date_local() {
        return this.start_date_local;
    }

    public void setStart_date_local(String start_date_local) {
        this.start_date_local = start_date_local;
    }

    public String getTime_zone() {
        return this.time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
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

    public String getLocation_city() {
        return this.location_city;
    }

    public void setLocation_city(String location_city) {
        this.location_city = location_city;
    }

    public String getLocation_state() {
        return this.location_state;
    }

    public void setLocation_state(String location_state) {
        this.location_state = location_state;
    }

    public int getAchievement_count() {
        return this.achievement_count;
    }

    public void setAchievement_count(int achievement_count) {
        this.achievement_count = achievement_count;
    }

    public int getKudos_count() {
        return this.kudos_count;
    }

    public void setKudos_count(int kudos_count) {
        this.kudos_count = kudos_count;
    }

    public int getComment_count() {
        return this.comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getAthlete_count() {
        return this.athlete_count;
    }

    public void setAthlete_count(int athlete_count) {
        this.athlete_count = athlete_count;
    }

    public int getPhoto_count() {
        return this.photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public Polyline getMap() {
        return this.map;
    }

    public void setMap(Polyline map) {
        this.map = map;
    }

    public boolean getTrainer() {
        return this.trainer;
    }

    public void setTrainer(boolean trainer) {
        this.trainer = trainer;
    }

    public boolean getCommute() {
        return this.commute;
    }

    public void setCommute(boolean commute) {
        this.commute = commute;
    }

    public boolean getManual() {
        return this.manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean getPRIVATE() {
        return this.PRIVATE;
    }

    public void setPRIVATE(boolean PRIVATE) {
        this.PRIVATE = PRIVATE;
    }

    public boolean getFlagged() {
        return this.flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public String getGear_id() {
        return this.gear_id;
    }

    public void setGear_id(String gear_id) {
        this.gear_id = gear_id;
    }

    public float getAverage_speed() {
        return this.average_speed;
    }

    public void setAverage_speed(float average_speed) {
        this.average_speed = average_speed;
    }

    public float getMax_speed() {
        return this.max_speed;
    }

    public void setMax_speed(float max_speed) {
        this.max_speed = max_speed;
    }

    public float getAverage_cadence() {
        return this.average_cadence;
    }

    public void setAverage_cadence(float average_cadence) {
        this.average_cadence = average_cadence;
    }

    public int getAverage_temp() {
        return this.average_temp;
    }

    public void setAverage_temp(int average_temp) {
        this.average_temp = average_temp;
    }

    public float getAverage_watts() {
        return this.average_watts;
    }

    public void setAverage_watts(float average_watts) {
        this.average_watts = average_watts;
    }

    public float getKilojoules() {
        return this.kilojoules;
    }

    public void setKilojoules(float kilojoules) {
        this.kilojoules = kilojoules;
    }

    public float getAverage_heartrate() {
        return this.average_heartrate;
    }

    public void setAverage_heartrate(float average_heartrate) {
        this.average_heartrate = average_heartrate;
    }

    public float getMax_heartrate() {
        return this.max_heartrate;
    }

    public void setMax_heartrate(float max_heartrate) {
        this.max_heartrate = max_heartrate;
    }

    public float getCalories() {
        return this.calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public int getTruncated() {
        return this.truncated;
    }

    public void setTruncated(int truncated) {
        this.truncated = truncated;
    }

    public boolean getHas_kudoed() {
        return this.has_kudoed;
    }

    public void setHas_kudoed(boolean has_kudoed) {
        this.has_kudoed = has_kudoed;
    }

    public List<SegmentEffort> getSegment_efforts() {
        return this.segment_efforts;
    }

    public void setSegment_efforts(List<SegmentEffort> segment_efforts) {
        this.segment_efforts = segment_efforts;
    }

    public List<SplitsMetric> getSplits_metric() {
        return this.splits_metric;
    }

    public void setSplits_metric(List<SplitsMetric> splits_metric) {
        this.splits_metric = splits_metric;
    }

    public List<SplitsStandard> getSplits_standard() {
        return this.splits_standard;
    }

    public void setSplits_standard(List<SplitsStandard> splits_standard) {
        this.splits_standard = splits_standard;
    }

    public List<SegmentEffort> getBest_efforts() {
        return this.best_efforts;
    }

    public void setBest_efforts(List<SegmentEffort> best_efforts) {
        this.best_efforts = best_efforts;
    }

    public long getUpload_id() {
        return this.upload_id;
    }

    public void setUpload_id(long upload_id) {
        this.upload_id = upload_id;
    }
}
