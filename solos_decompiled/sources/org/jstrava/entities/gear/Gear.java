package org.jstrava.entities.gear;

/* JADX INFO: loaded from: classes68.dex */
public class Gear {
    private String brand_name;
    private String description;
    private float distance;
    private String frame_type;
    private String id;
    private String model_name;
    private String name;
    private boolean primary;
    private int resource_state;

    public String toString() {
        return this.name;
    }

    public Gear(String id) {
        this.id = id;
    }

    public Gear() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getPrimary() {
        return this.primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
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

    public String getBrand_name() {
        return this.brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getModel_name() {
        return this.model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getFrame_type() {
        return this.frame_type;
    }

    public void setFrame_type(String frame_type) {
        this.frame_type = frame_type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }
}
