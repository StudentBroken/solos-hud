package com.kopin.solos.share.strava;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.strava.StravaRide;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.CorrectedElevation;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.RideRecorder;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Trackable;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.CalorieCounter;
import com.kopin.solos.storage.util.NormalisedPower;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.jstrava.connector.JStravaV3;
import org.jstrava.entities.stream.Stream;

/* JADX INFO: loaded from: classes4.dex */
public class StravaSync {
    private static final String TAG = "StravaSync";
    private static Context mContext;
    private static String mEmail;
    private static JStravaV3 mjStravaV3;
    private static HashSet<DownloadListener> mDownloadListeners = new HashSet<>();
    private static boolean downloading = false;

    public interface DownloadListener {
        void onFailure();

        void onSuccess();
    }

    public static void init(Context context) {
        mContext = context;
    }

    public static void registerStravaListener(DownloadListener downloadListener) {
        mDownloadListeners.add(downloadListener);
    }

    public static void unRegisterStravaListener(DownloadListener downloadListener) {
        mDownloadListeners.remove(downloadListener);
    }

    public static void setStravaApi(JStravaV3 jStravaV3) {
        mjStravaV3 = jStravaV3;
    }

    public static void setEmail(String email) {
        mEmail = email;
    }

    public static boolean isDownloading() {
        return downloading;
    }

