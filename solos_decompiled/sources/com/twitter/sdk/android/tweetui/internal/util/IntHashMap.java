package com.twitter.sdk.android.tweetui.internal.util;

/* JADX INFO: loaded from: classes9.dex */
public class IntHashMap {
    private int count;
    private float loadFactor;
    private Entry[] table;
    private int threshold;

    private static class Entry {
        public final int hash;
        public int key;
        public Entry next;
        public Object value;

        protected Entry(int hash, int key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public IntHashMap() {
        this(20, 0.75f);
    }

    public IntHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }
        initialCapacity = initialCapacity == 0 ? 1 : initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Entry[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = this.table;
        int i = tab.length;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                for (Entry e = tab[i]; e != null; e = e.next) {
                    if (e.value.equals(value)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
    }

    public boolean containsValue(Object value) {
        return contains(value);
    }

    public boolean containsKey(int key) {
        Entry[] tab = this.table;
        int index = (Integer.MAX_VALUE & key) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == key) {
                return true;
            }
        }
        return false;
    }

    public Object get(int key) {
        Entry[] tab = this.table;
        int index = (Integer.MAX_VALUE & key) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == key) {
                return e.value;
            }
        }
        return null;
    }

    protected void rehash() {
        int oldCapacity = this.table.length;
        Entry[] oldMap = this.table;
        int newCapacity = (oldCapacity * 2) + 1;
        Entry[] newMap = new Entry[newCapacity];
        this.threshold = (int) (newCapacity * this.loadFactor);
        this.table = newMap;
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                Entry old = oldMap[i];
                while (old != null) {
                    Entry e = old;
                    old = old.next;
                    int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
                    e.next = newMap[index];
                    newMap[index] = e;
                }
            } else {
                return;
            }
        }
    }

    public Object put(int key, Object value) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == key) {
                Object old = e.value;
                e.value = value;
                return old;
            }
        }
        if (this.count >= this.threshold) {
            rehash();
            tab = this.table;
            index = (key & Integer.MAX_VALUE) % tab.length;
        }
        Entry e2 = new Entry(key, key, value, tab[index]);
        tab[index] = e2;
        this.count++;
        return null;
    }

    public Object remove(int key) {
        Entry[] tab = this.table;
        int index = (Integer.MAX_VALUE & key) % tab.length;
        Entry prev = null;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash != key) {
                prev = e;
            } else {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                this.count--;
                Object oldValue = e.value;
                e.value = null;
                return oldValue;
            }
        }
        return null;
    }

    public synchronized void clear() {
        Entry[] tab = this.table;
        int index = tab.length;
        while (true) {
            index--;
            if (index >= 0) {
                tab[index] = null;
            } else {
                this.count = 0;
            }
        }
    }
}
