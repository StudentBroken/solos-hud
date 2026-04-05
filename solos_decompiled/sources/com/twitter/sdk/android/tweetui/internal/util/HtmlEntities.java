package com.twitter.sdk.android.tweetui.internal.util;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes9.dex */
public class HtmlEntities {
    final EntityMap map = new LookupEntityMap();
    static final String[][] ISO8859_1_ARRAY = {new String[]{"nbsp", "160"}, new String[]{"iexcl", "161"}, new String[]{"cent", "162"}, new String[]{"pound", "163"}, new String[]{"curren", "164"}, new String[]{"yen", "165"}, new String[]{"brvbar", "166"}, new String[]{"sect", "167"}, new String[]{"uml", "168"}, new String[]{"copy", "169"}, new String[]{"ordf", "170"}, new String[]{"laquo", "171"}, new String[]{"not", "172"}, new String[]{"shy", "173"}, new String[]{"reg", "174"}, new String[]{"macr", "175"}, new String[]{"deg", "176"}, new String[]{"plusmn", "177"}, new String[]{"sup2", "178"}, new String[]{"sup3", "179"}, new String[]{"acute", "180"}, new String[]{"micro", "181"}, new String[]{"para", "182"}, new String[]{"middot", "183"}, new String[]{"cedil", "184"}, new String[]{"sup1", "185"}, new String[]{"ordm", "186"}, new String[]{"raquo", "187"}, new String[]{"frac14", "188"}, new String[]{"frac12", "189"}, new String[]{"frac34", "190"}, new String[]{"iquest", "191"}, new String[]{"Agrave", "192"}, new String[]{"Aacute", "193"}, new String[]{"Acirc", "194"}, new String[]{"Atilde", "195"}, new String[]{"Auml", "196"}, new String[]{"Aring", "197"}, new String[]{"AElig", "198"}, new String[]{"Ccedil", "199"}, new String[]{"Egrave", "200"}, new String[]{"Eacute", "201"}, new String[]{"Ecirc", "202"}, new String[]{"Euml", "203"}, new String[]{"Igrave", "204"}, new String[]{"Iacute", "205"}, new String[]{"Icirc", "206"}, new String[]{"Iuml", "207"}, new String[]{"ETH", "208"}, new String[]{"Ntilde", "209"}, new String[]{"Ograve", "210"}, new String[]{"Oacute", "211"}, new String[]{"Ocirc", "212"}, new String[]{"Otilde", "213"}, new String[]{"Ouml", "214"}, new String[]{"times", "215"}, new String[]{"Oslash", "216"}, new String[]{"Ugrave", "217"}, new String[]{"Uacute", "218"}, new String[]{"Ucirc", "219"}, new String[]{"Uuml", "220"}, new String[]{"Yacute", "221"}, new String[]{"THORN", "222"}, new String[]{"szlig", "223"}, new String[]{"agrave", "224"}, new String[]{"aacute", "225"}, new String[]{"acirc", "226"}, new String[]{"atilde", "227"}, new String[]{"auml", "228"}, new String[]{"aring", "229"}, new String[]{"aelig", "230"}, new String[]{"ccedil", "231"}, new String[]{"egrave", "232"}, new String[]{"eacute", "233"}, new String[]{"ecirc", "234"}, new String[]{"euml", "235"}, new String[]{"igrave", "236"}, new String[]{"iacute", "237"}, new String[]{"icirc", "238"}, new String[]{"iuml", "239"}, new String[]{"eth", "240"}, new String[]{"ntilde", "241"}, new String[]{"ograve", "242"}, new String[]{"oacute", "243"}, new String[]{"ocirc", "244"}, new String[]{"otilde", "245"}, new String[]{"ouml", "246"}, new String[]{"divide", "247"}, new String[]{"oslash", "248"}, new String[]{"ugrave", "249"}, new String[]{"uacute", "250"}, new String[]{"ucirc", "251"}, new String[]{"uuml", "252"}, new String[]{"yacute", "253"}, new String[]{"thorn", "254"}, new String[]{"yuml", "255"}};
    static final String[][] HTML40_ARRAY = {new String[]{"fnof", "402"}, new String[]{"Alpha", "913"}, new String[]{"Beta", "914"}, new String[]{"Gamma", "915"}, new String[]{"Delta", "916"}, new String[]{"Epsilon", "917"}, new String[]{"Zeta", "918"}, new String[]{"Eta", "919"}, new String[]{"Theta", "920"}, new String[]{"Iota", "921"}, new String[]{"Kappa", "922"}, new String[]{"Lambda", "923"}, new String[]{"Mu", "924"}, new String[]{"Nu", "925"}, new String[]{"Xi", "926"}, new String[]{"Omicron", "927"}, new String[]{"Pi", "928"}, new String[]{"Rho", "929"}, new String[]{"Sigma", "931"}, new String[]{"Tau", "932"}, new String[]{"Upsilon", "933"}, new String[]{"Phi", "934"}, new String[]{"Chi", "935"}, new String[]{"Psi", "936"}, new String[]{"Omega", "937"}, new String[]{"alpha", "945"}, new String[]{SettingsJsonConstants.BETA_KEY, "946"}, new String[]{"gamma", "947"}, new String[]{"delta", "948"}, new String[]{"epsilon", "949"}, new String[]{"zeta", "950"}, new String[]{"eta", "951"}, new String[]{"theta", "952"}, new String[]{"iota", "953"}, new String[]{"kappa", "954"}, new String[]{"lambda", "955"}, new String[]{"mu", "956"}, new String[]{"nu", "957"}, new String[]{"xi", "958"}, new String[]{"omicron", "959"}, new String[]{"pi", "960"}, new String[]{"rho", "961"}, new String[]{"sigmaf", "962"}, new String[]{"sigma", "963"}, new String[]{"tau", "964"}, new String[]{"upsilon", "965"}, new String[]{"phi", "966"}, new String[]{"chi", "967"}, new String[]{"psi", "968"}, new String[]{"omega", "969"}, new String[]{"thetasym", "977"}, new String[]{"upsih", "978"}, new String[]{"piv", "982"}, new String[]{"bull", "8226"}, new String[]{"hellip", "8230"}, new String[]{"prime", "8242"}, new String[]{"Prime", "8243"}, new String[]{"oline", "8254"}, new String[]{"frasl", "8260"}, new String[]{"weierp", "8472"}, new String[]{"image", "8465"}, new String[]{"real", "8476"}, new String[]{"trade", "8482"}, new String[]{"alefsym", "8501"}, new String[]{"larr", "8592"}, new String[]{"uarr", "8593"}, new String[]{"rarr", "8594"}, new String[]{"darr", "8595"}, new String[]{"harr", "8596"}, new String[]{"crarr", "8629"}, new String[]{"lArr", "8656"}, new String[]{"uArr", "8657"}, new String[]{"rArr", "8658"}, new String[]{"dArr", "8659"}, new String[]{"hArr", "8660"}, new String[]{"forall", "8704"}, new String[]{"part", "8706"}, new String[]{"exist", "8707"}, new String[]{"empty", "8709"}, new String[]{"nabla", "8711"}, new String[]{"isin", "8712"}, new String[]{"notin", "8713"}, new String[]{"ni", "8715"}, new String[]{"prod", "8719"}, new String[]{"sum", "8721"}, new String[]{"minus", "8722"}, new String[]{"lowast", "8727"}, new String[]{"radic", "8730"}, new String[]{"prop", "8733"}, new String[]{"infin", "8734"}, new String[]{"ang", "8736"}, new String[]{"and", "8743"}, new String[]{"or", "8744"}, new String[]{"cap", "8745"}, new String[]{"cup", "8746"}, new String[]{"int", "8747"}, new String[]{"there4", "8756"}, new String[]{"sim", "8764"}, new String[]{"cong", "8773"}, new String[]{"asymp", "8776"}, new String[]{"ne", "8800"}, new String[]{"equiv", "8801"}, new String[]{"le", "8804"}, new String[]{"ge", "8805"}, new String[]{"sub", "8834"}, new String[]{"sup", "8835"}, new String[]{"sube", "8838"}, new String[]{"supe", "8839"}, new String[]{"oplus", "8853"}, new String[]{"otimes", "8855"}, new String[]{"perp", "8869"}, new String[]{"sdot", "8901"}, new String[]{"lceil", "8968"}, new String[]{"rceil", "8969"}, new String[]{"lfloor", "8970"}, new String[]{"rfloor", "8971"}, new String[]{"lang", "9001"}, new String[]{"rang", "9002"}, new String[]{"loz", "9674"}, new String[]{"spades", "9824"}, new String[]{"clubs", "9827"}, new String[]{"hearts", "9829"}, new String[]{"diams", "9830"}, new String[]{"OElig", "338"}, new String[]{"oelig", "339"}, new String[]{"Scaron", "352"}, new String[]{"scaron", "353"}, new String[]{"Yuml", "376"}, new String[]{"circ", "710"}, new String[]{"tilde", "732"}, new String[]{"ensp", "8194"}, new String[]{"emsp", "8195"}, new String[]{"thinsp", "8201"}, new String[]{"zwnj", "8204"}, new String[]{"zwj", "8205"}, new String[]{"lrm", "8206"}, new String[]{"rlm", "8207"}, new String[]{"ndash", "8211"}, new String[]{"mdash", "8212"}, new String[]{"lsquo", "8216"}, new String[]{"rsquo", "8217"}, new String[]{"sbquo", "8218"}, new String[]{"ldquo", "8220"}, new String[]{"rdquo", "8221"}, new String[]{"bdquo", "8222"}, new String[]{"dagger", "8224"}, new String[]{"Dagger", "8225"}, new String[]{"permil", "8240"}, new String[]{"lsaquo", "8249"}, new String[]{"rsaquo", "8250"}, new String[]{"euro", "8364"}};
    private static final String[][] BASIC_ARRAY = {new String[]{"quot", "34"}, new String[]{"amp", "38"}, new String[]{"lt", "60"}, new String[]{"gt", "62"}};
    public static final HtmlEntities HTML40 = new HtmlEntities();

