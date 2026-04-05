package com.kopin.solos.navigate.geolocation;

/* JADX INFO: loaded from: classes47.dex */
public class TurnBasedDirections {

    public enum Turn {
        STRAIGHT_ON,
        TURN_AROUND,
        SLIGHT_LEFT,
        LEFT,
        HARD_LEFT,
        SLIGHT_RIGHT,
        RIGHT,
        HARD_RIGHT;

        public static Turn fromValues(float bearingFrom, float bearingTo) {
            float turnBearing = bearingTo - bearingFrom;
            if (turnBearing < -179.0f) {
                turnBearing += 360.0f;
            } else if (turnBearing > 179.0f) {
                turnBearing -= 360.0f;
            }
            if (turnBearing < -170.0f || turnBearing >= 170.0f) {
                return TURN_AROUND;
            }
            if (turnBearing < -125.0f) {
                return HARD_LEFT;
            }
            if (turnBearing < -35.0f) {
                return LEFT;
            }
            if (turnBearing < -10.0f) {
                return SLIGHT_LEFT;
            }
            if (turnBearing < 10.0f) {
                return STRAIGHT_ON;
            }
            if (turnBearing < 35.0f) {
                return SLIGHT_RIGHT;
            }
            if (turnBearing < 125.0f) {
                return RIGHT;
            }
            if (turnBearing < 170.0f) {
                return HARD_RIGHT;
            }
            return STRAIGHT_ON;
        }

        public boolean sameDirection(Turn as) {
            switch (this) {
                case HARD_LEFT:
                case LEFT:
                case SLIGHT_LEFT:
                    return as.equals(HARD_LEFT) || as.equals(LEFT) || as.equals(SLIGHT_LEFT);
                case HARD_RIGHT:
                case RIGHT:
                case SLIGHT_RIGHT:
                    return as.equals(HARD_RIGHT) || as.equals(RIGHT) || as.equals(SLIGHT_RIGHT);
                default:
                    return as.equals(this);
            }
        }
    }

    public static DirectionContainer FindDirection(float bearing, float bearing1) {
        int initialBearing = (int) bearing;
        int newBearing = (int) bearing1;
        int turnBearing = newBearing - initialBearing;
        if (turnBearing < -179) {
            turnBearing += 360;
        } else if (turnBearing > 179) {
            turnBearing -= 360;
        }
        DirectionContainer dC = new DirectionContainer();
        dC.bearing = turnBearing;
        if (turnBearing < -170 || turnBearing >= 170) {
            dC.instruction = "Turn Around";
        } else if (turnBearing < -125) {
            dC.instruction = "Turn Hard Left";
        } else if (turnBearing < -35) {
            dC.instruction = "Turn Left";
        } else if (turnBearing < -10) {
            dC.instruction = "Turn Slight Left";
        } else if (turnBearing < 10) {
            dC.instruction = "Straight On";
        } else if (turnBearing < 35) {
            dC.instruction = "Turn Slight Right";
        } else if (turnBearing < 125) {
            dC.instruction = "Turn Right";
        } else if (turnBearing < 170) {
            dC.instruction = "Turn Hard Right";
        }
        return dC;
    }

    public static String GetTurn(int turnBearing) {
        if (turnBearing < -170 || turnBearing >= 170) {
            return "Turn Around";
        }
        if (turnBearing < -125) {
            return "Turn Hard Left";
        }
        if (turnBearing < -35) {
            return "Turn Left";
        }
        if (turnBearing < -10) {
            return "Turn Slight Left";
        }
        if (turnBearing < 10) {
            return "Straight On";
        }
        if (turnBearing < 35) {
            return "Turn Slight Right";
        }
        if (turnBearing < 125) {
            return "Turn Right";
        }
        if (turnBearing < 170) {
            return "Turn Hard Right";
        }
        return "";
    }

    public static String GetCompassDirection(int bearing) {
        int turnBearing = bearing - 90;
        if (turnBearing < 0) {
            turnBearing += 360;
        }
        if (turnBearing < 23 || turnBearing >= 337) {
            return "Head North";
        }
        if (turnBearing < 68) {
            return "Head North East";
        }
        if (turnBearing < 113) {
            return "Head East";
        }
        if (turnBearing < 158) {
            return "Head South East";
        }
        if (turnBearing < 203) {
            return "Head South";
        }
        if (turnBearing < 248) {
            return "Head South West";
        }
        if (turnBearing < 293) {
            return "Head West";
        }
        return "Head North West";
    }
}
