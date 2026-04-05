package com.kopin.solos.share;

import android.content.Context;
import com.kopin.solos.share.trainingpeaks.TPHelper;

/* JADX INFO: loaded from: classes4.dex */
public class Share {

    public interface ShareListener {
        void onResponse(ShareResponse shareResponse);
    }

    public static class ShareResponse {
        public Object serialisedResponse;
        public int responseCode = 601;
        public String rawResponse = "Unknown Error";

        public boolean isSuccess() {
            return this.responseCode >= 200 && this.responseCode < 300;
        }
    }

    public static void importTrainingWorkouts(Platforms platform, Context context, ShareListener listener) {
        switch (platform) {
            case TrainingPeaks:
                TPHelper.importWorkouts(context, listener);
                break;
        }
    }
}
