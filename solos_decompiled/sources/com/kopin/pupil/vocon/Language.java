package com.kopin.pupil.vocon;

import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class Language {
    public static final Locale ENGLISH_AUS = new Locale("en", "AU");
    public static final Locale ENGLISH_UK = new Locale("en", "GB");
    public static final Locale ENGLISH_US = new Locale("en", "US");
    public static final Locale ARABIC_EGYPT = new Locale("ar", "EG");
    public static final Locale ARABIC_SAUDI = new Locale("ar", "SA");
    public static final Locale ARABIC_UAE = new Locale("ar", "AE");
    public static final Locale CHINESE_CAN = new Locale("zh", "HK");
    public static final Locale CHINESE_MAN = new Locale("cn", "MA");
    public static final Locale TAIWANESE_MAN = new Locale("zh", "TW");
    public static final Locale FRENCH_CAN = new Locale("fr", "CA");
    public static final Locale FRENCH_EU = new Locale("fr", "FR");
    public static final Locale PORTUGUESE_BRA = new Locale("pt", "BR");
    public static final Locale PORTUGUESE_EU = new Locale("pt", "PT");
    public static final Locale SPANISH_EU = new Locale("es", "ES");
    public static final Locale SPANISH_MEX = new Locale("es", "MX");
    public static final Locale SPANISH_US = new Locale("es", "US");
    public static final Locale CZECH = new Locale("cs", "CZ");
    public static final Locale DANISH = new Locale("da", "DK");
    public static final Locale DUTCH = new Locale("nl", "NL");
    public static final Locale FINNISH = new Locale("fi", "FI");
    public static final Locale GERNMAN = new Locale("de", "DE");
    public static final Locale GREEK = new Locale("el", "GR");
    public static final Locale HUNGARIAN = new Locale("hu", "HU");
    public static final Locale INDONESIAN = new Locale("id", "ID");
    public static final Locale ITALIAN = new Locale("it", "IT");
    public static final Locale JAPANESE = new Locale("ja", "JP");
    public static final Locale KOREAN = new Locale("ko", "KR");
    public static final Locale MALAY = new Locale("ms", "MY");
    public static final Locale NORWEIGIAN = new Locale("no", "NO");
    public static final Locale POLISH = new Locale("pl", "PL");
    public static final Locale ROMANIAN = new Locale("ro", "RO");
    public static final Locale SLOVAK = new Locale("sk", "SK");
    public static final Locale SWEDISH = new Locale("sv", "SE");
    public static final Locale TURKISH = new Locale("tr", "TR");
    public static final Locale VIETNAMESE = new Locale("vi", "VN");
    private static final Locale[] LANG_LOOKUP = {ENGLISH_US, ENGLISH_UK, ENGLISH_AUS, ARABIC_EGYPT, ARABIC_SAUDI, ARABIC_UAE, CHINESE_MAN, CHINESE_CAN, TAIWANESE_MAN, FRENCH_EU, FRENCH_CAN, PORTUGUESE_EU, PORTUGUESE_BRA, SPANISH_EU, SPANISH_MEX, SPANISH_US, CZECH, DANISH, DUTCH, FINNISH, GERNMAN, GREEK, HUNGARIAN, INDONESIAN, ITALIAN, JAPANESE, KOREAN, MALAY, NORWEIGIAN, POLISH, ROMANIAN, SLOVAK, SWEDISH, TURKISH, VIETNAMESE};

    public static Locale sanitise(String language) {
        if (language.contains("zh_CN")) {
            language = "cn_MA";
        }
        String[] codes = language.trim().replace('-', '_').split("[_]");
        Locale locale = null;
        if (codes.length > 2 || codes.length < 1) {
            return ENGLISH_US;
        }
        if (codes.length == 1) {
            locale = new Locale(codes[0]);
        } else if (codes.length == 2) {
            locale = new Locale(codes[0], codes[1]);
        }
        ArrayList<Locale> possibleMatches = new ArrayList<>();
        for (int i = 0; i < LANG_LOOKUP.length; i++) {
            if (locale.getLanguage().equalsIgnoreCase(LANG_LOOKUP[i].getLanguage())) {
                possibleMatches.add(LANG_LOOKUP[i]);
            }
        }
        if (possibleMatches.size() == 1) {
            return locale;
        }
        if (possibleMatches.size() == 0) {
            return ENGLISH_US;
        }
        if (locale.getCountry() != null) {
            for (int i2 = 0; i2 < possibleMatches.size(); i2++) {
                if (locale.getCountry().equals(possibleMatches.get(i2).getCountry())) {
                    return locale;
                }
            }
        }
        return possibleMatches.get(0);
    }

    public static Locale getDefaultLanguage() {
        Locale locale = Locale.getDefault();
        return sanitise(locale.toString());
    }
}
