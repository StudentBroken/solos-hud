package org.jstrava.utility;

import com.kopin.peloton.PelotonResponse;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.jstrava.entities.VCTrakFile;

/* JADX INFO: loaded from: classes68.dex */
public class MultipartUtility {
    private static final String LINE_FEED = "\r\n";
    private final String boundary = "===" + System.currentTimeMillis() + "===";
    private String charset;
    private HttpURLConnection httpConn;
    private PrintWriter writer;

    public enum ResponseType {
        BAD_REQUEST,
        OK,
        ACCESS_DENIED,
        UNKNOWN
    }

    public MultipartUtility(String requestURL, String charset, String accessToken) throws IOException {
        this.charset = charset;
        URL url = new URL(requestURL);
        this.httpConn = (HttpURLConnection) url.openConnection();
        this.httpConn.setUseCaches(false);
        this.httpConn.setDoOutput(true);
        this.httpConn.setDoInput(true);
        this.httpConn.setRequestMethod(HttpRequest.METHOD_POST);
        this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
        if (accessToken != null) {
            this.httpConn.setRequestProperty("Accept", "application/json");
            this.httpConn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }
        this.writer = new PrintWriter((Writer) new OutputStreamWriter(this.httpConn.getOutputStream(), charset), true);
    }

    public void addFormField(String name, String value) throws IOException {
        this.writer.append((CharSequence) "--").append((CharSequence) this.boundary).append((CharSequence) "\r\n");
        this.writer.append((CharSequence) "Content-Disposition: form-data; name=\"").append((CharSequence) name).append((CharSequence) "\"").append((CharSequence) "\r\n");
        this.writer.append((CharSequence) "Content-Type: text/plain; charset=").append((CharSequence) this.charset).append((CharSequence) "\r\n");
        this.writer.append((CharSequence) "\r\n");
        this.writer.append((CharSequence) value).append((CharSequence) "\r\n");
        this.writer.flush();
    }

    public void addFilePart(String fieldName, VCTrakFile uploadFile) throws IOException {
        String fileName = uploadFile.getFileName();
        this.writer.append((CharSequence) "--").append((CharSequence) this.boundary).append((CharSequence) "\r\n");
        this.writer.append((CharSequence) "Content-Disposition: form-data; name=\"").append((CharSequence) fieldName).append((CharSequence) "\"; ");
        this.writer.append((CharSequence) "filename=\"").append((CharSequence) fileName).append((CharSequence) "\"").append((CharSequence) "\r\n");
        this.writer.append((CharSequence) "Content-Type: ").append((CharSequence) "text/plain").append((CharSequence) "\r\n");
        this.writer.append((CharSequence) "\r\n");
        this.writer.flush();
        uploadFile.write(this.writer);
        this.writer.flush();
        this.writer.append((CharSequence) "\r\n");
        this.writer.flush();
    }

    public ResponseType finish(StringBuilder sb) throws IOException {
        this.writer.append((CharSequence) "\r\n").flush();
        this.writer.append((CharSequence) "--").append((CharSequence) this.boundary).append((CharSequence) "--").append((CharSequence) "\r\n");
        this.writer.close();
        int status = this.httpConn.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        sb.append(line);
                    } else {
                        reader.close();
                        this.httpConn.disconnect();
                        return ResponseType.OK;
                    }
                }
                break;
            case 400:
                System.out.println("status: " + status);
                for (Map.Entry<String, List<String>> entry : this.httpConn.getHeaderFields().entrySet()) {
                    System.out.println("{ " + entry.getKey() + " : " + entry.getValue() + " }");
                }
                return ResponseType.BAD_REQUEST;
            case PelotonResponse.HTTP_UNAUTHORISED /* 401 */:
                System.out.println("status: " + status);
                for (Map.Entry<String, List<String>> entry2 : this.httpConn.getHeaderFields().entrySet()) {
                    System.out.println("{ " + entry2.getKey() + " : " + entry2.getValue() + " }");
                }
                return ResponseType.ACCESS_DENIED;
            default:
                System.out.println("status: " + status);
                for (Map.Entry<String, List<String>> entry3 : this.httpConn.getHeaderFields().entrySet()) {
                    System.out.println("{ " + entry3.getKey() + " : " + entry3.getValue() + " }");
                }
                return ResponseType.UNKNOWN;
        }
    }
}
