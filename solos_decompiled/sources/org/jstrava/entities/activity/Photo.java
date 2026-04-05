package org.jstrava.entities.activity;

/* JADX INFO: loaded from: classes68.dex */
public class Photo {
    private int activity_id;
    private String caption;
    private String created_at;
    private int id;
    private String[] location;
    private String ref;
    private int resource_state;
    private String type;
    private String uid;
    private String uploaded_at;

    public String toString() {
        return this.ref;
    }

    public Photo() {
    }

    public Photo(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivity_id() {
        return this.activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getResource_state() {
        return this.resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public String getRef() {
        return this.ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUploaded_at() {
        return this.uploaded_at;
    }

    public void setUploaded_at(String uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String[] getLocation() {
        return this.location;
    }

    public void setLocation(String[] location) {
        this.location = location;
    }
}
