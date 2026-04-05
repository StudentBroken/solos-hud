package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.FieldSet;
import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes49.dex */
class SmallSortedMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    private List<SmallSortedMap<K, V>.Entry> entryList;
    private boolean isImmutable;
    private volatile SmallSortedMap<K, V>.EntrySet lazyEntrySet;
    private final int maxArraySize;
    private Map<K, V> overflowEntries;

    static <FieldDescriptorType extends FieldSet.FieldDescriptorLite<FieldDescriptorType>> SmallSortedMap<FieldDescriptorType, Object> newFieldMap(int i) {
        return (SmallSortedMap<FieldDescriptorType, Object>) new SmallSortedMap<FieldDescriptorType, Object>(i) { // from class: com.google.tagmanager.protobuf.SmallSortedMap.1
            @Override // com.google.tagmanager.protobuf.SmallSortedMap, java.util.AbstractMap, java.util.Map
            public /* bridge */ /* synthetic */ Object put(Object x0, Object x1) {
                return super.put((FieldSet.FieldDescriptorLite) x0, x1);
            }

            @Override // com.google.tagmanager.protobuf.SmallSortedMap
            public void makeImmutable() {
                if (!isImmutable()) {
                    for (int i2 = 0; i2 < getNumArrayEntries(); i2++) {
                        Map.Entry<FieldDescriptorType, Object> entry = getArrayEntryAt(i2);
                        if (((FieldSet.FieldDescriptorLite) entry.getKey()).isRepeated()) {
                            List value = (List) entry.getValue();
                            entry.setValue(Collections.unmodifiableList(value));
                        }
                    }
                    for (Map.Entry<FieldDescriptorType, Object> entry2 : getOverflowEntries()) {
                        if (((FieldSet.FieldDescriptorLite) entry2.getKey()).isRepeated()) {
                            List value2 = (List) entry2.getValue();
                            entry2.setValue(Collections.unmodifiableList(value2));
                        }
                    }
                }
                super.makeImmutable();
            }
        };
    }

    static <K extends Comparable<K>, V> SmallSortedMap<K, V> newInstanceForTest(int arraySize) {
        return new SmallSortedMap<>(arraySize);
    }

    private SmallSortedMap(int arraySize) {
        this.maxArraySize = arraySize;
        this.entryList = Collections.emptyList();
        this.overflowEntries = Collections.emptyMap();
    }

    public void makeImmutable() {
        if (!this.isImmutable) {
            this.overflowEntries = this.overflowEntries.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.overflowEntries);
            this.isImmutable = true;
        }
    }

    public boolean isImmutable() {
        return this.isImmutable;
    }

    public int getNumArrayEntries() {
        return this.entryList.size();
    }

    public Map.Entry<K, V> getArrayEntryAt(int index) {
        return this.entryList.get(index);
    }

    public int getNumOverflowEntries() {
        return this.overflowEntries.size();
    }

    public Iterable<Map.Entry<K, V>> getOverflowEntries() {
        return this.overflowEntries.isEmpty() ? EmptySet.iterable() : this.overflowEntries.entrySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.entryList.size() + this.overflowEntries.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object o) {
        Comparable comparable = (Comparable) o;
        return binarySearchInArray(comparable) >= 0 || this.overflowEntries.containsKey(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object o) {
        Comparable comparable = (Comparable) o;
        int index = binarySearchInArray(comparable);
        return index >= 0 ? this.entryList.get(index).getValue() : this.overflowEntries.get(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        checkMutable();
        int iBinarySearchInArray = binarySearchInArray(k);
        if (iBinarySearchInArray >= 0) {
            return this.entryList.get(iBinarySearchInArray).setValue(v);
        }
        ensureEntryArrayMutable();
        int i = -(iBinarySearchInArray + 1);
        if (i >= this.maxArraySize) {
            return getOverflowEntriesMutable().put(k, v);
        }
        if (this.entryList.size() == this.maxArraySize) {
            SmallSortedMap<K, V>.Entry entryRemove = this.entryList.remove(this.maxArraySize - 1);
            getOverflowEntriesMutable().put(entryRemove.getKey(), entryRemove.getValue());
        }
        this.entryList.add(i, new Entry(k, v));
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        checkMutable();
        if (!this.entryList.isEmpty()) {
            this.entryList.clear();
        }
        if (!this.overflowEntries.isEmpty()) {
            this.overflowEntries.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object o) {
        checkMutable();
        Comparable comparable = (Comparable) o;
        int index = binarySearchInArray(comparable);
        if (index >= 0) {
            return removeArrayEntryAt(index);
        }
        if (this.overflowEntries.isEmpty()) {
            return null;
        }
        return this.overflowEntries.remove(comparable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V removeArrayEntryAt(int index) {
        checkMutable();
        V removed = this.entryList.remove(index).getValue();
        if (!this.overflowEntries.isEmpty()) {
            Iterator<Map.Entry<K, V>> iterator = getOverflowEntriesMutable().entrySet().iterator();
            this.entryList.add(new Entry(this, iterator.next()));
            iterator.remove();
        }
        return removed;
    }

    private int binarySearchInArray(K key) {
        int left = 0;
        int right = this.entryList.size() - 1;
        if (right >= 0) {
            int cmp = key.compareTo(this.entryList.get(right).getKey());
            if (cmp > 0) {
                return -(right + 2);
            }
            if (cmp == 0) {
                return right;
            }
        }
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp2 = key.compareTo(this.entryList.get(mid).getKey());
            if (cmp2 < 0) {
                right = mid - 1;
            } else if (cmp2 > 0) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -(left + 1);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.lazyEntrySet == null) {
            this.lazyEntrySet = new EntrySet();
        }
        return this.lazyEntrySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMutable() {
        if (this.isImmutable) {
            throw new UnsupportedOperationException();
        }
    }

    private SortedMap<K, V> getOverflowEntriesMutable() {
        checkMutable();
        if (this.overflowEntries.isEmpty() && !(this.overflowEntries instanceof TreeMap)) {
            this.overflowEntries = new TreeMap();
        }
        return (SortedMap) this.overflowEntries;
    }

    private void ensureEntryArrayMutable() {
        checkMutable();
        if (this.entryList.isEmpty() && !(this.entryList instanceof ArrayList)) {
            this.entryList = new ArrayList(this.maxArraySize);
        }
    }

    private class Entry implements Map.Entry<K, V>, Comparable<SmallSortedMap<K, V>.Entry> {
        private final K key;
        private V value;

        Entry(SmallSortedMap smallSortedMap, Map.Entry<K, V> copy) {
            this(copy.getKey(), copy.getValue());
        }

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.lang.Comparable
        public int compareTo(SmallSortedMap<K, V>.Entry other) {
            return getKey().compareTo(other.getKey());
        }

        @Override // java.util.Map.Entry
        public V setValue(V newValue) {
            SmallSortedMap.this.checkMutable();
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> other = (Map.Entry) o;
            return equals(this.key, other.getKey()) && equals(this.value, other.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value != null ? this.value.hashCode() : 0);
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        private boolean equals(Object o1, Object o2) {
            return o1 == null ? o2 == null : o1.equals(o2);
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return SmallSortedMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            Map.Entry<K, V> entry = (Map.Entry) o;
            Object obj = SmallSortedMap.this.get(entry.getKey());
            V value = entry.getValue();
            return obj == value || (obj != null && obj.equals(value));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(Map.Entry<K, V> entry) {
            if (contains(entry)) {
                return false;
            }
            SmallSortedMap.this.put((Comparable) entry.getKey(), (Object) entry.getValue());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            Map.Entry<K, V> entry = (Map.Entry) o;
            if (!contains(entry)) {
                return false;
            }
            SmallSortedMap.this.remove(entry.getKey());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            SmallSortedMap.this.clear();
        }
    }

    private class EntryIterator implements Iterator<Map.Entry<K, V>> {
        private Iterator<Map.Entry<K, V>> lazyOverflowIterator;
        private boolean nextCalledBeforeRemove;
        private int pos;

        private EntryIterator() {
            this.pos = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.pos + 1 < SmallSortedMap.this.entryList.size() || getOverflowIterator().hasNext();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            this.nextCalledBeforeRemove = true;
            int i = this.pos + 1;
            this.pos = i;
            return i < SmallSortedMap.this.entryList.size() ? (Map.Entry) SmallSortedMap.this.entryList.get(this.pos) : getOverflowIterator().next();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.nextCalledBeforeRemove) {
                throw new IllegalStateException("remove() was called before next()");
            }
            this.nextCalledBeforeRemove = false;
            SmallSortedMap.this.checkMutable();
            if (this.pos < SmallSortedMap.this.entryList.size()) {
                SmallSortedMap smallSortedMap = SmallSortedMap.this;
                int i = this.pos;
                this.pos = i - 1;
                smallSortedMap.removeArrayEntryAt(i);
                return;
            }
            getOverflowIterator().remove();
        }

        private Iterator<Map.Entry<K, V>> getOverflowIterator() {
            if (this.lazyOverflowIterator == null) {
                this.lazyOverflowIterator = SmallSortedMap.this.overflowEntries.entrySet().iterator();
            }
            return this.lazyOverflowIterator;
        }
    }

    private static class EmptySet {
        private static final Iterator<Object> ITERATOR = new Iterator<Object>() { // from class: com.google.tagmanager.protobuf.SmallSortedMap.EmptySet.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        private static final Iterable<Object> ITERABLE = new Iterable<Object>() { // from class: com.google.tagmanager.protobuf.SmallSortedMap.EmptySet.2
            @Override // java.lang.Iterable
            public Iterator<Object> iterator() {
                return EmptySet.ITERATOR;
            }
        };

        private EmptySet() {
        }

        static <T> Iterable<T> iterable() {
            return (Iterable<T>) ITERABLE;
        }
    }
}
