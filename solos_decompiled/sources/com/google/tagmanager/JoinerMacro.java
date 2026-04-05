package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes49.dex */
class JoinerMacro extends FunctionCallImplementation {
    private static final String DEFAULT_ITEM_SEPARATOR = "";
    private static final String DEFAULT_KEY_VALUE_SEPARATOR = "=";
    private static final String ID = FunctionType.JOINER.toString();
    private static final String ARG0 = Key.ARG0.toString();
    private static final String ITEM_SEPARATOR = Key.ITEM_SEPARATOR.toString();
    private static final String KEY_VALUE_SEPARATOR = Key.KEY_VALUE_SEPARATOR.toString();
    private static final String ESCAPE = Key.ESCAPE.toString();

    private enum EscapeType {
        NONE,
        URL,
        BACKSLASH
    }

    public static String getFunctionId() {
        return ID;
    }

    public JoinerMacro() {
        super(ID, ARG0);
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public boolean isCacheable() {
        return true;
    }

    @Override // com.google.tagmanager.FunctionCallImplementation
    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        TypeSystem.Value input = parameters.get(ARG0);
        if (input == null) {
            return Types.getDefaultValue();
        }
        TypeSystem.Value itemSeparatorParameter = parameters.get(ITEM_SEPARATOR);
        String itemSeparator = itemSeparatorParameter != null ? Types.valueToString(itemSeparatorParameter) : "";
        TypeSystem.Value keyValueSeparatorParameter = parameters.get(KEY_VALUE_SEPARATOR);
        String keyValueSeparator = keyValueSeparatorParameter != null ? Types.valueToString(keyValueSeparatorParameter) : DEFAULT_KEY_VALUE_SEPARATOR;
        EscapeType escapeType = EscapeType.NONE;
        TypeSystem.Value escapeParameter = parameters.get(ESCAPE);
        Set<Character> charsToBackslashEscape = null;
        if (escapeParameter != null) {
            String escape = Types.valueToString(escapeParameter);
            if ("url".equals(escape)) {
                escapeType = EscapeType.URL;
            } else if ("backslash".equals(escape)) {
                escapeType = EscapeType.BACKSLASH;
                charsToBackslashEscape = new HashSet<>();
                addTo(charsToBackslashEscape, itemSeparator);
                addTo(charsToBackslashEscape, keyValueSeparator);
                charsToBackslashEscape.remove('\\');
            } else {
                Log.e("Joiner: unsupported escape type: " + escape);
                return Types.getDefaultValue();
            }
        }
        StringBuilder sb = new StringBuilder();
        switch (input.getType()) {
            case LIST:
                boolean firstTime = true;
                for (TypeSystem.Value itemVal : input.getListItemList()) {
                    if (!firstTime) {
                        sb.append(itemSeparator);
                    }
                    firstTime = false;
                    append(sb, Types.valueToString(itemVal), escapeType, charsToBackslashEscape);
                }
                break;
            case MAP:
                for (int i = 0; i < input.getMapKeyCount(); i++) {
                    if (i > 0) {
                        sb.append(itemSeparator);
                    }
                    String key = Types.valueToString(input.getMapKey(i));
                    String value = Types.valueToString(input.getMapValue(i));
                    append(sb, key, escapeType, charsToBackslashEscape);
                    sb.append(keyValueSeparator);
                    append(sb, value, escapeType, charsToBackslashEscape);
                }
                break;
            default:
                append(sb, Types.valueToString(input), escapeType, charsToBackslashEscape);
                break;
        }
        return Types.objectToValue(sb.toString());
    }

    private void addTo(Set<Character> set, String string) {
        for (int i = 0; i < string.length(); i++) {
            set.add(Character.valueOf(string.charAt(i)));
        }
    }

    private void append(StringBuilder sb, String s, EscapeType escapeType, Set<Character> charsToBackslashEscape) {
        sb.append(escape(s, escapeType, charsToBackslashEscape));
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0012, code lost:
    
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0013, code lost:
    
        com.google.tagmanager.Log.e("Joiner: unsupported encoding", r2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String escape(java.lang.String r7, com.google.tagmanager.JoinerMacro.EscapeType r8, java.util.Set<java.lang.Character> r9) {
        /*
            r6 = this;
            int[] r4 = com.google.tagmanager.JoinerMacro.AnonymousClass1.$SwitchMap$com$google$tagmanager$JoinerMacro$EscapeType
            int r5 = r8.ordinal()
            r4 = r4[r5]
            switch(r4) {
                case 1: goto Ld;
                case 2: goto L1a;
                default: goto Lb;
            }
        Lb:
            r4 = r7
        Lc:
            return r4
        Ld:
            java.lang.String r4 = com.google.tagmanager.ValueEscapeUtil.urlEncode(r7)     // Catch: java.io.UnsupportedEncodingException -> L12
            goto Lc
        L12:
            r2 = move-exception
            java.lang.String r4 = "Joiner: unsupported encoding"
            com.google.tagmanager.Log.e(r4, r2)
            r4 = r7
            goto Lc
        L1a:
            java.lang.String r4 = "\\"
            java.lang.String r5 = "\\\\"
            java.lang.String r7 = r7.replace(r4, r5)
            java.util.Iterator r3 = r9.iterator()
        L26:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L4e
            java.lang.Object r0 = r3.next()
            java.lang.Character r0 = (java.lang.Character) r0
            java.lang.String r1 = r0.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "\\"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r1)
            java.lang.String r4 = r4.toString()
            java.lang.String r7 = r7.replace(r1, r4)
            goto L26
        L4e:
            r4 = r7
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.JoinerMacro.escape(java.lang.String, com.google.tagmanager.JoinerMacro$EscapeType, java.util.Set):java.lang.String");
    }
}
