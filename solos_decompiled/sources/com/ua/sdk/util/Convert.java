package com.ua.sdk.util;

/* JADX INFO: loaded from: classes65.dex */
public final class Convert {
    public static final long HOURS_IN_DAY = 24;
    public static final double INCH_IN_METER = 39.3701d;
    public static final double LBS_IN_KG = 2.20462d;
    public static final double METER_IN_KILOMETER = 1000.0d;
    public static final double METER_IN_MILE = 1609.344d;
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

    public static final Double inchToMeter(Double inch) {
        if (inch != null) {
            return Double.valueOf(inch.doubleValue() / 39.3701d);
        }
        return null;
    }

    public static final Double kilometerPerHourToMeterPerSec(Double kilometerPerHour) {
        if (kilometerPerHour != null) {
            return Double.valueOf((kilometerPerHour.doubleValue() * 1000.0d) / 3600.0d);
        }
        return null;
    }

    public static final Double kilometerToMeter(Double kilometer) {
        if (kilometer != null) {
            return Double.valueOf(kilometer.doubleValue() * 1000.0d);
        }
        return null;
    }

    public static final Double kilometerToMile(Double kilometer) {
        if (kilometer == null) {
            return null;
        }
        Double meter = kilometerToMeter(kilometer);
        return meterToMile(meter);
    }

    public static final Double kgToLbs(Double kg) {
        if (kg != null) {
            return Double.valueOf(kg.doubleValue() * 2.20462d);
        }
        return kg;
    }

    public static final Double lbsToKg(Double lbs) {
        if (lbs != null) {
            return Double.valueOf(lbs.doubleValue() / 2.20462d);
        }
        return null;
    }

    public static final Double meterToInch(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() * 39.3701d);
        }
        return null;
    }

    public static final Double meterToKilometer(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() / 1000.0d);
        }
        return null;
    }

    public static final Double meterToMile(Double meter) {
        if (meter != null) {
            return Double.valueOf(meter.doubleValue() / 1609.344d);
        }
        return null;
    }

    public static final Double meterPerSecToMeterPerHour(Double meterPerSec) {
        if (meterPerSec != null) {
            return Double.valueOf(meterPerSec.doubleValue() * 3600.0d);
        }
        return null;
    }

    public static final Double meterPerSecToMilePerHour(Double meterPerSec) {
        if (meterPerSec != null) {
            return Double.valueOf((meterPerSec.doubleValue() * 3600.0d) / 1609.344d);
        }
        return null;
    }

    public static final Double meterPerSecToKilometerPerHour(Double meterPerSec) {
        if (meterPerSec != null) {
            return Double.valueOf((meterPerSec.doubleValue() * 3600.0d) / 1000.0d);
        }
        return null;
    }

    public static final Double meterPerHourToMeterPerSecond(Double meterPerHour) {
        if (meterPerHour != null) {
            return Double.valueOf(meterPerHour.doubleValue() / 3600.0d);
        }
        return null;
    }

    public static final Double meterPerHourToMilePerHour(Double meterPerHour) {
        if (meterPerHour != null) {
            return Double.valueOf(meterPerHour.doubleValue() / 1609.344d);
        }
        return null;
    }

    public static final Double milePerHourToMeterPerSecond(Double milePerHour) {
        if (milePerHour != null) {
            return Double.valueOf((milePerHour.doubleValue() * 1609.344d) / 3600.0d);
        }
        return null;
    }

    public static final Double milePerHourToMeterPerHour(Double milePerHour) {
        if (milePerHour != null) {
            return Double.valueOf(milePerHour.doubleValue() * 1609.344d);
        }
        return null;
    }

    public static final Double minPerMeterToSecPerMeter(Double minPerMeter) {
        if (minPerMeter != null) {
            return Double.valueOf(minPerMeter.doubleValue() * 60.0d);
        }
        return null;
    }

    public static final Double minPerMeterToMinPerMile(Double minPerMeter) {
        if (minPerMeter != null) {
            return Double.valueOf(minPerMeter.doubleValue() * 1609.344d);
        }
        return null;
    }

    public static final Double mileToKilometer(Double mile) {
        if (mile == null) {
            return null;
        }
        Double meter = mileToMeter(mile);
        return meterToKilometer(meter);
    }

    public static final Double mileToMeter(Double mile) {
        if (mile != null) {
            return Double.valueOf(mile.doubleValue() * 1609.344d);
        }
        return null;
    }

    public static final Double minPerMileToSecPerMeter(Double minPerMile) {
        if (minPerMile != null) {
            return Double.valueOf((minPerMile.doubleValue() * 60.0d) / 1609.344d);
        }
        return null;
    }

    public static final Double minPerMileToMinPerMeter(Double minPerMile) {
        if (minPerMile != null) {
            return Double.valueOf(minPerMile.doubleValue() / 1609.344d);
        }
        return null;
    }

    public static final Double minPerKilometerToSecPerMeter(Double minPerKilometer) {
        if (minPerKilometer != null) {
            return Double.valueOf((minPerKilometer.doubleValue() * 60.0d) / 1000.0d);
        }
        return null;
    }

    public static final Double secPerMeterToMinPerMeter(Double secPerMeter) {
        if (secPerMeter != null) {
            return Double.valueOf(secPerMeter.doubleValue() / 60.0d);
        }
        return null;
    }

    public static final Double secPerMeterToMinPerMile(Double secPerMeter) {
        if (secPerMeter != null) {
            return Double.valueOf((secPerMeter.doubleValue() * 1609.344d) / 60.0d);
        }
        return null;
    }

    public static final Double secPerMeterToMinPerKilometer(Double secPerMeter) {
        if (secPerMeter != null) {
            return Double.valueOf((secPerMeter.doubleValue() * 1000.0d) / 60.0d);
        }
        return null;
    }
}
