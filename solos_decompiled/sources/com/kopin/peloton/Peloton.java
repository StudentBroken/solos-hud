package com.kopin.peloton;

import android.content.Context;
import android.os.AsyncTask;
import com.kopin.peloton.PelotonBase;
import com.kopin.peloton.ride.Route;
import com.kopin.peloton.training.TrainingWorkout;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class Peloton extends PelotonBase {
    private static PelotonBase.AddBikeTask addBikeTask;
    private static PelotonBase.AddFTPTask addFTPTask;
    private static PelotonBase.AddGearTask addGearTask;
    private static PelotonBase.AddRideHeaderTask addRideHeaderTask;
    private static PelotonBase.AddRideTask addRideTask;
    private static PelotonBase.AddRouteTask addRouteTask;
    private static PelotonBase.AddRunHeaderTask addRunHeaderTask;
    private static PelotonBase.AddRunTask addRunTask;
    private static PelotonBase.AddTrainingTask addTrainingTask;
    private static PelotonBase.GetBikesTask getBikesTask;
    private static PelotonBase.GetFTPTask getFTPTask;
    private static PelotonBase.GetGearTask getGearTask;
    private static PelotonBase.GetProfileTask getProfileTask;
    private static PelotonBase.GetRideDataTask getRideDataTask;
    private static PelotonBase.GetRidesTask getRidesTask;
    private static PelotonBase.GetRoutesTask getRoutesTask;
    private static PelotonBase.GetRunDataTask getRunDataTask;
    private static PelotonBase.GetRunsTask getRunsTask;
    private static PelotonBase.GetTrainingWorkoutsTask getTrainingsTask;
    private static PelotonBase.GetUserDataTask getUserDataTask;
    private static PelotonBase.GetWorkoutsTask getWorkoutsTask;
    private static PelotonBase.RegisterExternalTask registerExternalTask;
    private static PelotonBase.RegisterTask registerTask;
    private static PelotonBase.RemoveBikeTask removeBikeTask;
    private static PelotonBase.RemoveGearTask removeGearTask;
    private static PelotonBase.RemoveRideTask removeRideTask;
    private static PelotonBase.RemoveRouteTask removeRouteTask;
    private static PelotonBase.RemoveRunTask removeRunTask;
    private static PelotonBase.RemoveTrainingTask removeTrainingTask;
    private static PelotonBase.SetProfileTask setProfileTask;
    private static PelotonBase.SetRideDataTask setRideDataTask;
    private static PelotonBase.SetRunDataTask setRunDataTask;
    private static PelotonBase.SignInTask signInTask;
    private static PelotonBase.UpdateBikeTask updateBikeTask;
    private static PelotonBase.UpdateGearTask updateGearTask;
    private static PelotonBase.UpdateRideTask updateRideTask;
    private static PelotonBase.UpdateRunTask updateRunTask;

    public interface AddFTPListener extends CloudListener {
        void onAdded(FTP ftp);
    }

    public interface AddRideListener extends CloudListener {
        void onRideAdded(Ride ride);
    }

    public interface AddRunListener extends CloudListener {
        void onRunAdded(Run run);
    }

    public interface AddTrainingListener extends CloudListener {
        void onTrainingAdded(TrainingWorkout trainingWorkout);
    }

    public interface AuthenticationListener {
        void onFailed(PelotonResponse pelotonResponse);
    }

    public interface BikeListener extends CloudListener {
        void onBike(Bike bike);
    }

    public interface BikesListener extends CloudListener {
        void onBikes(List<Bike> list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface CloudListener {
        void onFailure(Failure failure, int i, String str);
    }

    public interface DataListener extends CloudListener {
        void onData(Profile profile, List<Bike> list, List<FTP> list2);
    }

    public interface GearListener extends CloudListener {
        void onGear(Gear gear);
    }

    public interface GetFTPListener extends CloudListener {
        void onFTP(List<FTP> list);
    }

    public interface GetGearListener extends CloudListener {
        void onGear(List<Gear> list);
    }

    public interface LoginListener extends CloudListener {
        void onLoggedIn();
    }

    public interface LoginUserDataListener extends CloudListener {
        void onLoggedIn(UserData userData);
    }

    public interface ProfileListener extends CloudListener {
        void onProfile(Profile profile);
    }

    public interface RemoveListener extends CloudListener {
        void onRemoved();
    }

    public interface RideDataListener extends CloudListener {
        void onRideData(Ride ride);
    }

    public interface RidesListener extends CloudListener {
        void onRides(List<Ride> list);
    }

    public interface RouteListener extends CloudListener {
        void onRoute(Route route);
    }

    public interface RoutesListener extends CloudListener {
        void onRoutes(List<Route> list);
    }

    public interface RunDataListener extends CloudListener {
        void onRunData(Run run);
    }

    public interface RunsListener extends CloudListener {
        void onRuns(List<Run> list);
    }

    public interface TrainingsListener extends CloudListener {
        void onTrainings(List<TrainingWorkout> list);
    }

    public interface UserDataListener extends CloudListener {
        void onUserData(UserData userData);
    }

    public interface WorkoutsListener extends CloudListener {
        void onWorkouts(List<Ride> list, List<Run> list2);
    }

    public static boolean init(Context context, String deviceId, AuthenticationListener authenticationListener) {
        return Cloud.init(context, deviceId, authenticationListener);
    }

    public static void register(UserInfo userInfo, LoginListener loginListener) {
        registerTask = new PelotonBase.RegisterTask(userInfo, loginListener);
        registerTask.execute(new Void[0]);
    }

    public static void registerExternal(UserInfo userInfo, LoginListener loginListener) {
        registerExternalTask = new PelotonBase.RegisterExternalTask(userInfo, loginListener);
        registerExternalTask.execute(new Void[0]);
    }

    public static void signIn(String email, String password, LoginUserDataListener loginListener) {
        signInTask = new PelotonBase.SignInTask(email, password, loginListener);
        signInTask.execute(new Void[0]);
    }

    public static void getUserData(UserDataListener userDataListener) {
        getUserDataTask = new PelotonBase.GetUserDataTask(userDataListener);
        getUserDataTask.execute(new Void[0]);
    }

    public static UserData getUserDataNotAsync() {
        PelotonResponse response = Cloud.getUserDataWork();
        if (!response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (UserData) response.result;
    }

    public static void getProfile(ProfileListener profileListener) {
        getProfileTask = new PelotonBase.GetProfileTask(profileListener);
        getProfileTask.execute(new Void[0]);
    }

    public static void setProfile(Profile profile, ProfileListener profileListener) {
        setProfileTask = new PelotonBase.SetProfileTask(profile, profileListener);
        setProfileTask.execute(new Void[0]);
    }

    public static void getBikes(BikesListener bikeListener) {
        getBikesTask = new PelotonBase.GetBikesTask(bikeListener);
        getBikesTask.execute(new Void[0]);
    }

    public static void addBike(Bike bike, BikeListener bikeListener) {
        addBikeTask = new PelotonBase.AddBikeTask(bike, bikeListener);
        addBikeTask.execute(new Void[0]);
    }

    public static void updateBike(Bike bike, BikeListener bikeListener) {
        updateBikeTask = new PelotonBase.UpdateBikeTask(bike, bikeListener);
        updateBikeTask.execute(new Void[0]);
    }

    public static void removeBike(String id, RemoveListener removeListener) {
        removeBikeTask = new PelotonBase.RemoveBikeTask(id, removeListener);
        removeBikeTask.execute(new Void[0]);
    }

    public static void getGear(GetGearListener getGearListener) {
        getGearTask = new PelotonBase.GetGearTask(getGearListener);
        getGearTask.execute(new Void[0]);
    }

    public static void addGear(Gear gear, GearListener gearListener) {
        addGearTask = new PelotonBase.AddGearTask(gear, gearListener);
        addGearTask.execute(new Void[0]);
    }

    public static void updateGear(Gear gear, GearListener gearListener) {
        updateGearTask = new PelotonBase.UpdateGearTask(gear, gearListener);
        updateGearTask.execute(new Void[0]);
    }

    public static void removeGear(String id, RemoveListener removeListener) {
        removeGearTask = new PelotonBase.RemoveGearTask(id, removeListener);
        removeGearTask.execute(new Void[0]);
    }

    public static void getRoutes(RoutesListener listener) {
        getRoutesTask = new PelotonBase.GetRoutesTask(listener);
        getRoutesTask.execute(new Void[0]);
    }

    public static List<Route> getRoutesNotAsync() {
        PelotonResponse response = Cloud.getRoutesWork();
        if (!response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (List) response.result;
    }

    public static void addRoute(Route route, RouteListener routeListener) {
        addRouteTask = new PelotonBase.AddRouteTask(route, routeListener);
        addRouteTask.execute(new Void[0]);
    }

    public static void removeRoute(String id, RemoveListener removeListener) {
        removeRouteTask = new PelotonBase.RemoveRouteTask(id, removeListener);
        removeRouteTask.execute(new Void[0]);
    }

    public static void getFTP(GetFTPListener ftpListener) {
        getFTPTask = new PelotonBase.GetFTPTask(ftpListener);
        getFTPTask.execute(new Void[0]);
    }

    public static void addFTP(FTP ftp, AddFTPListener listener) {
        addFTPTask = new PelotonBase.AddFTPTask(ftp, listener);
        addFTPTask.execute(new Void[0]);
    }

    public static void getRides(RidesListener listener) {
        getRidesTask = new PelotonBase.GetRidesTask(listener);
        getRidesTask.execute(new Void[0]);
    }

    public static void getWorkouts(WorkoutsListener listener) {
        getWorkoutsTask = new PelotonBase.GetWorkoutsTask(listener);
        getWorkoutsTask.execute(new Void[0]);
    }

    public static List<Ride> getRideHeadersNotAsync() {
        PelotonResponse response = Cloud.getRidesWork();
        if (!response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (List) response.result;
    }

    public static List<Run> getRunHeadersNotAsync() {
        PelotonResponse response = Cloud.getRunsWork();
        if (!response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (List) response.result;
    }

    public static void addRideHeader(Ride ride, AddRideListener listener) {
        addRideHeaderTask = new PelotonBase.AddRideHeaderTask(ride, listener);
        addRideHeaderTask.execute(new Void[0]);
    }

    @Deprecated
    public static void addRide(Ride ride, AddRideListener listener) {
        addRideTask = new PelotonBase.AddRideTask(ride, listener);
        addRideTask.execute(new Void[0]);
    }

    @Deprecated
    public static List<PelotonResponse> addRide(Ride ride) {
        return Cloud.addRideWork(ride);
    }

    public static void updateRide(Ride ride, AddRideListener rideListener) {
        updateRideTask = new PelotonBase.UpdateRideTask(ride, rideListener);
        updateRideTask.execute(new Void[0]);
    }

    public static Ride updateRideNotAsync(Ride ride) {
        PelotonResponse response = Cloud.updateRideDataWork(ride);
        if (response.isServerSuccess()) {
            return ride;
        }
        return null;
    }

    public static Ride setRideNotAsync(Ride ride) {
        PelotonResponse response = Cloud.setRide(ride);
        if (response == null || !response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (Ride) response.result;
    }

    public static void removeRide(String rideId, RemoveListener listener) {
        removeRideTask = new PelotonBase.RemoveRideTask(rideId, listener);
        removeRideTask.execute(new Void[0]);
    }

    public static Ride getRideDataNotAsync(String rideId) {
        PelotonResponse response = Cloud.getRideDataWork(rideId);
        if (response == null || !response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (Ride) response.result;
    }

    public static void getRideData(String rideId, RideDataListener listener) {
        getRideDataTask = new PelotonBase.GetRideDataTask(rideId, listener);
        getRideDataTask.execute(new Void[0]);
    }

    public static void setRideData(Ride rideData, RideDataListener listener) {
        setRideDataTask = new PelotonBase.SetRideDataTask(rideData, listener);
        setRideDataTask.execute(new Void[0]);
    }

    public static void getRuns(RunsListener listener) {
        getRunsTask = new PelotonBase.GetRunsTask(listener);
        getRunsTask.execute(new Void[0]);
    }

    public static void addRunHeader(Run run, AddRunListener listener) {
        addRunHeaderTask = new PelotonBase.AddRunHeaderTask(run, listener);
        addRunHeaderTask.execute(new Void[0]);
    }

    public static void addRunHeader(Run run, Gear defaultGear, AddRunListener listener) {
        addRunHeaderTask = new PelotonBase.AddRunHeaderTask(run, defaultGear, listener);
        addRunHeaderTask.execute(new Void[0]);
    }

    public static void addRun(Run run, AddRunListener listener) {
        addRunTask = new PelotonBase.AddRunTask(run, listener);
        addRunTask.execute(new Void[0]);
    }

    @Deprecated
    public static List<PelotonResponse> addRun(Run run) {
        return Cloud.addRunWork(run);
    }

    public static void updateRun(Run run, AddRunListener runListener) {
        updateRunTask = new PelotonBase.UpdateRunTask(run, runListener);
        updateRunTask.execute(new Void[0]);
    }

    public static void updateRun(Run run, Gear defaultGear, AddRunListener runListener) {
        updateRunTask = new PelotonBase.UpdateRunTask(run, defaultGear, runListener);
        updateRunTask.execute(new Void[0]);
    }

    public static Run updateRunNotAsync(Run run) {
        PelotonResponse response = Cloud.updateRunDataWork(run);
        if (response.isServerSuccess()) {
            return run;
        }
        return null;
    }

    public static Run setRunNotAsync(Run run) {
        PelotonResponse response = Cloud.setRun(run);
        if (response == null || !response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (Run) response.result;
    }

    public static void removeRun(String runId, RemoveListener listener) {
        removeRunTask = new PelotonBase.RemoveRunTask(runId, listener);
        removeRunTask.execute(new Void[0]);
    }

    public static Run getRunDataNotAsync(String runId) {
        PelotonResponse response = Cloud.getRunDataWork(runId);
        if (response == null || !response.isServerSuccess() || response.result == null) {
            return null;
        }
        return (Run) response.result;
    }

    public static void getRunData(String runId, RunDataListener listener) {
        getRunDataTask = new PelotonBase.GetRunDataTask(runId, listener);
        getRunDataTask.execute(new Void[0]);
    }

    public static void setRunData(Run runData, RunDataListener listener) {
        setRunDataTask = new PelotonBase.SetRunDataTask(runData, listener);
        setRunDataTask.execute(new Void[0]);
    }

    public static void getTrainingWorkoutsAsync(TrainingsListener listener) {
        getTrainingsTask = new PelotonBase.GetTrainingWorkoutsTask(listener);
        getTrainingsTask.execute(new Void[0]);
    }

    public static List<TrainingWorkout> getTrainingWorkoutsSync() {
        PelotonResponse response = getTrainingWorkouts();
        return (response == null || !response.isServerSuccess() || response.result == null) ? new ArrayList() : (List) response.result;
    }

    public static void addTrainingAsync(TrainingWorkout training, AddTrainingListener listener) {
        addTrainingTask = new PelotonBase.AddTrainingTask(training, listener);
        addTrainingTask.execute(new Void[0]);
    }

    public static PelotonResponse addTrainingSync(TrainingWorkout training) {
        PelotonResponse response = Cloud.addTrainingHeader(training);
        if (response != null && response.isServerSuccess() && response.result != null) {
            TrainingWorkout header = (TrainingWorkout) response.result;
            training.TrainingId = header.TrainingId;
            return Cloud.addTrainingData(training, false);
        }
        return response;
    }

    public static void removeTrainingAsync(String trainingId, RemoveListener listener) {
        removeTrainingTask = new PelotonBase.RemoveTrainingTask(trainingId, listener);
        removeTrainingTask.execute(new Void[0]);
    }

    public static void removeAllTrainingAsync(RemoveListener listener) {
        new PelotonBase.RemoveAllTrainingTask(listener).execute(new Void[0]);
    }

    public static void configure(boolean live, boolean https) {
        Cloud.setHttps(https);
        if (live) {
            Cloud.setHostLive();
        } else {
            Cloud.setHostDev();
        }
    }

    public static String getHost() {
        return Cloud.getHost();
    }

    public static String getPasswordRecoveryUrl() {
        return Cloud.getPasswordRecoveryUrl();
    }

    public static void cancelTasks() {
        cancelTasks(registerTask, registerExternalTask, signInTask, getUserDataTask, getProfileTask, setProfileTask, getBikesTask, addBikeTask, updateBikeTask, removeBikeTask, getRoutesTask, addRouteTask, removeRouteTask, getFTPTask, addFTPTask, getRidesTask, addRideHeaderTask, addRideTask, removeRideTask, getRideDataTask, setRideDataTask, updateRideTask, getWorkoutsTask, getRunsTask, addRunHeaderTask, addRunTask, updateRunTask, removeRunTask, getRunDataTask, setRunDataTask, getGearTask, addGearTask, updateGearTask, removeGearTask, getTrainingsTask, addTrainingTask, removeTrainingTask);
    }

    private static void cancelTasks(AsyncTask... asyncTasks) {
        if (asyncTasks != null) {
            for (AsyncTask asyncTask : asyncTasks) {
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
            }
        }
    }
}
