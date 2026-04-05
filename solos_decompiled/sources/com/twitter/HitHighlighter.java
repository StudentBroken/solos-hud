package com.twitter;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

/* JADX INFO: loaded from: classes60.dex */
public class HitHighlighter {
    public static final String DEFAULT_HIGHLIGHT_TAG = "em";
    protected String highlightTag = DEFAULT_HIGHLIGHT_TAG;

    public String highlight(String text, List<List<Integer>> hits) {
        if (hits != null && !hits.isEmpty()) {
            StringBuilder sb = new StringBuilder(text.length());
            CharacterIterator iterator = new StringCharacterIterator(text);
            boolean isCounting = true;
            boolean tagOpened = false;
            int currentIndex = 0;
            for (char currentChar = iterator.first(); currentChar != 65535; currentChar = iterator.next()) {
                for (List<Integer> start_end : hits) {
                    if (start_end.get(0).intValue() == currentIndex) {
                        sb.append(tag(false));
                        tagOpened = true;
                    } else if (start_end.get(1).intValue() == currentIndex) {
                        sb.append(tag(true));
                        tagOpened = false;
                    }
                }
                if (currentChar == '<') {
                    isCounting = false;
                } else if (currentChar == '>' && !isCounting) {
                    isCounting = true;
                }
                if (isCounting) {
                    currentIndex++;
                }
                sb.append(currentChar);
            }
            if (tagOpened) {
                sb.append(tag(true));
            }
            return sb.toString();
        }
        return text;
    }

    protected String tag(boolean closeTag) {
        StringBuilder sb = new StringBuilder(this.highlightTag.length() + 3);
        sb.append("<");
        if (closeTag) {
            sb.append("/");
        }
        sb.append(this.highlightTag).append(">");
        return sb.toString();
    }

    public String getHighlightTag() {
        return this.highlightTag;
    }

    public void setHighlightTag(String highlightTag) {
        this.highlightTag = highlightTag;
    }
}
