package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/* JADX INFO: loaded from: classes51.dex */
public class LookupTranslator extends CharSequenceTranslator {
    private final int longest;
    private final HashMap<CharSequence, CharSequence> lookupMap = new HashMap<>();
    private final int shortest;

    public LookupTranslator(CharSequence[]... lookup) {
        int _shortest = Integer.MAX_VALUE;
        int _longest = 0;
        if (lookup != null) {
            for (CharSequence[] seq : lookup) {
                this.lookupMap.put(seq[0], seq[1]);
                int sz = seq[0].length();
                _shortest = sz < _shortest ? sz : _shortest;
                if (sz > _longest) {
                    _longest = sz;
                }
            }
        }
        this.shortest = _shortest;
        this.longest = _longest;
    }

    @Override // org.apache.commons.lang3.text.translate.CharSequenceTranslator
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int max = this.longest;
        if (this.longest + index > input.length()) {
            max = input.length() - index;
        }
        for (int i = max; i >= this.shortest; i--) {
            CharSequence subSeq = input.subSequence(index, index + i);
            CharSequence result = this.lookupMap.get(subSeq);
            if (result != null) {
                out.write(result.toString());
                return i;
            }
        }
        return 0;
    }
}
