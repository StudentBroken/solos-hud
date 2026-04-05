package com.kopin.solos.storage;

import android.location.Location;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import java.util.ArrayList;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes54.dex */
public class RideRecorder extends Trackable implements SplitHelper.SplitListener {
    private static final String TAG = "RideRecorder";
    private static final int VALUES_TO_SAVE = 20;
    private volatile Record mCurrent;
    private int mNextResolution;
    private int mRecordCount;
    private TimeHelper mTimeHelper;
    private LinkedList<Record> mRecords = new LinkedList<>();
    private final Object mRecordLock = new Object();
    private long mRideId = -1;
    private long mRouteId = -1;
    private ArrayList<AveragedRecord> mAverages = new ArrayList<>();

    public interface OnCoordinateSavedCallback {
        void onPositionSaved(long j, Coordinate coordinate);
    }

    public interface OnRecordSavedCallback {
        void onRecordSaved(Record record);
    }

    public RideRecorder(TimeHelper timeHelper) {
        this.mTimeHelper = timeHelper;
    }

    private long getTimeStamp() {
        long timestamp;
        if (this.mTimeHelper != null) {
            return this.mTimeHelper.getTime();
        }
        synchronized (this.mRecordLock) {
            if (this.mCurrent != null) {
                timestamp = this.mCurrent.getTimestamp();
            } else {
                timestamp = 0;
            }
        }
        return timestamp;
    }

    private void addCurrent(boolean paused) {
        addCurrent(paused, getTimeStamp());
    }

    private void addCurrent(boolean paused, long timestamp) {
        synchronized (this.mRecordLock) {
            if (this.mCurrent != null) {
                this.mRecords.add(this.mCurrent);
                this.mRecordCount++;
                if (!this.mAverages.isEmpty()) {
                    checkCount(getTimeStamp());
                }
            }
        }
        if (paused) {
            internalSave();
        } else {
            saveIfRequired();
        }
        synchronized (this.mRecordLock) {
            this.mCurrent = !paused ? new ExtendableRecord(timestamp) : Record.createBreak(timestamp);
        }
    }

    private void checkCurrent() {
        synchronized (this.mRecordLock) {
            if (this.mCurrent == null) {
                this.mCurrent = new ExtendableRecord(getTimeStamp());
            }
        }
    }

    private void saveIfRequired() {
        boolean save;
        synchronized (this.mRecordLock) {
            save = this.mRecords.size() > 20;
        }
        if (save) {
            internalSave();
        }
    }

    private void internalSave() {
        LinkedList<Record> toSave;
        synchronized (this.mRecordLock) {
            toSave = this.mRecords;
            this.mRecords = new LinkedList<>();
        }
        if (this.mTimeHelper == null) {
            SQLHelper.addRecords(this.mRideId, toSave, this.mRouteId, true);
            for (Record r : toSave) {
                if (r instanceof ExtendableRecord) {
                    ((ExtendableRecord) r).saved();
                }
            }
            return;
        }
        SQLHelper.addRecords(this.mRideId, toSave, this.mRouteId);
    }

    public void start(long rideId, long routeId) {
        start(rideId, routeId, true);
    }

    public void start(long rideId, long routeId, boolean generateAverages) {
        this.mRideId = rideId;
        this.mRouteId = routeId;
        synchronized (this.mRecordLock) {
            this.mCurrent = null;
            this.mRecords.clear();
            this.mRecordCount = 0;
        }
        this.mAverages.clear();
        if (generateAverages) {
            this.mAverages.add(new AveragedRecord(5, 0L));
            this.mNextResolution = 13;
        }
    }

    public void resume() {
    }

    public void pause() {
        addCurrent(true);
    }

    public void end() {
        internalSave();
        this.mRideId = -1L;
        this.mRouteId = -1L;
    }

    private boolean isRecording() {
        return this.mRideId != -1;
    }

