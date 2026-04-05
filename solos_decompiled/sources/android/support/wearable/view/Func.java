package android.support.wearable.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.View;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(20)
class Func {
    Func() {
    }

    static float clamp(float value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        return value > max ? max : value;
    }

    static boolean getWindowOverscan(View v) {
        Context ctx = v.getContext();
        if (!(ctx instanceof Activity)) {
            return false;
        }
        Activity act = (Activity) ctx;
        int windowFlags = act.getWindow().getAttributes().flags;
        return (33554432 & windowFlags) != 0;
    }
}
