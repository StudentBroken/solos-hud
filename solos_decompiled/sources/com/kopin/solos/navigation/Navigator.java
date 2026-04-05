package com.kopin.solos.navigation;

import android.location.Location;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.RideControl;
import com.kopin.solos.core.R;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.gps.LocationHandler;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.DataListener;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.FakeRideData;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class Navigator {
    static String DESTINATION_AHEAD = null;
    static String DESTINATION_REACHED = null;
    static String OFF_ROUTE = null;
    private static final String TAG = "SolosNav";
    static Navigation mNavPages;
    private static Navigator self;
    private HardwareReceiverService mService;
    private NavigationRoute navigationRoute;
    private RouteFollower routeFollower;
    private final NavigationRoute.RouteObserver mRouteObserver = new NavigationRoute.RouteObserver() { // from class: com.kopin.solos.navigation.Navigator.1
        @Override // com.kopin.solos.navigate.geolocation.NavigationRoute.RouteObserver
        public void onRouteCalculated() {
            NavLog.d(Navigator.TAG, "New route calculated.");
        }
    };
    private final RideControl.RideObserver mRideListener = new RideControl.RideObserver() { // from class: com.kopin.solos.navigation.Navigator.2
        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideIdle() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideReady(RideControl.StartMode startMode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStarted(Workout.RideMode mode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public boolean okToStop() {
            return true;
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStopped(SavedWorkout ride) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRidePaused(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideResumed(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onNewLap(double lastDistance, long lastTime) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownStarted() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdown(int counter) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownComplete(boolean wasCancelled) {
        }
    };
    private final DataListener mDataListener = new DataListener() { // from class: com.kopin.solos.navigation.Navigator.3
        @Override // com.kopin.solos.storage.DataListener
        public void onSpeed(double speed) {
            super.onSpeed(speed);
            Navigator.this.updateSpeed(speed);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onLocation(Location location) {
            super.onLocation(location);
            Navigator.this.updateLocation(location);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onButtonPress(Sensor.ButtonAction action) {
            super.onButtonPress(action);
        }
    };

    private Navigator(HardwareReceiverService service) {
        this.mService = service;
        service.addDataListener(this.mDataListener);
        RideControl.registerObserver(this.mRideListener);
        Compass.init(this.mService.getResources());
        DESTINATION_AHEAD = service.getString(R.string.destination_ahead);
        DESTINATION_REACHED = service.getString(R.string.destination_reached);
        OFF_ROUTE = service.getString(R.string.msg_off_route);
    }

    public static void init(HardwareReceiverService service) {
        self = new Navigator(service);
    }

    public static boolean isNavScreenShowing() {
        return LiveRide.isNavigtionRideMode() && Prefs.navigationIsFullScreen();
    }

    public static String getScreenToShow() {
        return mNavPages.getCurrentPageId();
    }

    public static void setRoute(NavigationRoute route, Coordinate startPos) {
        if (self != null) {
            self.navigationRoute = route;
            self.routeFollower = new RouteFollower(route, startPos.getLocation(false));
            mNavPages.setRoute(self.routeFollower);
            RideControl.setNavigationRouteId(route.getId());
            NavLog.start(self.mService);
            NavLog.d(TAG, "Route details: ");
            for (Waypoint p : route.getRoute()) {
                NavLog.d(TAG, "  " + p.toString());
            }
            FakeRideData.followRoute(route.getRoute());
        }
    }

    public static void cancel() {
        if (self != null) {
            mNavPages.markRideStopped();
            RideControl.setNavigationRouteId(-1L);
            self.navigationRoute = null;
            FakeRideData.followRoute(null);
            NavLog.stop();
        }
    }

    public static List<Coordinate> getRouteCoordinates() {
        return self.navigationRoute == null ? new ArrayList() : self.navigationRoute.getPreProcessRoute();
    }

    public static List<Waypoint> getWaypoints() {
        return self.navigationRoute == null ? new ArrayList() : self.navigationRoute.getRoute();
    }

    public static Waypoint getNextWaypoint() {
        if (self.routeFollower == null) {
            return null;
        }
        return self.routeFollower.nextWaypoint();
    }

    public static boolean isLocationEnabled() {
        return (self == null || self.mService == null || !self.mService.isGPSEnabled()) ? false : true;
    }

    public static boolean hasRecentLocation() {
        return self.mService.hasRecentLocation();
    }

    public static void startLocationUpdates(LocationHandler.InternalLocationListener cb) {
        self.mService.addLocationListener(cb);
        self.mService.prepareLocation();
    }

    public static void stopLocationUpdates(LocationHandler.InternalLocationListener cb) {
        self.mService.stopLocation();
        self.mService.removeLocationListener(cb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocation(Location loc) {
        if (this.navigationRoute != null) {
            NavLog.d(TAG, "New Location: " + loc);
            boolean isOffRoute = !this.routeFollower.isOnRoute();
            boolean updateInstruction = this.routeFollower.updateLocation(loc);
            if (updateInstruction) {
                if (this.routeFollower.routeCompleted()) {
                    mNavPages.signalDestination();
                } else if (!isOffRoute && !this.routeFollower.isOnRoute()) {
                    mNavPages.signalOffRoute();
                } else {
                    mNavPages.showManeuver();
                }
            }
            if (this.routeFollower.shouldReRoute() && Utility.isNetworkAvailable(this.mService)) {
                NavLog.d(TAG, "Off route for too long/far, calculating new route...");
                mNavPages.signalReRouting();
                this.navigationRoute.calculateRoute(loc, this.mRouteObserver);
            } else {
                mNavPages.updateDistanceAndTime();
                mNavPages.updateCompass();
            }
            mNavPages.updateMap(loc);
            if (isNavScreenShowing()) {
                mNavPages.refreshPage();
            }
            NavLog.d(TAG, "  distance to next waypoint now " + this.routeFollower.distanceToNextWaypoint());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeed(double speed) {
        if (this.navigationRoute != null) {
            NavLog.d(TAG, "New Speed: " + speed);
            this.routeFollower.updateSpeed(speed);
            mNavPages.updateSpeed();
            NavLog.d(TAG, "  time to next waypoint now " + (this.routeFollower.distanceToNextWaypoint() / speed));
        }
    }
}
