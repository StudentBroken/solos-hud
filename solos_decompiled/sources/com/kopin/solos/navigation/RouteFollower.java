package com.kopin.solos.navigation;

import android.location.Location;
import android.util.Log;
import com.kopin.solos.navigate.geolocation.DirectionContainer;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigate.helperclasses.CoordCalc;
import com.kopin.solos.storage.Coordinate;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class RouteFollower {
    private static final int OFF_ROUTE_DISTANCE = 100;
    private static final long OFF_ROUTE_TIME = 30000;
    static final int ROUTE_WIDTH = 40;
    private static final String TAG = "NavRouteFollower";
    static final int WAYPOINT_SIZE = 30;
    static final int WAYPOINT_WIDTH = 20;
    private double TimeToGoal;
    private DirectionContainer currentInfo;
    private Location currentLocation;
    private double distanceToGoal;
    private double distanceToWaypoint;
    private Location lastLocation;
    private long mOffRouteTime;
    private final NavigationRoute navigationRoute;
    List<Coordinate> nextSegment;
    private Waypoint nextWaypoint;
    private double timeToWaypoint;
    private List<Location> mLastTenList = new ArrayList();
    private int lastWaypointIdx = 0;
    private int nextWaypointIdx = 0;
    private boolean isShowingInitialInstruction = true;
    private boolean isFinalLeg = false;
    private boolean isAtDestination = false;
    private boolean isWithinWaypoint = false;
    private double currentSpeed = 0.0d;

    RouteFollower(NavigationRoute route, Location curLoc) {
        this.navigationRoute = route;
        this.currentLocation = curLoc;
        this.nextWaypoint = this.navigationRoute.getWaypoint(this.nextWaypointIdx);
    }

    NavigationRoute currentRoute() {
        return this.navigationRoute;
    }

    boolean updateLocation(Location location) {
        this.lastLocation = this.currentLocation;
        this.currentLocation = location;
        Log.d(TAG, String.format("Moved from %.3f, %.3f to %.3f, %.3f bearing now %.2f", Double.valueOf(this.lastLocation.getLatitude()), Double.valueOf(this.lastLocation.getLongitude()), Double.valueOf(this.currentLocation.getLatitude()), Double.valueOf(this.currentLocation.getLongitude()), Float.valueOf(this.lastLocation.bearingTo(this.currentLocation))));
        this.mLastTenList.add(this.lastLocation);
        if (this.mLastTenList.size() >= 10) {
            this.mLastTenList.remove(0);
        }
        return takeStep();
    }

    void updateSpeed(double speed) {
        this.currentSpeed = (int) speed;
    }

    boolean isMoving() {
        return this.currentSpeed > 0.0d && this.currentSpeed != -2.147483648E9d;
    }

    boolean isOnRoute() {
        return this.mOffRouteTime == 0;
    }

    boolean checkOnRoute() {
        return this.navigationRoute.isOnRoute(this.currentLocation, 40);
    }

    boolean shouldReRoute() {
        if (isOnRoute() || !this.navigationRoute.hasDestination() || isOnRoute()) {
            return false;
        }
        return this.distanceToWaypoint > 100.0d || System.currentTimeMillis() - this.mOffRouteTime > OFF_ROUTE_TIME;
    }

    boolean routeCompleted() {
        return this.isAtDestination;
    }

    String currentManeuver() {
        if (this.isShowingInitialInstruction) {
            return this.navigationRoute.getWaypoint(0).instruction;
        }
        if (this.isAtDestination) {
            return Navigator.DESTINATION_REACHED;
        }
        if (this.isFinalLeg) {
            return Navigator.DESTINATION_AHEAD;
        }
        if (!isOnRoute()) {
            return Navigator.OFF_ROUTE;
        }
        return this.nextWaypoint.maneuver;
    }

    String currentInstruction() {
        if (this.isShowingInitialInstruction) {
            return this.navigationRoute.getWaypoint(0).instruction;
        }
        if (this.isAtDestination) {
            return Navigator.DESTINATION_REACHED;
        }
        if (this.isFinalLeg) {
            return Navigator.DESTINATION_AHEAD;
        }
        if (!isOnRoute()) {
            return Navigator.OFF_ROUTE;
        }
        return this.nextWaypoint.instruction;
    }

    Waypoint nextWaypoint() {
        return this.nextWaypoint;
    }

    double currentSpeed() {
        return this.currentSpeed;
    }

    int timeToNextWaypoint() {
        if (isMoving() && this.distanceToWaypoint != -2.147483648E9d) {
            return (int) (this.distanceToWaypoint / this.currentSpeed);
        }
        return Integer.MIN_VALUE;
    }

    double distanceToNextWaypoint() {
        return this.distanceToWaypoint;
    }

    private void updateDistanceToNextWaypoint() {
        if (this.nextWaypoint == null || this.currentLocation == null) {
            this.distanceToWaypoint = -2.147483648E9d;
            return;
        }
        double straightLine = CoordCalc.GetDistance(this.nextWaypoint.toLocation(), this.currentLocation);
        if (this.nextSegment == null || this.nextSegment.isEmpty()) {
            this.distanceToWaypoint = straightLine;
            return;
        }
        int curIdx = CoordCalc.findClosestCoordinate(this.currentLocation, this.nextSegment);
        Coordinate c1 = this.nextSegment.get(curIdx);
        double cumulative = CoordCalc.GetDistance(this.currentLocation, c1);
        for (int i = curIdx + 1; i < this.nextSegment.size(); i++) {
            Coordinate c2 = this.nextSegment.get(i);
            cumulative += (double) CoordCalc.GetDistance(c1, c2);
            c1 = c2;
        }
        Log.d(TAG, String.format("Distance to next WP: %.2f (%.2f straight line)", Double.valueOf(cumulative), Double.valueOf(straightLine)));
        this.distanceToWaypoint = cumulative;
    }

    double getStepDistance() {
        float[] stepData = CoordCalc.GetDetails(this.lastLocation, this.currentLocation);
        return stepData[0];
    }

    float getHeading() {
        if (this.currentLocation == null || this.lastLocation == null) {
            return 0.0f;
        }
        return this.lastLocation.bearingTo(this.currentLocation);
    }

    float bearingToNextWaypoint() {
        return CoordCalc.GetBearing(this.nextWaypoint, this.currentLocation);
    }

    float bearingToFollowingWaypoint() {
        if (isApproachingDestination()) {
            return bearingToNextWaypoint();
        }
        Waypoint wp = this.navigationRoute.getWaypoint(this.nextWaypointIdx + 1);
        return CoordCalc.GetBearing(wp, this.currentLocation);
    }

    boolean isApproachingDestination() {
        return this.navigationRoute.isLastStep(this.nextWaypointIdx);
    }

    private boolean takeStep() {
        boolean updateInstruction = false;
        if (!checkOnRoute()) {
            if (this.mOffRouteTime == 0) {
                Log.d(TAG, "Off route detected!");
                this.mOffRouteTime = System.currentTimeMillis();
                return true;
            }
            Log.d(TAG, "Still off route. (" + (System.currentTimeMillis() - this.mOffRouteTime) + "ms)");
            return false;
        }
        if (this.mOffRouteTime > 0) {
            Log.d(TAG, "Rejoined route, find next Waypoint..");
            this.nextWaypointIdx = this.navigationRoute.findClosestWaypointInFront(this.currentLocation, getHeading());
            this.nextWaypoint = this.navigationRoute.rejoinRoute(this.nextWaypointIdx, this.currentLocation, getHeading());
            if (this.nextSegment != null) {
                this.nextSegment.clear();
            }
            updateInstruction = true;
        }
        this.mOffRouteTime = 0L;
        updateDistanceToNextWaypoint();
        double lastDistance = CoordCalc.GetDistance(this.nextWaypoint.toLocation(), this.lastLocation);
        double curDistance = CoordCalc.GetDistance(this.nextWaypoint.toLocation(), this.currentLocation);
        if (this.distanceToWaypoint <= 30.0d || this.isWithinWaypoint) {
            this.isWithinWaypoint = true;
            Log.d(TAG, String.format("Within Waypoint radius, last distance: %.2fm current distance: %.2fm", Double.valueOf(lastDistance), Double.valueOf(curDistance)));
            if (curDistance >= lastDistance) {
                if (this.isShowingInitialInstruction) {
                    Log.d(TAG, " Route started!");
                    this.isShowingInitialInstruction = false;
                    updateInstruction = true;
                } else if (this.isFinalLeg) {
                    Log.d(TAG, " Destination reached!");
                    this.isAtDestination = true;
                    updateInstruction = true;
                } else if (this.navigationRoute.isOnRoute(this.currentLocation, 20)) {
                    Log.d(TAG, " Move to next Waypoint");
                    moveToNextWaypoint();
                    updateInstruction = true;
                }
            }
        } else if (curDistance > lastDistance) {
            Log.d(TAG, String.format("Outside Waypoint radius and moving away!, last distance: %.2fm current distance: %.2fm", Double.valueOf(lastDistance), Double.valueOf(curDistance)));
            if (!this.nextWaypoint.isEnd) {
                Waypoint fwp = this.navigationRoute.getWaypoint(this.nextWaypointIdx + 1);
                double lastDist2 = CoordCalc.GetDistance(fwp.toLocation(), this.lastLocation);
                double curDist2 = CoordCalc.GetDistance(fwp.toLocation(), this.currentLocation);
                if (curDist2 < lastDist2) {
                    Log.d(TAG, "Heading to following WP... skip?");
                } else {
                    Log.d(TAG, "Potentially heading the wrong way!");
                    this.mOffRouteTime = System.currentTimeMillis();
                    updateInstruction = true;
                }
            }
        }
        return updateInstruction;
    }

    private void moveToNextWaypoint() {
        this.isWithinWaypoint = false;
        if (this.navigationRoute.hasRoute() && !this.navigationRoute.isLastStep(this.nextWaypointIdx)) {
            this.nextWaypointIdx++;
            Waypoint lastWp = this.nextWaypoint;
            this.nextWaypoint = this.navigationRoute.getWaypoint(this.nextWaypointIdx);
            Log.d(TAG, "Next Waypoint: " + this.nextWaypoint);
            this.nextSegment = this.navigationRoute.getRouteSegment(lastWp, this.nextWaypoint);
            updateDistanceToNextWaypoint();
        }
        this.currentInfo = DirectionContainer.CurrentStep(this.nextWaypoint, this.currentLocation);
        this.isFinalLeg = this.navigationRoute.isLastStep(this.nextWaypointIdx);
    }
}
