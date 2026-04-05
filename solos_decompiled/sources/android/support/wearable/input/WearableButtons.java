package android.support.wearable.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.wearable.R;
import android.support.wearable.internal.SharedLibraryVersion;
import android.view.WindowManager;
import com.google.android.wearable.input.WearableInputDevice;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public final class WearableButtons {

    @VisibleForTesting
    static final int LOC_BOTTOM_CENTER = 107;

    @VisibleForTesting
    static final int LOC_BOTTOM_LEFT = 106;

    @VisibleForTesting
    static final int LOC_BOTTOM_RIGHT = 108;

    @VisibleForTesting
    static final int LOC_EAST = 0;

    @VisibleForTesting
    static final int LOC_ENE = 1;

    @VisibleForTesting
    static final int LOC_ESE = 15;

    @VisibleForTesting
    static final int LOC_LEFT_BOTTOM = 105;

    @VisibleForTesting
    static final int LOC_LEFT_CENTER = 104;

    @VisibleForTesting
    static final int LOC_LEFT_TOP = 103;

    @VisibleForTesting
    static final int LOC_NE = 2;

    @VisibleForTesting
    static final int LOC_NNE = 3;

    @VisibleForTesting
    static final int LOC_NNW = 5;

    @VisibleForTesting
    static final int LOC_NORTH = 4;

    @VisibleForTesting
    static final int LOC_NW = 6;

    @VisibleForTesting
    static final int LOC_RIGHT_BOTTOM = 109;

    @VisibleForTesting
    static final int LOC_RIGHT_CENTER = 110;

    @VisibleForTesting
    static final int LOC_RIGHT_TOP = 111;
    private static final int LOC_ROUND_COUNT = 16;

    @VisibleForTesting
    static final int LOC_SE = 14;

    @VisibleForTesting
    static final int LOC_SOUTH = 12;

    @VisibleForTesting
    static final int LOC_SSE = 13;

    @VisibleForTesting
    static final int LOC_SSW = 11;

    @VisibleForTesting
    static final int LOC_SW = 10;

    @VisibleForTesting
    static final int LOC_TOP_CENTER = 101;

    @VisibleForTesting
    static final int LOC_TOP_LEFT = 102;

    @VisibleForTesting
    static final int LOC_TOP_RIGHT = 100;

    @VisibleForTesting
    static final int LOC_UNKNOWN = -1;

    @VisibleForTesting
    static final int LOC_WEST = 8;

    @VisibleForTesting
    static final int LOC_WNW = 7;

    @VisibleForTesting
    static final int LOC_WSW = 9;
    private static volatile int sButtonCount = -1;

    private WearableButtons() {
        throw new RuntimeException("WearableButtons should not be instantiated");
    }

    @Nullable
    public static final ButtonInfo getButtonInfo(Context context, int keycode) {
        if (!isApiAvailable()) {
            return null;
        }
        Bundle bundle = WearableInputDevice.getButtonInfo(context, keycode);
        if (!bundle.containsKey("x_key") || !bundle.containsKey("y_key")) {
            return null;
        }
        float screenLocationX = bundle.getFloat("x_key");
        float screenLocationY = bundle.getFloat("y_key");
        WindowManager wm = (WindowManager) context.getSystemService("window");
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);
        boolean isRound = context.getResources().getConfiguration().isScreenRound();
        return new ButtonInfo(keycode, screenLocationX, screenLocationY, getLocationZone(isRound, screenSize, screenLocationX, screenLocationY));
    }

    public static int getButtonCount(Context context) throws Throwable {
        int gottenValue;
        if (!isApiAvailable()) {
            return -1;
        }
        int gottenValue2 = sButtonCount;
        if (gottenValue2 == -1) {
            synchronized (WearableButtons.class) {
                try {
                    int[] buttonCodes = WearableInputDevice.getAvailableButtonKeyCodes(context);
                    gottenValue = buttonCodes.length;
                    sButtonCount = gottenValue;
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    return gottenValue;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }
        return gottenValue2;
    }

    @Nullable
    public static final Drawable getButtonIcon(Context context, int keycode) {
        ButtonInfo info = getButtonInfo(context, keycode);
        if (info == null) {
            return null;
        }
        return getButtonIconFromLocationZone(context, info.locationZone);
    }

    @VisibleForTesting
    static final RotateDrawable getButtonIconFromLocationZone(Context context, int locationZone) {
        int id;
        int degrees;
        switch (locationZone) {
            case 0:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = 0;
                break;
            case 1:
            case 2:
            case 3:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = -45;
                break;
            case 4:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = -90;
                break;
            case 5:
            case 6:
            case 7:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = -135;
                break;
            case 8:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = 180;
                break;
            case 9:
            case 10:
            case 11:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = 135;
                break;
            case 12:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = 90;
                break;
            case 13:
            case 14:
            case 15:
                id = R.drawable.ic_cc_settings_button_e;
                degrees = 45;
                break;
            case 100:
                id = R.drawable.ic_cc_settings_button_bottom;
                degrees = -90;
                break;
            case 101:
                id = R.drawable.ic_cc_settings_button_center;
                degrees = -90;
                break;
            case 102:
                id = R.drawable.ic_cc_settings_button_top;
                degrees = -90;
                break;
            case LOC_LEFT_TOP /* 103 */:
                id = R.drawable.ic_cc_settings_button_bottom;
                degrees = 180;
                break;
            case LOC_LEFT_CENTER /* 104 */:
                id = R.drawable.ic_cc_settings_button_center;
                degrees = 180;
                break;
            case LOC_LEFT_BOTTOM /* 105 */:
                id = R.drawable.ic_cc_settings_button_top;
                degrees = 180;
                break;
            case LOC_BOTTOM_LEFT /* 106 */:
                id = R.drawable.ic_cc_settings_button_bottom;
                degrees = 90;
                break;
            case LOC_BOTTOM_CENTER /* 107 */:
                id = R.drawable.ic_cc_settings_button_center;
                degrees = 90;
                break;
            case LOC_BOTTOM_RIGHT /* 108 */:
                id = R.drawable.ic_cc_settings_button_top;
                degrees = 90;
                break;
            case LOC_RIGHT_BOTTOM /* 109 */:
                id = R.drawable.ic_cc_settings_button_bottom;
                degrees = 0;
                break;
            case LOC_RIGHT_CENTER /* 110 */:
                id = R.drawable.ic_cc_settings_button_center;
                degrees = 0;
                break;
            case LOC_RIGHT_TOP /* 111 */:
                id = R.drawable.ic_cc_settings_button_top;
                degrees = 0;
                break;
            default:
                throw new IllegalArgumentException("Unexpected location zone");
        }
        RotateDrawable rotateIcon = new RotateDrawable();
        rotateIcon.setDrawable(context.getDrawable(id));
        rotateIcon.setFromDegrees(degrees);
        rotateIcon.setToDegrees(degrees);
        rotateIcon.setLevel(1);
        return rotateIcon;
    }

    public static final CharSequence getButtonLabel(Context context, int keycode) {
        int quadrantIndex;
        int[] buttonsInQuadrantCount = new int[4];
        int[] buttonCodes = WearableInputDevice.getAvailableButtonKeyCodes(context);
        for (int key : buttonCodes) {
            ButtonInfo info = getButtonInfo(context, key);
            if (info != null && (quadrantIndex = getQuadrantIndex(info.locationZone)) != -1) {
                buttonsInQuadrantCount[quadrantIndex] = buttonsInQuadrantCount[quadrantIndex] + 1;
            }
        }
        ButtonInfo info2 = getButtonInfo(context, keycode);
        int quadrantIndex2 = info2 != null ? getQuadrantIndex(info2.locationZone) : -1;
        if (info2 == null) {
            return null;
        }
        return context.getString(getFriendlyLocationZoneStringId(info2.locationZone, quadrantIndex2 == -1 ? 0 : buttonsInQuadrantCount[quadrantIndex2]));
    }

    private static int getQuadrantIndex(int locationZone) {
        switch (locationZone) {
            case 1:
            case 2:
            case 3:
                return 0;
            case 4:
            case 8:
            case 12:
            default:
                return -1;
            case 5:
            case 6:
            case 7:
                return 1;
            case 9:
            case 10:
            case 11:
                return 2;
            case 13:
            case 14:
            case 15:
                return 3;
        }
    }

    @VisibleForTesting
    static int getFriendlyLocationZoneStringId(int locationZone, int buttonsInQuadrantCount) {
        if (buttonsInQuadrantCount == 2) {
            switch (locationZone) {
                case 1:
                    return R.string.buttons_round_top_right_lower;
                case 2:
                case 3:
                    return R.string.buttons_round_top_right_upper;
                case 5:
                case 6:
                    return R.string.buttons_round_top_left_upper;
                case 7:
                    return R.string.buttons_round_top_left_lower;
                case 9:
                case 10:
                    return R.string.buttons_round_bottom_right_upper;
                case 11:
                    return R.string.buttons_round_bottom_right_lower;
                case 13:
                    return R.string.buttons_round_bottom_left_lower;
                case 14:
                case 15:
                    return R.string.buttons_round_bottom_left_upper;
            }
        }
        switch (locationZone) {
            case 0:
                return R.string.buttons_round_center_right;
            case 1:
            case 2:
            case 3:
                return R.string.buttons_round_top_right;
            case 4:
                return R.string.buttons_round_top_center;
            case 5:
            case 6:
            case 7:
                return R.string.buttons_round_top_left;
            case 8:
                return R.string.buttons_round_center_left;
            case 9:
            case 10:
            case 11:
                return R.string.buttons_round_bottom_left;
            case 12:
                return R.string.buttons_round_bottom_center;
            case 13:
            case 14:
            case 15:
                return R.string.buttons_round_bottom_right;
            case 100:
                return R.string.buttons_rect_top_right;
            case 101:
                return R.string.buttons_rect_top_center;
            case 102:
                return R.string.buttons_rect_top_left;
            case LOC_LEFT_TOP /* 103 */:
                return R.string.buttons_rect_left_top;
            case LOC_LEFT_CENTER /* 104 */:
                return R.string.buttons_rect_left_center;
            case LOC_LEFT_BOTTOM /* 105 */:
                return R.string.buttons_rect_left_bottom;
            case LOC_BOTTOM_LEFT /* 106 */:
                return R.string.buttons_rect_bottom_left;
            case LOC_BOTTOM_CENTER /* 107 */:
                return R.string.buttons_rect_bottom_center;
            case LOC_BOTTOM_RIGHT /* 108 */:
                return R.string.buttons_rect_bottom_right;
            case LOC_RIGHT_BOTTOM /* 109 */:
                return R.string.buttons_rect_right_bottom;
            case LOC_RIGHT_CENTER /* 110 */:
                return R.string.buttons_rect_right_center;
            case LOC_RIGHT_TOP /* 111 */:
                return R.string.buttons_rect_right_top;
            default:
                throw new IllegalArgumentException("Unexpected location zone");
        }
    }

    @VisibleForTesting
    static int getLocationZone(boolean isRound, Point screenSize, float screenLocationX, float screenLocationY) {
        if (screenLocationX == Float.MAX_VALUE || screenLocationY == Float.MAX_VALUE) {
            return -1;
        }
        if (isRound) {
            return getLocationZoneRound(screenSize, screenLocationX, screenLocationY);
        }
        return getLocationZoneRectangular(screenSize, screenLocationX, screenLocationY);
    }

    private static int getLocationZoneRound(Point screenSize, float screenLocationX, float screenLocationY) {
        float cartesianX = screenLocationX - (screenSize.x / 2);
        float cartesianY = (screenSize.y / 2) - screenLocationY;
        double angle = Math.atan2(cartesianY, cartesianX);
        if (angle < 0.0d) {
            angle += 6.283185307179586d;
        }
        return Math.round((float) (angle / 0.39269908169872414d)) % 16;
    }

    private static int getLocationZoneRectangular(Point screenSize, float screenLocationX, float screenLocationY) {
        float deltaFromRight = screenSize.x - screenLocationX;
        float deltaFromBottom = screenSize.y - screenLocationY;
        float minDelta = Math.min(screenLocationX, Math.min(deltaFromRight, Math.min(screenLocationY, deltaFromBottom)));
        if (minDelta == screenLocationX) {
            switch (whichThird(screenSize.y, screenLocationY)) {
                case 0:
                    return LOC_LEFT_TOP;
                case 1:
                    return LOC_LEFT_CENTER;
                default:
                    return LOC_LEFT_BOTTOM;
            }
        }
        if (minDelta == deltaFromRight) {
            switch (whichThird(screenSize.y, screenLocationY)) {
                case 0:
                    return LOC_RIGHT_TOP;
                case 1:
                    return LOC_RIGHT_CENTER;
                default:
                    return LOC_RIGHT_BOTTOM;
            }
        }
        if (minDelta == screenLocationY) {
            switch (whichThird(screenSize.x, screenLocationX)) {
                case 0:
                    return 102;
                case 1:
                    return 101;
                default:
                    return 100;
            }
        }
        switch (whichThird(screenSize.x, screenLocationX)) {
            case 0:
                return LOC_BOTTOM_LEFT;
            case 1:
                return LOC_BOTTOM_CENTER;
            default:
                return LOC_BOTTOM_RIGHT;
        }
    }

    private static int whichThird(float screenLength, float screenLocation) {
        if (screenLocation <= screenLength / 3.0f) {
            return 0;
        }
        if (screenLocation <= (2.0f * screenLength) / 3.0f) {
            return 1;
        }
        return 2;
    }

    private static boolean isApiAvailable() {
        return SharedLibraryVersion.version() >= 1;
    }

    public static final class ButtonInfo {
        private final int keycode;
        private final int locationZone;
        private final float x;
        private final float y;

        public int getKeycode() {
            return this.keycode;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        @VisibleForTesting
        ButtonInfo(int keycode, float x, float y, int locationZone) {
            this.keycode = keycode;
            this.x = x;
            this.y = y;
            this.locationZone = locationZone;
        }
    }
}
