package com.google.tagmanager.protobuf;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/* JADX INFO: loaded from: classes49.dex */
public class LazyStringArrayList extends AbstractList<String> implements LazyStringList, RandomAccess {
    public static final LazyStringList EMPTY = new UnmodifiableLazyStringList(new LazyStringArrayList());
    private final List<Object> list;

    public LazyStringArrayList() {
        this.list = new ArrayList();
    }

    public LazyStringArrayList(LazyStringList from) {
        this.list = new ArrayList(from.size());
        addAll(from);
    }

    public LazyStringArrayList(List<String> from) {
        this.list = new ArrayList(from);
    }

    @Override // java.util.AbstractList, java.util.List
    public String get(int index) {
        Object o = this.list.get(index);
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof ByteString) {
            ByteString bs = (ByteString) o;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.list.set(index, s);
                return s;
            }
            return s;
        }
        byte[] ba = (byte[]) o;
        String s2 = Internal.toStringUtf8(ba);
        if (Internal.isValidUtf8(ba)) {
            this.list.set(index, s2);
            return s2;
        }
        return s2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public String set(int index, String s) {
        Object o = this.list.set(index, s);
        return asString(o);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, String element) {
        this.list.add(index, element);
        this.modCount++;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends String> c) {
        return addAll(size(), c);
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<? extends String> c) {
        boolean ret = this.list.addAll(index, c instanceof LazyStringList ? ((LazyStringList) c).getUnderlyingElements() : c);
        this.modCount++;
        return ret;
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public boolean addAllByteString(Collection<? extends ByteString> values) {
        boolean ret = this.list.addAll(values);
        this.modCount++;
        return ret;
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public boolean addAllByteArray(Collection<byte[]> c) {
        boolean ret = this.list.addAll(c);
        this.modCount++;
        return ret;
    }

    @Override // java.util.AbstractList, java.util.List
    public String remove(int index) {
        Object o = this.list.remove(index);
        this.modCount++;
        return asString(o);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.list.clear();
        this.modCount++;
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public void add(ByteString element) {
        this.list.add(element);
        this.modCount++;
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public void add(byte[] element) {
        this.list.add(element);
        this.modCount++;
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public ByteString getByteString(int index) {
        Object o = this.list.get(index);
        ByteString b = asByteString(o);
        if (b != o) {
            this.list.set(index, b);
        }
        return b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.tagmanager.protobuf.LazyStringList
    public byte[] getByteArray(int i) {
        Object obj = this.list.get(i);
        byte[] bArrAsByteArray = asByteArray(obj);
        if (bArrAsByteArray != obj) {
            this.list.set(i, bArrAsByteArray);
        }
        return bArrAsByteArray;
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public void set(int index, ByteString s) {
        this.list.set(index, s);
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public void set(int index, byte[] s) {
        this.list.set(index, s);
    }

    private static String asString(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof ByteString) {
            return ((ByteString) o).toStringUtf8();
        }
        return Internal.toStringUtf8((byte[]) o);
    }

    private static ByteString asByteString(Object o) {
        if (o instanceof ByteString) {
            return (ByteString) o;
        }
        if (o instanceof String) {
            return ByteString.copyFromUtf8((String) o);
        }
        return ByteString.copyFrom((byte[]) o);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] asByteArray(Object o) {
        if (o instanceof byte[]) {
            return (byte[]) o;
        }
        if (o instanceof String) {
            return Internal.toByteArray((String) o);
        }
        return ((ByteString) o).toByteArray();
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public List<?> getUnderlyingElements() {
        return Collections.unmodifiableList(this.list);
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public void mergeFrom(LazyStringList other) {
        for (Object o : other.getUnderlyingElements()) {
            if (o instanceof byte[]) {
                byte[] b = (byte[]) o;
                this.list.add(Arrays.copyOf(b, b.length));
            } else {
                this.list.add(o);
            }
        }
    }

    private static class ByteArrayListView extends AbstractList<byte[]> {
        private final List<Object> list;

        ByteArrayListView(List<Object> list) {
            this.list = list;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractList, java.util.List
        public byte[] get(int i) {
            Object obj = this.list.get(i);
            byte[] bArrAsByteArray = LazyStringArrayList.asByteArray(obj);
            if (bArrAsByteArray != obj) {
                this.list.set(i, bArrAsByteArray);
            }
            return bArrAsByteArray;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.list.size();
        }

        @Override // java.util.AbstractList, java.util.List
        public byte[] set(int index, byte[] s) {
            Object o = this.list.set(index, s);
            return LazyStringArrayList.asByteArray(o);
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int index, byte[] s) {
            this.list.add(index, s);
            this.modCount++;
        }

        @Override // java.util.AbstractList, java.util.List
        public byte[] remove(int index) {
            Object o = this.list.remove(index);
            this.modCount++;
            return LazyStringArrayList.asByteArray(o);
        }
    }

    @Override // com.google.tagmanager.protobuf.LazyStringList
    public List<byte[]> asByteArrayList() {
        return new ByteArrayListView(this.list);
    }
}
