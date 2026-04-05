package com.kopin.solos.navigation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.core.R;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.storage.Coordinate;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes37.dex */
class PathMap {
    private static final int ARROW_SIZE_PIXELS = 18;
    private static final int BITMAP_SIZE = 120;
    private static final int MAP_CENTRE_ARROW_OFFSET = 9;
    private static final int MAP_CENTRE_X = 60;
    private static final int MAP_CENTRE_Y = 80;
    private static final double METRES_PER_DEGREE_LATITUDE = 111321.0d;
    private static final float METRES_PER_PIXEL = 0.25f;
    private static final String TAG = "PathMap";
    private static final int WAYPOINT_SIZE_PIXELS = 6;
    private static final int WAYPOINT_SIZE_PIXELS_DEBUG = 1;
    private Bitmap BITMAP_ARROW;
    private int HEADSET_GREEN;
    private Matrix LATLNG_TO_BITMAP;
    private Paint PAINT_ARROW;
    private Paint PAINT_DEBUG_OFF_ROUTE;
    private Paint PAINT_DEBUG_WAYPOINT;
    private Paint PAINT_ROUTE;
    private Paint PAINT_WAYPOINT;
    private int SOLOS_ORANGE;
    private Coordinate mNextWaypoint;
    private ArrayList<Coordinate> mRoutePoints = new ArrayList<>();

    private static float pixelsForMetres(float metres) {
        return 0.25f * metres;
    }

    PathMap(Resources resources) {
        this.SOLOS_ORANGE = resources.getColor(R.color.navigation_orange);
        this.HEADSET_GREEN = resources.getColor(R.color.navigation_green);
        Bitmap arrow = BitmapFactory.decodeResource(resources, R.drawable.ic_nav_arrow_flat_headset);
        this.BITMAP_ARROW = Bitmap.createScaledBitmap(arrow, 18, 18, true);
        this.PAINT_ROUTE = new Paint();
        this.PAINT_ROUTE.setColor(-1);
        this.PAINT_ROUTE.setStrokeWidth(2.0f);
        this.PAINT_ROUTE.setStyle(Paint.Style.STROKE);
        this.PAINT_ROUTE.setAntiAlias(true);
        this.PAINT_WAYPOINT = new Paint();
        this.PAINT_WAYPOINT.setColor(this.HEADSET_GREEN);
        this.PAINT_WAYPOINT.setStrokeWidth(2.0f);
        this.PAINT_WAYPOINT.setStyle(Paint.Style.FILL);
        this.PAINT_WAYPOINT.setAntiAlias(true);
        this.PAINT_DEBUG_WAYPOINT = new Paint();
        this.PAINT_DEBUG_WAYPOINT.setColor(this.HEADSET_GREEN);
        this.PAINT_DEBUG_WAYPOINT.setStrokeWidth(1.0f);
        this.PAINT_DEBUG_WAYPOINT.setStyle(Paint.Style.STROKE);
        this.PAINT_DEBUG_WAYPOINT.setAntiAlias(true);
        this.PAINT_DEBUG_OFF_ROUTE = new Paint();
        this.PAINT_DEBUG_OFF_ROUTE.setColor(this.SOLOS_ORANGE);
        this.PAINT_DEBUG_OFF_ROUTE.setStrokeWidth(1.0f);
        this.PAINT_DEBUG_OFF_ROUTE.setStyle(Paint.Style.STROKE);
        this.PAINT_DEBUG_OFF_ROUTE.setAntiAlias(true);
        this.PAINT_ARROW = new Paint();
        this.PAINT_ARROW.setColor(this.SOLOS_ORANGE);
        this.PAINT_ARROW.setStrokeWidth(2.0f);
        this.PAINT_ARROW.setStyle(Paint.Style.FILL);
        this.PAINT_ARROW.setAntiAlias(true);
    }

