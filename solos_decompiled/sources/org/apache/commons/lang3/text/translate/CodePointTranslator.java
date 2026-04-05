package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: classes51.dex */
public abstract class CodePointTranslator extends CharSequenceTranslator {
    public abstract boolean translate(int i, Writer writer) throws IOException;

    @Override // org.apache.commons.lang3.text.translate.CharSequenceTranslator
    public final int translate(CharSequence input, int index, Writer out) throws IOException {
        int codepoint = Character.codePointAt(input, index);
        boolean consumed = translate(codepoint, out);
        return consumed ? 1 : 0;
    }
}
