package com.ua.sdk.recorder.datasource.sensor.location;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import com.ua.sdk.UaLog;
import com.ua.sdk.recorder.datasource.RollingAverage;
import com.ua.sdk.recorder.datasource.sensor.location.LocationClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class MockLocationClient implements LocationClient {
    public static final int CONNECTION_TIMEOUT = 30000;
    public static final String MOCK_GPS_ENABLED_KEY = "mockGpsEnabled";
    public static final String MOCK_GPS_FAST_KEY = "mockGpsFast";
    public static final String MOCK_GPS_URL_KEY = "mockGpsUrl";
    private Handler backgroundHandler;
    private Context context;
    private List<Location> data;
    private boolean fastForward;
    private LocationClient.LocationClientListener locationClientListener;
    private String url;
    private RollingAverage<Float> accuracyAccumulator = new RollingAverage<>(3);
    private boolean isRunning = false;
    private MockGpsLoader dataLoader = null;
    private boolean mockLocationRunning = false;
    private int lineIndex = 0;
    private Location currentLocation = null;
    private long currentOriginalTime = 0;
    private boolean stopped = false;
    private HandlerThread handlerThread = new HandlerThread("MockLocationClientTimer");

    public MockLocationClient(Context context, String url, boolean fastForward) {
        this.context = context;
        this.url = url;
        this.fastForward = fastForward;
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.location.LocationClient
    public void connect(LocationClient.LocationClientListener locationClientListener) {
        this.locationClientListener = locationClientListener;
        if (!this.isRunning) {
            this.isRunning = true;
            this.dataLoader = new MockGpsLoader(this.context, this.url);
            this.dataLoader.execute(new Void[0]);
        }
        this.handlerThread.start();
        this.backgroundHandler = new MyHandler(this.handlerThread.getLooper());
    }

    @Override // com.ua.sdk.recorder.datasource.sensor.location.LocationClient
    public void disconnect() {
        if (this.dataLoader != null) {
            this.dataLoader.cancel(true);
            this.dataLoader = null;
        }
        stopLocationUpdates();
        this.handlerThread.quit();
        this.isRunning = false;
    }

    public void beginLocationUpdates() {
        if (this.data != null && this.data.size() > 0) {
            this.locationClientListener.onStatus(true, true, 100.0f);
            updateLocation();
            dispatchCurrentLocation();
        }
    }

    public void stopLocationUpdates() {
        if (this.backgroundHandler != null) {
            this.backgroundHandler.removeMessages(0);
        }
        this.stopped = true;
    }

    public void dispatchCurrentLocation() {
        if (!this.stopped) {
            UaLog.debug("MockLocationClient next location " + this.lineIndex + " of " + this.data.size());
            this.accuracyAccumulator.addValue(Float.valueOf(this.currentLocation.getAccuracy()));
            this.locationClientListener.onStatus(true, true, Double.valueOf(this.accuracyAccumulator.getAverage()).floatValue());
            this.locationClientListener.onLocation(this.currentLocation);
            if (this.lineIndex < this.data.size()) {
                updateLocation();
                long delay = this.currentLocation.getTime() - System.currentTimeMillis();
                if (delay < 0) {
                    delay = 0;
                }
                this.backgroundHandler.sendEmptyMessageDelayed(0, delay);
                return;
            }
            this.mockLocationRunning = false;
        }
    }

    private void updateLocation() {
        long now = System.currentTimeMillis();
        List<Location> list = this.data;
        int i = this.lineIndex;
        this.lineIndex = i + 1;
        Location location = list.get(i);
        long nextOriginalTime = location.getTime();
        if (this.currentOriginalTime != 0) {
            long adjustment = nextOriginalTime - this.currentOriginalTime;
            if (this.fastForward) {
                adjustment /= 2;
            }
            long adjustedTime = now + adjustment;
            location.setTime(adjustedTime);
        } else {
            location.setTime(now);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete", new Class[0]);
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(location, new Object[0]);
                }
            } catch (Exception e) {
                UaLog.warn("failed Location.makeComplete", (Throwable) e);
            }
        }
        this.currentLocation = location;
        this.currentOriginalTime = nextOriginalTime;
    }

    public class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            MockLocationClient.this.dispatchCurrentLocation();
        }
    }

    protected class MockGpsLoader extends AsyncTask<Void, Void, List<Location>> {
        private ContentResolver cr;
        private String dataUrl;

        public MockGpsLoader(Context c, String dataUrl) {
            this.cr = c.getContentResolver();
            this.dataUrl = dataUrl;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public List<Location> doInBackground(Void... params) {
            InputStream is;
            int longColumn;
            int latColumn;
            int accuracyColumn;
            int timeColumn;
            List<Location> result = new ArrayList<>();
            InputStream is2 = null;
            try {
                try {
                } catch (Exception e) {
                    UaLog.error("Exception during Mock GPS URL load.", (Throwable) e);
                    result = null;
                    if (0 != 0) {
                        try {
                            is2.close();
                        } catch (IOException e2) {
                        }
                    }
                }
                if (this.dataUrl == null) {
                    UaLog.error("Mock GPS URL is empty, aborting data mock data load.");
                    List<Location> list = MockLocationClient.this.data;
                    if (0 == 0) {
                        return list;
                    }
                    try {
                        is2.close();
                        return list;
                    } catch (IOException e3) {
                        return list;
                    }
                }
                UaLog.info("Importing mock data from URI " + this.dataUrl);
                if (this.dataUrl.startsWith("http")) {
                    URL url = new URL(this.dataUrl);
                    is = url.openConnection().getInputStream();
                } else {
                    is = this.cr.openInputStream(Uri.parse(this.dataUrl));
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                if (line.startsWith("#Android")) {
                    latColumn = 0;
                    longColumn = 1;
                    timeColumn = 2;
                    accuracyColumn = 3;
                    line = reader.readLine();
                } else {
                    longColumn = 0;
                    latColumn = 1;
                    accuracyColumn = 2;
                    timeColumn = 3;
                }
                while (line != null) {
                    String[] parts = line.split(",");
                    Double longitude = Double.valueOf(parts[longColumn].trim());
                    Double latitude = Double.valueOf(parts[latColumn].trim());
                    float acc = Float.valueOf(parts[accuracyColumn].trim()).floatValue();
                    long time = Long.valueOf(parts[timeColumn].trim()).longValue();
                    Location location = new Location("mock");
                    location.setLatitude(latitude.doubleValue());
                    location.setLongitude(longitude.doubleValue());
                    location.setAccuracy(acc);
                    location.setTime(time);
                    result.add(location);
                    line = reader.readLine();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
                return result;
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        is2.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<Location> result) {
            if (result != null) {
                MockLocationClient.this.data = result;
                MockLocationClient.this.beginLocationUpdates();
            } else {
                Toast.makeText(MockLocationClient.this.context, "Mock GPS load failed.", 1).show();
            }
            MockLocationClient.this.dataLoader = null;
        }
    }
}
