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
import com.google.tagmanager.protobuf.MessageLiteOrBuilder;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import com.google.tagmanager.protobuf.WireFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
public final class Debug {

    public interface DataLayerEventEvaluationInfoOrBuilder extends MessageLiteOrBuilder {
        ResolvedFunctionCall getResults(int i);

        int getResultsCount();

        List<ResolvedFunctionCall> getResultsList();

        RuleEvaluationStepInfo getRulesEvaluation();

        boolean hasRulesEvaluation();
    }

    public interface DebugEventsOrBuilder extends MessageLiteOrBuilder {
        EventInfo getEvent(int i);

        int getEventCount();

        List<EventInfo> getEventList();
    }

    public interface EventInfoOrBuilder extends MessageLiteOrBuilder {
        String getContainerId();

        ByteString getContainerIdBytes();

        String getContainerVersion();

        ByteString getContainerVersionBytes();

        DataLayerEventEvaluationInfo getDataLayerEventResult();

        EventInfo.EventType getEventType();

        String getKey();

        ByteString getKeyBytes();

        MacroEvaluationInfo getMacroResult();

        boolean hasContainerId();

        boolean hasContainerVersion();

        boolean hasDataLayerEventResult();

        boolean hasEventType();

        boolean hasKey();

        boolean hasMacroResult();
    }

    public interface MacroEvaluationInfoOrBuilder extends MessageLiteOrBuilder {
        ResolvedFunctionCall getResult();

        RuleEvaluationStepInfo getRulesEvaluation();

        boolean hasResult();

        boolean hasRulesEvaluation();
    }

    public interface ResolvedFunctionCallOrBuilder extends MessageLiteOrBuilder {
        String getAssociatedRuleName();

        ByteString getAssociatedRuleNameBytes();

        ResolvedProperty getProperties(int i);

        int getPropertiesCount();

        List<ResolvedProperty> getPropertiesList();

        TypeSystem.Value getResult();

        boolean hasAssociatedRuleName();

        boolean hasResult();
    }

    public interface ResolvedPropertyOrBuilder extends MessageLiteOrBuilder {
        String getKey();

        ByteString getKeyBytes();

        TypeSystem.Value getValue();

        boolean hasKey();

        boolean hasValue();
    }

    public interface ResolvedRuleOrBuilder extends MessageLiteOrBuilder {
        ResolvedFunctionCall getAddMacros(int i);

        int getAddMacrosCount();

        List<ResolvedFunctionCall> getAddMacrosList();

        ResolvedFunctionCall getAddTags(int i);

        int getAddTagsCount();

        List<ResolvedFunctionCall> getAddTagsList();

        ResolvedFunctionCall getNegativePredicates(int i);

        int getNegativePredicatesCount();

        List<ResolvedFunctionCall> getNegativePredicatesList();

        ResolvedFunctionCall getPositivePredicates(int i);

        int getPositivePredicatesCount();

        List<ResolvedFunctionCall> getPositivePredicatesList();

        ResolvedFunctionCall getRemoveMacros(int i);

        int getRemoveMacrosCount();

        List<ResolvedFunctionCall> getRemoveMacrosList();

        ResolvedFunctionCall getRemoveTags(int i);

        int getRemoveTagsCount();

        List<ResolvedFunctionCall> getRemoveTagsList();

        TypeSystem.Value getResult();

        boolean hasResult();
    }

    public interface RuleEvaluationStepInfoOrBuilder extends MessageLiteOrBuilder {
        ResolvedFunctionCall getEnabledFunctions(int i);

        int getEnabledFunctionsCount();

        List<ResolvedFunctionCall> getEnabledFunctionsList();

        ResolvedRule getRules(int i);

        int getRulesCount();

        List<ResolvedRule> getRulesList();
    }

