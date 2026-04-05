package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.AbstractMessageLite;
import com.google.tagmanager.protobuf.FieldSet;
import com.google.tagmanager.protobuf.GeneratedMutableMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
public abstract class GeneratedMessageLite extends AbstractMessageLite implements Serializable {
    private static final long serialVersionUID = 1;

    public interface ExtendableMessageOrBuilder<MessageType extends ExtendableMessage> extends MessageLiteOrBuilder {
        <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension);

        <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i);

        <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension);

        <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension);
    }

    protected GeneratedMessageLite() {
    }

    protected GeneratedMessageLite(Builder builder) {
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public Parser<? extends MessageLite> getParserForType() {
        throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
    }

    protected boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        return input.skipField(tag, unknownFieldsCodedOutput);
    }

    protected void makeExtensionsImmutable() {
    }

    public static abstract class Builder<MessageType extends GeneratedMessageLite, BuilderType extends Builder> extends AbstractMessageLite.Builder<BuilderType> {
        private ByteString unknownFields = ByteString.EMPTY;

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public abstract MessageType getDefaultInstanceForType();

        public abstract BuilderType mergeFrom(MessageType messagetype);

        protected Builder() {
        }

        @Override // com.google.tagmanager.protobuf.MessageLite.Builder
        public BuilderType clear() {
            this.unknownFields = ByteString.EMPTY;
            return this;
        }

        @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder
        /* JADX INFO: renamed from: clone */
        public BuilderType mo5clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        protected boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
            return input.skipField(tag, unknownFieldsCodedOutput);
        }

        public final ByteString getUnknownFields() {
            return this.unknownFields;
        }

        public final BuilderType setUnknownFields(ByteString unknownFields) {
            this.unknownFields = unknownFields;
            return this;
        }
    }

    protected MutableMessageLite internalMutableDefault() {
        throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
    }

    protected static MutableMessageLite internalMutableDefault(String name) {
        try {
            Method defaultInstanceMethod = getMethodOrDie(Class.forName(name), "getDefaultInstance", new Class[0]);
            return (MutableMessageLite) invokeOrDie(defaultInstanceMethod, null, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Cannot load the corresponding mutable class. Please add necessary dependencies.");
        }
    }

    @Override // com.google.tagmanager.protobuf.AbstractMessageLite, com.google.tagmanager.protobuf.MessageLite
    public MutableMessageLite mutableCopy() {
        MutableMessageLite result = internalMutableDefault().newMessageForType();
        if (this != getDefaultInstanceForType()) {
            result.mergeFrom(CodedInputStream.newInstance(toByteArray()));
        }
        return result;
    }

    public static abstract class ExtendableMessage<MessageType extends ExtendableMessage<MessageType>> extends GeneratedMessageLite implements ExtendableMessageOrBuilder<MessageType> {
        private final FieldSet<ExtensionDescriptor> extensions;

        protected ExtendableMessage() {
            this.extensions = FieldSet.newFieldSet();
        }

        protected ExtendableMessage(ExtendableBuilder<MessageType, ?> builder) {
            this.extensions = builder.buildExtensions();
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.hasField(generatedExtension.descriptor);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.getRepeatedFieldCount(extension.descriptor);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Object field = this.extensions.getField(generatedExtension.descriptor);
            return field == null ? generatedExtension.defaultValue : (Type) generatedExtension.fromFieldSetType(field);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return (Type) generatedExtension.singularFromFieldSetType(this.extensions.getRepeatedField(generatedExtension.descriptor, i));
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
            return GeneratedMessageLite.parseUnknownField(this.extensions, getDefaultInstanceForType(), input, unknownFieldsCodedOutput, extensionRegistry, tag);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.AbstractMessageLite, com.google.tagmanager.protobuf.MessageLite
        public MutableMessageLite mutableCopy() {
            GeneratedMutableMessageLite.ExtendableMutableMessage result = (GeneratedMutableMessageLite.ExtendableMutableMessage) super.mutableCopy();
            result.internalSetExtensionSet(this.extensions.cloneWithAllFieldsToMutable());
            return result;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected void makeExtensionsImmutable() {
            this.extensions.makeImmutable();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public class ExtensionWriter {
            private final Iterator<Map.Entry<ExtensionDescriptor, Object>> iter;
            private final boolean messageSetWireFormat;
            private Map.Entry<ExtensionDescriptor, Object> next;

            private ExtensionWriter(boolean messageSetWireFormat) {
                this.iter = ExtendableMessage.this.extensions.iterator();
                if (this.iter.hasNext()) {
                    this.next = this.iter.next();
                }
                this.messageSetWireFormat = messageSetWireFormat;
            }

            public void writeUntil(int end, CodedOutputStream output) throws IOException {
                while (this.next != null && this.next.getKey().getNumber() < end) {
                    ExtensionDescriptor extension = this.next.getKey();
                    if (this.messageSetWireFormat && extension.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !extension.isRepeated()) {
                        output.writeMessageSetExtension(extension.getNumber(), (MessageLite) this.next.getValue());
                    } else {
                        FieldSet.writeField(extension, this.next.getValue(), output);
                    }
                    if (this.iter.hasNext()) {
                        this.next = this.iter.next();
                    } else {
                        this.next = null;
                    }
                }
            }
        }

        protected ExtendableMessage<MessageType>.ExtensionWriter newExtensionWriter() {
            return new ExtensionWriter(false);
        }

        protected ExtendableMessage<MessageType>.ExtensionWriter newMessageSetExtensionWriter() {
            return new ExtensionWriter(true);
        }

        protected int extensionsSerializedSize() {
            return this.extensions.getSerializedSize();
        }

        protected int extensionsSerializedSizeAsMessageSet() {
            return this.extensions.getMessageSetSerializedSize();
        }
    }

    public static abstract class ExtendableBuilder<MessageType extends ExtendableMessage<MessageType>, BuilderType extends ExtendableBuilder<MessageType, BuilderType>> extends Builder<MessageType, BuilderType> implements ExtendableMessageOrBuilder<MessageType> {
        private FieldSet<ExtensionDescriptor> extensions = FieldSet.emptySet();
        private boolean extensionsIsMutable;

        protected ExtendableBuilder() {
        }

        void internalSetExtensionSet(FieldSet<ExtensionDescriptor> extensions) {
            this.extensions = extensions;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
        public BuilderType clear() {
            this.extensions.clear();
            this.extensionsIsMutable = false;
            return (BuilderType) super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (!this.extensionsIsMutable) {
                this.extensions = this.extensions.m8clone();
                this.extensionsIsMutable = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FieldSet<ExtensionDescriptor> buildExtensions() {
            this.extensions.makeImmutable();
            this.extensionsIsMutable = false;
            return this.extensions;
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.hasField(generatedExtension.descriptor);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.getRepeatedFieldCount(extension.descriptor);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Object field = this.extensions.getField(generatedExtension.descriptor);
            return field == null ? generatedExtension.defaultValue : (Type) generatedExtension.fromFieldSetType(field);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableMessageOrBuilder
        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return (Type) generatedExtension.singularFromFieldSetType(this.extensions.getRepeatedField(generatedExtension.descriptor, i));
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
        /* JADX INFO: renamed from: clone */
        public BuilderType mo5clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, Type> generatedExtension, Type value) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.setField(generatedExtension.descriptor, generatedExtension.toFieldSetType(value));
            return this;
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, List<Type>> extension, int index, Type value) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.setRepeatedField(extension.descriptor, index, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> BuilderType addExtension(GeneratedExtension<MessageType, List<Type>> extension, Type value) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> BuilderType clearExtension(GeneratedExtension<MessageType, ?> extension) {
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.clearField(extension.descriptor);
            return this;
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
        protected boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
            ensureExtensionsIsMutable();
            return GeneratedMessageLite.parseUnknownField(this.extensions, getDefaultInstanceForType(), input, unknownFieldsCodedOutput, extensionRegistry, tag);
        }

        protected final void mergeExtensionFields(MessageType other) {
            ensureExtensionsIsMutable();
            this.extensions.mergeFrom(((ExtendableMessage) other).extensions);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <MessageType extends MessageLite> boolean parseUnknownField(FieldSet<ExtensionDescriptor> extensions, MessageType defaultInstance, CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        Object objFindValueByNumber;
        MessageLite existingValue;
        int wireType = WireFormat.getTagWireType(tag);
        int fieldNumber = WireFormat.getTagFieldNumber(tag);
        GeneratedExtension<MessageType, ?> extension = extensionRegistry.findLiteExtensionByNumber(defaultInstance, fieldNumber);
        boolean unknown = false;
        boolean packed = false;
        if (extension == null) {
            unknown = true;
        } else if (wireType == FieldSet.getWireFormatForFieldType(extension.descriptor.getLiteType(), false)) {
            packed = false;
        } else if (extension.descriptor.isRepeated && extension.descriptor.type.isPackable() && wireType == FieldSet.getWireFormatForFieldType(extension.descriptor.getLiteType(), true)) {
            packed = true;
        } else {
            unknown = true;
        }
        if (unknown) {
            return input.skipField(tag, unknownFieldsCodedOutput);
        }
        if (packed) {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (extension.descriptor.getLiteType() == WireFormat.FieldType.ENUM) {
                while (input.getBytesUntilLimit() > 0) {
                    Internal.EnumLite value = extension.descriptor.getEnumType().findValueByNumber(input.readEnum());
                    if (value == null) {
                        return true;
                    }
                    extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value));
                }
            } else {
                while (input.getBytesUntilLimit() > 0) {
                    extensions.addRepeatedField(extension.descriptor, FieldSet.readPrimitiveField(input, extension.descriptor.getLiteType(), false));
                }
            }
            input.popLimit(limit);
        } else {
            switch (extension.descriptor.getLiteJavaType()) {
                case MESSAGE:
                    MessageLite.Builder subBuilder = null;
                    if (!extension.descriptor.isRepeated() && (existingValue = (MessageLite) extensions.getField(extension.descriptor)) != null) {
                        subBuilder = existingValue.toBuilder();
                    }
                    if (subBuilder == null) {
                        subBuilder = extension.getMessageDefaultInstance().newBuilderForType();
                    }
                    if (extension.descriptor.getLiteType() == WireFormat.FieldType.GROUP) {
                        input.readGroup(extension.getNumber(), subBuilder, extensionRegistry);
                    } else {
                        input.readMessage(subBuilder, extensionRegistry);
                    }
                    objFindValueByNumber = subBuilder.build();
                    break;
                case ENUM:
                    int rawValue = input.readEnum();
                    objFindValueByNumber = extension.descriptor.getEnumType().findValueByNumber(rawValue);
                    if (objFindValueByNumber == null) {
                        unknownFieldsCodedOutput.writeRawVarint32(tag);
                        unknownFieldsCodedOutput.writeUInt32NoTag(rawValue);
                        return true;
                    }
                    break;
                default:
                    objFindValueByNumber = FieldSet.readPrimitiveField(input, extension.descriptor.getLiteType(), false);
                    break;
            }
            if (extension.descriptor.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(objFindValueByNumber));
            } else {
                extensions.setField(extension.descriptor, extension.singularToFieldSetType(objFindValueByNumber));
            }
        }
        return true;
    }

    public static <ContainingType extends MessageLite, Type> GeneratedExtension<ContainingType, Type> newSingularGeneratedExtension(ContainingType containingTypeDefaultInstance, Type defaultValue, MessageLite messageDefaultInstance, Internal.EnumLiteMap<?> enumTypeMap, int number, WireFormat.FieldType type, Class singularType) {
        return new GeneratedExtension<>(containingTypeDefaultInstance, defaultValue, messageDefaultInstance, new ExtensionDescriptor(enumTypeMap, number, type, false, false), singularType);
    }

    public static <ContainingType extends MessageLite, Type> GeneratedExtension<ContainingType, Type> newRepeatedGeneratedExtension(ContainingType containingTypeDefaultInstance, MessageLite messageDefaultInstance, Internal.EnumLiteMap<?> enumTypeMap, int number, WireFormat.FieldType type, boolean isPacked, Class singularType) {
        return new GeneratedExtension<>(containingTypeDefaultInstance, Collections.emptyList(), messageDefaultInstance, new ExtensionDescriptor(enumTypeMap, number, type, true, isPacked), singularType);
    }

    static final class ExtensionDescriptor implements FieldSet.FieldDescriptorLite<ExtensionDescriptor> {
        final Internal.EnumLiteMap<?> enumTypeMap;
        final boolean isPacked;
        final boolean isRepeated;
        final int number;
        final WireFormat.FieldType type;

        ExtensionDescriptor(Internal.EnumLiteMap<?> enumTypeMap, int number, WireFormat.FieldType type, boolean isRepeated, boolean isPacked) {
            this.enumTypeMap = enumTypeMap;
            this.number = number;
            this.type = type;
            this.isRepeated = isRepeated;
            this.isPacked = isPacked;
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public int getNumber() {
            return this.number;
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public WireFormat.FieldType getLiteType() {
            return this.type;
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public WireFormat.JavaType getLiteJavaType() {
            return this.type.getJavaType();
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public boolean isRepeated() {
            return this.isRepeated;
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public boolean isPacked() {
            return this.isPacked;
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public Internal.EnumLiteMap<?> getEnumType() {
            return this.enumTypeMap;
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public MessageLite.Builder internalMergeFrom(MessageLite.Builder to, MessageLite from) {
            return ((Builder) to).mergeFrom((GeneratedMessageLite) from);
        }

        @Override // com.google.tagmanager.protobuf.FieldSet.FieldDescriptorLite
        public MutableMessageLite internalMergeFrom(MutableMessageLite to, MutableMessageLite from) {
            return ((GeneratedMutableMessageLite) to).mergeFrom((GeneratedMutableMessageLite) from);
        }

        @Override // java.lang.Comparable
        public int compareTo(ExtensionDescriptor other) {
            return this.number - other.number;
        }
    }

    static Method getMethodOrDie(Class clazz, String name, Class... params) {
        try {
            return clazz.getMethod(name, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Generated message class \"" + clazz.getName() + "\" missing method \"" + name + "\".", e);
        }
    }

    static Object invokeOrDie(Method method, Object object, Object... params) throws Throwable {
        try {
            return method.invoke(object, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
        }
    }

    public static class GeneratedExtension<ContainingType extends MessageLite, Type> {
        final ContainingType containingTypeDefaultInstance;
        final Type defaultValue;
        final ExtensionDescriptor descriptor;
        final Method enumValueOf;
        final MessageLite messageDefaultInstance;
        final Class singularType;

        GeneratedExtension(ContainingType containingTypeDefaultInstance, Type defaultValue, MessageLite messageDefaultInstance, ExtensionDescriptor descriptor, Class singularType) {
            if (containingTypeDefaultInstance == null) {
                throw new IllegalArgumentException("Null containingTypeDefaultInstance");
            }
            if (descriptor.getLiteType() == WireFormat.FieldType.MESSAGE && messageDefaultInstance == null) {
                throw new IllegalArgumentException("Null messageDefaultInstance");
            }
            this.containingTypeDefaultInstance = containingTypeDefaultInstance;
            this.defaultValue = defaultValue;
            this.messageDefaultInstance = messageDefaultInstance;
            this.descriptor = descriptor;
            this.singularType = singularType;
            if (Internal.EnumLite.class.isAssignableFrom(singularType)) {
                this.enumValueOf = GeneratedMessageLite.getMethodOrDie(singularType, "valueOf", Integer.TYPE);
            } else {
                this.enumValueOf = null;
            }
        }

        public ContainingType getContainingTypeDefaultInstance() {
            return this.containingTypeDefaultInstance;
        }

        public int getNumber() {
            return this.descriptor.getNumber();
        }

        public MessageLite getMessageDefaultInstance() {
            return this.messageDefaultInstance;
        }

        Object fromFieldSetType(Object value) {
            if (!this.descriptor.isRepeated()) {
                return singularFromFieldSetType(value);
            }
            if (this.descriptor.getLiteJavaType() != WireFormat.JavaType.ENUM) {
                return value;
            }
            List result = new ArrayList();
            for (Object element : (List) value) {
                result.add(singularFromFieldSetType(element));
            }
            return result;
        }

        Object singularFromFieldSetType(Object value) {
            return this.descriptor.getLiteJavaType() == WireFormat.JavaType.ENUM ? GeneratedMessageLite.invokeOrDie(this.enumValueOf, null, (Integer) value) : value;
        }

        Object toFieldSetType(Object value) {
            if (!this.descriptor.isRepeated()) {
                return singularToFieldSetType(value);
            }
            if (this.descriptor.getLiteJavaType() != WireFormat.JavaType.ENUM) {
                return value;
            }
            List result = new ArrayList();
            for (Object element : (List) value) {
                result.add(singularToFieldSetType(element));
            }
            return result;
        }

        Object singularToFieldSetType(Object value) {
            if (this.descriptor.getLiteJavaType() == WireFormat.JavaType.ENUM) {
                return Integer.valueOf(((Internal.EnumLite) value).getNumber());
            }
            return value;
        }
    }

    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private byte[] asBytes;
        private String messageClassName;

        SerializedForm(MessageLite regularForm) {
            this.messageClassName = regularForm.getClass().getName();
            this.asBytes = regularForm.toByteArray();
        }

        protected Object readResolve() throws ObjectStreamException {
            try {
                Method newBuilder = Class.forName(this.messageClassName).getMethod("newBuilder", new Class[0]);
                MessageLite.Builder builder = (MessageLite.Builder) newBuilder.invoke(null, new Object[0]);
                builder.mergeFrom(this.asBytes);
                return builder.buildPartial();
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException("Unable to understand proto buffer", e);
            } catch (ClassNotFoundException e2) {
                throw new RuntimeException("Unable to find proto buffer class", e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException("Unable to call newBuilder method", e3);
            } catch (NoSuchMethodException e4) {
                throw new RuntimeException("Unable to find newBuilder method", e4);
            } catch (InvocationTargetException e5) {
                throw new RuntimeException("Error calling newBuilder", e5.getCause());
            }
        }
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
}
