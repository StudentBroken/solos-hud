package com.kopin.pupil;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.kopin.accessory.packets.ButtonType;
import com.kopin.pupil.ConnectionManager;
import com.kopin.pupil.PupilDevice;
import com.kopin.pupil.exception.XmlParserException;
import com.kopin.pupil.pagerenderer.PageRenderer;
import com.kopin.pupil.ui.VCNotification;
import com.kopin.pupil.util.Action;
import com.kopin.pupil.util.Utility;
import com.kopin.pupil.util.XmlParserPackage;
import java.io.StringReader;
import java.net.InetAddress;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes25.dex */
public abstract class VCApplication extends ContextWrapper {
    public static final String COLOR_DEFAULT_FONT = "default_font";
    private static final String TAG = "VCAppService";
    private Map<String, Bitmap> mBitmapCache;
    private ConnectionListener mConnectionListener;
    private ConnectionManager mConnectionManager;
    private Map<String, StackTraceElement[]> mExceptionCache;
    private Handler mExceptionHandler;
    private HashSet<StatusListener> mStatusListeners;
    private Map<String, Utility.ApplicationInfo> mXMLPageCache;
    private Map<String, Utility.ApplicationInfo> mXMLPageId;

    public interface ConnectionListener {

        public enum ConnectionState {
            CONNECTED,
            DISCONNECTED
        }

        void onStateChanged(ConnectionState connectionState);
    }

    public interface StatusListener {
        void onStatus(PupilDevice.DeviceStatus deviceStatus);
    }

    protected abstract void onAction(Action action);

    public abstract void onConnected();

    public abstract void onDisconnected();

    public abstract void onEnter();

    public abstract void onExit();

    protected abstract void onTTSComplete(int i);

    public VCApplication(ConnectionManager connectionManager, Context context) {
        super(context);
        this.mConnectionManager = null;
        this.mXMLPageCache = null;
        this.mBitmapCache = null;
        this.mExceptionCache = null;
        this.mXMLPageId = null;
        this.mConnectionManager = connectionManager;
        this.mXMLPageCache = new HashMap();
        this.mBitmapCache = new HashMap();
        this.mExceptionCache = new HashMap();
        this.mXMLPageId = new HashMap();
        this.mExceptionHandler = new Handler(Looper.getMainLooper());
        this.mStatusListeners = new HashSet<>();
    }

    protected void initTTS() {
        this.mConnectionManager.initTTS();
    }

    void throwXmlException(final XmlParserException e) {
        final StackTraceElement[] stackTrace = this.mExceptionCache.get(e.getPageId());
        this.mExceptionHandler.post(new Runnable() { // from class: com.kopin.pupil.VCApplication.1
            @Override // java.lang.Runnable
            public void run() {
                RuntimeException exception = new RuntimeException(e);
                if (stackTrace != null) {
                    exception.setStackTrace(stackTrace);
                }
                Log.e("Page renderer", "", exception);
            }
        });
    }

    public XmlParserPackage getPageXmlParser(String id, String template) {
        if (this.mXMLPageId.containsKey(template)) {
            Utility.ApplicationInfo info = this.mXMLPageId.get(template);
            XmlResourceParser parser = getResources().getXml(info.getId());
            return new XmlParserPackage(parser, info.getVersion(), id, template);
        }
        if (this.mXMLPageCache.containsKey(template)) {
            try {
                XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
                Utility.ApplicationInfo info2 = this.mXMLPageCache.get(template);
                xmlParser.setInput(new StringReader(info2.getData()));
                return new XmlParserPackage(xmlParser, info2.getVersion(), id, template);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Could not find xml parser " + id, e);
                return null;
            }
        }
        if (this.mXMLPageId.containsKey(id)) {
            Utility.ApplicationInfo info3 = this.mXMLPageId.get(id);
            XmlResourceParser parser2 = getResources().getXml(info3.getId());
            return new XmlParserPackage(parser2, info3.getVersion(), id, template);
        }
        if (this.mXMLPageCache.containsKey(id)) {
            try {
                XmlPullParser xmlParser2 = XmlPullParserFactory.newInstance().newPullParser();
                Utility.ApplicationInfo info4 = this.mXMLPageCache.get(id);
                xmlParser2.setInput(new StringReader(info4.getData()));
                return new XmlParserPackage(xmlParser2, info4.getVersion(), id, template);
            } catch (XmlPullParserException e2) {
                Log.e(TAG, "Could not find xml parser " + id, e2);
                return null;
            }
        }
        Log.e(TAG, " - Could not find xml parser " + id);
        return null;
    }

