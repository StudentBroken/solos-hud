package com.kopin.peloton;

import android.os.AsyncTask;
import android.util.Log;
import com.kopin.peloton.Peloton;
import com.kopin.peloton.ride.Route;
import com.kopin.peloton.training.TrainingWorkout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
abstract class PelotonBase {
    static final String TAG = "PelotonBase";

    PelotonBase() {
    }

    protected static class RegisterTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.LoginListener mLoginListener;
        final UserInfo mUserInfo;

        RegisterTask(UserInfo userInfo, Peloton.LoginListener listener) {
            this.mUserInfo = userInfo;
            this.mLoginListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.registerWork(this.mUserInfo);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.registerDoCallback(response, this.mLoginListener);
        }
    }

    protected static class RegisterExternalTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.LoginListener mLoginListener;
        final UserInfo mUserInfo;

        RegisterExternalTask(UserInfo userInfo, Peloton.LoginListener listener) {
            this.mUserInfo = userInfo;
            this.mLoginListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.registerExternalWork(this.mUserInfo);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.registerDoCallback(response, this.mLoginListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerDoCallback(PelotonResponse response, Peloton.LoginListener listener) {
        Log.d(TAG, "registerDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess()) {
                listener.onLoggedIn();
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class SignInTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mEmail;
        final Peloton.LoginUserDataListener mListener;
        final String mPassword;

        SignInTask(String email, String password, Peloton.LoginUserDataListener loginListener) {
            this.mEmail = email;
            this.mPassword = password;
            this.mListener = loginListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            PelotonResponse response = Cloud.signInWork(this.mEmail, this.mPassword);
            if (this.mListener != null && response != null && response.isServerSuccess() && response.result != null) {
                return Cloud.getUserDataWork();
            }
            return response;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.signInDoCallback(response, this.mListener);
        }
    }

    protected static void signInDoCallback(PelotonResponse response, Peloton.LoginUserDataListener listener) {
        Log.d(TAG, "signInDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess() && response.result != null && (response.result instanceof UserData)) {
                listener.onLoggedIn((UserData) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetUserDataTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.UserDataListener mUserDataListener;

        public GetUserDataTask(Peloton.UserDataListener profileListener) {
            this.mUserDataListener = profileListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getUserDataWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.userDataDoCallback(response, this.mUserDataListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void userDataDoCallback(PelotonResponse response, Peloton.UserDataListener userDataListener) {
        Log.d(TAG, "userDataDoCallback listener is null " + (userDataListener == null));
        if (userDataListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                userDataListener.onUserData((UserData) response.result);
            } else {
                userDataListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetProfileTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.ProfileListener mProfileListener;

        public GetProfileTask(Peloton.ProfileListener profileListener) {
            this.mProfileListener = profileListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getProfileWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.profileDoCallback(response, this.mProfileListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void profileDoCallback(PelotonResponse response, Peloton.ProfileListener profileListener) {
        Log.d(TAG, "profileDoCallback listener is null " + (profileListener == null));
        if (profileListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                profileListener.onProfile((Profile) response.result);
            } else {
                profileListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class SetProfileTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Profile mProfile;
        final Peloton.ProfileListener mProfileListener;

        public SetProfileTask(Profile profile, Peloton.ProfileListener profileListener) {
            this.mProfile = profile;
            this.mProfileListener = profileListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.setProfileWork(this.mProfile);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.profileDoCallback(response, this.mProfileListener);
        }
    }

    protected static class GetBikesTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.BikesListener mBikeListener;

        public GetBikesTask(Peloton.BikesListener bikeListener) {
            this.mBikeListener = bikeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getBikesWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.getBikesDoCallback(response, this.mBikeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getBikesDoCallback(PelotonResponse response, Peloton.BikesListener bikeListener) {
        Log.d(TAG, "getBikesDoCallback listener is null " + (bikeListener == null));
        if (bikeListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                bikeListener.onBikes((List) response.result);
            } else {
                bikeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class AddBikeTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Bike mBike;
        final Peloton.BikeListener mBikeListener;

        public AddBikeTask(Bike bike, Peloton.BikeListener bikeListener) {
            this.mBike = bike;
            this.mBikeListener = bikeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.addBikeWork(this.mBike);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addBikeDoCallback(response, this.mBikeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addBikeDoCallback(PelotonResponse response, Peloton.BikeListener bikeListener) {
        Log.d(TAG, "addBikeDoCallback listener is null " + (bikeListener == null));
        if (bikeListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                bikeListener.onBike((Bike) response.result);
            } else {
                bikeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class UpdateBikeTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Bike mBike;
        final Peloton.BikeListener mBikeListener;

        public UpdateBikeTask(Bike bike, Peloton.BikeListener bikeListener) {
            this.mBike = bike;
            this.mBikeListener = bikeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.updateBikeWork(this.mBike);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.updateBikeDoCallback(response, this.mBikeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateBikeDoCallback(PelotonResponse response, Peloton.BikeListener bikeListener) {
        Log.d(TAG, "updateBikeDoCallback listener is null " + (bikeListener == null));
        if (bikeListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                bikeListener.onBike((Bike) response.result);
            } else {
                bikeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class RemoveBikeTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mBikeId;
        final Peloton.RemoveListener mRemoveListener;

        public RemoveBikeTask(String bikeId, Peloton.RemoveListener removeListener) {
            this.mBikeId = bikeId;
            this.mRemoveListener = removeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.removeBikeWork(this.mBikeId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeBikeDoCallback(response, this.mRemoveListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeBikeDoCallback(PelotonResponse response, Peloton.RemoveListener removeListener) {
        Log.d(TAG, "removeBikeDoCallback listener is null " + (removeListener == null));
        if (removeListener != null) {
            if (response.isServerSuccess()) {
                removeListener.onRemoved();
            } else {
                removeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetGearTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.GetGearListener mGearListener;

        public GetGearTask(Peloton.GetGearListener gearListener) {
            this.mGearListener = gearListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getGearWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.getGearDoCallback(response, this.mGearListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getGearDoCallback(PelotonResponse response, Peloton.GetGearListener gearListener) {
        Log.d(TAG, "getGearDoCallback listener is null " + (gearListener == null));
        if (gearListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                gearListener.onGear((List) response.result);
            } else {
                gearListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class AddGearTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Gear mGear;
        final Peloton.GearListener mGearListener;

        public AddGearTask(Gear gear, Peloton.GearListener gearListener) {
            this.mGear = gear;
            this.mGearListener = gearListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.addGearWork(this.mGear);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addGearDoCallback(response, this.mGearListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addGearDoCallback(PelotonResponse response, Peloton.GearListener gearListener) {
        Log.d(TAG, "addGearDoCallback listener is null " + (gearListener == null));
        if (gearListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                gearListener.onGear((Gear) response.result);
            } else {
                gearListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class UpdateGearTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Gear mGear;
        final Peloton.GearListener mGearListener;

        public UpdateGearTask(Gear gear, Peloton.GearListener gearListener) {
            this.mGear = gear;
            this.mGearListener = gearListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.updateGearWork(this.mGear);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.updateGearDoCallback(response, this.mGearListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateGearDoCallback(PelotonResponse response, Peloton.GearListener gearListener) {
        Log.d(TAG, "updateGearDoCallback listener is null " + (gearListener == null));
        if (gearListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                gearListener.onGear((Gear) response.result);
            } else {
                gearListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class RemoveGearTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mGearId;
        final Peloton.RemoveListener mRemoveListener;

        public RemoveGearTask(String gearId, Peloton.RemoveListener removeListener) {
            this.mGearId = gearId;
            this.mRemoveListener = removeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.removeGearWork(this.mGearId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeGearDoCallback(response, this.mRemoveListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeGearDoCallback(PelotonResponse response, Peloton.RemoveListener removeListener) {
        Log.d(TAG, "removeGearDoCallback listener is null " + (removeListener == null));
        if (removeListener != null) {
            if (response.isServerSuccess()) {
                removeListener.onRemoved();
            } else {
                removeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetRoutesTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RoutesListener mRoutesListener;

        public GetRoutesTask(Peloton.RoutesListener routesListener) {
            this.mRoutesListener = routesListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getRoutesWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.getRoutesDoCallback(response, this.mRoutesListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getRoutesDoCallback(PelotonResponse response, Peloton.RoutesListener routesListener) {
        Log.d(TAG, "getRoutesDoCallback listener is null " + (routesListener == null));
        if (routesListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                routesListener.onRoutes((List) response.result);
            } else {
                routesListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class AddRouteTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Route mRoute;
        final Peloton.RouteListener mRouteListener;

        public AddRouteTask(Route route, Peloton.RouteListener routeListener) {
            this.mRoute = route;
            this.mRouteListener = routeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.addRouteWork(this.mRoute);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addRouteDoCallback(response, this.mRouteListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRouteDoCallback(PelotonResponse response, Peloton.RouteListener routeListener) {
        Log.d(TAG, "addRouteDoCallback listener is null " + (routeListener == null));
        if (routeListener != null) {
            if (response.isServerSuccess() && response.result != null) {
                routeListener.onRoute((Route) response.result);
            } else {
                routeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class RemoveRouteTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RemoveListener mRemoveListener;
        final String mRouteId;

        public RemoveRouteTask(String routeId, Peloton.RemoveListener removeListener) {
            this.mRouteId = routeId;
            this.mRemoveListener = removeListener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.removeRouteWork(this.mRouteId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeRouteDoCallback(response, this.mRemoveListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeRouteDoCallback(PelotonResponse response, Peloton.RemoveListener removeListener) {
        Log.d(TAG, "removeRouteDoCallback listener is null " + (removeListener == null));
        if (removeListener != null) {
            if (response.isServerSuccess()) {
                removeListener.onRemoved();
            } else {
                removeListener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetFTPTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.GetFTPListener mFtpListener;

        public GetFTPTask(Peloton.GetFTPListener listener) {
            this.mFtpListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getFTPWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.ftpDoCallback(response, this.mFtpListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void ftpDoCallback(PelotonResponse response, Peloton.GetFTPListener listener) {
        Log.d(TAG, "ftpDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess() && response.result != null) {
                listener.onFTP((List) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class AddFTPTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.AddFTPListener mAddFTPListener;
        final FTP mFtp;

        public AddFTPTask(FTP ftp, Peloton.AddFTPListener listener) {
            this.mFtp = ftp;
            this.mAddFTPListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.addFTPWork(this.mFtp);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addFTPDoCallback(response, this.mAddFTPListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addFTPDoCallback(PelotonResponse response, Peloton.AddFTPListener listener) {
        Log.d(TAG, "addFTPDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess()) {
                listener.onAdded((FTP) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetRidesTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RidesListener mListener;

        public GetRidesTask(Peloton.RidesListener listener) {
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getRidesWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.getRidesDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getRidesDoCallback(PelotonResponse response, Peloton.RidesListener listener) {
        Log.d(TAG, "getRidesDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess() && response.result != null) {
                listener.onRides((List) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    @Deprecated
    protected static class AddRideHeaderTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.AddRideListener mListener;
        final Ride mRide;

        public AddRideHeaderTask(Ride ride, Peloton.AddRideListener listener) {
            this.mRide = ride;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.addRideHeaderWork(this.mRide);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addRideHeaderDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRideHeaderDoCallback(PelotonResponse response, Peloton.AddRideListener listener) {
        Log.d(TAG, "addRideHeaderDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                listener.onFailure(null, 0, "");
            } else if (response.isServerSuccess() && response.result != null) {
                listener.onRideAdded((Ride) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    @Deprecated
    protected static class AddRideTask extends AsyncTask<Void, Void, List<PelotonResponse>> {
        final Peloton.AddRideListener mListener;
        final Ride mRide;

        public AddRideTask(Ride ride, Peloton.AddRideListener listener) {
            this.mRide = ride;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public List<PelotonResponse> doInBackground(Void... params) {
            return Cloud.addRideWork(this.mRide);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<PelotonResponse> response) {
            PelotonBase.addRideDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRideDoCallback(List<PelotonResponse> responses, Peloton.AddRideListener listener) {
        Log.d(TAG, "addRideDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (responses == null || responses.size() != 2) {
                listener.onFailure(null, 0, "");
                return;
            }
            PelotonResponse response = responses.get(0);
            PelotonResponse bodyResponse = responses.get(1);
            if (response.isServerSuccess() && response.result != null && bodyResponse.isServerSuccess()) {
                listener.onRideAdded((Ride) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class UpdateRideTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.AddRideListener mListener;
        final Ride mRide;

        public UpdateRideTask(Ride ride, Peloton.AddRideListener listener) {
            this.mRide = ride;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.updateRideDataWork(this.mRide);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.updateRideDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateRideDoCallback(PelotonResponse response, Peloton.AddRideListener listener) {
        Log.d(TAG, "updateRideDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess()) {
                listener.onRideAdded(null);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class RemoveRideTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RemoveListener mListener;
        final String mRideId;

        public RemoveRideTask(String rideId, Peloton.RemoveListener listener) {
            this.mRideId = rideId;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.removeRideWork(this.mRideId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeRideDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeRideDoCallback(PelotonResponse response, Peloton.RemoveListener listener) {
        Log.d(TAG, "removeRideDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess()) {
                listener.onRemoved();
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetRideDataTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mId;
        final Peloton.RideDataListener mListener;

        public GetRideDataTask(String rideId, Peloton.RideDataListener listener) {
            this.mId = rideId;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getRideDataWork(this.mId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.rideDataDoCallback(response, this.mListener);
        }
    }

    protected static class SetRideDataTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RideDataListener mListener;
        final Ride mRideData;

        public SetRideDataTask(Ride rideData, Peloton.RideDataListener listener) {
            this.mRideData = rideData;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.setRideDataWork(this.mRideData, this.mRideData.RideId, false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.rideDataDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void rideDataDoCallback(PelotonResponse response, Peloton.RideDataListener listener) {
        Log.d(TAG, "rideDataDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                response = new PelotonResponse();
            }
            if (response.isServerSuccess() && response.result != null) {
                listener.onRideData((Ride) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetWorkoutsTask extends AsyncTask<Void, Void, List<PelotonResponse>> {
        final Peloton.WorkoutsListener mListener;

        public GetWorkoutsTask(Peloton.WorkoutsListener listener) {
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public List<PelotonResponse> doInBackground(Void... params) {
            List<PelotonResponse> responses = new ArrayList<>();
            responses.add(Cloud.getRidesWork());
            responses.add(Cloud.getRunsWork());
            return responses;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<PelotonResponse> responses) {
            Log.d(PelotonBase.TAG, "GetWorkoutsTask listener is null " + (this.mListener == null));
            if (this.mListener != null) {
                List<Ride> rides = new ArrayList<>();
                List<Run> runs = new ArrayList<>();
                if (responses.size() > 0) {
                    PelotonResponse response = responses.get(0);
                    if (response.isServerSuccess() && response.result != null) {
                        rides.addAll((List) response.result);
                    }
                    if (responses.size() > 1) {
                        PelotonResponse response2 = responses.get(1);
                        if (response2.isServerSuccess() && response2.result != null) {
                            runs.addAll((List) response2.result);
                        }
                    }
                    this.mListener.onWorkouts(rides, runs);
                }
            }
        }
    }

    protected static class GetRunsTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RunsListener mListener;

        public GetRunsTask(Peloton.RunsListener listener) {
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getRunsWork();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.getRunsDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getRunsDoCallback(PelotonResponse response, Peloton.RunsListener listener) {
        Log.d(TAG, "getRunsDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess() && response.result != null) {
                listener.onRuns((List) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    @Deprecated
    protected static class AddRunHeaderTask extends AsyncTask<Void, Void, PelotonResponse> {
        Gear mDefaultGear;
        final Peloton.AddRunListener mListener;
        final Run mRun;

        public AddRunHeaderTask(Run run, Peloton.AddRunListener listener) {
            this.mRun = run;
            this.mListener = listener;
        }

        public AddRunHeaderTask(Run run, Gear defaultGear, Peloton.AddRunListener listener) {
            this.mRun = run;
            this.mDefaultGear = defaultGear;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            if (this.mDefaultGear != null) {
                PelotonResponse response = Cloud.getGearWork();
                if (response != null && response.isServerSuccess() && response.result != null) {
                    List<Gear> gears = (List) response.result;
                    if (gears.size() > 0 && gears.get(0) != null) {
                        this.mRun.GearId = gears.get(0).GearId;
                        Log.i(PelotonBase.TAG, "run gear id = " + gears.get(0).GearId);
                        return Cloud.addRunHeaderWork(this.mRun);
                    }
                }
                PelotonResponse response2 = Cloud.addGearWork(this.mDefaultGear);
                if (response2.isServerSuccess() && response2.result != null) {
                    Gear gear = (Gear) response2.result;
                    this.mRun.GearId = gear.GearId;
                    Log.i(PelotonBase.TAG, "run gear id = " + gear.GearId);
                    return Cloud.addRunHeaderWork(this.mRun);
                }
                return null;
            }
            Log.i(PelotonBase.TAG, "no default gear id, run's gear id = " + this.mRun.GearId);
            return Cloud.addRunHeaderWork(this.mRun);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addRunHeaderDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRunHeaderDoCallback(PelotonResponse response, Peloton.AddRunListener listener) {
        Log.d(TAG, "addRunHeaderDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                listener.onFailure(null, 0, "");
            } else if (response.isServerSuccess() && response.result != null) {
                listener.onRunAdded((Run) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class AddRunTask extends AsyncTask<Void, Void, List<PelotonResponse>> {
        final Peloton.AddRunListener mListener;
        final Run mRun;

        public AddRunTask(Run run, Peloton.AddRunListener listener) {
            this.mRun = run;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public List<PelotonResponse> doInBackground(Void... params) {
            return Cloud.addRunWork(this.mRun);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<PelotonResponse> response) {
            PelotonBase.addRunDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addRunDoCallback(List<PelotonResponse> responses, Peloton.AddRunListener listener) {
        Log.d(TAG, "addRunDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (responses == null || responses.size() != 2) {
                listener.onFailure(null, 0, "");
                return;
            }
            PelotonResponse response = responses.get(0);
            PelotonResponse bodyResponse = responses.get(1);
            if (response.isServerSuccess() && response.result != null && bodyResponse.isServerSuccess()) {
                listener.onRunAdded((Run) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class UpdateRunTask extends AsyncTask<Void, Void, PelotonResponse> {
        Gear mDefaultGear;
        final Peloton.AddRunListener mListener;
        final Run mRun;

        public UpdateRunTask(Run run, Peloton.AddRunListener listener) {
            this.mRun = run;
            this.mListener = listener;
        }

        public UpdateRunTask(Run run, Gear defaultGear, Peloton.AddRunListener listener) {
            this.mRun = run;
            this.mDefaultGear = defaultGear;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            if (this.mDefaultGear != null) {
                PelotonResponse response = Cloud.getGearWork();
                if (response != null && response.isServerSuccess() && response.result != null) {
                    List<Gear> gears = (List) response.result;
                    if (gears.size() > 0 && gears.get(0) != null) {
                        this.mRun.GearId = gears.get(0).GearId;
                        Log.i(PelotonBase.TAG, "update run gear default id = " + this.mRun.GearId);
                        return Cloud.updateRunDataWork(this.mRun);
                    }
                }
                PelotonResponse response2 = Cloud.addGearWork(this.mDefaultGear);
                if (response2.isServerSuccess() && response2.result != null) {
                    Gear gear = (Gear) response2.result;
                    this.mRun.GearId = gear.GearId;
                    Log.i(PelotonBase.TAG, "update run gear default id = " + this.mRun.GearId);
                    return Cloud.updateRunDataWork(this.mRun);
                }
                return null;
            }
            Log.i(PelotonBase.TAG, "update run gear id = " + this.mRun.GearId);
            return Cloud.updateRunDataWork(this.mRun);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.updateRunDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateRunDoCallback(PelotonResponse response, Peloton.AddRunListener listener) {
        Log.d(TAG, "updateRunDoCallback listener is null " + (listener == null));
        if (listener != null && response != null) {
            if (response.isServerSuccess()) {
                listener.onRunAdded((Run) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
        if (response == null) {
            listener.onFailure(null, 0, "");
        }
    }

    protected static class RemoveRunTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RemoveListener mListener;
        final String mRunId;

        public RemoveRunTask(String runId, Peloton.RemoveListener listener) {
            this.mRunId = runId;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.removeRunWork(this.mRunId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeRunDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeRunDoCallback(PelotonResponse response, Peloton.RemoveListener listener) {
        Log.d(TAG, "removeRunDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess()) {
                listener.onRemoved();
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class GetRunDataTask extends AsyncTask<Void, Void, PelotonResponse> {
        final String mId;
        final Peloton.RunDataListener mListener;

        public GetRunDataTask(String runId, Peloton.RunDataListener listener) {
            this.mId = runId;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.getRunDataWork(this.mId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.runDataDoCallback(response, this.mListener);
        }
    }

    protected static class SetRunDataTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RunDataListener mListener;
        final Run mRunData;

        public SetRunDataTask(Run runData, Peloton.RunDataListener listener) {
            this.mRunData = runData;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.setRunDataWork(this.mRunData, this.mRunData.RunId, false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.runDataDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runDataDoCallback(PelotonResponse response, Peloton.RunDataListener listener) {
        Log.d(TAG, "runDataDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                response = new PelotonResponse();
            }
            if (response.isServerSuccess() && response.result != null) {
                listener.onRunData((Run) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    static PelotonResponse getTrainingWorkouts() {
        PelotonResponse response = Cloud.getTrainingHeaders();
        ArrayList<TrainingWorkout> trainingWorkouts = new ArrayList<>();
        if (response != null && response.isServerSuccess() && response.result != null) {
            List<TrainingWorkout> headers = (List) response.result;
            Iterator<TrainingWorkout> it = headers.iterator();
            while (true) {
                if (it.hasNext()) {
                    TrainingWorkout header = it.next();
                    response = Cloud.getTrainingData(header.TrainingId);
                    if (response == null || !response.isServerSuccess() || response.result == null) {
                        break;
                    }
                    TrainingWorkout trainingWorkout = (TrainingWorkout) response.result;
                    if (trainingWorkout.TrainingId.isEmpty() || trainingWorkout.TrainingType == null || trainingWorkout.SportType == null) {
                        trainingWorkout.addHeader(header);
                    }
                    trainingWorkouts.add(trainingWorkout);
                } else {
                    response.result = trainingWorkouts;
                    break;
                }
            }
        }
        return response;
    }

    static PelotonResponse removeAllTraining() {
        PelotonResponse response = Cloud.getTrainingHeaders();
        if (response != null && response.isServerSuccess() && response.result != null) {
            List<TrainingWorkout> headers = (List) response.result;
            int deletedCount = 0;
            for (TrainingWorkout header : headers) {
                response = Cloud.removeTraining(header.TrainingId);
                if (response != null && response.isServerSuccess() && response.result != null) {
                    deletedCount++;
                }
            }
            if (response != null) {
                response.result = "Deleted Count : " + deletedCount + " / " + headers.size();
            }
        }
        return response;
    }

    protected static class GetTrainingWorkoutsTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.TrainingsListener mListener;

        GetTrainingWorkoutsTask(Peloton.TrainingsListener listener) {
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return PelotonBase.getTrainingWorkouts();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.getTrainingsDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getTrainingsDoCallback(PelotonResponse response, Peloton.TrainingsListener listener) {
        Log.d(TAG, "trainingsDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                response = new PelotonResponse();
            }
            if (response.isServerSuccess() && response.result != null) {
                listener.onTrainings((List) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class AddTrainingTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.AddTrainingListener mListener;
        final TrainingWorkout training;

        AddTrainingTask(TrainingWorkout training, Peloton.AddTrainingListener listener) {
            this.training = training;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            PelotonResponse response = Cloud.addTrainingHeader(this.training);
            if (response != null && response.isServerSuccess() && response.result != null) {
                TrainingWorkout header = (TrainingWorkout) response.result;
                this.training.TrainingId = header.TrainingId;
                return Cloud.addTrainingData(this.training, false);
            }
            return response;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.addTrainingDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addTrainingDoCallback(PelotonResponse response, Peloton.AddTrainingListener listener) {
        Log.d(TAG, "trainingsDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                response = new PelotonResponse();
            }
            if (response.isServerSuccess() && response.result != null) {
                listener.onTrainingAdded((TrainingWorkout) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class RemoveTrainingTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RemoveListener mListener;
        final String trainingId;

        RemoveTrainingTask(String trainingId, Peloton.RemoveListener listener) {
            this.trainingId = trainingId;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return Cloud.removeTraining(this.trainingId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeTrainingDoCallback(response, this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeTrainingDoCallback(PelotonResponse response, Peloton.RemoveListener listener) {
        Log.d(TAG, "removeTrainingDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response.isServerSuccess()) {
                listener.onRemoved();
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeAllTrainingDoCallback(PelotonResponse response, Peloton.RemoveListener listener) {
        Log.d(TAG, "removeAllTrainingDoCallback listener is null " + (listener == null));
        if (listener != null) {
            if (response == null) {
                response = new PelotonResponse();
            }
            if (response.isServerSuccess() && response.result != null) {
                listener.onFailure(null, 200, (String) response.result);
            } else {
                listener.onFailure(response.serverFailure, response.httpCode, response.rawResponse);
            }
        }
    }

    protected static class RemoveAllTrainingTask extends AsyncTask<Void, Void, PelotonResponse> {
        final Peloton.RemoveListener mListener;

        RemoveAllTrainingTask(Peloton.RemoveListener listener) {
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public PelotonResponse doInBackground(Void... params) {
            return PelotonBase.removeAllTraining();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(PelotonResponse response) {
            PelotonBase.removeAllTrainingDoCallback(response, this.mListener);
        }
    }
}
