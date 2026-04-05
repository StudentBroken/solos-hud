package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: classes51.dex */
public class OctalUnescaper extends CharSequenceTranslator {
    private static int OCTAL_MAX = 377;

    @Override // org.apache.commons.lang3.text.translate.CharSequenceTranslator
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input.charAt(index) != '\\' || index >= input.length() - 1 || !Character.isDigit(input.charAt(index + 1))) {
            return 0;
        }
        int start = index + 1;
        int end = index + 2;
        while (true) {
            if (end >= input.length() || !Character.isDigit(input.charAt(end))) {
                break;
            }
            end++;
            if (Integer.parseInt(input.subSequence(start, end).toString(), 10) > OCTAL_MAX) {
                end--;
                break;
            }
        }
        out.write(Integer.parseInt(input.subSequence(start, end).toString(), 8));
        return (end + 1) - start;
    }
}
