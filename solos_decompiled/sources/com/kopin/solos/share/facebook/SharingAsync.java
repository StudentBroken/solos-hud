package com.kopin.solos.share.facebook;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.kopin.peloton.Cloud;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes4.dex */
public class SharingAsync extends AsyncTask<JSONObject, Void, JSONObject> {
    private static final String JSON_KEY_IMAGE = "image";
    private static final String JSON_KEY_RIDE = "jsondata";
    public static final String JSON_KEY_URL_CONTENT = "ContentUrl";
    public static final String JSON_KEY_URL_IMAGE = "ImageUrl";
    private static final String LINE_END = "\r\n";
    private static final String MAP_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String MAP_IMAGE_FILE = "map.jpg";
    private static final int MAX_BUFFER_SIZE = 1048576;
    private static final String MULITPART_BOUNDARY = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
    private static final String MULTIPART_HYPHENS = "--";
    private static final String POST_HEADER_TOKEN_KEY = "client_id";
    private static final String POST_HEADER_TOKEN_VALUE = "ST3ljqo5YN7dNog+rcK4+FgLMBwIGoMAncfTFECgG3M=";
    private static final String SERVER_UPLOAD_PATH = "api/Share/";
    private View mDisableView;
    private File mMapFile;
    private Bitmap mMapImage;
    private ProgressBar mProgressBar;

    public void setProgressBar(ProgressBar progressBar) {
        this.mProgressBar = progressBar;
    }

    public void setDisableView(View view) {
        this.mDisableView = view;
    }

    public void setMapImage(Bitmap bitmap) {
        this.mMapImage = bitmap;
    }

    public void setMapFile(File file) {
        this.mMapFile = file;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(0);
        }
        if (this.mDisableView != null) {
            this.mDisableView.setEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public JSONObject doInBackground(JSONObject... rideDataJson) {
        if (rideDataJson == null || rideDataJson[0] == null) {
            return null;
        }
        return upload(this.mMapImage, rideDataJson[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.AsyncTask
    public void onPostExecute(JSONObject result) {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(4);
        }
        if (this.mDisableView != null) {
            this.mDisableView.setEnabled(result != null);
        }
    }

    private JSONObject upload(Bitmap bitmap, JSONObject rideDataJson) throws Throwable {
        try {
            FacebookSharingHelper.addImageAttributes(rideDataJson, bitmap);
            String response = multipartRequest(getHost() + SERVER_UPLOAD_PATH, JSON_KEY_RIDE, rideDataJson.toString(), this.mMapFile.getAbsolutePath(), "image");
            if (response == null || response.isEmpty()) {
                return null;
            }
            JSONObject result = new JSONObject(response);
            return result;
        } catch (IOException e) {
            Log.e("SharingAsync", "IOexception " + e.getMessage());
            return null;
        } catch (JSONException je) {
            Log.e("SharingAsync", "Result JSON exception " + je.getMessage());
            return null;
        } catch (Exception ex) {
            Log.e("SharingAsync", "Upload exception " + ex.getMessage());
            return null;
        }
    }

    private byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        return stream.toByteArray();
    }

    private String multipartRequest(String urlTo, String postKey, String postVal, String filepath, String filefield) throws Throwable {
        DataOutputStream outputStream;
        HttpURLConnection connection = null;
        DataOutputStream outputStream2 = null;
        String result = "";
        String[] pathTokens = filepath.split("/");
        int index = pathTokens.length - 1;
        try {
            try {
                URL url = new URL(urlTo);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod(HttpRequest.METHOD_POST);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + MULITPART_BOUNDARY);
                connection.setRequestProperty("client_id", POST_HEADER_TOKEN_VALUE);
                outputStream = new DataOutputStream(connection.getOutputStream());
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        } catch (Exception e2) {
            e = e2;
        }
        try {
            outputStream.writeBytes(MULTIPART_HYPHENS + MULITPART_BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + pathTokens[index] + "\"\r\n");
            outputStream.writeBytes("Content-Type: image/jpeg\r\n");
            outputStream.writeBytes("Content-Transfer-Encoding: binary\r\n");
            outputStream.writeBytes("\r\n");
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesAvailable = fileInputStream.available();
            int bufferSize = Math.min(bytesAvailable, 1048576);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                int bytesAvailable2 = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable2, 1048576);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes(MULTIPART_HYPHENS + MULITPART_BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + postKey + "\"\r\n");
            outputStream.writeBytes("Content-Type: text/plain\r\n");
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes(postVal);
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes(MULTIPART_HYPHENS + MULITPART_BOUNDARY + MULTIPART_HYPHENS + "\r\n");
            InputStream inputStream = connection.getInputStream();
            result = convertStreamToString(inputStream);
            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            if (outputStream != null) {
                outputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
                outputStream2 = outputStream;
            } else {
                outputStream2 = outputStream;
            }
        } catch (IOException e3) {
            e = e3;
            outputStream2 = outputStream;
            Log.w("SharingAsync", "Multipart IOexception " + e.getMessage());
            if (outputStream2 != null) {
                outputStream2.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Exception e4) {
            e = e4;
            outputStream2 = outputStream;
            Log.w("SharingAsync", "Multipart error " + e.getMessage());
            if (outputStream2 != null) {
                outputStream2.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Throwable th2) {
            th = th2;
            outputStream2 = outputStream;
            if (outputStream2 != null) {
                outputStream2.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
            throw th;
        }
        return result;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        sb.append(line);
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        is.close();
        return sb.toString();
    }

    public static String getHost() {
        return Cloud.getHost();
    }
}
