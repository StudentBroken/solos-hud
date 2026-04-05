package com.kopin.solos.navigation;

import com.kopin.solos.core.R;

/* JADX INFO: loaded from: classes37.dex */
public enum DirectionArrows {
    DEFAULT(R.drawable.ic_straight, null),
    SHARP_LEFT(R.drawable.ic_sharp_left, new String[]{"sharp left", "hard left", "turn-sharp-left"}),
    SLIGHT_LEFT(R.drawable.ic_slight_left, new String[]{"slight left", "turn-slight-left"}),
    LEFT(R.drawable.ic_left, new String[]{"left"}),
    SHARP_RIGHT(R.drawable.ic_sharp_right, new String[]{"sharp right", "hard right", "turn-sharp-right"}),
    SLIGHT_RIGHT(R.drawable.ic_slight_right, new String[]{"slight right", "turn-slight-right"}),
    RIGHT(R.drawable.ic_right, new String[]{"right"}),
    CONTINUE_AHEAD(R.drawable.ic_straight, new String[]{"continue", "straight", "merge"}),
    FIRST_EXIT(R.drawable.ic_roundabout_1, new String[]{"1st exit"}),
    SECOND_EXIT(R.drawable.ic_roundabout_2, new String[]{"2nd exit"}),
    THIRD_EXIT(R.drawable.ic_roundabout_3, new String[]{"3rd exit"}),
    FOURTH_EXIT(R.drawable.ic_roundabout_4, new String[]{"4th exit"}),
    FIFTH_EXIT(R.drawable.ic_roundabout_5, new String[]{"5th exit"}),
    SIXTH_EXIT(R.drawable.ic_roundabout_6, new String[]{"6th exit"}),
    DESTINATION(R.drawable.ic_straight, new String[]{"destination"}),
    BACK(R.drawable.ic_back, null),
    TURNAROUND(R.drawable.ic_back, null);

    private final String[] matches;
    public final int resId;

    DirectionArrows(int id, String[] instructions) {
        this.resId = id;
        this.matches = instructions;
    }

    static DirectionArrows fromInstruction(String theString) {
        if (theString == null || theString.isEmpty()) {
            return DEFAULT;
        }
        String instruction = theString.toLowerCase();
        for (DirectionArrows arrow : values()) {
            if (arrow.matches != null) {
                for (String m : arrow.matches) {
                    if (instruction.contains(m)) {
                        return arrow;
                    }
                }
            }
        }
        return BACK;
    }

    public boolean isBasic() {
        return this == DEFAULT || this == CONTINUE_AHEAD || this == LEFT || this == RIGHT || this == BACK;
    }

    static int getArrow(String theString, String currentInstruction) {
        if (theString == null || currentInstruction == null) {
            return R.drawable.ic_straight;
        }
        if (theString.toLowerCase().contains("sharp left") || theString.contains("hard left") || theString.contains("turn-sharp-left")) {
            return R.drawable.ic_sharp_left;
        }
        if (theString.toLowerCase().contains("sharp right") || theString.contains("hard right") || theString.contains("turn-sharp-right")) {
            return R.drawable.ic_sharp_right;
        }
        if (theString.toLowerCase().contains("slight left") || theString.contains("turn-slight-left")) {
            return R.drawable.ic_slight_left;
        }
        if (theString.toLowerCase().contains("slight right") || theString.contains("turn-slight-right")) {
            return R.drawable.ic_slight_right;
        }
        if (theString.toLowerCase().contains("continue") || theString.contains("straight") || theString.contains("merge")) {
            return R.drawable.ic_straight;
        }
        if (currentInstruction.toLowerCase().contains("1st exit")) {
            return R.drawable.ic_roundabout_1;
        }
        if (currentInstruction.toLowerCase().contains("2nd exit")) {
            return R.drawable.ic_roundabout_2;
        }
        if (currentInstruction.toLowerCase().contains("3rd exit")) {
            return R.drawable.ic_roundabout_3;
        }
        if (currentInstruction.toLowerCase().contains("4th exit")) {
            return R.drawable.ic_roundabout_4;
        }
        if (currentInstruction.toLowerCase().contains("5th exit")) {
            return R.drawable.ic_roundabout_5;
        }
        if (currentInstruction.toLowerCase().contains("6th exit")) {
            return R.drawable.ic_roundabout_6;
        }
        if (theString.toLowerCase().contains("destination") || theString.toLowerCase().contains("straight")) {
            return R.drawable.ic_straight;
        }
        if (theString.toLowerCase().contains("left")) {
            return R.drawable.ic_left;
        }
        if (theString.toLowerCase().contains("right")) {
            return R.drawable.ic_right;
        }
        return R.drawable.ic_back;
    }
}
