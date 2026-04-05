package com.google.maps.android.kml;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;

/* JADX INFO: loaded from: classes69.dex */
public class KmlBoolean {
    public static boolean parseBoolean(String text) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(text) || ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equals(text);
    }
}