    private Debug() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
        registry.add(MacroEvaluationInfo.macro);
    }

    public static final class DebugEvents extends GeneratedMessageLite implements DebugEventsOrBuilder {
        public static final int EVENT_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private List<EventInfo> event_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private final ByteString unknownFields;
        public static Parser<DebugEvents> PARSER = new AbstractParser<DebugEvents>() { // from class: com.google.analytics.containertag.proto.Debug.DebugEvents.1
            @Override // com.google.tagmanager.protobuf.Parser
            public DebugEvents parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new DebugEvents(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final DebugEvents defaultInstance = new DebugEvents(true);

        private DebugEvents(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private DebugEvents(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static DebugEvents getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public DebugEvents getDefaultInstanceForType() {
            return defaultInstance;
        }

        private DebugEvents(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
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
                                    if ((i & 1) != 1) {
                                        this.event_ = new ArrayList();
                                        i |= 1;
                                    }
                                    this.event_.add((EventInfo) codedInputStream.readMessage(EventInfo.PARSER, extensionRegistryLite));
                                    break;
                                default:
                                    if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                        z = true;
                                    }
                                    break;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                    }
                } catch (Throwable th) {
                    if ((i & 1) == 1) {
                        this.event_ = Collections.unmodifiableList(this.event_);
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
                this.event_ = Collections.unmodifiableList(this.event_);
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
        public Parser<DebugEvents> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.DebugEventsOrBuilder
        public List<EventInfo> getEventList() {
            return this.event_;
        }

        public List<? extends EventInfoOrBuilder> getEventOrBuilderList() {
            return this.event_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.DebugEventsOrBuilder
        public int getEventCount() {
            return this.event_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.DebugEventsOrBuilder
        public EventInfo getEvent(int index) {
            return this.event_.get(index);
        }

        public EventInfoOrBuilder getEventOrBuilder(int index) {
            return this.event_.get(index);
        }

        private void initFields() {
            this.event_ = Collections.emptyList();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            for (int i = 0; i < getEventCount(); i++) {
                if (!getEvent(i).isInitialized()) {
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
            for (int i = 0; i < this.event_.size(); i++) {
                output.writeMessage(1, this.event_.get(i));
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            for (int i = 0; i < this.event_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.event_.get(i));
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
            if (!(obj instanceof DebugEvents)) {
                return super.equals(obj);
            }
            DebugEvents other = (DebugEvents) obj;
            boolean result = 1 != 0 && getEventList().equals(other.getEventList());
            return result;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = DebugEvents.class.hashCode() + 779;
            if (getEventCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getEventList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$DebugEvents");
            }
            return mutableDefault;
        }

        public static DebugEvents parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DebugEvents parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DebugEvents parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DebugEvents parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DebugEvents parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static DebugEvents parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static DebugEvents parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static DebugEvents parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static DebugEvents parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static DebugEvents parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(DebugEvents prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<DebugEvents, Builder> implements DebugEventsOrBuilder {
            private int bitField0_;
            private List<EventInfo> event_ = Collections.emptyList();

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
                this.event_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public DebugEvents getDefaultInstanceForType() {
                return DebugEvents.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public DebugEvents build() {
                DebugEvents result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public DebugEvents buildPartial() {
                DebugEvents result = new DebugEvents(this);
                int i = this.bitField0_;
                if ((this.bitField0_ & 1) == 1) {
                    this.event_ = Collections.unmodifiableList(this.event_);
                    this.bitField0_ &= -2;
                }
                result.event_ = this.event_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(DebugEvents other) {
                if (other != DebugEvents.getDefaultInstance()) {
                    if (!other.event_.isEmpty()) {
                        if (this.event_.isEmpty()) {
                            this.event_ = other.event_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureEventIsMutable();
                            this.event_.addAll(other.event_);
                        }
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                for (int i = 0; i < getEventCount(); i++) {
                    if (!getEvent(i).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                DebugEvents parsedMessage = null;
                try {
                    try {
                        parsedMessage = DebugEvents.PARSER.parsePartialFrom(input, extensionRegistry);
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

            private void ensureEventIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.event_ = new ArrayList(this.event_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.DebugEventsOrBuilder
            public List<EventInfo> getEventList() {
                return Collections.unmodifiableList(this.event_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.DebugEventsOrBuilder
            public int getEventCount() {
                return this.event_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.DebugEventsOrBuilder
            public EventInfo getEvent(int index) {
                return this.event_.get(index);
            }

            public Builder setEvent(int index, EventInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEventIsMutable();
                this.event_.set(index, value);
                return this;
            }

            public Builder setEvent(int index, EventInfo.Builder builderForValue) {
                ensureEventIsMutable();
                this.event_.set(index, builderForValue.build());
                return this;
            }

            public Builder addEvent(EventInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEventIsMutable();
                this.event_.add(value);
                return this;
            }

            public Builder addEvent(int index, EventInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEventIsMutable();
                this.event_.add(index, value);
                return this;
            }

            public Builder addEvent(EventInfo.Builder builderForValue) {
                ensureEventIsMutable();
                this.event_.add(builderForValue.build());
                return this;
            }

            public Builder addEvent(int index, EventInfo.Builder builderForValue) {
                ensureEventIsMutable();
                this.event_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllEvent(Iterable<? extends EventInfo> values) {
                ensureEventIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.event_);
                return this;
            }

            public Builder clearEvent() {
                this.event_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removeEvent(int index) {
                ensureEventIsMutable();
                this.event_.remove(index);
                return this;
            }
        }
    }

    public static final class EventInfo extends GeneratedMessageLite implements EventInfoOrBuilder {
        public static final int CONTAINER_ID_FIELD_NUMBER = 3;
        public static final int CONTAINER_VERSION_FIELD_NUMBER = 2;
        public static final int DATA_LAYER_EVENT_RESULT_FIELD_NUMBER = 7;
        public static final int EVENT_TYPE_FIELD_NUMBER = 1;
        public static final int KEY_FIELD_NUMBER = 4;
        public static final int MACRO_RESULT_FIELD_NUMBER = 6;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private Object containerId_;
        private Object containerVersion_;
        private DataLayerEventEvaluationInfo dataLayerEventResult_;
        private EventType eventType_;
        private Object key_;
        private MacroEvaluationInfo macroResult_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private final ByteString unknownFields;
        public static Parser<EventInfo> PARSER = new AbstractParser<EventInfo>() { // from class: com.google.analytics.containertag.proto.Debug.EventInfo.1
            @Override // com.google.tagmanager.protobuf.Parser
            public EventInfo parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new EventInfo(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final EventInfo defaultInstance = new EventInfo(true);

        private EventInfo(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private EventInfo(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static EventInfo getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public EventInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        private EventInfo(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                EventType value = EventType.valueOf(rawValue);
                                if (value == null) {
                                    unknownFieldsCodedOutput.writeRawVarint32(tag);
                                    unknownFieldsCodedOutput.writeRawVarint32(rawValue);
                                } else {
                                    this.bitField0_ |= 1;
                                    this.eventType_ = value;
                                }
                                break;
                            case 18:
                                ByteString bs = input.readBytes();
                                this.bitField0_ |= 2;
                                this.containerVersion_ = bs;
                                break;
                            case 26:
                                ByteString bs2 = input.readBytes();
                                this.bitField0_ |= 4;
                                this.containerId_ = bs2;
                                break;
                            case 34:
                                ByteString bs3 = input.readBytes();
                                this.bitField0_ |= 8;
                                this.key_ = bs3;
                                break;
                            case 50:
                                MacroEvaluationInfo.Builder subBuilder = (this.bitField0_ & 16) == 16 ? this.macroResult_.toBuilder() : null;
                                this.macroResult_ = (MacroEvaluationInfo) input.readMessage(MacroEvaluationInfo.PARSER, extensionRegistry);
                                if (subBuilder != null) {
                                    subBuilder.mergeFrom(this.macroResult_);
                                    this.macroResult_ = subBuilder.buildPartial();
                                }
                                this.bitField0_ |= 16;
                                break;
                            case 58:
                                DataLayerEventEvaluationInfo.Builder subBuilder2 = (this.bitField0_ & 32) == 32 ? this.dataLayerEventResult_.toBuilder() : null;
                                this.dataLayerEventResult_ = (DataLayerEventEvaluationInfo) input.readMessage(DataLayerEventEvaluationInfo.PARSER, extensionRegistry);
                                if (subBuilder2 != null) {
                                    subBuilder2.mergeFrom(this.dataLayerEventResult_);
                                    this.dataLayerEventResult_ = subBuilder2.buildPartial();
                                }
                                this.bitField0_ |= 32;
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
        public Parser<EventInfo> getParserForType() {
            return PARSER;
        }

        public enum EventType implements Internal.EnumLite {
            DATA_LAYER_EVENT(0, 1),
            MACRO_REFERENCE(1, 2);

            public static final int DATA_LAYER_EVENT_VALUE = 1;
            public static final int MACRO_REFERENCE_VALUE = 2;
            private static Internal.EnumLiteMap<EventType> internalValueMap = new Internal.EnumLiteMap<EventType>() { // from class: com.google.analytics.containertag.proto.Debug.EventInfo.EventType.1
                @Override // com.google.tagmanager.protobuf.Internal.EnumLiteMap
                public EventType findValueByNumber(int number) {
                    return EventType.valueOf(number);
                }
            };
            private final int value;

            @Override // com.google.tagmanager.protobuf.Internal.EnumLite
            public final int getNumber() {
                return this.value;
            }

            public static EventType valueOf(int value) {
                switch (value) {
                    case 1:
                        return DATA_LAYER_EVENT;
                    case 2:
                        return MACRO_REFERENCE;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<EventType> internalGetValueMap() {
                return internalValueMap;
            }

            EventType(int index, int value) {
                this.value = value;
            }
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public boolean hasEventType() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public EventType getEventType() {
            return this.eventType_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public boolean hasContainerVersion() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public String getContainerVersion() {
            Object ref = this.containerVersion_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.containerVersion_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public ByteString getContainerVersionBytes() {
            Object ref = this.containerVersion_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.containerVersion_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public boolean hasContainerId() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
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

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public ByteString getContainerIdBytes() {
            Object ref = this.containerId_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.containerId_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public boolean hasKey() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public String getKey() {
            Object ref = this.key_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.key_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public ByteString getKeyBytes() {
            Object ref = this.key_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.key_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public boolean hasMacroResult() {
            return (this.bitField0_ & 16) == 16;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public MacroEvaluationInfo getMacroResult() {
            return this.macroResult_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public boolean hasDataLayerEventResult() {
            return (this.bitField0_ & 32) == 32;
        }

        @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
        public DataLayerEventEvaluationInfo getDataLayerEventResult() {
            return this.dataLayerEventResult_;
        }

        private void initFields() {
            this.eventType_ = EventType.DATA_LAYER_EVENT;
            this.containerVersion_ = "";
            this.containerId_ = "";
            this.key_ = "";
            this.macroResult_ = MacroEvaluationInfo.getDefaultInstance();
            this.dataLayerEventResult_ = DataLayerEventEvaluationInfo.getDefaultInstance();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (hasMacroResult() && !getMacroResult().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            if (hasDataLayerEventResult() && !getDataLayerEventResult().isInitialized()) {
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
                output.writeEnum(1, this.eventType_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(2, getContainerVersionBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBytes(3, getContainerIdBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(4, getKeyBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeMessage(6, this.macroResult_);
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeMessage(7, this.dataLayerEventResult_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeEnumSize(1, this.eventType_.getNumber()) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(2, getContainerVersionBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeBytesSize(3, getContainerIdBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(4, getKeyBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                size2 += CodedOutputStream.computeMessageSize(6, this.macroResult_);
            }
            if ((this.bitField0_ & 32) == 32) {
                size2 += CodedOutputStream.computeMessageSize(7, this.dataLayerEventResult_);
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
            if (!(obj instanceof EventInfo)) {
                return super.equals(obj);
            }
            EventInfo other = (EventInfo) obj;
            boolean result = 1 != 0 && hasEventType() == other.hasEventType();
            if (hasEventType()) {
                result = result && getEventType() == other.getEventType();
            }
            boolean result2 = result && hasContainerVersion() == other.hasContainerVersion();
            if (hasContainerVersion()) {
                result2 = result2 && getContainerVersion().equals(other.getContainerVersion());
            }
            boolean result3 = result2 && hasContainerId() == other.hasContainerId();
            if (hasContainerId()) {
                result3 = result3 && getContainerId().equals(other.getContainerId());
            }
            boolean result4 = result3 && hasKey() == other.hasKey();
            if (hasKey()) {
                result4 = result4 && getKey().equals(other.getKey());
            }
            boolean result5 = result4 && hasMacroResult() == other.hasMacroResult();
            if (hasMacroResult()) {
                result5 = result5 && getMacroResult().equals(other.getMacroResult());
            }
            boolean result6 = result5 && hasDataLayerEventResult() == other.hasDataLayerEventResult();
            if (hasDataLayerEventResult()) {
                result6 = result6 && getDataLayerEventResult().equals(other.getDataLayerEventResult());
            }
            return result6;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = EventInfo.class.hashCode() + 779;
            if (hasEventType()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashEnum(getEventType());
            }
            if (hasContainerVersion()) {
                hash = (((hash * 37) + 2) * 53) + getContainerVersion().hashCode();
            }
            if (hasContainerId()) {
                hash = (((hash * 37) + 3) * 53) + getContainerId().hashCode();
            }
            if (hasKey()) {
                hash = (((hash * 37) + 4) * 53) + getKey().hashCode();
            }
            if (hasMacroResult()) {
                hash = (((hash * 37) + 6) * 53) + getMacroResult().hashCode();
            }
            if (hasDataLayerEventResult()) {
                hash = (((hash * 37) + 7) * 53) + getDataLayerEventResult().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$EventInfo");
            }
            return mutableDefault;
        }

        public static EventInfo parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static EventInfo parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static EventInfo parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static EventInfo parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static EventInfo parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static EventInfo parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static EventInfo parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static EventInfo parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static EventInfo parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static EventInfo parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(EventInfo prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<EventInfo, Builder> implements EventInfoOrBuilder {
            private int bitField0_;
            private EventType eventType_ = EventType.DATA_LAYER_EVENT;
            private Object containerVersion_ = "";
            private Object containerId_ = "";
            private Object key_ = "";
            private MacroEvaluationInfo macroResult_ = MacroEvaluationInfo.getDefaultInstance();
            private DataLayerEventEvaluationInfo dataLayerEventResult_ = DataLayerEventEvaluationInfo.getDefaultInstance();

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
                this.eventType_ = EventType.DATA_LAYER_EVENT;
                this.bitField0_ &= -2;
                this.containerVersion_ = "";
                this.bitField0_ &= -3;
                this.containerId_ = "";
                this.bitField0_ &= -5;
                this.key_ = "";
                this.bitField0_ &= -9;
                this.macroResult_ = MacroEvaluationInfo.getDefaultInstance();
                this.bitField0_ &= -17;
                this.dataLayerEventResult_ = DataLayerEventEvaluationInfo.getDefaultInstance();
                this.bitField0_ &= -33;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public EventInfo getDefaultInstanceForType() {
                return EventInfo.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public EventInfo build() {
                EventInfo result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public EventInfo buildPartial() {
                EventInfo result = new EventInfo(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.eventType_ = this.eventType_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.containerVersion_ = this.containerVersion_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 4;
                }
                result.containerId_ = this.containerId_;
                if ((from_bitField0_ & 8) == 8) {
                    to_bitField0_ |= 8;
                }
                result.key_ = this.key_;
                if ((from_bitField0_ & 16) == 16) {
                    to_bitField0_ |= 16;
                }
                result.macroResult_ = this.macroResult_;
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 32;
                }
                result.dataLayerEventResult_ = this.dataLayerEventResult_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(EventInfo other) {
                if (other != EventInfo.getDefaultInstance()) {
                    if (other.hasEventType()) {
                        setEventType(other.getEventType());
                    }
                    if (other.hasContainerVersion()) {
                        this.bitField0_ |= 2;
                        this.containerVersion_ = other.containerVersion_;
                    }
                    if (other.hasContainerId()) {
                        this.bitField0_ |= 4;
                        this.containerId_ = other.containerId_;
                    }
                    if (other.hasKey()) {
                        this.bitField0_ |= 8;
                        this.key_ = other.key_;
                    }
                    if (other.hasMacroResult()) {
                        mergeMacroResult(other.getMacroResult());
                    }
                    if (other.hasDataLayerEventResult()) {
                        mergeDataLayerEventResult(other.getDataLayerEventResult());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                if (!hasMacroResult() || getMacroResult().isInitialized()) {
                    return !hasDataLayerEventResult() || getDataLayerEventResult().isInitialized();
                }
                return false;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                EventInfo parsedMessage = null;
                try {
                    try {
                        parsedMessage = EventInfo.PARSER.parsePartialFrom(input, extensionRegistry);
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

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public boolean hasEventType() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public EventType getEventType() {
                return this.eventType_;
            }

            public Builder setEventType(EventType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.eventType_ = value;
                return this;
            }

            public Builder clearEventType() {
                this.bitField0_ &= -2;
                this.eventType_ = EventType.DATA_LAYER_EVENT;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public boolean hasContainerVersion() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public String getContainerVersion() {
                Object ref = this.containerVersion_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.containerVersion_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public ByteString getContainerVersionBytes() {
                Object ref = this.containerVersion_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.containerVersion_ = b;
                return b;
            }

            public Builder setContainerVersion(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.containerVersion_ = value;
                return this;
            }

            public Builder clearContainerVersion() {
                this.bitField0_ &= -3;
                this.containerVersion_ = EventInfo.getDefaultInstance().getContainerVersion();
                return this;
            }

            public Builder setContainerVersionBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.containerVersion_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public boolean hasContainerId() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
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

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
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
                this.bitField0_ |= 4;
                this.containerId_ = value;
                return this;
            }

            public Builder clearContainerId() {
                this.bitField0_ &= -5;
                this.containerId_ = EventInfo.getDefaultInstance().getContainerId();
                return this;
            }

            public Builder setContainerIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.containerId_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public boolean hasKey() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public String getKey() {
                Object ref = this.key_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.key_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public ByteString getKeyBytes() {
                Object ref = this.key_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.key_ = b;
                return b;
            }

            public Builder setKey(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.key_ = value;
                return this;
            }

            public Builder clearKey() {
                this.bitField0_ &= -9;
                this.key_ = EventInfo.getDefaultInstance().getKey();
                return this;
            }

            public Builder setKeyBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 8;
                this.key_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public boolean hasMacroResult() {
                return (this.bitField0_ & 16) == 16;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public MacroEvaluationInfo getMacroResult() {
                return this.macroResult_;
            }

            public Builder setMacroResult(MacroEvaluationInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.macroResult_ = value;
                this.bitField0_ |= 16;
                return this;
            }

            public Builder setMacroResult(MacroEvaluationInfo.Builder builderForValue) {
                this.macroResult_ = builderForValue.build();
                this.bitField0_ |= 16;
                return this;
            }

            public Builder mergeMacroResult(MacroEvaluationInfo value) {
                if ((this.bitField0_ & 16) == 16 && this.macroResult_ != MacroEvaluationInfo.getDefaultInstance()) {
                    this.macroResult_ = MacroEvaluationInfo.newBuilder(this.macroResult_).mergeFrom(value).buildPartial();
                } else {
                    this.macroResult_ = value;
                }
                this.bitField0_ |= 16;
                return this;
            }

            public Builder clearMacroResult() {
                this.macroResult_ = MacroEvaluationInfo.getDefaultInstance();
                this.bitField0_ &= -17;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public boolean hasDataLayerEventResult() {
                return (this.bitField0_ & 32) == 32;
            }

            @Override // com.google.analytics.containertag.proto.Debug.EventInfoOrBuilder
            public DataLayerEventEvaluationInfo getDataLayerEventResult() {
                return this.dataLayerEventResult_;
            }

            public Builder setDataLayerEventResult(DataLayerEventEvaluationInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.dataLayerEventResult_ = value;
                this.bitField0_ |= 32;
                return this;
            }

            public Builder setDataLayerEventResult(DataLayerEventEvaluationInfo.Builder builderForValue) {
                this.dataLayerEventResult_ = builderForValue.build();
                this.bitField0_ |= 32;
                return this;
            }

            public Builder mergeDataLayerEventResult(DataLayerEventEvaluationInfo value) {
                if ((this.bitField0_ & 32) == 32 && this.dataLayerEventResult_ != DataLayerEventEvaluationInfo.getDefaultInstance()) {
                    this.dataLayerEventResult_ = DataLayerEventEvaluationInfo.newBuilder(this.dataLayerEventResult_).mergeFrom(value).buildPartial();
                } else {
                    this.dataLayerEventResult_ = value;
                }
                this.bitField0_ |= 32;
                return this;
            }

            public Builder clearDataLayerEventResult() {
                this.dataLayerEventResult_ = DataLayerEventEvaluationInfo.getDefaultInstance();
                this.bitField0_ &= -33;
                return this;
            }
        }
    }

    public static final class ResolvedRule extends GeneratedMessageLite implements ResolvedRuleOrBuilder {
        public static final int ADD_MACROS_FIELD_NUMBER = 5;
        public static final int ADD_TAGS_FIELD_NUMBER = 3;
        public static final int NEGATIVE_PREDICATES_FIELD_NUMBER = 2;
        public static final int POSITIVE_PREDICATES_FIELD_NUMBER = 1;
        public static final int REMOVE_MACROS_FIELD_NUMBER = 6;
        public static final int REMOVE_TAGS_FIELD_NUMBER = 4;
        public static final int RESULT_FIELD_NUMBER = 7;
        private static final long serialVersionUID = 0;
        private List<ResolvedFunctionCall> addMacros_;
        private List<ResolvedFunctionCall> addTags_;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<ResolvedFunctionCall> negativePredicates_;
        private List<ResolvedFunctionCall> positivePredicates_;
        private List<ResolvedFunctionCall> removeMacros_;
        private List<ResolvedFunctionCall> removeTags_;
        private TypeSystem.Value result_;
        private final ByteString unknownFields;
        public static Parser<ResolvedRule> PARSER = new AbstractParser<ResolvedRule>() { // from class: com.google.analytics.containertag.proto.Debug.ResolvedRule.1
            @Override // com.google.tagmanager.protobuf.Parser
            public ResolvedRule parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ResolvedRule(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final ResolvedRule defaultInstance = new ResolvedRule(true);

        private ResolvedRule(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ResolvedRule(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ResolvedRule getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public ResolvedRule getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ResolvedRule(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int i = 0;
            CodedOutputStream codedOutputStreamNewInstance = CodedOutputStream.newInstance(ByteString.newOutput());
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        switch (tag) {
                            case 0:
                                z = true;
                                break;
                            case 10:
                                if ((i & 1) != 1) {
                                    this.positivePredicates_ = new ArrayList();
                                    i |= 1;
                                }
                                this.positivePredicates_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            case 18:
                                if ((i & 2) != 2) {
                                    this.negativePredicates_ = new ArrayList();
                                    i |= 2;
                                }
                                this.negativePredicates_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            case 26:
                                if ((i & 4) != 4) {
                                    this.addTags_ = new ArrayList();
                                    i |= 4;
                                }
                                this.addTags_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            case 34:
                                if ((i & 8) != 8) {
                                    this.removeTags_ = new ArrayList();
                                    i |= 8;
                                }
                                this.removeTags_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                                if ((i & 16) != 16) {
                                    this.addMacros_ = new ArrayList();
                                    i |= 16;
                                }
                                this.addMacros_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            case 50:
                                if ((i & 32) != 32) {
                                    this.removeMacros_ = new ArrayList();
                                    i |= 32;
                                }
                                this.removeMacros_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            case 58:
                                TypeSystem.Value.Builder builder = (this.bitField0_ & 1) == 1 ? this.result_.toBuilder() : null;
                                this.result_ = (TypeSystem.Value) codedInputStream.readMessage(TypeSystem.Value.PARSER, extensionRegistryLite);
                                if (builder != null) {
                                    builder.mergeFrom(this.result_);
                                    this.result_ = builder.buildPartial();
                                }
                                this.bitField0_ |= 1;
                                break;
                            default:
                                if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                    z = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        if ((i & 1) == 1) {
                            this.positivePredicates_ = Collections.unmodifiableList(this.positivePredicates_);
                        }
                        if ((i & 2) == 2) {
                            this.negativePredicates_ = Collections.unmodifiableList(this.negativePredicates_);
                        }
                        if ((i & 4) == 4) {
                            this.addTags_ = Collections.unmodifiableList(this.addTags_);
                        }
                        if ((i & 8) == 8) {
                            this.removeTags_ = Collections.unmodifiableList(this.removeTags_);
                        }
                        if ((i & 16) == 16) {
                            this.addMacros_ = Collections.unmodifiableList(this.addMacros_);
                        }
                        if ((i & 32) == 32) {
                            this.removeMacros_ = Collections.unmodifiableList(this.removeMacros_);
                        }
                        try {
                            codedOutputStreamNewInstance.flush();
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
            if ((i & 1) == 1) {
                this.positivePredicates_ = Collections.unmodifiableList(this.positivePredicates_);
            }
            if ((i & 2) == 2) {
                this.negativePredicates_ = Collections.unmodifiableList(this.negativePredicates_);
            }
            if ((i & 4) == 4) {
                this.addTags_ = Collections.unmodifiableList(this.addTags_);
            }
            if ((i & 8) == 8) {
                this.removeTags_ = Collections.unmodifiableList(this.removeTags_);
            }
            if ((i & 16) == 16) {
                this.addMacros_ = Collections.unmodifiableList(this.addMacros_);
            }
            if ((i & 32) == 32) {
                this.removeMacros_ = Collections.unmodifiableList(this.removeMacros_);
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
        public Parser<ResolvedRule> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public List<ResolvedFunctionCall> getPositivePredicatesList() {
            return this.positivePredicates_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getPositivePredicatesOrBuilderList() {
            return this.positivePredicates_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public int getPositivePredicatesCount() {
            return this.positivePredicates_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public ResolvedFunctionCall getPositivePredicates(int index) {
            return this.positivePredicates_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getPositivePredicatesOrBuilder(int index) {
            return this.positivePredicates_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public List<ResolvedFunctionCall> getNegativePredicatesList() {
            return this.negativePredicates_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getNegativePredicatesOrBuilderList() {
            return this.negativePredicates_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public int getNegativePredicatesCount() {
            return this.negativePredicates_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public ResolvedFunctionCall getNegativePredicates(int index) {
            return this.negativePredicates_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getNegativePredicatesOrBuilder(int index) {
            return this.negativePredicates_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public List<ResolvedFunctionCall> getAddTagsList() {
            return this.addTags_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getAddTagsOrBuilderList() {
            return this.addTags_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public int getAddTagsCount() {
            return this.addTags_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public ResolvedFunctionCall getAddTags(int index) {
            return this.addTags_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getAddTagsOrBuilder(int index) {
            return this.addTags_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public List<ResolvedFunctionCall> getRemoveTagsList() {
            return this.removeTags_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getRemoveTagsOrBuilderList() {
            return this.removeTags_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public int getRemoveTagsCount() {
            return this.removeTags_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public ResolvedFunctionCall getRemoveTags(int index) {
            return this.removeTags_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getRemoveTagsOrBuilder(int index) {
            return this.removeTags_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public List<ResolvedFunctionCall> getAddMacrosList() {
            return this.addMacros_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getAddMacrosOrBuilderList() {
            return this.addMacros_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public int getAddMacrosCount() {
            return this.addMacros_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public ResolvedFunctionCall getAddMacros(int index) {
            return this.addMacros_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getAddMacrosOrBuilder(int index) {
            return this.addMacros_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public List<ResolvedFunctionCall> getRemoveMacrosList() {
            return this.removeMacros_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getRemoveMacrosOrBuilderList() {
            return this.removeMacros_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public int getRemoveMacrosCount() {
            return this.removeMacros_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public ResolvedFunctionCall getRemoveMacros(int index) {
            return this.removeMacros_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getRemoveMacrosOrBuilder(int index) {
            return this.removeMacros_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public boolean hasResult() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
        public TypeSystem.Value getResult() {
            return this.result_;
        }

        private void initFields() {
            this.positivePredicates_ = Collections.emptyList();
            this.negativePredicates_ = Collections.emptyList();
            this.addTags_ = Collections.emptyList();
            this.removeTags_ = Collections.emptyList();
            this.addMacros_ = Collections.emptyList();
            this.removeMacros_ = Collections.emptyList();
            this.result_ = TypeSystem.Value.getDefaultInstance();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            for (int i = 0; i < getPositivePredicatesCount(); i++) {
                if (!getPositivePredicates(i).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i2 = 0; i2 < getNegativePredicatesCount(); i2++) {
                if (!getNegativePredicates(i2).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i3 = 0; i3 < getAddTagsCount(); i3++) {
                if (!getAddTags(i3).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i4 = 0; i4 < getRemoveTagsCount(); i4++) {
                if (!getRemoveTags(i4).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i5 = 0; i5 < getAddMacrosCount(); i5++) {
                if (!getAddMacros(i5).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i6 = 0; i6 < getRemoveMacrosCount(); i6++) {
                if (!getRemoveMacros(i6).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            if (hasResult() && !getResult().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.positivePredicates_.size(); i++) {
                output.writeMessage(1, this.positivePredicates_.get(i));
            }
            for (int i2 = 0; i2 < this.negativePredicates_.size(); i2++) {
                output.writeMessage(2, this.negativePredicates_.get(i2));
            }
            for (int i3 = 0; i3 < this.addTags_.size(); i3++) {
                output.writeMessage(3, this.addTags_.get(i3));
            }
            for (int i4 = 0; i4 < this.removeTags_.size(); i4++) {
                output.writeMessage(4, this.removeTags_.get(i4));
            }
            for (int i5 = 0; i5 < this.addMacros_.size(); i5++) {
                output.writeMessage(5, this.addMacros_.get(i5));
            }
            for (int i6 = 0; i6 < this.removeMacros_.size(); i6++) {
                output.writeMessage(6, this.removeMacros_.get(i6));
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(7, this.result_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            for (int i = 0; i < this.positivePredicates_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.positivePredicates_.get(i));
            }
            for (int i2 = 0; i2 < this.negativePredicates_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(2, this.negativePredicates_.get(i2));
            }
            for (int i3 = 0; i3 < this.addTags_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.addTags_.get(i3));
            }
            for (int i4 = 0; i4 < this.removeTags_.size(); i4++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.removeTags_.get(i4));
            }
            for (int i5 = 0; i5 < this.addMacros_.size(); i5++) {
                size2 += CodedOutputStream.computeMessageSize(5, this.addMacros_.get(i5));
            }
            for (int i6 = 0; i6 < this.removeMacros_.size(); i6++) {
                size2 += CodedOutputStream.computeMessageSize(6, this.removeMacros_.get(i6));
            }
            if ((this.bitField0_ & 1) == 1) {
                size2 += CodedOutputStream.computeMessageSize(7, this.result_);
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
            if (!(obj instanceof ResolvedRule)) {
                return super.equals(obj);
            }
            ResolvedRule other = (ResolvedRule) obj;
            boolean result = 1 != 0 && getPositivePredicatesList().equals(other.getPositivePredicatesList());
            boolean result2 = result && getNegativePredicatesList().equals(other.getNegativePredicatesList());
            boolean result3 = result2 && getAddTagsList().equals(other.getAddTagsList());
            boolean result4 = result3 && getRemoveTagsList().equals(other.getRemoveTagsList());
            boolean result5 = result4 && getAddMacrosList().equals(other.getAddMacrosList());
            boolean result6 = result5 && getRemoveMacrosList().equals(other.getRemoveMacrosList());
            boolean result7 = result6 && hasResult() == other.hasResult();
            if (hasResult()) {
                result7 = result7 && getResult().equals(other.getResult());
            }
            return result7;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ResolvedRule.class.hashCode() + 779;
            if (getPositivePredicatesCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getPositivePredicatesList().hashCode();
            }
            if (getNegativePredicatesCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getNegativePredicatesList().hashCode();
            }
            if (getAddTagsCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getAddTagsList().hashCode();
            }
            if (getRemoveTagsCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getRemoveTagsList().hashCode();
            }
            if (getAddMacrosCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getAddMacrosList().hashCode();
            }
            if (getRemoveMacrosCount() > 0) {
                hash = (((hash * 37) + 6) * 53) + getRemoveMacrosList().hashCode();
            }
            if (hasResult()) {
                hash = (((hash * 37) + 7) * 53) + getResult().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$ResolvedRule");
            }
            return mutableDefault;
        }

        public static ResolvedRule parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResolvedRule parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResolvedRule parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResolvedRule parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResolvedRule parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResolvedRule parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ResolvedRule parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ResolvedRule parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ResolvedRule parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResolvedRule parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ResolvedRule prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ResolvedRule, Builder> implements ResolvedRuleOrBuilder {
            private int bitField0_;
            private List<ResolvedFunctionCall> positivePredicates_ = Collections.emptyList();
            private List<ResolvedFunctionCall> negativePredicates_ = Collections.emptyList();
            private List<ResolvedFunctionCall> addTags_ = Collections.emptyList();
            private List<ResolvedFunctionCall> removeTags_ = Collections.emptyList();
            private List<ResolvedFunctionCall> addMacros_ = Collections.emptyList();
            private List<ResolvedFunctionCall> removeMacros_ = Collections.emptyList();
            private TypeSystem.Value result_ = TypeSystem.Value.getDefaultInstance();

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
                this.positivePredicates_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.negativePredicates_ = Collections.emptyList();
                this.bitField0_ &= -3;
                this.addTags_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.removeTags_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.addMacros_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.removeMacros_ = Collections.emptyList();
                this.bitField0_ &= -33;
                this.result_ = TypeSystem.Value.getDefaultInstance();
                this.bitField0_ &= -65;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public ResolvedRule getDefaultInstanceForType() {
                return ResolvedRule.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ResolvedRule build() {
                ResolvedRule result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ResolvedRule buildPartial() {
                ResolvedRule result = new ResolvedRule(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.positivePredicates_ = Collections.unmodifiableList(this.positivePredicates_);
                    this.bitField0_ &= -2;
                }
                result.positivePredicates_ = this.positivePredicates_;
                if ((this.bitField0_ & 2) == 2) {
                    this.negativePredicates_ = Collections.unmodifiableList(this.negativePredicates_);
                    this.bitField0_ &= -3;
                }
                result.negativePredicates_ = this.negativePredicates_;
                if ((this.bitField0_ & 4) == 4) {
                    this.addTags_ = Collections.unmodifiableList(this.addTags_);
                    this.bitField0_ &= -5;
                }
                result.addTags_ = this.addTags_;
                if ((this.bitField0_ & 8) == 8) {
                    this.removeTags_ = Collections.unmodifiableList(this.removeTags_);
                    this.bitField0_ &= -9;
                }
                result.removeTags_ = this.removeTags_;
                if ((this.bitField0_ & 16) == 16) {
                    this.addMacros_ = Collections.unmodifiableList(this.addMacros_);
                    this.bitField0_ &= -17;
                }
                result.addMacros_ = this.addMacros_;
                if ((this.bitField0_ & 32) == 32) {
                    this.removeMacros_ = Collections.unmodifiableList(this.removeMacros_);
                    this.bitField0_ &= -33;
                }
                result.removeMacros_ = this.removeMacros_;
                if ((from_bitField0_ & 64) == 64) {
                    to_bitField0_ = 0 | 1;
                }
                result.result_ = this.result_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(ResolvedRule other) {
                if (other != ResolvedRule.getDefaultInstance()) {
                    if (!other.positivePredicates_.isEmpty()) {
                        if (this.positivePredicates_.isEmpty()) {
                            this.positivePredicates_ = other.positivePredicates_;
                            this.bitField0_ &= -2;
                        } else {
                            ensurePositivePredicatesIsMutable();
                            this.positivePredicates_.addAll(other.positivePredicates_);
                        }
                    }
                    if (!other.negativePredicates_.isEmpty()) {
                        if (this.negativePredicates_.isEmpty()) {
                            this.negativePredicates_ = other.negativePredicates_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureNegativePredicatesIsMutable();
                            this.negativePredicates_.addAll(other.negativePredicates_);
                        }
                    }
                    if (!other.addTags_.isEmpty()) {
                        if (this.addTags_.isEmpty()) {
                            this.addTags_ = other.addTags_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureAddTagsIsMutable();
                            this.addTags_.addAll(other.addTags_);
                        }
                    }
                    if (!other.removeTags_.isEmpty()) {
                        if (this.removeTags_.isEmpty()) {
                            this.removeTags_ = other.removeTags_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureRemoveTagsIsMutable();
                            this.removeTags_.addAll(other.removeTags_);
                        }
                    }
                    if (!other.addMacros_.isEmpty()) {
                        if (this.addMacros_.isEmpty()) {
                            this.addMacros_ = other.addMacros_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureAddMacrosIsMutable();
                            this.addMacros_.addAll(other.addMacros_);
                        }
                    }
                    if (!other.removeMacros_.isEmpty()) {
                        if (this.removeMacros_.isEmpty()) {
                            this.removeMacros_ = other.removeMacros_;
                            this.bitField0_ &= -33;
                        } else {
                            ensureRemoveMacrosIsMutable();
                            this.removeMacros_.addAll(other.removeMacros_);
                        }
                    }
                    if (other.hasResult()) {
                        mergeResult(other.getResult());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                for (int i = 0; i < getPositivePredicatesCount(); i++) {
                    if (!getPositivePredicates(i).isInitialized()) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getNegativePredicatesCount(); i2++) {
                    if (!getNegativePredicates(i2).isInitialized()) {
                        return false;
                    }
                }
                for (int i3 = 0; i3 < getAddTagsCount(); i3++) {
                    if (!getAddTags(i3).isInitialized()) {
                        return false;
                    }
                }
                for (int i4 = 0; i4 < getRemoveTagsCount(); i4++) {
                    if (!getRemoveTags(i4).isInitialized()) {
                        return false;
                    }
                }
                for (int i5 = 0; i5 < getAddMacrosCount(); i5++) {
                    if (!getAddMacros(i5).isInitialized()) {
                        return false;
                    }
                }
                for (int i6 = 0; i6 < getRemoveMacrosCount(); i6++) {
                    if (!getRemoveMacros(i6).isInitialized()) {
                        return false;
                    }
                }
                return !hasResult() || getResult().isInitialized();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ResolvedRule parsedMessage = null;
                try {
                    try {
                        parsedMessage = ResolvedRule.PARSER.parsePartialFrom(input, extensionRegistry);
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

            private void ensurePositivePredicatesIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.positivePredicates_ = new ArrayList(this.positivePredicates_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public List<ResolvedFunctionCall> getPositivePredicatesList() {
                return Collections.unmodifiableList(this.positivePredicates_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public int getPositivePredicatesCount() {
                return this.positivePredicates_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public ResolvedFunctionCall getPositivePredicates(int index) {
                return this.positivePredicates_.get(index);
            }

            public Builder setPositivePredicates(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.set(index, value);
                return this;
            }

            public Builder setPositivePredicates(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.set(index, builderForValue.build());
                return this;
            }

            public Builder addPositivePredicates(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.add(value);
                return this;
            }

            public Builder addPositivePredicates(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.add(index, value);
                return this;
            }

            public Builder addPositivePredicates(ResolvedFunctionCall.Builder builderForValue) {
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.add(builderForValue.build());
                return this;
            }

            public Builder addPositivePredicates(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllPositivePredicates(Iterable<? extends ResolvedFunctionCall> values) {
                ensurePositivePredicatesIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.positivePredicates_);
                return this;
            }

            public Builder clearPositivePredicates() {
                this.positivePredicates_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removePositivePredicates(int index) {
                ensurePositivePredicatesIsMutable();
                this.positivePredicates_.remove(index);
                return this;
            }

            private void ensureNegativePredicatesIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.negativePredicates_ = new ArrayList(this.negativePredicates_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public List<ResolvedFunctionCall> getNegativePredicatesList() {
                return Collections.unmodifiableList(this.negativePredicates_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public int getNegativePredicatesCount() {
                return this.negativePredicates_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public ResolvedFunctionCall getNegativePredicates(int index) {
                return this.negativePredicates_.get(index);
            }

            public Builder setNegativePredicates(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.set(index, value);
                return this;
            }

            public Builder setNegativePredicates(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.set(index, builderForValue.build());
                return this;
            }

            public Builder addNegativePredicates(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.add(value);
                return this;
            }

            public Builder addNegativePredicates(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.add(index, value);
                return this;
            }

            public Builder addNegativePredicates(ResolvedFunctionCall.Builder builderForValue) {
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.add(builderForValue.build());
                return this;
            }

            public Builder addNegativePredicates(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllNegativePredicates(Iterable<? extends ResolvedFunctionCall> values) {
                ensureNegativePredicatesIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.negativePredicates_);
                return this;
            }

            public Builder clearNegativePredicates() {
                this.negativePredicates_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder removeNegativePredicates(int index) {
                ensureNegativePredicatesIsMutable();
                this.negativePredicates_.remove(index);
                return this;
            }

            private void ensureAddTagsIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.addTags_ = new ArrayList(this.addTags_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public List<ResolvedFunctionCall> getAddTagsList() {
                return Collections.unmodifiableList(this.addTags_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public int getAddTagsCount() {
                return this.addTags_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public ResolvedFunctionCall getAddTags(int index) {
                return this.addTags_.get(index);
            }

            public Builder setAddTags(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAddTagsIsMutable();
                this.addTags_.set(index, value);
                return this;
            }

            public Builder setAddTags(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureAddTagsIsMutable();
                this.addTags_.set(index, builderForValue.build());
                return this;
            }

            public Builder addAddTags(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAddTagsIsMutable();
                this.addTags_.add(value);
                return this;
            }

            public Builder addAddTags(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAddTagsIsMutable();
                this.addTags_.add(index, value);
                return this;
            }

            public Builder addAddTags(ResolvedFunctionCall.Builder builderForValue) {
                ensureAddTagsIsMutable();
                this.addTags_.add(builderForValue.build());
                return this;
            }

            public Builder addAddTags(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureAddTagsIsMutable();
                this.addTags_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllAddTags(Iterable<? extends ResolvedFunctionCall> values) {
                ensureAddTagsIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addTags_);
                return this;
            }

            public Builder clearAddTags() {
                this.addTags_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            public Builder removeAddTags(int index) {
                ensureAddTagsIsMutable();
                this.addTags_.remove(index);
                return this;
            }

            private void ensureRemoveTagsIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.removeTags_ = new ArrayList(this.removeTags_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public List<ResolvedFunctionCall> getRemoveTagsList() {
                return Collections.unmodifiableList(this.removeTags_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public int getRemoveTagsCount() {
                return this.removeTags_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public ResolvedFunctionCall getRemoveTags(int index) {
                return this.removeTags_.get(index);
            }

            public Builder setRemoveTags(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRemoveTagsIsMutable();
                this.removeTags_.set(index, value);
                return this;
            }

            public Builder setRemoveTags(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureRemoveTagsIsMutable();
                this.removeTags_.set(index, builderForValue.build());
                return this;
            }

            public Builder addRemoveTags(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRemoveTagsIsMutable();
                this.removeTags_.add(value);
                return this;
            }

            public Builder addRemoveTags(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRemoveTagsIsMutable();
                this.removeTags_.add(index, value);
                return this;
            }

            public Builder addRemoveTags(ResolvedFunctionCall.Builder builderForValue) {
                ensureRemoveTagsIsMutable();
                this.removeTags_.add(builderForValue.build());
                return this;
            }

            public Builder addRemoveTags(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureRemoveTagsIsMutable();
                this.removeTags_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllRemoveTags(Iterable<? extends ResolvedFunctionCall> values) {
                ensureRemoveTagsIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeTags_);
                return this;
            }

            public Builder clearRemoveTags() {
                this.removeTags_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            public Builder removeRemoveTags(int index) {
                ensureRemoveTagsIsMutable();
                this.removeTags_.remove(index);
                return this;
            }

            private void ensureAddMacrosIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.addMacros_ = new ArrayList(this.addMacros_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public List<ResolvedFunctionCall> getAddMacrosList() {
                return Collections.unmodifiableList(this.addMacros_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public int getAddMacrosCount() {
                return this.addMacros_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public ResolvedFunctionCall getAddMacros(int index) {
                return this.addMacros_.get(index);
            }

            public Builder setAddMacros(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAddMacrosIsMutable();
                this.addMacros_.set(index, value);
                return this;
            }

            public Builder setAddMacros(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureAddMacrosIsMutable();
                this.addMacros_.set(index, builderForValue.build());
                return this;
            }

            public Builder addAddMacros(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAddMacrosIsMutable();
                this.addMacros_.add(value);
                return this;
            }

            public Builder addAddMacros(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAddMacrosIsMutable();
                this.addMacros_.add(index, value);
                return this;
            }

            public Builder addAddMacros(ResolvedFunctionCall.Builder builderForValue) {
                ensureAddMacrosIsMutable();
                this.addMacros_.add(builderForValue.build());
                return this;
            }

            public Builder addAddMacros(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureAddMacrosIsMutable();
                this.addMacros_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllAddMacros(Iterable<? extends ResolvedFunctionCall> values) {
                ensureAddMacrosIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.addMacros_);
                return this;
            }

            public Builder clearAddMacros() {
                this.addMacros_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            public Builder removeAddMacros(int index) {
                ensureAddMacrosIsMutable();
                this.addMacros_.remove(index);
                return this;
            }

            private void ensureRemoveMacrosIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.removeMacros_ = new ArrayList(this.removeMacros_);
                    this.bitField0_ |= 32;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public List<ResolvedFunctionCall> getRemoveMacrosList() {
                return Collections.unmodifiableList(this.removeMacros_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public int getRemoveMacrosCount() {
                return this.removeMacros_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public ResolvedFunctionCall getRemoveMacros(int index) {
                return this.removeMacros_.get(index);
            }

            public Builder setRemoveMacros(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.set(index, value);
                return this;
            }

            public Builder setRemoveMacros(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.set(index, builderForValue.build());
                return this;
            }

            public Builder addRemoveMacros(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.add(value);
                return this;
            }

            public Builder addRemoveMacros(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.add(index, value);
                return this;
            }

            public Builder addRemoveMacros(ResolvedFunctionCall.Builder builderForValue) {
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.add(builderForValue.build());
                return this;
            }

            public Builder addRemoveMacros(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllRemoveMacros(Iterable<? extends ResolvedFunctionCall> values) {
                ensureRemoveMacrosIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.removeMacros_);
                return this;
            }

            public Builder clearRemoveMacros() {
                this.removeMacros_ = Collections.emptyList();
                this.bitField0_ &= -33;
                return this;
            }

            public Builder removeRemoveMacros(int index) {
                ensureRemoveMacrosIsMutable();
                this.removeMacros_.remove(index);
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public boolean hasResult() {
                return (this.bitField0_ & 64) == 64;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedRuleOrBuilder
            public TypeSystem.Value getResult() {
                return this.result_;
            }

            public Builder setResult(TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.result_ = value;
                this.bitField0_ |= 64;
                return this;
            }

            public Builder setResult(TypeSystem.Value.Builder builderForValue) {
                this.result_ = builderForValue.build();
                this.bitField0_ |= 64;
                return this;
            }

            public Builder mergeResult(TypeSystem.Value value) {
                if ((this.bitField0_ & 64) == 64 && this.result_ != TypeSystem.Value.getDefaultInstance()) {
                    this.result_ = TypeSystem.Value.newBuilder(this.result_).mergeFrom(value).buildPartial();
                } else {
                    this.result_ = value;
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder clearResult() {
                this.result_ = TypeSystem.Value.getDefaultInstance();
                this.bitField0_ &= -65;
                return this;
            }
        }
    }

    public static final class ResolvedFunctionCall extends GeneratedMessageLite implements ResolvedFunctionCallOrBuilder {
        public static final int ASSOCIATED_RULE_NAME_FIELD_NUMBER = 3;
        public static final int PROPERTIES_FIELD_NUMBER = 1;
        public static final int RESULT_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private Object associatedRuleName_;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<ResolvedProperty> properties_;
        private TypeSystem.Value result_;
        private final ByteString unknownFields;
        public static Parser<ResolvedFunctionCall> PARSER = new AbstractParser<ResolvedFunctionCall>() { // from class: com.google.analytics.containertag.proto.Debug.ResolvedFunctionCall.1
            @Override // com.google.tagmanager.protobuf.Parser
            public ResolvedFunctionCall parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ResolvedFunctionCall(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final ResolvedFunctionCall defaultInstance = new ResolvedFunctionCall(true);

        private ResolvedFunctionCall(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ResolvedFunctionCall(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ResolvedFunctionCall getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public ResolvedFunctionCall getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ResolvedFunctionCall(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
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
                                    if ((i & 1) != 1) {
                                        this.properties_ = new ArrayList();
                                        i |= 1;
                                    }
                                    this.properties_.add((ResolvedProperty) codedInputStream.readMessage(ResolvedProperty.PARSER, extensionRegistryLite));
                                    break;
                                case 18:
                                    TypeSystem.Value.Builder builder = (this.bitField0_ & 1) == 1 ? this.result_.toBuilder() : null;
                                    this.result_ = (TypeSystem.Value) codedInputStream.readMessage(TypeSystem.Value.PARSER, extensionRegistryLite);
                                    if (builder != null) {
                                        builder.mergeFrom(this.result_);
                                        this.result_ = builder.buildPartial();
                                    }
                                    this.bitField0_ |= 1;
                                    break;
                                case 26:
                                    ByteString bytes = codedInputStream.readBytes();
                                    this.bitField0_ |= 2;
                                    this.associatedRuleName_ = bytes;
                                    break;
                                default:
                                    if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                        z = true;
                                    }
                                    break;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                    }
                } catch (Throwable th) {
                    if ((i & 1) == 1) {
                        this.properties_ = Collections.unmodifiableList(this.properties_);
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
                this.properties_ = Collections.unmodifiableList(this.properties_);
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
        public Parser<ResolvedFunctionCall> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public List<ResolvedProperty> getPropertiesList() {
            return this.properties_;
        }

        public List<? extends ResolvedPropertyOrBuilder> getPropertiesOrBuilderList() {
            return this.properties_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public int getPropertiesCount() {
            return this.properties_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public ResolvedProperty getProperties(int index) {
            return this.properties_.get(index);
        }

        public ResolvedPropertyOrBuilder getPropertiesOrBuilder(int index) {
            return this.properties_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public boolean hasResult() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public TypeSystem.Value getResult() {
            return this.result_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public boolean hasAssociatedRuleName() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public String getAssociatedRuleName() {
            Object ref = this.associatedRuleName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.associatedRuleName_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
        public ByteString getAssociatedRuleNameBytes() {
            Object ref = this.associatedRuleName_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.associatedRuleName_ = b;
            return b;
        }

        private void initFields() {
            this.properties_ = Collections.emptyList();
            this.result_ = TypeSystem.Value.getDefaultInstance();
            this.associatedRuleName_ = "";
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            for (int i = 0; i < getPropertiesCount(); i++) {
                if (!getProperties(i).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            if (hasResult() && !getResult().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.properties_.size(); i++) {
                output.writeMessage(1, this.properties_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(2, this.result_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(3, getAssociatedRuleNameBytes());
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            for (int i = 0; i < this.properties_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.properties_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                size2 += CodedOutputStream.computeMessageSize(2, this.result_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(3, getAssociatedRuleNameBytes());
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
            if (!(obj instanceof ResolvedFunctionCall)) {
                return super.equals(obj);
            }
            ResolvedFunctionCall other = (ResolvedFunctionCall) obj;
            boolean result = (1 != 0 && getPropertiesList().equals(other.getPropertiesList())) && hasResult() == other.hasResult();
            if (hasResult()) {
                result = result && getResult().equals(other.getResult());
            }
            boolean result2 = result && hasAssociatedRuleName() == other.hasAssociatedRuleName();
            if (hasAssociatedRuleName()) {
                result2 = result2 && getAssociatedRuleName().equals(other.getAssociatedRuleName());
            }
            return result2;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ResolvedFunctionCall.class.hashCode() + 779;
            if (getPropertiesCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getPropertiesList().hashCode();
            }
            if (hasResult()) {
                hash = (((hash * 37) + 2) * 53) + getResult().hashCode();
            }
            if (hasAssociatedRuleName()) {
                hash = (((hash * 37) + 3) * 53) + getAssociatedRuleName().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$ResolvedFunctionCall");
            }
            return mutableDefault;
        }

        public static ResolvedFunctionCall parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResolvedFunctionCall parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResolvedFunctionCall parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResolvedFunctionCall parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResolvedFunctionCall parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResolvedFunctionCall parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ResolvedFunctionCall parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ResolvedFunctionCall parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ResolvedFunctionCall parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResolvedFunctionCall parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ResolvedFunctionCall prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ResolvedFunctionCall, Builder> implements ResolvedFunctionCallOrBuilder {
            private int bitField0_;
            private List<ResolvedProperty> properties_ = Collections.emptyList();
            private TypeSystem.Value result_ = TypeSystem.Value.getDefaultInstance();
            private Object associatedRuleName_ = "";

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
                this.properties_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.result_ = TypeSystem.Value.getDefaultInstance();
                this.bitField0_ &= -3;
                this.associatedRuleName_ = "";
                this.bitField0_ &= -5;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public ResolvedFunctionCall getDefaultInstanceForType() {
                return ResolvedFunctionCall.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ResolvedFunctionCall build() {
                ResolvedFunctionCall result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ResolvedFunctionCall buildPartial() {
                ResolvedFunctionCall result = new ResolvedFunctionCall(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((this.bitField0_ & 1) == 1) {
                    this.properties_ = Collections.unmodifiableList(this.properties_);
                    this.bitField0_ &= -2;
                }
                result.properties_ = this.properties_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ = 0 | 1;
                }
                result.result_ = this.result_;
                if ((from_bitField0_ & 4) == 4) {
                    to_bitField0_ |= 2;
                }
                result.associatedRuleName_ = this.associatedRuleName_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(ResolvedFunctionCall other) {
                if (other != ResolvedFunctionCall.getDefaultInstance()) {
                    if (!other.properties_.isEmpty()) {
                        if (this.properties_.isEmpty()) {
                            this.properties_ = other.properties_;
                            this.bitField0_ &= -2;
                        } else {
                            ensurePropertiesIsMutable();
                            this.properties_.addAll(other.properties_);
                        }
                    }
                    if (other.hasResult()) {
                        mergeResult(other.getResult());
                    }
                    if (other.hasAssociatedRuleName()) {
                        this.bitField0_ |= 4;
                        this.associatedRuleName_ = other.associatedRuleName_;
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                for (int i = 0; i < getPropertiesCount(); i++) {
                    if (!getProperties(i).isInitialized()) {
                        return false;
                    }
                }
                return !hasResult() || getResult().isInitialized();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ResolvedFunctionCall parsedMessage = null;
                try {
                    try {
                        parsedMessage = ResolvedFunctionCall.PARSER.parsePartialFrom(input, extensionRegistry);
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

            private void ensurePropertiesIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.properties_ = new ArrayList(this.properties_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public List<ResolvedProperty> getPropertiesList() {
                return Collections.unmodifiableList(this.properties_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public int getPropertiesCount() {
                return this.properties_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public ResolvedProperty getProperties(int index) {
                return this.properties_.get(index);
            }

            public Builder setProperties(int index, ResolvedProperty value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertiesIsMutable();
                this.properties_.set(index, value);
                return this;
            }

            public Builder setProperties(int index, ResolvedProperty.Builder builderForValue) {
                ensurePropertiesIsMutable();
                this.properties_.set(index, builderForValue.build());
                return this;
            }

            public Builder addProperties(ResolvedProperty value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertiesIsMutable();
                this.properties_.add(value);
                return this;
            }

            public Builder addProperties(int index, ResolvedProperty value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensurePropertiesIsMutable();
                this.properties_.add(index, value);
                return this;
            }

            public Builder addProperties(ResolvedProperty.Builder builderForValue) {
                ensurePropertiesIsMutable();
                this.properties_.add(builderForValue.build());
                return this;
            }

            public Builder addProperties(int index, ResolvedProperty.Builder builderForValue) {
                ensurePropertiesIsMutable();
                this.properties_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllProperties(Iterable<? extends ResolvedProperty> values) {
                ensurePropertiesIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.properties_);
                return this;
            }

            public Builder clearProperties() {
                this.properties_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removeProperties(int index) {
                ensurePropertiesIsMutable();
                this.properties_.remove(index);
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public boolean hasResult() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public TypeSystem.Value getResult() {
                return this.result_;
            }

            public Builder setResult(TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.result_ = value;
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setResult(TypeSystem.Value.Builder builderForValue) {
                this.result_ = builderForValue.build();
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeResult(TypeSystem.Value value) {
                if ((this.bitField0_ & 2) == 2 && this.result_ != TypeSystem.Value.getDefaultInstance()) {
                    this.result_ = TypeSystem.Value.newBuilder(this.result_).mergeFrom(value).buildPartial();
                } else {
                    this.result_ = value;
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearResult() {
                this.result_ = TypeSystem.Value.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public boolean hasAssociatedRuleName() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public String getAssociatedRuleName() {
                Object ref = this.associatedRuleName_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.associatedRuleName_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedFunctionCallOrBuilder
            public ByteString getAssociatedRuleNameBytes() {
                Object ref = this.associatedRuleName_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.associatedRuleName_ = b;
                return b;
            }

            public Builder setAssociatedRuleName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.associatedRuleName_ = value;
                return this;
            }

            public Builder clearAssociatedRuleName() {
                this.bitField0_ &= -5;
                this.associatedRuleName_ = ResolvedFunctionCall.getDefaultInstance().getAssociatedRuleName();
                return this;
            }

            public Builder setAssociatedRuleNameBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 4;
                this.associatedRuleName_ = value;
                return this;
            }
        }
    }

    public static final class RuleEvaluationStepInfo extends GeneratedMessageLite implements RuleEvaluationStepInfoOrBuilder {
        public static final int ENABLED_FUNCTIONS_FIELD_NUMBER = 2;
        public static final int RULES_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private List<ResolvedFunctionCall> enabledFunctions_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<ResolvedRule> rules_;
        private final ByteString unknownFields;
        public static Parser<RuleEvaluationStepInfo> PARSER = new AbstractParser<RuleEvaluationStepInfo>() { // from class: com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfo.1
            @Override // com.google.tagmanager.protobuf.Parser
            public RuleEvaluationStepInfo parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new RuleEvaluationStepInfo(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final RuleEvaluationStepInfo defaultInstance = new RuleEvaluationStepInfo(true);

        private RuleEvaluationStepInfo(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private RuleEvaluationStepInfo(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static RuleEvaluationStepInfo getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public RuleEvaluationStepInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        private RuleEvaluationStepInfo(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int i = 0;
            CodedOutputStream codedOutputStreamNewInstance = CodedOutputStream.newInstance(ByteString.newOutput());
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        switch (tag) {
                            case 0:
                                z = true;
                                break;
                            case 10:
                                if ((i & 1) != 1) {
                                    this.rules_ = new ArrayList();
                                    i |= 1;
                                }
                                this.rules_.add((ResolvedRule) codedInputStream.readMessage(ResolvedRule.PARSER, extensionRegistryLite));
                                break;
                            case 18:
                                if ((i & 2) != 2) {
                                    this.enabledFunctions_ = new ArrayList();
                                    i |= 2;
                                }
                                this.enabledFunctions_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            default:
                                if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                    z = true;
                                }
                                break;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                    }
                } catch (Throwable th) {
                    if ((i & 1) == 1) {
                        this.rules_ = Collections.unmodifiableList(this.rules_);
                    }
                    if ((i & 2) == 2) {
                        this.enabledFunctions_ = Collections.unmodifiableList(this.enabledFunctions_);
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
                this.rules_ = Collections.unmodifiableList(this.rules_);
            }
            if ((i & 2) == 2) {
                this.enabledFunctions_ = Collections.unmodifiableList(this.enabledFunctions_);
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
        public Parser<RuleEvaluationStepInfo> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
        public List<ResolvedRule> getRulesList() {
            return this.rules_;
        }

        public List<? extends ResolvedRuleOrBuilder> getRulesOrBuilderList() {
            return this.rules_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
        public int getRulesCount() {
            return this.rules_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
        public ResolvedRule getRules(int index) {
            return this.rules_.get(index);
        }

        public ResolvedRuleOrBuilder getRulesOrBuilder(int index) {
            return this.rules_.get(index);
        }

        @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
        public List<ResolvedFunctionCall> getEnabledFunctionsList() {
            return this.enabledFunctions_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getEnabledFunctionsOrBuilderList() {
            return this.enabledFunctions_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
        public int getEnabledFunctionsCount() {
            return this.enabledFunctions_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
        public ResolvedFunctionCall getEnabledFunctions(int index) {
            return this.enabledFunctions_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getEnabledFunctionsOrBuilder(int index) {
            return this.enabledFunctions_.get(index);
        }

        private void initFields() {
            this.rules_ = Collections.emptyList();
            this.enabledFunctions_ = Collections.emptyList();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            for (int i = 0; i < getRulesCount(); i++) {
                if (!getRules(i).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i2 = 0; i2 < getEnabledFunctionsCount(); i2++) {
                if (!getEnabledFunctions(i2).isInitialized()) {
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
            for (int i = 0; i < this.rules_.size(); i++) {
                output.writeMessage(1, this.rules_.get(i));
            }
            for (int i2 = 0; i2 < this.enabledFunctions_.size(); i2++) {
                output.writeMessage(2, this.enabledFunctions_.get(i2));
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            for (int i = 0; i < this.rules_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.rules_.get(i));
            }
            for (int i2 = 0; i2 < this.enabledFunctions_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(2, this.enabledFunctions_.get(i2));
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
            if (!(obj instanceof RuleEvaluationStepInfo)) {
                return super.equals(obj);
            }
            RuleEvaluationStepInfo other = (RuleEvaluationStepInfo) obj;
            boolean result = 1 != 0 && getRulesList().equals(other.getRulesList());
            boolean result2 = result && getEnabledFunctionsList().equals(other.getEnabledFunctionsList());
            return result2;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = RuleEvaluationStepInfo.class.hashCode() + 779;
            if (getRulesCount() > 0) {
                hash = (((hash * 37) + 1) * 53) + getRulesList().hashCode();
            }
            if (getEnabledFunctionsCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getEnabledFunctionsList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$RuleEvaluationStepInfo");
            }
            return mutableDefault;
        }

        public static RuleEvaluationStepInfo parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RuleEvaluationStepInfo parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RuleEvaluationStepInfo parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static RuleEvaluationStepInfo parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static RuleEvaluationStepInfo parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RuleEvaluationStepInfo parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static RuleEvaluationStepInfo parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static RuleEvaluationStepInfo parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static RuleEvaluationStepInfo parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static RuleEvaluationStepInfo parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(RuleEvaluationStepInfo prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<RuleEvaluationStepInfo, Builder> implements RuleEvaluationStepInfoOrBuilder {
            private int bitField0_;
            private List<ResolvedRule> rules_ = Collections.emptyList();
            private List<ResolvedFunctionCall> enabledFunctions_ = Collections.emptyList();

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
                this.rules_ = Collections.emptyList();
                this.bitField0_ &= -2;
                this.enabledFunctions_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public RuleEvaluationStepInfo getDefaultInstanceForType() {
                return RuleEvaluationStepInfo.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public RuleEvaluationStepInfo build() {
                RuleEvaluationStepInfo result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public RuleEvaluationStepInfo buildPartial() {
                RuleEvaluationStepInfo result = new RuleEvaluationStepInfo(this);
                int i = this.bitField0_;
                if ((this.bitField0_ & 1) == 1) {
                    this.rules_ = Collections.unmodifiableList(this.rules_);
                    this.bitField0_ &= -2;
                }
                result.rules_ = this.rules_;
                if ((this.bitField0_ & 2) == 2) {
                    this.enabledFunctions_ = Collections.unmodifiableList(this.enabledFunctions_);
                    this.bitField0_ &= -3;
                }
                result.enabledFunctions_ = this.enabledFunctions_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(RuleEvaluationStepInfo other) {
                if (other != RuleEvaluationStepInfo.getDefaultInstance()) {
                    if (!other.rules_.isEmpty()) {
                        if (this.rules_.isEmpty()) {
                            this.rules_ = other.rules_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureRulesIsMutable();
                            this.rules_.addAll(other.rules_);
                        }
                    }
                    if (!other.enabledFunctions_.isEmpty()) {
                        if (this.enabledFunctions_.isEmpty()) {
                            this.enabledFunctions_ = other.enabledFunctions_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureEnabledFunctionsIsMutable();
                            this.enabledFunctions_.addAll(other.enabledFunctions_);
                        }
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                for (int i = 0; i < getRulesCount(); i++) {
                    if (!getRules(i).isInitialized()) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getEnabledFunctionsCount(); i2++) {
                    if (!getEnabledFunctions(i2).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                RuleEvaluationStepInfo parsedMessage = null;
                try {
                    try {
                        parsedMessage = RuleEvaluationStepInfo.PARSER.parsePartialFrom(input, extensionRegistry);
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

            private void ensureRulesIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.rules_ = new ArrayList(this.rules_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
            public List<ResolvedRule> getRulesList() {
                return Collections.unmodifiableList(this.rules_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
            public int getRulesCount() {
                return this.rules_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
            public ResolvedRule getRules(int index) {
                return this.rules_.get(index);
            }

            public Builder setRules(int index, ResolvedRule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRulesIsMutable();
                this.rules_.set(index, value);
                return this;
            }

            public Builder setRules(int index, ResolvedRule.Builder builderForValue) {
                ensureRulesIsMutable();
                this.rules_.set(index, builderForValue.build());
                return this;
            }

            public Builder addRules(ResolvedRule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRulesIsMutable();
                this.rules_.add(value);
                return this;
            }

            public Builder addRules(int index, ResolvedRule value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRulesIsMutable();
                this.rules_.add(index, value);
                return this;
            }

            public Builder addRules(ResolvedRule.Builder builderForValue) {
                ensureRulesIsMutable();
                this.rules_.add(builderForValue.build());
                return this;
            }

            public Builder addRules(int index, ResolvedRule.Builder builderForValue) {
                ensureRulesIsMutable();
                this.rules_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllRules(Iterable<? extends ResolvedRule> values) {
                ensureRulesIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.rules_);
                return this;
            }

            public Builder clearRules() {
                this.rules_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removeRules(int index) {
                ensureRulesIsMutable();
                this.rules_.remove(index);
                return this;
            }

            private void ensureEnabledFunctionsIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.enabledFunctions_ = new ArrayList(this.enabledFunctions_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
            public List<ResolvedFunctionCall> getEnabledFunctionsList() {
                return Collections.unmodifiableList(this.enabledFunctions_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
            public int getEnabledFunctionsCount() {
                return this.enabledFunctions_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.RuleEvaluationStepInfoOrBuilder
            public ResolvedFunctionCall getEnabledFunctions(int index) {
                return this.enabledFunctions_.get(index);
            }

            public Builder setEnabledFunctions(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.set(index, value);
                return this;
            }

            public Builder setEnabledFunctions(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.set(index, builderForValue.build());
                return this;
            }

            public Builder addEnabledFunctions(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.add(value);
                return this;
            }

            public Builder addEnabledFunctions(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.add(index, value);
                return this;
            }

            public Builder addEnabledFunctions(ResolvedFunctionCall.Builder builderForValue) {
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.add(builderForValue.build());
                return this;
            }

            public Builder addEnabledFunctions(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllEnabledFunctions(Iterable<? extends ResolvedFunctionCall> values) {
                ensureEnabledFunctionsIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.enabledFunctions_);
                return this;
            }

            public Builder clearEnabledFunctions() {
                this.enabledFunctions_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder removeEnabledFunctions(int index) {
                ensureEnabledFunctionsIsMutable();
                this.enabledFunctions_.remove(index);
                return this;
            }
        }
    }

    public static final class DataLayerEventEvaluationInfo extends GeneratedMessageLite implements DataLayerEventEvaluationInfoOrBuilder {
        public static final int RESULTS_FIELD_NUMBER = 2;
        public static final int RULES_EVALUATION_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<ResolvedFunctionCall> results_;
        private RuleEvaluationStepInfo rulesEvaluation_;
        private final ByteString unknownFields;
        public static Parser<DataLayerEventEvaluationInfo> PARSER = new AbstractParser<DataLayerEventEvaluationInfo>() { // from class: com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfo.1
            @Override // com.google.tagmanager.protobuf.Parser
            public DataLayerEventEvaluationInfo parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new DataLayerEventEvaluationInfo(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final DataLayerEventEvaluationInfo defaultInstance = new DataLayerEventEvaluationInfo(true);

        private DataLayerEventEvaluationInfo(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private DataLayerEventEvaluationInfo(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static DataLayerEventEvaluationInfo getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public DataLayerEventEvaluationInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        private DataLayerEventEvaluationInfo(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            int i = 0;
            CodedOutputStream codedOutputStreamNewInstance = CodedOutputStream.newInstance(ByteString.newOutput());
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        switch (tag) {
                            case 0:
                                z = true;
                                break;
                            case 10:
                                RuleEvaluationStepInfo.Builder builder = (this.bitField0_ & 1) == 1 ? this.rulesEvaluation_.toBuilder() : null;
                                this.rulesEvaluation_ = (RuleEvaluationStepInfo) codedInputStream.readMessage(RuleEvaluationStepInfo.PARSER, extensionRegistryLite);
                                if (builder != null) {
                                    builder.mergeFrom(this.rulesEvaluation_);
                                    this.rulesEvaluation_ = builder.buildPartial();
                                }
                                this.bitField0_ |= 1;
                                break;
                            case 18:
                                if ((i & 2) != 2) {
                                    this.results_ = new ArrayList();
                                    i |= 2;
                                }
                                this.results_.add((ResolvedFunctionCall) codedInputStream.readMessage(ResolvedFunctionCall.PARSER, extensionRegistryLite));
                                break;
                            default:
                                if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                    z = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        if ((i & 2) == 2) {
                            this.results_ = Collections.unmodifiableList(this.results_);
                        }
                        try {
                            codedOutputStreamNewInstance.flush();
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
            if ((i & 2) == 2) {
                this.results_ = Collections.unmodifiableList(this.results_);
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
        public Parser<DataLayerEventEvaluationInfo> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
        public boolean hasRulesEvaluation() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
        public RuleEvaluationStepInfo getRulesEvaluation() {
            return this.rulesEvaluation_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
        public List<ResolvedFunctionCall> getResultsList() {
            return this.results_;
        }

        public List<? extends ResolvedFunctionCallOrBuilder> getResultsOrBuilderList() {
            return this.results_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
        public int getResultsCount() {
            return this.results_.size();
        }

        @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
        public ResolvedFunctionCall getResults(int index) {
            return this.results_.get(index);
        }

        public ResolvedFunctionCallOrBuilder getResultsOrBuilder(int index) {
            return this.results_.get(index);
        }

        private void initFields() {
            this.rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
            this.results_ = Collections.emptyList();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (hasRulesEvaluation() && !getRulesEvaluation().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            for (int i = 0; i < getResultsCount(); i++) {
                if (!getResults(i).isInitialized()) {
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
            if ((this.bitField0_ & 1) == 1) {
                output.writeMessage(1, this.rulesEvaluation_);
            }
            for (int i = 0; i < this.results_.size(); i++) {
                output.writeMessage(2, this.results_.get(i));
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeMessageSize(1, this.rulesEvaluation_) : 0;
            for (int i = 0; i < this.results_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(2, this.results_.get(i));
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
            if (!(obj instanceof DataLayerEventEvaluationInfo)) {
                return super.equals(obj);
            }
            DataLayerEventEvaluationInfo other = (DataLayerEventEvaluationInfo) obj;
            boolean result = 1 != 0 && hasRulesEvaluation() == other.hasRulesEvaluation();
            if (hasRulesEvaluation()) {
                result = result && getRulesEvaluation().equals(other.getRulesEvaluation());
            }
            return result && getResultsList().equals(other.getResultsList());
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = DataLayerEventEvaluationInfo.class.hashCode() + 779;
            if (hasRulesEvaluation()) {
                hash = (((hash * 37) + 1) * 53) + getRulesEvaluation().hashCode();
            }
            if (getResultsCount() > 0) {
                hash = (((hash * 37) + 2) * 53) + getResultsList().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$DataLayerEventEvaluationInfo");
            }
            return mutableDefault;
        }

        public static DataLayerEventEvaluationInfo parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DataLayerEventEvaluationInfo parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DataLayerEventEvaluationInfo parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DataLayerEventEvaluationInfo parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DataLayerEventEvaluationInfo parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static DataLayerEventEvaluationInfo parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static DataLayerEventEvaluationInfo parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static DataLayerEventEvaluationInfo parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static DataLayerEventEvaluationInfo parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static DataLayerEventEvaluationInfo parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(DataLayerEventEvaluationInfo prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<DataLayerEventEvaluationInfo, Builder> implements DataLayerEventEvaluationInfoOrBuilder {
            private int bitField0_;
            private RuleEvaluationStepInfo rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
            private List<ResolvedFunctionCall> results_ = Collections.emptyList();

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
                this.rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
                this.bitField0_ &= -2;
                this.results_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public DataLayerEventEvaluationInfo getDefaultInstanceForType() {
                return DataLayerEventEvaluationInfo.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public DataLayerEventEvaluationInfo build() {
                DataLayerEventEvaluationInfo result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public DataLayerEventEvaluationInfo buildPartial() {
                DataLayerEventEvaluationInfo result = new DataLayerEventEvaluationInfo(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.rulesEvaluation_ = this.rulesEvaluation_;
                if ((this.bitField0_ & 2) == 2) {
                    this.results_ = Collections.unmodifiableList(this.results_);
                    this.bitField0_ &= -3;
                }
                result.results_ = this.results_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(DataLayerEventEvaluationInfo other) {
                if (other != DataLayerEventEvaluationInfo.getDefaultInstance()) {
                    if (other.hasRulesEvaluation()) {
                        mergeRulesEvaluation(other.getRulesEvaluation());
                    }
                    if (!other.results_.isEmpty()) {
                        if (this.results_.isEmpty()) {
                            this.results_ = other.results_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureResultsIsMutable();
                            this.results_.addAll(other.results_);
                        }
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                if (hasRulesEvaluation() && !getRulesEvaluation().isInitialized()) {
                    return false;
                }
                for (int i = 0; i < getResultsCount(); i++) {
                    if (!getResults(i).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                DataLayerEventEvaluationInfo parsedMessage = null;
                try {
                    try {
                        parsedMessage = DataLayerEventEvaluationInfo.PARSER.parsePartialFrom(input, extensionRegistry);
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

            @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
            public boolean hasRulesEvaluation() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
            public RuleEvaluationStepInfo getRulesEvaluation() {
                return this.rulesEvaluation_;
            }

            public Builder setRulesEvaluation(RuleEvaluationStepInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.rulesEvaluation_ = value;
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setRulesEvaluation(RuleEvaluationStepInfo.Builder builderForValue) {
                this.rulesEvaluation_ = builderForValue.build();
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeRulesEvaluation(RuleEvaluationStepInfo value) {
                if ((this.bitField0_ & 1) == 1 && this.rulesEvaluation_ != RuleEvaluationStepInfo.getDefaultInstance()) {
                    this.rulesEvaluation_ = RuleEvaluationStepInfo.newBuilder(this.rulesEvaluation_).mergeFrom(value).buildPartial();
                } else {
                    this.rulesEvaluation_ = value;
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearRulesEvaluation() {
                this.rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }

            private void ensureResultsIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.results_ = new ArrayList(this.results_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
            public List<ResolvedFunctionCall> getResultsList() {
                return Collections.unmodifiableList(this.results_);
            }

            @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
            public int getResultsCount() {
                return this.results_.size();
            }

            @Override // com.google.analytics.containertag.proto.Debug.DataLayerEventEvaluationInfoOrBuilder
            public ResolvedFunctionCall getResults(int index) {
                return this.results_.get(index);
            }

            public Builder setResults(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureResultsIsMutable();
                this.results_.set(index, value);
                return this;
            }

            public Builder setResults(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureResultsIsMutable();
                this.results_.set(index, builderForValue.build());
                return this;
            }

            public Builder addResults(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureResultsIsMutable();
                this.results_.add(value);
                return this;
            }

            public Builder addResults(int index, ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureResultsIsMutable();
                this.results_.add(index, value);
                return this;
            }

            public Builder addResults(ResolvedFunctionCall.Builder builderForValue) {
                ensureResultsIsMutable();
                this.results_.add(builderForValue.build());
                return this;
            }

            public Builder addResults(int index, ResolvedFunctionCall.Builder builderForValue) {
                ensureResultsIsMutable();
                this.results_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllResults(Iterable<? extends ResolvedFunctionCall> values) {
                ensureResultsIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.results_);
                return this;
            }

            public Builder clearResults() {
                this.results_ = Collections.emptyList();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder removeResults(int index) {
                ensureResultsIsMutable();
                this.results_.remove(index);
                return this;
            }
        }
    }

    public static final class MacroEvaluationInfo extends GeneratedMessageLite implements MacroEvaluationInfoOrBuilder {
        public static final int MACRO_FIELD_NUMBER = 47497405;
        public static final int RESULT_FIELD_NUMBER = 3;
        public static final int RULES_EVALUATION_FIELD_NUMBER = 1;
        public static final GeneratedMessageLite.GeneratedExtension<TypeSystem.Value, MacroEvaluationInfo> macro;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private ResolvedFunctionCall result_;
        private RuleEvaluationStepInfo rulesEvaluation_;
        private final ByteString unknownFields;
        public static Parser<MacroEvaluationInfo> PARSER = new AbstractParser<MacroEvaluationInfo>() { // from class: com.google.analytics.containertag.proto.Debug.MacroEvaluationInfo.1
            @Override // com.google.tagmanager.protobuf.Parser
            public MacroEvaluationInfo parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new MacroEvaluationInfo(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final MacroEvaluationInfo defaultInstance = new MacroEvaluationInfo(true);

        private MacroEvaluationInfo(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private MacroEvaluationInfo(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static MacroEvaluationInfo getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public MacroEvaluationInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        private MacroEvaluationInfo(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                RuleEvaluationStepInfo.Builder subBuilder = (this.bitField0_ & 1) == 1 ? this.rulesEvaluation_.toBuilder() : null;
                                this.rulesEvaluation_ = (RuleEvaluationStepInfo) input.readMessage(RuleEvaluationStepInfo.PARSER, extensionRegistry);
                                if (subBuilder != null) {
                                    subBuilder.mergeFrom(this.rulesEvaluation_);
                                    this.rulesEvaluation_ = subBuilder.buildPartial();
                                }
                                this.bitField0_ |= 1;
                                break;
                            case 26:
                                ResolvedFunctionCall.Builder subBuilder2 = (this.bitField0_ & 2) == 2 ? this.result_.toBuilder() : null;
                                this.result_ = (ResolvedFunctionCall) input.readMessage(ResolvedFunctionCall.PARSER, extensionRegistry);
                                if (subBuilder2 != null) {
                                    subBuilder2.mergeFrom(this.result_);
                                    this.result_ = subBuilder2.buildPartial();
                                }
                                this.bitField0_ |= 2;
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
            macro = GeneratedMessageLite.newSingularGeneratedExtension(TypeSystem.Value.getDefaultInstance(), getDefaultInstance(), getDefaultInstance(), null, 47497405, WireFormat.FieldType.MESSAGE, MacroEvaluationInfo.class);
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite, com.google.tagmanager.protobuf.MessageLite
        public Parser<MacroEvaluationInfo> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
        public boolean hasRulesEvaluation() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
        public RuleEvaluationStepInfo getRulesEvaluation() {
            return this.rulesEvaluation_;
        }

        @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
        public boolean hasResult() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
        public ResolvedFunctionCall getResult() {
            return this.result_;
        }

        private void initFields() {
            this.rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
            this.result_ = ResolvedFunctionCall.getDefaultInstance();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (hasRulesEvaluation() && !getRulesEvaluation().isInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            if (hasResult() && !getResult().isInitialized()) {
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
                output.writeMessage(1, this.rulesEvaluation_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(3, this.result_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeMessageSize(1, this.rulesEvaluation_) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeMessageSize(3, this.result_);
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
            if (!(obj instanceof MacroEvaluationInfo)) {
                return super.equals(obj);
            }
            MacroEvaluationInfo other = (MacroEvaluationInfo) obj;
            boolean result = 1 != 0 && hasRulesEvaluation() == other.hasRulesEvaluation();
            if (hasRulesEvaluation()) {
                result = result && getRulesEvaluation().equals(other.getRulesEvaluation());
            }
            boolean result2 = result && hasResult() == other.hasResult();
            if (hasResult()) {
                result2 = result2 && getResult().equals(other.getResult());
            }
            return result2;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = MacroEvaluationInfo.class.hashCode() + 779;
            if (hasRulesEvaluation()) {
                hash = (((hash * 37) + 1) * 53) + getRulesEvaluation().hashCode();
            }
            if (hasResult()) {
                hash = (((hash * 37) + 3) * 53) + getResult().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$MacroEvaluationInfo");
            }
            return mutableDefault;
        }

        public static MacroEvaluationInfo parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static MacroEvaluationInfo parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static MacroEvaluationInfo parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static MacroEvaluationInfo parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static MacroEvaluationInfo parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static MacroEvaluationInfo parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static MacroEvaluationInfo parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static MacroEvaluationInfo parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static MacroEvaluationInfo parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static MacroEvaluationInfo parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(MacroEvaluationInfo prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<MacroEvaluationInfo, Builder> implements MacroEvaluationInfoOrBuilder {
            private int bitField0_;
            private RuleEvaluationStepInfo rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
            private ResolvedFunctionCall result_ = ResolvedFunctionCall.getDefaultInstance();

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
                this.rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
                this.bitField0_ &= -2;
                this.result_ = ResolvedFunctionCall.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public MacroEvaluationInfo getDefaultInstanceForType() {
                return MacroEvaluationInfo.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public MacroEvaluationInfo build() {
                MacroEvaluationInfo result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public MacroEvaluationInfo buildPartial() {
                MacroEvaluationInfo result = new MacroEvaluationInfo(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.rulesEvaluation_ = this.rulesEvaluation_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.result_ = this.result_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(MacroEvaluationInfo other) {
                if (other != MacroEvaluationInfo.getDefaultInstance()) {
                    if (other.hasRulesEvaluation()) {
                        mergeRulesEvaluation(other.getRulesEvaluation());
                    }
                    if (other.hasResult()) {
                        mergeResult(other.getResult());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                if (!hasRulesEvaluation() || getRulesEvaluation().isInitialized()) {
                    return !hasResult() || getResult().isInitialized();
                }
                return false;
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                MacroEvaluationInfo parsedMessage = null;
                try {
                    try {
                        parsedMessage = MacroEvaluationInfo.PARSER.parsePartialFrom(input, extensionRegistry);
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

            @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
            public boolean hasRulesEvaluation() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
            public RuleEvaluationStepInfo getRulesEvaluation() {
                return this.rulesEvaluation_;
            }

            public Builder setRulesEvaluation(RuleEvaluationStepInfo value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.rulesEvaluation_ = value;
                this.bitField0_ |= 1;
                return this;
            }

            public Builder setRulesEvaluation(RuleEvaluationStepInfo.Builder builderForValue) {
                this.rulesEvaluation_ = builderForValue.build();
                this.bitField0_ |= 1;
                return this;
            }

            public Builder mergeRulesEvaluation(RuleEvaluationStepInfo value) {
                if ((this.bitField0_ & 1) == 1 && this.rulesEvaluation_ != RuleEvaluationStepInfo.getDefaultInstance()) {
                    this.rulesEvaluation_ = RuleEvaluationStepInfo.newBuilder(this.rulesEvaluation_).mergeFrom(value).buildPartial();
                } else {
                    this.rulesEvaluation_ = value;
                }
                this.bitField0_ |= 1;
                return this;
            }

            public Builder clearRulesEvaluation() {
                this.rulesEvaluation_ = RuleEvaluationStepInfo.getDefaultInstance();
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
            public boolean hasResult() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Debug.MacroEvaluationInfoOrBuilder
            public ResolvedFunctionCall getResult() {
                return this.result_;
            }

            public Builder setResult(ResolvedFunctionCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.result_ = value;
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setResult(ResolvedFunctionCall.Builder builderForValue) {
                this.result_ = builderForValue.build();
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeResult(ResolvedFunctionCall value) {
                if ((this.bitField0_ & 2) == 2 && this.result_ != ResolvedFunctionCall.getDefaultInstance()) {
                    this.result_ = ResolvedFunctionCall.newBuilder(this.result_).mergeFrom(value).buildPartial();
                } else {
                    this.result_ = value;
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearResult() {
                this.result_ = ResolvedFunctionCall.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }
        }
    }

    public static final class ResolvedProperty extends GeneratedMessageLite implements ResolvedPropertyOrBuilder {
        public static final int KEY_FIELD_NUMBER = 1;
        public static final int VALUE_FIELD_NUMBER = 2;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private Object key_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private final ByteString unknownFields;
        private TypeSystem.Value value_;
        public static Parser<ResolvedProperty> PARSER = new AbstractParser<ResolvedProperty>() { // from class: com.google.analytics.containertag.proto.Debug.ResolvedProperty.1
            @Override // com.google.tagmanager.protobuf.Parser
            public ResolvedProperty parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ResolvedProperty(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final ResolvedProperty defaultInstance = new ResolvedProperty(true);

        private ResolvedProperty(GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ResolvedProperty(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ResolvedProperty getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public ResolvedProperty getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ResolvedProperty(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                ByteString bs = input.readBytes();
                                this.bitField0_ |= 1;
                                this.key_ = bs;
                                break;
                            case 18:
                                TypeSystem.Value.Builder subBuilder = (this.bitField0_ & 2) == 2 ? this.value_.toBuilder() : null;
                                this.value_ = (TypeSystem.Value) input.readMessage(TypeSystem.Value.PARSER, extensionRegistry);
                                if (subBuilder != null) {
                                    subBuilder.mergeFrom(this.value_);
                                    this.value_ = subBuilder.buildPartial();
                                }
                                this.bitField0_ |= 2;
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
        public Parser<ResolvedProperty> getParserForType() {
            return PARSER;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
        public boolean hasKey() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
        public String getKey() {
            Object ref = this.key_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.key_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
        public ByteString getKeyBytes() {
            Object ref = this.key_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.key_ = b;
            return b;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
        public boolean hasValue() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
        public TypeSystem.Value getValue() {
            return this.value_;
        }

        private void initFields() {
            this.key_ = "";
            this.value_ = TypeSystem.Value.getDefaultInstance();
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (hasValue() && !getValue().isInitialized()) {
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
                output.writeBytes(1, getKeyBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.value_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeBytesSize(1, getKeyBytes()) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeMessageSize(2, this.value_);
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
            if (!(obj instanceof ResolvedProperty)) {
                return super.equals(obj);
            }
            ResolvedProperty other = (ResolvedProperty) obj;
            boolean result = 1 != 0 && hasKey() == other.hasKey();
            if (hasKey()) {
                result = result && getKey().equals(other.getKey());
            }
            boolean result2 = result && hasValue() == other.hasValue();
            if (hasValue()) {
                result2 = result2 && getValue().equals(other.getValue());
            }
            return result2;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ResolvedProperty.class.hashCode() + 779;
            if (hasKey()) {
                hash = (((hash * 37) + 1) * 53) + getKey().hashCode();
            }
            if (hasValue()) {
                hash = (((hash * 37) + 2) * 53) + getValue().hashCode();
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.containertag.proto.MutableDebug$ResolvedProperty");
            }
            return mutableDefault;
        }

        public static ResolvedProperty parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResolvedProperty parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResolvedProperty parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ResolvedProperty parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResolvedProperty parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResolvedProperty parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static ResolvedProperty parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static ResolvedProperty parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ResolvedProperty parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static ResolvedProperty parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ResolvedProperty prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<ResolvedProperty, Builder> implements ResolvedPropertyOrBuilder {
            private int bitField0_;
            private Object key_ = "";
            private TypeSystem.Value value_ = TypeSystem.Value.getDefaultInstance();

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
                this.key_ = "";
                this.bitField0_ &= -2;
                this.value_ = TypeSystem.Value.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public ResolvedProperty getDefaultInstanceForType() {
                return ResolvedProperty.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ResolvedProperty build() {
                ResolvedProperty result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public ResolvedProperty buildPartial() {
                ResolvedProperty result = new ResolvedProperty(this);
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
            public Builder mergeFrom(ResolvedProperty other) {
                if (other != ResolvedProperty.getDefaultInstance()) {
                    if (other.hasKey()) {
                        this.bitField0_ |= 1;
                        this.key_ = other.key_;
                    }
                    if (other.hasValue()) {
                        mergeValue(other.getValue());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return !hasValue() || getValue().isInitialized();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ResolvedProperty parsedMessage = null;
                try {
                    try {
                        parsedMessage = ResolvedProperty.PARSER.parsePartialFrom(input, extensionRegistry);
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

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
            public boolean hasKey() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
            public String getKey() {
                Object ref = this.key_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.key_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
            public ByteString getKeyBytes() {
                Object ref = this.key_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.key_ = b;
                return b;
            }

            public Builder setKey(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.key_ = value;
                return this;
            }

            public Builder clearKey() {
                this.bitField0_ &= -2;
                this.key_ = ResolvedProperty.getDefaultInstance().getKey();
                return this;
            }

            public Builder setKeyBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.key_ = value;
                return this;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
            public boolean hasValue() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.containertag.proto.Debug.ResolvedPropertyOrBuilder
            public TypeSystem.Value getValue() {
                return this.value_;
            }

            public Builder setValue(TypeSystem.Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.value_ = value;
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setValue(TypeSystem.Value.Builder builderForValue) {
                this.value_ = builderForValue.build();
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeValue(TypeSystem.Value value) {
                if ((this.bitField0_ & 2) == 2 && this.value_ != TypeSystem.Value.getDefaultInstance()) {
                    this.value_ = TypeSystem.Value.newBuilder(this.value_).mergeFrom(value).buildPartial();
                } else {
                    this.value_ = value;
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearValue() {
                this.value_ = TypeSystem.Value.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }
        }
    }
}
