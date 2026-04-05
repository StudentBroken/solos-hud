package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.LazyField;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
final class FieldSet<FieldDescriptorType extends FieldDescriptorLite<FieldDescriptorType>> {
    private static final FieldSet DEFAULT_INSTANCE = new FieldSet(true);
    private boolean isImmutable;
    private boolean hasLazyField = false;
    private final SmallSortedMap<FieldDescriptorType, Object> fields = SmallSortedMap.newFieldMap(16);

    public interface FieldDescriptorLite<T extends FieldDescriptorLite<T>> extends Comparable<T> {
        Internal.EnumLiteMap<?> getEnumType();

        WireFormat.JavaType getLiteJavaType();

        WireFormat.FieldType getLiteType();

        int getNumber();

        MessageLite.Builder internalMergeFrom(MessageLite.Builder builder, MessageLite messageLite);

        MutableMessageLite internalMergeFrom(MutableMessageLite mutableMessageLite, MutableMessageLite mutableMessageLite2);

        boolean isPacked();

        boolean isRepeated();
    }

    private FieldSet() {
    }

    private FieldSet(boolean dummy) {
        makeImmutable();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> newFieldSet() {
        return new FieldSet<>();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> emptySet() {
        return DEFAULT_INSTANCE;
    }

    public void makeImmutable() {
        if (!this.isImmutable) {
            this.fields.makeImmutable();
            this.isImmutable = true;
        }
    }

    public boolean isImmutable() {
        return this.isImmutable;
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public FieldSet<FieldDescriptorType> m8clone() {
        FieldSet<FieldDescriptorType> clone = newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<K, Object> arrayEntryAt = this.fields.getArrayEntryAt(i);
            clone.setField((FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue());
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            FieldDescriptorType descriptor = entry.getKey();
            clone.setField(descriptor, entry.getValue());
        }
        clone.hasLazyField = this.hasLazyField;
        return clone;
    }

    private Object convertToImmutable(FieldDescriptorType descriptor, Object value) {
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (descriptor.isRepeated()) {
                List<Object> mutableMessages = (List) value;
                List<Object> immutableMessages = new ArrayList<>();
                for (Object mutableMessage : mutableMessages) {
                    immutableMessages.add(((MutableMessageLite) mutableMessage).immutableCopy());
                }
                return immutableMessages;
            }
            if (value instanceof LazyField) {
                return ((MutableMessageLite) ((LazyField) value).getValue()).immutableCopy();
            }
            return ((MutableMessageLite) value).immutableCopy();
        }
        if (descriptor.getLiteJavaType() != WireFormat.JavaType.BYTE_STRING) {
            return value;
        }
        if (descriptor.isRepeated()) {
            List<Object> mutableFields = (List) value;
            List<Object> immutableFields = new ArrayList<>();
            for (Object mutableField : mutableFields) {
                immutableFields.add(ByteString.copyFrom((byte[]) mutableField));
            }
            return immutableFields;
        }
        return ByteString.copyFrom((byte[]) value);
    }

    private Object convertToMutable(FieldDescriptorType descriptor, Object value) {
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (descriptor.isRepeated()) {
                List<Object> mutableMessages = new ArrayList<>();
                List<Object> immutableMessages = (List) value;
                for (Object immutableMessage : immutableMessages) {
                    mutableMessages.add(((MessageLite) immutableMessage).mutableCopy());
                }
                return mutableMessages;
            }
            if (value instanceof LazyField) {
                return ((LazyField) value).getValue().mutableCopy();
            }
            return ((MessageLite) value).mutableCopy();
        }
        if (descriptor.getLiteJavaType() != WireFormat.JavaType.BYTE_STRING) {
            return value;
        }
        if (descriptor.isRepeated()) {
            List<Object> immutableFields = (List) value;
            List<Object> mutableFields = new ArrayList<>();
            for (Object immutableField : immutableFields) {
                mutableFields.add(((ByteString) immutableField).toByteArray());
            }
            return mutableFields;
        }
        return ((ByteString) value).toByteArray();
    }

    public FieldSet<FieldDescriptorType> cloneWithAllFieldsToImmutable() {
        FieldSet<FieldDescriptorType> clone = newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<K, Object> arrayEntryAt = this.fields.getArrayEntryAt(i);
            FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) arrayEntryAt.getKey();
            clone.setField(fieldDescriptorLite, convertToImmutable(fieldDescriptorLite, arrayEntryAt.getValue()));
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            FieldDescriptorType descriptor = entry.getKey();
            clone.setField(descriptor, convertToImmutable(descriptor, entry.getValue()));
        }
        clone.hasLazyField = false;
        return clone;
    }

    public FieldSet<FieldDescriptorType> cloneWithAllFieldsToMutable() {
        FieldSet<FieldDescriptorType> clone = newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<K, Object> arrayEntryAt = this.fields.getArrayEntryAt(i);
            FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) arrayEntryAt.getKey();
            clone.setField(fieldDescriptorLite, convertToMutable(fieldDescriptorLite, arrayEntryAt.getValue()));
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            FieldDescriptorType descriptor = entry.getKey();
            clone.setField(descriptor, convertToMutable(descriptor, entry.getValue()));
        }
        clone.hasLazyField = false;
        return clone;
    }

    public void clear() {
        this.fields.clear();
        this.hasLazyField = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Map<FieldDescriptorType, Object> getAllFields() {
        if (!this.hasLazyField) {
            return this.fields.isImmutable() ? this.fields : Collections.unmodifiableMap(this.fields);
        }
        SmallSortedMap<FieldDescriptorType, Object> result = SmallSortedMap.newFieldMap(16);
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            cloneFieldEntry(result, this.fields.getArrayEntryAt(i));
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            cloneFieldEntry(result, entry);
        }
        if (this.fields.isImmutable()) {
            result.makeImmutable();
            return result;
        }
        return result;
    }

    private void cloneFieldEntry(Map<FieldDescriptorType, Object> map, Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof LazyField) {
            map.put(key, ((LazyField) value).getValue());
        } else {
            map.put(key, value);
        }
    }

    public Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
        return this.hasLazyField ? new LazyField.LazyIterator(this.fields.entrySet().iterator()) : this.fields.entrySet().iterator();
    }

    public boolean hasField(FieldDescriptorType descriptor) {
        if (descriptor.isRepeated()) {
            throw new IllegalArgumentException("hasField() can only be called on non-repeated fields.");
        }
        return this.fields.get(descriptor) != null;
    }

    public Object getField(FieldDescriptorType descriptor) {
        Object o = this.fields.get(descriptor);
        if (o instanceof LazyField) {
            return ((LazyField) o).getValue();
        }
        return o;
    }

    public void setField(FieldDescriptorType fielddescriptortype, Object obj) {
        if (fielddescriptortype.isRepeated()) {
            if (!(obj instanceof List)) {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                verifyType(fielddescriptortype.getLiteType(), it.next());
            }
            obj = arrayList;
        } else {
            verifyType(fielddescriptortype.getLiteType(), obj);
        }
        if (obj instanceof LazyField) {
            this.hasLazyField = true;
        }
        this.fields.put(fielddescriptortype, obj);
    }

    public void clearField(FieldDescriptorType descriptor) {
        this.fields.remove(descriptor);
        if (this.fields.isEmpty()) {
            this.hasLazyField = false;
        }
    }

    public int getRepeatedFieldCount(FieldDescriptorType descriptor) {
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
        }
        Object value = getField(descriptor);
        if (value == null) {
            return 0;
        }
        return ((List) value).size();
    }

    public Object getRepeatedField(FieldDescriptorType descriptor, int index) {
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
        }
        Object value = getField(descriptor);
        if (value == null) {
            throw new IndexOutOfBoundsException();
        }
        return ((List) value).get(index);
    }

    public void setRepeatedField(FieldDescriptorType descriptor, int index, Object value) {
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
        }
        Object list = getField(descriptor);
        if (list == null) {
            throw new IndexOutOfBoundsException();
        }
        verifyType(descriptor.getLiteType(), value);
        ((List) list).set(index, value);
    }

    public void addRepeatedField(FieldDescriptorType descriptor, Object value) {
        List<Object> list;
        if (!descriptor.isRepeated()) {
            throw new IllegalArgumentException("addRepeatedField() can only be called on repeated fields.");
        }
        verifyType(descriptor.getLiteType(), value);
        Object existingValue = getField(descriptor);
        if (existingValue == null) {
            list = new ArrayList<>();
            this.fields.put(descriptor, list);
        } else {
            list = (List) existingValue;
        }
        list.add(value);
    }

    private static void verifyType(WireFormat.FieldType type, Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        boolean isValid = false;
        switch (type.getJavaType()) {
            case INT:
                isValid = value instanceof Integer;
                break;
            case LONG:
                isValid = value instanceof Long;
                break;
            case FLOAT:
                isValid = value instanceof Float;
                break;
            case DOUBLE:
                isValid = value instanceof Double;
                break;
            case BOOLEAN:
                isValid = value instanceof Boolean;
                break;
            case STRING:
                isValid = value instanceof String;
                break;
            case BYTE_STRING:
                isValid = (value instanceof ByteString) || (value instanceof byte[]);
                break;
            case ENUM:
                isValid = (value instanceof Integer) || (value instanceof Internal.EnumLite);
                break;
            case MESSAGE:
                isValid = (value instanceof MessageLite) || (value instanceof LazyField);
                break;
        }
        if (!isValid) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    public boolean isInitialized() {
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            if (!isInitialized(this.fields.getArrayEntryAt(i))) {
                return false;
            }
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            if (!isInitialized(entry)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInitialized(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType descriptor = entry.getKey();
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (descriptor.isRepeated()) {
                for (MessageLite element : (List) entry.getValue()) {
                    if (!element.isInitialized()) {
                        return false;
                    }
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof MessageLite) {
                    if (!((MessageLite) value).isInitialized()) {
                        return false;
                    }
                } else {
                    if (value instanceof LazyField) {
                        return true;
                    }
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }

    static int getWireFormatForFieldType(WireFormat.FieldType type, boolean isPacked) {
        if (isPacked) {
            return 2;
        }
        return type.getWireType();
    }

    public void mergeFrom(FieldSet<FieldDescriptorType> other) {
        for (int i = 0; i < other.fields.getNumArrayEntries(); i++) {
            mergeFromField(other.fields.getArrayEntryAt(i));
        }
        Iterator i$ = other.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            mergeFromField(entry);
        }
    }

    private void mergeFromField(Map.Entry<FieldDescriptorType, Object> entry) {
        MessageLite messageLiteBuild;
        FieldDescriptorType descriptor = entry.getKey();
        Object otherValue = entry.getValue();
        if (otherValue instanceof LazyField) {
            otherValue = ((LazyField) otherValue).getValue();
        }
        if (descriptor.isRepeated()) {
            Object value = getField(descriptor);
            if (value == null) {
                this.fields.put(descriptor, new ArrayList((List) otherValue));
                return;
            } else {
                ((List) value).addAll((List) otherValue);
                return;
            }
        }
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            Object value2 = getField(descriptor);
            if (value2 == null) {
                this.fields.put(descriptor, otherValue);
                return;
            }
            if (value2 instanceof MutableMessageLite) {
                messageLiteBuild = descriptor.internalMergeFrom((MutableMessageLite) value2, (MutableMessageLite) otherValue);
            } else {
                messageLiteBuild = descriptor.internalMergeFrom(((MessageLite) value2).toBuilder(), (MessageLite) otherValue).build();
            }
            this.fields.put(descriptor, messageLiteBuild);
            return;
        }
        this.fields.put(descriptor, otherValue);
    }

    /* JADX INFO: renamed from: com.google.tagmanager.protobuf.FieldSet$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$WireFormat$FieldType = new int[WireFormat.FieldType.values().length];

        static {
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.DOUBLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.INT64.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.UINT64.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.INT32.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FIXED64.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FIXED32.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.BOOL.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.STRING.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.BYTES.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.UINT32.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SFIXED32.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SFIXED64.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SINT32.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SINT64.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.GROUP.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.MESSAGE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.ENUM.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            $SwitchMap$com$google$protobuf$WireFormat$JavaType = new int[WireFormat.JavaType.values().length];
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.BYTE_STRING.ordinal()] = 7;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.ENUM.ordinal()] = 8;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.MESSAGE.ordinal()] = 9;
            } catch (NoSuchFieldError e27) {
            }
        }
    }

    public static Object readPrimitiveField(CodedInputStream input, WireFormat.FieldType type, boolean checkUtf8) throws IOException {
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$FieldType[type.ordinal()]) {
            case 1:
                return Double.valueOf(input.readDouble());
            case 2:
                return Float.valueOf(input.readFloat());
            case 3:
                return Long.valueOf(input.readInt64());
            case 4:
                return Long.valueOf(input.readUInt64());
            case 5:
                return Integer.valueOf(input.readInt32());
            case 6:
                return Long.valueOf(input.readFixed64());
            case 7:
                return Integer.valueOf(input.readFixed32());
            case 8:
                return Boolean.valueOf(input.readBool());
            case 9:
                if (checkUtf8) {
                    return input.readStringRequireUtf8();
                }
                return input.readString();
            case 10:
                return input.readBytes();
            case 11:
                return Integer.valueOf(input.readUInt32());
            case 12:
                return Integer.valueOf(input.readSFixed32());
            case 13:
                return Long.valueOf(input.readSFixed64());
            case 14:
                return Integer.valueOf(input.readSInt32());
            case 15:
                return Long.valueOf(input.readSInt64());
            case 16:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
            case 17:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
            case 18:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static Object readPrimitiveFieldForMutable(CodedInputStream input, WireFormat.FieldType type, boolean checkUtf8) throws IOException {
        return type == WireFormat.FieldType.BYTES ? input.readByteArray() : readPrimitiveField(input, type, checkUtf8);
    }

    public void writeTo(CodedOutputStream output) throws IOException {
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<K, Object> arrayEntryAt = this.fields.getArrayEntryAt(i);
            writeField((FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue(), output);
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            writeField(entry.getKey(), entry.getValue(), output);
        }
    }

    public void writeMessageSetTo(CodedOutputStream output) throws IOException {
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            writeMessageSetTo(this.fields.getArrayEntryAt(i), output);
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            writeMessageSetTo(entry, output);
        }
    }

    private void writeMessageSetTo(Map.Entry<FieldDescriptorType, Object> entry, CodedOutputStream output) throws IOException {
        FieldDescriptorType descriptor = entry.getKey();
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !descriptor.isRepeated() && !descriptor.isPacked()) {
            output.writeMessageSetExtension(entry.getKey().getNumber(), (MessageLite) entry.getValue());
        } else {
            writeField(descriptor, entry.getValue(), output);
        }
    }

    private static void writeElement(CodedOutputStream output, WireFormat.FieldType type, int number, Object value) throws IOException {
        if (type == WireFormat.FieldType.GROUP) {
            if (Internal.isProto1Group((MessageLite) value)) {
                output.writeTag(number, 3);
                output.writeGroupNoTag((MessageLite) value);
                return;
            } else {
                output.writeGroup(number, (MessageLite) value);
                return;
            }
        }
        output.writeTag(number, getWireFormatForFieldType(type, false));
        writeElementNoTag(output, type, value);
    }

    private static void writeElementNoTag(CodedOutputStream output, WireFormat.FieldType type, Object value) throws IOException {
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$FieldType[type.ordinal()]) {
            case 1:
                output.writeDoubleNoTag(((Double) value).doubleValue());
                break;
            case 2:
                output.writeFloatNoTag(((Float) value).floatValue());
                break;
            case 3:
                output.writeInt64NoTag(((Long) value).longValue());
                break;
            case 4:
                output.writeUInt64NoTag(((Long) value).longValue());
                break;
            case 5:
                output.writeInt32NoTag(((Integer) value).intValue());
                break;
            case 6:
                output.writeFixed64NoTag(((Long) value).longValue());
                break;
            case 7:
                output.writeFixed32NoTag(((Integer) value).intValue());
                break;
            case 8:
                output.writeBoolNoTag(((Boolean) value).booleanValue());
                break;
            case 9:
                output.writeStringNoTag((String) value);
                break;
            case 10:
                if (value instanceof ByteString) {
                    output.writeBytesNoTag((ByteString) value);
                } else {
                    output.writeByteArrayNoTag((byte[]) value);
                }
                break;
            case 11:
                output.writeUInt32NoTag(((Integer) value).intValue());
                break;
            case 12:
                output.writeSFixed32NoTag(((Integer) value).intValue());
                break;
            case 13:
                output.writeSFixed64NoTag(((Long) value).longValue());
                break;
            case 14:
                output.writeSInt32NoTag(((Integer) value).intValue());
                break;
            case 15:
                output.writeSInt64NoTag(((Long) value).longValue());
                break;
            case 16:
                output.writeGroupNoTag((MessageLite) value);
                break;
            case 17:
                output.writeMessageNoTag((MessageLite) value);
                break;
            case 18:
                if (value instanceof Internal.EnumLite) {
                    output.writeEnumNoTag(((Internal.EnumLite) value).getNumber());
                } else {
                    output.writeEnumNoTag(((Integer) value).intValue());
                }
                break;
        }
    }

    public static void writeField(FieldDescriptorLite<?> descriptor, Object value, CodedOutputStream output) throws IOException {
        WireFormat.FieldType type = descriptor.getLiteType();
        int number = descriptor.getNumber();
        if (descriptor.isRepeated()) {
            List<?> valueList = (List) value;
            if (descriptor.isPacked()) {
                output.writeTag(number, 2);
                int dataSize = 0;
                for (Object element : valueList) {
                    dataSize += computeElementSizeNoTag(type, element);
                }
                output.writeRawVarint32(dataSize);
                for (Object element2 : valueList) {
                    writeElementNoTag(output, type, element2);
                }
                return;
            }
            for (Object element3 : valueList) {
                writeElement(output, type, number, element3);
            }
            return;
        }
        if (value instanceof LazyField) {
            writeElement(output, type, number, ((LazyField) value).getValue());
        } else {
            writeElement(output, type, number, value);
        }
    }

    public int getSerializedSize() {
        int size = 0;
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            Map.Entry<K, Object> arrayEntryAt = this.fields.getArrayEntryAt(i);
            size += computeFieldSize((FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue());
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            size += computeFieldSize(entry.getKey(), entry.getValue());
        }
        return size;
    }

    public int getMessageSetSerializedSize() {
        int size = 0;
        for (int i = 0; i < this.fields.getNumArrayEntries(); i++) {
            size += getMessageSetSerializedSize(this.fields.getArrayEntryAt(i));
        }
        Iterator i$ = this.fields.getOverflowEntries().iterator();
        while (i$.hasNext()) {
            Map.Entry<FieldDescriptorType, Object> entry = (Map.Entry) i$.next();
            size += getMessageSetSerializedSize(entry);
        }
        return size;
    }

    private int getMessageSetSerializedSize(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorType descriptor = entry.getKey();
        Object value = entry.getValue();
        if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !descriptor.isRepeated() && !descriptor.isPacked()) {
            if (value instanceof LazyField) {
                return CodedOutputStream.computeLazyFieldMessageSetExtensionSize(entry.getKey().getNumber(), (LazyField) value);
            }
            return CodedOutputStream.computeMessageSetExtensionSize(entry.getKey().getNumber(), (MessageLite) value);
        }
        return computeFieldSize(descriptor, value);
    }

    private static int computeElementSize(WireFormat.FieldType type, int number, Object value) {
        int tagSize = CodedOutputStream.computeTagSize(number);
        if (type == WireFormat.FieldType.GROUP && !Internal.isProto1Group((MessageLite) value)) {
            tagSize *= 2;
        }
        return computeElementSizeNoTag(type, value) + tagSize;
    }

    private static int computeElementSizeNoTag(WireFormat.FieldType type, Object value) {
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$FieldType[type.ordinal()]) {
            case 1:
                return CodedOutputStream.computeDoubleSizeNoTag(((Double) value).doubleValue());
            case 2:
                return CodedOutputStream.computeFloatSizeNoTag(((Float) value).floatValue());
            case 3:
                return CodedOutputStream.computeInt64SizeNoTag(((Long) value).longValue());
            case 4:
                return CodedOutputStream.computeUInt64SizeNoTag(((Long) value).longValue());
            case 5:
                return CodedOutputStream.computeInt32SizeNoTag(((Integer) value).intValue());
            case 6:
                return CodedOutputStream.computeFixed64SizeNoTag(((Long) value).longValue());
            case 7:
                return CodedOutputStream.computeFixed32SizeNoTag(((Integer) value).intValue());
            case 8:
                return CodedOutputStream.computeBoolSizeNoTag(((Boolean) value).booleanValue());
            case 9:
                return CodedOutputStream.computeStringSizeNoTag((String) value);
            case 10:
                if (value instanceof ByteString) {
                    return CodedOutputStream.computeBytesSizeNoTag((ByteString) value);
                }
                return CodedOutputStream.computeByteArraySizeNoTag((byte[]) value);
            case 11:
                return CodedOutputStream.computeUInt32SizeNoTag(((Integer) value).intValue());
            case 12:
                return CodedOutputStream.computeSFixed32SizeNoTag(((Integer) value).intValue());
            case 13:
                return CodedOutputStream.computeSFixed64SizeNoTag(((Long) value).longValue());
            case 14:
                return CodedOutputStream.computeSInt32SizeNoTag(((Integer) value).intValue());
            case 15:
                return CodedOutputStream.computeSInt64SizeNoTag(((Long) value).longValue());
            case 16:
                return CodedOutputStream.computeGroupSizeNoTag((MessageLite) value);
            case 17:
                if (value instanceof LazyField) {
                    return CodedOutputStream.computeLazyFieldSizeNoTag((LazyField) value);
                }
                return CodedOutputStream.computeMessageSizeNoTag((MessageLite) value);
            case 18:
                if (value instanceof Internal.EnumLite) {
                    return CodedOutputStream.computeEnumSizeNoTag(((Internal.EnumLite) value).getNumber());
                }
                return CodedOutputStream.computeEnumSizeNoTag(((Integer) value).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static int computeFieldSize(FieldDescriptorLite<?> descriptor, Object value) {
        WireFormat.FieldType type = descriptor.getLiteType();
        int number = descriptor.getNumber();
        if (descriptor.isRepeated()) {
            if (descriptor.isPacked()) {
                int dataSize = 0;
                for (Object element : (List) value) {
                    dataSize += computeElementSizeNoTag(type, element);
                }
                return CodedOutputStream.computeTagSize(number) + dataSize + CodedOutputStream.computeRawVarint32Size(dataSize);
            }
            int size = 0;
            for (Object element2 : (List) value) {
                size += computeElementSize(type, number, element2);
            }
            return size;
        }
        int size2 = computeElementSize(type, number, value);
        return size2;
    }
}
