package com.kopin.solos.storage.settings;

import android.app.DatePickerDialog;
import android.content.Context;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.R;
import com.kopin.solos.storage.Rider;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.util.DataHolder;
import com.kopin.solos.storage.util.Utility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* JADX INFO: loaded from: classes54.dex */
public class UserProfile {
    private static final double DEFAULT_AVG_SPEED_RIDE = 7.0d;
    private static final double DEFAULT_AVG_SPEED_RUN = 3.0d;
    private static Context mContext;

    public enum Gender {
        UNKNOWN,
        MALE,
        FEMALE;

        public static Gender get(int i) {
            return (i < 0 || i >= values().length) ? UNKNOWN : values()[i];
        }

        public static Gender getGender(String gender) {
            if (gender == null || gender.trim().isEmpty()) {
                return UNKNOWN;
            }
            if (MALE.name().equalsIgnoreCase(gender) || MALE.name().contains(gender)) {
                return MALE;
            }
            if (FEMALE.name().equalsIgnoreCase(gender) || FEMALE.name().contains(gender)) {
                return FEMALE;
            }
            return UNKNOWN;
        }

        public String getInitial() {
            switch (this) {
                case UNKNOWN:
                    return "";
                default:
                    return "" + name().charAt(0);
            }
        }
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static void setDOB(long dob) {
        Date date = new Date(dob);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(date);
        if (s != null && s.contains("-")) {
            String[] items = s.split("-");
            if (items.length > 2) {
                Prefs.setDOB(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]), true);
            }
        }
    }

    public static void setDOB(int year, int month, int day, boolean monthBase1) {
        Prefs.setDOB(year, month, day, monthBase1);
    }

    public static int getAge() {
        return ageFromDOB(getDOB());
    }

    public static String getDOB() {
        return Prefs.getDOB(false);
    }

    public static long getDOBMillis() {
        String yyyymd = Prefs.getDOB(true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy,M,d");
        try {
            return df.parse(yyyymd).getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    public static int ageFromDOB(String date) {
        int year = 1995;
        int month = 0;
        int day = 1;
        if (date != null && date.contains(",")) {
            String[] parts = date.split(",");
            if (parts.length >= 3) {
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[2]);
            }
        }
        Calendar today = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(1) - dob.get(1);
        if (today.get(6) < dob.get(6)) {
            return age - 1;
        }
        return age;
    }

    public static int getDatePart(String date, int index, int defaultValue) {
        if (date != null && date.contains(",")) {
            String[] parts = date.split(",");
            if (parts.length >= 3) {
                return Integer.parseInt(parts[index]);
            }
            return defaultValue;
        }
        return defaultValue;
    }

    public static void prepareDateOfBirthDialog(DatePickerDialog dialog) {
        Calendar cal = Calendar.getInstance();
        cal.add(1, -5);
        dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        Calendar cal2 = Calendar.getInstance();
        int oldestYear = cal2.get(1) - dialog.getContext().getResources().getInteger(R.integer.user_max_age);
        cal2.set(oldestYear, 0, 1);
        dialog.getDatePicker().setMinDate(cal2.getTimeInMillis() - 100);
    }

    public static Gender getGender() {
        return Prefs.getGender();
    }

    public static void setGender(String gender) {
        setGender(Gender.getGender(gender));
    }

    private static void setGender(Gender gender) {
        Prefs.setGender(gender);
    }

    public static void setName(String name) {
        Prefs.setName(name);
    }

    public static String getName() {
        String name = Prefs.getName(0);
        return (name == null || name.isEmpty()) ? mContext.getString(R.string.default_rider_name) : name;
    }

    public static String getName(int nameResDefault) {
        return Prefs.getName(nameResDefault);
    }

    public static double getWeightKG() {
        return Prefs.getWeightKG();
    }

    public static double getWeightPounds() {
        return Utility.kilogramsToPounds(getWeightKG());
    }

    public static void setWeight(String weight, boolean kg) {
        Prefs.setWeight(weight, kg);
    }

    public static void setWeightKG(double weightKG) {
        Prefs.setWeightKG(weightKG);
    }

    public static boolean isValidWeight(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        Double metricWeight = Double.valueOf(Double.parseDouble(s));
        Double weight = Utility.convertToKg(metricWeight);
        return weight.doubleValue() >= ((double) mContext.getResources().getInteger(R.integer.weight_min)) && weight.doubleValue() <= ((double) mContext.getResources().getInteger(R.integer.weight_max));
    }

    public static boolean isValidWeight(double weightKG) {
        return weightKG >= ((double) mContext.getResources().getInteger(R.integer.weight_min)) && weightKG <= ((double) mContext.getResources().getInteger(R.integer.weight_max));
    }

    public static Rider createRider() {
        return new Rider(getAge(), getWeightKG(), getName(), getGender());
    }

    public static double getAverageSpeed(SportType sportType) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -30);
        switch (sportType) {
            case RIDE:
                double speed = SQLHelper.getRideAverageSpeed(calendar.getTimeInMillis());
                return (!DataHolder.isValid(speed) || speed <= 0.0d) ? DEFAULT_AVG_SPEED_RIDE : speed;
            case RUN:
                double speed2 = SQLHelper.getRunAvgSpeed(calendar.getTimeInMillis());
                return (!DataHolder.isValid(speed2) || speed2 <= 0.0d) ? DEFAULT_AVG_SPEED_RUN : speed2;
            default:
                return 0.0d;
        }
    }
}
