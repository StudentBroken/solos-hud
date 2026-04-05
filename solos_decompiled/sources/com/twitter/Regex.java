package com.twitter;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes60.dex */
public class Regex {
    public static final Pattern AT_SIGNS;
    private static final String AT_SIGNS_CHARS = "@＠";
    private static final String CASHTAG = "[a-z]{1,6}(?:[._][a-z]{1,2})?";
    private static final String DOLLAR_SIGN_CHAR = "\\$";
    private static final String HASHTAG_LETTERS = "\\p{L}\\p{M}";
    private static final String HASHTAG_LETTERS_NUMERALS = "\\p{L}\\p{M}\\p{Nd}_\\u200c\\u200d\\ua67e\\u05be\\u05f3\\u05f4\\u309b\\u309c\\u30a0\\u30fb\\u3003\\u0f0b\\u0f0c\\u00b7";
    private static final String HASHTAG_LETTERS_NUMERALS_SET = "[\\p{L}\\p{M}\\p{Nd}_\\u200c\\u200d\\ua67e\\u05be\\u05f3\\u05f4\\u309b\\u309c\\u30a0\\u30fb\\u3003\\u0f0b\\u0f0c\\u00b7]";
    private static final String HASHTAG_LETTERS_SET = "[\\p{L}\\p{M}]";
    private static final String HASHTAG_NUMERALS = "\\p{Nd}";
    private static final String HASHTAG_SPECIAL_CHARS = "_\\u200c\\u200d\\ua67e\\u05be\\u05f3\\u05f4\\u309b\\u309c\\u30a0\\u30fb\\u3003\\u0f0b\\u0f0c\\u00b7";
    public static final Pattern INVALID_HASHTAG_MATCH_END;
    public static final Pattern INVALID_MENTION_MATCH_END;
    public static final Pattern INVALID_URL_WITHOUT_PROTOCOL_MATCH_BEGIN;
    private static final String LATIN_ACCENTS_CHARS = "\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff";
    public static final Pattern RTL_CHARACTERS;
    private static final String UNICODE_SPACES = "[\\u0009-\\u000d\\u0020\\u0085\\u00a0\\u1680\\u180E\\u2000-\\u200a\\u2028\\u2029\\u202F\\u205F\\u3000]";
    private static final String URL_BALANCED_PARENS = "\\((?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+|(?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*\\([a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+\\)[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*))\\)";
    private static final String URL_VALID_CHARS = "[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]";
    private static final String URL_VALID_GENERAL_PATH_CHARS = "[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]";
    private static final String URL_VALID_PATH_ENDING_CHARS = "[a-z0-9=_#/\\-\\+\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]|(?:\\((?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+|(?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*\\([a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+\\)[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*))\\))";
    private static final String URL_VALID_PRECEEDING_CHARS = "(?:[^A-Z0-9@＠$#＃\u202a-\u202e]|^)";
    private static final String URL_VALID_SUBDOMAIN = "(?>(?:[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff][[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\-_]*)?[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\.)";
    public static final Pattern VALID_CASHTAG;
    public static final int VALID_CASHTAG_GROUP_BEFORE = 1;
    public static final int VALID_CASHTAG_GROUP_CASHTAG = 3;
    public static final int VALID_CASHTAG_GROUP_DOLLAR = 2;
    public static final Pattern VALID_DOMAIN;
    public static final Pattern VALID_HASHTAG;
    public static final int VALID_HASHTAG_GROUP_BEFORE = 1;
    public static final int VALID_HASHTAG_GROUP_HASH = 2;
    public static final int VALID_HASHTAG_GROUP_TAG = 3;
    public static final Pattern VALID_MENTION_OR_LIST;
    public static final int VALID_MENTION_OR_LIST_GROUP_AT = 2;
    public static final int VALID_MENTION_OR_LIST_GROUP_BEFORE = 1;
    public static final int VALID_MENTION_OR_LIST_GROUP_LIST = 4;
    public static final int VALID_MENTION_OR_LIST_GROUP_USERNAME = 3;
    public static final Pattern VALID_REPLY;
    public static final int VALID_REPLY_GROUP_USERNAME = 1;
    public static final Pattern VALID_TCO_URL;
    public static final Pattern VALID_URL;
    public static final int VALID_URL_GROUP_ALL = 1;
    public static final int VALID_URL_GROUP_BEFORE = 2;
    public static final int VALID_URL_GROUP_DOMAIN = 5;
    public static final int VALID_URL_GROUP_PATH = 7;
    public static final int VALID_URL_GROUP_PORT = 6;
    public static final int VALID_URL_GROUP_PROTOCOL = 4;
    public static final int VALID_URL_GROUP_QUERY_STRING = 8;
    public static final int VALID_URL_GROUP_URL = 3;
    private static final String URL_VALID_GTLD = "(?:(?:" + join(TldLists.GTLDS, "|") + ")(?=[^\\p{Alnum}@]|$))";
    private static final String URL_VALID_CCTLD = "(?:(?:" + join(TldLists.CTLDS, "|") + ")(?=[^\\p{Alnum}@]|$))";
    private static final String URL_PUNYCODE = "(?:xn--[0-9a-z]+)";
    private static final String URL_VALID_DOMAIN_NAME = "(?:(?:[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff][[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\-]*)?[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\.)";
    private static final String SPECIAL_URL_VALID_CCTLD = "(?:(?:co|tv)(?=[^\\p{Alnum}@]|$))";
    private static final String URL_VALID_UNICODE_CHARS = "[.[^\\p{Punct}\\s\\p{Z}\\p{InGeneralPunctuation}]]";
    private static final String URL_VALID_DOMAIN = "(?:(?>(?:[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff][[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\-_]*)?[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\.)+(?:(?:[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff][[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\-]*)?[\\p{Alnum}\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]\\.)(?:" + URL_VALID_GTLD + "|" + URL_VALID_CCTLD + "|" + URL_PUNYCODE + "))|(?:" + URL_VALID_DOMAIN_NAME + "(?:" + URL_VALID_GTLD + "|" + URL_PUNYCODE + "|" + SPECIAL_URL_VALID_CCTLD + "))|(?:(?<=https?://)(?:(?:" + URL_VALID_DOMAIN_NAME + URL_VALID_CCTLD + ")|(?:" + URL_VALID_UNICODE_CHARS + "+\\.(?:" + URL_VALID_GTLD + "|" + URL_VALID_CCTLD + "))))|(?:" + URL_VALID_DOMAIN_NAME + URL_VALID_CCTLD + "(?=/))";
    private static final String URL_VALID_PORT_NUMBER = "[0-9]++";
    private static final String URL_VALID_PATH = "(?:(?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*(?:\\((?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+|(?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*\\([a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+\\)[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*))\\)[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*)*[a-z0-9=_#/\\-\\+\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]|(?:\\((?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+|(?:[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*\\([a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+\\)[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]*))\\)))|(?:@[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]+/))";
    private static final String URL_VALID_URL_QUERY_CHARS = "[a-z0-9!?\\*'\\(\\);:&=\\+\\$/%#\\[\\]\\-_\\.,~\\|@]";
    private static final String URL_VALID_URL_QUERY_ENDING_CHARS = "[a-z0-9_&=#/]";
    private static final String VALID_URL_PATTERN_STRING = "(((?:[^A-Z0-9@＠$#＃\u202a-\u202e]|^))((https?://)?(" + URL_VALID_DOMAIN + ")(?::(" + URL_VALID_PORT_NUMBER + "))?(/" + URL_VALID_PATH + "*+)?(\\?" + URL_VALID_URL_QUERY_CHARS + "*" + URL_VALID_URL_QUERY_ENDING_CHARS + ")?))";

