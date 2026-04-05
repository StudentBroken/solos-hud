package com.kopin.solos.share.peloton;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import com.kopin.peloton.Cloud;
import com.kopin.peloton.Failure;
import com.kopin.peloton.Gear;
import com.kopin.peloton.PHR;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.peloton.PelotonResponse;
import com.kopin.peloton.Profile;
import com.kopin.peloton.RFTP;
import com.kopin.peloton.Ride;
import com.kopin.peloton.Run;
import com.kopin.peloton.UserData;
import com.kopin.peloton.ride.Activity;
import com.kopin.peloton.ride.Metric;
import com.kopin.peloton.ride.MetricStat;
import com.kopin.peloton.ride.RideHeader;
import com.kopin.peloton.ride.RideRecord;
import com.kopin.peloton.ride.WayPoint;
import com.kopin.peloton.training.TrainingWorkout;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Sync;
import com.kopin.solos.share.SyncManager;
import com.kopin.solos.share.peloton.PelotonConversion;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.CorrectedElevation;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.RideRecorder;
import com.kopin.solos.storage.Rider;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.ICancellable;
import com.kopin.solos.util.RideActivity;
import com.kopin.solos.util.RunType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes4.dex */
public class PelotonHelper {
    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MIN_TOKEN_LENGTH = 5;
    private static final String TAG = "PelotonHelper";
    private static AddItemsTask addItemsTask;
    private static Sync.UIListener mProfileCompleteUIListener;
    private static Sync.SyncUpdateListener mSyncUpdateListener;
    private static Gear pelotonGear;
    private static ProcessDownloadedRideDataTask processDownloadedRideDataTask;
    private static ProcessDownloadedRides processDownloadedRides;
    private static ProcessDownloadedRunDataTask processDownloadedRunDataTask;
    private static SyncItemsTask syncItemsTask;
    private static boolean isUploading = false;
    private static int pendingRides = 0;
    private static int numUnsyncedBikes = 0;
    private static int numUnsyncedRoutes = 0;
    private static int numUnsyncedTraining = 0;
    private static int numDownloadingRides = 0;
    private static int numDownloadedRides = 0;
    private static Map<String, Long> idMap = new HashMap();
    private static Sync.SyncListener addRideResponse = new Sync.SyncListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.10
        @Override // com.kopin.solos.share.Sync.SyncListener
        public void onSuccess() {
            Sync.uploadAll();
        }

