package com.kopin.pupil.pagerenderer;

import android.graphics.Paint;
import com.kopin.pupil.util.MutableInt;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes25.dex */
public class StringHelper {
    private static final String ELLIPSES = "...";

    public static List<String> wrap(int startPosition, String text, Paint textPaint, int width, int height, boolean ellipse, MutableInt out_endPosition, boolean paging) {
        int lineBreak;
        ArrayList<String> lines = new ArrayList<>();
        if (text != null && text.length() != 0) {
            String[] chunks = text.split("\n");
            int length = chunks.length;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= length) {
                    break;
                }
                boolean hasMore = true;
                int current = 0;
                int nextSpace = -1;
                int blockHeight = 0;
                String chunk = chunks[i2].substring(startPosition);
                while (true) {
                    if (hasMore) {
                        int wordCount = 0;
                        while (true) {
                            lineBreak = nextSpace;
                            if (lineBreak == chunk.length() - 1) {
                                hasMore = false;
                                if (out_endPosition != null) {
                                    out_endPosition.setValue(chunk.length());
                                }
                            } else {
                                nextSpace = chunk.indexOf(32, lineBreak + 1);
                                if (nextSpace == -1) {
                                    nextSpace = chunk.length() - 1;
                                }
                                int lineWidth = (int) textPaint.measureText(chunk, current, nextSpace);
                                if (lineWidth > width) {
                                    if (wordCount >= 1) {
                                        break;
                                    }
                                    MutableInt tempBlockHeight = new MutableInt(blockHeight);
                                    current = wrapWord(chunk, textPaint, width, height, tempBlockHeight, ellipse, lines, current, nextSpace, paging);
                                    if (current == -1) {
                                        if (paging) {
                                            int size = 0;
                                            for (String string : lines) {
                                                size += string.length();
                                                if (string.endsWith(ELLIPSES)) {
                                                    size -= ELLIPSES.length();
                                                }
                                            }
                                            if (out_endPosition != null) {
                                                out_endPosition.setValue(size);
                                            }
                                        } else if (out_endPosition != null) {
                                            out_endPosition.setValue(-1);
                                        }
                                    } else {
                                        blockHeight = tempBlockHeight.getValue();
                                    }
                                }
                                wordCount++;
                            }
                        }
                        blockHeight = (int) (blockHeight + textPaint.getFontSpacing());
                        if (blockHeight > height) {
                            if (ellipse) {
                                int distance = handleBlockEnd(textPaint, width, lines, paging);
                                if (out_endPosition != null) {
                                    out_endPosition.setValue(current - distance);
                                }
                            }
                        } else {
                            String line = chunk.substring(current, lineBreak + 1);
                            lines.add(line);
                            current = lineBreak + 1;
                        }
                    }
                }
                i = i2 + 1;
            }
        }
        return lines;
    }

    private static int handleBlockEnd(Paint textPaint, int width, ArrayList<String> lines, boolean showFullWords) {
        if (lines.size() < 1) {
            return 0;
        }
        String preTruncatedLine = lines.get(lines.size() - 1);
        int ellipsesLength = (int) textPaint.measureText(ELLIPSES);
        String line = preTruncatedLine.replaceAll("\\s+$", "");
        int distance = preTruncatedLine.length() - line.length();
        do {
            if (textPaint.measureText(line) + ellipsesLength < width && line.charAt(line.length() - 1) != '.') {
                break;
            }
            if (showFullWords) {
                String line2 = line.replaceAll("\\s+$", "");
                int lastSpace = line2.lastIndexOf(32);
                if (lastSpace != -1) {
                    int preTruncatedLength = line2.length();
                    line = line2.substring(0, lastSpace);
                    distance += preTruncatedLength - line.length();
                } else {
                    line = line2.substring(0, line2.length() - 1);
                }
            } else {
                line = line.substring(0, line.length() - 1).replaceAll("\\s+$", "");
                distance++;
            }
        } while (line.length() >= 1);
        lines.set(lines.size() - 1, line + ELLIPSES);
        return distance;
    }

    private static int wrapWord(String text, Paint textPaint, int width, int height, MutableInt tempBlockHeight, boolean ellipse, ArrayList<String> lines, int current, int nextSpace, boolean showFullWords) {
        while (true) {
            int textWidth = textPaint.breakText(text, current, nextSpace, true, width, null);
            while (textPaint.measureText(text.substring(current, current + textWidth + 1)) > width) {
                textWidth--;
            }
            tempBlockHeight.setValue(tempBlockHeight.getValue() + textPaint.getFontSpacing());
            if (tempBlockHeight.getValue() > height) {
                if (ellipse) {
                    handleBlockEnd(textPaint, width, lines, showFullWords);
                    return -1;
                }
            } else {
                String line = text.substring(current, current + textWidth + 1);
                lines.add(line);
                current += textWidth + 1;
                if (textPaint.measureText(text, current, nextSpace) <= width) {
                    return current;
                }
            }
        }
    }
}
