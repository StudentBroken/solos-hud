package com.kopin.solos.navigate.communication.tasks;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.kopin.solos.navigate.apimodels.googleplacesmodel.GooglePlacesResult;
import com.kopin.solos.navigate.communication.CallAPI;
import java.net.URL;

/* JADX INFO: loaded from: classes47.dex */
public class PlacesTask extends AsyncTask<Object, Object, GooglePlacesResult> {
    private final URL theURLToCall;

    public PlacesTask(URL theURL) {
        this.theURLToCall = theURL;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.AsyncTask
    public GooglePlacesResult doInBackground(Object... params) {
        StringBuilder stringBuilder = CallAPI.requestURL(this.theURLToCall);
        Gson mGson = new Gson();
        GooglePlacesResult placesResult = (GooglePlacesResult) mGson.fromJson(stringBuilder.toString(), GooglePlacesResult.class);
        return placesResult;
    }
}
