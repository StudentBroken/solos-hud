package org.jstrava.entities.activity;

import java.util.List;

/* JADX INFO: loaded from: classes68.dex */
public class Zone {
    private double athlete_weight;
    private double bike_weight;
    private boolean custom_zones;
    private List<DistributionBucket> distribution_buckets;
    private int max;
    private int points;
    private int resource_state;
    private int score;
    private boolean sensor_based;
    private String type;

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<DistributionBucket> getDistribution_buckets() {
        return this.distribution_buckets;
    }

    public void setDistribution_buckets(List<DistributionBucket> distribution_buckets) {
        this.distribution_buckets = distribution_buckets;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public boolean isSensor_based() {
        return this.sensor_based;
    }

    public void setSensor_based(boolean sensor_based) {
        this.sensor_based = sensor_based;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isCustom_zones() {
        return this.custom_zones;
    }

    public void setCustom_zones(boolean custom_zones) {
        this.custom_zones = custom_zones;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getBike_weight() {
        return this.bike_weight;
    }

    public void setBike_weight(double bike_weight) {
        this.bike_weight = bike_weight;
    }

    public double getAthlete_weight() {
        return this.athlete_weight;
    }

    public void setAthlete_weight(double athlete_weight) {
        this.athlete_weight = athlete_weight;
    }
}
