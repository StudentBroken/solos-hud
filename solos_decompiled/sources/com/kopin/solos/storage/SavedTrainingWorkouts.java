package com.kopin.solos.storage;

import android.database.Cursor;
import java.util.HashMap;

/* JADX INFO: loaded from: classes54.dex */
public class SavedTrainingWorkouts {
    private static HashMap<Long, SavedTraining> sCache = new HashMap<>();

    public static long add(SavedTraining training) {
        long id = training.addToDb();
        sCache.put(Long.valueOf(id), training);
        return id;
    }

    public static void remove(long id) {
        sCache.remove(Long.valueOf(id));
        Training.removeFromDb(id);
    }

    public static SavedTraining get(long id) {
        SavedTraining training = sCache.get(Long.valueOf(id));
        if (training == null) {
            Cursor cursor = SQLHelper.getTrainingCursor(id);
            if (cursor.moveToNext()) {
                return new SavedTraining(new Training(cursor));
            }
            return training;
        }
        return training;
    }

    public static void clearCache() {
        sCache.clear();
    }
}
