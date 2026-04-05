package com.kopin.accessory.packets.base;

import com.kopin.accessory.base.PacketContent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.CharEncoding;

/* JADX INFO: loaded from: classes14.dex */
public abstract class FlagPacketContent extends PacketContent {
    private Map<Integer, Entry> dataEntries;
    private int flags;

    private interface ArrayFillCallback<T> {
        T get();
    }

    protected abstract FlagDescriptor[] getDescriptors();

    private class Entry {
        public Object data;
        public FlagDescriptor descriptor;

        private Entry() {
        }
    }

    public FlagPacketContent() {
        super(null);
        this.dataEntries = new HashMap();
        this.flags = 0;
    }

    public FlagPacketContent(ByteBuffer byteBuffer) {
        super(byteBuffer);
        this.dataEntries = new HashMap();
        this.flags = byteBuffer.getInt();
        readData(this.flags, byteBuffer);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public void write(ByteBuffer byteBuffer) {
        byteBuffer.putInt(this.flags);
        writeData(this.flags, byteBuffer);
    }

    @Override // com.kopin.accessory.base.PacketContent
    public int getSize() {
        int length = 4;
        FlagDescriptor[] descriptors = getDescriptors();
        for (FlagDescriptor descriptor : descriptors) {
            if ((this.flags & descriptor.getFlag()) == descriptor.getFlag()) {
                if (descriptor.getType().equals(Long.TYPE)) {
                    length += descriptor.getCount() * 8;
                } else if (descriptor.getType().equals(Integer.TYPE)) {
                    length += descriptor.getCount() * 4;
                } else if (descriptor.getType().equals(Short.TYPE)) {
                    length += descriptor.getCount() * 2;
                } else if (descriptor.getType().equals(Byte.TYPE)) {
                    length += descriptor.getCount() * 1;
                } else if (descriptor.getType().equals(Boolean.TYPE)) {
                    length += descriptor.getCount() * 1;
                } else if (descriptor.getType().equals(String.class)) {
                    length += descriptor.getCount() * 2;
                }
            }
        }
        return length;
    }

    public int getFlags() {
        return this.flags;
    }

    public byte getByte(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Byte.TYPE || entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return ((Byte) entry.data).byteValue();
    }

    public byte[] getByteArray(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Byte.TYPE || !entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return (byte[]) entry.data;
    }

    public short getShort(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Short.TYPE || entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return ((Short) entry.data).shortValue();
    }

    public short[] getShortArray(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Short.TYPE || !entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return (short[]) entry.data;
    }

    public int getInt(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Integer.TYPE || entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return ((Integer) entry.data).intValue();
    }

    public int[] getIntArray(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Integer.TYPE || !entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return (int[]) entry.data;
    }

    public long getLong(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Long.TYPE || entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return ((Long) entry.data).longValue();
    }

    public long[] getLongArray(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != Long.TYPE || !entry.descriptor.isArray()) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return (long[]) entry.data;
    }

