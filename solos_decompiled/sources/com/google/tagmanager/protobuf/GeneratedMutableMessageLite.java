package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.GeneratedMutableMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.MessageLite;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes49.dex */
public abstract class GeneratedMutableMessageLite<MessageType extends GeneratedMutableMessageLite<MessageType>> extends AbstractMutableMessageLite implements Serializable {
    private static final long serialVersionUID = 1;
    protected ByteString unknownFields = ByteString.EMPTY;

    @Override // com.google.tagmanager.protobuf.MutableMessageLite, com.google.tagmanager.protobuf.MessageLiteOrBuilder
    public abstract MessageType getDefaultInstanceForType();

    protected abstract MessageLite internalImmutableDefault();

    public abstract MessageType mergeFrom(MessageType messagetype);

    @Override // com.google.tagmanager.protobuf.MessageLite
    public Parser<MessageType> getParserForType() {
        throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public MessageType clear() {
        assertMutable();
        this.unknownFields = ByteString.EMPTY;
        return this;
    }

    protected boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        return input.skipField(tag, unknownFieldsCodedOutput);
    }

    static MessageLite.Builder internalCopyToBuilder(MutableMessageLite fromMessage, MessageLite toMessagePrototype) {
        MessageLite.Builder builder = toMessagePrototype.newBuilderForType();
        try {
            builder.mergeFrom(fromMessage.toByteArray());
            return builder;
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Failed to parse serialized bytes (should not happen)");
        }
    }

