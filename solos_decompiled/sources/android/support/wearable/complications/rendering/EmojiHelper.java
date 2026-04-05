package android.support.wearable.complications.rendering;

import android.support.annotation.Nullable;

/* JADX INFO: loaded from: classes33.dex */
final class EmojiHelper {
    private static final int[][] EMOJI_RANGES = {new int[]{169, 169}, new int[]{8252, 8252}, new int[]{8265, 8265}, new int[]{8482, 8482}, new int[]{8505, 8505}, new int[]{8596, 8618}, new int[]{8986, 9000}, new int[]{9167, 9210}, new int[]{9410, 9410}, new int[]{9642, 10175}, new int[]{10548, 10549}, new int[]{11013, 11093}, new int[]{12336, 12336}, new int[]{12349, 12349}, new int[]{12951, 12953}, new int[]{126980, 126980}, new int[]{127183, 127183}, new int[]{127344, 127569}, new int[]{127744, 128758}, new int[]{129296, 129472}};

    private EmojiHelper() {
    }

    static boolean isEmoji(int charCode) {
        for (int[] range : EMOJI_RANGES) {
            if (charCode >= range[0] && charCode <= range[1]) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    static String replaceEmoji(@Nullable CharSequence text, int newCharacter) {
        if (text == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder(text.length());
        int length = text.length();
        boolean isPreviousCharacterEmoji = false;
        int offset = 0;
        while (offset < length) {
            int codePoint = Character.codePointAt(text, offset);
            if (!isEmoji(codePoint)) {
                buffer.appendCodePoint(codePoint);
            } else if (!isPreviousCharacterEmoji) {
                buffer.appendCodePoint(newCharacter);
            }
            isPreviousCharacterEmoji = isEmoji(codePoint);
            offset += Character.charCount(codePoint);
        }
        return buffer.toString();
    }
}
