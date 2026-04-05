package com.google.analytics.midtier.proto.containertag;

import android.support.v4.view.MotionEventCompat;
import com.google.tagmanager.protobuf.AbstractMessageLite;
import com.google.tagmanager.protobuf.AbstractParser;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.InvalidProtocolBufferException;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import com.nuance.android.vocalizer.VocalizerEngine;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes49.dex */
public final class TypeSystem {

    public interface ValueOrBuilder extends GeneratedMessageLite.ExtendableMessageOrBuilder<Value> {
        boolean getBoolean();

        boolean getContainsReferences();

        Value.Escaping getEscaping(int i);

        int getEscapingCount();

        List<Value.Escaping> getEscapingList();

        String getFunctionId();

        ByteString getFunctionIdBytes();

        long getInteger();

        Value getListItem(int i);

        int getListItemCount();

        List<Value> getListItemList();

        String getMacroReference();

        ByteString getMacroReferenceBytes();

        Value getMapKey(int i);

        int getMapKeyCount();

        List<Value> getMapKeyList();

        Value getMapValue(int i);

        int getMapValueCount();

        List<Value> getMapValueList();

        String getString();

        ByteString getStringBytes();

        Value getTemplateToken(int i);

        int getTemplateTokenCount();

        List<Value> getTemplateTokenList();

        Value.Type getType();

        boolean hasBoolean();

        boolean hasContainsReferences();

        boolean hasFunctionId();

        boolean hasInteger();

        boolean hasMacroReference();

        boolean hasString();

        boolean hasType();
    }

    private TypeSystem() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static final class Value extends GeneratedMessageLite.ExtendableMessage<Value> implements ValueOrBuilder {
        public static final int BOOLEAN_FIELD_NUMBER = 12;
        public static final int CONTAINS_REFERENCES_FIELD_NUMBER = 9;
        public static final int ESCAPING_FIELD_NUMBER = 10;
        public static final int FUNCTION_ID_FIELD_NUMBER = 7;
        public static final int INTEGER_FIELD_NUMBER = 8;
        public static final int LIST_ITEM_FIELD_NUMBER = 3;
        public static final int MACRO_REFERENCE_FIELD_NUMBER = 6;
        public static final int MAP_KEY_FIELD_NUMBER = 4;
        public static final int MAP_VALUE_FIELD_NUMBER = 5;
        public static final int STRING_FIELD_NUMBER = 2;
        public static final int TEMPLATE_TOKEN_FIELD_NUMBER = 11;
        public static final int TYPE_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private int bitField0_;
        private boolean boolean_;
        private boolean containsReferences_;
        private List<Escaping> escaping_;
        private Object functionId_;
        private long integer_;
        private List<Value> listItem_;
        private Object macroReference_;
        private List<Value> mapKey_;
        private List<Value> mapValue_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private Object string_;
        private List<Value> templateToken_;
        private Type type_;
        private final ByteString unknownFields;
        public static Parser<Value> PARSER = new AbstractParser<Value>() { // from class: com.google.analytics.midtier.proto.containertag.TypeSystem.Value.1
            @Override // com.google.tagmanager.protobuf.Parser
            public Value parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Value(input, extensionRegistry);
            }
        };
        private static volatile MutableMessageLite mutableDefault = null;
        private static final Value defaultInstance = new Value(true);

