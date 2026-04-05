package com.ua.sdk.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.webkit.MimeTypeMap;
import com.ua.sdk.NetworkError;
import com.ua.sdk.Ua;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class Precondition {
    public static final String[] IMAGE_EXT = {"jpg", "png", "gif", "jpeg"};

    public static void isInBackground() throws IllegalStateException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot call on main thread.");
        }
    }

    public static void isAuthenticated(AuthenticationManager authenticationManager) throws UaException {
        if (authenticationManager == null) {
            throw new UaException("AuthenticaitonManager is null.");
        }
        if (!authenticationManager.isAuthenticated()) {
            throw new UaException(UaException.Code.NOT_AUTHENTICATED);
        }
    }

    public static void isNotAuthenticated(Ua ua) {
        if (ua.isAuthenticated()) {
            throw new IllegalStateException("Already authenticated.");
        }
    }

    public static void isOnMain() throws IllegalStateException {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Must call on main thread.");
        }
    }

    public static <T> T isNotNull(T param) throws NullPointerException {
        if (param == null) {
            throw new NullPointerException();
        }
        return param;
    }

    public static <T> T isNotNull(T param, String name) throws NullPointerException {
        if (param == null) {
            if (name == null) {
                throw new NullPointerException();
            }
            throw new NullPointerException(name + " is null");
        }
        return param;
    }

    public static int isNotNegative(int param) throws IllegalArgumentException {
        return isNotNegative(param, null);
    }

    public static int isNotNegative(int param, String name) throws IllegalArgumentException {
        String name2 = (name == null || name.length() == 0) ? "" : name + " must be a positive number.";
        if (param < 0) {
            throw new IllegalArgumentException(name2);
        }
        return param;
    }

    public static int isValidIndex(List<?> list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            int size = list == null ? 0 : list.size();
            throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
        }
        return index;
    }

    public static void check(boolean test, String message) throws IllegalArgumentException {
        if (!test) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isResponseHttpOk(HttpURLConnection conn) throws UaException {
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new NetworkError(responseCode, conn);
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN);
        }
    }

    public static void isConnected(Context context) throws UaException {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            throw new UaException(UaException.Code.NOT_CONNECTED);
        }
    }

    public static void isResponseSuccess(HttpURLConnection conn) throws UaException {
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode < 200 || responseCode > 299) {
                throw new NetworkError(responseCode, conn);
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN);
        }
    }

    public static void isExpectedResponse(HttpURLConnection conn, int expectedResponseCode) throws UaException {
        if (conn == null) {
            throw new UaException("connection is null, cannot check response code.");
        }
        try {
            int responseCode = conn.getResponseCode();
            if (responseCode != expectedResponseCode) {
                throw new NetworkError(responseCode, conn);
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN);
        }
    }

    public static void hasImageExtension(File file) throws UaException {
        if (file == null) {
            throw new UaException("file is null");
        }
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        if (ext != null && !ext.isEmpty()) {
            String ext2 = ext.toLowerCase();
            int length = IMAGE_EXT.length;
            for (int i = 0; i < length; i++) {
                if (IMAGE_EXT[i].equals(ext2)) {
                    return;
                }
            }
        }
        throw new UaException(file.getAbsolutePath() + " not an image.");
    }
}
