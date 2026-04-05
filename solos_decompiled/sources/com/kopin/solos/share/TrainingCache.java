package com.kopin.solos.share;

import android.util.ArraySet;
import android.util.LongSparseArray;
import android.util.SparseArray;
import com.kopin.solos.storage.SavedTraining;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class TrainingCache {
    private static SparseArray<HashMap<String, SavedTraining>> sCache = new SparseArray<>();
    private static SparseArray<LongSparseArray<ArraySet<String>>> schedule = new SparseArray<>();

    public static void add(Platforms platform, SavedTraining training, long trainingDate) {
        HashMap<String, SavedTraining> trainingHashMap = sCache.get(platform.getSharedKey());
        if (trainingHashMap == null) {
            trainingHashMap = new HashMap<>();
        }
        trainingHashMap.put(training.getExternalId(), training);
        sCache.put(platform.getSharedKey(), trainingHashMap);
        LongSparseArray<ArraySet<String>> scheduleMap = schedule.get(platform.getSharedKey());
        if (scheduleMap == null) {
            scheduleMap = new LongSparseArray<>();
        }
        ArraySet<String> trainingSet = scheduleMap.get(trainingDate);
        if (trainingSet == null) {
            trainingSet = new ArraySet<>();
        }
        trainingSet.add(training.getExternalId());
        scheduleMap.put(trainingDate, trainingSet);
        schedule.put(platform.getSharedKey(), scheduleMap);
    }

    public static SavedTraining get(Platforms platform, String externalId) {
        Map<String, SavedTraining> trainingHashMap = sCache.get(platform.getSharedKey());
        if (trainingHashMap != null) {
            return trainingHashMap.get(externalId);
        }
        return null;
    }

    public static LongSparseArray<ArraySet<String>> getTrainingSchedule(Platforms platform) {
        return schedule.get(platform.getSharedKey());
    }

    public static void clearCache() {
        for (int i = 0; i < sCache.size(); i++) {
            int key = sCache.keyAt(i);
            sCache.remove(key);
            schedule.remove(key);
        }
    }

    public static void clearCache(Platforms platform) {
        sCache.remove(platform.getSharedKey());
        schedule.remove(platform.getSharedKey());
    }
}
