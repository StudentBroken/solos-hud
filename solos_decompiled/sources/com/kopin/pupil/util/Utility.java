package com.kopin.pupil.util;

import android.content.Context;
import android.content.res.Resources;
import com.facebook.internal.ServerProtocol;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes25.dex */
public class Utility {

    public static class ApplicationInfo {
        private String mData;
        private int mId;
        private String mName;
        private int mVersion;

        ApplicationInfo(String name) {
            this(name, 1);
        }

        ApplicationInfo(String name, int version) {
            this.mName = name;
            this.mVersion = version;
        }

        public String getData() {
            return this.mData;
        }

        public int getId() {
            return this.mId;
        }

        public String getName() {
            return this.mName;
        }

        public int getVersion() {
            return this.mVersion;
        }

        public void setData(String data) {
            this.mData = data;
        }

        public void setId(int id) {
            this.mId = id;
        }

        public void setName(String mName) {
            this.mName = mName;
        }
    }

    public static ApplicationInfo pullInfoFromXml(XmlPullParser xmlPage, String pageName) {
        while (xmlPage.getEventType() != 1) {
            try {
                if (xmlPage.getEventType() == 2 && (xmlPage.getName().equalsIgnoreCase("page") || xmlPage.getName().equalsIgnoreCase("cloudpage"))) {
                    Map<String, String> attributes = getAttributes(xmlPage);
                    if (!attributes.containsKey("name") && pageName == null) {
                        return null;
                    }
                    if (attributes.containsKey(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION)) {
                        int version = intFromString(attributes.get(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION), 1);
                        if (pageName == null) {
                            return new ApplicationInfo(attributes.get("name"), version);
                        }
                        return new ApplicationInfo(pageName, version);
                    }
                    if (pageName == null) {
                        return new ApplicationInfo(attributes.get("name"));
                    }
                    return new ApplicationInfo(pageName);
                }
                xmlPage.next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Map<String, String> getAttributes(XmlPullParser parser) throws Exception {
        int count = parser.getAttributeCount();
        if (count != -1) {
            Map<String, String> attibutes = new HashMap<>(count);
            for (int x = 0; x < count; x++) {
                attibutes.put(parser.getAttributeName(x), parser.getAttributeValue(x));
            }
            return attibutes;
        }
        throw new Exception("Required entity attributes missing");
    }

    public static int intFromString(String string, int def) {
        try {
            int def2 = Integer.valueOf(string).intValue();
            return def2;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static String getXMLString(int id, Context context) {
        StringBuilder builder = new StringBuilder();
        Resources resources = context.getResources();
        InputStream stream = resources.openRawResource(id);
        try {
            char[] buffer = new char[2048];
            InputStreamReader reader = new InputStreamReader(stream);
            while (true) {
                int size = reader.read(buffer);
                if (size == -1) {
                    break;
                }
                builder.append(buffer, 0, size);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return builder.toString();
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
