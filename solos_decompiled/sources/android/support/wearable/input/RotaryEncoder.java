package android.support.wearable.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.wearable.internal.SharedLibraryVersion;
import android.view.MotionEvent;
import com.google.android.wearable.input.RotaryEncoderHelper;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class RotaryEncoder {
    private static final float FALLBACK_SCROLL_FACTOR = 64.0f;

    private static boolean isLibAvailable() {
        return SharedLibraryVersion.version() >= 1;
    }

    public static boolean isFromRotaryEncoder(MotionEvent ev) {
        return isLibAvailable() && RotaryEncoderHelper.isFromRotaryEncoder(ev);
    }

    public static float getRotaryAxisValue(MotionEvent ev) {
        if (isLibAvailable()) {
            return RotaryEncoderHelper.getRotaryAxisValue(ev);
        }
        return 0.0f;
    }

    public static float getScaledScrollFactor(Context context) {
        return isLibAvailable() ? RotaryEncoderHelper.getScaledScrollFactor(context) : FALLBACK_SCROLL_FACTOR;
    }

    private RotaryEncoder() {
    }
}