    public String getString(int flag) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry.descriptor.getType() != String.class) {
            throw new RuntimeException("Flag is of wrong type.");
        }
        return (String) entry.data;
    }

    public boolean hasEntry(int flag) {
        return this.dataEntries.containsKey(Integer.valueOf(flag));
    }

    public boolean hasEntryOfType(int flag, Class type) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        return entry != null && entry.getClass().isInstance(type);
    }

    public void putBytes(FlagPacketEnum packetEnum, byte... values) {
        Entry entry = findEntry(packetEnum.getFlag(), Byte.TYPE, values.length);
        if (values.length > 1) {
            entry.data = values;
        } else {
            entry.data = Byte.valueOf(values[0]);
        }
        this.flags |= packetEnum.getFlag();
    }

    public void putBooleans(FlagPacketEnum packetEnum, boolean... values) {
        Entry entry = findEntry(packetEnum.getFlag(), Boolean.TYPE, values.length);
        if (values.length > 1) {
            entry.data = values;
        } else {
            entry.data = Boolean.valueOf(values[0]);
        }
        this.flags |= packetEnum.getFlag();
    }

    public void putShorts(FlagPacketEnum packetEnum, short... values) {
        Entry entry = findEntry(packetEnum.getFlag(), Short.TYPE, values.length);
        if (values.length > 1) {
            entry.data = values;
        } else {
            entry.data = Short.valueOf(values[0]);
        }
        this.flags |= packetEnum.getFlag();
    }

    public void putIntegers(FlagPacketEnum packetEnum, int... values) {
        Entry entry = findEntry(packetEnum.getFlag(), Byte.TYPE, values.length);
        if (values.length > 1) {
            entry.data = values;
        } else {
            entry.data = Integer.valueOf(values[0]);
        }
        this.flags |= packetEnum.getFlag();
    }

    public void putLongs(FlagPacketEnum packetEnum, long... values) {
        Entry entry = findEntry(packetEnum.getFlag(), Byte.TYPE, values.length);
        if (values.length > 1) {
            entry.data = values;
        } else {
            entry.data = Long.valueOf(values[0]);
        }
        this.flags |= packetEnum.getFlag();
    }

    public final void putString(FlagPacketEnum packetEnum, String string) {
        Entry entry = this.dataEntries.get(Integer.valueOf(packetEnum.getFlag()));
        if (entry == null) {
            FlagDescriptor descriptor = findCompatibleDescriptor(packetEnum.getFlag(), String.class, -1);
            entry = new Entry();
            entry.descriptor = descriptor;
            this.dataEntries.put(Integer.valueOf(descriptor.getFlag()), entry);
        } else if (entry.descriptor.getFlag() != packetEnum.getFlag() || !entry.descriptor.getType().equals(String.class)) {
            throw new RuntimeException("No valid descriptor found.");
        }
        entry.data = string.substring(0, Math.min(string.length(), entry.descriptor.getCount()));
        this.flags |= packetEnum.getFlag();
    }

    private void readData(int flags, final ByteBuffer byteBuffer) {
        this.dataEntries.clear();
        FlagDescriptor[] descriptors = getDescriptors();
        for (FlagDescriptor descriptor : descriptors) {
            if ((descriptor.getFlag() & flags) == descriptor.getFlag()) {
                if (byteBuffer.position() >= byteBuffer.limit()) {
                    System.out.println("FlagPacketContent: Expected more packets, but not received enough");
                    return;
                }
                Entry newEntry = new Entry();
                newEntry.descriptor = descriptor;
                if (descriptor.getType().equals(Long.TYPE)) {
                    if (descriptor.isArray()) {
                        Long[] array = new Long[descriptor.getCount()];
                        fillArray(array, new ArrayFillCallback<Long>() { // from class: com.kopin.accessory.packets.base.FlagPacketContent.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // com.kopin.accessory.packets.base.FlagPacketContent.ArrayFillCallback
                            public Long get() {
                                return Long.valueOf(byteBuffer.getLong());
                            }
                        });
                        newEntry.data = array;
                    } else {
                        newEntry.data = Long.valueOf(byteBuffer.getLong());
                    }
                } else if (descriptor.getType().equals(Integer.TYPE)) {
                    if (descriptor.isArray()) {
                        Integer[] array2 = new Integer[descriptor.getCount()];
                        fillArray(array2, new ArrayFillCallback<Integer>() { // from class: com.kopin.accessory.packets.base.FlagPacketContent.2
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // com.kopin.accessory.packets.base.FlagPacketContent.ArrayFillCallback
                            public Integer get() {
                                return Integer.valueOf(byteBuffer.getInt());
                            }
                        });
                        newEntry.data = array2;
                    } else {
                        newEntry.data = Integer.valueOf(byteBuffer.getInt());
                    }
                } else if (descriptor.getType().equals(Short.TYPE)) {
                    if (descriptor.isArray()) {
                        Short[] array3 = new Short[descriptor.getCount()];
                        fillArray(array3, new ArrayFillCallback<Short>() { // from class: com.kopin.accessory.packets.base.FlagPacketContent.3
                            @Override // com.kopin.accessory.packets.base.FlagPacketContent.ArrayFillCallback
                            public Short get() {
                                return Short.valueOf(byteBuffer.getShort());
                            }
                        });
                        newEntry.data = array3;
                    } else {
                        newEntry.data = Short.valueOf(byteBuffer.getShort());
                    }
                } else if (descriptor.getType().equals(Byte.TYPE)) {
                    if (descriptor.isArray()) {
                        byte[] array4 = new byte[descriptor.getCount()];
                        byteBuffer.get(array4);
                        newEntry.data = array4;
                    } else {
                        newEntry.data = Byte.valueOf(byteBuffer.get());
                    }
                } else if (descriptor.getType().equals(String.class)) {
                    byte[] data = new byte[descriptor.getCount() * 2];
                    byteBuffer.get(data);
                    int length = 0;
                    for (int i = 0; i < data.length; i += 2) {
                        if (data[i] == 0) {
                            length++;
                        }
                    }
                    try {
                        String tmp = new String(data, 0, length * 2, CharEncoding.UTF_16LE);
                        if (tmp.indexOf(0) != -1) {
                            newEntry.data = tmp.substring(0, tmp.indexOf(0));
                        } else {
                            newEntry.data = tmp;
                        }
                    } catch (UnsupportedEncodingException e) {
                    }
                }
                this.dataEntries.put(Integer.valueOf(newEntry.descriptor.getFlag()), newEntry);
            }
        }
    }

    private void writeData(int flags, ByteBuffer byteBuffer) {
        Entry entry;
        FlagDescriptor[] descriptors = getDescriptors();
        for (FlagDescriptor descriptor : descriptors) {
            if ((descriptor.getFlag() & flags) == descriptor.getFlag() && (entry = this.dataEntries.get(Integer.valueOf(descriptor.getFlag()))) != null) {
                if (descriptor.getType().equals(Long.TYPE)) {
                    if (descriptor.isArray()) {
                        long[] array = (long[]) entry.data;
                        for (int i = 0; i < descriptor.getCount(); i++) {
                            byteBuffer.putLong(array[i]);
                        }
                    } else {
                        byteBuffer.putLong(((Long) entry.data).longValue());
                    }
                } else if (descriptor.getType().equals(Integer.TYPE)) {
                    if (descriptor.isArray()) {
                        int[] array2 = (int[]) entry.data;
                        for (int i2 = 0; i2 < descriptor.getCount(); i2++) {
                            byteBuffer.putInt(array2[i2]);
                        }
                    } else {
                        byteBuffer.putInt(((Integer) entry.data).intValue());
                    }
                } else if (descriptor.getType().equals(Short.TYPE)) {
                    if (descriptor.isArray()) {
                        short[] array3 = (short[]) entry.data;
                        for (int i3 = 0; i3 < descriptor.getCount(); i3++) {
                            byteBuffer.putShort(array3[i3]);
                        }
                    } else {
                        byteBuffer.putShort(((Short) entry.data).shortValue());
                    }
                } else if (descriptor.getType().equals(Boolean.TYPE)) {
                    if (descriptor.isArray()) {
                        boolean[] array4 = (boolean[]) entry.data;
                        for (int i4 = 0; i4 < descriptor.getCount(); i4++) {
                            byteBuffer.put((byte) (array4[i4] ? 1 : 0));
                        }
                    } else {
                        byteBuffer.put((byte) (((Boolean) entry.data).booleanValue() ? 1 : 0));
                    }
                } else if (descriptor.getType().equals(Byte.TYPE)) {
                    if (descriptor.isArray()) {
                        byte[] array5 = (byte[]) entry.data;
                        for (int i5 = 0; i5 < descriptor.getCount(); i5++) {
                            byteBuffer.put(array5[i5]);
                        }
                    } else {
                        byteBuffer.put(((Byte) entry.data).byteValue());
                    }
                } else if (descriptor.getType().equals(String.class)) {
                    String string = (String) entry.data;
                    try {
                        byte[] stringData = string.getBytes(CharEncoding.UTF_16LE);
                        byte[] data = new byte[entry.descriptor.getCount() * 2];
                        Arrays.fill(data, (byte) 0);
                        System.arraycopy(stringData, 0, data, 0, Math.min(data.length, stringData.length));
                        byteBuffer.put(data);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
        }
    }

    private FlagDescriptor findCompatibleDescriptor(int flag, Type type, int count) {
        for (FlagDescriptor descriptor : getDescriptors()) {
            if (descriptor.getFlag() == flag && descriptor.getType().equals(type) && (count == -1 || descriptor.getCount() == count)) {
                return descriptor;
            }
        }
        throw new RuntimeException("No valid descriptor found.");
    }

    private Entry findEntry(int flag, Type type, int length) {
        Entry entry = this.dataEntries.get(Integer.valueOf(flag));
        if (entry == null) {
            FlagDescriptor descriptor = findCompatibleDescriptor(flag, type, length);
            Entry entry2 = new Entry();
            entry2.descriptor = descriptor;
            this.dataEntries.put(Integer.valueOf(descriptor.getFlag()), entry2);
            return entry2;
        }
        if (entry.descriptor.getFlag() != flag || !entry.descriptor.getType().equals(type) || entry.descriptor.getCount() != length) {
            throw new RuntimeException("No valid descriptor found.");
        }
        return entry;
    }

    private static <T> void fillArray(T[] array, ArrayFillCallback<T> fillCallback) {
        for (int i = 0; i < array.length; i++) {
            array[i] = fillCallback.get();
        }
    }
}