        private Value(GeneratedMessageLite.ExtendableBuilder<Value, ?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Value(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static Value getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public Value getDefaultInstanceForType() {
            return defaultInstance;
        }

        private Value(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
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
                            case 8:
                                int i2 = codedInputStream.readEnum();
                                Type typeValueOf = Type.valueOf(i2);
                                if (typeValueOf == null) {
                                    codedOutputStreamNewInstance.writeRawVarint32(tag);
                                    codedOutputStreamNewInstance.writeRawVarint32(i2);
                                } else {
                                    this.bitField0_ |= 1;
                                    this.type_ = typeValueOf;
                                }
                                break;
                            case 18:
                                ByteString bytes = codedInputStream.readBytes();
                                this.bitField0_ |= 2;
                                this.string_ = bytes;
                                break;
                            case 26:
                                if ((i & 4) != 4) {
                                    this.listItem_ = new ArrayList();
                                    i |= 4;
                                }
                                this.listItem_.add((Value) codedInputStream.readMessage(PARSER, extensionRegistryLite));
                                break;
                            case 34:
                                if ((i & 8) != 8) {
                                    this.mapKey_ = new ArrayList();
                                    i |= 8;
                                }
                                this.mapKey_.add((Value) codedInputStream.readMessage(PARSER, extensionRegistryLite));
                                break;
                            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                                if ((i & 16) != 16) {
                                    this.mapValue_ = new ArrayList();
                                    i |= 16;
                                }
                                this.mapValue_.add((Value) codedInputStream.readMessage(PARSER, extensionRegistryLite));
                                break;
                            case 50:
                                ByteString bytes2 = codedInputStream.readBytes();
                                this.bitField0_ |= 4;
                                this.macroReference_ = bytes2;
                                break;
                            case 58:
                                ByteString bytes3 = codedInputStream.readBytes();
                                this.bitField0_ |= 8;
                                this.functionId_ = bytes3;
                                break;
                            case 64:
                                this.bitField0_ |= 16;
                                this.integer_ = codedInputStream.readInt64();
                                break;
                            case 72:
                                this.bitField0_ |= 64;
                                this.containsReferences_ = codedInputStream.readBool();
                                break;
                            case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                                int i3 = codedInputStream.readEnum();
                                Escaping escapingValueOf = Escaping.valueOf(i3);
                                if (escapingValueOf == null) {
                                    codedOutputStreamNewInstance.writeRawVarint32(tag);
                                    codedOutputStreamNewInstance.writeRawVarint32(i3);
                                } else {
                                    if ((i & 1024) != 1024) {
                                        this.escaping_ = new ArrayList();
                                        i |= 1024;
                                    }
                                    this.escaping_.add(escapingValueOf);
                                }
                                break;
                            case 82:
                                int iPushLimit = codedInputStream.pushLimit(codedInputStream.readRawVarint32());
                                while (codedInputStream.getBytesUntilLimit() > 0) {
                                    int i4 = codedInputStream.readEnum();
                                    Escaping escapingValueOf2 = Escaping.valueOf(i4);
                                    if (escapingValueOf2 == null) {
                                        codedOutputStreamNewInstance.writeRawVarint32(tag);
                                        codedOutputStreamNewInstance.writeRawVarint32(i4);
                                    } else {
                                        if ((i & 1024) != 1024) {
                                            this.escaping_ = new ArrayList();
                                            i |= 1024;
                                        }
                                        this.escaping_.add(escapingValueOf2);
                                    }
                                }
                                codedInputStream.popLimit(iPushLimit);
                                break;
                            case 90:
                                if ((i & 512) != 512) {
                                    this.templateToken_ = new ArrayList();
                                    i |= 512;
                                }
                                this.templateToken_.add((Value) codedInputStream.readMessage(PARSER, extensionRegistryLite));
                                break;
                            case 96:
                                this.bitField0_ |= 32;
                                this.boolean_ = codedInputStream.readBool();
                                break;
                            default:
                                if (!parseUnknownField(codedInputStream, codedOutputStreamNewInstance, extensionRegistryLite, tag)) {
                                    z = true;
                                }
                                break;
                        }
                    } catch (Throwable th) {
                        if ((i & 4) == 4) {
                            this.listItem_ = Collections.unmodifiableList(this.listItem_);
                        }
                        if ((i & 8) == 8) {
                            this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                        }
                        if ((i & 16) == 16) {
                            this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                        }
                        if ((i & 1024) == 1024) {
                            this.escaping_ = Collections.unmodifiableList(this.escaping_);
                        }
                        if ((i & 512) == 512) {
                            this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
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
            if ((i & 4) == 4) {
                this.listItem_ = Collections.unmodifiableList(this.listItem_);
            }
            if ((i & 8) == 8) {
                this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
            }
            if ((i & 16) == 16) {
                this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
            }
            if ((i & 1024) == 1024) {
                this.escaping_ = Collections.unmodifiableList(this.escaping_);
            }
            if ((i & 512) == 512) {
                this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
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
        public Parser<Value> getParserForType() {
            return PARSER;
        }

        public enum Type implements Internal.EnumLite {
            STRING(0, 1),
            LIST(1, 2),
            MAP(2, 3),
            MACRO_REFERENCE(3, 4),
            FUNCTION_ID(4, 5),
            INTEGER(5, 6),
            TEMPLATE(6, 7),
            BOOLEAN(7, 8);

            public static final int BOOLEAN_VALUE = 8;
            public static final int FUNCTION_ID_VALUE = 5;
            public static final int INTEGER_VALUE = 6;
            public static final int LIST_VALUE = 2;
            public static final int MACRO_REFERENCE_VALUE = 4;
            public static final int MAP_VALUE = 3;
            public static final int STRING_VALUE = 1;
            public static final int TEMPLATE_VALUE = 7;
            private static Internal.EnumLiteMap<Type> internalValueMap = new Internal.EnumLiteMap<Type>() { // from class: com.google.analytics.midtier.proto.containertag.TypeSystem.Value.Type.1
                @Override // com.google.tagmanager.protobuf.Internal.EnumLiteMap
                public Type findValueByNumber(int number) {
                    return Type.valueOf(number);
                }
            };
            private final int value;

            @Override // com.google.tagmanager.protobuf.Internal.EnumLite
            public final int getNumber() {
                return this.value;
            }

            public static Type valueOf(int value) {
                switch (value) {
                    case 1:
                        return STRING;
                    case 2:
                        return LIST;
                    case 3:
                        return MAP;
                    case 4:
                        return MACRO_REFERENCE;
                    case 5:
                        return FUNCTION_ID;
                    case 6:
                        return INTEGER;
                    case 7:
                        return TEMPLATE;
                    case 8:
                        return BOOLEAN;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<Type> internalGetValueMap() {
                return internalValueMap;
            }

            Type(int index, int value) {
                this.value = value;
            }
        }

        public enum Escaping implements Internal.EnumLite {
            ESCAPE_HTML(0, 1),
            ESCAPE_HTML_RCDATA(1, 2),
            ESCAPE_HTML_ATTRIBUTE(2, 3),
            ESCAPE_HTML_ATTRIBUTE_NOSPACE(3, 4),
            FILTER_HTML_ELEMENT_NAME(4, 5),
            FILTER_HTML_ATTRIBUTES(5, 6),
            ESCAPE_JS_STRING(6, 7),
            ESCAPE_JS_VALUE(7, 8),
            ESCAPE_JS_REGEX(8, 9),
            ESCAPE_CSS_STRING(9, 10),
            FILTER_CSS_VALUE(10, 11),
            ESCAPE_URI(11, 12),
            NORMALIZE_URI(12, 13),
            FILTER_NORMALIZE_URI(13, 14),
            NO_AUTOESCAPE(14, 15),
            TEXT(15, 17),
            CONVERT_JS_VALUE_TO_EXPRESSION(16, 16);

            public static final int CONVERT_JS_VALUE_TO_EXPRESSION_VALUE = 16;
            public static final int ESCAPE_CSS_STRING_VALUE = 10;
            public static final int ESCAPE_HTML_ATTRIBUTE_NOSPACE_VALUE = 4;
            public static final int ESCAPE_HTML_ATTRIBUTE_VALUE = 3;
            public static final int ESCAPE_HTML_RCDATA_VALUE = 2;
            public static final int ESCAPE_HTML_VALUE = 1;
            public static final int ESCAPE_JS_REGEX_VALUE = 9;
            public static final int ESCAPE_JS_STRING_VALUE = 7;
            public static final int ESCAPE_JS_VALUE_VALUE = 8;
            public static final int ESCAPE_URI_VALUE = 12;
            public static final int FILTER_CSS_VALUE_VALUE = 11;
            public static final int FILTER_HTML_ATTRIBUTES_VALUE = 6;
            public static final int FILTER_HTML_ELEMENT_NAME_VALUE = 5;
            public static final int FILTER_NORMALIZE_URI_VALUE = 14;
            public static final int NORMALIZE_URI_VALUE = 13;
            public static final int NO_AUTOESCAPE_VALUE = 15;
            public static final int TEXT_VALUE = 17;
            private static Internal.EnumLiteMap<Escaping> internalValueMap = new Internal.EnumLiteMap<Escaping>() { // from class: com.google.analytics.midtier.proto.containertag.TypeSystem.Value.Escaping.1
                @Override // com.google.tagmanager.protobuf.Internal.EnumLiteMap
                public Escaping findValueByNumber(int number) {
                    return Escaping.valueOf(number);
                }
            };
            private final int value;

            @Override // com.google.tagmanager.protobuf.Internal.EnumLite
            public final int getNumber() {
                return this.value;
            }

            public static Escaping valueOf(int value) {
                switch (value) {
                    case 1:
                        return ESCAPE_HTML;
                    case 2:
                        return ESCAPE_HTML_RCDATA;
                    case 3:
                        return ESCAPE_HTML_ATTRIBUTE;
                    case 4:
                        return ESCAPE_HTML_ATTRIBUTE_NOSPACE;
                    case 5:
                        return FILTER_HTML_ELEMENT_NAME;
                    case 6:
                        return FILTER_HTML_ATTRIBUTES;
                    case 7:
                        return ESCAPE_JS_STRING;
                    case 8:
                        return ESCAPE_JS_VALUE;
                    case 9:
                        return ESCAPE_JS_REGEX;
                    case 10:
                        return ESCAPE_CSS_STRING;
                    case 11:
                        return FILTER_CSS_VALUE;
                    case 12:
                        return ESCAPE_URI;
                    case 13:
                        return NORMALIZE_URI;
                    case 14:
                        return FILTER_NORMALIZE_URI;
                    case 15:
                        return NO_AUTOESCAPE;
                    case 16:
                        return CONVERT_JS_VALUE_TO_EXPRESSION;
                    case 17:
                        return TEXT;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<Escaping> internalGetValueMap() {
                return internalValueMap;
            }

            Escaping(int index, int value) {
                this.value = value;
            }
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasType() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public Type getType() {
            return this.type_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasString() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public String getString() {
            Object ref = this.string_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.string_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public ByteString getStringBytes() {
            Object ref = this.string_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.string_ = b;
            return b;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public List<Value> getListItemList() {
            return this.listItem_;
        }

        public List<? extends ValueOrBuilder> getListItemOrBuilderList() {
            return this.listItem_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public int getListItemCount() {
            return this.listItem_.size();
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public Value getListItem(int index) {
            return this.listItem_.get(index);
        }

        public ValueOrBuilder getListItemOrBuilder(int index) {
            return this.listItem_.get(index);
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public List<Value> getMapKeyList() {
            return this.mapKey_;
        }

        public List<? extends ValueOrBuilder> getMapKeyOrBuilderList() {
            return this.mapKey_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public int getMapKeyCount() {
            return this.mapKey_.size();
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public Value getMapKey(int index) {
            return this.mapKey_.get(index);
        }

        public ValueOrBuilder getMapKeyOrBuilder(int index) {
            return this.mapKey_.get(index);
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public List<Value> getMapValueList() {
            return this.mapValue_;
        }

        public List<? extends ValueOrBuilder> getMapValueOrBuilderList() {
            return this.mapValue_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public int getMapValueCount() {
            return this.mapValue_.size();
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public Value getMapValue(int index) {
            return this.mapValue_.get(index);
        }

        public ValueOrBuilder getMapValueOrBuilder(int index) {
            return this.mapValue_.get(index);
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasMacroReference() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public String getMacroReference() {
            Object ref = this.macroReference_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.macroReference_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public ByteString getMacroReferenceBytes() {
            Object ref = this.macroReference_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.macroReference_ = b;
            return b;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasFunctionId() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public String getFunctionId() {
            Object ref = this.functionId_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.functionId_ = s;
            }
            return s;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public ByteString getFunctionIdBytes() {
            Object ref = this.functionId_;
            if (!(ref instanceof String)) {
                return (ByteString) ref;
            }
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.functionId_ = b;
            return b;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasInteger() {
            return (this.bitField0_ & 16) == 16;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public long getInteger() {
            return this.integer_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasBoolean() {
            return (this.bitField0_ & 32) == 32;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean getBoolean() {
            return this.boolean_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public List<Value> getTemplateTokenList() {
            return this.templateToken_;
        }

        public List<? extends ValueOrBuilder> getTemplateTokenOrBuilderList() {
            return this.templateToken_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public int getTemplateTokenCount() {
            return this.templateToken_.size();
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public Value getTemplateToken(int index) {
            return this.templateToken_.get(index);
        }

        public ValueOrBuilder getTemplateTokenOrBuilder(int index) {
            return this.templateToken_.get(index);
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public List<Escaping> getEscapingList() {
            return this.escaping_;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public int getEscapingCount() {
            return this.escaping_.size();
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public Escaping getEscaping(int index) {
            return this.escaping_.get(index);
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean hasContainsReferences() {
            return (this.bitField0_ & 64) == 64;
        }

        @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
        public boolean getContainsReferences() {
            return this.containsReferences_;
        }

        private void initFields() {
            this.type_ = Type.STRING;
            this.string_ = "";
            this.listItem_ = Collections.emptyList();
            this.mapKey_ = Collections.emptyList();
            this.mapValue_ = Collections.emptyList();
            this.macroReference_ = "";
            this.functionId_ = "";
            this.integer_ = 0L;
            this.boolean_ = false;
            this.templateToken_ = Collections.emptyList();
            this.escaping_ = Collections.emptyList();
            this.containsReferences_ = false;
        }

        @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            if (!hasType()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            for (int i = 0; i < getListItemCount(); i++) {
                if (!getListItem(i).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i2 = 0; i2 < getMapKeyCount(); i2++) {
                if (!getMapKey(i2).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i3 = 0; i3 < getMapValueCount(); i3++) {
                if (!getMapValue(i3).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            for (int i4 = 0; i4 < getTemplateTokenCount(); i4++) {
                if (!getTemplateToken(i4).isInitialized()) {
                    this.memoizedIsInitialized = (byte) 0;
                    return false;
                }
            }
            if (!extensionsAreInitialized()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            GeneratedMessageLite.ExtendableMessage<MessageType>.ExtensionWriter extensionWriterNewExtensionWriter = newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                output.writeEnum(1, this.type_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeBytes(2, getStringBytes());
            }
            for (int i = 0; i < this.listItem_.size(); i++) {
                output.writeMessage(3, this.listItem_.get(i));
            }
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                output.writeMessage(4, this.mapKey_.get(i2));
            }
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                output.writeMessage(5, this.mapValue_.get(i3));
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeBytes(6, getMacroReferenceBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeBytes(7, getFunctionIdBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeInt64(8, this.integer_);
            }
            if ((this.bitField0_ & 64) == 64) {
                output.writeBool(9, this.containsReferences_);
            }
            for (int i4 = 0; i4 < this.escaping_.size(); i4++) {
                output.writeEnum(10, this.escaping_.get(i4).getNumber());
            }
            for (int i5 = 0; i5 < this.templateToken_.size(); i5++) {
                output.writeMessage(11, this.templateToken_.get(i5));
            }
            if ((this.bitField0_ & 32) == 32) {
                output.writeBool(12, this.boolean_);
            }
            extensionWriterNewExtensionWriter.writeUntil(536870912, output);
            output.writeRawBytes(this.unknownFields);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = (this.bitField0_ & 1) == 1 ? 0 + CodedOutputStream.computeEnumSize(1, this.type_.getNumber()) : 0;
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeBytesSize(2, getStringBytes());
            }
            for (int i = 0; i < this.listItem_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.listItem_.get(i));
            }
            for (int i2 = 0; i2 < this.mapKey_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.mapKey_.get(i2));
            }
            for (int i3 = 0; i3 < this.mapValue_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(5, this.mapValue_.get(i3));
            }
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeBytesSize(6, getMacroReferenceBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                size2 += CodedOutputStream.computeBytesSize(7, getFunctionIdBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                size2 += CodedOutputStream.computeInt64Size(8, this.integer_);
            }
            if ((this.bitField0_ & 64) == 64) {
                size2 += CodedOutputStream.computeBoolSize(9, this.containsReferences_);
            }
            int dataSize = 0;
            for (int i4 = 0; i4 < this.escaping_.size(); i4++) {
                dataSize += CodedOutputStream.computeEnumSizeNoTag(this.escaping_.get(i4).getNumber());
            }
            int size3 = size2 + dataSize + (this.escaping_.size() * 1);
            for (int i5 = 0; i5 < this.templateToken_.size(); i5++) {
                size3 += CodedOutputStream.computeMessageSize(11, this.templateToken_.get(i5));
            }
            if ((this.bitField0_ & 32) == 32) {
                size3 += CodedOutputStream.computeBoolSize(12, this.boolean_);
            }
            int size4 = size3 + extensionsSerializedSize() + this.unknownFields.size();
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
            if (!(obj instanceof Value)) {
                return super.equals(obj);
            }
            Value other = (Value) obj;
            boolean result = 1 != 0 && hasType() == other.hasType();
            if (hasType()) {
                result = result && getType() == other.getType();
            }
            boolean result2 = result && hasString() == other.hasString();
            if (hasString()) {
                result2 = result2 && getString().equals(other.getString());
            }
            boolean result3 = (((result2 && getListItemList().equals(other.getListItemList())) && getMapKeyList().equals(other.getMapKeyList())) && getMapValueList().equals(other.getMapValueList())) && hasMacroReference() == other.hasMacroReference();
            if (hasMacroReference()) {
                result3 = result3 && getMacroReference().equals(other.getMacroReference());
            }
            boolean result4 = result3 && hasFunctionId() == other.hasFunctionId();
            if (hasFunctionId()) {
                result4 = result4 && getFunctionId().equals(other.getFunctionId());
            }
            boolean result5 = result4 && hasInteger() == other.hasInteger();
            if (hasInteger()) {
                result5 = result5 && getInteger() == other.getInteger();
            }
            boolean result6 = result5 && hasBoolean() == other.hasBoolean();
            if (hasBoolean()) {
                result6 = result6 && getBoolean() == other.getBoolean();
            }
            boolean result7 = ((result6 && getTemplateTokenList().equals(other.getTemplateTokenList())) && getEscapingList().equals(other.getEscapingList())) && hasContainsReferences() == other.hasContainsReferences();
            if (hasContainsReferences()) {
                result7 = result7 && getContainsReferences() == other.getContainsReferences();
            }
            return result7;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = Value.class.hashCode() + 779;
            if (hasType()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashEnum(getType());
            }
            if (hasString()) {
                hash = (((hash * 37) + 2) * 53) + getString().hashCode();
            }
            if (getListItemCount() > 0) {
                hash = (((hash * 37) + 3) * 53) + getListItemList().hashCode();
            }
            if (getMapKeyCount() > 0) {
                hash = (((hash * 37) + 4) * 53) + getMapKeyList().hashCode();
            }
            if (getMapValueCount() > 0) {
                hash = (((hash * 37) + 5) * 53) + getMapValueList().hashCode();
            }
            if (hasMacroReference()) {
                hash = (((hash * 37) + 6) * 53) + getMacroReference().hashCode();
            }
            if (hasFunctionId()) {
                hash = (((hash * 37) + 7) * 53) + getFunctionId().hashCode();
            }
            if (hasInteger()) {
                hash = (((hash * 37) + 8) * 53) + Internal.hashLong(getInteger());
            }
            if (hasBoolean()) {
                hash = (((hash * 37) + 12) * 53) + Internal.hashBoolean(getBoolean());
            }
            if (getTemplateTokenCount() > 0) {
                hash = (((hash * 37) + 11) * 53) + getTemplateTokenList().hashCode();
            }
            if (getEscapingCount() > 0) {
                hash = (((hash * 37) + 10) * 53) + Internal.hashEnumList(getEscapingList());
            }
            if (hasContainsReferences()) {
                hash = (((hash * 37) + 9) * 53) + Internal.hashBoolean(getContainsReferences());
            }
            int hash2 = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        @Override // com.google.tagmanager.protobuf.GeneratedMessageLite
        protected MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = internalMutableDefault("com.google.analytics.midtier.proto.containertag.MutableTypeSystem$Value");
            }
            return mutableDefault;
        }

        public static Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Value parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Value parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Value parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Value prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.tagmanager.protobuf.MessageLite
        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.ExtendableBuilder<Value, Builder> implements ValueOrBuilder {
            private int bitField0_;
            private boolean boolean_;
            private boolean containsReferences_;
            private long integer_;
            private Type type_ = Type.STRING;
            private Object string_ = "";
            private List<Value> listItem_ = Collections.emptyList();
            private List<Value> mapKey_ = Collections.emptyList();
            private List<Value> mapValue_ = Collections.emptyList();
            private Object macroReference_ = "";
            private Object functionId_ = "";
            private List<Value> templateToken_ = Collections.emptyList();
            private List<Escaping> escaping_ = Collections.emptyList();

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableBuilder, com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder clear() {
                super.clear();
                this.type_ = Type.STRING;
                this.bitField0_ &= -2;
                this.string_ = "";
                this.bitField0_ &= -3;
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -5;
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.macroReference_ = "";
                this.bitField0_ &= -33;
                this.functionId_ = "";
                this.bitField0_ &= -65;
                this.integer_ = 0L;
                this.bitField0_ &= -129;
                this.boolean_ = false;
                this.bitField0_ &= -257;
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -513;
                this.escaping_ = Collections.emptyList();
                this.bitField0_ &= -1025;
                this.containsReferences_ = false;
                this.bitField0_ &= -2049;
                return this;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableBuilder, com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.AbstractMessageLite.Builder
            /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
            public Builder mo5clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder, com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public Value getDefaultInstanceForType() {
                return Value.getDefaultInstance();
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Value build() {
                Value result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override // com.google.tagmanager.protobuf.MessageLite.Builder
            public Value buildPartial() {
                Value result = new Value(this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.type_ = this.type_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.string_ = this.string_;
                if ((this.bitField0_ & 4) == 4) {
                    this.listItem_ = Collections.unmodifiableList(this.listItem_);
                    this.bitField0_ &= -5;
                }
                result.listItem_ = this.listItem_;
                if ((this.bitField0_ & 8) == 8) {
                    this.mapKey_ = Collections.unmodifiableList(this.mapKey_);
                    this.bitField0_ &= -9;
                }
                result.mapKey_ = this.mapKey_;
                if ((this.bitField0_ & 16) == 16) {
                    this.mapValue_ = Collections.unmodifiableList(this.mapValue_);
                    this.bitField0_ &= -17;
                }
                result.mapValue_ = this.mapValue_;
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 4;
                }
                result.macroReference_ = this.macroReference_;
                if ((from_bitField0_ & 64) == 64) {
                    to_bitField0_ |= 8;
                }
                result.functionId_ = this.functionId_;
                if ((from_bitField0_ & 128) == 128) {
                    to_bitField0_ |= 16;
                }
                result.integer_ = this.integer_;
                if ((from_bitField0_ & 256) == 256) {
                    to_bitField0_ |= 32;
                }
                result.boolean_ = this.boolean_;
                if ((this.bitField0_ & 512) == 512) {
                    this.templateToken_ = Collections.unmodifiableList(this.templateToken_);
                    this.bitField0_ &= -513;
                }
                result.templateToken_ = this.templateToken_;
                if ((this.bitField0_ & 1024) == 1024) {
                    this.escaping_ = Collections.unmodifiableList(this.escaping_);
                    this.bitField0_ &= -1025;
                }
                result.escaping_ = this.escaping_;
                if ((from_bitField0_ & 2048) == 2048) {
                    to_bitField0_ |= 64;
                }
                result.containsReferences_ = this.containsReferences_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            @Override // com.google.tagmanager.protobuf.GeneratedMessageLite.Builder
            public Builder mergeFrom(Value other) {
                if (other != Value.getDefaultInstance()) {
                    if (other.hasType()) {
                        setType(other.getType());
                    }
                    if (other.hasString()) {
                        this.bitField0_ |= 2;
                        this.string_ = other.string_;
                    }
                    if (!other.listItem_.isEmpty()) {
                        if (this.listItem_.isEmpty()) {
                            this.listItem_ = other.listItem_;
                            this.bitField0_ &= -5;
                        } else {
                            ensureListItemIsMutable();
                            this.listItem_.addAll(other.listItem_);
                        }
                    }
                    if (!other.mapKey_.isEmpty()) {
                        if (this.mapKey_.isEmpty()) {
                            this.mapKey_ = other.mapKey_;
                            this.bitField0_ &= -9;
                        } else {
                            ensureMapKeyIsMutable();
                            this.mapKey_.addAll(other.mapKey_);
                        }
                    }
                    if (!other.mapValue_.isEmpty()) {
                        if (this.mapValue_.isEmpty()) {
                            this.mapValue_ = other.mapValue_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureMapValueIsMutable();
                            this.mapValue_.addAll(other.mapValue_);
                        }
                    }
                    if (other.hasMacroReference()) {
                        this.bitField0_ |= 32;
                        this.macroReference_ = other.macroReference_;
                    }
                    if (other.hasFunctionId()) {
                        this.bitField0_ |= 64;
                        this.functionId_ = other.functionId_;
                    }
                    if (other.hasInteger()) {
                        setInteger(other.getInteger());
                    }
                    if (other.hasBoolean()) {
                        setBoolean(other.getBoolean());
                    }
                    if (!other.templateToken_.isEmpty()) {
                        if (this.templateToken_.isEmpty()) {
                            this.templateToken_ = other.templateToken_;
                            this.bitField0_ &= -513;
                        } else {
                            ensureTemplateTokenIsMutable();
                            this.templateToken_.addAll(other.templateToken_);
                        }
                    }
                    if (!other.escaping_.isEmpty()) {
                        if (this.escaping_.isEmpty()) {
                            this.escaping_ = other.escaping_;
                            this.bitField0_ &= -1025;
                        } else {
                            ensureEscapingIsMutable();
                            this.escaping_.addAll(other.escaping_);
                        }
                    }
                    if (other.hasContainsReferences()) {
                        setContainsReferences(other.getContainsReferences());
                    }
                    mergeExtensionFields(other);
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            @Override // com.google.tagmanager.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                if (!hasType()) {
                    return false;
                }
                for (int i = 0; i < getListItemCount(); i++) {
                    if (!getListItem(i).isInitialized()) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getMapKeyCount(); i2++) {
                    if (!getMapKey(i2).isInitialized()) {
                        return false;
                    }
                }
                for (int i3 = 0; i3 < getMapValueCount(); i3++) {
                    if (!getMapValue(i3).isInitialized()) {
                        return false;
                    }
                }
                for (int i4 = 0; i4 < getTemplateTokenCount(); i4++) {
                    if (!getTemplateToken(i4).isInitialized()) {
                        return false;
                    }
                }
                return extensionsAreInitialized();
            }

            @Override // com.google.tagmanager.protobuf.AbstractMessageLite.Builder, com.google.tagmanager.protobuf.MessageLite.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Value parsedMessage = null;
                try {
                    try {
                        parsedMessage = Value.PARSER.parsePartialFrom(input, extensionRegistry);
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

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasType() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public Type getType() {
                return this.type_;
            }

            public Builder setType(Type value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.type_ = value;
                return this;
            }

            public Builder clearType() {
                this.bitField0_ &= -2;
                this.type_ = Type.STRING;
                return this;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasString() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public String getString() {
                Object ref = this.string_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.string_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public ByteString getStringBytes() {
                Object ref = this.string_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.string_ = b;
                return b;
            }

            public Builder setString(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.string_ = value;
                return this;
            }

            public Builder clearString() {
                this.bitField0_ &= -3;
                this.string_ = Value.getDefaultInstance().getString();
                return this;
            }

            public Builder setStringBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 2;
                this.string_ = value;
                return this;
            }

            private void ensureListItemIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.listItem_ = new ArrayList(this.listItem_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public List<Value> getListItemList() {
                return Collections.unmodifiableList(this.listItem_);
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public int getListItemCount() {
                return this.listItem_.size();
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public Value getListItem(int index) {
                return this.listItem_.get(index);
            }

            public Builder setListItem(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureListItemIsMutable();
                this.listItem_.set(index, value);
                return this;
            }

            public Builder setListItem(int index, Builder builderForValue) {
                ensureListItemIsMutable();
                this.listItem_.set(index, builderForValue.build());
                return this;
            }

            public Builder addListItem(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureListItemIsMutable();
                this.listItem_.add(value);
                return this;
            }

            public Builder addListItem(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureListItemIsMutable();
                this.listItem_.add(index, value);
                return this;
            }

            public Builder addListItem(Builder builderForValue) {
                ensureListItemIsMutable();
                this.listItem_.add(builderForValue.build());
                return this;
            }

            public Builder addListItem(int index, Builder builderForValue) {
                ensureListItemIsMutable();
                this.listItem_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllListItem(Iterable<? extends Value> values) {
                ensureListItemIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.listItem_);
                return this;
            }

            public Builder clearListItem() {
                this.listItem_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            public Builder removeListItem(int index) {
                ensureListItemIsMutable();
                this.listItem_.remove(index);
                return this;
            }

            private void ensureMapKeyIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.mapKey_ = new ArrayList(this.mapKey_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public List<Value> getMapKeyList() {
                return Collections.unmodifiableList(this.mapKey_);
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public int getMapKeyCount() {
                return this.mapKey_.size();
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public Value getMapKey(int index) {
                return this.mapKey_.get(index);
            }

            public Builder setMapKey(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapKeyIsMutable();
                this.mapKey_.set(index, value);
                return this;
            }

            public Builder setMapKey(int index, Builder builderForValue) {
                ensureMapKeyIsMutable();
                this.mapKey_.set(index, builderForValue.build());
                return this;
            }

            public Builder addMapKey(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapKeyIsMutable();
                this.mapKey_.add(value);
                return this;
            }

            public Builder addMapKey(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapKeyIsMutable();
                this.mapKey_.add(index, value);
                return this;
            }

            public Builder addMapKey(Builder builderForValue) {
                ensureMapKeyIsMutable();
                this.mapKey_.add(builderForValue.build());
                return this;
            }

            public Builder addMapKey(int index, Builder builderForValue) {
                ensureMapKeyIsMutable();
                this.mapKey_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllMapKey(Iterable<? extends Value> values) {
                ensureMapKeyIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapKey_);
                return this;
            }

            public Builder clearMapKey() {
                this.mapKey_ = Collections.emptyList();
                this.bitField0_ &= -9;
                return this;
            }

            public Builder removeMapKey(int index) {
                ensureMapKeyIsMutable();
                this.mapKey_.remove(index);
                return this;
            }

            private void ensureMapValueIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.mapValue_ = new ArrayList(this.mapValue_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public List<Value> getMapValueList() {
                return Collections.unmodifiableList(this.mapValue_);
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public int getMapValueCount() {
                return this.mapValue_.size();
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public Value getMapValue(int index) {
                return this.mapValue_.get(index);
            }

            public Builder setMapValue(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapValueIsMutable();
                this.mapValue_.set(index, value);
                return this;
            }

            public Builder setMapValue(int index, Builder builderForValue) {
                ensureMapValueIsMutable();
                this.mapValue_.set(index, builderForValue.build());
                return this;
            }

            public Builder addMapValue(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapValueIsMutable();
                this.mapValue_.add(value);
                return this;
            }

            public Builder addMapValue(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureMapValueIsMutable();
                this.mapValue_.add(index, value);
                return this;
            }

            public Builder addMapValue(Builder builderForValue) {
                ensureMapValueIsMutable();
                this.mapValue_.add(builderForValue.build());
                return this;
            }

            public Builder addMapValue(int index, Builder builderForValue) {
                ensureMapValueIsMutable();
                this.mapValue_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllMapValue(Iterable<? extends Value> values) {
                ensureMapValueIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.mapValue_);
                return this;
            }

            public Builder clearMapValue() {
                this.mapValue_ = Collections.emptyList();
                this.bitField0_ &= -17;
                return this;
            }

            public Builder removeMapValue(int index) {
                ensureMapValueIsMutable();
                this.mapValue_.remove(index);
                return this;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasMacroReference() {
                return (this.bitField0_ & 32) == 32;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public String getMacroReference() {
                Object ref = this.macroReference_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.macroReference_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public ByteString getMacroReferenceBytes() {
                Object ref = this.macroReference_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.macroReference_ = b;
                return b;
            }

            public Builder setMacroReference(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 32;
                this.macroReference_ = value;
                return this;
            }

            public Builder clearMacroReference() {
                this.bitField0_ &= -33;
                this.macroReference_ = Value.getDefaultInstance().getMacroReference();
                return this;
            }

            public Builder setMacroReferenceBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 32;
                this.macroReference_ = value;
                return this;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasFunctionId() {
                return (this.bitField0_ & 64) == 64;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public String getFunctionId() {
                Object ref = this.functionId_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.functionId_ = s;
                    return s;
                }
                return s;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public ByteString getFunctionIdBytes() {
                Object ref = this.functionId_;
                if (!(ref instanceof String)) {
                    return (ByteString) ref;
                }
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.functionId_ = b;
                return b;
            }

            public Builder setFunctionId(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 64;
                this.functionId_ = value;
                return this;
            }

            public Builder clearFunctionId() {
                this.bitField0_ &= -65;
                this.functionId_ = Value.getDefaultInstance().getFunctionId();
                return this;
            }

            public Builder setFunctionIdBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 64;
                this.functionId_ = value;
                return this;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasInteger() {
                return (this.bitField0_ & 128) == 128;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public long getInteger() {
                return this.integer_;
            }

            public Builder setInteger(long value) {
                this.bitField0_ |= 128;
                this.integer_ = value;
                return this;
            }

            public Builder clearInteger() {
                this.bitField0_ &= -129;
                this.integer_ = 0L;
                return this;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasBoolean() {
                return (this.bitField0_ & 256) == 256;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean getBoolean() {
                return this.boolean_;
            }

            public Builder setBoolean(boolean value) {
                this.bitField0_ |= 256;
                this.boolean_ = value;
                return this;
            }

            public Builder clearBoolean() {
                this.bitField0_ &= -257;
                this.boolean_ = false;
                return this;
            }

            private void ensureTemplateTokenIsMutable() {
                if ((this.bitField0_ & 512) != 512) {
                    this.templateToken_ = new ArrayList(this.templateToken_);
                    this.bitField0_ |= 512;
                }
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public List<Value> getTemplateTokenList() {
                return Collections.unmodifiableList(this.templateToken_);
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public int getTemplateTokenCount() {
                return this.templateToken_.size();
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public Value getTemplateToken(int index) {
                return this.templateToken_.get(index);
            }

            public Builder setTemplateToken(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTemplateTokenIsMutable();
                this.templateToken_.set(index, value);
                return this;
            }

            public Builder setTemplateToken(int index, Builder builderForValue) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.set(index, builderForValue.build());
                return this;
            }

            public Builder addTemplateToken(Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(value);
                return this;
            }

            public Builder addTemplateToken(int index, Value value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(index, value);
                return this;
            }

            public Builder addTemplateToken(Builder builderForValue) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(builderForValue.build());
                return this;
            }

            public Builder addTemplateToken(int index, Builder builderForValue) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.add(index, builderForValue.build());
                return this;
            }

            public Builder addAllTemplateToken(Iterable<? extends Value> values) {
                ensureTemplateTokenIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.templateToken_);
                return this;
            }

            public Builder clearTemplateToken() {
                this.templateToken_ = Collections.emptyList();
                this.bitField0_ &= -513;
                return this;
            }

            public Builder removeTemplateToken(int index) {
                ensureTemplateTokenIsMutable();
                this.templateToken_.remove(index);
                return this;
            }

            private void ensureEscapingIsMutable() {
                if ((this.bitField0_ & 1024) != 1024) {
                    this.escaping_ = new ArrayList(this.escaping_);
                    this.bitField0_ |= 1024;
                }
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public List<Escaping> getEscapingList() {
                return Collections.unmodifiableList(this.escaping_);
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public int getEscapingCount() {
                return this.escaping_.size();
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public Escaping getEscaping(int index) {
                return this.escaping_.get(index);
            }

            public Builder setEscaping(int index, Escaping value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEscapingIsMutable();
                this.escaping_.set(index, value);
                return this;
            }

            public Builder addEscaping(Escaping value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureEscapingIsMutable();
                this.escaping_.add(value);
                return this;
            }

            public Builder addAllEscaping(Iterable<? extends Escaping> values) {
                ensureEscapingIsMutable();
                AbstractMessageLite.Builder.addAll(values, this.escaping_);
                return this;
            }

            public Builder clearEscaping() {
                this.escaping_ = Collections.emptyList();
                this.bitField0_ &= -1025;
                return this;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean hasContainsReferences() {
                return (this.bitField0_ & 2048) == 2048;
            }

            @Override // com.google.analytics.midtier.proto.containertag.TypeSystem.ValueOrBuilder
            public boolean getContainsReferences() {
                return this.containsReferences_;
            }

            public Builder setContainsReferences(boolean value) {
                this.bitField0_ |= 2048;
                this.containsReferences_ = value;
                return this;
            }

            public Builder clearContainsReferences() {
                this.bitField0_ &= -2049;
                this.containsReferences_ = false;
                return this;
            }
        }
    }
}
