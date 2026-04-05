package com.ua.sdk.route;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class RouteJsonParser implements JsonParser<Route> {
    private Gson gson;

    public RouteJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonParser
    public Route parse(InputStream inputStream) throws UaException {
        try {
            RouteTO to = (RouteTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), RouteTO.class);
            RouteImpl route = RouteTO.fromTransferObject(to);
            return route;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
