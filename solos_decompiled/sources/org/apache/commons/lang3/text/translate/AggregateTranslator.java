package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.ArrayUtils;

/* JADX INFO: loaded from: classes51.dex */
public class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
        this.translators = (CharSequenceTranslator[]) ArrayUtils.clone(translators);
    }

    @Override // org.apache.commons.lang3.text.translate.CharSequenceTranslator
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        CharSequenceTranslator[] arr$ = this.translators;
        for (CharSequenceTranslator translator : arr$) {
            int consumed = translator.translate(input, index, out);
            if (consumed != 0) {
                return consumed;
            }
        }
        return 0;
    }
}
