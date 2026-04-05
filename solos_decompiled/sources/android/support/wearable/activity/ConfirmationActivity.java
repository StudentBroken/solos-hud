package android.support.wearable.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.R;
import android.support.wearable.view.ConfirmationOverlay;
import android.util.SparseIntArray;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(21)
public class ConfirmationActivity extends Activity implements ConfirmationOverlay.FinishedAnimationListener {
    private static final SparseIntArray CONFIRMATION_OVERLAY_TYPES = new SparseIntArray();
    public static final String EXTRA_ANIMATION_TYPE = "android.support.wearable.activity.extra.ANIMATION_TYPE";
    public static final String EXTRA_MESSAGE = "android.support.wearable.activity.extra.MESSAGE";
    public static final int FAILURE_ANIMATION = 3;
    public static final int OPEN_ON_PHONE_ANIMATION = 2;
    public static final int SUCCESS_ANIMATION = 1;

    static {
        CONFIRMATION_OVERLAY_TYPES.append(1, 0);
        CONFIRMATION_OVERLAY_TYPES.append(2, 2);
        CONFIRMATION_OVERLAY_TYPES.append(3, 1);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ConfirmationActivity);
        Intent intent = getIntent();
        int requestedType = intent.getIntExtra(EXTRA_ANIMATION_TYPE, 1);
        if (CONFIRMATION_OVERLAY_TYPES.indexOfKey(requestedType) < 0) {
            throw new IllegalArgumentException(new StringBuilder(38).append("Unknown type of animation: ").append(requestedType).toString());
        }
        int type = CONFIRMATION_OVERLAY_TYPES.get(requestedType);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        new ConfirmationOverlay().setType(type).setMessage(message).setFinishedAnimationListener(this).showOn(this);
    }

    @Override // android.support.wearable.view.ConfirmationOverlay.FinishedAnimationListener
    public void onAnimationFinished() {
        finish();
    }
}
