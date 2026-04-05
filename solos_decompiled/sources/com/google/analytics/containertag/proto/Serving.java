package com.google.analytics.containertag.proto;

import android.support.v4.view.MotionEventCompat;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.tagmanager.protobuf.AbstractMessageLite;
import com.google.tagmanager.protobuf.AbstractParser;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.InvalidProtocolBufferException;
import com.google.tagmanager.protobuf.LazyStringArrayList;
import com.google.tagmanager.protobuf.LazyStringList;
import com.google.tagmanager.protobuf.MessageLiteOrBuilder;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import com.google.tagmanager.protobuf.UnmodifiableLazyStringList;
import com.google.tagmanager.protobuf.WireFormat;
import com.kopin.solos.view.graphics.Bar;
import com.nuance.android.vocalizer.VocalizerEngine;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
public final class Serving {

    public interface CacheOptionOrBuilder extends MessageLiteOrBuilder {
        int getExpirationSeconds();

        int getGcacheExpirationSeconds();

        CacheOption.CacheLevel getLevel();

        boolean hasExpirationSeconds();

        boolean hasGcacheExpirationSeconds();

        boolean hasLevel();
    }

    public interface ContainerOrBuilder extends MessageLiteOrBuilder {
        String getContainerId();

        ByteString getContainerIdBytes();

        Resource getJsResource();

        ResourceState getState();

        String getVersion();

        ByteString getVersionBytes();

        boolean hasContainerId();

        boolean hasJsResource();

        boolean hasState();

        boolean hasVersion();
    }

    public interface FunctionCallOrBuilder extends MessageLiteOrBuilder {
        int getFunction();

        boolean getLiveOnly();

        int getName();

        int getProperty(int i);

        int getPropertyCount();

        List<Integer> getPropertyList();

        boolean getServerSide();

        boolean hasFunction();

        boolean hasLiveOnly();

        boolean hasName();

        boolean hasServerSide();
    }

    public interface OptionalResourceOrBuilder extends MessageLiteOrBuilder {
        Resource getResource();

        boolean hasResource();
    }

    public interface PropertyOrBuilder extends MessageLiteOrBuilder {
        int getKey();

        int getValue();

        boolean hasKey();

        boolean hasValue();
    }

    public interface ResourceOrBuilder extends MessageLiteOrBuilder {
        boolean getEnableAutoEventTracking();

        String getKey(int i);

        ByteString getKeyBytes(int i);

        int getKeyCount();

        List<String> getKeyList();

        CacheOption getLiveJsCacheOption();

        FunctionCall getMacro(int i);

        int getMacroCount();

        List<FunctionCall> getMacroList();

        String getMalwareScanAuthCode();

        ByteString getMalwareScanAuthCodeBytes();

        FunctionCall getPredicate(int i);

        int getPredicateCount();

        List<FunctionCall> getPredicateList();

        String getPreviewAuthCode();

        ByteString getPreviewAuthCodeBytes();

        Property getProperty(int i);

        int getPropertyCount();

        List<Property> getPropertyList();

        float getReportingSampleRate();

        int getResourceFormatVersion();

        Rule getRule(int i);

        int getRuleCount();

        List<Rule> getRuleList();

        FunctionCall getTag(int i);

        int getTagCount();

        List<FunctionCall> getTagList();

        String getTemplateVersionSet();

        ByteString getTemplateVersionSetBytes();

        String getUsageContext(int i);

        ByteString getUsageContextBytes(int i);

        int getUsageContextCount();

        List<String> getUsageContextList();

        TypeSystem.Value getValue(int i);

        int getValueCount();

        List<TypeSystem.Value> getValueList();

        String getVersion();

        ByteString getVersionBytes();

        boolean hasEnableAutoEventTracking();

        boolean hasLiveJsCacheOption();

        boolean hasMalwareScanAuthCode();

        boolean hasPreviewAuthCode();

        boolean hasReportingSampleRate();

        boolean hasResourceFormatVersion();

        boolean hasTemplateVersionSet();

        boolean hasVersion();
    }

    public interface RuleOrBuilder extends MessageLiteOrBuilder {
        int getAddMacro(int i);

        int getAddMacroCount();

        List<Integer> getAddMacroList();

        int getAddMacroRuleName(int i);

        int getAddMacroRuleNameCount();

        List<Integer> getAddMacroRuleNameList();

        int getAddTag(int i);

        int getAddTagCount();

        List<Integer> getAddTagList();

        int getAddTagRuleName(int i);

        int getAddTagRuleNameCount();

        List<Integer> getAddTagRuleNameList();

        int getNegativePredicate(int i);

        int getNegativePredicateCount();

        List<Integer> getNegativePredicateList();

        int getPositivePredicate(int i);

        int getPositivePredicateCount();

        List<Integer> getPositivePredicateList();

        int getRemoveMacro(int i);

        int getRemoveMacroCount();

        List<Integer> getRemoveMacroList();

        int getRemoveMacroRuleName(int i);

        int getRemoveMacroRuleNameCount();

        List<Integer> getRemoveMacroRuleNameList();

        int getRemoveTag(int i);

        int getRemoveTagCount();

        List<Integer> getRemoveTagList();

        int getRemoveTagRuleName(int i);

        int getRemoveTagRuleNameCount();

        List<Integer> getRemoveTagRuleNameList();
    }

    public interface ServingValueOrBuilder extends MessageLiteOrBuilder {
        int getListItem(int i);

        int getListItemCount();

        List<Integer> getListItemList();

        int getMacroNameReference();

        int getMacroReference();

        int getMapKey(int i);

        int getMapKeyCount();

        List<Integer> getMapKeyList();

        int getMapValue(int i);

        int getMapValueCount();

        List<Integer> getMapValueList();

        int getTemplateToken(int i);

        int getTemplateTokenCount();

        List<Integer> getTemplateTokenList();

        boolean hasMacroNameReference();

        boolean hasMacroReference();
    }

