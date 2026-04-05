package com.kopin.peloton.ride;

import android.location.Location;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public class Route extends Activity {
    public double Distance;
    public long Duration;
    public String RouteId;
    public ArrayList<WayPoint> WayPoints = new ArrayList<>();

    public Route() {
    }

    public Route(String name, long duration, double distance) {
        this.Name = name;
        this.Duration = duration;
        this.Distance = distance;
    }

    public void addWayPoint(WayPoint wayPoint) {
        this.WayPoints.add(wayPoint);
    }

    public List<Location> getLocations() {
        List<Location> list = new ArrayList<>();
        for (WayPoint point : this.WayPoints) {
            if (point != null && point.Position != null && point.Position.Latitude != -2.147483648E9d) {
                Location location = new Location("");
                location.setLatitude(point.Position.Latitude);
                location.setLongitude(point.Position.Longitude);
                if (point.Position.Altitude != -2.147483648E9d) {
                    location.setAltitude(point.Position.Altitude);
                }
                list.add(location);
            }
        }
        return list;
    }
}
