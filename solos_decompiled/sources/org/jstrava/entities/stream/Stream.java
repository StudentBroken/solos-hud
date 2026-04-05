package org.jstrava.entities.stream;

import java.util.List;

/* JADX INFO: loaded from: classes68.dex */
public class Stream {
    private List<Object> data;
    private int original_size;
    private String resolution;
    private String series_type;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getData() {
        return this.data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getSeries_type() {
        return this.series_type;
    }

    public void setSeries_type(String series_type) {
        this.series_type = series_type;
    }

    public int getOriginal_size() {
        return this.original_size;
    }

    public void setOriginal_size(int original_size) {
        this.original_size = original_size;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
