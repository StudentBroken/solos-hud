package com.kopin.solos.navigate.communication.tasks;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.kopin.solos.navigate.apimodels.googleelevation.ElevationPoint;
import com.kopin.solos.navigate.apimodels.googleelevation.ElevationResults;
import com.kopin.solos.navigate.communication.CallAPI;
import com.kopin.solos.storage.Coordinate;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class ElevationTask extends AsyncTask<Object, Object, List<ElevationPoint>> {
    private final URL theURLToCall;

    public interface ElevationTaskResults {
        void onResults(List<ElevationPoint> list);
    }

    private ElevationTask(URL theURL) {
        this.theURLToCall = theURL;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public List<ElevationPoint> doInBackground(Object... params) {
        StringBuilder stringBuilder = CallAPI.requestURL(this.theURLToCall);
        Gson mGson = new Gson();
        ElevationResults rS = (ElevationResults) mGson.fromJson(stringBuilder.toString(), ElevationResults.class);
        return rS.results;
    }

    public static ElevationTask createTaskFor(String encodedPolyLine, ElevationTaskResults cb) {
        return null;
    }

    public static ElevationTask createTaskFor(List<Coordinate> theCoords, final ElevationTaskResults cb) {
        StringBuilder params = new StringBuilder();
        boolean first = true;
        int skipCount = theCoords.size() / 200;
        int count = 0;
        for (Coordinate c : theCoords) {
            if (count != 0) {
                count--;
            } else {
                count = skipCount;
                params.append(first ? "&locations=" : "|").append(Double.toString(c.getLatitude())).append(",").append(Double.toString(c.getLongitude()));
                first = false;
            }
        }
        URL url = null;
        try {
            URL url2 = new URL(CallAPI.GOOGLE_ELEVATION_URL + CallAPI.googleAPIKey + params.toString());
            url = url2;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ElevationTask self = new ElevationTask(url) { // from class: com.kopin.solos.navigate.communication.tasks.ElevationTask.1
            @Override // com.kopin.solos.navigate.communication.tasks.ElevationTask, android.os.AsyncTask
            protected /* bridge */ /* synthetic */ List<ElevationPoint> doInBackground(Object[] objArr) {
                return super.doInBackground(objArr);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(List<ElevationPoint> elevationResults) {
                cb.onResults(elevationResults);
            }
        };
        return self;
    }

    public static List<ElevationPoint> getElevationResults(List<Coordinate> forCoords) {
        StringBuilder stringBuilder = CallAPI.requestURL(getRequestURL(forCoords));
        Gson mGson = new Gson();
        ElevationResults rS = (ElevationResults) mGson.fromJson(stringBuilder.toString(), ElevationResults.class);
        return rS != null ? rS.results : new ArrayList();
    }

    private static URL getRequestURL(List<Coordinate> theCoords) {
        StringBuilder params = new StringBuilder();
        boolean first = true;
        for (Coordinate c : theCoords) {
            params.append(first ? "&locations=" : "|").append(Double.toString(c.getLatitude())).append(",").append(Double.toString(c.getLongitude()));
            first = false;
        }
        try {
            URL url = new URL(CallAPI.GOOGLE_ELEVATION_URL + CallAPI.googleAPIKey + params.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
