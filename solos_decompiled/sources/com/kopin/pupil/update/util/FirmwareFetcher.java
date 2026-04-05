package com.kopin.pupil.update.util;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes13.dex */
public class FirmwareFetcher {
    private static final String BASE_URL_DEBUG = "https://kopinsolosapi-dev.azurewebsites.net/";
    private static final String BASE_URL_LIVE = "https://api.solos-wearables.com/";
    private static final ArrayList<NameValuePair> DEFAULT_HEADERS = new ArrayList<>();
    private static final String TAG = "FirmwareFetcher";
    private static final String UPDATES_ENDPOINT = "api/firmware/versions";
    private static final String VERSION_ENDPOINT = "api/firmware/version";

    public static class FirmwareVersion {
        public String Api;
        public VersionInfo Firmware;
        public String Message;
        public boolean Success;
    }

    public static class FirmwareVersions {
        public String Api;
        public VersionInfo[] History;
        public VersionInfo Latest;
        public String Message;
        public boolean Success;
    }

    public interface ResultCallback {
        void onDownloaded(VersionInfo versionInfo);

        void onError(String str);

        void onLatestVersion(VersionInfo versionInfo);

        void onVersions(VersionInfo[] versionInfoArr);
    }

    static {
        DEFAULT_HEADERS.add(new NameValuePair("client_id", "ST3ljqo5YN7dNog+rcK4+FgLMBwIGoMAncfTFECgG3M="));
    }

    public static void getLatestVersion(Context context, String product, ResultCallback cb) {
        getLatestVersion(context, product, false, cb);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.kopin.pupil.update.util.FirmwareFetcher$1] */
    public static void getLatestVersion(Context context, String product, boolean devServer, final ResultCallback cb) {
        new HttpRequestThread(context, (devServer ? BASE_URL_DEBUG : BASE_URL_LIVE) + VERSION_ENDPOINT + "?product=" + product) { // from class: com.kopin.pupil.update.util.FirmwareFetcher.1
            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected ArrayList<NameValuePair> getHeaders() {
                return FirmwareFetcher.DEFAULT_HEADERS;
            }

            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected boolean processStream(InputStream stream, Map<String, List<String>> headers) {
                return false;
            }

            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected void processResponse(String resp, Map<String, List<String>> headers) {
                Gson decoder = new Gson();
                FirmwareVersion response = (FirmwareVersion) decoder.fromJson(resp, FirmwareVersion.class);
                if (response.Success && response.Firmware != null) {
                    Log.d(FirmwareFetcher.TAG, "Latest version: " + response.Firmware.Comments);
                    if (cb != null) {
                        cb.onLatestVersion(response.Firmware);
                        return;
                    }
                    return;
                }
                Log.e(FirmwareFetcher.TAG, "" + response.Message);
                if (cb != null) {
                    cb.onError(response.Message);
                }
            }
        }.start();
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.kopin.pupil.update.util.FirmwareFetcher$2] */
    public static void getAvailableVersions(Context context, String product, boolean devServer, final ResultCallback cb) {
        new HttpRequestThread(context, (devServer ? BASE_URL_DEBUG : BASE_URL_LIVE) + UPDATES_ENDPOINT + "?product=" + product) { // from class: com.kopin.pupil.update.util.FirmwareFetcher.2
            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected ArrayList<NameValuePair> getHeaders() {
                return FirmwareFetcher.DEFAULT_HEADERS;
            }

            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected boolean processStream(InputStream stream, Map<String, List<String>> headers) {
                return false;
            }

            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected void processResponse(String resp, Map<String, List<String>> headers) {
                Gson decoder = new Gson();
                FirmwareVersions response = (FirmwareVersions) decoder.fromJson(resp, FirmwareVersions.class);
                if (response.Success && response.Latest != null && response.History != null) {
                    Log.d(FirmwareFetcher.TAG, "Latest version: " + response.Latest.Comments + " number of versions: " + response.History.length);
                    if (cb != null) {
                        cb.onVersions(response.History);
                        return;
                    }
                    return;
                }
                Log.e(FirmwareFetcher.TAG, "" + response.Message);
                if (cb != null) {
                    cb.onError(response.Message);
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean saveUpdate(InputStream in, File file) {
        int len;
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            do {
                len = in.read(buf);
                if (len > 0) {
                    out.write(buf, 0, len);
                }
            } while (len > 0);
            out.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.kopin.pupil.update.util.FirmwareFetcher$3] */
    public static void downloadVersion(final Context context, final VersionInfo version, final ResultCallback cb) {
        String manifestEndpoint = version.XmlFilename;
        new HttpRequestThread(context, manifestEndpoint) { // from class: com.kopin.pupil.update.util.FirmwareFetcher.3
            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected ArrayList<NameValuePair> getHeaders() {
                return FirmwareFetcher.DEFAULT_HEADERS;
            }

            /* JADX WARN: Type inference failed for: r3v3, types: [com.kopin.pupil.update.util.FirmwareFetcher$3$1] */
            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected boolean processStream(InputStream stream, Map<String, List<String>> headers) {
                File newManifest = FirmwareFlash.updateFile(version.XmlFilename);
                boolean saved = FirmwareFetcher.saveUpdate(stream, newManifest);
                if (!saved) {
                    newManifest.delete();
                    cb.onError("Couldn't download update manifest");
                    return true;
                }
                String imageEndpoint = version.Filename;
                new HttpRequestThread(context, imageEndpoint) { // from class: com.kopin.pupil.update.util.FirmwareFetcher.3.1
                    @Override // com.kopin.pupil.update.util.HttpRequestThread
                    protected boolean processStream(InputStream stream2, Map<String, List<String>> headers2) {
                        File newImage = FirmwareFlash.updateFile(version.Filename);
                        boolean saved2 = FirmwareFetcher.saveUpdate(stream2, newImage);
                        if (!saved2) {
                            newImage.delete();
                            cb.onError("Couldn't download update image");
                            return true;
                        }
                        cb.onDownloaded(version);
                        return true;
                    }

                    @Override // com.kopin.pupil.update.util.HttpRequestThread
                    protected void processResponse(String resp, Map<String, List<String>> headers2) {
                    }

                    @Override // com.kopin.pupil.update.util.HttpRequestThread
                    protected ArrayList<NameValuePair> getHeaders() {
                        return FirmwareFetcher.DEFAULT_HEADERS;
                    }
                }.start();
                return true;
            }

            @Override // com.kopin.pupil.update.util.HttpRequestThread
            protected void processResponse(String resp, Map<String, List<String>> headers) {
            }
        }.start();
    }

    public static class VersionInfo {
        public String Comments;
        public String Filename;
        public String Id;
        public String Product;
        public String Uploaded;
        public String UploadedBy;
        public String Version;
        public String XmlFilename;

        public String toString() {
            return this.Version + " - " + this.Comments;
        }
    }
}