    static {
        synchronized (Regex.class) {
            VALID_HASHTAG = Pattern.compile("(^|[^&\\p{L}\\p{M}\\p{Nd}_\\u200c\\u200d\\ua67e\\u05be\\u05f3\\u05f4\\u309b\\u309c\\u30a0\\u30fb\\u3003\\u0f0b\\u0f0c\\u00b7])(#|＃)([\\p{L}\\p{M}\\p{Nd}_\\u200c\\u200d\\ua67e\\u05be\\u05f3\\u05f4\\u309b\\u309c\\u30a0\\u30fb\\u3003\\u0f0b\\u0f0c\\u00b7]*[\\p{L}\\p{M}][\\p{L}\\p{M}\\p{Nd}_\\u200c\\u200d\\ua67e\\u05be\\u05f3\\u05f4\\u309b\\u309c\\u30a0\\u30fb\\u3003\\u0f0b\\u0f0c\\u00b7]*)", 2);
            INVALID_HASHTAG_MATCH_END = Pattern.compile("^(?:[#＃]|://)");
            RTL_CHARACTERS = Pattern.compile("[\u0600-ۿݐ-ݿ\u0590-\u05ffﹰ-\ufeff]");
            AT_SIGNS = Pattern.compile("[@＠]");
            VALID_MENTION_OR_LIST = Pattern.compile("([^a-z0-9_!#$%&*@＠]|^|(?:^|[^a-z0-9_+~.-])RT:?)(" + AT_SIGNS + "+)([a-z0-9_]{1,20})(/[a-z][a-z0-9_\\-]{0,24})?", 2);
            VALID_REPLY = Pattern.compile("^(?:[\\u0009-\\u000d\\u0020\\u0085\\u00a0\\u1680\\u180E\\u2000-\\u200a\\u2028\\u2029\\u202F\\u205F\\u3000])*" + AT_SIGNS + "([a-z0-9_]{1,20})", 2);
            INVALID_MENTION_MATCH_END = Pattern.compile("^(?:[@＠\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff]|://)");
            INVALID_URL_WITHOUT_PROTOCOL_MATCH_BEGIN = Pattern.compile("[-_./]$");
            VALID_URL = Pattern.compile(VALID_URL_PATTERN_STRING, 2);
            VALID_TCO_URL = Pattern.compile("^https?:\\/\\/t\\.co\\/[a-z0-9]+", 2);
            VALID_CASHTAG = Pattern.compile("(^|[\\u0009-\\u000d\\u0020\\u0085\\u00a0\\u1680\\u180E\\u2000-\\u200a\\u2028\\u2029\\u202F\\u205F\\u3000])(\\$)([a-z]{1,6}(?:[._][a-z]{1,2})?)(?=$|\\s|\\p{Punct})", 2);
            VALID_DOMAIN = Pattern.compile(URL_VALID_DOMAIN, 2);
        }
    }

    private static String join(Collection<?> col, String delim) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = col.iterator();
        if (iter.hasNext()) {
            sb.append(iter.next().toString());
        }
        while (iter.hasNext()) {
            sb.append(delim);
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }
}
