package com.opentok.impl;

import com.opentok.android.Connection;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import java.util.Date;

/* JADX INFO: loaded from: classes15.dex */
public class StreamImpl extends Stream {
    protected long pstream;

    private native void finalizeNative(long j);

    public StreamImpl(long pstream, String streamId, String streamName, Date creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, Connection connection, Session session, int videoType) {
        super(streamId, streamName, creationTime, videoWidth, videoHeight, hasAudio, hasVideo, connection, session, videoType);
        this.pstream = pstream;
    }

    public StreamImpl(long pstream, String streamId, String streamName, Date creationTime, int videoWidth, int videoHeight, boolean hasAudio, boolean hasVideo, Session session, int videoType) {
        super(streamId, streamName, creationTime, videoWidth, videoHeight, hasAudio, hasVideo, null, session, videoType);
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPStream() {
        return this.pstream;
    }

    protected void finalize() throws Throwable {
        finalizeNative(this.pstream);
        super.finalize();
    }
}
