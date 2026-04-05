package com.kopin.solos.navigate.geolocation;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.kopin.solos.navigate.apimodels.googledirectionsapimodel.DirectionResults;
import com.kopin.solos.navigate.communication.CallAPI;
import com.kopin.solos.navigate.communication.tasks.DirectionsTask;
import com.kopin.solos.navigate.helperclasses.CoordCalc;
import com.kopin.solos.storage.Coordinate;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class NavigationRoute {
    private static final String TAG = NavigationRoute.class.getSimpleName();
    public static final String TRAVEL_MODE_BICYCLING = "bicycling";
    public static final String TRAVEL_MODE_WALKING = "walking";
    private String mDestination;
    private List<Coordinate> mPreProcessRoute;
    private long routeId;
    private List<Waypoint> mRoute = new ArrayList();
    private boolean hasDirections = false;
    private int totalTimeInSeconds = 0;
    private int totalDistanceInMeters = 0;
    private String mTravelMode = TRAVEL_MODE_BICYCLING;

    public interface RouteObserver {
        void onRouteCalculated();
    }

    NavigationRoute(long id, String destination, List<Coordinate> coords) {
        this.mPreProcessRoute = new ArrayList();
        this.routeId = -1L;
        this.routeId = id;
        this.mDestination = destination;
        this.mPreProcessRoute = coords;
    }

    void dump() {
        Log.d(TAG, "Route Id: " + this.routeId + ", destination: " + this.mDestination);
        for (Waypoint wp : this.mRoute) {
            Log.d(TAG, wp.toString());
        }
    }

    public void setId(long id) {
        this.routeId = id;
    }

    public long getId() {
        return this.routeId;
    }

    void setTravelMode(String mode) {
        this.mTravelMode = mode;
    }

    public List<Waypoint> getRoute() {
        return this.mRoute;
    }

    public Coordinate getStartPoint() {
        return (this.mRoute == null || this.mRoute.isEmpty()) ? Coordinate.NONE : this.mRoute.get(0);
    }

    public Waypoint getWaypoint(int idx) {
        if (this.mRoute == null || this.mRoute.isEmpty()) {
            return Waypoint.NONE;
        }
        if (idx >= this.mRoute.size()) {
            return Waypoint.NONE;
        }
        return this.mRoute.get(idx);
    }

    public void setRoute(List<Waypoint> route) {
        this.mRoute = route;
    }

    public void addWaypoint(Waypoint waypoint) {
        this.mRoute.add(waypoint);
    }

    private void addDirectionResult(DirectionResults d) {
        for (Waypoint w : d.getWaypoints()) {
            addWaypoint(w);
        }
    }

    void addDirectionResults(List<DirectionResults> d) {
        for (DirectionResults directionResult : d) {
            addDirectionResult(directionResult);
        }
        setDistance((int) d.get(0).getTotalDistance().value);
        this.hasDirections = true;
    }

    public List<Coordinate> getPreProcessRoute() {
        return this.mPreProcessRoute;
    }

    public List<Coordinate> getRouteSegment(Waypoint from, Waypoint to) {
        List<Coordinate> segment = new ArrayList<>();
        boolean inSegment = false;
        for (Coordinate c : this.mPreProcessRoute) {
            if (to.equals(c)) {
                break;
            }
            if (inSegment) {
                segment.add(c);
            }
            if (from.equals(c)) {
                inSegment = true;
            }
        }
        return segment;
    }

    public List<Coordinate> toCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Waypoint w : this.mRoute) {
            coordinates.add(w);
        }
        return coordinates;
    }

    public boolean hasDestination() {
        return (this.mDestination == null || this.mDestination.isEmpty()) ? false : true;
    }

    public String getDestination() {
        return this.mDestination;
    }

    public boolean hasRoute() {
        return this.mRoute != null && this.mRoute.size() > 0;
    }

    public boolean isLastStep(int idx) {
        return this.mRoute == null || this.mRoute.isEmpty() || this.mRoute.size() + (-1) <= idx;
    }

    public boolean hasDirections() {
        return this.hasDirections;
    }

    public boolean isOnRoute(Location coordinate, int theRadius) {
        for (Coordinate c : this.mPreProcessRoute) {
            double dist = coordinate.distanceTo(c.getLocation(false));
            if (dist <= theRadius) {
                return true;
            }
        }
        return false;
    }

    public void setDistance(int distance) {
        this.totalDistanceInMeters = distance;
    }

    public void setTime(int time) {
        this.totalTimeInSeconds = time;
    }

    public int getDistance() {
        return this.totalDistanceInMeters;
    }

    public int getTime() {
        return this.totalTimeInSeconds;
    }

    public void calculateRoute(Location from, RouteObserver cb) {
        calculateRoute(from.getLatitude(), from.getLongitude(), cb);
    }

    public void calculateRoute(LatLng from, RouteObserver cb) {
        calculateRoute(from.latitude, from.longitude, cb);
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [com.kopin.solos.navigate.geolocation.NavigationRoute$1] */
    private void calculateRoute(double latitude, double longitude, final RouteObserver cb) {
        URL url = null;
        try {
            String theParams = "&origin=" + latitude + "," + longitude + "&destination=" + URLEncoder.encode(this.mDestination, "utf-8");
            url = new URL(CallAPI.GOOGLE_DIRECTION_URL + CallAPI.googleAPIKey + "&mode=" + this.mTravelMode + theParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        new DirectionsTask(url) { // from class: com.kopin.solos.navigate.geolocation.NavigationRoute.1
            @Override // android.os.AsyncTask
            public void onPostExecute(List<DirectionResults> results) {
                if (!isCancelled()) {
                    if (results != null && !results.isEmpty() && !results.get(0).theRoute.isEmpty()) {
                        NavigationRoute.this.updateRoute(results.get(0));
                        NavigationRoute.this.addDirectionResults(results);
                        for (Waypoint wp : NavigationRoute.this.mRoute) {
                            Log.d(NavigationRoute.TAG, wp.toString());
                        }
                    }
                    cb.onRouteCalculated();
                }
            }
        }.execute(new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRoute(DirectionResults result) {
        this.mPreProcessRoute = result.getPolyLine();
        this.totalDistanceInMeters = (int) result.getTotalDistance().value;
        this.totalTimeInSeconds = (int) result.getTotalDuration().value;
    }

    public int findClosestCoordinate(Location from) {
        return CoordCalc.findClosestCoordinate(from, this.mPreProcessRoute);
    }

    public int findClosestWaypointInFront(Location from, float heading) {
        if (this.mRoute == null || this.mRoute.isEmpty()) {
            return -1;
        }
        int closest = -1;
        double lastDist = 0.0d;
        double lastBearing = heading;
        for (int i = 0; i < this.mRoute.size(); i++) {
            Waypoint wp = this.mRoute.get(i);
            double dist = CoordCalc.GetDistance(from, wp);
            if (lastDist == 0.0d || dist < lastDist) {
                closest = i;
                lastDist = dist;
                lastBearing = 180.0f - CoordCalc.GetBearing(wp, from);
            }
        }
        Math.abs(((double) heading) - lastBearing);
        return closest;
    }

    public Waypoint rejoinRoute(int wp, Location from, float heading) {
        Waypoint tmp = new Waypoint(this.mRoute.get(wp));
        if (wp + 1 == this.mRoute.size()) {
            Log.d(TAG, "rejoining route at destination");
        } else {
            Log.d(TAG, "rejoin route at WP: " + wp + " (" + tmp + ") current pos: " + from + ", heading: " + heading);
            CoordCalc.GetDistance(from, tmp);
            int[] results = CoordCalc.GetDistanceResults(from, tmp.toLocation());
            Log.d(TAG, "  new WP is " + results[0] + " mark " + results[1]);
            Waypoint next = this.mRoute.get(wp + 1);
            Log.d(TAG, "  next WP is " + next);
            DirectionContainer directionContainer = TurnBasedDirections.FindDirection(results[1], next.bearing);
            tmp.instruction = directionContainer.instruction;
            tmp.maneuver = directionContainer.instruction;
        }
        return tmp;
    }
}