    public static boolean downloadStrava(StravaRide ride) {
        if (downloading) {
            return false;
        }
        downloading = true;
        new ImportRideTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ride);
        return true;
    }

    private static class ImportRideTask extends AsyncTask<StravaRide, Void, Boolean> {
        private ImportRideTask() {
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(StravaRide... rides) {
            boolean unused = StravaSync.downloading = true;
            boolean completed = false;
            if (rides[0] != null) {
                StravaRide stravaRide = rides[0];
                Shared.ShareType sport = Shared.ShareType.fromSportType(LiveRide.getCurrentSport());
                long rideId = 0;
                switch (sport) {
                    case RIDE:
                        rideId = SQLHelper.addRide(-1L, Workout.RideMode.NORMAL, -1L);
                        break;
                    case RUN:
                        rideId = SQLHelper.addRun(-1L, Workout.RideMode.NORMAL, -1L);
                        break;
                }
                Platforms.Strava.addShare(Shared.newImport(rideId, Platforms.Strava.getSharedKey(), StravaSync.mEmail, String.valueOf(stravaRide.getId()), sport));
                try {
                    List<Stream> streams = StravaSync.mjStravaV3.findActivityStreams(stravaRide.getId(), new String[]{"time", "latlng", BaseDataTypes.ID_DISTANCE, "altitude", "velocity_smooth", "heartrate", "cadence", "watts", "grade_smooth"});
                    if (streams != null && streams.size() > 0) {
                        stravaRide.setData(streams);
                        long routeId = stravaRide.hasLocations() ? SQLHelper.addRoute() : -1L;
                        switch (Prefs.getSportType()) {
                            case RIDE:
                                SQLHelper.updateRideRoute(rideId, routeId);
                                break;
                            case RUN:
                                SQLHelper.updateRunRoute(rideId, routeId);
                                break;
                        }
                        stravaRide.setSolosId(rideId);
                        Lap lap = new Lap(rideId, SplitHelper.SplitType.TIME, new TimeHelper());
                        Lap.Saved lapSaved = new Lap.Saved(lap, 0L, 1000 * stravaRide.getDuration(), 0.0d, 0L, 0, 0.0f);
                        SQLHelper.updateLap(lapSaved);
                        StravaSync.processStravaRecords(stravaRide, routeId);
                        if (stravaRide.getCalories() == 0 || stravaRide.getCalories() == Integer.MIN_VALUE) {
                            stravaRide.setCalories(CalorieCounter.calculate(UserProfile.getWeightKG(), 0.0d, stravaRide.getDuration(), stravaRide.getAverageSpeed(), stravaRide.getGainedAltitude()));
                        }
                        switch (sport) {
                            case RIDE:
                                stravaRide.saveRide();
                                SQLHelper.setRideFinished(rideId, true);
                                break;
                            case RUN:
                                stravaRide.saveRun();
                                SQLHelper.setRunFinished(rideId, true);
                                break;
                        }
                        boolean unused2 = StravaSync.downloading = false;
                        completed = true;
                        for (DownloadListener listener : StravaSync.mDownloadListeners) {
                            listener.onSuccess();
                        }
                        return true;
                    }
                } catch (Exception e) {
                    boolean unused3 = StravaSync.downloading = false;
                    if (StravaSync.mContext != null) {
                        StravaHelper.logoutIfAccessDenied(StravaSync.mContext, e);
                    }
                    e.printStackTrace();
                }
                if (!completed) {
                    Platforms.Strava.removeShare(String.valueOf(stravaRide.getId()), sport);
                    switch (sport) {
                        case RIDE:
                            SQLHelper.removeRide(rideId);
                            break;
                        case RUN:
                            SQLHelper.removeRun(rideId);
                            break;
                    }
                }
            }
            boolean unused4 = StravaSync.downloading = false;
            if (!completed) {
                for (DownloadListener listener2 : StravaSync.mDownloadListeners) {
                    listener2.onFailure();
                }
            }
            return Boolean.valueOf(completed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processStravaRecords(StravaRide stravaRide, final long routeId) {
        long localId = stravaRide.getSolosId();
        final StravaTrackable metrics = new StravaTrackable();
        final RideRecorder recorder = new RideRecorder(null);
        final List<CorrectedElevation> elevations = new ArrayList<>();
        recorder.start(localId, routeId, true);
        stravaRide.foreachRecord(new StravaRide.StravaRecordCallback() { // from class: com.kopin.solos.share.strava.StravaSync.1
            long lastTimeStamp = 0;

            @Override // com.kopin.solos.share.strava.StravaRide.StravaRecordCallback
            public boolean onRecord(long time, double distance, int hr, double speed, double cadence, double power, final float elevation, Location loc) {
                recorder.newRecord(time, false);
                if (routeId != -1 && loc != null) {
                    recorder.onLocation(loc.getLatitude(), loc.getLongitude(), elevation);
                }
                boolean hasDistance = false;
                if (distance != -2.147483648E9d) {
                    hasDistance = true;
                    recorder.onDistance(distance);
                }
                if (speed != -2.147483648E9d) {
                    recorder.onSpeed(speed);
                    metrics.onSpeed(speed);
                }
                if (hr != Integer.MIN_VALUE) {
                    recorder.onHeartRate(hr);
                    metrics.onHeartRate(hr);
                }
                if (cadence != -2.147483648E9d) {
                    recorder.onCadence(cadence);
                    metrics.onCadence(cadence);
                }
                if (power != -2.147483648E9d) {
                    recorder.onBikePower(power);
                    metrics.onBikePower(power);
                    metrics.onNormalisePower(power, time);
                }
                if (loc != null && elevation != -2.14748365E9f) {
                    recorder.onPostCoordinateSaved(new RideRecorder.OnCoordinateSavedCallback() { // from class: com.kopin.solos.share.strava.StravaSync.1.1
                        @Override // com.kopin.solos.storage.RideRecorder.OnCoordinateSavedCallback
                        public void onPositionSaved(long coordId, Coordinate coord) {
                            CorrectedElevation elev = new CorrectedElevation(coord.getTimestamp(), coordId, elevation);
                            elevations.add(elev);
                            metrics.onAltitude(elevation);
                        }
                    });
                }
                if (!hasDistance) {
                    recorder.onDistance(distance + (((time - this.lastTimeStamp) * speed) / 1000.0d));
                    this.lastTimeStamp = time;
                    return true;
                }
                return true;
            }
        });
        recorder.end();
        SQLHelper.addElevation(elevations, null);
        metrics.saveStats(stravaRide);
    }

    private static class StravaTrackable extends Trackable {
        private NormalisedPower mPowerNormaliser = new NormalisedPower();

        StravaTrackable() {
        }

        void onNormalisePower(double power, long time) {
            double np = this.mPowerNormaliser.calculateNormalisedPower(power, time);
            if (np > 0.0d) {
                this.mNormPower.addValue(Double.valueOf(np));
            }
        }

        void saveStats(StravaRide toRide) {
            toRide.setStats(getSpeedStats(), getCadenceStats(), getHeartRateStats(), getPowerStats(), getNormalisedPowerStats(), getElevationRange(), getOverallClimb());
        }
    }
}
