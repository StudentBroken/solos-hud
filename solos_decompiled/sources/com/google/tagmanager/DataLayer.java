package com.google.tagmanager;

import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes49.dex */
public class DataLayer {
    static final int MAX_QUEUE_DEPTH = 500;
    public static final Object OBJECT_NOT_PRESENT = new Object();
    private final ConcurrentHashMap<Listener, Integer> mListeners = new ConcurrentHashMap<>();
    private final Map<Object, Object> mModel = new HashMap();
    private final ReentrantLock mPushLock = new ReentrantLock();
    private final LinkedList<Map<Object, Object>> mUpdateQueue = new LinkedList<>();

    interface Listener {
        void changed(Map<Object, Object> map);
    }

    DataLayer() {
    }

    public void push(Object key, Object value) {
        Map<Object, Object> expanded = expandKeyValue(key, value);
        push(expanded);
    }

    public void push(Map<Object, Object> update) {
        this.mPushLock.lock();
        try {
            this.mUpdateQueue.offer(update);
            if (this.mPushLock.getHoldCount() == 1) {
                processQueuedUpdates();
            }
        } finally {
            this.mPushLock.unlock();
        }
    }

    private void processQueuedUpdates() {
        int numUpdatesProcessed = 0;
        do {
            Map<Object, Object> update = this.mUpdateQueue.poll();
            if (update != null) {
                processUpdate(update);
                numUpdatesProcessed++;
            } else {
                return;
            }
        } while (numUpdatesProcessed <= MAX_QUEUE_DEPTH);
        this.mUpdateQueue.clear();
        throw new RuntimeException("Seems like an infinite loop of pushing to the data layer");
    }

    private void processUpdate(Map<Object, Object> update) {
        synchronized (this.mModel) {
            for (Object key : update.keySet()) {
                mergeMap(expandKeyValue(key, update.get(key)), this.mModel);
            }
        }
        notifyListeners(update);
    }

    public Object get(String key) {
        synchronized (this.mModel) {
            Object obj = this.mModel;
            String[] split = key.split("\\.");
            for (String s : split) {
                if (!(obj instanceof Map)) {
                    return null;
                }
                Map<Object, Object> map = (Map) obj;
                Object value = map.get(s);
                if (value == null) {
                    return null;
                }
                obj = value;
            }
            return obj;
        }
    }

    public static Map<Object, Object> mapOf(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("expected even number of key-value pairs");
        }
        Map<Object, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length; i += 2) {
            map.put(objects[i], objects[i + 1]);
        }
        return map;
    }

    public static List<Object> listOf(Object... objects) {
        List<Object> list = new ArrayList<>();
        for (Object obj : objects) {
            list.add(obj);
        }
        return list;
    }

    void registerListener(Listener listener) {
        this.mListeners.put(listener, 0);
    }

    void unregisterListener(Listener listener) {
        this.mListeners.remove(listener);
    }

    private void notifyListeners(Map<Object, Object> update) {
        for (Listener listener : this.mListeners.keySet()) {
            listener.changed(update);
        }
    }

    @VisibleForTesting
    Map<Object, Object> expandKeyValue(Object key, Object value) {
        Map<Object, Object> result = new HashMap<>();
        Map<Object, Object> target = result;
        Object[] split = key.toString().split("\\.");
        for (int i = 0; i < split.length - 1; i++) {
            Map<Object, Object> map = new HashMap<>();
            target.put(split[i], map);
            target = map;
        }
        target.put(split[split.length - 1], value);
        return result;
    }

    @VisibleForTesting
    void mergeMap(Map<Object, Object> from, Map<Object, Object> to) {
        for (Object key : from.keySet()) {
            Object fromValue = from.get(key);
            if (fromValue instanceof List) {
                if (!(to.get(key) instanceof List)) {
                    to.put(key, new ArrayList());
                }
                List<Object> mergeFrom = (List) fromValue;
                List<Object> mergeTo = (List) to.get(key);
                mergeList(mergeFrom, mergeTo);
            } else if (fromValue instanceof Map) {
                if (!(to.get(key) instanceof Map)) {
                    to.put(key, new HashMap());
                }
                Map<Object, Object> mergeFrom2 = (Map) fromValue;
                Map<Object, Object> mergeTo2 = (Map) to.get(key);
                mergeMap(mergeFrom2, mergeTo2);
            } else {
                to.put(key, fromValue);
            }
        }
    }

    @VisibleForTesting
    void mergeList(List<Object> from, List<Object> to) {
        while (to.size() < from.size()) {
            to.add(null);
        }
        for (int index = 0; index < from.size(); index++) {
            Object fromValue = from.get(index);
            if (fromValue instanceof List) {
                if (!(to.get(index) instanceof List)) {
                    to.set(index, new ArrayList());
                }
                List<Object> mergeFrom = (List) fromValue;
                List<Object> mergeTo = (List) to.get(index);
                mergeList(mergeFrom, mergeTo);
            } else if (fromValue instanceof Map) {
                if (!(to.get(index) instanceof Map)) {
                    to.set(index, new HashMap());
                }
                Map<Object, Object> mergeFrom2 = (Map) fromValue;
                Map<Object, Object> mergeTo2 = (Map) to.get(index);
                mergeMap(mergeFrom2, mergeTo2);
            } else if (fromValue != OBJECT_NOT_PRESENT) {
                to.set(index, fromValue);
            }
        }
    }
}