    void setRoute(NavigationRoute route) {
        this.mRoutePoints.clear();
        this.mRoutePoints.addAll(route.getPreProcessRoute());
        if (this.mRoutePoints.isEmpty()) {
            this.LATLNG_TO_BITMAP = matrixForPosition(0.0d, 0.0d);
        } else {
            this.LATLNG_TO_BITMAP = matrixForPosition(this.mRoutePoints.get(0).getLatitude(), this.mRoutePoints.get(0).getLongitude());
        }
        getPath();
    }

    void setNextWaypoint(Coordinate c) {
        this.mNextWaypoint = c;
    }

    void updatePositionAndBearing(double lat, double lng, float bearing) {
        Log.d(TAG, String.format("new position: %.2f, %.2f mark %.1f", Double.valueOf(lat), Double.valueOf(lng), Float.valueOf(bearing)));
        this.LATLNG_TO_BITMAP = matrixForPosition(lat, lng);
        float[] point = new float[2];
        this.LATLNG_TO_BITMAP.mapPoints(point, new float[]{(float) lat, (float) lng});
        this.LATLNG_TO_BITMAP.postRotate((-bearing) - 90.0f);
        this.LATLNG_TO_BITMAP.postTranslate(60.0f, 80.0f);
        this.LATLNG_TO_BITMAP.mapPoints(point, new float[]{(float) lat, (float) lng});
    }

    Bitmap draw() {
        Bitmap pathBitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(pathBitmap);
        Path path = getPath();
        if (!path.isEmpty()) {
            canvas.drawPath(path, this.PAINT_ROUTE);
        }
        if (this.mNextWaypoint != null) {
            float[] point = new float[2];
            this.LATLNG_TO_BITMAP.mapPoints(point, new float[]{(float) this.mNextWaypoint.getLatitude(), (float) this.mNextWaypoint.getLongitude()});
            Log.d(TAG, String.format("waypoint: (%.3f, %.3f) -> (%.3f, %.3f)", Double.valueOf(this.mNextWaypoint.getLatitude()), Double.valueOf(this.mNextWaypoint.getLongitude()), Float.valueOf(point[0]), Float.valueOf(point[1])));
            if (Config.DEBUG) {
                canvas.drawCircle(point[0], point[1], 1.0f, this.PAINT_WAYPOINT);
                canvas.drawCircle(point[0], point[1], pixelsForMetres(30.0f), this.PAINT_DEBUG_WAYPOINT);
                canvas.drawCircle(point[0], point[1], pixelsForMetres(20.0f), this.PAINT_DEBUG_WAYPOINT);
            } else {
                canvas.drawCircle(point[0], point[1], 6.0f, this.PAINT_WAYPOINT);
            }
        }
        canvas.drawBitmap(this.BITMAP_ARROW, 51.0f, 71.0f, (Paint) null);
        if (Config.DEBUG) {
            canvas.drawCircle(60.0f, 80.0f, pixelsForMetres(40.0f), this.PAINT_DEBUG_OFF_ROUTE);
        }
        return pathBitmap;
    }

    private Path getPath() {
        boolean first = true;
        Path path = new Path();
        for (Coordinate c : this.mRoutePoints) {
            float[] point = new float[2];
            this.LATLNG_TO_BITMAP.mapPoints(point, new float[]{(float) c.getLatitude(), (float) c.getLongitude()});
            if (first) {
                path.moveTo(point[0], point[1]);
            } else {
                path.lineTo(point[0], point[1]);
            }
            first = false;
        }
        return path;
    }

    private double mapScaleForSpeed(double metresPerSecond) {
        return 1.0d;
    }

    private Matrix matrixForPosition(double latitude, double longitude) {
        double latAdjust = pixelsForMetres(111321.0f);
        double longAdjust = pixelsForMetres((float) (Math.cos(Math.toRadians(latitude)) * METRES_PER_DEGREE_LATITUDE));
        Matrix ret = new Matrix();
        ret.preTranslate((float) (-latitude), (float) (-longitude));
        ret.postScale((float) latAdjust, (float) longAdjust);
        return ret;
    }
}
