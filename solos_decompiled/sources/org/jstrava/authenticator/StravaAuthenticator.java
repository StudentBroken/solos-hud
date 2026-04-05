package org.jstrava.authenticator;

import com.google.gson.Gson;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes68.dex */
public class StravaAuthenticator {
    private static final String TOKEN_URL = "https://www.strava.com/oauth/token";
    private int clientId;
    private String redirectUri;
    private String secrete;

    public int getClientId() {
        return this.clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUri() {
        return this.redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getSecrete() {
        return this.secrete;
    }

    public void setSecrete(String secrete) {
        this.secrete = secrete;
    }

    public StravaAuthenticator(int clientId, String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public StravaAuthenticator(int clientId, String redirectUri, String secrete) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.secrete = secrete;
    }

    public String getRequestAccessUrl(String approvalPrompt, boolean viewPrivate, boolean write, String state) {
        String url = "https://www.strava.com/oauth/authorize?client_id=" + this.clientId + "&response_type=code&redirect_uri=" + this.redirectUri;
        StringBuilder sb = new StringBuilder(url);
        if (viewPrivate || write) {
            sb.append("&scope=");
            if (viewPrivate) {
                sb.append("view_private");
            }
            if (viewPrivate && write) {
                sb.append(",");
            }
            if (write) {
                sb.append("write");
            }
        }
        sb.append("&state=").append(state);
        if (!approvalPrompt.equals("force") && !approvalPrompt.equals("auto")) {
            throw new IllegalArgumentException("approvalPrompt should be 'force' or 'auto'");
        }
        sb.append("&approval_prompt=").append(approvalPrompt);
        return sb.toString();
    }

    public AuthResponse getToken(String code) throws URISyntaxException, IOException {
        if (this.secrete == null) {
            throw new IllegalStateException("Application secrete is not set");
        }
        URI uri = new URI(TOKEN_URL);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("client_id=").append(this.clientId);
            sb.append("&client_secret=").append(this.secrete);
            sb.append("&code=").append(code);
            conn.setRequestMethod(HttpRequest.METHOD_POST);
            conn.setRequestProperty("Accept", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(sb.toString().getBytes("UTF-8"));
            if (conn.getResponseCode() != 200) {
                for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " ; URL: " + url + " ; sb: " + sb.toString());
            }
            Reader br = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            return (AuthResponse) gson.fromJson(br, AuthResponse.class);
        } finally {
            conn.disconnect();
        }
    }
}