    private Serving() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
        registry.add(ServingValue.ext);
    }

    public enum ResourceType implements Internal.EnumLite {
        JS_RESOURCE(0, 1),
        NS_RESOURCE(1, 2),
        PIXEL_COLLECTION(2, 3),
        SET_COOKIE(3, 4),
        GET_COOKIE(4, 5),
        CLEAR_CACHE(5, 6),
        RAW_PROTO(6, 7);

        public static final int CLEAR_CACHE_VALUE = 6;
        public static final int GET_COOKIE_VALUE = 5;
        public static final int JS_RESOURCE_VALUE = 1;
        public static final int NS_RESOURCE_VALUE = 2;
        public static final int PIXEL_COLLECTION_VALUE = 3;
        public static final int RAW_PROTO_VALUE = 7;
        public static final int SET_COOKIE_VALUE = 4;
        private static Internal.EnumLiteMap<ResourceType> internalValueMap = new Internal.EnumLiteMap<ResourceType>() { // from class: com.google.analytics.containertag.proto.Serving.ResourceType.1
            @Override // com.google.tagmanager.protobuf.Internal.EnumLiteMap
            public ResourceType findValueByNumber(int number) {
                return ResourceType.valueOf(number);
            }
        };
        private final int value;

        @Override // com.google.tagmanager.protobuf.Internal.EnumLite
        public final int getNumber() {
            return this.value;
        }

        public static ResourceType valueOf(int value) {
            switch (value) {
                case 1:
                    return JS_RESOURCE;
                case 2:
                    return NS_RESOURCE;
                case 3:
                    return PIXEL_COLLECTION;
                case 4:
                    return SET_COOKIE;
                case 5:
                    return GET_COOKIE;
                case 6:
                    return CLEAR_CACHE;
                case 7:
                    return RAW_PROTO;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<ResourceType> internalGetValueMap() {
            return internalValueMap;
        }

        ResourceType(int index, int value) {
            this.value = value;
        }
    }

    public enum ResourceState implements Internal.EnumLite {
        PREVIEW(0, 1),
        LIVE(1, 2);

        public static final int LIVE_VALUE = 2;
        public static final int PREVIEW_VALUE = 1;
        private static Internal.EnumLiteMap<ResourceState> internalValueMap = new Internal.EnumLiteMap<ResourceState>() { // from class: com.google.analytics.containertag.proto.Serving.ResourceState.1
            @Override // com.google.tagmanager.protobuf.Internal.EnumLiteMap
            public ResourceState findValueByNumber(int number) {
                return ResourceState.valueOf(number);
            }
        };
        private final int value;

        @Override // com.google.tagmanager.protobuf.Internal.EnumLite
        public final int getNumber() {
            return this.value;
        }

        public static ResourceState valueOf(int value) {
            switch (value) {
                case 1:
                    return PREVIEW;
                case 2:
                    return LIVE;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<ResourceState> internalGetValueMap() {
            return internalValueMap;
        }

        ResourceState(int index, int value) {
            this.value = value;
        }
    }

    public static final class Container extends GeneratedMessageLite implements ContainerOrBuilder {
        public static final int CONTAINER_ID_FIELD_NUMBER = 3;
        public static final int JS_RESOURCE_FIELD_NUMBER = 1;
        public static final int STATE_FIELD_NUMBER = 4;
        public static final int VERSION_FIELD_NUMBER = 5;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private Object containerId_;
        private Resource jsResource_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private ResourceState state_;
        private final ByteString unknownFields;
        private Object version_;
        public static Parser<Container> PARSER = new AbstractParser<Container>() { // from class: com.google.analytics.containertag.proto.Serving.Container.1
            @Override // com.google.tagmanager.protobuf.Parser
            public Container parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Container(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final Container defaultInstance = new Container(true);

        private Container(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Container(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Container getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public Container getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Container(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 10:
                                Resource.Builder subBuilder = (this.bitField0_ & 1) == 1 ? this.jsResource_.toBuilder() : null;
                                this.jsResource_ = (Resource) input.readMessage(Resource.PARSER, extensionRegistry);
                                if (subBuilder != null) {
                                    subBuilder.mergeFrom(this.jsResource_);
                                    this.jsResource_ = subBuilder.buildPartial();
                                }
                                this.bitField0_ |= 1;
                                break;
                            case 26:
                                ByteString bs = input.readBytes();
                                this.bitField0_ |= 2;
                                this.containerId_ = bs;
                                break;
                            case 32:
                                int rawValue = input.readEnum();
                                ResourceState value = ResourceState.valueOf(rawValue);
                                if (value == null) {
                                    unknownFieldsCodedOutput.writeRawVarint32(tag);
                                    unknownFieldsCodedOutput.writeRawVarint32(rawValue);
                                } else {
                                    this.bitField0_ |= 4;
                                    this.state_ = value;
                                }
                                break;
                            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                                ByteString bs2 = input.readBytes();
                                this.bitField0_ |= 8;
                                this.version_ = bs2;
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        try {
                            unknownFieldsCodedOutput.flush();
                        } catch (IOException e) {
                        } finally {
                        }
                        makeExtensionsImmutable();
                        throw th;
                    }
                } catch (InvalidProtocolBufferException e2) {
                    throw e2.setUnfinishedMessage(this);
                } catch (IOException e3) {
                    throw new InvalidProtocolBufferException(e3.getMessage()).setUnfinishedMessage(this);
                }
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<Container> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public boolean hasJsResource() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public Resource getJsResource() {
            return this.jsResource_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public boolean hasContainerId() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public String getContainerId() {
            Object ref = this.containerId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.containerId_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public ByteString getContainerIdBytes() {
            Object ref = this.containerId_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.containerId_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public boolean hasState() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public ResourceState getState() {
            return this.state_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public boolean hasVersion() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public String getVersion() {
            Object ref = this.version_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.version_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
        public ByteString getVersionBytes() {
            Object ref = this.version_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.version_ = b;
            return b;
        }

        private void initFields() {
            this.jsResource_ = Resource.getDefaultInstance();
            this.containerId_ = "";
            this.state_ = ResourceState.PREVIEW;
            this.version_ = "";
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (!hasJsResource()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            if (!hasContainerId()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            if (!hasState()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            if (!getJsResource().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.jsResource_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(3, getContainerIdBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeEnum(4, this.state_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(5, getVersionBytes());
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeMessageSize(1, this.jsResource_) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(3, getContainerIdBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeEnumSize(4, this.state_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(5, getVersionBytes());
            }
            int size3 = size2 + this.unknownFields.size();
            this.memoizedSerializedSize = size3;
            return size3;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Container)) {
                return super.equals(obj);
            }
            Container other = (Container) obj;
            boolean result = 1 != 0 && hasJsResource() == other.hasJsResource();
            if (hasJsResource()) {
                result = result && getJsResource().equals(other.getJsResource());
            }
            boolean result2 = result && hasContainerId() == other.hasContainerId();
            if (hasContainerId()) {
                result2 = result2 && getContainerId().equals(other.getContainerId());
            }
            boolean result3 = result2 && hasState() == other.hasState();
            if (hasState()) {
                result3 = result3 && getState() == other.getState();
            }
            boolean result4 = result3 && hasVersion() == other.hasVersion();
            if (hasVersion()) {
                result4 = result4 && getVersion().equals(other.getVersion());
            }
            return result4;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Container.class.hashCode() + 779;
            if (hasJsResource()) {
                hash = (((hash * 37) + 1) * 53) + getJsResource().hashCode();
            }
            if (hasContainerId()) {
                hash = (((hash * 37) + 3) * 53) + getContainerId().hashCode();
            }
            if (hasState()) {
                hash = (((hash * 37) + 4) * 53) + Internal.hashEnum(getState());
            }
            if (hasVersion()) {
                hash = (((hash * 37) + 5) * 53) + getVersion().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Container");
            }
            return mutableDefault;
        }

        public static Container parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Container parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Container parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Container parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Container parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Container parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Container parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Container parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Container parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Container parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Container prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Container, Builder> implements ContainerOrBuilder {
            private int bitField0_;
            private Resource jsResource_ = Resource.getDefaultInstance();
            private Object containerId_ = "";
            private ResourceState state_ = ResourceState.PREVIEW;
            private Object version_ = "";

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.jsResource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                this.containerId_ = "";
                this.bitField0_ &= -3;
                this.state_ = ResourceState.PREVIEW;
                this.bitField0_ &= -5;
                this.version_ = "";
                this.bitField0_ &= -9;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public Container getDefaultInstanceForType() {
                return Container.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Container build() {
                Container result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Container buildPartial() {
                Container result = new Container(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.jsResource_ = this.jsResource_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.containerId_ = this.containerId_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                result.state_ = this.state_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 8;
                }
                result.version_ = this.version_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(Container other) {
                if (other != Container.getDefaultInstance()) {
                    if (other.hasJsResource()) {
                        mergeJsResource(other.getJsResource());
                    }
                    if (other.hasContainerId()) {
                        this.bitField0_ |= 2;
                        this.containerId_ = other.containerId_;
                    }
                    if (other.hasState()) {
                        setState(other.getState());
                    }
                    if (other.hasVersion()) {
                        this.bitField0_ |= 8;
                        this.version_ = other.version_;
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return hasJsResource() && hasContainerId() && hasState() && getJsResource().isInitialized();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Container parsedMessage = null;
                try {
                    try {
                        parsedMessage = Container.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public boolean hasJsResource() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public Resource getJsResource() {
                return this.jsResource_;
            }

            public Builder setJsResource(Resource value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.jsResource_ = value;
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setJsResource(Resource.Builder builderForValue) {
                this.jsResource_ = builderForValue.build();
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeJsResource(Resource value) {
                if ((this.bitField0_ & 1) == 1 && this.jsResource_ != Resource.getDefaultInstance()) {
                    this.jsResource_ = Resource.newBuilder(this.jsResource_).mergeFrom(value).buildPartial();
                } else {
                    this.jsResource_ = value;
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearJsResource() {
                this.jsResource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public boolean hasContainerId() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public String getContainerId() {
                Object ref = this.containerId_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.containerId_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public ByteString getContainerIdBytes() {
                Object ref = this.containerId_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.containerId_ = b;
                return b;
            }

            public Builder setContainerId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.containerId_ = value;
                return this;
            }

            public Builder clearContainerId() {
                this.bitField0_ &= -3;
                this.containerId_ = Container.getDefaultInstance().getContainerId();
                return this;
            }

            public Builder setContainerIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.containerId_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public boolean hasState() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public ResourceState getState() {
                return this.state_;
            }

            public Builder setState(ResourceState value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.state_ = value;
                return this;
            }

            public Builder clearState() {
                this.bitField0_ &= -5;
                this.state_ = ResourceState.PREVIEW;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public boolean hasVersion() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public String getVersion() {
                Object ref = this.version_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.version_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ContainerOrBuilder
            public ByteString getVersionBytes() {
                Object ref = this.version_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.version_ = b;
                return b;
            }

            public Builder setVersion(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.version_ = value;
                return this;
            }

            public Builder clearVersion() {
                this.bitField0_ &= -9;
                this.version_ = Container.getDefaultInstance().getVersion();
                return this;
            }

            public Builder setVersionBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.version_ = value;
                return this;
            }
        }
    }

    public static final class ServingValue extends GeneratedMessageLite implements ServingValueOrBuilder {
        public static final int EXT_FIELD_NUMBER = 101;
        public static final int LIST_ITEM_FIELD_NUMBER = 1;
        public static final int MACRO_NAME_REFERENCE_FIELD_NUMBER = 6;
        public static final int MACRO_REFERENCE_FIELD_NUMBER = 4;
        public static final int MAP_KEY_FIELD_NUMBER = 2;
        public static final int MAP_VALUE_FIELD_NUMBER = 3;
        public static final int TEMPLATE_TOKEN_FIELD_NUMBER = 5;
        public static final GeneratedMessageLite.GeneratedExtension<TypeSystem.Value, ServingValue> ext;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private List<Integer> listItem_;
        private int macroNameReference_;
        private int macroReference_;
        private List<Integer> mapKey_;
        private List<Integer> mapValue_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<Integer> templateToken_;
        private final ByteString unknownFields;
        public static Parser<ServingValue> PARSER = new AbstractParser<ServingValue>() { // from class: com.google.analytics.containertag.proto.Serving.ServingValue.1
            @Override // com.google.tagmanager.protobuf.Parser
            public ServingValue parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ServingValue(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final ServingValue defaultInstance = new ServingValue(true);

        private ServingValue(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ServingValue(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ServingValue getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public ServingValue getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ServingValue(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                if ((mutable_bitField0_ & 1) != 1) {
                                    this.listItem_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.listItem_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 10:
                                int length = input.readRawVarint32();
                                int limit = input.pushLimit(length);
                                if ((mutable_bitField0_ & 1) != 1 && input.getBytesUntilLimit() > 0) {
                                    this.listItem_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.listItem_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit);
                                break;
                            case 16:
                                if ((mutable_bitField0_ & 2) != 2) {
                                    this.mapKey_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.mapKey_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 18:
                                int length2 = input.readRawVarint32();
                                int limit2 = input.pushLimit(length2);
                                if ((mutable_bitField0_ & 2) != 2 && input.getBytesUntilLimit() > 0) {
                                    this.mapKey_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.mapKey_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit2);
                                break;
                            case 24:
                                if ((mutable_bitField0_ & 4) != 4) {
                                    this.mapValue_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                this.mapValue_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 26:
                                int length3 = input.readRawVarint32();
                                int limit3 = input.pushLimit(length3);
                                if ((mutable_bitField0_ & 4) != 4 && input.getBytesUntilLimit() > 0) {
                                    this.mapValue_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.mapValue_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit3);
                                break;
                            case 32:
                                this.bitField0_ |= 1;
                                this.macroReference_ = input.readInt32();
                                break;
                            case 40:
                                if ((mutable_bitField0_ & 16) != 16) {
                                    this.templateToken_ = new ArrayList();
                                    mutable_bitField0_ |= 16;
                                }
                                this.templateToken_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                                int length4 = input.readRawVarint32();
                                int limit4 = input.pushLimit(length4);
                                if ((mutable_bitField0_ & 16) != 16 && input.getBytesUntilLimit() > 0) {
                                    this.templateToken_ = new ArrayList();
                                    mutable_bitField0_ |= 16;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.templateToken_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit4);
                                break;
                            case Bar.DEFAULT_HEIGHT /* 48 */:
                                this.bitField0_ |= 2;
                                this.macroNameReference_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        if ((mutable_bitField0_ & 1) == 1) {
                            this.listItem_ = Collections.unmodifiableList(this.listItem_);
                        }
                        if ((mutable_bitField0_ & 2) == 2) {
                            this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                        }
                        if ((mutable_bitField0_ & 4) == 4) {
                            this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                        }
                        if ((mutable_bitField0_ & 16) == 16) {
                            this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
                        }
                        try {
                            unknownFieldsCodedOutput.flush();
                        } catch (IOException e) {
                        } finally {
                        }
                        makeExtensionsImmutable();
                        throw th;
                    }
                } catch (InvalidProtocolBufferException e2) {
                    throw e2.setUnfinishedMessage(this);
                } catch (IOException e3) {
                    throw new InvalidProtocolBufferException(e3.getMessage()).setUnfinishedMessage(this);
                }
            }
            if ((mutable_bitField0_ & 1) == 1) {
                this.listItem_ = Collections.unmodifiableList(this.listItem_);
            }
            if ((mutable_bitField0_ & 2) == 2) {
                this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
            }
            if ((mutable_bitField0_ & 4) == 4) {
                this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
            }
            if ((mutable_bitField0_ & 16) == 16) {
                this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
            ext = GeneratedMessageLite.newSingularGeneratedExtension(TypeSystem.Value.getDefaultInstance(), getDefaultInstance(), getDefaultInstance(), null, 101, WireFormat.FieldType.MESSAGE, ServingValue.class);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<ServingValue> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public List<Integer> getListItemList() {
            return this.listItem_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getListItemCount() {
            return this.listItem_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getListItem(int index) {
            return this.listItem_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public List<Integer> getMapKeyList() {
            return this.mapKey_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getMapKeyCount() {
            return this.mapKey_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getMapKey(int index) {
            return this.mapKey_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public List<Integer> getMapValueList() {
            return this.mapValue_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getMapValueCount() {
            return this.mapValue_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getMapValue(int index) {
            return this.mapValue_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public boolean hasMacroReference() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getMacroReference() {
            return this.macroReference_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public List<Integer> getTemplateTokenList() {
            return this.templateToken_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getTemplateTokenCount() {
            return this.templateToken_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getTemplateToken(int index) {
            return this.templateToken_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public boolean hasMacroNameReference() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
        public int getMacroNameReference() {
            return this.macroNameReference_;
        }

        private void initFields() {
            this.listItem_ = Collections.emptyList();
            this.mapKey_ = Collections.emptyList();
            this.mapValue_ = Collections.emptyList();
            this.macroReference_ = 0;
            this.templateToken_ = Collections.emptyList();
            this.macroNameReference_ = 0;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.listItem_.size(); i++) {
                output.writeInt32(1, this.listItem_.get(i).intValue());
            }
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                output.writeInt32(2, this.mapKey_.get(i2).intValue());
            }
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                output.writeInt32(3, this.mapValue_.get(i3).intValue());
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt32(4, this.macroReference_);
            }
            for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
                output.writeInt32(5, this.templateToken_.get(i4).intValue());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(6, this.macroNameReference_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int dataSize = 0;
            for (int i = 0; i < this.listItem_.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(this.listItem_.get(i).intValue());
            }
            int size2 = 0 + dataSize + (getListItemList().size() * 1);
            int dataSize2 = 0;
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.mapKey_.get(i2).intValue());
            }
            int size3 = size2 + dataSize2 + (getMapKeyList().size() * 1);
            int dataSize3 = 0;
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                dataSize3 += CodedOutputStream.computeInt32SizeNoTag(this.mapValue_.get(i3).intValue());
            }
            int size4 = size3 + dataSize3 + (getMapValueList().size() * 1);
            if ((this.bitField0_ & 1) == 1) {
                size4 += CodedOutputStream.computeInt32Size(4, this.macroReference_);
            }
            int dataSize4 = 0;
            for (int i4 = 0; i4 < this.templateToken_.size(); i4++) {
                dataSize4 += CodedOutputStream.computeInt32SizeNoTag(this.templateToken_.get(i4).intValue());
            }
            int size5 = size4 + dataSize4 + (getTemplateTokenList().size() * 1);
            if ((this.bitField0_ & 2) == 2) {
                size5 += CodedOutputStream.computeInt32Size(6, this.macroNameReference_);
            }
            int size6 = size5 + this.unknownFields.size();
            this.memoizedSerializedSize = size6;
            return size6;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ServingValue)) {
                return super.equals(obj);
            }
            ServingValue other = (ServingValue) obj;
            boolean result = 1 != 0 && getListItemList().equals(other.getListItemList());
            boolean result2 = result && getMapKeyList().equals(other.getMapKeyList());
            boolean result3 = result2 && getMapValueList().equals(other.getMapValueList());
            boolean result4 = result3 && hasMacroReference() == other.hasMacroReference();
            if (hasMacroReference()) {
                result4 = result4 && getMacroReference() == other.getMacroReference();
            }
            boolean result5 = result4 && getTemplateTokenList().equals(other.getTemplateTokenList());
            boolean result6 = result5 && hasMacroNameReference() == other.hasMacroNameReference();
            if (hasMacroNameReference()) {
                result6 = result6 && getMacroNameReference() == other.getMacroNameReference();
            }
            return result6;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ServingValue.class.hashCode() + 779;
            if (getListItemCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getListItemList().hashCode();
            }
            if (getMapKeyCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getMapKeyList().hashCode();
            }
            if (getMapValueCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getMapValueList().hashCode();
            }
            if (hasMacroReference()) {
                hash = (((hash * 37) + 4) * 53) + getMacroReference();
            }
            if (getTemplateTokenCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getTemplateTokenList().hashCode();
            }
            if (hasMacroNameReference()) {
                hash = (((hash * 37) + 6) * 53) + getMacroNameReference();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$ServingValue");
            }
            return mutableDefault;
        }

        public static ServingValue parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServingValue parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServingValue parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ServingValue parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ServingValue parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServingValue parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ServingValue parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ServingValue parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ServingValue parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ServingValue parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ServingValue prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ServingValue, Builder> implements ServingValueOrBuilder {
            private int bitField0_;
            private int macroNameReference_;
            private int macroReference_;
            private List<Integer> listItem_ = Collections.emptyList();
            private List<Integer> mapKey_ = Collections.emptyList();
            private List<Integer> mapValue_ = Collections.emptyList();
            private List<Integer> templateToken_ = Collections.emptyList();

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.macroReference_ = 0;
                this.bitField0_ &= -9;
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.macroNameReference_ = 0;
                this.bitField0_ &= -33;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public ServingValue getDefaultInstanceForType() {
                return ServingValue.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ServingValue build() {
                ServingValue result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ServingValue buildPartial() {
                ServingValue result = new ServingValue(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.listItem_ = Collections.unmodifiableList(this.listItem_);
                    this.bitField0_ &= -2;
                }
                result.listItem_ = this.listItem_;
                if ((this.bitField0_ & 2) == 2) {
                    this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                    this.bitField0_ &= -3;
                }
                result.mapKey_ = this.mapKey_;
                if ((this.bitField0_ & 4) == 4) {
                    this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                    this.bitField0_ &= -5;
                }
                result.mapValue_ = this.mapValue_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ = 0 | 1;
                }
                result.macroReference_ = this.macroReference_;
                if ((this.bitField0_ & 16) == 16) {
                    this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
                    this.bitField0_ &= -17;
                }
                result.templateToken_ = this.templateToken_;
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 2;
                }
                result.macroNameReference_ = this.macroNameReference_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(ServingValue other) {
                if (other != ServingValue.getDefaultInstance()) {
                    if (!other.listItem_.isEmpty()) {
                        if (this.listItem_.isEmpty()) {
                            this.listItem_ = other.listItem_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureListItemIsMutable();
                            this.listItem_.addAll(other.listItem_);
                        }
                    }
                    if (!other.mapKey_.isEmpty()) {
                        if (this.mapKey_.isEmpty()) {
                            this.mapKey_ = other.mapKey_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureMapKeyIsMutable();
                            this.mapKey_.addAll(other.mapKey_);
                        }
                    }
                    if (!other.mapValue_.isEmpty()) {
                        if (this.mapValue_.isEmpty()) {
                            this.mapValue_ = other.mapValue_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureMapValueIsMutable();
                            this.mapValue_.addAll(other.mapValue_);
                        }
                    }
                    if (other.hasMacroReference()) {
                        setMacroReference(other.getMacroReference());
                    }
                    if (!other.templateToken_.isEmpty()) {
                        if (this.templateToken_.isEmpty()) {
                            this.templateToken_ = other.templateToken_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureTemplateTokenIsMutable();
                            this.templateToken_.addAll(other.templateToken_);
                        }
                    }
                    if (other.hasMacroNameReference()) {
                        setMacroNameReference(other.getMacroNameReference());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ServingValue parsedMessage = null;
                try {
                    try {
                        parsedMessage = ServingValue.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            private void ensureListItemIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.listItem_ = new ArrayList(this.listItem_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public List<Integer> getListItemList() {
                return Collections.unmodifiableList(this.listItem_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getListItemCount() {
                return this.listItem_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getListItem(int index) {
                return this.listItem_.get(index).intValue();
            }

            public Builder setListItem(int index, int value) {
                ensureListItemIsMutable();
                this.listItem_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addListItem(int value) {
                ensureListItemIsMutable();
                this.listItem_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllListItem(Iterable<? extends Integer> values) {
                ensureListItemIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.listItem_);
                return this;
            }

            public Builder clearListItem() {
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            private void ensureMapKeyIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.mapKey_ = new ArrayList(this.mapKey_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public List<Integer> getMapKeyList() {
                return Collections.unmodifiableList(this.mapKey_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getMapKeyCount() {
                return this.mapKey_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getMapKey(int index) {
                return this.mapKey_.get(index).intValue();
            }

            public Builder setMapKey(int index, int value) {
                ensureMapKeyIsMutable();
                this.mapKey_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addMapKey(int value) {
                ensureMapKeyIsMutable();
                this.mapKey_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllMapKey(Iterable<? extends Integer> values) {
                ensureMapKeyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapKey_);
                return this;
            }

            public Builder clearMapKey() {
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            private void ensureMapValueIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.mapValue_ = new ArrayList(this.mapValue_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public List<Integer> getMapValueList() {
                return Collections.unmodifiableList(this.mapValue_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getMapValueCount() {
                return this.mapValue_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getMapValue(int index) {
                return this.mapValue_.get(index).intValue();
            }

            public Builder setMapValue(int index, int value) {
                ensureMapValueIsMutable();
                this.mapValue_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addMapValue(int value) {
                ensureMapValueIsMutable();
                this.mapValue_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllMapValue(Iterable<? extends Integer> values) {
                ensureMapValueIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapValue_);
                return this;
            }

            public Builder clearMapValue() {
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public boolean hasMacroReference() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getMacroReference() {
                return this.macroReference_;
            }

            public Builder setMacroReference(int value) {
                this.bitField0_ |= 8;
                this.macroReference_ = value;
                return this;
            }

            public Builder clearMacroReference() {
                this.bitField0_ &= -9;
                this.macroReference_ = 0;
                return this;
            }

            private void ensureTemplateTokenIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.templateToken_ = new ArrayList(this.templateToken_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public List<Integer> getTemplateTokenList() {
                return Collections.unmodifiableList(this.templateToken_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getTemplateTokenCount() {
                return this.templateToken_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getTemplateToken(int index) {
                return this.templateToken_.get(index).intValue();
            }

            public Builder setTemplateToken(int index, int value) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addTemplateToken(int value) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllTemplateToken(Iterable<? extends Integer> values) {
                ensureTemplateTokenIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.templateToken_);
                return this;
            }

            public Builder clearTemplateToken() {
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public boolean hasMacroNameReference() {
                return (this.bitField0_ & 32) == 32;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ServingValueOrBuilder
            public int getMacroNameReference() {
                return this.macroNameReference_;
            }

            public Builder setMacroNameReference(int value) {
                this.bitField0_ |= 32;
                this.macroNameReference_ = value;
                return this;
            }

            public Builder clearMacroNameReference() {
                this.bitField0_ &= -33;
                this.macroNameReference_ = 0;
                return this;
            }
        }
    }

    public static final class Property extends GeneratedMessageLite implements PropertyOrBuilder {
        public static final int KEY_FIELD_NUMBER = 1;
        public static final int VALUE_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private int key_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private final ByteString unknownFields;
        private int value_;
        public static Parser<Property> PARSER = new AbstractParser<Property>() { // from class: com.google.analytics.containertag.proto.Serving.Property.1
            @Override // com.google.tagmanager.protobuf.Parser
            public Property parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Property(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final Property defaultInstance = new Property(true);

        private Property(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Property(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Property getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public Property getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Property(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                this.bitField0_ |= 1;
                                this.key_ = input.readInt32();
                                break;
                            case 16:
                                this.bitField0_ |= 2;
                                this.value_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                    }
                } catch (Throwable th) {
                    try {
                        unknownFieldsCodedOutput.flush();
                    } catch (IOException e3) {
                    } finally {
                    }
                    makeExtensionsImmutable();
                    throw th;
                }
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<Property> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
        public boolean hasKey() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
        public int getKey() {
            return this.key_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
        public boolean hasValue() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
        public int getValue() {
            return this.value_;
        }

        private void initFields() {
            this.key_ = 0;
            this.value_ = 0;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (!hasKey()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            if (!hasValue()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt32(1, this.key_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(2, this.value_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeInt32Size(1, this.key_) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeInt32Size(2, this.value_);
            }
            int size3 = size2 + this.unknownFields.size();
            this.memoizedSerializedSize = size3;
            return size3;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Property)) {
                return super.equals(obj);
            }
            Property other = (Property) obj;
            boolean result = 1 != 0 && hasKey() == other.hasKey();
            if (hasKey()) {
                result = result && getKey() == other.getKey();
            }
            boolean result2 = result && hasValue() == other.hasValue();
            if (hasValue()) {
                result2 = result2 && getValue() == other.getValue();
            }
            return result2;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Property.class.hashCode() + 779;
            if (hasKey()) {
                hash = (((hash * 37) + 1) * 53) + getKey();
            }
            if (hasValue()) {
                hash = (((hash * 37) + 2) * 53) + getValue();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Property");
            }
            return mutableDefault;
        }

        public static Property parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Property parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Property parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Property parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Property parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Property parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Property parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Property parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Property parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Property parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Property prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Property, Builder> implements PropertyOrBuilder {
            private int bitField0_;
            private int key_;
            private int value_;

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.key_ = 0;
                this.bitField0_ &= -2;
                this.value_ = 0;
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public Property getDefaultInstanceForType() {
                return Property.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Property build() {
                Property result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Property buildPartial() {
                Property result = new Property(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.key_ = this.key_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.value_ = this.value_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(Property other) {
                if (other != Property.getDefaultInstance()) {
                    if (other.hasKey()) {
                        setKey(other.getKey());
                    }
                    if (other.hasValue()) {
                        setValue(other.getValue());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return hasKey() && hasValue();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Property parsedMessage = null;
                try {
                    try {
                        parsedMessage = Property.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
            public boolean hasKey() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
            public int getKey() {
                return this.key_;
            }

            public Builder setKey(int value) {
                this.bitField0_ |= 1;
                this.key_ = value;
                return this;
            }

            public Builder clearKey() {
                this.bitField0_ &= -2;
                this.key_ = 0;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
            public boolean hasValue() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Serving.PropertyOrBuilder
            public int getValue() {
                return this.value_;
            }

            public Builder setValue(int value) {
                this.bitField0_ |= 2;
                this.value_ = value;
                return this;
            }

            public Builder clearValue() {
                this.bitField0_ &= -3;
                this.value_ = 0;
                return this;
            }
        }
    }

    public static final class FunctionCall extends GeneratedMessageLite implements FunctionCallOrBuilder {
        public static final int FUNCTION_FIELD_NUMBER = 2;
        public static final int LIVE_ONLY_FIELD_NUMBER = 6;
        public static final int NAME_FIELD_NUMBER = 4;
        public static final int PROPERTY_FIELD_NUMBER = 3;
        public static final int SERVER_SIDE_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private int function_;
        private boolean liveOnly_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private int name_;
        private List<Integer> property_;
        private boolean serverSide_;
        private final ByteString unknownFields;
        public static Parser<FunctionCall> PARSER = new AbstractParser<FunctionCall>() { // from class: com.google.analytics.containertag.proto.Serving.FunctionCall.1
            @Override // com.google.tagmanager.protobuf.Parser
            public FunctionCall parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new FunctionCall(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final FunctionCall defaultInstance = new FunctionCall(true);

        private FunctionCall(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private FunctionCall(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static FunctionCall getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public FunctionCall getDefaultInstanceForType() {
            return defaultInstance;
        }

        private FunctionCall(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                this.bitField0_ |= 8;
                                this.serverSide_ = input.readBool();
                                break;
                            case 16:
                                this.bitField0_ |= 1;
                                this.function_ = input.readInt32();
                                break;
                            case 24:
                                if ((mutable_bitField0_ & 1) != 1) {
                                    this.property_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.property_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 26:
                                int length = input.readRawVarint32();
                                int limit = input.pushLimit(length);
                                if ((mutable_bitField0_ & 1) != 1 && input.getBytesUntilLimit() > 0) {
                                    this.property_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.property_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit);
                                break;
                            case 32:
                                this.bitField0_ |= 2;
                                this.name_ = input.readInt32();
                                break;
                            case Bar.DEFAULT_HEIGHT /* 48 */:
                                this.bitField0_ |= 4;
                                this.liveOnly_ = input.readBool();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        if ((mutable_bitField0_ & 1) == 1) {
                            this.property_ = Collections.unmodifiableList(this.property_);
                        }
                        try {
                            unknownFieldsCodedOutput.flush();
                        } catch (IOException e) {
                        } finally {
                        }
                        makeExtensionsImmutable();
                        throw th;
                    }
                } catch (InvalidProtocolBufferException e2) {
                    throw e2.setUnfinishedMessage(this);
                } catch (IOException e3) {
                    throw new InvalidProtocolBufferException(e3.getMessage()).setUnfinishedMessage(this);
                }
            }
            if ((mutable_bitField0_ & 1) == 1) {
                this.property_ = Collections.unmodifiableList(this.property_);
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<FunctionCall> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public List<Integer> getPropertyList() {
            return this.property_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public int getPropertyCount() {
            return this.property_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public int getProperty(int index) {
            return this.property_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public boolean hasFunction() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public int getFunction() {
            return this.function_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public boolean hasName() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public int getName() {
            return this.name_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public boolean hasLiveOnly() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public boolean getLiveOnly() {
            return this.liveOnly_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public boolean hasServerSide() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
        public boolean getServerSide() {
            return this.serverSide_;
        }

        private void initFields() {
            this.property_ = Collections.emptyList();
            this.function_ = 0;
            this.name_ = 0;
            this.liveOnly_ = false;
            this.serverSide_ = false;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (!hasFunction()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 8) == 8) {
                output.writeBool(1, this.serverSide_);
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt32(2, this.function_);
            }
            for (int i = 0; i < this.property_.size(); i++) {
                output.writeInt32(3, this.property_.get(i).intValue());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(4, this.name_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBool(6, this.liveOnly_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 8) == 8 ? 0 + CodedOutputStream.computeBoolSize(1, this.serverSide_) : 0;
            if ((this.bitField0_ & 1) == 1) {
                size2 += CodedOutputStream.computeInt32Size(2, this.function_);
            }
            int dataSize = 0;
            for (int i = 0; i < this.property_.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(this.property_.get(i).intValue());
            }
            int size3 = size2 + dataSize + (getPropertyList().size() * 1);
            if ((this.bitField0_ & 2) == 2) {
                size3 += CodedOutputStream.computeInt32Size(4, this.name_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size3 += CodedOutputStream.computeBoolSize(6, this.liveOnly_);
            }
            int size4 = size3 + this.unknownFields.size();
            this.memoizedSerializedSize = size4;
            return size4;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FunctionCall)) {
                return super.equals(obj);
            }
            FunctionCall other = (FunctionCall) obj;
            boolean result = (1 != 0 && getPropertyList().equals(other.getPropertyList())) && hasFunction() == other.hasFunction();
            if (hasFunction()) {
                result = result && getFunction() == other.getFunction();
            }
            boolean result2 = result && hasName() == other.hasName();
            if (hasName()) {
                result2 = result2 && getName() == other.getName();
            }
            boolean result3 = result2 && hasLiveOnly() == other.hasLiveOnly();
            if (hasLiveOnly()) {
                result3 = result3 && getLiveOnly() == other.getLiveOnly();
            }
            boolean result4 = result3 && hasServerSide() == other.hasServerSide();
            if (hasServerSide()) {
                result4 = result4 && getServerSide() == other.getServerSide();
            }
            return result4;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = FunctionCall.class.hashCode() + 779;
            if (getPropertyCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getPropertyList().hashCode();
            }
            if (hasFunction()) {
                hash = (((hash * 37) + 2) * 53) + getFunction();
            }
            if (hasName()) {
                hash = (((hash * 37) + 4) * 53) + getName();
            }
            if (hasLiveOnly()) {
                hash = (((hash * 37) + 6) * 53) + Internal.hashBoolean(getLiveOnly());
            }
            if (hasServerSide()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashBoolean(getServerSide());
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$FunctionCall");
            }
            return mutableDefault;
        }

        public static FunctionCall parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FunctionCall parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FunctionCall parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FunctionCall parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FunctionCall parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static FunctionCall parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static FunctionCall parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static FunctionCall parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static FunctionCall parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static FunctionCall parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(FunctionCall prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<FunctionCall, Builder> implements FunctionCallOrBuilder {
            private int bitField0_;
            private int function_;
            private boolean liveOnly_;
            private int name_;
            private List<Integer> property_ = Collections.emptyList();
            private boolean serverSide_;

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.function_ = 0;
                this.bitField0_ &= -3;
                this.name_ = 0;
                this.bitField0_ &= -5;
                this.liveOnly_ = false;
                this.bitField0_ &= -9;
                this.serverSide_ = false;
                this.bitField0_ &= -17;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public FunctionCall getDefaultInstanceForType() {
                return FunctionCall.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public FunctionCall build() {
                FunctionCall result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public FunctionCall buildPartial() {
                FunctionCall result = new FunctionCall(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.property_ = Collections.unmodifiableList(this.property_);
                    this.bitField0_ &= -2;
                }
                result.property_ = this.property_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ = 0 | 1;
                }
                result.function_ = this.function_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 2;
                }
                result.name_ = this.name_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 4;
                }
                result.liveOnly_ = this.liveOnly_;
                if ((from_bitField0_ & 16) == 16) {
                    to_bitField0_ |= 8;
                }
                result.serverSide_ = this.serverSide_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(FunctionCall other) {
                if (other != FunctionCall.getDefaultInstance()) {
                    if (!other.property_.isEmpty()) {
                        if (this.property_.isEmpty()) {
                            this.property_ = other.property_;
                            this.bitField0_ &= -2;
                        } else {
                            ensurePropertyIsMutable();
                            this.property_.addAll(other.property_);
                        }
                    }
                    if (other.hasFunction()) {
                        setFunction(other.getFunction());
                    }
                    if (other.hasName()) {
                        setName(other.getName());
                    }
                    if (other.hasLiveOnly()) {
                        setLiveOnly(other.getLiveOnly());
                    }
                    if (other.hasServerSide()) {
                        setServerSide(other.getServerSide());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return hasFunction();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                FunctionCall parsedMessage = null;
                try {
                    try {
                        parsedMessage = FunctionCall.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            private void ensurePropertyIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.property_ = new ArrayList(this.property_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public List<Integer> getPropertyList() {
                return Collections.unmodifiableList(this.property_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public int getPropertyCount() {
                return this.property_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public int getProperty(int index) {
                return this.property_.get(index).intValue();
            }

            public Builder setProperty(int index, int value) {
                ensurePropertyIsMutable();
                this.property_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addProperty(int value) {
                ensurePropertyIsMutable();
                this.property_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllProperty(Iterable<? extends Integer> values) {
                ensurePropertyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.property_);
                return this;
            }

            public Builder clearProperty() {
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public boolean hasFunction() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public int getFunction() {
                return this.function_;
            }

            public Builder setFunction(int value) {
                this.bitField0_ |= 2;
                this.function_ = value;
                return this;
            }

            public Builder clearFunction() {
                this.bitField0_ &= -3;
                this.function_ = 0;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public boolean hasName() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public int getName() {
                return this.name_;
            }

            public Builder setName(int value) {
                this.bitField0_ |= 4;
                this.name_ = value;
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -5;
                this.name_ = 0;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public boolean hasLiveOnly() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public boolean getLiveOnly() {
                return this.liveOnly_;
            }

            public Builder setLiveOnly(boolean value) {
                this.bitField0_ |= 8;
                this.liveOnly_ = value;
                return this;
            }

            public Builder clearLiveOnly() {
                this.bitField0_ &= -9;
                this.liveOnly_ = false;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public boolean hasServerSide() {
                return (this.bitField0_ & 16) == 16;
            }

            @Override // com.google.analytics.containertag.proto.Serving.FunctionCallOrBuilder
            public boolean getServerSide() {
                return this.serverSide_;
            }

            public Builder setServerSide(boolean value) {
                this.bitField0_ |= 16;
                this.serverSide_ = value;
                return this;
            }

            public Builder clearServerSide() {
                this.bitField0_ &= -17;
                this.serverSide_ = false;
                return this;
            }
        }
    }

    public static final class Rule extends GeneratedMessageLite implements RuleOrBuilder {
        public static final int ADD_MACRO_FIELD_NUMBER = 7;
        public static final int ADD_MACRO_RULE_NAME_FIELD_NUMBER = 9;
        public static final int ADD_TAG_FIELD_NUMBER = 3;
        public static final int ADD_TAG_RULE_NAME_FIELD_NUMBER = 5;
        public static final int NEGATIVE_PREDICATE_FIELD_NUMBER = 2;
        public static final int POSITIVE_PREDICATE_FIELD_NUMBER = 1;
        public static final int REMOVE_MACRO_FIELD_NUMBER = 8;
        public static final int REMOVE_MACRO_RULE_NAME_FIELD_NUMBER = 10;
        public static final int REMOVE_TAG_FIELD_NUMBER = 4;
        public static final int REMOVE_TAG_RULE_NAME_FIELD_NUMBER = 6;
        private static final long serialVersionUID = 0;
        private List<Integer> addMacroRuleName_;
        private List<Integer> addMacro_;
        private List<Integer> addTagRuleName_;
        private List<Integer> addTag_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<Integer> negativePredicate_;
        private List<Integer> positivePredicate_;
        private List<Integer> removeMacroRuleName_;
        private List<Integer> removeMacro_;
        private List<Integer> removeTagRuleName_;
        private List<Integer> removeTag_;
        private final ByteString unknownFields;
        public static Parser<Rule> PARSER = new AbstractParser<Rule>() { // from class: com.google.analytics.containertag.proto.Serving.Rule.1
            @Override // com.google.tagmanager.protobuf.Parser
            public Rule parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Rule(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final Rule defaultInstance = new Rule(true);

        private Rule(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Rule(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Rule getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public Rule getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Rule(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int mutable_bitField0_ = 0;
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                if ((mutable_bitField0_ & 1) != 1) {
                                    this.positivePredicate_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.positivePredicate_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 10:
                                int length = input.readRawVarint32();
                                int limit = input.pushLimit(length);
                                if ((mutable_bitField0_ & 1) != 1 && input.getBytesUntilLimit() > 0) {
                                    this.positivePredicate_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.positivePredicate_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit);
                                break;
                            case 16:
                                if ((mutable_bitField0_ & 2) != 2) {
                                    this.negativePredicate_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.negativePredicate_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 18:
                                int length2 = input.readRawVarint32();
                                int limit2 = input.pushLimit(length2);
                                if ((mutable_bitField0_ & 2) != 2 && input.getBytesUntilLimit() > 0) {
                                    this.negativePredicate_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.negativePredicate_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit2);
                                break;
                            case 24:
                                if ((mutable_bitField0_ & 4) != 4) {
                                    this.addTag_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                this.addTag_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 26:
                                int length3 = input.readRawVarint32();
                                int limit3 = input.pushLimit(length3);
                                if ((mutable_bitField0_ & 4) != 4 && input.getBytesUntilLimit() > 0) {
                                    this.addTag_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.addTag_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit3);
                                break;
                            case 32:
                                if ((mutable_bitField0_ & 8) != 8) {
                                    this.removeTag_ = new ArrayList();
                                    mutable_bitField0_ |= 8;
                                }
                                this.removeTag_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 34:
                                int length4 = input.readRawVarint32();
                                int limit4 = input.pushLimit(length4);
                                if ((mutable_bitField0_ & 8) != 8 && input.getBytesUntilLimit() > 0) {
                                    this.removeTag_ = new ArrayList();
                                    mutable_bitField0_ |= 8;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.removeTag_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit4);
                                break;
                            case 40:
                                if ((mutable_bitField0_ & 16) != 16) {
                                    this.addTagRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 16;
                                }
                                this.addTagRuleName_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                                int length5 = input.readRawVarint32();
                                int limit5 = input.pushLimit(length5);
                                if ((mutable_bitField0_ & 16) != 16 && input.getBytesUntilLimit() > 0) {
                                    this.addTagRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 16;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.addTagRuleName_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit5);
                                break;
                            case Bar.DEFAULT_HEIGHT /* 48 */:
                                if ((mutable_bitField0_ & 32) != 32) {
                                    this.removeTagRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 32;
                                }
                                this.removeTagRuleName_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 50:
                                int length6 = input.readRawVarint32();
                                int limit6 = input.pushLimit(length6);
                                if ((mutable_bitField0_ & 32) != 32 && input.getBytesUntilLimit() > 0) {
                                    this.removeTagRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 32;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.removeTagRuleName_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit6);
                                break;
                            case 56:
                                if ((mutable_bitField0_ & 64) != 64) {
                                    this.addMacro_ = new ArrayList();
                                    mutable_bitField0_ |= 64;
                                }
                                this.addMacro_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 58:
                                int length7 = input.readRawVarint32();
                                int limit7 = input.pushLimit(length7);
                                if ((mutable_bitField0_ & 64) != 64 && input.getBytesUntilLimit() > 0) {
                                    this.addMacro_ = new ArrayList();
                                    mutable_bitField0_ |= 64;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.addMacro_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit7);
                                break;
                            case 64:
                                if ((mutable_bitField0_ & 128) != 128) {
                                    this.removeMacro_ = new ArrayList();
                                    mutable_bitField0_ |= 128;
                                }
                                this.removeMacro_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 66:
                                int length8 = input.readRawVarint32();
                                int limit8 = input.pushLimit(length8);
                                if ((mutable_bitField0_ & 128) != 128 && input.getBytesUntilLimit() > 0) {
                                    this.removeMacro_ = new ArrayList();
                                    mutable_bitField0_ |= 128;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.removeMacro_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit8);
                                break;
                            case 72:
                                if ((mutable_bitField0_ & 256) != 256) {
                                    this.addMacroRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 256;
                                }
                                this.addMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 74:
                                int length9 = input.readRawVarint32();
                                int limit9 = input.pushLimit(length9);
                                if ((mutable_bitField0_ & 256) != 256 && input.getBytesUntilLimit() > 0) {
                                    this.addMacroRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 256;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.addMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit9);
                                break;
                            case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                                if ((mutable_bitField0_ & 512) != 512) {
                                    this.removeMacroRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 512;
                                }
                                this.removeMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 82:
                                int length10 = input.readRawVarint32();
                                int limit10 = input.pushLimit(length10);
                                if ((mutable_bitField0_ & 512) != 512 && input.getBytesUntilLimit() > 0) {
                                    this.removeMacroRuleName_ = new ArrayList();
                                    mutable_bitField0_ |= 512;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.removeMacroRuleName_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit10);
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        if ((mutable_bitField0_ & 1) == 1) {
                            this.positivePredicate_ = Collections.unmodifiableList(this.positivePredicate_);
                        }
                        if ((mutable_bitField0_ & 2) == 2) {
                            this.negativePredicate_ = Collections.unmodifiableList(this.negativePredicate_);
                        }
                        if ((mutable_bitField0_ & 4) == 4) {
                            this.addTag_ = Collections.unmodifiableList(this.addTag_);
                        }
                        if ((mutable_bitField0_ & 8) == 8) {
                            this.removeTag_ = Collections.unmodifiableList(this.removeTag_);
                        }
                        if ((mutable_bitField0_ & 16) == 16) {
                            this.addTagRuleName_ = Collections.unmodifiableList(this.addTagRuleName_);
                        }
                        if ((mutable_bitField0_ & 32) == 32) {
                            this.removeTagRuleName_ = Collections.unmodifiableList(this.removeTagRuleName_);
                        }
                        if ((mutable_bitField0_ & 64) == 64) {
                            this.addMacro_ = Collections.unmodifiableList(this.addMacro_);
                        }
                        if ((mutable_bitField0_ & 128) == 128) {
                            this.removeMacro_ = Collections.unmodifiableList(this.removeMacro_);
                        }
                        if ((mutable_bitField0_ & 256) == 256) {
                            this.addMacroRuleName_ = Collections.unmodifiableList(this.addMacroRuleName_);
                        }
                        if ((mutable_bitField0_ & 512) == 512) {
                            this.removeMacroRuleName_ = Collections.unmodifiableList(this.removeMacroRuleName_);
                        }
                        try {
                            unknownFieldsCodedOutput.flush();
                        } catch (IOException e) {
                        } finally {
                        }
                        makeExtensionsImmutable();
                        throw th;
                    }
                } catch (InvalidProtocolBufferException e2) {
                    throw e2.setUnfinishedMessage(this);
                } catch (IOException e3) {
                    throw new InvalidProtocolBufferException(e3.getMessage()).setUnfinishedMessage(this);
                }
            }
            if ((mutable_bitField0_ & 1) == 1) {
                this.positivePredicate_ = Collections.unmodifiableList(this.positivePredicate_);
            }
            if ((mutable_bitField0_ & 2) == 2) {
                this.negativePredicate_ = Collections.unmodifiableList(this.negativePredicate_);
            }
            if ((mutable_bitField0_ & 4) == 4) {
                this.addTag_ = Collections.unmodifiableList(this.addTag_);
            }
            if ((mutable_bitField0_ & 8) == 8) {
                this.removeTag_ = Collections.unmodifiableList(this.removeTag_);
            }
            if ((mutable_bitField0_ & 16) == 16) {
                this.addTagRuleName_ = Collections.unmodifiableList(this.addTagRuleName_);
            }
            if ((mutable_bitField0_ & 32) == 32) {
                this.removeTagRuleName_ = Collections.unmodifiableList(this.removeTagRuleName_);
            }
            if ((mutable_bitField0_ & 64) == 64) {
                this.addMacro_ = Collections.unmodifiableList(this.addMacro_);
            }
            if ((mutable_bitField0_ & 128) == 128) {
                this.removeMacro_ = Collections.unmodifiableList(this.removeMacro_);
            }
            if ((mutable_bitField0_ & 256) == 256) {
                this.addMacroRuleName_ = Collections.unmodifiableList(this.addMacroRuleName_);
            }
            if ((mutable_bitField0_ & 512) == 512) {
                this.removeMacroRuleName_ = Collections.unmodifiableList(this.removeMacroRuleName_);
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<Rule> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getPositivePredicateList() {
            return this.positivePredicate_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getPositivePredicateCount() {
            return this.positivePredicate_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getPositivePredicate(int index) {
            return this.positivePredicate_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getNegativePredicateList() {
            return this.negativePredicate_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getNegativePredicateCount() {
            return this.negativePredicate_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getNegativePredicate(int index) {
            return this.negativePredicate_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getAddTagList() {
            return this.addTag_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddTagCount() {
            return this.addTag_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddTag(int index) {
            return this.addTag_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getRemoveTagList() {
            return this.removeTag_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveTagCount() {
            return this.removeTag_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveTag(int index) {
            return this.removeTag_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getAddTagRuleNameList() {
            return this.addTagRuleName_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddTagRuleNameCount() {
            return this.addTagRuleName_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddTagRuleName(int index) {
            return this.addTagRuleName_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getRemoveTagRuleNameList() {
            return this.removeTagRuleName_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveTagRuleNameCount() {
            return this.removeTagRuleName_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveTagRuleName(int index) {
            return this.removeTagRuleName_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getAddMacroList() {
            return this.addMacro_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddMacroCount() {
            return this.addMacro_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddMacro(int index) {
            return this.addMacro_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getRemoveMacroList() {
            return this.removeMacro_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveMacroCount() {
            return this.removeMacro_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveMacro(int index) {
            return this.removeMacro_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getAddMacroRuleNameList() {
            return this.addMacroRuleName_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddMacroRuleNameCount() {
            return this.addMacroRuleName_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getAddMacroRuleName(int index) {
            return this.addMacroRuleName_.get(index).intValue();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public List<Integer> getRemoveMacroRuleNameList() {
            return this.removeMacroRuleName_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveMacroRuleNameCount() {
            return this.removeMacroRuleName_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
        public int getRemoveMacroRuleName(int index) {
            return this.removeMacroRuleName_.get(index).intValue();
        }

        private void initFields() {
            this.positivePredicate_ = Collections.emptyList();
            this.negativePredicate_ = Collections.emptyList();
            this.addTag_ = Collections.emptyList();
            this.removeTag_ = Collections.emptyList();
            this.addTagRuleName_ = Collections.emptyList();
            this.removeTagRuleName_ = Collections.emptyList();
            this.addMacro_ = Collections.emptyList();
            this.removeMacro_ = Collections.emptyList();
            this.addMacroRuleName_ = Collections.emptyList();
            this.removeMacroRuleName_ = Collections.emptyList();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.positivePredicate_.size(); i++) {
                output.writeInt32(1, this.positivePredicate_.get(i).intValue());
            }
            for (int i2 = 0; i2 < this.negativePredicate_.size(); i2++) {
                output.writeInt32(2, this.negativePredicate_.get(i2).intValue());
            }
            for (int i3 = 0; i3 < this.addTag_.size(); i3++) {
                output.writeInt32(3, this.addTag_.get(i3).intValue());
            }
            for (int i4 = 0; i4 < this.removeTag_.size(); i4++) {
                output.writeInt32(4, this.removeTag_.get(i4).intValue());
            }
            for (int i5 = 0; i5 < this.addTagRuleName_.size(); i5++) {
                output.writeInt32(5, this.addTagRuleName_.get(i5).intValue());
            }
            for (int i6 = 0; i6 < this.removeTagRuleName_.size(); i6++) {
                output.writeInt32(6, this.removeTagRuleName_.get(i6).intValue());
            }
            for (int i7 = 0; i7 < this.addMacro_.size(); i7++) {
                output.writeInt32(7, this.addMacro_.get(i7).intValue());
            }
            for (int i8 = 0; i8 < this.removeMacro_.size(); i8++) {
                output.writeInt32(8, this.removeMacro_.get(i8).intValue());
            }
            for (int i9 = 0; i9 < this.addMacroRuleName_.size(); i9++) {
                output.writeInt32(9, this.addMacroRuleName_.get(i9).intValue());
            }
            for (int i10 = 0; i10 < this.removeMacroRuleName_.size(); i10++) {
                output.writeInt32(10, this.removeMacroRuleName_.get(i10).intValue());
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int dataSize = 0;
            for (int i = 0; i < this.positivePredicate_.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(this.positivePredicate_.get(i).intValue());
            }
            int size2 = 0 + dataSize + (getPositivePredicateList().size() * 1);
            int dataSize2 = 0;
            for (int i2 = 0; i2 < this.negativePredicate_.size(); i2++) {
                dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.negativePredicate_.get(i2).intValue());
            }
            int size3 = size2 + dataSize2 + (getNegativePredicateList().size() * 1);
            int dataSize3 = 0;
            for (int i3 = 0; i3 < this.addTag_.size(); i3++) {
                dataSize3 += CodedOutputStream.computeInt32SizeNoTag(this.addTag_.get(i3).intValue());
            }
            int size4 = size3 + dataSize3 + (getAddTagList().size() * 1);
            int dataSize4 = 0;
            for (int i4 = 0; i4 < this.removeTag_.size(); i4++) {
                dataSize4 += CodedOutputStream.computeInt32SizeNoTag(this.removeTag_.get(i4).intValue());
            }
            int size5 = size4 + dataSize4 + (getRemoveTagList().size() * 1);
            int dataSize5 = 0;
            for (int i5 = 0; i5 < this.addTagRuleName_.size(); i5++) {
                dataSize5 += CodedOutputStream.computeInt32SizeNoTag(this.addTagRuleName_.get(i5).intValue());
            }
            int size6 = size5 + dataSize5 + (getAddTagRuleNameList().size() * 1);
            int dataSize6 = 0;
            for (int i6 = 0; i6 < this.removeTagRuleName_.size(); i6++) {
                dataSize6 += CodedOutputStream.computeInt32SizeNoTag(this.removeTagRuleName_.get(i6).intValue());
            }
            int size7 = size6 + dataSize6 + (getRemoveTagRuleNameList().size() * 1);
            int dataSize7 = 0;
            for (int i7 = 0; i7 < this.addMacro_.size(); i7++) {
                dataSize7 += CodedOutputStream.computeInt32SizeNoTag(this.addMacro_.get(i7).intValue());
            }
            int size8 = size7 + dataSize7 + (getAddMacroList().size() * 1);
            int dataSize8 = 0;
            for (int i8 = 0; i8 < this.removeMacro_.size(); i8++) {
                dataSize8 += CodedOutputStream.computeInt32SizeNoTag(this.removeMacro_.get(i8).intValue());
            }
            int size9 = size8 + dataSize8 + (getRemoveMacroList().size() * 1);
            int dataSize9 = 0;
            for (int i9 = 0; i9 < this.addMacroRuleName_.size(); i9++) {
                dataSize9 += CodedOutputStream.computeInt32SizeNoTag(this.addMacroRuleName_.get(i9).intValue());
            }
            int size10 = size9 + dataSize9 + (getAddMacroRuleNameList().size() * 1);
            int dataSize10 = 0;
            for (int i10 = 0; i10 < this.removeMacroRuleName_.size(); i10++) {
                dataSize10 += CodedOutputStream.computeInt32SizeNoTag(this.removeMacroRuleName_.get(i10).intValue());
            }
            int size11 = size10 + dataSize10 + (getRemoveMacroRuleNameList().size() * 1) + this.unknownFields.size();
            this.memoizedSerializedSize = size11;
            return size11;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Rule)) {
                return super.equals(obj);
            }
            Rule other = (Rule) obj;
            boolean result = 1 != 0 && getPositivePredicateList().equals(other.getPositivePredicateList());
            boolean result2 = result && getNegativePredicateList().equals(other.getNegativePredicateList());
            boolean result3 = result2 && getAddTagList().equals(other.getAddTagList());
            boolean result4 = result3 && getRemoveTagList().equals(other.getRemoveTagList());
            boolean result5 = result4 && getAddTagRuleNameList().equals(other.getAddTagRuleNameList());
            boolean result6 = result5 && getRemoveTagRuleNameList().equals(other.getRemoveTagRuleNameList());
            boolean result7 = result6 && getAddMacroList().equals(other.getAddMacroList());
            boolean result8 = result7 && getRemoveMacroList().equals(other.getRemoveMacroList());
            boolean result9 = result8 && getAddMacroRuleNameList().equals(other.getAddMacroRuleNameList());
            boolean result10 = result9 && getRemoveMacroRuleNameList().equals(other.getRemoveMacroRuleNameList());
            return result10;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Rule.class.hashCode() + 779;
            if (getPositivePredicateCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getPositivePredicateList().hashCode();
            }
            if (getNegativePredicateCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getNegativePredicateList().hashCode();
            }
            if (getAddTagCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getAddTagList().hashCode();
            }
            if (getRemoveTagCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getRemoveTagList().hashCode();
            }
            if (getAddTagRuleNameCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getAddTagRuleNameList().hashCode();
            }
            if (getRemoveTagRuleNameCount() > 0) {
                hash = (((hash * 37) + 6) * 53) + getRemoveTagRuleNameList().hashCode();
            }
            if (getAddMacroCount() > 0) {
                hash = (((hash * 37) + 7) * 53) + getAddMacroList().hashCode();
            }
            if (getRemoveMacroCount() > 0) {
                hash = (((hash * 37) + 8) * 53) + getRemoveMacroList().hashCode();
            }
            if (getAddMacroRuleNameCount() > 0) {
                hash = (((hash * 37) + 9) * 53) + getAddMacroRuleNameList().hashCode();
            }
            if (getRemoveMacroRuleNameCount() > 0) {
                hash = (((hash * 37) + 10) * 53) + getRemoveMacroRuleNameList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Rule");
            }
            return mutableDefault;
        }

        public static Rule parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Rule parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Rule parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Rule parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Rule parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Rule parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Rule parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Rule parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Rule parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Rule parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Rule prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Rule, Builder> implements RuleOrBuilder {
            private int bitField0_;
            private List<Integer> positivePredicate_ = Collections.emptyList();
            private List<Integer> negativePredicate_ = Collections.emptyList();
            private List<Integer> addTag_ = Collections.emptyList();
            private List<Integer> removeTag_ = Collections.emptyList();
            private List<Integer> addTagRuleName_ = Collections.emptyList();
            private List<Integer> removeTagRuleName_ = Collections.emptyList();
            private List<Integer> addMacro_ = Collections.emptyList();
            private List<Integer> removeMacro_ = Collections.emptyList();
            private List<Integer> addMacroRuleName_ = Collections.emptyList();
            private List<Integer> removeMacroRuleName_ = Collections.emptyList();

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.positivePredicate_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.negativePredicate_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.addTag_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.removeTag_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.addTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.removeTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -33;
                this.addMacro_ = Collections.emptyList();
                this.bitField0_ &= -65;
                this.removeMacro_ = Collections.emptyList();
                this.bitField0_ &= -129;
                this.addMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -257;
                this.removeMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -513;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public Rule getDefaultInstanceForType() {
                return Rule.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Rule build() {
                Rule result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Rule buildPartial() {
                Rule result = new Rule(this);
                int i = this.bitField0_;
                if ((this.bitField0_ & 1) == 1) {
                    this.positivePredicate_ = Collections.unmodifiableList(this.positivePredicate_);
                    this.bitField0_ &= -2;
                }
                result.positivePredicate_ = this.positivePredicate_;
                if ((this.bitField0_ & 2) == 2) {
                    this.negativePredicate_ = Collections.unmodifiableList(this.negativePredicate_);
                    this.bitField0_ &= -3;
                }
                result.negativePredicate_ = this.negativePredicate_;
                if ((this.bitField0_ & 4) == 4) {
                    this.addTag_ = Collections.unmodifiableList(this.addTag_);
                    this.bitField0_ &= -5;
                }
                result.addTag_ = this.addTag_;
                if ((this.bitField0_ & 8) == 8) {
                    this.removeTag_ = Collections.unmodifiableList(this.removeTag_);
                    this.bitField0_ &= -9;
                }
                result.removeTag_ = this.removeTag_;
                if ((this.bitField0_ & 16) == 16) {
                    this.addTagRuleName_ = Collections.unmodifiableList(this.addTagRuleName_);
                    this.bitField0_ &= -17;
                }
                result.addTagRuleName_ = this.addTagRuleName_;
                if ((this.bitField0_ & 32) == 32) {
                    this.removeTagRuleName_ = Collections.unmodifiableList(this.removeTagRuleName_);
                    this.bitField0_ &= -33;
                }
                result.removeTagRuleName_ = this.removeTagRuleName_;
                if ((this.bitField0_ & 64) == 64) {
                    this.addMacro_ = Collections.unmodifiableList(this.addMacro_);
                    this.bitField0_ &= -65;
                }
                result.addMacro_ = this.addMacro_;
                if ((this.bitField0_ & 128) == 128) {
                    this.removeMacro_ = Collections.unmodifiableList(this.removeMacro_);
                    this.bitField0_ &= -129;
                }
                result.removeMacro_ = this.removeMacro_;
                if ((this.bitField0_ & 256) == 256) {
                    this.addMacroRuleName_ = Collections.unmodifiableList(this.addMacroRuleName_);
                    this.bitField0_ &= -257;
                }
                result.addMacroRuleName_ = this.addMacroRuleName_;
                if ((this.bitField0_ & 512) == 512) {
                    this.removeMacroRuleName_ = Collections.unmodifiableList(this.removeMacroRuleName_);
                    this.bitField0_ &= -513;
                }
                result.removeMacroRuleName_ = this.removeMacroRuleName_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(Rule other) {
                if (other != Rule.getDefaultInstance()) {
                    if (!other.positivePredicate_.isEmpty()) {
                        if (this.positivePredicate_.isEmpty()) {
                            this.positivePredicate_ = other.positivePredicate_;
                            this.bitField0_ &= -2;
                        } else {
                            ensurePositivePredicateIsMutable();
                            this.positivePredicate_.addAll(other.positivePredicate_);
                        }
                    }
                    if (!other.negativePredicate_.isEmpty()) {
                        if (this.negativePredicate_.isEmpty()) {
                            this.negativePredicate_ = other.negativePredicate_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureNegativePredicateIsMutable();
                            this.negativePredicate_.addAll(other.negativePredicate_);
                        }
                    }
                    if (!other.addTag_.isEmpty()) {
                        if (this.addTag_.isEmpty()) {
                            this.addTag_ = other.addTag_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureAddTagIsMutable();
                            this.addTag_.addAll(other.addTag_);
                        }
                    }
                    if (!other.removeTag_.isEmpty()) {
                        if (this.removeTag_.isEmpty()) {
                            this.removeTag_ = other.removeTag_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureRemoveTagIsMutable();
                            this.removeTag_.addAll(other.removeTag_);
                        }
                    }
                    if (!other.addTagRuleName_.isEmpty()) {
                        if (this.addTagRuleName_.isEmpty()) {
                            this.addTagRuleName_ = other.addTagRuleName_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureAddTagRuleNameIsMutable();
                            this.addTagRuleName_.addAll(other.addTagRuleName_);
                        }
                    }
                    if (!other.removeTagRuleName_.isEmpty()) {
                        if (this.removeTagRuleName_.isEmpty()) {
                            this.removeTagRuleName_ = other.removeTagRuleName_;
                            this.bitField0_ &= -33;
                        } else {
                            ensureRemoveTagRuleNameIsMutable();
                            this.removeTagRuleName_.addAll(other.removeTagRuleName_);
                        }
                    }
                    if (!other.addMacro_.isEmpty()) {
                        if (this.addMacro_.isEmpty()) {
                            this.addMacro_ = other.addMacro_;
                            this.bitField0_ &= -65;
                        } else {
                            ensureAddMacroIsMutable();
                            this.addMacro_.addAll(other.addMacro_);
                        }
                    }
                    if (!other.removeMacro_.isEmpty()) {
                        if (this.removeMacro_.isEmpty()) {
                            this.removeMacro_ = other.removeMacro_;
                            this.bitField0_ &= -129;
                        } else {
                            ensureRemoveMacroIsMutable();
                            this.removeMacro_.addAll(other.removeMacro_);
                        }
                    }
                    if (!other.addMacroRuleName_.isEmpty()) {
                        if (this.addMacroRuleName_.isEmpty()) {
                            this.addMacroRuleName_ = other.addMacroRuleName_;
                            this.bitField0_ &= -257;
                        } else {
                            ensureAddMacroRuleNameIsMutable();
                            this.addMacroRuleName_.addAll(other.addMacroRuleName_);
                        }
                    }
                    if (!other.removeMacroRuleName_.isEmpty()) {
                        if (this.removeMacroRuleName_.isEmpty()) {
                            this.removeMacroRuleName_ = other.removeMacroRuleName_;
                            this.bitField0_ &= -513;
                        } else {
                            ensureRemoveMacroRuleNameIsMutable();
                            this.removeMacroRuleName_.addAll(other.removeMacroRuleName_);
                        }
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Rule parsedMessage = null;
                try {
                    try {
                        parsedMessage = Rule.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            private void ensurePositivePredicateIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.positivePredicate_ = new ArrayList(this.positivePredicate_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getPositivePredicateList() {
                return Collections.unmodifiableList(this.positivePredicate_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getPositivePredicateCount() {
                return this.positivePredicate_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getPositivePredicate(int index) {
                return this.positivePredicate_.get(index).intValue();
            }

            public Builder setPositivePredicate(int index, int value) {
                ensurePositivePredicateIsMutable();
                this.positivePredicate_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addPositivePredicate(int value) {
                ensurePositivePredicateIsMutable();
                this.positivePredicate_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllPositivePredicate(Iterable<? extends Integer> values) {
                ensurePositivePredicateIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.positivePredicate_);
                return this;
            }

            public Builder clearPositivePredicate() {
                this.positivePredicate_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            private void ensureNegativePredicateIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.negativePredicate_ = new ArrayList(this.negativePredicate_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getNegativePredicateList() {
                return Collections.unmodifiableList(this.negativePredicate_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getNegativePredicateCount() {
                return this.negativePredicate_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getNegativePredicate(int index) {
                return this.negativePredicate_.get(index).intValue();
            }

            public Builder setNegativePredicate(int index, int value) {
                ensureNegativePredicateIsMutable();
                this.negativePredicate_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addNegativePredicate(int value) {
                ensureNegativePredicateIsMutable();
                this.negativePredicate_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllNegativePredicate(Iterable<? extends Integer> values) {
                ensureNegativePredicateIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.negativePredicate_);
                return this;
            }

            public Builder clearNegativePredicate() {
                this.negativePredicate_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            private void ensureAddTagIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.addTag_ = new ArrayList(this.addTag_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getAddTagList() {
                return Collections.unmodifiableList(this.addTag_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddTagCount() {
                return this.addTag_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddTag(int index) {
                return this.addTag_.get(index).intValue();
            }

            public Builder setAddTag(int index, int value) {
                ensureAddTagIsMutable();
                this.addTag_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddTag(int value) {
                ensureAddTagIsMutable();
                this.addTag_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddTag(Iterable<? extends Integer> values) {
                ensureAddTagIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addTag_);
                return this;
            }

            public Builder clearAddTag() {
                this.addTag_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            private void ensureRemoveTagIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.removeTag_ = new ArrayList(this.removeTag_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getRemoveTagList() {
                return Collections.unmodifiableList(this.removeTag_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveTagCount() {
                return this.removeTag_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveTag(int index) {
                return this.removeTag_.get(index).intValue();
            }

            public Builder setRemoveTag(int index, int value) {
                ensureRemoveTagIsMutable();
                this.removeTag_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveTag(int value) {
                ensureRemoveTagIsMutable();
                this.removeTag_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveTag(Iterable<? extends Integer> values) {
                ensureRemoveTagIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeTag_);
                return this;
            }

            public Builder clearRemoveTag() {
                this.removeTag_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            private void ensureAddTagRuleNameIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.addTagRuleName_ = new ArrayList(this.addTagRuleName_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getAddTagRuleNameList() {
                return Collections.unmodifiableList(this.addTagRuleName_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddTagRuleNameCount() {
                return this.addTagRuleName_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddTagRuleName(int index) {
                return this.addTagRuleName_.get(index).intValue();
            }

            public Builder setAddTagRuleName(int index, int value) {
                ensureAddTagRuleNameIsMutable();
                this.addTagRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddTagRuleName(int value) {
                ensureAddTagRuleNameIsMutable();
                this.addTagRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddTagRuleName(Iterable<? extends Integer> values) {
                ensureAddTagRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addTagRuleName_);
                return this;
            }

            public Builder clearAddTagRuleName() {
                this.addTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            private void ensureRemoveTagRuleNameIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.removeTagRuleName_ = new ArrayList(this.removeTagRuleName_);
                    this.bitField0_ |= 32;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getRemoveTagRuleNameList() {
                return Collections.unmodifiableList(this.removeTagRuleName_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveTagRuleNameCount() {
                return this.removeTagRuleName_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveTagRuleName(int index) {
                return this.removeTagRuleName_.get(index).intValue();
            }

            public Builder setRemoveTagRuleName(int index, int value) {
                ensureRemoveTagRuleNameIsMutable();
                this.removeTagRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveTagRuleName(int value) {
                ensureRemoveTagRuleNameIsMutable();
                this.removeTagRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveTagRuleName(Iterable<? extends Integer> values) {
                ensureRemoveTagRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeTagRuleName_);
                return this;
            }

            public Builder clearRemoveTagRuleName() {
                this.removeTagRuleName_ = Collections.emptyList();
                this.bitField0_ &= -33;
                return this;
            }

            private void ensureAddMacroIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.addMacro_ = new ArrayList(this.addMacro_);
                    this.bitField0_ |= 64;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getAddMacroList() {
                return Collections.unmodifiableList(this.addMacro_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddMacroCount() {
                return this.addMacro_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddMacro(int index) {
                return this.addMacro_.get(index).intValue();
            }

            public Builder setAddMacro(int index, int value) {
                ensureAddMacroIsMutable();
                this.addMacro_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddMacro(int value) {
                ensureAddMacroIsMutable();
                this.addMacro_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddMacro(Iterable<? extends Integer> values) {
                ensureAddMacroIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addMacro_);
                return this;
            }

            public Builder clearAddMacro() {
                this.addMacro_ = Collections.emptyList();
                this.bitField0_ &= -65;
                return this;
            }

            private void ensureRemoveMacroIsMutable() {
                if ((this.bitField0_ & 128) != 128) {
                    this.removeMacro_ = new ArrayList(this.removeMacro_);
                    this.bitField0_ |= 128;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getRemoveMacroList() {
                return Collections.unmodifiableList(this.removeMacro_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveMacroCount() {
                return this.removeMacro_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveMacro(int index) {
                return this.removeMacro_.get(index).intValue();
            }

            public Builder setRemoveMacro(int index, int value) {
                ensureRemoveMacroIsMutable();
                this.removeMacro_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveMacro(int value) {
                ensureRemoveMacroIsMutable();
                this.removeMacro_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveMacro(Iterable<? extends Integer> values) {
                ensureRemoveMacroIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeMacro_);
                return this;
            }

            public Builder clearRemoveMacro() {
                this.removeMacro_ = Collections.emptyList();
                this.bitField0_ &= -129;
                return this;
            }

            private void ensureAddMacroRuleNameIsMutable() {
                if ((this.bitField0_ & 256) != 256) {
                    this.addMacroRuleName_ = new ArrayList(this.addMacroRuleName_);
                    this.bitField0_ |= 256;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getAddMacroRuleNameList() {
                return Collections.unmodifiableList(this.addMacroRuleName_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddMacroRuleNameCount() {
                return this.addMacroRuleName_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getAddMacroRuleName(int index) {
                return this.addMacroRuleName_.get(index).intValue();
            }

            public Builder setAddMacroRuleName(int index, int value) {
                ensureAddMacroRuleNameIsMutable();
                this.addMacroRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addAddMacroRuleName(int value) {
                ensureAddMacroRuleNameIsMutable();
                this.addMacroRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllAddMacroRuleName(Iterable<? extends Integer> values) {
                ensureAddMacroRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addMacroRuleName_);
                return this;
            }

            public Builder clearAddMacroRuleName() {
                this.addMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -257;
                return this;
            }

            private void ensureRemoveMacroRuleNameIsMutable() {
                if ((this.bitField0_ & 512) != 512) {
                    this.removeMacroRuleName_ = new ArrayList(this.removeMacroRuleName_);
                    this.bitField0_ |= 512;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public List<Integer> getRemoveMacroRuleNameList() {
                return Collections.unmodifiableList(this.removeMacroRuleName_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveMacroRuleNameCount() {
                return this.removeMacroRuleName_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.RuleOrBuilder
            public int getRemoveMacroRuleName(int index) {
                return this.removeMacroRuleName_.get(index).intValue();
            }

            public Builder setRemoveMacroRuleName(int index, int value) {
                ensureRemoveMacroRuleNameIsMutable();
                this.removeMacroRuleName_.set(index, Integer.valueOf(value));
                return this;
            }

            public Builder addRemoveMacroRuleName(int value) {
                ensureRemoveMacroRuleNameIsMutable();
                this.removeMacroRuleName_.add(Integer.valueOf(value));
                return this;
            }

            public Builder addAllRemoveMacroRuleName(Iterable<? extends Integer> values) {
                ensureRemoveMacroRuleNameIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeMacroRuleName_);
                return this;
            }

            public Builder clearRemoveMacroRuleName() {
                this.removeMacroRuleName_ = Collections.emptyList();
                this.bitField0_ &= -513;
                return this;
            }
        }
    }

    public static final class CacheOption extends GeneratedMessageLite implements CacheOptionOrBuilder {
        public static final int EXPIRATION_SECONDS_FIELD_NUMBER = 2;
        public static final int GCACHE_EXPIRATION_SECONDS_FIELD_NUMBER = 3;
        public static final int LEVEL_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private int expirationSeconds_;
        private int gcacheExpirationSeconds_;
        private CacheLevel level_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private final ByteString unknownFields;
        public static Parser<CacheOption> PARSER = new AbstractParser<CacheOption>() { // from class: com.google.analytics.containertag.proto.Serving.CacheOption.1
            @Override // com.google.tagmanager.protobuf.Parser
            public CacheOption parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new CacheOption(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final CacheOption defaultInstance = new CacheOption(true);

        private CacheOption(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private CacheOption(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static CacheOption getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public CacheOption getDefaultInstanceForType() {
            return defaultInstance;
        }

        private CacheOption(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                int rawValue = input.readEnum();
                                CacheLevel value = CacheLevel.valueOf(rawValue);
                                if (value == null) {
                                    unknownFieldsCodedOutput.writeRawVarint32(tag);
                                    unknownFieldsCodedOutput.writeRawVarint32(rawValue);
                                } else {
                                    this.bitField0_ |= 1;
                                    this.level_ = value;
                                }
                                break;
                            case 16:
                                this.bitField0_ |= 2;
                                this.expirationSeconds_ = input.readInt32();
                                break;
                            case 24:
                                this.bitField0_ |= 4;
                                this.gcacheExpirationSeconds_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        try {
                            unknownFieldsCodedOutput.flush();
                        } catch (IOException e) {
                        } finally {
                        }
                        makeExtensionsImmutable();
                        throw th;
                    }
                } catch (InvalidProtocolBufferException e2) {
                    throw e2.setUnfinishedMessage(this);
                } catch (IOException e3) {
                    throw new InvalidProtocolBufferException(e3.getMessage()).setUnfinishedMessage(this);
                }
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<CacheOption> getParserForType() {
            return PARSER;
        }

        public enum CacheLevel implements Internal.EnumLite {
            NO_CACHE(0, 1),
            PRIVATE(1, 2),
            PUBLIC(2, 3);

            public static final int NO_CACHE_VALUE = 1;
            public static final int PRIVATE_VALUE = 2;
            public static final int PUBLIC_VALUE = 3;
            private static Internal.EnumLiteMap<CacheLevel> internalValueMap = new Internal.EnumLiteMap<CacheLevel>() { // from class: com.google.analytics.containertag.proto.Serving.CacheOption.CacheLevel.1
                @Override // com.google.tagmanager.protobuf.Internal.EnumLiteMap
                public CacheLevel findValueByNumber(int number) {
                    return CacheLevel.valueOf(number);
                }
            };
            private final int value;

            @Override // com.google.tagmanager.protobuf.Internal.EnumLite
            public final int getNumber() {
                return this.value;
            }

            public static CacheLevel valueOf(int value) {
                switch (value) {
                    case 1:
                        return NO_CACHE;
                    case 2:
                        return PRIVATE;
                    case 3:
                        return PUBLIC;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<CacheLevel> internalGetValueMap() {
                return internalValueMap;
            }

            CacheLevel(int index, int value) {
                this.value = value;
            }
        }

        @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
        public boolean hasLevel() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
        public CacheLevel getLevel() {
            return this.level_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
        public boolean hasExpirationSeconds() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
        public int getExpirationSeconds() {
            return this.expirationSeconds_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
        public boolean hasGcacheExpirationSeconds() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
        public int getGcacheExpirationSeconds() {
            return this.gcacheExpirationSeconds_;
        }

        private void initFields() {
            this.level_ = CacheLevel.NO_CACHE;
            this.expirationSeconds_ = 0;
            this.gcacheExpirationSeconds_ = 0;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeEnum(1, this.level_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeInt32(2, this.expirationSeconds_);
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeInt32(3, this.gcacheExpirationSeconds_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeEnumSize(1, this.level_.getNumber()) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeInt32Size(2, this.expirationSeconds_);
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeInt32Size(3, this.gcacheExpirationSeconds_);
            }
            int size3 = size2 + this.unknownFields.size();
            this.memoizedSerializedSize = size3;
            return size3;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof CacheOption)) {
                return super.equals(obj);
            }
            CacheOption other = (CacheOption) obj;
            boolean result = 1 != 0 && hasLevel() == other.hasLevel();
            if (hasLevel()) {
                result = result && getLevel() == other.getLevel();
            }
            boolean result2 = result && hasExpirationSeconds() == other.hasExpirationSeconds();
            if (hasExpirationSeconds()) {
                result2 = result2 && getExpirationSeconds() == other.getExpirationSeconds();
            }
            boolean result3 = result2 && hasGcacheExpirationSeconds() == other.hasGcacheExpirationSeconds();
            if (hasGcacheExpirationSeconds()) {
                result3 = result3 && getGcacheExpirationSeconds() == other.getGcacheExpirationSeconds();
            }
            return result3;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = CacheOption.class.hashCode() + 779;
            if (hasLevel()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashEnum(getLevel());
            }
            if (hasExpirationSeconds()) {
                hash = (((hash * 37) + 2) * 53) + getExpirationSeconds();
            }
            if (hasGcacheExpirationSeconds()) {
                hash = (((hash * 37) + 3) * 53) + getGcacheExpirationSeconds();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$CacheOption");
            }
            return mutableDefault;
        }

        public static CacheOption parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static CacheOption parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static CacheOption parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static CacheOption parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static CacheOption parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static CacheOption parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static CacheOption parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static CacheOption parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static CacheOption parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static CacheOption parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CacheOption prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<CacheOption, Builder> implements CacheOptionOrBuilder {
            private int bitField0_;
            private int expirationSeconds_;
            private int gcacheExpirationSeconds_;
            private CacheLevel level_ = CacheLevel.NO_CACHE;

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.level_ = CacheLevel.NO_CACHE;
                this.bitField0_ &= -2;
                this.expirationSeconds_ = 0;
                this.bitField0_ &= -3;
                this.gcacheExpirationSeconds_ = 0;
                this.bitField0_ &= -5;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public CacheOption getDefaultInstanceForType() {
                return CacheOption.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public CacheOption build() {
                CacheOption result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public CacheOption buildPartial() {
                CacheOption result = new CacheOption(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.level_ = this.level_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.expirationSeconds_ = this.expirationSeconds_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                result.gcacheExpirationSeconds_ = this.gcacheExpirationSeconds_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(CacheOption other) {
                if (other != CacheOption.getDefaultInstance()) {
                    if (other.hasLevel()) {
                        setLevel(other.getLevel());
                    }
                    if (other.hasExpirationSeconds()) {
                        setExpirationSeconds(other.getExpirationSeconds());
                    }
                    if (other.hasGcacheExpirationSeconds()) {
                        setGcacheExpirationSeconds(other.getGcacheExpirationSeconds());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                CacheOption parsedMessage = null;
                try {
                    try {
                        parsedMessage = CacheOption.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
            public boolean hasLevel() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
            public CacheLevel getLevel() {
                return this.level_;
            }

            public Builder setLevel(CacheLevel value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.level_ = value;
                return this;
            }

            public Builder clearLevel() {
                this.bitField0_ &= -2;
                this.level_ = CacheLevel.NO_CACHE;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
            public boolean hasExpirationSeconds() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
            public int getExpirationSeconds() {
                return this.expirationSeconds_;
            }

            public Builder setExpirationSeconds(int value) {
                this.bitField0_ |= 2;
                this.expirationSeconds_ = value;
                return this;
            }

            public Builder clearExpirationSeconds() {
                this.bitField0_ &= -3;
                this.expirationSeconds_ = 0;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
            public boolean hasGcacheExpirationSeconds() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override // com.google.analytics.containertag.proto.Serving.CacheOptionOrBuilder
            public int getGcacheExpirationSeconds() {
                return this.gcacheExpirationSeconds_;
            }

            public Builder setGcacheExpirationSeconds(int value) {
                this.bitField0_ |= 4;
                this.gcacheExpirationSeconds_ = value;
                return this;
            }

            public Builder clearGcacheExpirationSeconds() {
                this.bitField0_ &= -5;
                this.gcacheExpirationSeconds_ = 0;
                return this;
            }
        }
    }

    public static final class Resource extends GeneratedMessageLite implements ResourceOrBuilder {
        public static final int ENABLE_AUTO_EVENT_TRACKING_FIELD_NUMBER = 18;
        public static final int KEY_FIELD_NUMBER = 1;
        public static final int LIVE_JS_CACHE_OPTION_FIELD_NUMBER = 14;
        public static final int MACRO_FIELD_NUMBER = 4;
        public static final int MALWARE_SCAN_AUTH_CODE_FIELD_NUMBER = 10;
        public static final int PREDICATE_FIELD_NUMBER = 6;
        public static final int PREVIEW_AUTH_CODE_FIELD_NUMBER = 9;
        public static final int PROPERTY_FIELD_NUMBER = 3;
        public static final int REPORTING_SAMPLE_RATE_FIELD_NUMBER = 15;
        public static final int RESOURCE_FORMAT_VERSION_FIELD_NUMBER = 17;
        public static final int RULE_FIELD_NUMBER = 7;
        public static final int TAG_FIELD_NUMBER = 5;
        public static final int TEMPLATE_VERSION_SET_FIELD_NUMBER = 12;
        public static final int USAGE_CONTEXT_FIELD_NUMBER = 16;
        public static final int VALUE_FIELD_NUMBER = 2;
        public static final int VERSION_FIELD_NUMBER = 13;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private boolean enableAutoEventTracking_;
        private LazyStringList key_;
        private CacheOption liveJsCacheOption_;
        private List<FunctionCall> macro_;
        private Object malwareScanAuthCode_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<FunctionCall> predicate_;
        private Object previewAuthCode_;
        private List<Property> property_;
        private float reportingSampleRate_;
        private int resourceFormatVersion_;
        private List<Rule> rule_;
        private List<FunctionCall> tag_;
        private Object templateVersionSet_;
        private final ByteString unknownFields;
        private LazyStringList usageContext_;
        private List<TypeSystem.Value> value_;
        private Object version_;
        public static Parser<Resource> PARSER = new AbstractParser<Resource>() { // from class: com.google.analytics.containertag.proto.Serving.Resource.1
            @Override // com.google.tagmanager.protobuf.Parser
            public Resource parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Resource(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final Resource defaultInstance = new Resource(true);

        private Resource(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Resource(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Resource getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public Resource getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Resource(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int i = 0;
            CodedOutputStream codedOutputStreamNewInstance = CodedOutputStream.newInstance(ByteString.newOutput());
            boolean z = false;
            while (!z) {
                try {
                    try {
                        try {
                            int tag = codedInputStream.readTag();
                            switch (tag) {
                                case 0:
                                    z = true;
                                    break;
                                case 10:
                                    ByteString bytes = codedInputStream.readBytes();
                                    if ((i & 1) != 1) {
                                        this.key_ = new LazyStringArrayList();
                                        i |= 1;
                                    }
                                    this.key_.add(bytes);
                                    break;
                                case 18:
                                    if ((i & 2) != 2) {
                                        this.value_ = new ArrayList();
                                        i |= 2;
                                    }
                                    this.value_.add((TypeSystem.Value) codedInputStream.readMessage(TypeSystem.Value.PARSER, extensionRegistryLite));
                                    break;
                                case 26:
                                    if ((i & 4) != 4) {
                                        this.property_ = new ArrayList();
                                        i |= 4;
                                    }
                                    this.property_.add((Property) codedInputStream.readMessage(Property.PARSER, extensionRegistryLite));
                                    break;
                                case 34:
                                    if ((i & 8) != 8) {
                                        this.macro_ = new ArrayList();
                                        i |= 8;
                                    }
                                    this.macro_.add((FunctionCall) codedInputStream.readMessage(FunctionCall.PARSER, extensionRegistryLite));
                                    break;
                                case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                                    if ((i & 16) != 16) {
                                        this.tag_ = new ArrayList();
                                        i |= 16;
                                    }
                                    this.tag_.add((FunctionCall) codedInputStream.readMessage(FunctionCall.PARSER, extensionRegistryLite));
                                    break;
                                case 50:
                                    if ((i & 32) != 32) {
                                        this.predicate_ = new ArrayList();
                                        i |= 32;
                                    }
                                    this.predicate_.add((FunctionCall) codedInputStream.readMessage(FunctionCall.PARSER, extensionRegistryLite));
                                    break;
                                case 58:
                                    if ((i & 64) != 64) {
                                        this.rule_ = new ArrayList();
                                        i |= 64;
                                    }
                                    this.rule_.add((Rule) codedInputStream.readMessage(Rule.PARSER, extensionRegistryLite));
                                    break;
                                case 74:
                                    ByteString bytes2 = codedInputStream.readBytes();
                                    this.bitField0_ |= 1;
                                    this.previewAuthCode_ = bytes2;
                                    break;
                                case 82:
                                    ByteString bytes3 = codedInputStream.readBytes();
                                    this.bitField0_ |= 2;
                                    this.malwareScanAuthCode_ = bytes3;
                                    break;
                                case 98:
                                    ByteString bytes4 = codedInputStream.readBytes();
                                    this.bitField0_ |= 4;
                                    this.templateVersionSet_ = bytes4;
                                    break;
                                case 106:
                                    ByteString bytes5 = codedInputStream.readBytes();
                                    this.bitField0_ |= 8;
                                    this.version_ = bytes5;
                                    break;
                                case 114:
                                    CacheOption.Builder builder = (this.bitField0_ & 16) == 16 ? this.liveJsCacheOption_.toBuilder() : null;
                                    this.liveJsCacheOption_ = (CacheOption) codedInputStream.readMessage(CacheOption.PARSER, extensionRegistryLite);
                                    if (builder != null) {
                                        builder.mergeFrom(this.liveJsCacheOption_);
                                        this.liveJsCacheOption_ = builder.buildPartial();
                                    }
                                    this.bitField0_ |= 16;
                                    break;
                                case 125:
                                    this.bitField0_ |= 32;
                                    this.reportingSampleRate_ = codedInputStream.readFloat();
                                    break;
                                case 130:
                                    ByteString bytes6 = codedInputStream.readBytes();
                                    if ((i & 16384) != 16384) {
                                        this.usageContext_ = new LazyStringArrayList();
                                        i |= 16384;
                                    }
                                    this.usageContext_.add(bytes6);
                                    break;
                                case 136:
                                    this.bitField0_ |= 128;
                                    this.resourceFormatVersion_ = codedInputStream.readInt32();
                                    break;
                                case TwitterApiConstants.Errors.ALREADY_UNFAVORITED /* 144 */:
                                    this.bitField0_ |= 64;
                                    this.enableAutoEventTracking_ = codedInputStream.readBool();
                                    break;
                                default:
                                    if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                        z = true;
                                    }
                                    break;
                            }
                        } catch (IOException e) {
                            throw new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    }
                } catch (Throwable th) {
                    if ((i & 1) == 1) {
                        this.key_ = new UnmodifiableLazyStringList(this.key_);
                    }
                    if ((i & 2) == 2) {
                        this.value_ = Collections.unmodifiableList(this.value_);
                    }
                    if ((i & 4) == 4) {
                        this.property_ = Collections.unmodifiableList(this.property_);
                    }
                    if ((i & 8) == 8) {
                        this.macro_ = Collections.unmodifiableList(this.macro_);
                    }
                    if ((i & 16) == 16) {
                        this.tag_ = Collections.unmodifiableList(this.tag_);
                    }
                    if ((i & 32) == 32) {
                        this.predicate_ = Collections.unmodifiableList(this.predicate_);
                    }
                    if ((i & 64) == 64) {
                        this.rule_ = Collections.unmodifiableList(this.rule_);
                    }
                    if ((i & 16384) == 16384) {
                        this.usageContext_ = new UnmodifiableLazyStringList(this.usageContext_);
                    }
                    try {
                        codedOutputStreamNewInstance.flush();
                    } catch (IOException e3) {
                    } finally {
                    }
                    makeExtensionsImmutable();
                    throw th;
                }
            }
            if ((i & 1) == 1) {
                this.key_ = new UnmodifiableLazyStringList(this.key_);
            }
            if ((i & 2) == 2) {
                this.value_ = Collections.unmodifiableList(this.value_);
            }
            if ((i & 4) == 4) {
                this.property_ = Collections.unmodifiableList(this.property_);
            }
            if ((i & 8) == 8) {
                this.macro_ = Collections.unmodifiableList(this.macro_);
            }
            if ((i & 16) == 16) {
                this.tag_ = Collections.unmodifiableList(this.tag_);
            }
            if ((i & 32) == 32) {
                this.predicate_ = Collections.unmodifiableList(this.predicate_);
            }
            if ((i & 64) == 64) {
                this.rule_ = Collections.unmodifiableList(this.rule_);
            }
            if ((i & 16384) == 16384) {
                this.usageContext_ = new UnmodifiableLazyStringList(this.usageContext_);
            }
            try {
                codedOutputStreamNewInstance.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<Resource> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<String> getKeyList() {
            return this.key_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getKeyCount() {
            return this.key_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public String getKey(int index) {
            return this.key_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public ByteString getKeyBytes(int index) {
            return this.key_.getByteString(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<TypeSystem.Value> getValueList() {
            return this.value_;
        }

        public List<? extends TypeSystem.ValueOrBuilder> getValueOrBuilderList() {
            return this.value_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getValueCount() {
            return this.value_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public TypeSystem.Value getValue(int index) {
            return this.value_.get(index);
        }

        public TypeSystem.ValueOrBuilder getValueOrBuilder(int index) {
            return this.value_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<Property> getPropertyList() {
            return this.property_;
        }

        public List<? extends PropertyOrBuilder> getPropertyOrBuilderList() {
            return this.property_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getPropertyCount() {
            return this.property_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public Property getProperty(int index) {
            return this.property_.get(index);
        }

        public PropertyOrBuilder getPropertyOrBuilder(int index) {
            return this.property_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<FunctionCall> getMacroList() {
            return this.macro_;
        }

        public List<? extends FunctionCallOrBuilder> getMacroOrBuilderList() {
            return this.macro_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getMacroCount() {
            return this.macro_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public FunctionCall getMacro(int index) {
            return this.macro_.get(index);
        }

        public FunctionCallOrBuilder getMacroOrBuilder(int index) {
            return this.macro_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<FunctionCall> getTagList() {
            return this.tag_;
        }

        public List<? extends FunctionCallOrBuilder> getTagOrBuilderList() {
            return this.tag_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getTagCount() {
            return this.tag_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public FunctionCall getTag(int index) {
            return this.tag_.get(index);
        }

        public FunctionCallOrBuilder getTagOrBuilder(int index) {
            return this.tag_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<FunctionCall> getPredicateList() {
            return this.predicate_;
        }

        public List<? extends FunctionCallOrBuilder> getPredicateOrBuilderList() {
            return this.predicate_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getPredicateCount() {
            return this.predicate_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public FunctionCall getPredicate(int index) {
            return this.predicate_.get(index);
        }

        public FunctionCallOrBuilder getPredicateOrBuilder(int index) {
            return this.predicate_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<Rule> getRuleList() {
            return this.rule_;
        }

        public List<? extends RuleOrBuilder> getRuleOrBuilderList() {
            return this.rule_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getRuleCount() {
            return this.rule_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public Rule getRule(int index) {
            return this.rule_.get(index);
        }

        public RuleOrBuilder getRuleOrBuilder(int index) {
            return this.rule_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasPreviewAuthCode() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public String getPreviewAuthCode() {
            Object ref = this.previewAuthCode_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.previewAuthCode_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public ByteString getPreviewAuthCodeBytes() {
            Object ref = this.previewAuthCode_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.previewAuthCode_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasMalwareScanAuthCode() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public String getMalwareScanAuthCode() {
            Object ref = this.malwareScanAuthCode_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.malwareScanAuthCode_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public ByteString getMalwareScanAuthCodeBytes() {
            Object ref = this.malwareScanAuthCode_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.malwareScanAuthCode_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasTemplateVersionSet() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public String getTemplateVersionSet() {
            Object ref = this.templateVersionSet_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.templateVersionSet_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public ByteString getTemplateVersionSetBytes() {
            Object ref = this.templateVersionSet_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.templateVersionSet_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasVersion() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public String getVersion() {
            Object ref = this.version_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.version_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public ByteString getVersionBytes() {
            Object ref = this.version_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.version_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasLiveJsCacheOption() {
            return (this.bitField0_ & 16) == 16;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public CacheOption getLiveJsCacheOption() {
            return this.liveJsCacheOption_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasReportingSampleRate() {
            return (this.bitField0_ & 32) == 32;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public float getReportingSampleRate() {
            return this.reportingSampleRate_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasEnableAutoEventTracking() {
            return (this.bitField0_ & 64) == 64;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean getEnableAutoEventTracking() {
            return this.enableAutoEventTracking_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public List<String> getUsageContextList() {
            return this.usageContext_;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getUsageContextCount() {
            return this.usageContext_.size();
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public String getUsageContext(int index) {
            return this.usageContext_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public ByteString getUsageContextBytes(int index) {
            return this.usageContext_.getByteString(index);
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public boolean hasResourceFormatVersion() {
            return (this.bitField0_ & 128) == 128;
        }

        @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
        public int getResourceFormatVersion() {
            return this.resourceFormatVersion_;
        }

        private void initFields() {
            this.key_ = LazyStringArrayList.EMPTY;
            this.value_ = Collections.emptyList();
            this.property_ = Collections.emptyList();
            this.macro_ = Collections.emptyList();
            this.tag_ = Collections.emptyList();
            this.predicate_ = Collections.emptyList();
            this.rule_ = Collections.emptyList();
            this.previewAuthCode_ = "";
            this.malwareScanAuthCode_ = "";
            this.templateVersionSet_ = "0";
            this.version_ = "";
            this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
            this.reportingSampleRate_ = 0.0f;
            this.enableAutoEventTracking_ = false;
            this.usageContext_ = LazyStringArrayList.EMPTY;
            this.resourceFormatVersion_ = 0;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            for (int i = 0; i < getValueCount(); i++) {
                if (!getValue(i).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i2 = 0; i2 < getPropertyCount(); i2++) {
                if (!getProperty(i2).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i3 = 0; i3 < getMacroCount(); i3++) {
                if (!getMacro(i3).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i4 = 0; i4 < getTagCount(); i4++) {
                if (!getTag(i4).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i5 = 0; i5 < getPredicateCount(); i5++) {
                if (!getPredicate(i5).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.key_.size(); i++) {
                output.writeBytes(1, this.key_.getByteString(i));
            }
            for (int i2 = 0; i2 < this.value_.size(); i2++) {
                output.writeMessage(2, this.value_.get(i2));
            }
            for (int i3 = 0; i3 < this.property_.size(); i3++) {
                output.writeMessage(3, this.property_.get(i3));
            }
            for (int i4 = 0; i4 < this.macro_.size(); i4++) {
                output.writeMessage(4, this.macro_.get(i4));
            }
            for (int i5 = 0; i5 < this.tag_.size(); i5++) {
                output.writeMessage(5, this.tag_.get(i5));
            }
            for (int i6 = 0; i6 < this.predicate_.size(); i6++) {
                output.writeMessage(6, this.predicate_.get(i6));
            }
            for (int i7 = 0; i7 < this.rule_.size(); i7++) {
                output.writeMessage(7, this.rule_.get(i7));
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(9, getPreviewAuthCodeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(10, getMalwareScanAuthCodeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBytes(12, getTemplateVersionSetBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(13, getVersionBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeMessage(14, this.liveJsCacheOption_);
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeFloat(15, this.reportingSampleRate_);
            }
            for (int i8 = 0; i8 < this.usageContext_.size(); i8++) {
                output.writeBytes(16, this.usageContext_.getByteString(i8));
            }
            if ((this.bitField0_ & 128) == 128) {
                output.writeInt32(17, this.resourceFormatVersion_);
            }
            if ((this.bitField0_ & 64) == 64) {
                output.writeBool(18, this.enableAutoEventTracking_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int dataSize = 0;
            for (int i = 0; i < this.key_.size(); i++) {
                dataSize += CodedOutputStream.computeBytesSizeNoTag(this.key_.getByteString(i));
            }
            int size2 = 0 + dataSize + (getKeyList().size() * 1);
            for (int i2 = 0; i2 < this.value_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(2, this.value_.get(i2));
            }
            for (int i3 = 0; i3 < this.property_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.property_.get(i3));
            }
            for (int i4 = 0; i4 < this.macro_.size(); i4++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.macro_.get(i4));
            }
            for (int i5 = 0; i5 < this.tag_.size(); i5++) {
                size2 += CodedOutputStream.computeMessageSize(5, this.tag_.get(i5));
            }
            for (int i6 = 0; i6 < this.predicate_.size(); i6++) {
                size2 += CodedOutputStream.computeMessageSize(6, this.predicate_.get(i6));
            }
            for (int i7 = 0; i7 < this.rule_.size(); i7++) {
                size2 += CodedOutputStream.computeMessageSize(7, this.rule_.get(i7));
            }
            if ((this.bitField0_ & 1) == 1) {
                size2 += CodedOutputStream.computeBytesSize(9, getPreviewAuthCodeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(10, getMalwareScanAuthCodeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeBytesSize(12, getTemplateVersionSetBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(13, getVersionBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                size2 += CodedOutputStream.computeMessageSize(14, this.liveJsCacheOption_);
            }
            if ((this.bitField0_ & 32) == 32) {
                size2 += CodedOutputStream.computeFloatSize(15, this.reportingSampleRate_);
            }
            int dataSize2 = 0;
            for (int i8 = 0; i8 < this.usageContext_.size(); i8++) {
                dataSize2 += CodedOutputStream.computeBytesSizeNoTag(this.usageContext_.getByteString(i8));
            }
            int size3 = size2 + dataSize2 + (getUsageContextList().size() * 2);
            if ((this.bitField0_ & 128) == 128) {
                size3 += CodedOutputStream.computeInt32Size(17, this.resourceFormatVersion_);
            }
            if ((this.bitField0_ & 64) == 64) {
                size3 += CodedOutputStream.computeBoolSize(18, this.enableAutoEventTracking_);
            }
            int size4 = size3 + this.unknownFields.size();
            this.memoizedSerializedSize = size4;
            return size4;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Resource)) {
                return super.equals(obj);
            }
            Resource other = (Resource) obj;
            boolean result = 1 != 0 && getKeyList().equals(other.getKeyList());
            boolean result2 = result && getValueList().equals(other.getValueList());
            boolean result3 = result2 && getPropertyList().equals(other.getPropertyList());
            boolean result4 = result3 && getMacroList().equals(other.getMacroList());
            boolean result5 = result4 && getTagList().equals(other.getTagList());
            boolean result6 = result5 && getPredicateList().equals(other.getPredicateList());
            boolean result7 = result6 && getRuleList().equals(other.getRuleList());
            boolean result8 = result7 && hasPreviewAuthCode() == other.hasPreviewAuthCode();
            if (hasPreviewAuthCode()) {
                result8 = result8 && getPreviewAuthCode().equals(other.getPreviewAuthCode());
            }
            boolean result9 = result8 && hasMalwareScanAuthCode() == other.hasMalwareScanAuthCode();
            if (hasMalwareScanAuthCode()) {
                result9 = result9 && getMalwareScanAuthCode().equals(other.getMalwareScanAuthCode());
            }
            boolean result10 = result9 && hasTemplateVersionSet() == other.hasTemplateVersionSet();
            if (hasTemplateVersionSet()) {
                result10 = result10 && getTemplateVersionSet().equals(other.getTemplateVersionSet());
            }
            boolean result11 = result10 && hasVersion() == other.hasVersion();
            if (hasVersion()) {
                result11 = result11 && getVersion().equals(other.getVersion());
            }
            boolean result12 = result11 && hasLiveJsCacheOption() == other.hasLiveJsCacheOption();
            if (hasLiveJsCacheOption()) {
                result12 = result12 && getLiveJsCacheOption().equals(other.getLiveJsCacheOption());
            }
            boolean result13 = result12 && hasReportingSampleRate() == other.hasReportingSampleRate();
            if (hasReportingSampleRate()) {
                result13 = result13 && Float.floatToIntBits(getReportingSampleRate()) == Float.floatToIntBits(other.getReportingSampleRate());
            }
            boolean result14 = result13 && hasEnableAutoEventTracking() == other.hasEnableAutoEventTracking();
            if (hasEnableAutoEventTracking()) {
                result14 = result14 && getEnableAutoEventTracking() == other.getEnableAutoEventTracking();
            }
            boolean result15 = result14 && getUsageContextList().equals(other.getUsageContextList());
            boolean result16 = result15 && hasResourceFormatVersion() == other.hasResourceFormatVersion();
            if (hasResourceFormatVersion()) {
                result16 = result16 && getResourceFormatVersion() == other.getResourceFormatVersion();
            }
            return result16;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Resource.class.hashCode() + 779;
            if (getKeyCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getKeyList().hashCode();
            }
            if (getValueCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getValueList().hashCode();
            }
            if (getPropertyCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getPropertyList().hashCode();
            }
            if (getMacroCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getMacroList().hashCode();
            }
            if (getTagCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getTagList().hashCode();
            }
            if (getPredicateCount() > 0) {
                hash = (((hash * 37) + 6) * 53) + getPredicateList().hashCode();
            }
            if (getRuleCount() > 0) {
                hash = (((hash * 37) + 7) * 53) + getRuleList().hashCode();
            }
            if (hasPreviewAuthCode()) {
                hash = (((hash * 37) + 9) * 53) + getPreviewAuthCode().hashCode();
            }
            if (hasMalwareScanAuthCode()) {
                hash = (((hash * 37) + 10) * 53) + getMalwareScanAuthCode().hashCode();
            }
            if (hasTemplateVersionSet()) {
                hash = (((hash * 37) + 12) * 53) + getTemplateVersionSet().hashCode();
            }
            if (hasVersion()) {
                hash = (((hash * 37) + 13) * 53) + getVersion().hashCode();
            }
            if (hasLiveJsCacheOption()) {
                hash = (((hash * 37) + 14) * 53) + getLiveJsCacheOption().hashCode();
            }
            if (hasReportingSampleRate()) {
                hash = (((hash * 37) + 15) * 53) + Float.floatToIntBits(getReportingSampleRate());
            }
            if (hasEnableAutoEventTracking()) {
                hash = (((hash * 37) + 18) * 53) + Internal.hashBoolean(getEnableAutoEventTracking());
            }
            if (getUsageContextCount() > 0) {
                hash = (((hash * 37) + 16) * 53) + getUsageContextList().hashCode();
            }
            if (hasResourceFormatVersion()) {
                hash = (((hash * 37) + 17) * 53) + getResourceFormatVersion();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$Resource");
            }
            return mutableDefault;
        }

        public static Resource parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Resource parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Resource parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Resource parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Resource parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Resource parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Resource parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Resource parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Resource parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Resource parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Resource prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<Resource, Builder> implements ResourceOrBuilder {
            private int bitField0_;
            private boolean enableAutoEventTracking_;
            private float reportingSampleRate_;
            private int resourceFormatVersion_;
            private LazyStringList key_ = LazyStringArrayList.EMPTY;
            private List<TypeSystem.Value> value_ = Collections.emptyList();
            private List<Property> property_ = Collections.emptyList();
            private List<FunctionCall> macro_ = Collections.emptyList();
            private List<FunctionCall> tag_ = Collections.emptyList();
            private List<FunctionCall> predicate_ = Collections.emptyList();
            private List<Rule> rule_ = Collections.emptyList();
            private Object previewAuthCode_ = "";
            private Object malwareScanAuthCode_ = "";
            private Object templateVersionSet_ = "0";
            private Object version_ = "";
            private CacheOption liveJsCacheOption_ = CacheOption.getDefaultInstance();
            private LazyStringList usageContext_ = LazyStringArrayList.EMPTY;

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.key_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                this.value_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.macro_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.tag_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.predicate_ = Collections.emptyList();
                this.bitField0_ &= -33;
                this.rule_ = Collections.emptyList();
                this.bitField0_ &= -65;
                this.previewAuthCode_ = "";
                this.bitField0_ &= -129;
                this.malwareScanAuthCode_ = "";
                this.bitField0_ &= -257;
                this.templateVersionSet_ = "0";
                this.bitField0_ &= -513;
                this.version_ = "";
                this.bitField0_ &= -1025;
                this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
                this.bitField0_ &= -2049;
                this.reportingSampleRate_ = 0.0f;
                this.bitField0_ &= -4097;
                this.enableAutoEventTracking_ = false;
                this.bitField0_ &= -8193;
                this.usageContext_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -16385;
                this.resourceFormatVersion_ = 0;
                this.bitField0_ &= -32769;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public Resource getDefaultInstanceForType() {
                return Resource.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Resource build() {
                Resource result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Resource buildPartial() {
                Resource result = new Resource(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.key_ = new UnmodifiableLazyStringList(this.key_);
                    this.bitField0_ &= -2;
                }
                result.key_ = this.key_;
                if ((this.bitField0_ & 2) == 2) {
                    this.value_ = Collections.unmodifiableList(this.value_);
                    this.bitField0_ &= -3;
                }
                result.value_ = this.value_;
                if ((this.bitField0_ & 4) == 4) {
                    this.property_ = Collections.unmodifiableList(this.property_);
                    this.bitField0_ &= -5;
                }
                result.property_ = this.property_;
                if ((this.bitField0_ & 8) == 8) {
                    this.macro_ = Collections.unmodifiableList(this.macro_);
                    this.bitField0_ &= -9;
                }
                result.macro_ = this.macro_;
                if ((this.bitField0_ & 16) == 16) {
                    this.tag_ = Collections.unmodifiableList(this.tag_);
                    this.bitField0_ &= -17;
                }
                result.tag_ = this.tag_;
                if ((this.bitField0_ & 32) == 32) {
                    this.predicate_ = Collections.unmodifiableList(this.predicate_);
                    this.bitField0_ &= -33;
                }
                result.predicate_ = this.predicate_;
                if ((this.bitField0_ & 64) == 64) {
                    this.rule_ = Collections.unmodifiableList(this.rule_);
                    this.bitField0_ &= -65;
                }
                result.rule_ = this.rule_;
                if ((from_bitField0_ & 128) == 128) {
                    to_bitField0_ = 0 | 1;
                }
                result.previewAuthCode_ = this.previewAuthCode_;
                if ((from_bitField0_ & 256) == 256) {
                    to_bitField0_ |= 2;
                }
                result.malwareScanAuthCode_ = this.malwareScanAuthCode_;
                if ((from_bitField0_ & 512) == 512) {
                    to_bitField0_ |= 4;
                }
                result.templateVersionSet_ = this.templateVersionSet_;
                if ((from_bitField0_ & 1024) == 1024) {
                    to_bitField0_ |= 8;
                }
                result.version_ = this.version_;
                if ((from_bitField0_ & 2048) == 2048) {
                    to_bitField0_ |= 16;
                }
                result.liveJsCacheOption_ = this.liveJsCacheOption_;
                if ((from_bitField0_ & 4096) == 4096) {
                    to_bitField0_ |= 32;
                }
                result.reportingSampleRate_ = this.reportingSampleRate_;
                if ((from_bitField0_ & 8192) == 8192) {
                    to_bitField0_ |= 64;
                }
                result.enableAutoEventTracking_ = this.enableAutoEventTracking_;
                if ((this.bitField0_ & 16384) == 16384) {
                    this.usageContext_ = new UnmodifiableLazyStringList(this.usageContext_);
                    this.bitField0_ &= -16385;
                }
                result.usageContext_ = this.usageContext_;
                if ((from_bitField0_ & 32768) == 32768) {
                    to_bitField0_ |= 128;
                }
                result.resourceFormatVersion_ = this.resourceFormatVersion_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(Resource other) {
                if (other != Resource.getDefaultInstance()) {
                    if (!other.key_.isEmpty()) {
                        if (this.key_.isEmpty()) {
                            this.key_ = other.key_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureKeyIsMutable();
                            this.key_.addAll(other.key_);
                        }
                    }
                    if (!other.value_.isEmpty()) {
                        if (this.value_.isEmpty()) {
                            this.value_ = other.value_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureValueIsMutable();
                            this.value_.addAll(other.value_);
                        }
                    }
                    if (!other.property_.isEmpty()) {
                        if (this.property_.isEmpty()) {
                            this.property_ = other.property_;
                            this.bitField0_ &= -5;
                        } else {
                            ensurePropertyIsMutable();
                            this.property_.addAll(other.property_);
                        }
                    }
                    if (!other.macro_.isEmpty()) {
                        if (this.macro_.isEmpty()) {
                            this.macro_ = other.macro_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureMacroIsMutable();
                            this.macro_.addAll(other.macro_);
                        }
                    }
                    if (!other.tag_.isEmpty()) {
                        if (this.tag_.isEmpty()) {
                            this.tag_ = other.tag_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureTagIsMutable();
                            this.tag_.addAll(other.tag_);
                        }
                    }
                    if (!other.predicate_.isEmpty()) {
                        if (this.predicate_.isEmpty()) {
                            this.predicate_ = other.predicate_;
                            this.bitField0_ &= -33;
                        } else {
                            ensurePredicateIsMutable();
                            this.predicate_.addAll(other.predicate_);
                        }
                    }
                    if (!other.rule_.isEmpty()) {
                        if (this.rule_.isEmpty()) {
                            this.rule_ = other.rule_;
                            this.bitField0_ &= -65;
                        } else {
                            ensureRuleIsMutable();
                            this.rule_.addAll(other.rule_);
                        }
                    }
                    if (other.hasPreviewAuthCode()) {
                        this.bitField0_ |= 128;
                        this.previewAuthCode_ = other.previewAuthCode_;
                    }
                    if (other.hasMalwareScanAuthCode()) {
                        this.bitField0_ |= 256;
                        this.malwareScanAuthCode_ = other.malwareScanAuthCode_;
                    }
                    if (other.hasTemplateVersionSet()) {
                        this.bitField0_ |= 512;
                        this.templateVersionSet_ = other.templateVersionSet_;
                    }
                    if (other.hasVersion()) {
                        this.bitField0_ |= 1024;
                        this.version_ = other.version_;
                    }
                    if (other.hasLiveJsCacheOption()) {
                        mergeLiveJsCacheOption(other.getLiveJsCacheOption());
                    }
                    if (other.hasReportingSampleRate()) {
                        setReportingSampleRate(other.getReportingSampleRate());
                    }
                    if (other.hasEnableAutoEventTracking()) {
                        setEnableAutoEventTracking(other.getEnableAutoEventTracking());
                    }
                    if (!other.usageContext_.isEmpty()) {
                        if (this.usageContext_.isEmpty()) {
                            this.usageContext_ = other.usageContext_;
                            this.bitField0_ &= -16385;
                        } else {
                            ensureUsageContextIsMutable();
                            this.usageContext_.addAll(other.usageContext_);
                        }
                    }
                    if (other.hasResourceFormatVersion()) {
                        setResourceFormatVersion(other.getResourceFormatVersion());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                for (int i = 0; i < getValueCount(); i++) {
                    if (!getValue(i).isInitialized()) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getPropertyCount(); i2++) {
                    if (!getProperty(i2).isInitialized()) {
                        return false;
                    }
                }
                for (int i3 = 0; i3 < getMacroCount(); i3++) {
                    if (!getMacro(i3).isInitialized()) {
                        return false;
                    }
                }
                for (int i4 = 0; i4 < getTagCount(); i4++) {
                    if (!getTag(i4).isInitialized()) {
                        return false;
                    }
                }
                for (int i5 = 0; i5 < getPredicateCount(); i5++) {
                    if (!getPredicate(i5).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Resource parsedMessage = null;
                try {
                    try {
                        parsedMessage = Resource.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            private void ensureKeyIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.key_ = new LazyStringArrayList(this.key_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<String> getKeyList() {
                return Collections.unmodifiableList(this.key_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getKeyCount() {
                return this.key_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public String getKey(int index) {
                return this.key_.get(index);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public ByteString getKeyBytes(int index) {
                return this.key_.getByteString(index);
            }

            public Builder setKey(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureKeyIsMutable();
                this.key_.set(index, value);
                return this;
            }

            public Builder addKey(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureKeyIsMutable();
                this.key_.add(value);
                return this;
            }

            public Builder addAllKey(Iterable<String> values) {
                ensureKeyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.key_);
                return this;
            }

            public Builder clearKey() {
                this.key_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            public Builder addKeyBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureKeyIsMutable();
                this.key_.add(value);
                return this;
            }

            private void ensureValueIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.value_ = new ArrayList(this.value_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<TypeSystem.Value> getValueList() {
                return Collections.unmodifiableList(this.value_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getValueCount() {
                return this.value_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public TypeSystem.Value getValue(int index) {
                return this.value_.get(index);
            }

            public Builder setValue(int index, TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.set(index, value);
                return this;
            }

            public Builder setValue(int index, TypeSystem.Value.Builder builderForValue) {
                ensureValueIsMutable();
                this.value_.set(index, builderForValue.build());
                return this;
            }

            public Builder addValue(TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.add(value);
                return this;
            }

            public Builder addValue(int index, TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.add(index, value);
                return this;
            }

            public Builder addValue(TypeSystem.Value.Builder builderForValue) {
                ensureValueIsMutable();
                this.value_.add(builderForValue.build());
                return this;
            }

            public Builder addValue(int index, TypeSystem.Value.Builder builderForValue) {
                ensureValueIsMutable();
                this.value_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllValue(Iterable<? extends TypeSystem.Value> values) {
                ensureValueIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.value_);
                return this;
            }

            public Builder clearValue() {
                this.value_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder removeValue(int index) {
                ensureValueIsMutable();
                this.value_.remove(index);
                return this;
            }

            private void ensurePropertyIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.property_ = new ArrayList(this.property_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<Property> getPropertyList() {
                return Collections.unmodifiableList(this.property_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getPropertyCount() {
                return this.property_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public Property getProperty(int index) {
                return this.property_.get(index);
            }

            public Builder setProperty(int index, Property value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertyIsMutable();
                this.property_.set(index, value);
                return this;
            }

            public Builder setProperty(int index, Property.Builder builderForValue) {
                ensurePropertyIsMutable();
                this.property_.set(index, builderForValue.build());
                return this;
            }

            public Builder addProperty(Property value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertyIsMutable();
                this.property_.add(value);
                return this;
            }

            public Builder addProperty(int index, Property value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertyIsMutable();
                this.property_.add(index, value);
                return this;
            }

            public Builder addProperty(Property.Builder builderForValue) {
                ensurePropertyIsMutable();
                this.property_.add(builderForValue.build());
                return this;
            }

            public Builder addProperty(int index, Property.Builder builderForValue) {
                ensurePropertyIsMutable();
                this.property_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllProperty(Iterable<? extends Property> values) {
                ensurePropertyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.property_);
                return this;
            }

            public Builder clearProperty() {
                this.property_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            public Builder removeProperty(int index) {
                ensurePropertyIsMutable();
                this.property_.remove(index);
                return this;
            }

            private void ensureMacroIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.macro_ = new ArrayList(this.macro_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<FunctionCall> getMacroList() {
                return Collections.unmodifiableList(this.macro_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getMacroCount() {
                return this.macro_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public FunctionCall getMacro(int index) {
                return this.macro_.get(index);
            }

            public Builder setMacro(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMacroIsMutable();
                this.macro_.set(index, value);
                return this;
            }

            public Builder setMacro(int index, FunctionCall.Builder builderForValue) {
                ensureMacroIsMutable();
                this.macro_.set(index, builderForValue.build());
                return this;
            }

            public Builder addMacro(FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMacroIsMutable();
                this.macro_.add(value);
                return this;
            }

            public Builder addMacro(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMacroIsMutable();
                this.macro_.add(index, value);
                return this;
            }

            public Builder addMacro(FunctionCall.Builder builderForValue) {
                ensureMacroIsMutable();
                this.macro_.add(builderForValue.build());
                return this;
            }

            public Builder addMacro(int index, FunctionCall.Builder builderForValue) {
                ensureMacroIsMutable();
                this.macro_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllMacro(Iterable<? extends FunctionCall> values) {
                ensureMacroIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.macro_);
                return this;
            }

            public Builder clearMacro() {
                this.macro_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            public Builder removeMacro(int index) {
                ensureMacroIsMutable();
                this.macro_.remove(index);
                return this;
            }

            private void ensureTagIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.tag_ = new ArrayList(this.tag_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<FunctionCall> getTagList() {
                return Collections.unmodifiableList(this.tag_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getTagCount() {
                return this.tag_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public FunctionCall getTag(int index) {
                return this.tag_.get(index);
            }

            public Builder setTag(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTagIsMutable();
                this.tag_.set(index, value);
                return this;
            }

            public Builder setTag(int index, FunctionCall.Builder builderForValue) {
                ensureTagIsMutable();
                this.tag_.set(index, builderForValue.build());
                return this;
            }

            public Builder addTag(FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTagIsMutable();
                this.tag_.add(value);
                return this;
            }

            public Builder addTag(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTagIsMutable();
                this.tag_.add(index, value);
                return this;
            }

            public Builder addTag(FunctionCall.Builder builderForValue) {
                ensureTagIsMutable();
                this.tag_.add(builderForValue.build());
                return this;
            }

            public Builder addTag(int index, FunctionCall.Builder builderForValue) {
                ensureTagIsMutable();
                this.tag_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllTag(Iterable<? extends FunctionCall> values) {
                ensureTagIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.tag_);
                return this;
            }

            public Builder clearTag() {
                this.tag_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            public Builder removeTag(int index) {
                ensureTagIsMutable();
                this.tag_.remove(index);
                return this;
            }

            private void ensurePredicateIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.predicate_ = new ArrayList(this.predicate_);
                    this.bitField0_ |= 32;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<FunctionCall> getPredicateList() {
                return Collections.unmodifiableList(this.predicate_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getPredicateCount() {
                return this.predicate_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public FunctionCall getPredicate(int index) {
                return this.predicate_.get(index);
            }

            public Builder setPredicate(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePredicateIsMutable();
                this.predicate_.set(index, value);
                return this;
            }

            public Builder setPredicate(int index, FunctionCall.Builder builderForValue) {
                ensurePredicateIsMutable();
                this.predicate_.set(index, builderForValue.build());
                return this;
            }

            public Builder addPredicate(FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePredicateIsMutable();
                this.predicate_.add(value);
                return this;
            }

            public Builder addPredicate(int index, FunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePredicateIsMutable();
                this.predicate_.add(index, value);
                return this;
            }

            public Builder addPredicate(FunctionCall.Builder builderForValue) {
                ensurePredicateIsMutable();
                this.predicate_.add(builderForValue.build());
                return this;
            }

            public Builder addPredicate(int index, FunctionCall.Builder builderForValue) {
                ensurePredicateIsMutable();
                this.predicate_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllPredicate(Iterable<? extends FunctionCall> values) {
                ensurePredicateIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.predicate_);
                return this;
            }

            public Builder clearPredicate() {
                this.predicate_ = Collections.emptyList();
                this.bitField0_ &= -33;
                return this;
            }

            public Builder removePredicate(int index) {
                ensurePredicateIsMutable();
                this.predicate_.remove(index);
                return this;
            }

            private void ensureRuleIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.rule_ = new ArrayList(this.rule_);
                    this.bitField0_ |= 64;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<Rule> getRuleList() {
                return Collections.unmodifiableList(this.rule_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getRuleCount() {
                return this.rule_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public Rule getRule(int index) {
                return this.rule_.get(index);
            }

            public Builder setRule(int index, Rule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRuleIsMutable();
                this.rule_.set(index, value);
                return this;
            }

            public Builder setRule(int index, Rule.Builder builderForValue) {
                ensureRuleIsMutable();
                this.rule_.set(index, builderForValue.build());
                return this;
            }

            public Builder addRule(Rule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRuleIsMutable();
                this.rule_.add(value);
                return this;
            }

            public Builder addRule(int index, Rule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRuleIsMutable();
                this.rule_.add(index, value);
                return this;
            }

            public Builder addRule(Rule.Builder builderForValue) {
                ensureRuleIsMutable();
                this.rule_.add(builderForValue.build());
                return this;
            }

            public Builder addRule(int index, Rule.Builder builderForValue) {
                ensureRuleIsMutable();
                this.rule_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllRule(Iterable<? extends Rule> values) {
                ensureRuleIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.rule_);
                return this;
            }

            public Builder clearRule() {
                this.rule_ = Collections.emptyList();
                this.bitField0_ &= -65;
                return this;
            }

            public Builder removeRule(int index) {
                ensureRuleIsMutable();
                this.rule_.remove(index);
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasPreviewAuthCode() {
                return (this.bitField0_ & 128) == 128;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public String getPreviewAuthCode() {
                Object ref = this.previewAuthCode_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.previewAuthCode_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public ByteString getPreviewAuthCodeBytes() {
                Object ref = this.previewAuthCode_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.previewAuthCode_ = b;
                return b;
            }

            public Builder setPreviewAuthCode(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 128;
                this.previewAuthCode_ = value;
                return this;
            }

            public Builder clearPreviewAuthCode() {
                this.bitField0_ &= -129;
                this.previewAuthCode_ = Resource.getDefaultInstance().getPreviewAuthCode();
                return this;
            }

            public Builder setPreviewAuthCodeBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 128;
                this.previewAuthCode_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasMalwareScanAuthCode() {
                return (this.bitField0_ & 256) == 256;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public String getMalwareScanAuthCode() {
                Object ref = this.malwareScanAuthCode_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.malwareScanAuthCode_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public ByteString getMalwareScanAuthCodeBytes() {
                Object ref = this.malwareScanAuthCode_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.malwareScanAuthCode_ = b;
                return b;
            }

            public Builder setMalwareScanAuthCode(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 256;
                this.malwareScanAuthCode_ = value;
                return this;
            }

            public Builder clearMalwareScanAuthCode() {
                this.bitField0_ &= -257;
                this.malwareScanAuthCode_ = Resource.getDefaultInstance().getMalwareScanAuthCode();
                return this;
            }

            public Builder setMalwareScanAuthCodeBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 256;
                this.malwareScanAuthCode_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasTemplateVersionSet() {
                return (this.bitField0_ & 512) == 512;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public String getTemplateVersionSet() {
                Object ref = this.templateVersionSet_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.templateVersionSet_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public ByteString getTemplateVersionSetBytes() {
                Object ref = this.templateVersionSet_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.templateVersionSet_ = b;
                return b;
            }

            public Builder setTemplateVersionSet(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 512;
                this.templateVersionSet_ = value;
                return this;
            }

            public Builder clearTemplateVersionSet() {
                this.bitField0_ &= -513;
                this.templateVersionSet_ = Resource.getDefaultInstance().getTemplateVersionSet();
                return this;
            }

            public Builder setTemplateVersionSetBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 512;
                this.templateVersionSet_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasVersion() {
                return (this.bitField0_ & 1024) == 1024;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public String getVersion() {
                Object ref = this.version_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.version_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public ByteString getVersionBytes() {
                Object ref = this.version_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.version_ = b;
                return b;
            }

            public Builder setVersion(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1024;
                this.version_ = value;
                return this;
            }

            public Builder clearVersion() {
                this.bitField0_ &= -1025;
                this.version_ = Resource.getDefaultInstance().getVersion();
                return this;
            }

            public Builder setVersionBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1024;
                this.version_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasLiveJsCacheOption() {
                return (this.bitField0_ & 2048) == 2048;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public CacheOption getLiveJsCacheOption() {
                return this.liveJsCacheOption_;
            }

            public Builder setLiveJsCacheOption(CacheOption value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.liveJsCacheOption_ = value;
                this.bitField0_ |= 2048;
                return this;
            }

            public Builder setLiveJsCacheOption(CacheOption.Builder builderForValue) {
                this.liveJsCacheOption_ = builderForValue.build();
                this.bitField0_ |= 2048;
                return this;
            }

            public Builder mergeLiveJsCacheOption(CacheOption value) {
                if ((this.bitField0_ & 2048) == 2048 && this.liveJsCacheOption_ != CacheOption.getDefaultInstance()) {
                    this.liveJsCacheOption_ = CacheOption.newBuilder(this.liveJsCacheOption_).mergeFrom(value).buildPartial();
                } else {
                    this.liveJsCacheOption_ = value;
                }
                this.bitField0_ |= 2048;
                return this;
            }

            public Builder clearLiveJsCacheOption() {
                this.liveJsCacheOption_ = CacheOption.getDefaultInstance();
                this.bitField0_ &= -2049;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasReportingSampleRate() {
                return (this.bitField0_ & 4096) == 4096;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public float getReportingSampleRate() {
                return this.reportingSampleRate_;
            }

            public Builder setReportingSampleRate(float value) {
                this.bitField0_ |= 4096;
                this.reportingSampleRate_ = value;
                return this;
            }

            public Builder clearReportingSampleRate() {
                this.bitField0_ &= -4097;
                this.reportingSampleRate_ = 0.0f;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasEnableAutoEventTracking() {
                return (this.bitField0_ & 8192) == 8192;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean getEnableAutoEventTracking() {
                return this.enableAutoEventTracking_;
            }

            public Builder setEnableAutoEventTracking(boolean value) {
                this.bitField0_ |= 8192;
                this.enableAutoEventTracking_ = value;
                return this;
            }

            public Builder clearEnableAutoEventTracking() {
                this.bitField0_ &= -8193;
                this.enableAutoEventTracking_ = false;
                return this;
            }

            private void ensureUsageContextIsMutable() {
                if ((this.bitField0_ & 16384) != 16384) {
                    this.usageContext_ = new LazyStringArrayList(this.usageContext_);
                    this.bitField0_ |= 16384;
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public List<String> getUsageContextList() {
                return Collections.unmodifiableList(this.usageContext_);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getUsageContextCount() {
                return this.usageContext_.size();
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public String getUsageContext(int index) {
                return this.usageContext_.get(index);
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public ByteString getUsageContextBytes(int index) {
                return this.usageContext_.getByteString(index);
            }

            public Builder setUsageContext(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsageContextIsMutable();
                this.usageContext_.set(index, value);
                return this;
            }

            public Builder addUsageContext(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsageContextIsMutable();
                this.usageContext_.add(value);
                return this;
            }

            public Builder addAllUsageContext(Iterable<String> values) {
                ensureUsageContextIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.usageContext_);
                return this;
            }

            public Builder clearUsageContext() {
                this.usageContext_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -16385;
                return this;
            }

            public Builder addUsageContextBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsageContextIsMutable();
                this.usageContext_.add(value);
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public boolean hasResourceFormatVersion() {
                return (this.bitField0_ & 32768) == 32768;
            }

            @Override // com.google.analytics.containertag.proto.Serving.ResourceOrBuilder
            public int getResourceFormatVersion() {
                return this.resourceFormatVersion_;
            }

            public Builder setResourceFormatVersion(int value) {
                this.bitField0_ |= 32768;
                this.resourceFormatVersion_ = value;
                return this;
            }

            public Builder clearResourceFormatVersion() {
                this.bitField0_ &= -32769;
                this.resourceFormatVersion_ = 0;
                return this;
            }
        }
    }

    public static final class OptionalResource extends GeneratedMessageLite implements OptionalResourceOrBuilder {
        public static final int RESOURCE_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Resource resource_;
        private final ByteString unknownFields;
        public static Parser<OptionalResource> PARSER = new AbstractParser<OptionalResource>() { // from class: com.google.analytics.containertag.proto.Serving.OptionalResource.1
            @Override // com.google.tagmanager.protobuf.Parser
            public OptionalResource parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new OptionalResource(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final OptionalResource defaultInstance = new OptionalResource(true);

        private OptionalResource(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private OptionalResource(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static OptionalResource getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public OptionalResource getDefaultInstanceForType() {
            return defaultInstance;
        }

        private OptionalResource(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            ByteString.Output unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 18:
                                Resource.Builder subBuilder = (this.bitField0_ & 1) == 1 ? this.resource_.toBuilder() : null;
                                this.resource_ = (Resource) input.readMessage(Resource.PARSER, extensionRegistry);
                                if (subBuilder != null) {
                                    subBuilder.mergeFrom(this.resource_);
                                    this.resource_ = subBuilder.buildPartial();
                                }
                                this.bitField0_ |= 1;
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                    done = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        try {
                            unknownFieldsCodedOutput.flush();
                        } catch (IOException e) {
                        } finally {
                        }
                        makeExtensionsImmutable();
                        throw th;
                    }
                } catch (InvalidProtocolBufferException e2) {
                    throw e2.setUnfinishedMessage(this);
                } catch (IOException e3) {
                    throw new InvalidProtocolBufferException(e3.getMessage()).setUnfinishedMessage(this);
                }
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<OptionalResource> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Serving.OptionalResourceOrBuilder
        public boolean hasResource() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Serving.OptionalResourceOrBuilder
        public Resource getResource() {
            return this.resource_;
        }

        private void initFields() {
            this.resource_ = Resource.getDefaultInstance();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (hasResource() && !getResource().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(2, this.resource_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = ((this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeMessageSize(2, this.resource_) : 0) + this.unknownFields.size();
            this.memoizedSerializedSize = size2;
            return size2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OptionalResource)) {
                return super.equals(obj);
            }
            OptionalResource other = (OptionalResource) obj;
            boolean result = 1 != 0 && hasResource() == other.hasResource();
            if (hasResource()) {
                result = result && getResource().equals(other.getResource());
            }
            return result;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = OptionalResource.class.hashCode() + 779;
            if (hasResource()) {
                hash = (((hash * 37) + 2) * 53) + getResource().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableServing$OptionalResource");
            }
            return mutableDefault;
        }

        public static OptionalResource parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static OptionalResource parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static OptionalResource parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static OptionalResource parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static OptionalResource parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static OptionalResource parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static OptionalResource parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static OptionalResource parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static OptionalResource parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static OptionalResource parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(OptionalResource prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<OptionalResource, Builder> implements OptionalResourceOrBuilder {
            private int bitField0_;
            private Resource resource_ = Resource.getDefaultInstance();

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.resource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public OptionalResource getDefaultInstanceForType() {
                return OptionalResource.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public OptionalResource build() {
                OptionalResource result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public OptionalResource buildPartial() {
                OptionalResource result = new OptionalResource(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.resource_ = this.resource_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(OptionalResource other) {
                if (other != OptionalResource.getDefaultInstance()) {
                    if (other.hasResource()) {
                        mergeResource(other.getResource());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return !hasResource() || getResource().isInitialized();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                OptionalResource parsedMessage = null;
                try {
                    try {
                        parsedMessage = OptionalResource.PARSER.parsePartialFrom(input, extensionRegistry);
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        throw e;
                    }
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            @Override // com.google.analytics.containertag.proto.Serving.OptionalResourceOrBuilder
            public boolean hasResource() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Serving.OptionalResourceOrBuilder
            public Resource getResource() {
                return this.resource_;
            }

            public Builder setResource(Resource value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.resource_ = value;
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setResource(Resource.Builder builderForValue) {
                this.resource_ = builderForValue.build();
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeResource(Resource value) {
                if ((this.bitField0_ & 1) == 1 && this.resource_ != Resource.getDefaultInstance()) {
                    this.resource_ = Resource.newBuilder(this.resource_).mergeFrom(value).buildPartial();
                } else {
                    this.resource_ = value;
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearResource() {
                this.resource_ = Resource.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }
        }
    }
}
