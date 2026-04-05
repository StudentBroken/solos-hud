package com.google.tagmanager.protobuf;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
class LazyField {
    private ByteString bytes;
    private final MessageLite defaultInstance;
    private final ExtensionRegistryLite extensionRegistry;
    private volatile boolean isDirty = false;
    private volatile MessageLite value;

    public LazyField(MessageLite defaultInstance, ExtensionRegistryLite extensionRegistry, ByteString bytes) {
        this.defaultInstance = defaultInstance;
        this.extensionRegistry = extensionRegistry;
        this.bytes = bytes;
    }

    public MessageLite getValue() {
        ensureInitialized();
        return this.value;
    }

    public MessageLite setValue(MessageLite value) {
        MessageLite originalValue = this.value;
        this.value = value;
        this.bytes = null;
        this.isDirty = true;
        return originalValue;
    }

    public int getSerializedSize() {
        return this.isDirty ? this.value.getSerializedSize() : this.bytes.size();
    }

    public ByteString toByteString() {
        ByteString byteString;
        if (!this.isDirty) {
            return this.bytes;
        }
        synchronized (this) {
            if (!this.isDirty) {
                byteString = this.bytes;
            } else {
                this.bytes = this.value.toByteString();
                this.isDirty = false;
                byteString = this.bytes;
            }
        }
        return byteString;
    }

    public int hashCode() {
        ensureInitialized();
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        ensureInitialized();
        return this.value.equals(obj);
    }

    public String toString() {
        ensureInitialized();
        return this.value.toString();
    }

    private void ensureInitialized() {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    try {
                        if (this.bytes != null) {
                            this.value = this.defaultInstance.getParserForType().parseFrom(this.bytes, this.extensionRegistry);
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    static class LazyEntry<K> implements Map.Entry<K, Object> {
        private Map.Entry<K, LazyField> entry;

        private LazyEntry(Map.Entry<K, LazyField> entry) {
            this.entry = entry;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.entry.getKey();
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            LazyField field = this.entry.getValue();
            if (field == null) {
                return null;
            }
            return field.getValue();
        }

        public LazyField getField() {
            return this.entry.getValue();
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object value) {
            if (!(value instanceof MessageLite)) {
                throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
            }
            return this.entry.getValue().setValue((MessageLite) value);
        }
    }

    static class LazyIterator<K> implements Iterator<Map.Entry<K, Object>> {
        private Iterator<Map.Entry<K, Object>> iterator;

        public LazyIterator(Iterator<Map.Entry<K, Object>> iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, Object> next() {
            Map.Entry<K, ?> entry = this.iterator.next();
            if (entry.getValue() instanceof LazyField) {
                return new LazyEntry<>(entry);
            }
            return entry;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }
    }
}