        @Override // com.kopin.solos.share.Sync.SyncListener
        public void onFailure() {
            Sync.retryUploads();
        }
    };
    private static Sync.SyncListener uploadRideListener = new Sync.SyncListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.18
        @Override // com.kopin.solos.share.Sync.SyncListener
        public void onSuccess() {
            PelotonHelper.uploadRideResponse(true);
        }

        @Override // com.kopin.solos.share.Sync.SyncListener
        public void onFailure() {
            PelotonHelper.uploadRideResponse(false);
        }
    };

    public enum PelotonItem {
        Workout,
        Route,
        Bike,
        FTP,
        Training
    }

    static /* synthetic */ int access$1604() {
        int i = numDownloadedRides + 1;
        numDownloadedRides = i;
        return i;
    }

    public static void setSyncUpdateListener(Sync.SyncUpdateListener syncUpdateListener) {
        mSyncUpdateListener = syncUpdateListener;
    }

    public static void setProfileUIListener(Sync.UIListener completeListener) {
        mProfileCompleteUIListener = completeListener;
    }

    public static int getNumDownloadingRides() {
        return numDownloadingRides;
    }

    public static int getNumDownloadedRides() {
        return numDownloadedRides;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetRideCount() {
        numDownloadedRides = 0;
        numDownloadingRides = 0;
        if (mSyncUpdateListener != null) {
            mSyncUpdateListener.onComplete();
        }
    }

    public static boolean isLoggedIn() {
        String token = PelotonPrefs.getToken();
        String email = PelotonPrefs.getEmail();
        return Config.SYNC_PROVIDER == Platforms.Peloton && token != null && token.length() >= 5 && email != null && email.length() >= 5;
    }

    public static Ride getPelotonRide(SavedRide solosRide, String username) {
        Log.i(TAG, "getPelotonRide");
        String bikeId = SQLHelper.getExternalId(solosRide.getBikeId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.BIKE);
        Ride pelotonRide = new Ride("", solosRide.getTitle(), solosRide.getComment(), solosRide.getActualStartTime(), solosRide.getEndTime() - solosRide.getStartTime(), solosRide.getDuration(), solosRide.getDistance());
        pelotonRide.BikeId = bikeId;
        pelotonRide.RideType = RideActivity.RideType.getRideType(solosRide.getActivity()).name().toUpperCase(Locale.US);
        if (solosRide.getWorkoutMode() == Workout.RideMode.TRAINING) {
            pelotonRide.TrainingId = SQLHelper.getExternalId(solosRide.getVirtualWorkoutId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.TRAINING);
        }
        if (solosRide != null) {
            String ghost = getPelotonGhostId(solosRide.getGhostRideId(), SportType.RIDE);
            if (ghost != null && !ghost.isEmpty()) {
                pelotonRide.GhostRideId = ghost;
                Log.i(TAG, "getPelotonRide ghost server " + solosRide.getGhostRideId() + " : " + ghost);
            } else {
                Log.i(TAG, "Peloton ride from Solos: could not get shared ghost id for " + solosRide.getGhostRideId());
            }
            pelotonRide.FunctionalThresholdPower = solosRide.getFunctionalThresholdPower();
            pelotonRide.OverallStats.Calories = solosRide.getCalories() == Integer.MIN_VALUE ? 0.0d : solosRide.getCalories();
            if (PelotonConversion.isValid(Float.valueOf(solosRide.getGainedAltitude()))) {
                pelotonRide.OverallClimb = Double.valueOf(solosRide.getGainedAltitude());
            }
            if (PelotonConversion.isValid(Float.valueOf(solosRide.getMaxAltitudeDiff()))) {
                pelotonRide.AltitudeRange = Double.valueOf(solosRide.getMaxAltitudeDiff());
            }
            pelotonRide.OverallStats.Cadence = PelotonConversion.makeMetric(solosRide.getMaxCadence(), solosRide.getAverageCadence());
            pelotonRide.OverallStats.HeartRate = PelotonConversion.makeMetric(solosRide.getMaxHeartrate(), solosRide.getAverageHeartrate());
            pelotonRide.OverallStats.Oxygenation = PelotonConversion.makeMetric(100, solosRide.getAverageOxygen(), solosRide.getMinOxygen());
            pelotonRide.OverallStats.Power = PelotonConversion.makeMetric(solosRide.getMaxPower(), solosRide.getAveragePower());
            pelotonRide.OverallStats.Speed = PelotonConversion.makeMetric(solosRide.getMaxSpeed(), solosRide.getAverageSpeed());
            pelotonRide.Targets.add(new Metric(RideHeader.TargetType.TargetAverageCadence, solosRide.getTargetAverageCadence()));
            pelotonRide.Targets.add(new Metric(RideHeader.TargetType.TargetAverageHeartrate, solosRide.getTargetAverageHeartrate()));
            pelotonRide.Targets.add(new Metric(RideHeader.TargetType.TargetAveragePower, solosRide.getTargetAveragePower()));
            pelotonRide.Targets.add(new Metric(RideHeader.TargetType.TargetAverageSpeed, solosRide.getTargetAverageSpeedKm()));
            pelotonRide.OverallStats.NormalisedPower = PelotonConversion.makeMetric(solosRide.getMaxNormalisedPower(), solosRide.getAverageNormalisedPower());
            pelotonRide.OverallStats.IntensityFactor = PelotonConversion.makeMetric(solosRide.getMaxIntensity(), solosRide.getAverageIntensity());
        }
        Log.i(TAG, "solos ride ___ " + solosRide.getStartTime() + " : " + solosRide.getEndTime());
        Log.i(TAG, "peloton ride _ " + pelotonRide.StartTime + " : " + pelotonRide.EndTime);
        return pelotonRide;
    }

    public static Run getPelotonRun(SavedRun solosRun, String username) {
        Log.i(TAG, "getPelotonRun");
        Run pelotonRun = new Run("", solosRun.getTitle(), solosRun.getComment(), solosRun.getActualStartTime(), solosRun.getEndTime() - solosRun.getStartTime(), solosRun.getDuration(), solosRun.getDistance());
        if (solosRun.getWorkoutMode() == Workout.RideMode.TRAINING) {
            pelotonRun.TrainingId = SQLHelper.getExternalId(solosRun.getVirtualWorkoutId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.TRAINING);
        }
        if (pelotonGear != null && !pelotonGear.GearId.isEmpty()) {
            pelotonRun.GearId = pelotonGear.GearId;
        }
        pelotonRun.RunType = RunType.getRunType(solosRun.getActivity()).name().toUpperCase(Locale.US);
        if (solosRun != null) {
            String ghost = getPelotonGhostId(solosRun.getGhostRideId(), SportType.RUN);
            if (ghost != null && !ghost.isEmpty()) {
                pelotonRun.GhostRunId = ghost;
                Log.i(TAG, "getPelotonRun ghost server " + solosRun.getGhostRideId() + " : " + ghost);
            } else {
                Log.i(TAG, "Peloton run from Solos: could not get shared ghost id for " + solosRun.getGhostRideId());
            }
            pelotonRun.FunctionalThresholdPower = solosRun.getFunctionalThresholdPower();
            pelotonRun.OverallStats.Calories = solosRun.getCalories() == Integer.MIN_VALUE ? 0.0d : solosRun.getCalories();
            if (PelotonConversion.isValid(Float.valueOf(solosRun.getGainedAltitude()))) {
                pelotonRun.OverallClimb = Double.valueOf(solosRun.getGainedAltitude());
            }
            if (PelotonConversion.isValid(Float.valueOf(solosRun.getMaxAltitudeDiff()))) {
                pelotonRun.AltitudeRange = Double.valueOf(solosRun.getMaxAltitudeDiff());
            }
            pelotonRun.OverallStats.Cadence = PelotonConversion.makeMetric(solosRun.getMaxCadence(), solosRun.getAverageCadence());
            pelotonRun.OverallStats.HeartRate = PelotonConversion.makeMetric(solosRun.getMaxHeartrate(), solosRun.getAverageHeartrate());
            pelotonRun.OverallStats.Oxygenation = PelotonConversion.makeMetric(100, solosRun.getAverageOxygen(), solosRun.getMinOxygen());
            pelotonRun.OverallStats.Power = PelotonConversion.makeMetric(solosRun.getMaxPower(), solosRun.getAveragePower());
            pelotonRun.OverallStats.Speed = PelotonConversion.makeMetric(solosRun.getMaxSpeed(), solosRun.getAverageSpeed());
            pelotonRun.OverallStats.Pace = PelotonConversion.makeMetric(solosRun.getMaxPace(), solosRun.getAveragePace());
            pelotonRun.OverallStats.Stride = PelotonConversion.makeMetric(solosRun.getMaxStride(), solosRun.getAverageStride());
            pelotonRun.Targets.add(new Metric(RideHeader.TargetType.TargetAverageCadence, solosRun.getTargetAverageCadence()));
            pelotonRun.Targets.add(new Metric(RideHeader.TargetType.TargetAverageHeartrate, solosRun.getTargetAverageHeartrate()));
            pelotonRun.Targets.add(new Metric(RideHeader.TargetType.TargetAveragePower, solosRun.getTargetAveragePower()));
            pelotonRun.Targets.add(new Metric(RideHeader.TargetType.TargetAverageSpeed, solosRun.getTargetAverageSpeedKm()));
            pelotonRun.Targets.add(new Metric(RideHeader.TargetType.TargetAveragePace, solosRun.getTargetAveragePaceMinutesPerKm()));
            pelotonRun.OverallStats.NormalisedPower = PelotonConversion.makeMetric(solosRun.getMaxNormalisedPower(), solosRun.getAverageNormalisedPower());
            pelotonRun.OverallStats.IntensityFactor = PelotonConversion.makeMetric(solosRun.getMaxIntensity(), solosRun.getAverageIntensity());
        }
        Log.i(TAG, "solos run ___ " + solosRun.getStartTime() + " : " + solosRun.getEndTime());
        Log.i(TAG, "peloton run _ " + pelotonRun.StartTime + " : " + pelotonRun.EndTime);
        return pelotonRun;
    }

    private static String getPelotonGhostId(long ghostId, SportType sportType) {
        SavedWorkout refRideRun;
        if (ghostId > 0 && (refRideRun = SavedRides.getWorkout(sportType, ghostId, true)) != null && !Platforms.Strava.wasImported(refRideRun)) {
            String ghost = Platforms.Peloton.getLocalIdFor(ghostId);
            if (ghost == null || ghost.isEmpty()) {
                Log.w(TAG, "Peloton ride/run from Solos: could not get shared ghost id for " + ghostId);
            } else {
                return ghost;
            }
        }
        return null;
    }

    public static Ride getPelotonRideData(SavedRide solosRide, String username) {
        final Ride pelotonRide = getPelotonRide(solosRide, username);
        SQLHelper.foreachLap(solosRide.getId(), true, new SavedWorkout.foreachLapCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.1
            @Override // com.kopin.solos.storage.SavedWorkout.foreachLapCallback
            public boolean onLap(Lap.Saved lap) {
                pelotonRide.Laps.add(PelotonConversion.getPelotonLap(lap));
                return true;
            }
        });
        pelotonRide.LapCount = pelotonRide.Laps.size() > 1 ? pelotonRide.Laps.size() : 1L;
        solosRide.foreachExtendedRecord(PelotonConversion.PelotonMetric.getConversionMap(), new SavedWorkout.ForeachExtendedRecordCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.2
            @Override // com.kopin.solos.storage.SavedWorkout.ForeachExtendedRecordCallback
            public boolean onRecordValues(long timestamp, HashMap<String, Number> values, Coordinate position) {
                pelotonRide.Records.add(PelotonConversion.getPelotonRecord(timestamp, values, position));
                return true;
            }
        });
        return pelotonRide;
    }

    public static Run getPelotonRunData(SavedRun solosRun, String username) {
        final Run pelotonRun = getPelotonRun(solosRun, username);
        SQLHelper.foreachLap(solosRun.getId(), true, new SavedWorkout.foreachLapCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.3
            @Override // com.kopin.solos.storage.SavedWorkout.foreachLapCallback
            public boolean onLap(Lap.Saved lap) {
                pelotonRun.Laps.add(PelotonConversion.getPelotonLap(lap));
                return true;
            }
        });
        pelotonRun.LapCount = pelotonRun.Laps.size() > 1 ? pelotonRun.Laps.size() : 1L;
        solosRun.foreachExtendedRecord(PelotonConversion.PelotonMetric.getConversionMap(), new SavedWorkout.ForeachExtendedRecordCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.4
            @Override // com.kopin.solos.storage.SavedWorkout.ForeachExtendedRecordCallback
            public boolean onRecordValues(long timestamp, HashMap<String, Number> values, Coordinate position) {
                pelotonRun.Records.add(PelotonConversion.getPelotonRecord(timestamp, values, position));
                return true;
            }
        });
        return pelotonRun;
    }

    private static void uploadBike(final Bike solosBike, final Sync.SyncListener syncListener) {
        if (!solosBike.isActive() || !Sync.isEnabled()) {
            if (syncListener != null) {
                syncListener.onSuccess();
            }
        } else {
            final com.kopin.peloton.Bike bike = new com.kopin.peloton.Bike(solosBike.toContentValues(), Bike.Field.NAME.name(), Bike.Field.WHEEL_SIZE.name(), Bike.Field.WEIGHT.name());
            Log.i(TAG, "Uploading local Bike " + solosBike.getName() + ", " + bike.Name);
            if (SyncManager.isUnSynced(solosBike)) {
                SyncManager.startSyncing(solosBike);
                Peloton.addBike(bike, new Peloton.BikeListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.5
                    @Override // com.kopin.peloton.Peloton.BikeListener
                    public void onBike(com.kopin.peloton.Bike bike2) {
                        Log.i(PelotonHelper.TAG, "addedbike to peloton " + solosBike.getId() + ", " + bike2);
                        SyncManager.setSynced(solosBike, bike2);
                        Log.i(PelotonHelper.TAG, "Added Bike to share table " + bike2.Name);
                        if (syncListener != null) {
                            syncListener.onSuccess();
                        }
                    }

                    @Override // com.kopin.peloton.Peloton.CloudListener
                    public void onFailure(Failure failure, int i, String s) {
                        SyncManager.setSynced(solosBike, null);
                        Log.w(PelotonHelper.TAG, "Failed Added Bike to share table " + bike.Name);
                        if (syncListener != null) {
                            syncListener.onFailure();
                        }
                    }
                });
            }
        }
    }

    private static void uploadRoute(final Route.Saved solosRoute, final Sync.SyncListener syncListener) {
        final com.kopin.peloton.ride.Route route = new com.kopin.peloton.ride.Route(solosRoute.getRouteName(), solosRoute.getTimeToBeat(), solosRoute.getDistance());
        SQLHelper.foreachCoord(solosRoute.getId(), new SQLHelper.foreachCoordCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.6
            @Override // com.kopin.solos.storage.SQLHelper.foreachCoordCallback
            public boolean onCoordinate(Coordinate coord) {
                route.addWayPoint(new WayPoint(coord.getLatitude(), coord.getLongitude(), coord.getAltitude()));
                return false;
            }
        });
        Log.i(TAG, "Uploading local route " + solosRoute.getId() + ", " + solosRoute.getRouteName());
        if (SyncManager.isUnSynced(solosRoute) && Sync.isEnabled()) {
            SyncManager.startSyncing(solosRoute);
            Peloton.addRoute(route, new Peloton.RouteListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.7
                @Override // com.kopin.peloton.Peloton.RouteListener
                public void onRoute(com.kopin.peloton.ride.Route pelotonRoute) {
                    SyncManager.setSynced(solosRoute, pelotonRoute);
                    Log.i(PelotonHelper.TAG, "Added route to share table " + solosRoute.getRouteName());
                    if (syncListener != null) {
                        syncListener.onSuccess();
                    }
                }

                @Override // com.kopin.peloton.Peloton.CloudListener
                public void onFailure(Failure failure, int i, String s) {
                    Log.w(PelotonHelper.TAG, "Failed Added route to share table " + route.Name);
                    SyncManager.setSynced(solosRoute, null);
                    if (syncListener != null) {
                        syncListener.onFailure();
                    }
                }
            });
        }
    }

    private static void uploadTraining(final SavedTraining savedTraining, final Sync.SyncListener syncListener) {
        final TrainingWorkout pelotonTraining = TrainingConversion.savedToPelotonTraining(savedTraining);
        Log.i(TAG, "Uploading local training " + savedTraining.getId() + ", " + savedTraining.getTitle());
        if (SyncManager.isUnSynced(savedTraining)) {
            SyncManager.startSyncing(savedTraining);
            Peloton.addTrainingAsync(pelotonTraining, new Peloton.AddTrainingListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.8
                @Override // com.kopin.peloton.Peloton.AddTrainingListener
                public void onTrainingAdded(TrainingWorkout trainingWorkout) {
                    SyncManager.setSynced(savedTraining, pelotonTraining);
                    Log.i(PelotonHelper.TAG, "Added training to share table " + savedTraining.getTitle());
                    if (syncListener != null) {
                        syncListener.onSuccess();
                    }
                }

                @Override // com.kopin.peloton.Peloton.CloudListener
                public void onFailure(Failure failure, int i, String s) {
                    Log.w(PelotonHelper.TAG, "Failed: Add Training to share table " + pelotonTraining.Name + " " + s);
                    SyncManager.setSynced(savedTraining, null);
                    if (syncListener != null) {
                        syncListener.onFailure();
                    }
                }
            });
        } else {
            Log.i(TAG, "Already shared Training" + savedTraining.getId() + ", " + savedTraining.getTitle());
        }
    }

    public static void removeTraining(SavedTraining savedTraining, Peloton.RemoveListener removeListener) {
        removeTraining(savedTraining.getId(), removeListener);
    }

    public static void removeTraining(long localId, Peloton.RemoveListener removeListener) {
        String username = PelotonPrefs.getEmail();
        String serverId = SQLHelper.getExternalId(localId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.TRAINING);
        Peloton.removeTrainingAsync(serverId, removeListener);
    }

    public static void removeBike(Bike solosBike, Peloton.RemoveListener removeListener) {
        removeBike(solosBike.getId(), removeListener);
    }

    public static void removeBike(long localId, Peloton.RemoveListener removeListener) {
        String username = PelotonPrefs.getEmail();
        String serverId = SQLHelper.getExternalId(localId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.BIKE);
        Peloton.removeBike(serverId, removeListener);
    }

    public static void removeGear(com.kopin.solos.storage.Gear solosGear, Peloton.RemoveListener removeListener) {
        removeGear(solosGear.getId(), removeListener);
    }

    public static void removeGear(long localId, Peloton.RemoveListener removeListener) {
        String username = PelotonPrefs.getEmail();
        String serverId = SQLHelper.getExternalId(localId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.GEAR);
        Peloton.removeGear(serverId, removeListener);
    }

    public static void addRoute(final Route.Saved solosRoute, List<Coordinate> coords) {
        com.kopin.peloton.ride.Route pelotonRoute = new com.kopin.peloton.ride.Route(solosRoute.getRouteName(), solosRoute.getTimeToBeat(), solosRoute.getDistance());
        for (Coordinate coordinate : coords) {
            WayPoint wp = new WayPoint(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getAltitude());
            wp.Timestamp = coordinate.getTimestamp();
            pelotonRoute.WayPoints.add(wp);
        }
        if (SyncManager.isUnSynced(solosRoute) && Sync.isEnabled()) {
            SyncManager.startSyncing(solosRoute);
            Peloton.addRoute(pelotonRoute, new Peloton.RouteListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.9
                @Override // com.kopin.peloton.Peloton.RouteListener
                public void onRoute(com.kopin.peloton.ride.Route pelotonRoute2) {
                    Log.i(PelotonHelper.TAG, "Added route to server (and share) " + pelotonRoute2.Name + ", server id = " + pelotonRoute2.RouteId);
                    SyncManager.setSynced(solosRoute, pelotonRoute2);
                }

                @Override // com.kopin.peloton.Peloton.CloudListener
                public void onFailure(Failure failure, int i, String s) {
                    Log.w(PelotonHelper.TAG, "Added routes to server fail " + i + ", " + s);
                    SyncManager.setSynced(solosRoute, null);
                    Sync.retryUploads();
                }
            });
        }
    }

    public static void removeCloudWorkout(long rideId, SportType sportType, Peloton.RemoveListener removeListener) {
        String username = PelotonPrefs.getEmail();
        switch (sportType) {
            case RIDE:
                String id = SQLHelper.getExternalId(rideId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RIDE);
                if (id != null) {
                    Peloton.removeRide(id, removeListener);
                }
                break;
            case RUN:
                String id2 = SQLHelper.getExternalId(rideId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RUN);
                if (id2 != null) {
                    Peloton.removeRun(id2, removeListener);
                }
                break;
        }
    }

    private static void initDefaultGear() {
        if (pelotonGear == null) {
            com.kopin.solos.storage.Gear solosGear = com.kopin.solos.storage.Gear.getDefaultGear();
            pelotonGear = new Gear();
            pelotonGear.Name = solosGear.getName();
            pelotonGear.Lifespan = (int) solosGear.getLifeSpan();
            pelotonGear.TimeStamp = solosGear.getTimeStamp();
            pelotonGear.DateNew = solosGear.getTimeStamp();
        }
    }

    public static synchronized void uploadRideWorkFlat(final SavedRide localRide, ICancellable cancellable, final Sync.SyncListener syncListener) {
        String pelotonGhostId;
        String username = PelotonPrefs.getEmail();
        String bikeId = SQLHelper.getExternalId(localRide.getBikeId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.BIKE);
        Log.i(TAG, "bike id is " + localRide.getId() + ", " + bikeId);
        final RideData rideData = new RideData(localRide);
        Shared shareRideHeader = SQLHelper.getShare(localRide.getId(), Platforms.Peloton.getSharedKey(), Shared.ShareType.RIDE);
        final Shared shareRideData = SQLHelper.getShare(localRide.getId(), Platforms.Peloton.getSharedKey(), Shared.ShareType.RIDE_DATA);
        Log.i(TAG, "upload ride, bike " + bikeId);
        if (bikeId != null && !bikeId.isEmpty() && ((cancellable == null || cancellable.isActive()) && ((shareRideHeader == null || shareRideHeader.isUnsynced() || shareRideData == null || shareRideData.isUnsynced()) && Sync.isEnabled()))) {
            final Ride pelotonRide = getPelotonRideData(localRide, username);
            if (localRide.getGhostRideId() > 0 && (pelotonGhostId = SQLHelper.getExternalId(localRide.getGhostRideId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RIDE)) != null && !pelotonGhostId.isEmpty()) {
                pelotonRide.GhostRideId = pelotonGhostId;
            }
            if (cancellable == null || cancellable.isActive()) {
                System.gc();
                String pelotonRideId = null;
                if (shareRideHeader != null) {
                    pelotonRideId = shareRideHeader.getImportedFromId();
                }
                if (pelotonRideId == null || pelotonRideId.isEmpty()) {
                    if (shareRideHeader == null) {
                        SyncManager.startSyncing(localRide);
                    }
                    PelotonResponse response = Cloud.addRideHeaderWork(pelotonRide);
                    if (response != null && response.isServerSuccess() && response.result != null) {
                        Ride ride = (Ride) response.result;
                        SyncManager.setSynced(localRide, ride);
                        SyncManager.startSyncing(rideData);
                        Log.i(TAG, "Ride Header was uploaded " + pelotonRide.Name + ", " + ride.RideId);
                        pelotonRide.RideId = ride.RideId;
                        addRideData(rideData, pelotonRide, syncListener);
                    } else {
                        SyncManager.setSynced(localRide, null);
                    }
                } else if (shareRideHeader.isUnsynced()) {
                    Peloton.updateRide(pelotonRide, new Peloton.AddRideListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.11
                        @Override // com.kopin.peloton.Peloton.AddRideListener
                        public void onRideAdded(Ride ride2) {
                            SyncManager.setSynced(localRide, ride2);
                            if (shareRideData == null) {
                                SyncManager.startSyncing(rideData);
                            }
                            Log.i(PelotonHelper.TAG, "Ride Header was uploaded " + pelotonRide.Name + ", " + ride2.RideId);
                            pelotonRide.RideId = ride2.RideId;
                            PelotonHelper.addRideData(rideData, pelotonRide, syncListener);
                        }

                        @Override // com.kopin.peloton.Peloton.CloudListener
                        public void onFailure(Failure failure, int i, String s) {
                            SyncManager.setSynced(localRide, null);
                            Log.w(PelotonHelper.TAG, "Ride could not be uploaded " + pelotonRide.Name);
                        }
                    });
                } else {
                    if (shareRideData == null) {
                        SyncManager.startSyncing(rideData);
                    }
                    pelotonRide.RideId = pelotonRideId;
                    addRideData(rideData, pelotonRide, syncListener);
                }
            }
        } else {
            Log.w(TAG, "uploadRideWorkFlat ride not shared as bike was not synced (or ride is syncing)  " + localRide.getId() + ", " + localRide.getTitle());
        }
    }

    public static void uploadRunWorkFlat(final SavedRun localRun, ICancellable cancellable, final Sync.SyncListener syncListener) {
        String pelotonGhostId;
        String username = PelotonPrefs.getEmail();
        final RunData runData = new RunData(localRun);
        Shared shareRunHeader = SQLHelper.getShare(localRun.getId(), Platforms.Peloton.getSharedKey(), Shared.ShareType.RUN);
        final Shared shareRunData = SQLHelper.getShare(localRun.getId(), Platforms.Peloton.getSharedKey(), Shared.ShareType.RUN_DATA);
        initDefaultGear();
        if ((cancellable == null || cancellable.isActive()) && ((shareRunHeader == null || shareRunHeader.isUnsynced() || shareRunData == null || shareRunData.isUnsynced()) && Sync.isEnabled())) {
            final Run pelotonRun = getPelotonRunData(localRun, username);
            if (localRun.getGhostRideId() > 0 && (pelotonGhostId = SQLHelper.getExternalId(localRun.getGhostRideId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RUN)) != null && !pelotonGhostId.isEmpty()) {
                pelotonRun.GhostRunId = pelotonGhostId;
            }
            if (cancellable == null || cancellable.isActive()) {
                System.gc();
                String pelotonRunId = null;
                if (shareRunHeader != null) {
                    pelotonRunId = shareRunHeader.getImportedFromId();
                }
                if (pelotonRunId == null || pelotonRunId.isEmpty()) {
                    if (shareRunHeader == null) {
                        SyncManager.startSyncing(localRun);
                    }
                    PelotonResponse response = Cloud.addRunHeaderWork(pelotonRun);
                    if (response != null && response.isServerSuccess() && response.result != null) {
                        Run run = (Run) response.result;
                        SyncManager.setSynced(localRun, run);
                        SyncManager.startSyncing(runData);
                        Log.i(TAG, "Run Header was uploaded " + pelotonRun.Name + ", " + run.RunId);
                        pelotonRun.RunId = run.RunId;
                        addRunData(runData, pelotonRun, syncListener);
                        return;
                    }
                    SyncManager.setSynced(localRun, null);
                    return;
                }
                if (shareRunHeader.isUnsynced()) {
                    Peloton.updateRun(pelotonRun, pelotonGear, new Peloton.AddRunListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.12
                        @Override // com.kopin.peloton.Peloton.AddRunListener
                        public void onRunAdded(Run run2) {
                            SyncManager.setSynced(localRun, run2);
                            if (shareRunData == null) {
                                SyncManager.startSyncing(runData);
                            }
                            Log.i(PelotonHelper.TAG, "Run Header was uploaded " + pelotonRun.Name + ", " + run2.RunId);
                            pelotonRun.RunId = run2.RunId;
                            PelotonHelper.addRunData(runData, pelotonRun, syncListener);
                        }

                        @Override // com.kopin.peloton.Peloton.CloudListener
                        public void onFailure(Failure failure, int i, String s) {
                            SyncManager.setSynced(localRun, null);
                            Log.w(PelotonHelper.TAG, "Run could not be uploaded " + pelotonRun.Name);
                        }
                    });
                    return;
                }
                if (shareRunData == null) {
                    SyncManager.startSyncing(runData);
                }
                pelotonRun.RunId = pelotonRunId;
                addRunData(runData, pelotonRun, syncListener);
                return;
            }
            return;
        }
        Log.w(TAG, "uploadRunWorkFlat run not shared as gear was not synced (or run is syncing)  " + localRun.getId() + ", " + localRun.getTitle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRideData(final RideData rideData, final Ride ride, final Sync.SyncListener syncListener) {
        Log.i(TAG, "Uploading ride data " + ride.Name);
        Peloton.setRideData(ride, new Peloton.RideDataListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.13
            @Override // com.kopin.peloton.Peloton.RideDataListener
            public void onRideData(Ride ride2) {
                SyncManager.setSynced(rideData, ride2);
                Log.i(PelotonHelper.TAG, "Ride data was uploaded " + ride2.Name);
                if (syncListener != null) {
                    syncListener.onSuccess();
                }
            }

            @Override // com.kopin.peloton.Peloton.CloudListener
            public void onFailure(Failure failure, int i, String s) {
                Log.w(PelotonHelper.TAG, "Ride data was NOT uploaded " + ride.Name);
                if (syncListener != null) {
                    syncListener.onSuccess();
                }
            }
        });
    }

    public static void updateRide(SavedRide localRide) {
        String username = PelotonPrefs.getEmail();
        final Ride pelotonRide = getPelotonRideData(localRide, username);
        pelotonRide.RideId = SQLHelper.getExternalId(localRide.getId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RIDE);
        Peloton.updateRide(pelotonRide, new Peloton.AddRideListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.14
            @Override // com.kopin.peloton.Peloton.AddRideListener
            public void onRideAdded(Ride ride) {
                Log.w(PelotonHelper.TAG, "Ride has been updated " + pelotonRide.Name);
            }

            @Override // com.kopin.peloton.Peloton.CloudListener
            public void onFailure(Failure failure, int i, String s) {
                Log.w(PelotonHelper.TAG, "Ride could not be updated " + pelotonRide.Name);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRunData(final RunData runData, final Run run, final Sync.SyncListener syncListener) {
        Log.i(TAG, "Uploading run data " + run.Name);
        Peloton.setRunData(run, new Peloton.RunDataListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.15
            @Override // com.kopin.peloton.Peloton.RunDataListener
            public void onRunData(Run run2) {
                SyncManager.setSynced(runData, run2);
                Log.i(PelotonHelper.TAG, "Run data was uploaded " + run2.Name);
                if (syncListener != null) {
                    syncListener.onSuccess();
                }
            }

            @Override // com.kopin.peloton.Peloton.CloudListener
            public void onFailure(Failure failure, int i, String s) {
                Log.w(PelotonHelper.TAG, "Run data was NOT uploaded " + run.Name);
                if (syncListener != null) {
                    syncListener.onSuccess();
                }
            }
        });
    }

    public static void updateRun(SavedRun localRun) {
        String username = PelotonPrefs.getEmail();
        final Run pelotonRun = getPelotonRunData(localRun, username);
        pelotonRun.RunId = SQLHelper.getExternalId(localRun.getId(), Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RUN);
        initDefaultGear();
        Peloton.updateRun(pelotonRun, pelotonGear, new Peloton.AddRunListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.16
            @Override // com.kopin.peloton.Peloton.AddRunListener
            public void onRunAdded(Run run) {
                Log.w(PelotonHelper.TAG, "Run has been updated " + pelotonRun.Name);
            }

            @Override // com.kopin.peloton.Peloton.CloudListener
            public void onFailure(Failure failure, int i, String s) {
                Log.w(PelotonHelper.TAG, "Run could not be updated " + pelotonRun.Name);
            }
        });
    }

    public static void sync() {
        if (mProfileCompleteUIListener != null) {
            mProfileCompleteUIListener.onStart();
        }
        syncUserDataWork(false);
    }

    public static void download() {
        syncUserDataWork(true);
    }

    private static void syncUserDataWork(boolean downloadOnly) {
        if (syncItemsTask == null || syncItemsTask.getStatus() != AsyncTask.Status.RUNNING) {
            syncItemsTask = new SyncItemsTask(downloadOnly);
            syncItemsTask.execute(new Void[0]);
        } else {
            Log.i(TAG, "sync / download : SyncItemsTask already running");
        }
    }

    private static class SyncItemsTask extends AsyncTask<Void, Void, Void> {
        private boolean mDownloadOnly;
        private UserData mUserData;

        public SyncItemsTask(boolean downloadOnly) {
            this.mDownloadOnly = false;
            this.mDownloadOnly = downloadOnly;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            String username = PelotonPrefs.getEmail();
            this.mUserData = Peloton.getUserDataNotAsync();
            if (this.mUserData != null && this.mUserData.UserProfile != null && !isCancelled() && Sync.isEnabled()) {
                Log.i(PelotonHelper.TAG, "SyncItemsTask got user data");
                PelotonHelper.syncDownUserdata(this.mUserData.UserProfile, this.mUserData.Bikes, null, username);
                PelotonHelper.syncDownUserdata(this.mUserData.Ftps, username);
                PelotonHelper.syncDownUserdata(this.mUserData.Rtps, username);
                PelotonHelper.syncDownUserdata(this.mUserData.Phrs, username);
                if (!isCancelled()) {
                    if (PelotonHelper.mProfileCompleteUIListener != null) {
                        PelotonHelper.mProfileCompleteUIListener.onComplete();
                    }
                    Sync.setSyncDoneForCurrentSession();
                    if (!isCancelled()) {
                        List<com.kopin.peloton.ride.Route> routes = Peloton.getRoutesNotAsync();
                        if (routes != null && !isCancelled()) {
                            Log.d(PelotonHelper.TAG, "syncing routes downloaded size " + routes.size());
                            PelotonHelper.syncRoutes(routes, username);
                            Log.d(PelotonHelper.TAG, "synced routes downloaded size " + routes.size());
                        }
                        List<TrainingWorkout> trainingWorkouts = Peloton.getTrainingWorkoutsSync();
                        if (trainingWorkouts != null && !isCancelled()) {
                            Log.d(PelotonHelper.TAG, "syncing training downloaded size " + trainingWorkouts.size());
                            PelotonHelper.syncTrainingWorkouts(trainingWorkouts, username);
                            Log.d(PelotonHelper.TAG, "synced training downloaded size " + trainingWorkouts.size());
                        }
                        if (!isCancelled()) {
                            List<Activity> serverWorkouts = new ArrayList<>();
                            List<Ride> rides = Peloton.getRideHeadersNotAsync();
                            List<Run> runs = Peloton.getRunHeadersNotAsync();
                            if (rides != null) {
                                serverWorkouts.addAll(rides);
                            }
                            if (runs != null) {
                                serverWorkouts.addAll(runs);
                            }
                            if (!isCancelled() && serverWorkouts.size() > 0 && (PelotonHelper.processDownloadedRides == null || PelotonHelper.processDownloadedRides.getStatus() != AsyncTask.Status.RUNNING)) {
                                ProcessDownloadedRides unused = PelotonHelper.processDownloadedRides = new ProcessDownloadedRides(serverWorkouts, username, new Sync.CompleteListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.SyncItemsTask.1
                                    @Override // com.kopin.solos.share.Sync.CompleteListener
                                    public void onComplete() {
                                        if (!SyncItemsTask.this.mDownloadOnly) {
                                            Sync.uploadAll();
                                        }
                                    }
                                });
                                PelotonHelper.processDownloadedRides.execute(new Void[0]);
                            } else {
                                Log.i(PelotonHelper.TAG, "syncRides : processDownloadedRides already running");
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void voids) {
            if (this.mUserData == null || this.mUserData.UserProfile == null) {
                if (this.mDownloadOnly) {
                    Sync.retryDownload();
                } else {
                    Sync.retrySync();
                }
            }
        }
    }

    public static synchronized void uploadItems(SparseArray<List> items, final Sync.CompleteListener completeListener) {
        String token = PelotonPrefs.getToken();
        if (token != null && !token.isEmpty() && ((addItemsTask == null || addItemsTask.getStatus() != AsyncTask.Status.RUNNING) && !isUploading && Sync.isEnabled())) {
            addItemsTask = new AddItemsTask(items) { // from class: com.kopin.solos.share.peloton.PelotonHelper.17
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.kopin.solos.share.peloton.PelotonHelper.AddItemsTask, android.os.AsyncTask
                public void onPostExecute(Void v) {
                    super.onPostExecute(v);
                    if (completeListener != null) {
                        completeListener.onComplete();
                    }
                }
            };
            addItemsTask.execute(new Void[0]);
        }
    }

    protected static class AddItemsTask extends AsyncTask<Void, Integer, Void> implements ICancellable {
        SparseArray<List> uploadItems;

        AddItemsTask(SparseArray<List> items) {
            this.uploadItems = items;
        }

        @Override // com.kopin.solos.storage.util.ICancellable
        public boolean isActive() {
            return !isCancelled();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            boolean unused = PelotonHelper.isUploading = true;
            Log.i(PelotonHelper.TAG, "AddItemsTask - doUpload");
            PelotonResponse response = Cloud.getProfileWork();
            if (response.isServerSuccess() && response.result != null) {
                Log.i(PelotonHelper.TAG, "got profile from server, sync the profile now");
                if (!isCancelled() && Sync.isEnabled()) {
                    PelotonHelper.syncProfile((Profile) response.result, PelotonPrefs.getEmail(), null);
                }
            }
            List bikes = this.uploadItems.get(PelotonItem.Bike.ordinal());
            List ftps = this.uploadItems.get(PelotonItem.FTP.ordinal());
            List routes = this.uploadItems.get(PelotonItem.Route.ordinal());
            List trainingIds = this.uploadItems.get(PelotonItem.Training.ordinal());
            List workouts = this.uploadItems.get(PelotonItem.Workout.ordinal());
            if (bikes != null) {
                for (Object bike : bikes) {
                    if (!isCancelled()) {
                        processBike(bike);
                    }
                }
            }
            if (ftps != null) {
                for (Object ftp : ftps) {
                    if (!isCancelled() && ftp != null) {
                        processFtp(ftp);
                    }
                }
            }
            if (routes != null) {
                for (Object route : routes) {
                    if (!isCancelled()) {
                        processRoute(route);
                    }
                }
            }
            if (trainingIds != null) {
                for (Object trainingId : trainingIds) {
                    if (!isCancelled()) {
                        processTraining(trainingId);
                    }
                }
            }
            if (workouts != null) {
                int unused2 = PelotonHelper.pendingRides = workouts.size();
                for (Object workout : workouts) {
                    switch (((SavedWorkout) workout).getSportType()) {
                        case RIDE:
                            if (!isCancelled()) {
                                PelotonHelper.uploadRideWorkFlat((SavedRide) workout, this, PelotonHelper.uploadRideListener);
                            }
                            break;
                        case RUN:
                            if (!isCancelled()) {
                                PelotonHelper.uploadRunWorkFlat((SavedRun) workout, this, PelotonHelper.uploadRideListener);
                            }
                            break;
                    }
                }
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
            int i = Sync.incSyncedItems();
            Log.i(PelotonHelper.TAG, "bike/ftp/route progress " + i + "/" + Sync.getNumUnsyncedItems());
            if (PelotonHelper.mSyncUpdateListener != null) {
                PelotonHelper.mSyncUpdateListener.onProgress(i, Sync.getNumUnsyncedItems());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void v) {
            if (PelotonHelper.pendingRides == 0) {
                boolean unused = PelotonHelper.isUploading = false;
                if (PelotonHelper.mSyncUpdateListener != null) {
                    Log.i(PelotonHelper.TAG, "onComplete " + Sync.getNumSyncedItems() + " / " + Sync.getNumUnsyncedItems());
                    PelotonHelper.mSyncUpdateListener.onComplete();
                }
                Sync.reset();
            }
        }

        void processBike(Object object) {
            Bike solosBike = (Bike) object;
            if (solosBike.isActive()) {
                com.kopin.peloton.Bike bike = new com.kopin.peloton.Bike(solosBike.toContentValues(), Bike.Field.NAME.name(), Bike.Field.WHEEL_SIZE.name(), Bike.Field.WEIGHT.name());
                if (SyncManager.isUnSynced(solosBike) && Sync.isEnabled()) {
                    SyncManager.startSyncing(solosBike);
                    PelotonResponse response = Cloud.addBikeWork(bike);
                    if (response.isServerSuccess() && response.result != null) {
                        com.kopin.peloton.Bike bike2 = (com.kopin.peloton.Bike) response.result;
                        Log.i(PelotonHelper.TAG, "Added Bike to server (and share) " + bike2.Name + ", server id = " + bike2.BikeId);
                        SyncManager.setSynced(solosBike, bike2);
                        publishProgress(new Integer[0]);
                        return;
                    }
                    Log.w(PelotonHelper.TAG, "Added Bike to server fail");
                    SyncManager.setSynced(solosBike, null);
                    return;
                }
                return;
            }
            Log.e(PelotonHelper.TAG, "additems bike is not active don't include");
        }

        void processTraining(Object object) {
            Long trainingId = (Long) object;
            SavedTraining savedTraining = SavedTraining.getNewInstance(trainingId.longValue());
            if (savedTraining != null) {
                TrainingWorkout trainingWorkout = TrainingConversion.savedToPelotonTraining(savedTraining);
                if (SyncManager.isUnSynced(savedTraining)) {
                    SyncManager.startSyncing(savedTraining);
                    PelotonResponse response = Peloton.addTrainingSync(trainingWorkout);
                    if (response.isServerSuccess() && response.result != null) {
                        TrainingWorkout trainingWorkout1 = (TrainingWorkout) response.result;
                        Log.i(PelotonHelper.TAG, "Added Training to server (and share) " + trainingWorkout.Name + ", server id = " + trainingWorkout1.TrainingId);
                        SyncManager.setSynced(savedTraining, trainingWorkout1);
                        publishProgress(new Integer[0]);
                        return;
                    }
                    Log.w(PelotonHelper.TAG, "Added Training to server fail");
                    SyncManager.setSynced(savedTraining, null);
                    return;
                }
                return;
            }
            Log.e(PelotonHelper.TAG, "processTraining: Saved Training is null");
        }

        void processFtp(Object object) {
            FTP ftp = (FTP) object;
            if (SyncManager.isUnSynced(ftp) && Sync.isEnabled()) {
                SyncManager.startSyncing(ftp);
                switch (ftp.mThresholdType) {
                    case RUN_FTP:
                        RFTP pelotonRFTP = new RFTP(ftp.mValue, ftp.mDate);
                        PelotonResponse response = Cloud.addRFTPWork(pelotonRFTP);
                        if (response.isServerSuccess() && response.result != null) {
                            RFTP pelotonRFTP2 = (RFTP) response.result;
                            Log.i(PelotonHelper.TAG, "Added RFTP to server (and share) ");
                            SyncManager.setSynced(ftp, pelotonRFTP2);
                            publishProgress(new Integer[0]);
                        } else {
                            Log.w(PelotonHelper.TAG, "Added rftp to server fail ");
                            SyncManager.setSynced(ftp, null);
                        }
                        break;
                    case PEAK_HR:
                        PHR pelotonPHR = new PHR(ftp.mValue, ftp.mDate);
                        PelotonResponse response2 = Cloud.addPHRWork(pelotonPHR);
                        if (response2.isServerSuccess() && response2.result != null) {
                            PHR pelotonPHR2 = (PHR) response2.result;
                            Log.i(PelotonHelper.TAG, "Added PHR to server (and share) ");
                            SyncManager.setSynced(ftp, pelotonPHR2);
                            publishProgress(new Integer[0]);
                        } else {
                            Log.w(PelotonHelper.TAG, "Added phr to server fail ");
                            SyncManager.setSynced(ftp, null);
                        }
                        break;
                    default:
                        com.kopin.peloton.FTP pelotonFTP = new com.kopin.peloton.FTP(ftp.mValue, ftp.mDate);
                        PelotonResponse response3 = Cloud.addFTPWork(pelotonFTP);
                        if (response3.isServerSuccess() && response3.result != null) {
                            com.kopin.peloton.FTP pelotonFTP2 = (com.kopin.peloton.FTP) response3.result;
                            Log.i(PelotonHelper.TAG, "Added FTP to server (and share) ");
                            SyncManager.setSynced(ftp, pelotonFTP2);
                            publishProgress(new Integer[0]);
                        } else {
                            Log.w(PelotonHelper.TAG, "Added ftp to server fail ");
                            SyncManager.setSynced(ftp, null);
                        }
                        break;
                }
            }
        }

        void processRoute(Object object) {
            Route.Saved solosRoute = (Route.Saved) object;
            final com.kopin.peloton.ride.Route pelotonRoute = new com.kopin.peloton.ride.Route(solosRoute.getRouteName(), solosRoute.getTimeToBeat(), solosRoute.getDistance());
            SQLHelper.foreachCoord(solosRoute.getId(), new SQLHelper.foreachCoordCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.AddItemsTask.1
                @Override // com.kopin.solos.storage.SQLHelper.foreachCoordCallback
                public boolean onCoordinate(Coordinate coord) {
                    pelotonRoute.addWayPoint(new WayPoint(coord.getLatitude(), coord.getLongitude(), coord.getAltitude()));
                    return false;
                }
            });
            Log.i(PelotonHelper.TAG, "Uploading local route " + solosRoute.getId() + ", " + solosRoute.getRouteName());
            if (SyncManager.isUnSynced(solosRoute) && Sync.isEnabled()) {
                SyncManager.startSyncing(solosRoute);
                PelotonResponse response = Cloud.addRouteWork(pelotonRoute);
                if (response.isServerSuccess() && response.result != null) {
                    com.kopin.peloton.ride.Route route = (com.kopin.peloton.ride.Route) response.result;
                    Log.i(PelotonHelper.TAG, "Added route to server (and share) " + route.Name + ", server id = " + route.RouteId);
                    SyncManager.setSynced(solosRoute, route);
                    publishProgress(new Integer[0]);
                    return;
                }
                Log.w(PelotonHelper.TAG, "Added route to server fail ");
                SyncManager.setSynced(solosRoute, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void uploadRideResponse(boolean increment) {
        pendingRides--;
        int i = increment ? Sync.incSyncedItems() : Sync.getNumSyncedItems();
        Log.d(TAG, "upload ride progress " + i + " / " + Sync.getNumUnsyncedItems());
        if (mSyncUpdateListener != null) {
            mSyncUpdateListener.onProgress(i, Sync.getNumUnsyncedItems());
        }
        if (pendingRides <= 0) {
            isUploading = false;
            if (mSyncUpdateListener != null) {
                Log.i(TAG, "onComplete " + Sync.getNumSyncedItems() + " / " + Sync.getNumUnsyncedItems());
                mSyncUpdateListener.onComplete();
            }
            Sync.reset();
        }
    }

    public static void syncProfile(final Profile profile, final String username, final Sync.CompleteListener completeListener) {
        Rider rider = SQLHelper.getLatestRider();
        long date = rider == null ? 0L : SQLHelper.getSharedDate(rider.getId(), Platforms.Peloton.getSharedKey(), Shared.ShareType.PROFILE);
        if (profile.TimeStamp > date) {
            UserProfile.setName(profile.Name);
            UserProfile.setDOB(profile.DateOfBirth);
            UserProfile.setGender(profile.Gender);
            UserProfile.setWeightKG(profile.Weight);
            Log.i(TAG, "sync profile from server " + profile.Name + ", " + profile.DateOfBirth);
            SQLHelper.addRider(UserProfile.createRider(), username, new SQLHelper.AddListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.19
                @Override // com.kopin.solos.storage.SQLHelper.AddListener
                public void onAdded(long rowId) {
                    SQLHelper.addShare(new Shared(rowId, Platforms.Peloton.getSharedKey(), username, Sync.PROFILE_ID, true, Shared.ShareType.PROFILE, profile.TimeStamp), new SQLHelper.AddListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.19.1
                        @Override // com.kopin.solos.storage.SQLHelper.AddListener
                        public void onAdded(long rowId2) {
                            if (completeListener != null) {
                                completeListener.onComplete();
                            }
                        }
                    });
                }
            });
            return;
        }
        if (profile.TimeStamp < date) {
            Log.i(TAG, "syncProfile, local profile is newer, push to server, DOB = " + profile.DateOfBirth);
            Profile prof = new Profile(rider.name, UserProfile.getDOBMillis(), UserProfile.getWeightKG());
            prof.TimeStamp = date;
            prof.Gender = rider.gender.getInitial();
            Log.i(TAG, "sync profile from app " + prof.Name + ", " + prof.DateOfBirth);
            Peloton.setProfile(prof, new Peloton.ProfileListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.20
                @Override // com.kopin.peloton.Peloton.ProfileListener
                public void onProfile(Profile profile2) {
                    if (completeListener != null) {
                        completeListener.onComplete();
                    }
                }

                @Override // com.kopin.peloton.Peloton.CloudListener
                public void onFailure(Failure failure, int i, String s) {
                    if (completeListener != null) {
                        completeListener.onComplete();
                    }
                }
            });
            return;
        }
        if (completeListener != null) {
            Log.i(TAG, "sync profile no change");
            completeListener.onComplete();
        }
    }

    public static void syncBikes(List<com.kopin.peloton.Bike> list, String username, Sync.CompleteListener completeListener) {
        if (list != null && list.size() > 0) {
            Log.i(TAG, "syncBikes from server num " + list.size());
            List<String> serverIds = new ArrayList<>();
            for (com.kopin.peloton.Bike bike : list) {
                serverIds.add(bike.BikeId);
                Log.d(TAG, "bike " + bike.toString());
                Bike solosBike = new Bike(bike.toContentValues(Bike.Field.NAME.name(), Bike.Field.WHEEL_SIZE.name(), Bike.Field.WEIGHT.name()));
                if (SQLHelper.syncBike(Platforms.Peloton.getSharedKey(), bike.BikeId, username, solosBike, bike.TimeStamp)) {
                    Log.i(TAG, "syncBike " + bike.toString());
                } else {
                    Log.d(TAG, "syncBike already had been synced (or logging out) " + bike.toString());
                }
            }
            Map<String, Bike> localBikeMap = SQLHelper.getSyncedBikes(Platforms.Peloton.getSharedKey(), username);
            Set<String> keys = localBikeMap.keySet();
            for (String key : keys) {
                Bike localBike = localBikeMap.get(key);
                if (localBike.isActive() && !serverIds.contains(key)) {
                    Log.i(TAG, "hiding bike " + localBike.getId() + " " + localBike.getName());
                    SQLHelper.hideBike(localBike);
                } else if (!localBike.isActive() && serverIds.contains(key)) {
                    Peloton.removeBike(key, null);
                }
            }
        }
        if (completeListener != null) {
            Log.d(TAG, " *** sync bikes onComplete " + Sync.getNumSyncedItems() + " / " + Sync.getNumUnsyncedItems());
            completeListener.onComplete();
        }
    }

    public static void syncDownUserdata(Profile profile, List<com.kopin.peloton.Bike> list, List<com.kopin.peloton.FTP> listFtp, String username) {
        syncProfile(profile, username, null);
        if (list != null && list.size() > 0 && Sync.isEnabled()) {
            Log.i(TAG, "syncBikes from server num " + list.size());
            List<String> serverIds = new ArrayList<>();
            for (com.kopin.peloton.Bike bike : list) {
                serverIds.add(bike.BikeId);
                Log.d(TAG, "bike " + bike.toString());
                Bike solosBike = new Bike(bike.toContentValues(Bike.Field.NAME.name(), Bike.Field.WHEEL_SIZE.name(), Bike.Field.WEIGHT.name()));
                if (SQLHelper.syncBike(Platforms.Peloton.getSharedKey(), bike.BikeId, username, solosBike, bike.TimeStamp)) {
                    Log.i(TAG, "syncBike " + bike.toString());
                } else {
                    Log.d(TAG, "syncBike already had been synced (or logging out) " + bike.toString());
                }
            }
            Map<String, Bike> localBikeMap = SQLHelper.getSyncedBikes(Platforms.Peloton.getSharedKey(), username);
            Set<String> keys = localBikeMap.keySet();
            for (String key : keys) {
                Bike localBike = localBikeMap.get(key);
                if (localBike.isActive() && !serverIds.contains(key)) {
                    Log.i(TAG, "hiding bike " + localBike.getId() + " " + localBike.getName());
                    SQLHelper.hideBike(localBike);
                } else if (!localBike.isActive() && serverIds.contains(key)) {
                    Peloton.removeBike(key, null);
                }
            }
        }
        if (listFtp != null && listFtp.size() > 0 && Sync.isEnabled()) {
            Log.i(TAG, "syncFTP from server size " + listFtp.size());
            for (com.kopin.peloton.FTP ftp : listFtp) {
                if (SQLHelper.syncFTP(Platforms.Peloton.getSharedKey(), ftp.FunctionalThresholdPowerId, username, ftp.FtpValue, FTP.NAME, ftp.TimeStamp)) {
                    Log.i(TAG, "syncFTP " + ftp.toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void syncDownUserdata(List<? extends Object> list, String username) {
        if (list != null && list.size() > 0 && Sync.isEnabled()) {
            Log.i(TAG, "syncUserData from server size " + list.size());
            for (Object data : list) {
                if (data instanceof com.kopin.peloton.FTP) {
                    com.kopin.peloton.FTP ftp = (com.kopin.peloton.FTP) data;
                    if (SQLHelper.syncFTP(Platforms.Peloton.getSharedKey(), ftp.FunctionalThresholdPowerId, username, ftp.FtpValue, FTP.NAME, ftp.TimeStamp)) {
                        Log.i(TAG, "syncFTP " + ftp.toString());
                    }
                } else if (data instanceof RFTP) {
                    RFTP rftp = (RFTP) data;
                    if (SQLHelper.syncFTP(Platforms.Peloton.getSharedKey(), rftp.RtpId, username, rftp.RtpValue, "RUN_FTP", rftp.TimeStamp)) {
                        Log.i(TAG, "syncRFTP " + rftp.toString());
                    }
                } else if (data instanceof PHR) {
                    PHR phr = (PHR) data;
                    if (SQLHelper.syncFTP(Platforms.Peloton.getSharedKey(), phr.PhrId, username, phr.PhrValue, "PEAK_HR", phr.TimeStamp)) {
                        Log.i(TAG, "syncPHR " + phr.toString());
                    }
                } else {
                    Log.i(TAG, "sync unknown UserData type: " + data.toString());
                }
            }
        }
    }

    public static void syncRoutes(List<com.kopin.peloton.ride.Route> list, String username) {
        if (list != null && !list.isEmpty() && Sync.isEnabled()) {
            for (com.kopin.peloton.ride.Route route : list) {
                long id = SQLHelper.getLocalId(route.RouteId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.ROUTE);
                if (id > 0) {
                    if (!SQLHelper.isSavedRoute(id)) {
                        Sync.removeRoute(id, route.RouteId);
                    }
                } else if (SQLHelper.syncRoute(Platforms.Peloton.getSharedKey(), route.RouteId, username, route.Name, route.Duration, route.Distance, System.currentTimeMillis(), route.getLocations())) {
                    Log.d(TAG, "saving Route to db " + route.RouteId + ", " + route.Name);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void syncTrainingWorkouts(List<TrainingWorkout> list, String username) {
        int platformId = Platforms.Peloton.getSharedKey();
        if (list != null && !list.isEmpty() && Sync.isEnabled()) {
            for (TrainingWorkout trainingWorkout : list) {
                long id = SQLHelper.getLocalId(trainingWorkout.TrainingId, platformId, username, Shared.ShareType.TRAINING);
                if (id <= 0 && SQLHelper.canSyncTraining(username, trainingWorkout.TrainingId, platformId)) {
                    long trainingId = TrainingConversion.addPelotonTrainingToDb(trainingWorkout);
                    Shared shared = new Shared(trainingId, platformId, username, trainingWorkout.TrainingId, true, Shared.ShareType.TRAINING, System.currentTimeMillis());
                    SQLHelper.addShare(shared);
                    Log.d(TAG, "Saving Training to db " + trainingWorkout.TrainingId + ", " + trainingWorkout.Name);
                }
            }
        }
    }

    public static boolean removeCloudWorkout(long headerId, long dataId, SportType sportType, String serverId) {
        SavedWorkout localWorkout;
        if (headerId <= 0 || dataId <= 0 || (localWorkout = SavedRides.getWorkout(sportType, headerId, true)) == null || localWorkout.isComplete() || !(localWorkout.getTitle() == null || localWorkout.getTitle().isEmpty())) {
            return false;
        }
        Sync.removeWorkout(headerId, sportType);
        Log.w(TAG, "*** removing workout " + headerId + ", " + serverId + ", " + localWorkout.getOrGenerateTitle());
        return true;
    }

    public static void removeLocalWorkoutsNotOnServer(final List<String> cloudRideIds) {
        final String username = PelotonPrefs.getEmail();
        SavedRides.foreachWorkout(SportType.RIDE, 0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.21
            @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
            public boolean onWorkoutCursor(Cursor cursor) {
                String externalId;
                long localId = SavedRide.getIdFromCursor(cursor);
                if (localId != -1 && (externalId = SQLHelper.getExternalId(localId, Config.SYNC_PROVIDER.getSharedKey(), username, Shared.ShareType.RIDE)) != null && !externalId.isEmpty() && !cloudRideIds.contains(externalId)) {
                    Log.w(PelotonHelper.TAG, "removed ride that was deleted from another device " + localId + ", " + externalId);
                    SavedRides.deleteWorkout(SportType.RIDE, localId);
                    return true;
                }
                return true;
            }
        });
        SavedRides.foreachWorkout(SportType.RUN, 0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.22
            @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
            public boolean onWorkoutCursor(Cursor cursor) {
                String externalId;
                long localId = SavedRun.getIdFromCursor(cursor);
                if (localId != -1 && (externalId = SQLHelper.getExternalId(localId, Config.SYNC_PROVIDER.getSharedKey(), username, Shared.ShareType.RUN)) != null && !externalId.isEmpty() && !cloudRideIds.contains(externalId)) {
                    Log.w(PelotonHelper.TAG, "removed run that was deleted from another device " + localId + ", " + externalId);
                    SavedRides.deleteWorkout(SportType.RUN, localId);
                    return true;
                }
                return true;
            }
        });
    }

    public static long getLocalWorkoutId(Activity activity, String serverId, String username, boolean header) {
        if (activity instanceof Ride) {
            return SQLHelper.getLocalId(serverId, Platforms.Peloton.getSharedKey(), username, header ? Shared.ShareType.RIDE : Shared.ShareType.RIDE_DATA);
        }
        if (activity instanceof Run) {
            return SQLHelper.getLocalId(serverId, Platforms.Peloton.getSharedKey(), username, header ? Shared.ShareType.RUN : Shared.ShareType.RUN_DATA);
        }
        return 0L;
    }

    private static class ProcessDownloadedRides extends AsyncTask<Void, Void, Void> {
        private List<Activity> mCloudRides;
        private Sync.CompleteListener mCompleteListener;
        private String username;

        public ProcessDownloadedRides(List<Activity> cloudRides, String user, Sync.CompleteListener completeListener) {
            this.mCloudRides = cloudRides;
            this.username = user;
            this.mCompleteListener = completeListener;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            PelotonHelper.resetRideCount();
            if (this.mCloudRides == null || this.mCloudRides.size() == 0) {
                if (this.mCompleteListener != null) {
                    this.mCompleteListener.onComplete();
                }
                return null;
            }
            if (!isCancelled() && Sync.isEnabled()) {
                Log.i(PelotonHelper.TAG, "SyncRides, get rides/runs from server size " + this.mCloudRides.size());
                List<String> cloudRideIds = new ArrayList<>();
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (Activity activity : this.mCloudRides) {
                    if (activity != null) {
                        SportType sportType = SportType.DEFAULT_TYPE;
                        Ride ride = null;
                        Run run = null;
                        String serverId = null;
                        if (activity instanceof Ride) {
                            sportType = SportType.RIDE;
                            ride = (Ride) activity;
                            serverId = ride.RideId;
                            ride.Id = ride.RideId;
                        } else if (activity instanceof Run) {
                            sportType = SportType.RUN;
                            run = (Run) activity;
                            serverId = run.RunId;
                            run.Id = run.RunId;
                        }
                        long headerId = PelotonHelper.getLocalWorkoutId(activity, serverId, this.username, true);
                        long dataId = PelotonHelper.getLocalWorkoutId(activity, serverId, this.username, false);
                        if (serverId != null && !serverId.isEmpty()) {
                            cloudRideIds.add(serverId);
                        }
                        if (headerId > 0 && dataId > 0) {
                            PelotonHelper.removeCloudWorkout(headerId, dataId, sportType, serverId);
                        } else {
                            if (headerId > 0) {
                                SavedRides.deleteWorkout(sportType, headerId);
                                if (ride != null) {
                                    Log.i(PelotonHelper.TAG, "*** removed local header to get all the ride again" + headerId + ", " + ride.RideId + ", " + ride.Name);
                                }
                                if (run != null) {
                                    Log.i(PelotonHelper.TAG, "*** removed local header to get all the run again" + headerId + ", " + run.RunId + ", " + run.Name);
                                }
                            }
                            if (ride != null) {
                                arrayList.add(ride);
                            } else if (run != null) {
                                arrayList2.add(run);
                            }
                        }
                    }
                }
                PelotonHelper.removeLocalWorkoutsNotOnServer(cloudRideIds);
                int unused = PelotonHelper.numDownloadingRides = arrayList.size() + arrayList2.size();
                if (PelotonHelper.numDownloadingRides == 0) {
                    if (this.mCompleteListener != null) {
                        this.mCompleteListener.onComplete();
                    }
                    PelotonHelper.resetRideCount();
                    return null;
                }
                long jCurrentTimeMillis = System.currentTimeMillis() - Config.CLOUD_RIDE_DATA_LOAD_PERIOD_MILLIS;
                Collections.sort(arrayList, new Comparator<Ride>() { // from class: com.kopin.solos.share.peloton.PelotonHelper.ProcessDownloadedRides.1
                    @Override // java.util.Comparator
                    public int compare(Ride r1, Ride r2) {
                        return Long.valueOf(r2.StartTime).compareTo(Long.valueOf(r1.StartTime));
                    }
                });
                Collections.sort(arrayList2, new Comparator<Run>() { // from class: com.kopin.solos.share.peloton.PelotonHelper.ProcessDownloadedRides.2
                    @Override // java.util.Comparator
                    public int compare(Run r1, Run r2) {
                        return Long.valueOf(r2.StartTime).compareTo(Long.valueOf(r1.StartTime));
                    }
                });
                synchronized (PelotonHelper.idMap) {
                    PelotonHelper.idMap.clear();
                }
                List<Activity> newWorkouts = new ArrayList<>();
                newWorkouts.addAll(arrayList);
                newWorkouts.addAll(arrayList2);
                getNextWorkoutData(newWorkouts);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getNextWorkoutData(List<Activity> newWorkouts) {
            getNextWorkoutData(newWorkouts, 0);
        }

        private void getNextWorkoutData(List<Activity> newWorkouts, int index) {
            if (!isCancelled() && newWorkouts.size() > index && Sync.isEnabled()) {
                if (PelotonHelper.mSyncUpdateListener != null) {
                    PelotonHelper.mSyncUpdateListener.onProgress(Sync.getNumSyncedItems(), Sync.getNumUnsyncedItems());
                }
                Activity workout = newWorkouts.get(index);
                Log.d(PelotonHelper.TAG, "getNextWorkoutData " + index + " " + workout.Id + " " + workout.Name);
                if (workout instanceof Ride) {
                    Ride ride = (Ride) workout;
                    getGhostWorkoutData(newWorkouts, ride.GhostRideId);
                    Log.i(PelotonHelper.TAG, "continue, now remove ride and process " + ride.RideId + " " + ride.Name);
                    newWorkouts.remove(workout);
                    getNextRideData(ride, newWorkouts);
                    return;
                }
                if (workout instanceof Run) {
                    Run run = (Run) workout;
                    getGhostWorkoutData(newWorkouts, run.GhostRunId);
                    Log.i(PelotonHelper.TAG, "continue, now remove run and process " + run.RunId + " " + run.Name);
                    newWorkouts.remove(workout);
                    getNextRunData(run, newWorkouts);
                }
            }
        }

        private void getGhostWorkoutData(List<Activity> newWorkouts, String ghostId) {
            if (ghostId != null && !ghostId.isEmpty()) {
                int i = 0;
                for (Activity activity : newWorkouts) {
                    if (ghostId.equalsIgnoreCase(activity.Id)) {
                        Log.i(PelotonHelper.TAG, "found ghost workout index " + i + " ghostid " + ghostId);
                        getNextWorkoutData(newWorkouts, i);
                        return;
                    }
                    i++;
                }
            }
        }

        private void getNextRideData(Ride ride, final List<Activity> newRides) {
            Log.d(PelotonHelper.TAG, "getNextRideData download the data " + ride.RideId + " " + ride.Name);
            if (ride != null && ride.RideId != null && !ride.RideId.isEmpty() && !isCancelled() && Sync.getSyncPolicy() != Sync.SyncPolicy.OVERVIEW) {
                Ride rideData = Peloton.getRideDataNotAsync(ride.RideId);
                if (rideData != null) {
                    Log.d(PelotonHelper.TAG, "syncRides, getRideData " + ride.RideId + " GOT");
                    if (!isCancelled()) {
                        ProcessDownloadedRideDataTask unused = PelotonHelper.processDownloadedRideDataTask = new ProcessDownloadedRideDataTask(rideData, ride, new Sync.SyncListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.ProcessDownloadedRides.3
                            @Override // com.kopin.solos.share.Sync.SyncListener
                            public void onSuccess() {
                                if (PelotonHelper.access$1604() >= PelotonHelper.numDownloadingRides && ProcessDownloadedRides.this.mCompleteListener != null) {
                                    Log.i(PelotonHelper.TAG, "syncRides, getRideData COMPLETE");
                                    if (ProcessDownloadedRides.this.mCompleteListener != null) {
                                        ProcessDownloadedRides.this.mCompleteListener.onComplete();
                                    }
                                    PelotonHelper.resetRideCount();
                                }
                                Log.i(PelotonHelper.TAG, "rides " + PelotonHelper.numDownloadedRides + " : " + PelotonHelper.numDownloadingRides);
                                ProcessDownloadedRides.this.getNextWorkoutData(newRides);
                            }

                            @Override // com.kopin.solos.share.Sync.SyncListener
                            public void onFailure() {
                                ProcessDownloadedRides.this.getNextWorkoutData(newRides);
                            }
                        });
                        PelotonHelper.processDownloadedRideDataTask.execute(new Void[0]);
                        return;
                    }
                    return;
                }
                Log.w(PelotonHelper.TAG, "get server ride data fail " + ride.RideId);
                if (!isCancelled()) {
                    if (PelotonHelper.access$1604() >= PelotonHelper.numDownloadingRides && this.mCompleteListener != null) {
                        Log.i(PelotonHelper.TAG, "syncRides, getRideData COMPLETE");
                        if (this.mCompleteListener != null) {
                            this.mCompleteListener.onComplete();
                        }
                        PelotonHelper.resetRideCount();
                    }
                    Log.i(PelotonHelper.TAG, "rides " + PelotonHelper.numDownloadedRides + " : " + PelotonHelper.numDownloadingRides);
                    getNextWorkoutData(newRides);
                    return;
                }
                return;
            }
            if (Sync.getSyncPolicy() == Sync.SyncPolicy.OVERVIEW) {
                Log.i(PelotonHelper.TAG, "syncRides, getRideData OVerview do not download full ride data");
                if (PelotonHelper.access$1604() >= PelotonHelper.numDownloadingRides && this.mCompleteListener != null) {
                    Log.i(PelotonHelper.TAG, "syncRides, getRideData COMPLETE");
                    if (this.mCompleteListener != null) {
                        this.mCompleteListener.onComplete();
                    }
                    PelotonHelper.resetRideCount();
                }
                Log.i(PelotonHelper.TAG, "rides " + PelotonHelper.numDownloadedRides + " : " + PelotonHelper.numDownloadingRides);
            }
            getNextWorkoutData(newRides);
        }

        private void getNextRunData(Run run, final List<Activity> newRuns) {
            if (run != null && run.RunId != null && !run.RunId.isEmpty() && !isCancelled() && Sync.getSyncPolicy() != Sync.SyncPolicy.OVERVIEW) {
                Run runData = Peloton.getRunDataNotAsync(run.RunId);
                if (runData != null) {
                    Log.d(PelotonHelper.TAG, "syncRuns, getRunData " + run.RunId + " GOT");
                    if (!isCancelled()) {
                        ProcessDownloadedRunDataTask unused = PelotonHelper.processDownloadedRunDataTask = new ProcessDownloadedRunDataTask(runData, run, new Sync.SyncListener() { // from class: com.kopin.solos.share.peloton.PelotonHelper.ProcessDownloadedRides.4
                            @Override // com.kopin.solos.share.Sync.SyncListener
                            public void onSuccess() {
                                if (PelotonHelper.access$1604() >= PelotonHelper.numDownloadingRides && ProcessDownloadedRides.this.mCompleteListener != null) {
                                    Log.i(PelotonHelper.TAG, "syncRuns, getRunData COMPLETE");
                                    ProcessDownloadedRides.this.mCompleteListener.onComplete();
                                    PelotonHelper.resetRideCount();
                                }
                                Log.i(PelotonHelper.TAG, "runs " + PelotonHelper.numDownloadingRides + " : " + PelotonHelper.numDownloadingRides);
                                ProcessDownloadedRides.this.getNextWorkoutData(newRuns);
                            }

                            @Override // com.kopin.solos.share.Sync.SyncListener
                            public void onFailure() {
                                ProcessDownloadedRides.this.getNextWorkoutData(newRuns);
                            }
                        });
                        PelotonHelper.processDownloadedRunDataTask.execute(new Void[0]);
                        return;
                    }
                    return;
                }
                Log.w(PelotonHelper.TAG, "get server run data fail " + run.RunId);
                if (!isCancelled()) {
                    if (PelotonHelper.access$1604() >= PelotonHelper.numDownloadingRides && this.mCompleteListener != null) {
                        Log.i(PelotonHelper.TAG, "syncRuns, getRunData COMPLETE");
                        this.mCompleteListener.onComplete();
                        PelotonHelper.resetRideCount();
                    }
                    Log.i(PelotonHelper.TAG, "runs " + PelotonHelper.numDownloadedRides + " : " + PelotonHelper.numDownloadingRides);
                    getNextWorkoutData(newRuns);
                    return;
                }
                return;
            }
            if (Sync.getSyncPolicy() == Sync.SyncPolicy.OVERVIEW) {
                Log.i(PelotonHelper.TAG, "syncRuns, getRunData OVerview do not download full run data");
                if (PelotonHelper.access$1604() >= PelotonHelper.numDownloadingRides && this.mCompleteListener != null) {
                    Log.i(PelotonHelper.TAG, "syncRuns, getRunData COMPLETE");
                    this.mCompleteListener.onComplete();
                    PelotonHelper.resetRideCount();
                }
                Log.i(PelotonHelper.TAG, "runs " + PelotonHelper.numDownloadedRides + " : " + PelotonHelper.numDownloadingRides);
            }
            getNextWorkoutData(newRuns);
        }
    }

    private static class ProcessDownloadedRideDataTask extends AsyncTask<Void, Void, Void> {
        private Ride mHeader;
        private Ride mRide;
        private Sync.SyncListener mSyncListener;
        private String username = PelotonPrefs.getEmail();

        public ProcessDownloadedRideDataTask(Ride rideData, Ride rideHeader, Sync.SyncListener syncListener) {
            this.mRide = rideData;
            this.mHeader = rideHeader;
            this.mSyncListener = syncListener;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            PelotonHelper.saveRide(this.mRide, this.mHeader, this.username, this.mSyncListener, this);
            return null;
        }
    }

    private static long processRideHeader(Ride ride, String username, AsyncTask asyncTask) {
        SavedRide newRide;
        long externalId = -1;
        Workout.RideMode rideMode = Workout.RideMode.NORMAL;
        if (ride.GhostRideId != null && !ride.GhostRideId.isEmpty()) {
            rideMode = Workout.RideMode.GHOST_RIDE;
            Long localId = idMap.get(ride.GhostRideId);
            externalId = localId != null ? localId.longValue() : SQLHelper.getLocalId(ride.GhostRideId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RIDE);
            if (externalId == 0) {
                externalId = -1;
            }
            Log.d(TAG, "ProcessDownloadedRides, local ghost id is " + externalId);
        }
        if (ride.RouteFollowedId != null && !ride.RouteFollowedId.isEmpty()) {
            rideMode = Workout.RideMode.ROUTE;
            externalId = SQLHelper.getLocalId(ride.RouteFollowedId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.ROUTE);
            if (externalId == 0) {
                externalId = -1;
            }
        }
        if (ride.TrainingId != null && !ride.TrainingId.isEmpty()) {
            rideMode = Workout.RideMode.TRAINING;
            externalId = SQLHelper.getLocalId(ride.TrainingId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.TRAINING);
            if (externalId == 0) {
                externalId = -1;
            }
        }
        long id = SQLHelper.addDownloadedRide(ride.RideId, getRideInfo(ride, username, rideMode, externalId), Platforms.Peloton.getSharedKey(), username);
        idMap.put(ride.RideId, Long.valueOf(id));
        Log.i(TAG, "ProcessDownloadedRides, addFullride " + id);
        if ((asyncTask == null || !asyncTask.isCancelled()) && (newRide = (SavedRide) SavedRides.getWorkout(SportType.RIDE, id, true)) != null) {
            double trainingStressScore = newRide.calculateTrainingStressScore();
            SQLHelper.updateRideTSS(id, trainingStressScore);
        }
        return id;
    }

    public static long saveRide(Ride mRide, Ride mHeader, String username, Sync.SyncListener mSyncListener, AsyncTask asyncTask) {
        RideRecorder recorder = new RideRecorder(null);
        long routeId = SQLHelper.addRoute();
        List<CorrectedElevation> elevations = new ArrayList<>();
        long mLocalId = processRideHeader(mHeader, username, asyncTask);
        recorder.start(mLocalId, routeId, true);
        long locationCount = saveRecords(recorder, mRide.Records, elevations);
        recorder.end();
        SQLHelper.addElevation(elevations, null);
        if (locationCount >= 2) {
            SavedRide savedRide = (SavedRide) SavedRides.getWorkout(SportType.RIDE, mLocalId, true);
            if (savedRide != null) {
                SQLHelper.updateRoute(routeId, savedRide.getDuration(), savedRide.getDistance(), "");
                SQLHelper.updateRideRoute(mLocalId, routeId);
            } else {
                Log.w(TAG, "Unable to update route from a null local ride (from Peloton). RideId " + mLocalId);
                if (mSyncListener != null) {
                    mSyncListener.onFailure();
                }
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
                return 0L;
            }
        } else {
            SQLHelper.removeRoute(routeId);
        }
        for (com.kopin.peloton.ride.Lap lap : mRide.Laps) {
            long lapId = SQLHelper.addLap(mLocalId);
            SQLHelper.updateLap(lapId, PelotonConversion.getSolosLap(lap));
        }
        Shared rideShared = new Shared(mLocalId, Platforms.Peloton.getSharedKey(), username, mHeader.RideId, true, Shared.ShareType.RIDE, System.currentTimeMillis());
        Shared rideDataShared = new Shared(mLocalId, Platforms.Peloton.getSharedKey(), username, mHeader.RideId, true, Shared.ShareType.RIDE_DATA, System.currentTimeMillis());
        SQLHelper.markRideFinishedShared(mLocalId, rideShared, rideDataShared);
        if (mSyncListener != null) {
            mSyncListener.onSuccess();
            return mLocalId;
        }
        return mLocalId;
    }

    private static long saveRecords(RideRecorder recorder, List<RideRecord> rideRecords, final List<CorrectedElevation> elevations) {
        long locationCount = 0;
        for (RideRecord pelotonRecord : rideRecords) {
            recorder.newRecord(pelotonRecord.Timestamp, false);
            if (pelotonRecord.Position != null) {
                recorder.onLocation(pelotonRecord.Position.Latitude, pelotonRecord.Position.Longitude, (float) pelotonRecord.Position.Altitude);
                locationCount++;
            }
            for (Metric metric : pelotonRecord.Values) {
                if (metric.Name != null && (metric.ValueDouble != null || metric.ValueInteger != null)) {
                    final double d = metric.ValueDouble != null ? metric.ValueDouble.doubleValue() : metric.ValueInteger.intValue();
                    switch (PelotonConversion.PelotonMetric.get(metric.Name)) {
                        case Cadence:
                            recorder.onCadence(d);
                            break;
                        case HeartRate:
                            recorder.onHeartRate((int) d);
                            break;
                        case Oxygenation:
                            recorder.onOxygen((int) d);
                            break;
                        case Power:
                            recorder.onBikePower(d);
                            break;
                        case Distance:
                            recorder.onDistance(d);
                            break;
                        case Speed:
                            recorder.onSpeed(d);
                            break;
                        case Stride:
                            recorder.onStride(d);
                            break;
                        case CorrectedElevation:
                            recorder.onPostCoordinateSaved(new RideRecorder.OnCoordinateSavedCallback() { // from class: com.kopin.solos.share.peloton.PelotonHelper.23
                                @Override // com.kopin.solos.storage.RideRecorder.OnCoordinateSavedCallback
                                public void onPositionSaved(long coordId, Coordinate coord) {
                                    CorrectedElevation elev = new CorrectedElevation(coord.getTimestamp(), coordId, d);
                                    elevations.add(elev);
                                }
                            });
                            break;
                    }
                }
            }
        }
        return locationCount;
    }

    private static class ProcessDownloadedRunDataTask extends AsyncTask<Void, Void, Void> {
        private Run mHeader;
        private Run mRun;
        private Sync.SyncListener mSyncListener;
        private String username = PelotonPrefs.getEmail();

        public ProcessDownloadedRunDataTask(Run runData, Run runHeader, Sync.SyncListener syncListener) {
            this.mRun = runData;
            this.mHeader = runHeader;
            this.mSyncListener = syncListener;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            PelotonHelper.saveRun(this.mRun, this.mHeader, this.username, this.mSyncListener, this);
            return null;
        }
    }

    private static long processRunHeader(Run run, String username, AsyncTask asyncTask) {
        SavedRun newRun;
        long externalId = -1;
        Workout.RideMode runMode = Workout.RideMode.NORMAL;
        if (run.GhostRunId != null && !run.GhostRunId.isEmpty()) {
            runMode = Workout.RideMode.GHOST_RIDE;
            Long localId = idMap.get(run.GhostRunId);
            externalId = localId != null ? localId.longValue() : SQLHelper.getLocalId(run.GhostRunId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.RUN);
            if (externalId == 0) {
                externalId = -1;
            }
            Log.d(TAG, "ProcessDownloadedRuns, local ghost id is " + externalId);
        }
        if (run.RouteFollowedId != null && !run.RouteFollowedId.isEmpty()) {
            runMode = Workout.RideMode.ROUTE;
            externalId = SQLHelper.getLocalId(run.RouteFollowedId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.ROUTE);
            if (externalId == 0) {
                externalId = -1;
            }
        }
        if (run.TrainingId != null && !run.TrainingId.isEmpty()) {
            runMode = Workout.RideMode.TRAINING;
            externalId = SQLHelper.getLocalId(run.TrainingId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.TRAINING);
            if (externalId == 0) {
                externalId = -1;
            }
        }
        long id = SQLHelper.addDownloadedRun(run.RunId, getRunInfo(run, username, runMode, externalId), Platforms.Peloton.getSharedKey(), username);
        idMap.put(run.RunId, Long.valueOf(id));
        Log.i(TAG, "ProcessDownloadedRuns, addFullrun " + id);
        if ((asyncTask == null || !asyncTask.isCancelled()) && (newRun = (SavedRun) SavedRides.getWorkout(SportType.RUN, id, true)) != null) {
            double trainingStressScore = newRun.calculateRunningStressScore();
            SQLHelper.updateRunRSS(id, trainingStressScore);
        }
        return id;
    }

    public static long saveRun(Run mRun, Run mHeader, String username, Sync.SyncListener mSyncListener, AsyncTask asyncTask) {
        RideRecorder recorder = new RideRecorder(null);
        long routeId = SQLHelper.addRoute();
        List<CorrectedElevation> elevations = new ArrayList<>();
        long mLocalId = processRunHeader(mHeader, username, asyncTask);
        recorder.start(mLocalId, routeId, true);
        long locationCount = saveRecords(recorder, mRun.Records, elevations);
        recorder.end();
        SQLHelper.addElevation(elevations, null);
        if (locationCount >= 2) {
            SavedRun savedRun = (SavedRun) SavedRides.getWorkout(SportType.RUN, mLocalId, true);
            if (savedRun != null) {
                SQLHelper.updateRoute(routeId, savedRun.getDuration(), savedRun.getDistance(), "");
                SQLHelper.updateRunRoute(mLocalId, routeId);
            } else {
                Log.w(TAG, "Unable to update route from a null local run (from Peloton). RunId " + mLocalId);
                if (mSyncListener != null) {
                    mSyncListener.onFailure();
                }
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
                return 0L;
            }
        } else {
            SQLHelper.removeRoute(routeId);
        }
        for (com.kopin.peloton.ride.Lap lap : mRun.Laps) {
            long lapId = SQLHelper.addLap(mLocalId);
            SQLHelper.updateLap(lapId, PelotonConversion.getSolosLap(lap));
        }
        Shared runShared = new Shared(mLocalId, Platforms.Peloton.getSharedKey(), username, mHeader.RunId, true, Shared.ShareType.RUN, System.currentTimeMillis());
        Shared runDataShared = new Shared(mLocalId, Platforms.Peloton.getSharedKey(), username, mHeader.RunId, true, Shared.ShareType.RUN_DATA, System.currentTimeMillis());
        SQLHelper.markRideFinishedShared(mLocalId, runShared, runDataShared);
        if (mSyncListener != null) {
            mSyncListener.onSuccess();
            return mLocalId;
        }
        return mLocalId;
    }

    private static ContentValues getRideInfo(Ride pelotonRide, String username, Workout.RideMode rideMode, long externalId) {
        long bikeId = SQLHelper.getLocalId(pelotonRide.BikeId, Platforms.Peloton.getSharedKey(), username, Shared.ShareType.BIKE);
        long routeId = -1;
        if (rideMode == Workout.RideMode.ROUTE) {
            routeId = externalId;
            externalId = -1;
        }
        ContentValues values = new ContentValues();
        values.put("routeId", Long.valueOf(routeId));
        values.put(com.kopin.solos.storage.Ride.RIDE_MODE, Integer.valueOf(rideMode.ordinal()));
        values.put(com.kopin.solos.storage.Ride.GHOST_RIDE_ID, Long.valueOf(externalId));
        values.put(com.kopin.solos.storage.Ride.DISTANCE, Integer.valueOf((int) pelotonRide.Distance));
        values.put(com.kopin.solos.storage.Ride.GAINED_ALTITUDE, Double.valueOf(pelotonRide.OverallClimb == null ? -2.147483648E9d : pelotonRide.OverallClimb.doubleValue()));
        values.put(com.kopin.solos.storage.Ride.MAX_ALTITUDE_DIFF, Double.valueOf(pelotonRide.AltitudeRange != null ? pelotonRide.AltitudeRange.doubleValue() : -2.147483648E9d));
        values.put(com.kopin.solos.storage.Ride.START_TIME_ACTUAL, Long.valueOf(pelotonRide.StartTime));
        values.put(com.kopin.solos.storage.Ride.START_TIME, (Integer) 0);
        values.put(com.kopin.solos.storage.Ride.DURATION, Long.valueOf(pelotonRide.Duration));
        values.put(com.kopin.solos.storage.Ride.END_TIME, Long.valueOf(pelotonRide.EndTime));
        values.put(com.kopin.solos.storage.Ride.TITLE, pelotonRide.Name);
        values.put(com.kopin.solos.storage.Ride.COMMENT, pelotonRide.Description);
        if (pelotonRide.RideType != null && !pelotonRide.RideType.isEmpty()) {
            values.put(com.kopin.solos.storage.Ride.ACTIVITY, Integer.valueOf(RideActivity.RideType.getRideType(pelotonRide.RideType).ordinal()));
        }
        values.put(com.kopin.solos.storage.Ride.BIKE_ID, Long.valueOf(bikeId));
        values.put(com.kopin.solos.storage.Ride.FINAL, (Integer) 0);
        if (pelotonRide.OverallStats != null) {
            putAveMax(values, pelotonRide.OverallStats.Cadence, com.kopin.solos.storage.Ride.AVERAGE_CADENCE, com.kopin.solos.storage.Ride.MAX_CADENCE);
            putAveMax(values, pelotonRide.OverallStats.HeartRate, com.kopin.solos.storage.Ride.AVERAGE_HEARTRATE, com.kopin.solos.storage.Ride.MAX_HEARTRATE);
            putAveMax(values, pelotonRide.OverallStats.IntensityFactor, com.kopin.solos.storage.Ride.AVERAGE_INTENSITY, com.kopin.solos.storage.Ride.MAX_INTENSITY);
            putAveMax(values, pelotonRide.OverallStats.NormalisedPower, com.kopin.solos.storage.Ride.AVERAGE_NORM_POWER, com.kopin.solos.storage.Ride.MAX_NORM_POWER);
            putAveMax(values, pelotonRide.OverallStats.Power, com.kopin.solos.storage.Ride.AVERAGE_POWER, com.kopin.solos.storage.Ride.MAX_POWER);
            putAveMax(values, pelotonRide.OverallStats.Speed, com.kopin.solos.storage.Ride.AVERAGE_SPEED, com.kopin.solos.storage.Ride.MAX_SPEED);
            putAveMin(values, pelotonRide.OverallStats.Oxygenation, com.kopin.solos.storage.Ride.AVERAGE_OXYGEN, com.kopin.solos.storage.Ride.MIN_OXYGEN);
            values.put(com.kopin.solos.storage.Ride.CALORIES, Double.valueOf(pelotonRide.OverallStats.Calories));
        }
        putDouble(values, com.kopin.solos.storage.Ride.TARGET_AVERAGE_CADENCE, pelotonRide.getTarget(RideHeader.TargetType.TargetAverageCadence));
        putDouble(values, com.kopin.solos.storage.Ride.TARGET_AVERAGE_HEARTRATE, pelotonRide.getTarget(RideHeader.TargetType.TargetAverageHeartrate));
        putDouble(values, com.kopin.solos.storage.Ride.TARGET_AVERAGE_POWER, pelotonRide.getTarget(RideHeader.TargetType.TargetAveragePower));
        putDouble(values, com.kopin.solos.storage.Ride.TARGET_AVERAGE_SPEED, pelotonRide.getTarget(RideHeader.TargetType.TargetAverageSpeed));
        values.put("ftp", Double.valueOf(pelotonRide.FunctionalThresholdPower));
        Log.i(TAG, "_ peloton ride " + pelotonRide.StartTime + " : " + pelotonRide.EndTime);
        Log.i(TAG, "___ solos ride 0 : " + pelotonRide.EndTime);
        return values;
    }

    private static ContentValues getRunInfo(Run pelotonRun, String username, Workout.RideMode runMode, long externalId) {
        long routeId = -1;
        if (runMode == Workout.RideMode.ROUTE) {
            routeId = externalId;
            externalId = -1;
        }
        ContentValues values = new ContentValues();
        values.put(Run.Field.ROUTE_ID.name(), Long.valueOf(routeId));
        values.put(Run.Field.RUN_MODE.name(), Integer.valueOf(runMode.ordinal()));
        values.put(Run.Field.GHOST_OR_ROUTE_ID.name(), Long.valueOf(externalId));
        values.put(Run.Field.DISTANCE.name(), Integer.valueOf((int) pelotonRun.Distance));
        values.put(Run.Field.OVERALL_CLIMB.name(), Double.valueOf(pelotonRun.OverallClimb == null ? -2.147483648E9d : pelotonRun.OverallClimb.doubleValue()));
        values.put(Run.Field.ALTITUDE_RANGE.name(), Double.valueOf(pelotonRun.AltitudeRange != null ? pelotonRun.AltitudeRange.doubleValue() : -2.147483648E9d));
        values.put(Run.Field.START_TIME.name(), Long.valueOf(pelotonRun.StartTime));
        values.put(Run.Field.END_TIME.name(), Long.valueOf(pelotonRun.EndTime));
        values.put(Run.Field.DURATION.name(), Long.valueOf(pelotonRun.Duration));
        values.put(Run.Field.TITLE.name(), pelotonRun.Name);
        values.put(Run.Field.COMMENT.name(), pelotonRun.Description);
        if (pelotonRun.RunType != null && !pelotonRun.RunType.isEmpty()) {
            values.put(Run.Field.RUN_TYPE.name(), Integer.valueOf(RunType.getRunType(pelotonRun.RunType).ordinal()));
        }
        values.put(Run.Field.FINAL.name(), (Integer) 0);
        if (pelotonRun.OverallStats != null) {
            putAveMax(values, pelotonRun.OverallStats.Cadence, Run.Field.AVG_CADENCE.name(), Run.Field.MAX_CADENCE.name());
            putAveMax(values, pelotonRun.OverallStats.HeartRate, Run.Field.AVG_HEARTRATE.name(), Run.Field.MAX_HEARTRATE.name());
            putAveMax(values, pelotonRun.OverallStats.IntensityFactor, Run.Field.AVG_IF.name(), Run.Field.MAX_IF.name());
            putAveMax(values, pelotonRun.OverallStats.NormalisedPower, Run.Field.AVG_NORMALISED_POWER.name(), Run.Field.MAX_NORMALISED_POWER.name());
            putAveMax(values, pelotonRun.OverallStats.Power, Run.Field.AVG_POWER.name(), Run.Field.MAX_POWER.name());
            putAveMax(values, pelotonRun.OverallStats.Stride, Run.Field.AVG_STRIDE.name(), Run.Field.MAX_STRIDE.name());
            putAveMax(values, pelotonRun.OverallStats.Pace, Run.Field.AVG_PACE.name(), Run.Field.MAX_PACE.name());
            putAveMin(values, pelotonRun.OverallStats.Oxygenation, Run.Field.AVG_OXYGEN.name(), Run.Field.MIN_OXYGEN.name());
            values.put(Run.Field.CALORIES.name(), Double.valueOf(pelotonRun.OverallStats.Calories));
        }
        putDouble(values, Run.Field.TARGET_CADENCE.name(), pelotonRun.getTarget(RideHeader.TargetType.TargetAverageCadence));
        putDouble(values, Run.Field.TARGET_HEARTRATE.name(), pelotonRun.getTarget(RideHeader.TargetType.TargetAverageHeartrate));
        putDouble(values, Run.Field.TARGET_POWER.name(), pelotonRun.getTarget(RideHeader.TargetType.TargetAveragePower));
        putDouble(values, Run.Field.TARGET_STRIDE.name(), pelotonRun.getTarget(RideHeader.TargetType.TargetAverageStride));
        putDouble(values, Run.Field.TARGET_PACE.name(), pelotonRun.getTarget(RideHeader.TargetType.TargetAveragePace));
        values.put(Run.Field.FTP.name(), Double.valueOf(pelotonRun.FunctionalThresholdPower));
        Log.i(TAG, "_ peloton run " + pelotonRun.StartTime + " : " + pelotonRun.EndTime);
        Log.i(TAG, "___ solos run 0 : " + pelotonRun.EndTime);
        return values;
    }

    private static void putDouble(ContentValues values, String key, Double d) {
        if (d != null) {
            values.put(key, d);
        }
    }

    public static void putAveMin(ContentValues values, MetricStat metricStat, String keyAve, String keyMin) {
        values.put(keyAve, Double.valueOf(metricStat == null ? -2.147483648E9d : metricStat.Average));
        values.put(keyMin, Double.valueOf(metricStat != null ? metricStat.Minimum : -2.147483648E9d));
    }

    public static void putAveMax(ContentValues values, MetricStat metricStat, String keyAve, String keyMax) {
        values.put(keyAve, Double.valueOf(metricStat == null ? -2.147483648E9d : metricStat.Average));
        values.put(keyMax, Double.valueOf(metricStat != null ? metricStat.Maximum : -2.147483648E9d));
    }

    private static void putAveMax(ContentValues values, MetricStat metricStat, String keyAve, String keyMax, double multiplier) {
        values.put(keyAve, Double.valueOf(metricStat == null ? -2.147483648E9d : metricStat.Average * multiplier));
        values.put(keyMax, Double.valueOf(metricStat != null ? metricStat.Maximum * multiplier : -2.147483648E9d));
    }

    public static void fillValues(ContentValues contentValues, double value, String... keys) {
        for (String key : keys) {
            contentValues.put(key, Double.valueOf(value));
        }
    }

    public static void cancelTasks() {
        if (processDownloadedRides != null) {
            processDownloadedRides.cancel(true);
        }
        if (syncItemsTask != null) {
            syncItemsTask.cancel(true);
        }
        if (processDownloadedRideDataTask != null) {
            processDownloadedRideDataTask.cancel(true);
        }
        if (addItemsTask != null) {
            addItemsTask.cancel(true);
        }
        resetRideCount();
    }

    public static Shared.ShareType getShareType(Object pelotonData) {
        if (pelotonData == null) {
            return Shared.ShareType.NONE;
        }
        if ((pelotonData instanceof Ride) || (pelotonData instanceof SavedRide)) {
            return Shared.ShareType.RIDE;
        }
        if (pelotonData instanceof RideData) {
            return Shared.ShareType.RIDE_DATA;
        }
        if ((pelotonData instanceof com.kopin.peloton.Run) || (pelotonData instanceof SavedRun)) {
            return Shared.ShareType.RUN;
        }
        if (pelotonData instanceof RunData) {
            return Shared.ShareType.RUN_DATA;
        }
        if ((pelotonData instanceof com.kopin.peloton.Bike) || (pelotonData instanceof Bike)) {
            return Shared.ShareType.BIKE;
        }
        if ((pelotonData instanceof com.kopin.peloton.FTP) || (pelotonData instanceof FTP)) {
            return Shared.ShareType.FTP;
        }
        if ((pelotonData instanceof com.kopin.peloton.ride.Route) || (pelotonData instanceof Route.Saved)) {
            return Shared.ShareType.ROUTE;
        }
        if ((pelotonData instanceof TrainingWorkout) || (pelotonData instanceof SavedTraining)) {
            return Shared.ShareType.TRAINING;
        }
        return Shared.ShareType.NONE;
    }

    public static void clearIdMap() {
        synchronized (idMap) {
            idMap.clear();
        }
    }

    public static void setNumDownloadingRides(int numDownloading) {
        numDownloadingRides = numDownloading;
    }

    public static void setNumDownloadedRides(int numDownloaded) {
        numDownloadedRides = numDownloaded;
    }

    public static int incrementNumDownloadedRides() {
        int i = numDownloadedRides + 1;
        numDownloadedRides = i;
        return i;
    }
}
