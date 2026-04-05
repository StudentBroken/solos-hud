package com.kopin.solos;

import com.kopin.solos.RideControl;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;

/* JADX INFO: loaded from: classes37.dex */
public class FTP {
    private static final RideControl.RideObserver sRideObserver = new RideControl.RideObserver() { // from class: com.kopin.solos.FTP.1
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
            FTP.stop(false);
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

    public static void start() {
        LiveRide.setMode(Workout.RideMode.FTP, -1L);
        RideControl.ready(false);
        RideControl.registerObserver(sRideObserver);
    }

    public static void stop(boolean discard) {
        RideControl.stop(discard, sRideObserver);
    }
}
