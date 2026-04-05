package com.opentok.android;

import java.util.Date;

/* JADX INFO: loaded from: classes15.dex */
public class Stream implements Comparable<Stream> {
    protected Connection connection;
    protected Date creationTime;
    protected boolean hasAudio;
    protected boolean hasVideo;
    protected String name;
    protected Session session;
    protected String streamId;
    protected int videoHeight;
    protected StreamVideoType videoType;
    protected int videoWidth;

    public enum StreamVideoType {
        StreamVideoTypeCamera(1),
        StreamVideoTypeScreen(2);

        private int videoType;

        StreamVideoType(int type) {
            this.videoType = type;
        }

        public int getVideoType() {
            return this.videoType;
        }

        static StreamVideoType fromType(int typeId) {
            for (StreamVideoType type : values()) {
                if (type.getVideoType() == typeId) {
                    return type;
                }
            }
            throw new IllegalArgumentException("unknown type " + typeId);
        }
    }

    protected Stream(String streamId, String streamName, Date streamCreationTime, int videoWidht, int videoHeight, boolean hasAudio, boolean hasVideo, Connection connection, Session session, int videoType) {
        this.creationTime = streamCreationTime;
        this.hasAudio = hasAudio;
        this.hasVideo = hasVideo;
        this.connection = connection;
        this.streamId = streamId;
        this.name = streamName;
        this.videoWidth = videoWidht;
        this.videoHeight = videoHeight;
        this.session = session;
        this.videoType = StreamVideoType.fromType(videoType);
    }

    public String getStreamId() {
        return this.streamId;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public boolean hasVideo() {
        return this.hasVideo;
    }

    public boolean hasAudio() {
        return this.hasAudio;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public String getName() {
        return this.name;
    }

    public int getVideoWidth() {
        return this.videoWidth;
    }

    public int getVideoHeight() {
        return this.videoHeight;
    }

    public Session getSession() {
        return this.session;
    }

    public StreamVideoType getStreamVideoType() {
        return this.videoType;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Stream) && compareTo((Stream) obj) == 0;
    }

    @Override // java.lang.Comparable
    public int compareTo(Stream stream) {
        return this.streamId.compareTo(stream.getStreamId());
    }

    public String toString() {
        return String.format("streamId=%s", this.streamId);
    }

    public int hashCode() {
        return getStreamId().hashCode();
    }
}
