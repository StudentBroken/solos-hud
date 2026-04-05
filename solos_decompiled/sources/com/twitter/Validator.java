package com.twitter;

import com.twitter.Extractor;
import java.text.Normalizer;

/* JADX INFO: loaded from: classes60.dex */
public class Validator {
    public static final int MAX_TWEET_LENGTH = 140;
    protected int shortUrlLength = 23;
    protected int shortUrlLengthHttps = 23;
    private Extractor extractor = new Extractor();

    public int getTweetLength(String text) {
        String text2 = Normalizer.normalize(text, Normalizer.Form.NFC);
        int length = text2.codePointCount(0, text2.length());
        for (Extractor.Entity urlEntity : this.extractor.extractURLsWithIndices(text2)) {
            length = length + (urlEntity.start - urlEntity.end) + (urlEntity.value.toLowerCase().startsWith("https://") ? this.shortUrlLengthHttps : this.shortUrlLength);
        }
        return length;
    }

    public boolean isValidTweet(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        char[] arr$ = text.toCharArray();
        for (char c : arr$) {
            if (c == 65534 || c == 65279 || c == 65535) {
                return false;
            }
            if (c >= 8234 && c <= 8238) {
                return false;
            }
        }
        return getTweetLength(text) <= 140;
    }

    public int getShortUrlLength() {
        return this.shortUrlLength;
    }

    public void setShortUrlLength(int shortUrlLength) {
        this.shortUrlLength = shortUrlLength;
    }

    public int getShortUrlLengthHttps() {
        return this.shortUrlLengthHttps;
    }

    public void setShortUrlLengthHttps(int shortUrlLengthHttps) {
        this.shortUrlLengthHttps = shortUrlLengthHttps;
    }
}