    public void newRecord(long timestamp, boolean isBreak) {
        addCurrent(isBreak, timestamp);
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onDistance(double distance) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsDistance(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setDistance(distance);
                for (AveragedRecord av : this.mAverages) {
                    av.onDistance(distance);
                }
            }
            super.onDistance(distance);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onSpeed(double speed) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsSpeed(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setSpeed(speed);
                for (AveragedRecord av : this.mAverages) {
                    av.onSpeed(speed);
                }
            }
            super.onSpeed(speed);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onStride(double stride) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsStride(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setStride(stride);
                for (AveragedRecord av : this.mAverages) {
                    av.onStride(stride);
                }
            }
            super.onStride(stride);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onCadence(double cadenceRPM) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsCadence(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setCadence(cadenceRPM);
                for (AveragedRecord av : this.mAverages) {
                    av.onCadence(cadenceRPM);
                }
            }
            super.onCadence(cadenceRPM);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onHeartRate(int heartrate) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsHeartrate(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setHeartrate(heartrate);
                for (AveragedRecord av : this.mAverages) {
                    av.onHeartRate(heartrate);
                }
            }
            super.onHeartRate(heartrate);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onBikePower(double power) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsPower(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setPower(power);
                for (AveragedRecord av : this.mAverages) {
                    av.onBikePower(power);
                }
            }
            super.onBikePower(power);
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onOxygen(int oxygen) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsOxygen(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setOxygen(oxygen);
                for (AveragedRecord av : this.mAverages) {
                    av.onOxygen(oxygen);
                }
            }
            super.onOxygen(oxygen);
        }
    }

    @Override // com.kopin.solos.storage.DataListener
    public void onLocation(Location location) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsLocation(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setLocation(new Coordinate(this.mRouteId, location));
            }
        }
    }

    public void onLocation(double lat, double lng, float alt) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsLocation(getTimeStamp())) {
                    addCurrent(false);
                }
                if (alt != -2.14748365E9f) {
                    super.onAltitude(alt);
                }
                this.mCurrent.setLocation(new Coordinate(this.mRouteId, lat, lng, alt));
            }
        }
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onAltitude(float value) {
        if (isRecording()) {
            checkCurrent();
            synchronized (this.mRecordLock) {
                if (!this.mCurrent.acceptsAltitude(getTimeStamp())) {
                    addCurrent(false);
                }
                this.mCurrent.setAltitude(value);
                for (AveragedRecord av : this.mAverages) {
                    av.onAltitude(value);
                }
            }
            super.onAltitude(value);
        }
    }

    private void checkCount(long now) {
        for (int i = 0; i < this.mAverages.size(); i++) {
            AveragedRecord av = this.mAverages.get(i);
            int res = av.getResolution();
            if (this.mRecordCount % res == 0) {
                Record avg = av.save(now, this);
                this.mRecords.add(avg);
                this.mAverages.set(i, new AveragedRecord(res, now));
            }
        }
        if (this.mRecordCount >= this.mNextResolution) {
            AveragedRecord av2 = this.mAverages.get(this.mAverages.size() - 1);
            int nextRes = av2.getResolution();
            Record avg2 = av2.save(now, this);
            this.mRecords.add(avg2);
            this.mAverages.set(this.mAverages.size() - 1, new AveragedRecord(this.mNextResolution, now));
            for (int i2 = this.mAverages.size() - 2; i2 >= 0; i2--) {
                AveragedRecord av3 = this.mAverages.get(i2);
                int lastRes = av3.getResolution();
                av3.setResolution(nextRes);
                nextRes = lastRes;
            }
            this.mAverages.add(0, new AveragedRecord(5, now));
            this.mNextResolution = AveragedRecord.NEXT_RESOLUTION(this.mAverages.get(this.mAverages.size() - 1).getResolution(), this.mAverages.get(this.mAverages.size() - 2).getResolution());
        }
    }

    @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
    public boolean isLastLap() {
        return false;
    }

    @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
    public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
        if (!isEnd) {
            internalSave();
        }
    }

    public void onPostCoordinateSaved(OnCoordinateSavedCallback cb) {
        synchronized (this.mRecordLock) {
            if (this.mCurrent instanceof ExtendableRecord) {
                ((ExtendableRecord) this.mCurrent).setOnCoordinateSaved(cb);
            }
        }
    }

    private static class ExtendableRecord extends Record {
        private OnCoordinateSavedCallback mCoordCb;
        private OnRecordSavedCallback mRecordCb;

        ExtendableRecord(long timestamp) {
            super(timestamp);
        }

        void setOnCoordinateSaved(OnCoordinateSavedCallback cb) {
            this.mCoordCb = cb;
        }

        void saved() {
            if (this.mCoordCb != null && hasLocation()) {
                this.mCoordCb.onPositionSaved(getCoordId(), this.location);
            }
            if (this.mRecordCb != null) {
                this.mRecordCb.onRecordSaved(this);
            }
        }
    }
}