    public void requestStart() {
        this.mConnectionManager.reconnectToLastHeadset(this);
    }

    public void requestExit() {
        this.mConnectionManager.disconnect(false);
    }

    public final boolean isConnected() {
        return this.mConnectionManager.isConnected();
    }

    public final boolean isReconnecting() {
        return this.mConnectionManager.isReconnecting();
    }

    protected Bitmap getBitmap(int id) {
        Resources resources = getResources();
        if (resources == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = 1;
        options.inScaled = false;
        return BitmapFactory.decodeResource(resources, id, options);
    }

    protected void addBitmap(String id, int resID) {
        addBitmap(id, getBitmap(resID));
    }

    protected void addBitmap(String id, Bitmap bitmap) {
        this.mBitmapCache.put(id, bitmap);
        this.mConnectionManager.bitmapChanged(id);
    }

    protected Bitmap getBitmap(String id) {
        Bitmap bitmap = this.mBitmapCache.get(id);
        if (bitmap == null) {
            Log.e("VCApplication", "getBitmap not found for " + id);
        }
        return bitmap;
    }

    protected String addPage(int id) {
        return addPage((String) null, id);
    }

    protected void addPage(int... ids) {
        for (int id : ids) {
            addPage(id);
        }
    }

    protected String addPage(String pageName, int id) {
        String pageName2;
        String resType = getResources().getResourceTypeName(id);
        if (resType.equalsIgnoreCase("raw")) {
            pageName2 = addPageRaw(pageName, id);
        } else if (resType.equalsIgnoreCase("xml")) {
            pageName2 = addPageXml(pageName, id);
        } else {
            Log.e("AppService", "addPage not raw/xml: " + pageName + ", " + id);
            throw new InvalidParameterException("Invalid resource. Only resources from the xml or raw folder can be used.");
        }
        this.mConnectionManager.clearPageFromCache(pageName2);
        return pageName2;
    }

    private String addPageXml(String pageName, int id) {
        XmlPullParser xmlParser = getResources().getXml(id);
        Utility.ApplicationInfo info = Utility.pullInfoFromXml(xmlParser, pageName);
        if (info == null || info.getName().isEmpty()) {
            Log.e("AppService", "addPageXml: info null/empty");
            throw new InvalidParameterException("Could not resolve page ID.");
        }
        info.setId(id);
        this.mXMLPageId.put(info.getName(), info);
        this.mExceptionCache.put(info.getName(), new RuntimeException("Stack trace").getStackTrace());
        this.mXMLPageCache.remove(info.getName());
        return info.getName();
    }

    private String addPageRaw(String pageName, int id) {
        String xmlPage = Utility.getXMLString(id, this);
        if (xmlPage.isEmpty()) {
            throw new InvalidParameterException("Resource ID for the XML does not appear to be valid.");
        }
        if (xmlPage.length() > 32767) {
            throw new InvalidParameterException("XML cannot contain greater than 32767 characters.");
        }
        return addPage(pageName, xmlPage);
    }

    private String addPage(String pageName, String xmlPage) {
        Utility.ApplicationInfo info = null;
        try {
            XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlParser.setInput(new StringReader(xmlPage));
            info = Utility.pullInfoFromXml(xmlParser, pageName);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "", e);
        }
        if (info == null || info.getName().isEmpty()) {
            throw new InvalidParameterException("Could not resolve page ID.");
        }
        info.setData(xmlPage);
        this.mXMLPageCache.put(info.getName(), info);
        this.mExceptionCache.put(info.getName(), new RuntimeException("Stack trace").getStackTrace());
        this.mXMLPageId.remove(info.getName());
        return info.getName();
    }

    protected void resetPages() {
        this.mConnectionManager.resetPageCache();
    }

    public void gotoPage(String templateID, String pageID, Bundle extra) {
        try {
            this.mConnectionManager.gotoPage(templateID, pageID, extra);
        } catch (Exception e) {
            Log.e(TAG, "VCApp error going to page " + pageID);
        }
    }

    public void refresh() {
        this.mConnectionManager.refresh();
    }

    public void refresh(boolean force) {
        this.mConnectionManager.refresh(force);
    }

    public void setScreenWatcher(ConnectionManager.ScreenWatcher watcher) {
        this.mConnectionManager.setScreenWatcher(watcher);
    }

    public Bundle getElementProperties(String pageID, String elementID) {
        return this.mConnectionManager.getElementProperties(pageID, elementID);
    }

