package com.kopin.pupil.update.util;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes13.dex */
public class FirmwareManifest {
    private static final String ATTR_BANK = "bank";
    private static final String ATTR_DATA_INDEX = "n";
    private static final String ATTR_DATA_LENGTH = "num_data";
    private static final String ATTR_DATA_VALUE = "d";
    private static final String ATTR_FILENAME = "filename";
    private static final String ATTR_LOCALTIME = "localtime";
    private static final String ATTR_NUMSEGMENTS = "num_seg";
    private static final String ATTR_SEGMENT_NUMBER = "num";
    private static final String ATTR_SIGNATURE_LENGTH = "byte_length";
    private static final String ATTR_START_ADDRESS = "start_address";
    private static final String ATTR_VER_MAJOR = "major_ver_num";
    private static final String ATTR_VER_MINOR = "minor_ver_num";
    private static final String ATTR_VER_SUB_MINOR = "subminor_ver_num";
    private static final String TAG_BINFILE = "binfile";
    private static final String TAG_DATA = "data";
    private static final String TAG_SEGMENT = "segment";
    private static final String TAG_SEGMENTS = "segments";
    private static final String TAG_SIGNATURE = "signature";
    private static final String TAG_TIME = "time";
    private static final String TAG_VERSION = "version";

    private FirmwareManifest() {
    }

    public static FirmwareSignature fromInputStream(InputStream xmlStream) {
        return parseXml(xmlStream);
    }

    private static String getAttrValue(XmlPullParser mXmlParser, String forAttr) {
        return mXmlParser.getAttributeValue(null, forAttr);
    }

    private static int getIntAttrValue(XmlPullParser mXmlParser, String forAttr) {
        String intStr = mXmlParser.getAttributeValue(null, forAttr);
        if (intStr != null) {
            return Integer.parseInt(intStr);
        }
        return 0;
    }

    private static byte getByteAttrValue(XmlPullParser mXmlParser, String forAttr) {
        String intStr = mXmlParser.getAttributeValue(null, forAttr);
        if (intStr != null) {
            return Byte.parseByte(intStr);
        }
        return (byte) 0;
    }

    private static FirmwareSignature parseXml(InputStream mXmlStream) {
        int segmentIdx;
        FirmwareSignature fwSignature = new FirmwareSignature();
        try {
            XmlPullParserFactory mXmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser mXmlParser = mXmlFactoryObject.newPullParser();
            mXmlParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            mXmlParser.setInput(mXmlStream, null);
            FirmwareSegment currentSegment = null;
            int event = mXmlParser.getEventType();
            boolean inSig = false;
            byte[] curSig = null;
            int segmentIdx2 = 0;
            while (event != 1) {
                String tagName = mXmlParser.getName();
                switch (event) {
                    case 2:
                        if (tagName.equals(TAG_SEGMENT)) {
                            currentSegment = new FirmwareSegment();
                            currentSegment.mSegmentNumber = getIntAttrValue(mXmlParser, ATTR_SEGMENT_NUMBER);
                            currentSegment.mStartAddress = getIntAttrValue(mXmlParser, ATTR_START_ADDRESS);
                            currentSegment.mDataLength = getIntAttrValue(mXmlParser, ATTR_DATA_LENGTH);
                            currentSegment.mBank = getIntAttrValue(mXmlParser, ATTR_BANK);
                            segmentIdx = segmentIdx2;
                        } else if (tagName.equals("version")) {
                            fwSignature.mVersion = new byte[3];
                            fwSignature.mVersion[0] = getByteAttrValue(mXmlParser, ATTR_VER_MAJOR);
                            fwSignature.mVersion[1] = getByteAttrValue(mXmlParser, ATTR_VER_MINOR);
                            fwSignature.mVersion[2] = getByteAttrValue(mXmlParser, ATTR_VER_SUB_MINOR);
                            segmentIdx = segmentIdx2;
                        } else if (tagName.equals(TAG_BINFILE)) {
                            fwSignature.mImageFileName = getAttrValue(mXmlParser, ATTR_FILENAME);
                            fwSignature.mSegments = new FirmwareSegment[getIntAttrValue(mXmlParser, ATTR_NUMSEGMENTS)];
                            segmentIdx = segmentIdx2;
                        } else if (tagName.equals(TAG_TIME)) {
                            fwSignature.mDate = getAttrValue(mXmlParser, ATTR_LOCALTIME);
                            segmentIdx = segmentIdx2;
                        } else if (tagName.equals(TAG_SIGNATURE)) {
                            curSig = new byte[getIntAttrValue(mXmlParser, ATTR_SIGNATURE_LENGTH)];
                            inSig = true;
                            segmentIdx = segmentIdx2;
                        } else if (tagName.equals("data")) {
                            int idx = getIntAttrValue(mXmlParser, ATTR_DATA_INDEX);
                            if (inSig && idx >= 0 && idx < curSig.length) {
                                byte b = getByteAttrValue(mXmlParser, "d");
                                curSig[idx] = b;
                            }
                            segmentIdx = segmentIdx2;
                        } else {
                            segmentIdx = segmentIdx2;
                        }
                        break;
                    case 3:
                        if (tagName.equals(TAG_SEGMENT)) {
                            segmentIdx = segmentIdx2 + 1;
                            fwSignature.mSegments[segmentIdx2] = currentSegment;
                            currentSegment = null;
                        } else if (tagName.equals(TAG_SIGNATURE)) {
                            inSig = false;
                            currentSegment.mSignature = curSig;
                            curSig = null;
                            segmentIdx = segmentIdx2;
                        } else {
                            segmentIdx = segmentIdx2;
                        }
                        break;
                    case 4:
                        if (inSig) {
                            String hexText = mXmlParser.getText().trim();
                            for (int i = 0; i < hexText.length(); i += 2) {
                                byte b2 = (byte) Integer.parseInt(hexText.substring(i, i + 2), 16);
                                curSig[i / 2] = b2;
                            }
                            segmentIdx = segmentIdx2;
                        } else {
                            segmentIdx = segmentIdx2;
                        }
                        break;
                    default:
                        segmentIdx = segmentIdx2;
                        break;
                }
                event = mXmlParser.next();
                segmentIdx2 = segmentIdx;
            }
            try {
                mXmlStream.close();
            } catch (IOException e) {
            }
        } catch (Exception e2) {
            fwSignature = null;
            try {
                mXmlStream.close();
            } catch (IOException e3) {
            }
        } catch (Throwable th) {
            try {
                mXmlStream.close();
            } catch (IOException e4) {
            }
            throw th;
        }
        return fwSignature;
    }

    static class FirmwareSegment {
        public int mBank;
        public int mDataLength;
        public int mSegmentNumber;
        public byte[] mSignature;
        public long mStartAddress;

        FirmwareSegment() {
        }
    }

    static class FirmwareSignature {
        public String mDate;
        public String mImageFileName;
        public FirmwareSegment[] mSegments;
        public byte[] mVersion;

        FirmwareSignature() {
        }
    }
}
