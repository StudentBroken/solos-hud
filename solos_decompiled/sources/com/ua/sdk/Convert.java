package com.ua.sdk;

/* JADX INFO: loaded from: classes65.dex */
public final class Convert {
    public static final double FEET_IN_METER = 3.28084d;
    public static final long HOURS_IN_DAY = 24;
    public static final double INCH_IN_METER = 39.3701d;
    public static final double JOULES_IN_KCALORIE = 4184.0d;
    public static final int JOULES_TO_KJ = 1000;
    public static final double KCALORIES_IN_JOULE = 2.3900573E-4d;
    public static final double KJ_TO_LARGE_CALORIES = 0.239005736d;
    public static final double LBS_IN_KG = 2.20462d;
    public static final double METER_IN_KILOMETER = 1000.0d;
    public static final double METER_IN_MILE = 1609.344d;
    public static final double MILE_IN_KM = 0.621371d;
    public static final long MILLISECONDS_IN_DAY = 86400000;
    public static final long MILLISECONDS_IN_HOUR = 3600000;
    public static final long MILLISECONDS_IN_MINUTE = 60000;
    public static final long MILLISECONDS_IN_SECOND = 1000;
    public static final long MILLISECONDS_IN_WEEK = 604800000;
    public static final long MINUTES_IN_DAY = 1440;
    public static final long MINUTES_IN_HOUR = 60;
    public static final long SECONDS_IN_DAY = 86400;
    public static final long SECONDS_IN_HOUR = 3600;
    public static final long SECONDS_IN_MINUTE = 60;

    private Convert() {
    }

    public static Double inchToMeter(Double inch) {
        if (inch != null) {
            return Double.valueOf(inch.doubleValue() / 39.3701d);
        }
        return null;
    }

    public static Double kilometerPerHourToMeterPerSec(Double kilometerPerHour) {
        if (kilometerPerHour != null) {
            return Double.valueOf((kilometerPerHour.doubleValue() * 1000.0d) / 3600.0d);
        }
        return null;
    }

    public static Double kilometerToMeter(Double kilometer) {
        if (kilometer != null) {
            return Double.valueOf(kilometer.doubleValue() * 1000.0d);
        }
        return null;
    }

    public static Double kilometerToMile(Double kilometer) {
        if (kilometer == null) {
            return null;
        }
        Double meter = kilometerToMeter(kilometer);
        return meterToMile(meter);
    }

    public static Double kgToLbs(Double kg) {
        if (kg != null) {
            return Double.valueOf(kg.doubleValue() * 2.20462d);
        }
        return kg;
    }

    public static Double lbsToKg(Double lbs) {
        if (lbs != null) {
            return Double.valueOf(lbs.doubleValue() / 2.20462d);
        }
        return null;
    }

    public static Double meterToInch(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() * 39.3701d);
        }
        return null;
    }

    public static Double meterToFoot(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() * 3.28084d);
        }
        return null;
    }

    public static Double meterToKilometer(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() / 1000.0d);
        }
        return null;
    }

    public static Double meterToMile(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() / 1609.344d);
        }
        return null;
    }

    public static Double meterPerSecToMeterPerHour(Double meterPerSec) {
        if (meterPerSec != null) {
            return Double.valueOf(meterPerSec.doubleValue() * 3600.0d);
        }
        return null;
    }

    public static Double meterPerSecToMilePerHour(Double meterPerSec) {
        if (meterPerSec != null) {
            return Double.valueOf((meterPerSec.doubleValue() * 3600.0d) / 1609.344d);
        }
        return null;
    }

    public static Double meterPerSecToKilometerPerHour(Double meterPerSec) {
        if (meterPerSec != null) {
            return Double.valueOf((meterPerSec.doubleValue() * 3600.0d) / 1000.0d);
        }
        return null;
    }

    public static Double meterPerHourToMeterPerSecond(Double meterPerHour) {
        if (meterPerHour != null) {
            return Double.valueOf(meterPerHour.doubleValue() / 3600.0d);
        }
        return null;
    }

    public static Double meterPerHourToMilePerHour(Double meterPerHour) {
        if (meterPerHour != null) {
            return Double.valueOf(meterPerHour.doubleValue() / 1609.344d);
        }
        return null;
    }

    public static Double milePerHourToMeterPerSecond(Double milePerHour) {
        if (milePerHour != null) {
            return Double.valueOf((milePerHour.doubleValue() * 1609.344d) / 3600.0d);
        }
        return null;
    }

    public static Double milePerHourToMeterPerHour(Double milePerHour) {
        if (milePerHour != null) {
            return Double.valueOf(milePerHour.doubleValue() * 1609.344d);
        }
        return null;
    }

    public static Double minPerMeterToSecPerMeter(Double minPerMeter) {
        if (minPerMeter != null) {
            return Double.valueOf(minPerMeter.doubleValue() * 60.0d);
        }
        return null;
    }

    public static Double minPerMeterToMinPerMile(Double minPerMeter) {
        if (minPerMeter != null) {
            return Double.valueOf(minPerMeter.doubleValue() * 1609.344d);
        }
        return null;
    }

    public static Double mileToKilometer(Double mile) {
        if (mile == null) {
            return null;
        }
        Double meter = mileToMeter(mile);
        return meterToKilometer(meter);
    }

    public static Double mileToMeter(Double mile) {
        if (mile != null) {
            return Double.valueOf(mile.doubleValue() * 1609.344d);
        }
        return null;
    }

    public static Double minPerMileToSecPerMeter(Double minPerMile) {
        if (minPerMile != null) {
            return Double.valueOf((minPerMile.doubleValue() * 60.0d) / 1609.344d);
        }
        return null;
    }

    public static Double minPerMileToMinPerMeter(Double minPerMile) {
        if (minPerMile != null) {
            return Double.valueOf(minPerMile.doubleValue() / 1609.344d);
        }
        return null;
    }

    public static Double minPerKilometerToSecPerMeter(Double minPerKilometer) {
        if (minPerKilometer != null) {
            return Double.valueOf((minPerKilometer.doubleValue() * 60.0d) / 1000.0d);
        }
        return null;
    }

    public static Double secPerMeterToMinPerMeter(Double secPerMeter) {
        if (secPerMeter != null) {
            return Double.valueOf(secPerMeter.doubleValue() / 60.0d);
        }
        return null;
    }

    public static Double secPerMeterToMinPerMile(Double secPerMeter) {
        if (secPerMeter != null) {
            return Double.valueOf((secPerMeter.doubleValue() * 1609.344d) / 60.0d);
        }
        return null;
    }

    public static Double secPerMeterToMinPerKilometer(Double secPerMeter) {
        if (secPerMeter != null) {
            return Double.valueOf((secPerMeter.doubleValue() * 1000.0d) / 60.0d);
        }
        return null;
    }

    public static Double caloriesToJoules(Double calories) {
        if (calories != null) {
            return Double.valueOf(calories.doubleValue() * 4184.0d);
        }
        return null;
    }

    public static Double joulesToCalories(Double joules) {
        if (joules != null) {
            return Double.valueOf(joules.doubleValue() * 2.3900573E-4d);
        }
        return null;
    }

    public static Double secondsPerMeterToMinPerMile(Double secondsPerMeter) {
        if (secondsPerMeter != null) {
            return Double.valueOf((secondsPerMeter.doubleValue() * 1000.0d) / 37.28226d);
        }
        return null;
    }
}
