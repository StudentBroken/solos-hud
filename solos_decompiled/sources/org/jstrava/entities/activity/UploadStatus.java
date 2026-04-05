package org.jstrava.entities.activity;

/* JADX INFO: loaded from: classes68.dex */
public class UploadStatus {
    private long activity_id;
    private String error;
    private String external_id;
    private String id;
    public String json;
    private String status;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternal_id() {
        return this.external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public long getActivity_id() {
        return this.activity_id;
    }

    public void setActivity_id(long activity_id) {
        this.activity_id = activity_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String toString() {
        return this.status;
    }
}
