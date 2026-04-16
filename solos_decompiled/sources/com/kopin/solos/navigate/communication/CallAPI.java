package com.kopin.solos.navigate.communication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/* JADX INFO: loaded from: classes47.dex */
public class CallAPI {
    private static final String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/";
    public static final String GOOGLE_DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?key=";
    public static final String GOOGLE_ELEVATION_URL = "https://maps.googleapis.com/maps/api/elevation/json?key=";
    private static final String GOOGLE_KEY_PARAMETER = "?key=";
    public static final String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=geocode&radius=5000&input=";
    private static final String RETURN_TYPE = "json";
    public static String googleAPIKey = "REMOVED_FOR_SECURITY";

    public static StringBuilder requestURL(URL url) {
        URLConnection urlConnection = null;
        StringBuilder total = new StringBuilder();
        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            InputStream in2 = new BufferedInputStream(urlConnection.getInputStream());
            in = in2;
        } catch (IOException e2) {
        }
        try {
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                while (true) {
                    String line = r.readLine();
                    if (line != null) {
                        total.append(line);
                    } else {
                        try {
                            break;
                        } catch (Exception e3) {
                        }
                    }
                }
            } finally {
                try {
                    in.close();
                } catch (Exception e32) {
                    e32.printStackTrace();
                }
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            try {
                in.close();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        return total;
    }
}
