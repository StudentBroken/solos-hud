package org.jstrava.entities.activity;

import org.jstrava.entities.athlete.Athlete;

/* JADX INFO: loaded from: classes68.dex */
public class Comment {
    private Athlete athlete;
    private String created_at;
    private int id;
    private int resource_state;
    private String text;

    public String toString() {
        return this.text;
    }

    public Comment(int id) {
        this.id = id;
    }

    public Comment() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Athlete getAthlete() {
        return this.athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