    protected static MessageLite internalImmutableDefault(String name) {
        try {
            Method defaultInstanceMethod = GeneratedMessageLite.getMethodOrDie(Class.forName(name), "getDefaultInstance", new Class[0]);
            return (MessageLite) GeneratedMessageLite.invokeOrDie(defaultInstanceMethod, null, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Cannot load the corresponding immutable class. Please add necessary dependencies.");
        }
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public MessageLite immutableCopy() {
        MessageLite immutableDefaultInstance = internalImmutableDefault();
        return this == getDefaultInstanceForType() ? immutableDefaultInstance : internalCopyToBuilder(this, immutableDefaultInstance).buildPartial();
    }

    public static abstract class ExtendableMutableMessage<MessageType extends ExtendableMutableMessage<MessageType>> extends GeneratedMutableMessageLite<MessageType> {
        private FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions = FieldSet.emptySet();

        @Override // com.google.tagmanager.protobuf.GeneratedMutableMessageLite, com.google.tagmanager.protobuf.MutableMessageLite, com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public /* bridge */ /* synthetic */ MessageLite getDefaultInstanceForType() {
            return super.getDefaultInstanceForType();
        }

        protected ExtendableMutableMessage() {
        }

        void internalSetExtensionSet(FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) {
            this.extensions = extensions;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMutableMessageLite, com.google.tagmanager.protobuf.MutableMessageLite
        public MessageType clear() {
            assertMutable();
            this.extensions = FieldSet.emptySet();
            return (MessageType) super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (this.extensions.isImmutable()) {
                this.extensions = this.extensions.m8clone();
            }
        }

        private void verifyExtensionContainingType(GeneratedMessageLite.GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type> boolean hasExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.hasField(generatedExtension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.getRepeatedFieldCount(extension.descriptor);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type> Type getExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Object field = this.extensions.getField(generatedExtension.descriptor);
            if (field == null) {
                return generatedExtension.defaultValue;
            }
            if (generatedExtension.descriptor.isRepeated) {
                return (Type) Collections.unmodifiableList((List) generatedExtension.fromFieldSetType(field));
            }
            return (Type) generatedExtension.fromFieldSetType(field);
        }

        public final <Type> Type getExtension(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return (Type) generatedExtension.singularFromFieldSetType(this.extensions.getRepeatedField(generatedExtension.descriptor, i));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type extends MutableMessageLite> Type getMutableExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> generatedExtension) {
            assertMutable();
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            GeneratedMessageLite.ExtensionDescriptor extensionDescriptor = generatedExtension.descriptor;
            if (extensionDescriptor.getLiteJavaType() != WireFormat.JavaType.MESSAGE) {
                throw new UnsupportedOperationException("getMutableExtension() called on a non-Message type.");
            }
            if (extensionDescriptor.isRepeated()) {
                throw new UnsupportedOperationException("getMutableExtension() called on a repeated type.");
            }
            Object field = this.extensions.getField(generatedExtension.descriptor);
            if (field != null) {
                return (Type) field;
            }
            Type type = (Type) ((MutableMessageLite) generatedExtension.defaultValue).newMessageForType();
            this.extensions.setField(generatedExtension.descriptor, type);
            return type;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final <Type> MessageType setExtension(GeneratedMessageLite.GeneratedExtension<MessageType, Type> generatedExtension, Type value) {
            assertMutable();
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.setField(generatedExtension.descriptor, generatedExtension.toFieldSetType(value));
            return this;
        }

        public final <Type> MessageType setExtension(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension, int index, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.setRepeatedField(extension.descriptor, index, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> MessageType addExtension(GeneratedMessageLite.GeneratedExtension<MessageType, List<Type>> extension, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> MessageType clearExtension(GeneratedMessageLite.GeneratedExtension<MessageType, ?> extension) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.clearField(extension.descriptor);
            return this;
        }

        protected boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMutableMessageLite
        protected boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
            ensureExtensionsIsMutable();
            return GeneratedMutableMessageLite.parseUnknownField(this.extensions, getDefaultInstanceForType(), input, unknownFieldsCodedOutput, extensionRegistry, tag);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMutableMessageLite, com.google.tagmanager.protobuf.MutableMessageLite
        public MessageLite immutableCopy() {
            GeneratedMessageLite.ExtendableBuilder builder = (GeneratedMessageLite.ExtendableBuilder) internalCopyToBuilder(this, internalImmutableDefault());
            builder.internalSetExtensionSet(this.extensions.cloneWithAllFieldsToImmutable());
            return builder.buildPartial();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public class ExtensionWriter {
            private final Iterator<Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object>> iter;
            private final boolean messageSetWireFormat;
            private Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object> next;

            private ExtensionWriter(boolean messageSetWireFormat) {
                this.iter = ExtendableMutableMessage.this.extensions.iterator();
                if (this.iter.hasNext()) {
                    this.next = this.iter.next();
                }
                this.messageSetWireFormat = messageSetWireFormat;
            }

            public void writeUntil(int end, CodedOutputStream output) throws IOException {
                while (this.next != null && this.next.getKey().getNumber() < end) {
                    GeneratedMessageLite.ExtensionDescriptor extension = this.next.getKey();
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

        protected ExtendableMutableMessage<MessageType>.ExtensionWriter newExtensionWriter() {
            return new ExtensionWriter(false);
        }

        protected ExtendableMutableMessage<MessageType>.ExtensionWriter newMessageSetExtensionWriter() {
            return new ExtensionWriter(true);
        }

        protected int extensionsSerializedSize() {
            return this.extensions.getSerializedSize();
        }

        protected int extensionsSerializedSizeAsMessageSet() {
            return this.extensions.getMessageSetSerializedSize();
        }

        protected final void mergeExtensionFields(MessageType other) {
            ensureExtensionsIsMutable();
            this.extensions.mergeFrom(other.extensions);
        }
    }

    static <MessageType extends MutableMessageLite> boolean parseUnknownField(FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions, MessageType defaultInstance, CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        Object objFindValueByNumber;
        int wireType = WireFormat.getTagWireType(tag);
        int fieldNumber = WireFormat.getTagFieldNumber(tag);
        GeneratedMessageLite.GeneratedExtension<MessageType, ?> extension = extensionRegistry.findLiteExtensionByNumber(defaultInstance, fieldNumber);
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
                    extensions.addRepeatedField(extension.descriptor, FieldSet.readPrimitiveFieldForMutable(input, extension.descriptor.getLiteType(), false));
                }
            }
            input.popLimit(limit);
        } else {
            switch (extension.descriptor.getLiteJavaType()) {
                case MESSAGE:
                    MutableMessageLite message = ((MutableMessageLite) extension.messageDefaultInstance).newMessageForType();
                    if (extension.descriptor.getLiteType() == WireFormat.FieldType.GROUP) {
                        input.readGroup(extension.getNumber(), message, extensionRegistry);
                    } else {
                        input.readMessage(message, extensionRegistry);
                    }
                    objFindValueByNumber = message;
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
                    objFindValueByNumber = FieldSet.readPrimitiveFieldForMutable(input, extension.descriptor.getLiteType(), false);
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

    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private byte[] asBytes;
        private String messageClassName;

        SerializedForm(MutableMessageLite regularForm) {
            this.messageClassName = regularForm.getClass().getName();
            this.asBytes = regularForm.toByteArray();
        }

        protected Object readResolve() throws ObjectStreamException {
            try {
                Method newMessage = Class.forName(this.messageClassName).getMethod("newMessage", new Class[0]);
                MutableMessageLite message = (MutableMessageLite) newMessage.invoke(null, new Object[0]);
                if (!message.mergeFrom(CodedInputStream.newInstance(this.asBytes))) {
                    throw new RuntimeException("Unable to understand proto buffer");
                }
                return message;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find proto buffer class", e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException("Unable to call newMessage method", e2);
            } catch (NoSuchMethodException e3) {
                throw new RuntimeException("Unable to find newMessage method", e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Error calling newMessage", e4.getCause());
            }
        }
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
}
