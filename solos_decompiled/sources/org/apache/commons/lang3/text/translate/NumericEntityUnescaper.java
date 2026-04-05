package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

/* JADX INFO: loaded from: classes51.dex */
public class NumericEntityUnescaper extends CharSequenceTranslator {
    private final EnumSet<OPTION> options;

    public enum OPTION {
        semiColonRequired,
        semiColonOptional,
        errorIfNoSemiColon
    }

    public NumericEntityUnescaper(OPTION... options) {
        if (options.length > 0) {
            this.options = EnumSet.copyOf((Collection) Arrays.asList(options));
        } else {
            this.options = EnumSet.copyOf((Collection) Arrays.asList(OPTION.semiColonRequired));
        }
    }

    public boolean isSet(OPTION option) {
        if (this.options == null) {
            return false;
        }
        return this.options.contains(option);
    }

    @Override // org.apache.commons.lang3.text.translate.CharSequenceTranslator
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int entityValue;
        int seqEnd = input.length();
        if (input.charAt(index) == '&' && index < seqEnd - 2 && input.charAt(index + 1) == '#') {
            int start = index + 2;
            boolean isHex = false;
            char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
                if (start == seqEnd) {
                    return 0;
                }
            }
            int end = start;
            while (end < seqEnd && ((input.charAt(end) >= '0' && input.charAt(end) <= '9') || ((input.charAt(end) >= 'a' && input.charAt(end) <= 'f') || (input.charAt(end) >= 'A' && input.charAt(end) <= 'F')))) {
                end++;
            }
            boolean semiNext = end != seqEnd && input.charAt(end) == ';';
            if (!semiNext) {
                if (isSet(OPTION.semiColonRequired)) {
                    return 0;
                }
                if (isSet(OPTION.errorIfNoSemiColon)) {
                    throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
                }
            }
            try {
                if (isHex) {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
                } else {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
                }
                if (entityValue > 65535) {
                    char[] chrs = Character.toChars(entityValue);
                    out.write(chrs[0]);
                    out.write(chrs[1]);
                } else {
                    out.write(entityValue);
                }
                return (semiNext ? 1 : 0) + ((end + 2) - start) + (isHex ? 1 : 0);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
