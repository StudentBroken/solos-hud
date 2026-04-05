package org.jstrava.entities.club;

/* JADX INFO: loaded from: classes68.dex */
public class Club {
    private int id;
    private String name;
    private int resource_state;

    public String toString() {
        return this.name;
    }

    public Club(int id) {
        this.id = id;
    }

    public Club() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }
}