    public void sendDropDownNotification(Bitmap icon, String iconStr, String defaultIcon, int color, int borderColor, int duration, boolean ttsOn, Runnable action, String appName, VCNotification.Priority priority, VCNotification.TextPart... parts) {
        if (icon == null && (iconStr == null || getBitmap(iconStr) == null)) {
            iconStr = defaultIcon;
        }
        Bundle extra = new Bundle();
        extra.putInt("color", color);
        extra.putInt(PageRenderer.BORDER_COLOR, borderColor);
        extra.putString("name", appName);
        this.mConnectionManager.sendDropDownNotification(icon != null ? icon : getBitmap(iconStr), extra, duration, ttsOn, action, priority, parts);
        if (ttsOn && !this.mConnectionManager.hasTTS()) {
            StringBuffer buffer = new StringBuffer("Notification ");
            if (appName != null && !appName.isEmpty()) {
                buffer.append(" from " + appName + " , ");
            }
            for (VCNotification.TextPart textPart : parts) {
                buffer.append(textPart.getText());
            }
            speakText(8, buffer.toString());
        }
    }

    public void clearNotification() {
        this.mConnectionManager.clearNotification();
    }

    protected List<VCNotification.TextPart> getBrakedMessage(VCNotification.TextPart message) {
        return this.mConnectionManager.getBrakedMessage(message);
    }

    public void updateElements(String pageID) {
        this.mConnectionManager.updateElements(pageID);
        refresh();
    }

    public void updateElement(String pageID, String elementID, String attribute, Object value) {
        updateElement(null, pageID, elementID, attribute, value);
    }

    public void updateElement(String templateID, String pageID, String elementID, String attribute, Object value) {
        updateElement(templateID, pageID, elementID, attribute, value, false);
    }

    public void updateElement(String templateID, String pageID, String elementID, String attribute, Object value, boolean refresh) {
        if (!attribute.equalsIgnoreCase("name")) {
            this.mConnectionManager.updateElement(templateID, pageID, elementID, attribute, value, refresh);
            if (refresh) {
                refresh();
            }
        }
    }

    public void speakText(int id, String text) {
        if (text.length() > 2048) {
            text = text.substring(0, 2047);
            Log.e(TAG, "TTS Text was too long (" + text.length() + "), text has been automatically truncated to first 2048 characters.");
        }
        this.mConnectionManager.speakText(id, text);
        PupilDevice.sendDebugTTS(text);
    }

    public void stopTTS() {
        this.mConnectionManager.stopTTS();
    }

    protected boolean onButtonPress(ButtonType button, boolean longPress) {
        return false;
    }

    protected void onTapEvent() {
    }

    protected void onVoiceDetected() {
    }

    protected void onSilenceDetected() {
    }

    protected void onAudioReceived(byte[] data) {
    }

    public boolean hasNotification() {
        return this.mConnectionManager.hasNotification();
    }

    public VCNotification.VCNotificationType getNotificationType() {
        return this.mConnectionManager.getNotificationType();
    }

    public boolean onAcceptNotification() {
        return this.mConnectionManager.onAcceptNotification();
    }

    public void onDismissNotification() {
        this.mConnectionManager.onDismissNotification();
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.mConnectionListener = connectionListener;
    }

    void onStateChanged(ConnectionListener.ConnectionState state) {
        if (this.mConnectionListener != null) {
            this.mConnectionListener.onStateChanged(state);
        }
    }

    public synchronized void setStatusListener(StatusListener statusListener) {
        this.mStatusListeners.add(statusListener);
    }

    public synchronized void removeStatusListener(StatusListener statusListener) {
        this.mStatusListeners.remove(statusListener);
    }

    synchronized void onStatusChanged(PupilDevice.DeviceStatus deviceStatus) {
        for (StatusListener listener : this.mStatusListeners) {
            if (listener != null) {
                listener.onStatus(deviceStatus);
            }
        }
    }

    protected void onInfoChanged(PupilDevice.DeviceInfo deviceInfo) {
    }

    public void sendAnswerCall() {
        if (this.mConnectionManager != null) {
            this.mConnectionManager.sendAnswerCall();
        }
    }

    public void sendEndCall() {
        if (this.mConnectionManager != null) {
            this.mConnectionManager.sendEndCall();
        }
    }

    public void connectToMirrorService(InetAddress addr) {
        if (this.mConnectionManager != null) {
            this.mConnectionManager.connectToMirrorService(addr);
        }
    }
}