    interface EntityMap {
        void add(String str, int i);

        String name(int i);

        int value(String str);
    }

    static {
        fillWithHtml40Entities(HTML40);
    }

    static void fillWithHtml40Entities(HtmlEntities entities) {
        entities.addEntities(BASIC_ARRAY);
        entities.addEntities(ISO8859_1_ARRAY);
        entities.addEntities(HTML40_ARRAY);
    }

    static class PrimitiveEntityMap implements EntityMap {
        private final Map mapNameToValue = new HashMap();
        private final IntHashMap mapValueToName = new IntHashMap();

        PrimitiveEntityMap() {
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public void add(String name, int value) {
            this.mapNameToValue.put(name, Integer.valueOf(value));
            this.mapValueToName.put(value, name);
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public String name(int value) {
            return (String) this.mapValueToName.get(value);
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public int value(String name) {
            Object value = this.mapNameToValue.get(name);
            if (value == null) {
                return -1;
            }
            return ((Integer) value).intValue();
        }
    }

    static abstract class MapIntMap implements EntityMap {
        protected Map mapNameToValue;
        protected Map mapValueToName;

        MapIntMap() {
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public void add(String name, int value) {
            this.mapNameToValue.put(name, Integer.valueOf(value));
            this.mapValueToName.put(Integer.valueOf(value), name);
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public String name(int value) {
            return (String) this.mapValueToName.get(Integer.valueOf(value));
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public int value(String name) {
            Object value = this.mapNameToValue.get(name);
            if (value == null) {
                return -1;
            }
            return ((Integer) value).intValue();
        }
    }

    static class HashEntityMap extends MapIntMap {
        public HashEntityMap() {
            this.mapNameToValue = new HashMap();
            this.mapValueToName = new HashMap();
        }
    }

    static class TreeEntityMap extends MapIntMap {
        public TreeEntityMap() {
            this.mapNameToValue = new TreeMap();
            this.mapValueToName = new TreeMap();
        }
    }

    static class LookupEntityMap extends PrimitiveEntityMap {
        private static final int LOOKUP_TABLE_SIZE = 256;
        private String[] lookupTable;

        LookupEntityMap() {
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.PrimitiveEntityMap, com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public String name(int value) {
            return value < 256 ? lookupTable()[value] : super.name(value);
        }

        private String[] lookupTable() {
            if (this.lookupTable == null) {
                createLookupTable();
            }
            return this.lookupTable;
        }

        private void createLookupTable() {
            this.lookupTable = new String[256];
            for (int i = 0; i < 256; i++) {
                this.lookupTable[i] = super.name(i);
            }
        }
    }

    static class ArrayEntityMap implements EntityMap {
        protected int growBy;
        protected String[] names;
        protected int size;
        protected int[] values;

        public ArrayEntityMap() {
            this.growBy = 100;
            this.size = 0;
            this.names = new String[this.growBy];
            this.values = new int[this.growBy];
        }

        public ArrayEntityMap(int growBy) {
            this.growBy = 100;
            this.size = 0;
            this.growBy = growBy;
            this.names = new String[growBy];
            this.values = new int[growBy];
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public void add(String name, int value) {
            ensureCapacity(this.size + 1);
            this.names[this.size] = name;
            this.values[this.size] = value;
            this.size++;
        }

        protected void ensureCapacity(int capacity) {
            if (capacity > this.names.length) {
                int newSize = Math.max(capacity, this.size + this.growBy);
                String[] newNames = new String[newSize];
                System.arraycopy(this.names, 0, newNames, 0, this.size);
                this.names = newNames;
                int[] newValues = new int[newSize];
                System.arraycopy(this.values, 0, newValues, 0, this.size);
                this.values = newValues;
            }
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public String name(int value) {
            for (int i = 0; i < this.size; i++) {
                if (this.values[i] == value) {
                    return this.names[i];
                }
            }
            return null;
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public int value(String name) {
            for (int i = 0; i < this.size; i++) {
                if (this.names[i].equals(name)) {
                    return this.values[i];
                }
            }
            return -1;
        }
    }

    static class BinaryEntityMap extends ArrayEntityMap {
        public BinaryEntityMap() {
        }

        public BinaryEntityMap(int growBy) {
            super(growBy);
        }

        private int binarySearch(int key) {
            int low = 0;
            int high = this.size - 1;
            while (low <= high) {
                int mid = (low + high) >>> 1;
                int midVal = this.values[mid];
                if (midVal < key) {
                    low = mid + 1;
                } else {
                    if (midVal <= key) {
                        return mid;
                    }
                    high = mid - 1;
                }
            }
            return -(low + 1);
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.ArrayEntityMap, com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public void add(String name, int value) {
            ensureCapacity(this.size + 1);
            int insertAt = binarySearch(value);
            if (insertAt <= 0) {
                int insertAt2 = -(insertAt + 1);
                System.arraycopy(this.values, insertAt2, this.values, insertAt2 + 1, this.size - insertAt2);
                this.values[insertAt2] = value;
                System.arraycopy(this.names, insertAt2, this.names, insertAt2 + 1, this.size - insertAt2);
                this.names[insertAt2] = name;
                this.size++;
            }
        }

        @Override // com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.ArrayEntityMap, com.twitter.sdk.android.tweetui.internal.util.HtmlEntities.EntityMap
        public String name(int value) {
            int index = binarySearch(value);
            if (index < 0) {
                return null;
            }
            return this.names[index];
        }
    }

    public static final class Unescaped {
        public final ArrayList<int[]> indices;
        public final String unescaped;

        public Unescaped(String unescaped, ArrayList<int[]> indices) {
            this.unescaped = unescaped;
            this.indices = indices;
        }
    }

    public void addEntities(String[][] entityArray) {
        for (String[] anEntityArray : entityArray) {
            addEntity(anEntityArray[0], Integer.parseInt(anEntityArray[1]));
        }
    }

    public void addEntity(String name, int value) {
        this.map.add(name, value);
    }

    public String entityName(int value) {
        return this.map.name(value);
    }

    public int entityValue(String name) {
        return this.map.value(name);
    }

    public Unescaped unescape(String str) {
        int length = str.length();
        StringBuilder buf = new StringBuilder(length);
        ArrayList<int[]> indices = new ArrayList<>(5);
        int i = 0;
        while (i < length) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int semi = str.indexOf(59, i + 1);
                if (semi == -1) {
                    buf.append(ch);
                } else {
                    String entityName = str.substring(i + 1, semi);
                    int entityNameLength = entityName.length();
                    int entityValue = -1;
                    if (entityNameLength > 0) {
                        if (entityName.charAt(0) == '#' && entityNameLength > 1) {
                            char charAt1 = entityName.charAt(1);
                            if (charAt1 == 'x' || charAt1 == 'X') {
                                if (entityNameLength > 2) {
                                    entityValue = Integer.valueOf(entityName.substring(2), 16).intValue();
                                }
                            } else {
                                try {
                                    entityValue = Integer.parseInt(entityName.substring(1));
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            entityValue = entityValue(entityName);
                        }
                    }
                    if (entityValue == -1) {
                        buf.append('&');
                        int amp = entityName.indexOf(38);
                        if (amp == -1) {
                            buf.append(entityName);
                            buf.append(';');
                            i = semi;
                        }
                    } else {
                        buf.append((char) entityValue);
                        indices.add(new int[]{i, semi});
                        i = semi;
                    }
                }
            } else {
                buf.append(ch);
            }
            i++;
        }
        return new Unescaped(buf.toString(), indices);
    }
}
