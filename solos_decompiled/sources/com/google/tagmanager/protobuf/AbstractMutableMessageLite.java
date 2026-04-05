package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.AbstractMessageLite;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.MessageLite;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Collection;

/* JADX INFO: loaded from: classes49.dex */
public abstract class AbstractMutableMessageLite implements MutableMessageLite {
    private boolean isMutable = true;
    protected int cachedSize = -1;

    protected void makeImmutable() {
        this.isMutable = false;
    }

    protected void assertMutable() {
        if (!this.isMutable) {
            throw new IllegalStateException("Try to modify an immutable message.");
        }
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public MessageLite.Builder toBuilder() {
        throw new UnsupportedOperationException("toBuilder() is not supported in mutable messages.");
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public MessageLite.Builder newBuilderForType() {
        throw new UnsupportedOperationException("newBuilderForType() is not supported in mutable messages.");
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public MutableMessageLite mutableCopy() {
        throw new UnsupportedOperationException("mutableCopy() is not supported in mutable messages. Use clone() if you need to make a copy of the mutable message.");
    }

    @Override // 
    /* JADX INFO: renamed from: clone */
    public MutableMessageLite mo6clone() {
        throw new UnsupportedOperationException("clone() should be implemented by subclasses.");
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public ByteString toByteString() {
        try {
            ByteString.CodedBuilder out = ByteString.newCodedBuilder(getSerializedSize());
            writeTo(out.getCodedOutput());
            return out.build();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a ByteString threw an IOException (should never happen).", e);
        }
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public byte[] toByteArray() {
        try {
            byte[] result = new byte[getSerializedSize()];
            CodedOutputStream output = CodedOutputStream.newInstance(result);
            writeTo(output);
            output.checkNoSpaceLeft();
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public void writeTo(OutputStream output) throws IOException {
        int bufferSize = CodedOutputStream.computePreferredBufferSize(getSerializedSize());
        CodedOutputStream codedOutput = CodedOutputStream.newInstance(output, bufferSize);
        writeTo(codedOutput);
        codedOutput.flush();
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public void writeDelimitedTo(OutputStream output) throws IOException {
        int serialized = getSerializedSize();
        int bufferSize = CodedOutputStream.computePreferredBufferSize(CodedOutputStream.computeRawVarint32Size(serialized) + serialized);
        CodedOutputStream codedOutput = CodedOutputStream.newInstance(output, bufferSize);
        codedOutput.writeRawVarint32(serialized);
        writeTo(codedOutput);
        codedOutput.flush();
    }

    UninitializedMessageException newUninitializedMessageException() {
        return new UninitializedMessageException(this);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public final int getCachedSize() {
        return this.cachedSize;
    }

    @Override // com.google.tagmanager.protobuf.MessageLite
    public void writeTo(CodedOutputStream output) throws IOException {
        getSerializedSize();
        writeToWithCachedSizes(output);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(CodedInputStream input) {
        return mergeFrom(input, ExtensionRegistryLite.getEmptyRegistry());
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
        return mergeFrom(input, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(ByteString data) {
        CodedInputStream input = data.newCodedInput();
        return mergeFrom(input) && input.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(ByteString data, ExtensionRegistryLite extensionRegistry) {
        CodedInputStream input = data.newCodedInput();
        return mergeFrom(input, extensionRegistry) && input.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(byte[] data) {
        return mergeFrom(data, 0, data.length);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(byte[] data, int off, int len) {
        CodedInputStream input = CodedInputStream.newInstance(data, off, len);
        return mergeFrom(input) && input.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(byte[] data, ExtensionRegistryLite extensionRegistry) {
        return mergeFrom(data, 0, data.length, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) {
        CodedInputStream input = CodedInputStream.newInstance(data, off, len);
        return mergeFrom(input, extensionRegistry) && input.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(ByteBuffer data) {
        CodedInputStream input = CodedInputStream.newInstance(data);
        return mergeFrom(input) && input.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) {
        CodedInputStream input = CodedInputStream.newInstance(data);
        return mergeFrom(input, extensionRegistry) && input.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(InputStream input) {
        CodedInputStream codedInput = CodedInputStream.newInstance(input);
        return mergeFrom(codedInput) && codedInput.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeFrom(InputStream input, ExtensionRegistryLite extensionRegistry) {
        CodedInputStream codedInput = CodedInputStream.newInstance(input);
        return mergeFrom(codedInput, extensionRegistry) && codedInput.getLastTag() == 0;
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeDelimitedFrom(InputStream input) {
        return mergeDelimitedFrom(input, ExtensionRegistryLite.getEmptyRegistry());
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean mergeDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) {
        try {
            int firstByte = input.read();
            if (firstByte == -1) {
                return false;
            }
            int size = CodedInputStream.readRawVarint32(firstByte, input);
            InputStream limitedInput = new AbstractMessageLite.Builder.LimitedInputStream(input, size);
            return mergeFrom(limitedInput, extensionRegistry);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean parseFrom(CodedInputStream input) {
        clear();
        return mergeFrom(input);
    }

    public boolean parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeFrom(input, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(ByteString data) {
        clear();
        return mergeFrom(data);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeFrom(data, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(byte[] data) {
        clear();
        return mergeFrom(data, 0, data.length);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(byte[] data, int off, int len) {
        clear();
        return mergeFrom(data, off, len);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeFrom(data, 0, data.length, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeFrom(data, off, len, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(ByteBuffer data) {
        clear();
        return mergeFrom(data);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeFrom(data, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(InputStream input) {
        clear();
        return mergeFrom(input);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeFrom(input, extensionRegistry);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseDelimitedFrom(InputStream input) {
        clear();
        return mergeDelimitedFrom(input);
    }

    @Override // com.google.tagmanager.protobuf.MutableMessageLite
    public boolean parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) {
        clear();
        return mergeDelimitedFrom(input, extensionRegistry);
    }

    protected static UninitializedMessageException newUninitializedMessageException(MessageLite message) {
        return new UninitializedMessageException(message);
    }

    protected static <T extends MutableMessageLite> Parser<T> internalNewParserForType(final T t) {
        return new AbstractParser<T>() { // from class: com.google.tagmanager.protobuf.AbstractMutableMessageLite.1
            /* JADX WARN: Incorrect return type in method signature: (Lcom/google/tagmanager/protobuf/CodedInputStream;Lcom/google/tagmanager/protobuf/ExtensionRegistryLite;)TT; */
            @Override // com.google.tagmanager.protobuf.Parser
            public MutableMessageLite parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                MutableMessageLite mutableMessageLiteNewMessageForType = t.newMessageForType();
                if (!mutableMessageLiteNewMessageForType.mergeFrom(input, extensionRegistry)) {
                    throw InvalidProtocolBufferException.parseFailure().setUnfinishedMessage(mutableMessageLiteNewMessageForType);
                }
                return mutableMessageLiteNewMessageForType;
            }
        };
    }

    protected static <T> void addAll(Iterable<T> values, Collection<? super T> list) {
        AbstractMessageLite.Builder.addAll(values, list);
    }

    protected boolean isProto1Group() {
        return false;
    }
}
