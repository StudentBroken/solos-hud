package com.kopin.solos.navigate.geolocation;

import android.util.Log;
import com.kopin.solos.navigate.apimodels.googledirectionsapimodel.DirectionResults;
import com.kopin.solos.navigate.geolocation.TurnBasedDirections;
import com.kopin.solos.navigate.helperclasses.CoordCalc;
import com.kopin.solos.navigate.helperclasses.LocationHelper;
import com.kopin.solos.navigate.helperclasses.RouteHelper;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.SQLHelper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class RouteBuilder {
    private static final boolean CONFIG_ENABLE_PRE_SMOOTHING = true;
    public static final double MAX_ANGLE_TOLERANCE = 15.0d;
    public static final double MAX_CUMULATIVE_ANGLE_TOLERANCE = 40.0d;
    public static final int MAX_DISTANCE_SHORT_TRIP = 25;
    public static final int MAX_ELEVATION_TOLERANCE = 10;
    public static final int MIN_DISTANCE_DISTINCT_WP = 10;
    public static final int MIN_DISTANCE_SIMILAR_WP = 15;
    public static final double NEW_MAX_ANGLE_TOLERANCE = 10.0d;
    private static final String TAG = "NavRouteBuilder";

    public static NavigationRoute create(String dest, String mode) {
        NavigationRoute self = new NavigationRoute(-1L, dest, null);
        self.setTravelMode(mode);
        return self;
    }

    public static NavigationRoute create(List<Coordinate> coords, List<DirectionResults> directions, String destination) {
        NavigationRoute self = new NavigationRoute(-1L, destination, coords);
        self.addDirectionResults(directions);
        self.dump();
        return self;
    }

    public static NavigationRoute create(List<Coordinate> coords) {
        return create(-1L, coords);
    }

    public static NavigationRoute create(long id, List<Coordinate> coords) {
        List<Coordinate> coords2 = RouteHelper.smoothCoords(coords);
        NavigationRoute self = new NavigationRoute(id, null, coords2);
        if (coords2.size() > 1) {
            createTurnBasedDirections(self, coords2);
            self.dump();
        }
        return self;
    }

    public static NavigationRoute load(long routeId) {
        List<Coordinate> coords = SQLHelper.getRouteDetails(false, routeId);
        return create(routeId, coords);
    }

    private static boolean checkBearing(int bearing) {
        return RouteHelper.checkBearing(bearing, 15.0d);
    }

    private static List<Waypoint> generateWaypoints(List<Coordinate> coords) {
        return null;
    }

    private static void createTurnBasedDirections(NavigationRoute route, List<Coordinate> coords) {
        List<Waypoint> convertedList = RouteHelper.createWaypoints(coords);
        route.setRoute(generateTurnInstructions(convertedList));
    }

    private static void setInitialWaypoint(Waypoint start, Coordinate next) {
        int[] results = CoordCalc.GetDistanceResults(LocationHelper.location(start), LocationHelper.location(next));
        start.isStart = true;
        start.instruction = TurnBasedDirections.GetCompassDirection(results[1]);
        start.maneuver = "";
    }

    private static void setDirectionForWaypoint(Waypoint current, Coordinate next) {
        int[] results = CoordCalc.GetDistanceResults(LocationHelper.location(current), LocationHelper.location(next));
        DirectionContainer directionContainer = TurnBasedDirections.FindDirection(current.bearing, results[1]);
        current.instruction = directionContainer.instruction;
        current.maneuver = directionContainer.instruction;
    }

    private static List<Waypoint> generateTurnInstructions(List<Waypoint> initialWaypoints) {
        Log.d(TAG, "generateTurnInstructions");
        List<Waypoint> waypoints = new ArrayList<>();
        if (initialWaypoints.size() >= 2) {
            Waypoint wp = initialWaypoints.get(0);
            Waypoint nextWp = initialWaypoints.get(1);
            wp.isStart = true;
            waypoints.add(wp);
            double cumulativeBearing = 0.0d;
            double cumulativeDistance = nextWp.distance;
            for (int i = 1; i < initialWaypoints.size() - 1; i++) {
                Waypoint wp2 = initialWaypoints.get(i);
                nextWp = initialWaypoints.get(i + 1);
                double turnBearing = RouteHelper.diffBearing(nextWp.bearing, wp2.bearing);
                Log.d(TAG, "WP: move " + nextWp.distance + " mark " + nextWp.bearing + " heading now: " + turnBearing);
                if (Math.abs(turnBearing) > 30.0d) {
                    Log.d(TAG, " + turn detected - bearing delta: " + turnBearing + " (" + TurnBasedDirections.GetTurn((int) turnBearing) + ")");
                    waypoints.add(wp2);
                    cumulativeBearing = 0.0d;
                    cumulativeDistance = 0.0d;
                } else {
                    cumulativeBearing += turnBearing;
                    cumulativeDistance += (double) nextWp.distance;
                    if (cumulativeDistance > 125.0d) {
                        Log.d(TAG, "  no turn (" + cumulativeBearing + ") detected for " + cumulativeDistance + " metres, continue ahead.");
                        cumulativeDistance = 0.0d;
                        cumulativeBearing = 0.0d;
                    } else if (Math.abs(cumulativeBearing) > 40.0d) {
                        Log.d(TAG, " + cumulative turn detected - bearing delta: " + cumulativeBearing + " (" + TurnBasedDirections.GetTurn((int) cumulativeBearing) + ")");
                        wp2.distance = (int) cumulativeDistance;
                        wp2.bearing = (int) cumulativeBearing;
                        waypoints.add(wp2);
                        cumulativeBearing = 0.0d;
                        cumulativeDistance = 0.0d;
                    }
                }
            }
            nextWp.isEnd = true;
            waypoints.add(nextWp);
            MeasureCoords(waypoints);
            boolean removedInstruction = false;
            for (int i2 = waypoints.size() - 2; i2 > 2; i2--) {
                Waypoint wp3 = waypoints.get(i2);
                Waypoint prevWp = waypoints.get(i2 - 1);
                if (prevWp.distance < 25) {
                    Log.d(TAG, "cull WPs: check " + wp3.toString() + " and " + prevWp.toString());
                    if (wp3.sameDirection(prevWp)) {
                        Log.d(TAG, "  same direction, removing: " + wp3.toString());
                        waypoints.remove(i2);
                        removedInstruction = true;
                    }
                }
            }
            if (removedInstruction) {
                MeasureCoords(waypoints);
            }
        }
        return waypoints;
    }

    private static void generateTurnInstruction(Waypoint sourceWaypoint, Waypoint destinationWaypoint) {
        int[] results = CoordCalc.GetDistanceResults(LocationHelper.location(sourceWaypoint), LocationHelper.location(destinationWaypoint));
        destinationWaypoint.distance = results[0];
        destinationWaypoint.bearing = results[1];
        DirectionContainer directionContainer = TurnBasedDirections.FindDirection(sourceWaypoint.bearing, destinationWaypoint.bearing);
        sourceWaypoint.instruction = directionContainer.instruction;
        sourceWaypoint.maneuver = directionContainer.instruction;
    }

    private static void MeasureCoords(List<Waypoint> route) {
        if (route.size() > 1) {
            Waypoint sourceWaypoint = null;
            for (Waypoint destinationWaypoint : route) {
                if (sourceWaypoint != null) {
                    int[] results = CoordCalc.GetDistanceResults(LocationHelper.location(sourceWaypoint), LocationHelper.location(destinationWaypoint));
                    destinationWaypoint.distance = results[0];
                    destinationWaypoint.bearing = results[1];
                    if (sourceWaypoint.isStart) {
                        sourceWaypoint.instruction = TurnBasedDirections.GetCompassDirection(destinationWaypoint.bearing);
                        sourceWaypoint.maneuver = "";
                        sourceWaypoint.turn = TurnBasedDirections.Turn.STRAIGHT_ON;
                    } else {
                        DirectionContainer directionContainer = TurnBasedDirections.FindDirection(sourceWaypoint.bearing, destinationWaypoint.bearing);
                        sourceWaypoint.instruction = directionContainer.instruction;
                        sourceWaypoint.maneuver = directionContainer.instruction;
                        sourceWaypoint.turn = TurnBasedDirections.Turn.fromValues(sourceWaypoint.bearing, destinationWaypoint.bearing);
                    }
                    if (destinationWaypoint.isEnd) {
                        destinationWaypoint.instruction = "Destination";
                        destinationWaypoint.maneuver = "Destination";
                        destinationWaypoint.turn = TurnBasedDirections.Turn.STRAIGHT_ON;
                    }
                }
                sourceWaypoint = destinationWaypoint;
            }
        }
    }

    private static List<Waypoint> TrimList(List<Waypoint> initialList) {
        ArrayList<Waypoint> newTrimmedList = new ArrayList<>();
        ArrayList<Waypoint> clonedRoute = new ArrayList<>(initialList);
        Coordinate tempLL = null;
        int currentBearing = 0;
        int distanceTravelled = 0;
        for (Waypoint x : clonedRoute) {
            Waypoint c = new Waypoint(x);
            if (c.isStart) {
                currentBearing = c.bearing;
                newTrimmedList.add(c);
                Log.d(TAG, "Route Start: " + c.toString());
            } else if (c.isEnd) {
                newTrimmedList.add(c);
                Log.d(TAG, "Route End: " + c.toString());
            } else {
                int[] results = CoordCalc.GetDistanceResults(LocationHelper.location(tempLL), LocationHelper.location(c));
                int destinationBearing = results[1];
                distanceTravelled += results[0];
                int myBearing = currentBearing - destinationBearing;
                if (destinationBearing > currentBearing) {
                    myBearing = destinationBearing - currentBearing;
                }
                Log.d(TAG, String.format(" next Coordinate: distance = %d, next bearing = %d (%d)", Integer.valueOf(distanceTravelled), Integer.valueOf(myBearing), Integer.valueOf(destinationBearing)));
                if (distanceTravelled > 25 && (checkBearing(myBearing) || checkBearing(destinationBearing))) {
                    Waypoint t = newTrimmedList.get(newTrimmedList.size() - 1);
                    Log.d(TAG, " new Waypoint: " + t.toString());
                    int[] newResults = CoordCalc.GetDistanceResults(LocationHelper.location(t), LocationHelper.location(c));
                    int fullBearing = newResults[1];
                    Log.d(TAG, String.format("  fullBearing = %d, destinationBearing = %d", Integer.valueOf(myBearing), Integer.valueOf(destinationBearing)));
                    int myBearing2 = destinationBearing - fullBearing;
                    if (fullBearing > destinationBearing) {
                        myBearing2 = fullBearing - destinationBearing;
                    }
                    if (checkBearing(myBearing2)) {
                        Log.d(TAG, " Adding to waypoint list");
                        newTrimmedList.add(c);
                        currentBearing = destinationBearing;
                        distanceTravelled = 0;
                    }
                } else {
                    Log.d(TAG, "  Not reached bearing or distance threshold yet");
                }
            }
            tempLL = x;
        }
        return new ArrayList(newTrimmedList);
    }
}
