package android.support.wearable.input;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes33.dex */
public class RemoteInputConstants {
    public static final String EXTRA_DISALLOW_EMOJI = "android.support.wearable.input.extra.DISALLOW_EMOJI";
    public static final String EXTRA_INPUT_ACTION_TYPE = "android.support.wearable.input.extra.INPUT_ACTION_TYPE";
    public static final int INPUT_ACTION_TYPE_DONE = 2;
    public static final int INPUT_ACTION_TYPE_GO = 3;
    public static final int INPUT_ACTION_TYPE_SEARCH = 1;
    public static final int INPUT_ACTION_TYPE_SEND = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InputActionType {
    }
}
