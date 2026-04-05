package com.ua.sdk.internal;

import com.ua.sdk.LocalDate;
import com.ua.sdk.MetabolicEnergyCalculator;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.activitytype.WorkoutMetsSpeed;
import com.ua.sdk.user.Gender;
import com.ua.sdk.user.User;
import com.ua.sdk.util.Convert;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes65.dex */
public class MetabolicEnergyCalculatorImpl implements MetabolicEnergyCalculator {
    @Override // com.ua.sdk.MetabolicEnergyCalculator
    public double calculateJoules(User user, ActivityType activityType, double elapsedTimeInSecs, double distanceInMeters) {
        Precondition.isNotNull(user, "User");
        Double weight = user.getWeight();
        Double height = user.getHeight();
        Precondition.isNotNull(user.getBirthdate(), "User's birthdate");
        int age = extractAge(user.getBirthdate());
        if (age < 13) {
            throw new IllegalArgumentException("User's age must be > 13");
        }
        Gender gender = user.getGender();
        Precondition.isNotNull(weight, "User's weight");
        Precondition.isNotNull(height, "User's height");
        Precondition.isNotNull(gender, "User's gender");
        Precondition.isNotNull(activityType, "Activity Type");
        return getMets(elapsedTimeInSecs, distanceInMeters, activityType, height, weight, gender, age) * weight.doubleValue() * (elapsedTimeInSecs / 3600.0d) * 4184.0d;
    }

    private double getMets(double elapsedTimeInSecs, double distanceInMeters, ActivityType activityType, Double height, Double weight, Gender gender, int age) {
        double mets;
        if (distanceInMeters > 1.0d && elapsedTimeInSecs > 1.0d) {
            double metersPerSec = distanceInMeters / elapsedTimeInSecs;
            double mph = Convert.meterPerSecToMilePerHour(Double.valueOf(metersPerSec)).doubleValue();
            Double speedMets = getSpeedAwareMets(Double.valueOf(mph), activityType.getMetsSpeed());
            if (speedMets != null) {
                mets = speedMets.doubleValue();
            } else {
                mets = activityType.getMetsValue().doubleValue();
            }
        } else {
            mets = activityType.getMetsValue().doubleValue();
        }
        double rmr = getHarrisBenedictRmr(height, weight, gender, age);
        double calPerMin = rmr / 1440.0d;
        double literPerMin = calPerMin / 5.0d;
        double mLPerKgMin = (1000.0d * literPerMin) / weight.doubleValue();
        double correctedMets = mets * (3.5d / mLPerKgMin);
        return correctedMets;
    }

    private Double getSpeedAwareMets(Double mph, String metsSpeed) {
        Double resp;
        List<WorkoutMetsSpeed> list = WorkoutMetsSpeed.parseSpeedList(metsSpeed);
        if (list.size() >= 2) {
            WorkoutMetsSpeed lower = null;
            WorkoutMetsSpeed higher = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                WorkoutMetsSpeed checkMetsSpeed = list.get(i);
                lower = higher;
                higher = checkMetsSpeed;
                if (higher.getSpeedMilesPerHour().doubleValue() > mph.doubleValue()) {
                    break;
                }
            }
            if (mph.doubleValue() < lower.getSpeedMilesPerHour().doubleValue()) {
                resp = lower.getMets();
            } else if (mph.doubleValue() > higher.getSpeedMilesPerHour().doubleValue()) {
                resp = higher.getMets();
            } else {
                resp = linearInterpolateMets(lower, higher, mph);
            }
            if (resp.doubleValue() < 0.0d) {
                return null;
            }
            return resp;
        }
        return null;
    }

    public Double linearInterpolateMets(WorkoutMetsSpeed lower, WorkoutMetsSpeed higher, Double mph) {
        Double m = Double.valueOf((higher.getMets().doubleValue() - lower.getMets().doubleValue()) / (higher.getSpeedMilesPerHour().doubleValue() - lower.getSpeedMilesPerHour().doubleValue()));
        Double b = Double.valueOf(higher.getMets().doubleValue() - (m.doubleValue() * higher.getSpeedMilesPerHour().doubleValue()));
        return Double.valueOf((m.doubleValue() * mph.doubleValue()) + b.doubleValue());
    }

    private double getHarrisBenedictRmr(Double height, Double weight, Gender gender, int age) {
        double cm = height.doubleValue() * 100.0d;
        if (gender == Gender.MALE) {
            double rmr = ((66.473d + (13.7516d * weight.doubleValue())) + (5.0033d * cm)) - (6.755d * ((double) age));
            return rmr;
        }
        double rmr2 = ((655.0955d + (9.5634d * weight.doubleValue())) + (1.8496d * cm)) - (4.6756d * ((double) age));
        return rmr2;
    }

    private static int extractAge(LocalDate userBirthdate) {
        Calendar a = new GregorianCalendar(Locale.US);
        a.set(1, userBirthdate.getYear());
        a.set(2, userBirthdate.getMonth());
        a.set(5, userBirthdate.getDayOfMonth());
        Calendar b = getCalendar(new Date());
        int diff = b.get(1) - a.get(1);
        if (a.get(2) > b.get(2) || (a.get(2) == b.get(2) && a.get(5) > b.get(5))) {
            return diff - 1;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
