package com.kopin.solos.navigate.communication.tasks;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.kopin.solos.navigate.apimodels.googledirectionsapimodel.DirectionResults;
import com.kopin.solos.navigate.communication.CallAPI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class DirectionsTask extends AsyncTask<Object, Object, List<DirectionResults>> {
    private final URL theURLToCall;

    public DirectionsTask(URL theURL) {
        this.theURLToCall = theURL;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public List<DirectionResults> doInBackground(Object... params) {
        List<DirectionResults> directionResults = new ArrayList<>();
        StringBuilder stringBuilder = CallAPI.requestURL(this.theURLToCall);
        Gson mGson = new Gson();
        DirectionResults rS = (DirectionResults) mGson.fromJson(stringBuilder.toString(), DirectionResults.class);
        directionResults.add(rS);
        return directionResults;
    }
}
