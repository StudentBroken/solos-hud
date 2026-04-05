package com.kopin.accessory.utility;

/* JADX INFO: loaded from: classes14.dex */
public class VolumeHelper {
    private static final byte HEADSET_VOLUME_RANGE = 26;
    private static final byte MAX_HEADSET_BOOST_VOLUME = -1;
    private static final byte MAX_HEADSET_VOLUME = 63;
    private static final byte MAX_VOLUME = 3;
    private static final byte MID_HEADSET_BOOST_VOLUME = -65;
    private static final byte MIN_HEADSET_BOOST_VOLUME = 127;
    private static final byte MIN_HEADSET_VOLUME = 37;
    private static final byte MUTE_VOLUME = 0;

    public static byte friendlyToHeadset(byte friendlyVolume) {
        switch (friendlyVolume) {
            case 0:
                return (byte) 0;
            case 1:
                return MIN_HEADSET_BOOST_VOLUME;
            case 2:
                return MID_HEADSET_BOOST_VOLUME;
            case 3:
                return MAX_HEADSET_BOOST_VOLUME;
            default:
                return MID_HEADSET_BOOST_VOLUME;
        }
    }

    public static byte headsetToFriendly(byte headsetVolume) {
        switch (headsetVolume) {
            case -65:
                return (byte) 2;
            case -1:
                return (byte) 3;
            case 0:
                return (byte) 0;
            case 127:
                return (byte) 1;
            default:
                return (byte) 2;
        }
    }
}
