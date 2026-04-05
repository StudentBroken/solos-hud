package com.kopin.pupil.util;

import com.kopin.accessory.utility.CallHelper;

/* JADX INFO: loaded from: classes25.dex */
public class VolumeHelper {
    private static final byte[] HEADSET_VOLUME_VALUES = {0, 1, 2, 3, 4, 5, 7, 9, CallHelper.CallState.SCO_CONNECTED, CallHelper.CallState.CALL_IN_PROGRESS, 15};
    private static final byte MAX_HEADSET_VOLUME = -16;
    private static final byte MAX_VOLUME = 10;
    private static final byte MUTE_VOLUME = 0;

    public static byte friendlyToHeadset(byte friendlyVolume) {
        if (friendlyVolume >= 10) {
            return MAX_HEADSET_VOLUME;
        }
        byte finalVolume = HEADSET_VOLUME_VALUES[friendlyVolume];
        return (byte) (finalVolume << 4);
    }

    public static byte headsetToFriendly(byte headsetVolume) {
        byte headsetVolume2 = (byte) ((headsetVolume >> 4) & 15);
        byte finalVolume = 0;
        for (int i = 0; i < HEADSET_VOLUME_VALUES.length && HEADSET_VOLUME_VALUES[i] <= headsetVolume2; i++) {
            finalVolume = (byte) i;
        }
        return finalVolume;
    }
}
