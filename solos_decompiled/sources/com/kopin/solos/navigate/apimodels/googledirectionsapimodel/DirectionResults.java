package com.kopin.solos.navigate.apimodels.googledirectionsapimodel;

import com.google.gson.annotations.SerializedName;
import com.kopin.solos.navigate.apimodels.sharedmodel.AbstractModel;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleDistance;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleLatLng;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleTime;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigate.helperclasses.CoordinateHelper;
import com.kopin.solos.storage.Coordinate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class DirectionResults extends AbstractModel implements Serializable {

    @SerializedName("routes")
    public List<DirectionRoute> theRoute = null;

    public GoogleDistance getTotalDistance() {
        return this.theRoute.get(0).legList.get(0).totalDistance;
    }

    public GoogleTime getTotalDuration() {
        return this.theRoute.get(0).legList.get(0).totalTime;
    }

    public List<Coordinate> getList() {
        List<Coordinate> list = new ArrayList<>();
        boolean isFirst = true;
        for (DirectionRoute r : this.theRoute) {
            for (DirectionsLeg l : r.legList) {
                for (DirectionsStep s : l.stepList) {
                    if (isFirst) {
                        list.add(s.start.toWaypoint());
                        isFirst = false;
                    }
                    list.add(s.end.toWaypoint());
                }
            }
        }
        return list;
    }

    public List<Coordinate> getPolyLine() {
        List<Coordinate> list = new ArrayList<>();
        for (DirectionRoute r : this.theRoute) {
            for (Coordinate coordinate : CoordinateHelper.decodePoly(r.polyLine.polyLinePointsEncoded)) {
                list.add(coordinate);
            }
        }
        return list;
    }

    public List<Waypoint> getWaypoints() {
        boolean isFirst = true;
        List<Waypoint> records = new ArrayList<>();
        for (DirectionRoute r : this.theRoute) {
            for (DirectionsLeg l : r.legList) {
                for (DirectionsStep s : l.stepList) {
                    s.normalise();
                    if (s.maneuver == null) {
                        s.maneuver = "";
                    }
                    if (isFirst) {
                        records.add(StepToWaypoint(s.start, s.instructions, 0, s.maneuver));
                        isFirst = false;
                    }
                    records.add(StepToWaypoint(s.end, s.instructions, (int) s.distance.value, s.maneuver));
                }
            }
        }
        return records;
    }

    public Waypoint StepToWaypoint(GoogleLatLng s, String instruction, int distance) {
        Waypoint w = new Waypoint(new Coordinate(s.gLat, s.gLng));
        w.instruction = instruction;
        w.distance = distance;
        return w;
    }

    public Waypoint StepToWaypoint(GoogleLatLng s, String instruction, int distance, String maneuver) {
        Waypoint w = new Waypoint(new Coordinate(s.gLat, s.gLng));
        w.instruction = instruction;
        w.distance = distance;
        w.maneuver = maneuver;
        return w;
    }
}
