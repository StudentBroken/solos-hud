package com.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

/* JADX INFO: loaded from: classes60.dex */
public class Extractor {
    protected boolean extractURLWithoutProtocol = true;

    public static class Entity {
        protected String displayURL;
        protected int end;
        protected String expandedURL;
        protected final String listSlug;
        protected int start;
        protected final Type type;
        protected final String value;

        public enum Type {
            URL,
            HASHTAG,
            MENTION,
            CASHTAG
        }

        public Entity(int start, int end, String value, String listSlug, Type type) {
            this.displayURL = null;
            this.expandedURL = null;
            this.start = start;
            this.end = end;
            this.value = value;
            this.listSlug = listSlug;
            this.type = type;
        }

        public Entity(int start, int end, String value, Type type) {
            this(start, end, value, null, type);
        }

        public Entity(Matcher matcher, Type type, int groupNumber) {
            this(matcher, type, groupNumber, -1);
        }

        public Entity(Matcher matcher, Type type, int groupNumber, int startOffset) {
            this(matcher.start(groupNumber) + startOffset, matcher.end(groupNumber), matcher.group(groupNumber), type);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entity)) {
                return false;
            }
            Entity other = (Entity) obj;
            return this.type.equals(other.type) && this.start == other.start && this.end == other.end && this.value.equals(other.value);
        }

        public int hashCode() {
            return this.type.hashCode() + this.value.hashCode() + this.start + this.end;
        }

        public String toString() {
            return this.value + "(" + this.type + ") [" + this.start + "," + this.end + "]";
        }

        public Integer getStart() {
            return Integer.valueOf(this.start);
        }

        public Integer getEnd() {
            return Integer.valueOf(this.end);
        }

        public String getValue() {
            return this.value;
        }

        public String getListSlug() {
            return this.listSlug;
        }

        public Type getType() {
            return this.type;
        }

        public String getDisplayURL() {
            return this.displayURL;
        }

        public void setDisplayURL(String displayURL) {
            this.displayURL = displayURL;
        }

        public String getExpandedURL() {
            return this.expandedURL;
        }

        public void setExpandedURL(String expandedURL) {
            this.expandedURL = expandedURL;
        }
    }

    private void removeOverlappingEntities(List<Entity> entities) {
        Collections.sort(entities, new Comparator<Entity>() { // from class: com.twitter.Extractor.1
            @Override // java.util.Comparator
            public int compare(Entity e1, Entity e2) {
                return e1.start - e2.start;
            }
        });
        if (!entities.isEmpty()) {
            Iterator<Entity> it = entities.iterator();
            Entity prev = it.next();
            while (it.hasNext()) {
                Entity cur = it.next();
                if (prev.getEnd().intValue() > cur.getStart().intValue()) {
                    it.remove();
                } else {
                    prev = cur;
                }
            }
        }
    }

    public List<Entity> extractEntitiesWithIndices(String text) {
        List<Entity> entities = new ArrayList<>();
        entities.addAll(extractURLsWithIndices(text));
        entities.addAll(extractHashtagsWithIndices(text, false));
        entities.addAll(extractMentionsOrListsWithIndices(text));
        entities.addAll(extractCashtagsWithIndices(text));
        removeOverlappingEntities(entities);
        return entities;
    }

    public List<String> extractMentionedScreennames(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        List<String> extracted = new ArrayList<>();
        for (Entity entity : extractMentionedScreennamesWithIndices(text)) {
            extracted.add(entity.value);
        }
        return extracted;
    }

    public List<Entity> extractMentionedScreennamesWithIndices(String text) {
        List<Entity> extracted = new ArrayList<>();
        for (Entity entity : extractMentionsOrListsWithIndices(text)) {
            if (entity.listSlug == null) {
                extracted.add(entity);
            }
        }
        return extracted;
    }

    public List<Entity> extractMentionsOrListsWithIndices(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        boolean found = false;
        char[] arr$ = text.toCharArray();
        for (char c : arr$) {
            if (c == '@' || c == 65312) {
                found = true;
                break;
            }
        }
        if (!found) {
            return Collections.emptyList();
        }
        List<Entity> extracted = new ArrayList<>();
        Matcher matcher = Regex.VALID_MENTION_OR_LIST.matcher(text);
        while (matcher.find()) {
            String after = text.substring(matcher.end());
            if (!Regex.INVALID_MENTION_MATCH_END.matcher(after).find()) {
                if (matcher.group(4) == null) {
                    extracted.add(new Entity(matcher, Entity.Type.MENTION, 3));
                } else {
                    extracted.add(new Entity(matcher.start(3) - 1, matcher.end(4), matcher.group(3), matcher.group(4), Entity.Type.MENTION));
                }
            }
        }
        return extracted;
    }

    public String extractReplyScreenname(String text) {
        if (text == null) {
            return null;
        }
        Matcher matcher = Regex.VALID_REPLY.matcher(text);
        if (!matcher.find()) {
            return null;
        }
        String after = text.substring(matcher.end());
        if (Regex.INVALID_MENTION_MATCH_END.matcher(after).find()) {
            return null;
        }
        return matcher.group(1);
    }

    public List<String> extractURLs(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        List<String> urls = new ArrayList<>();
        for (Entity entity : extractURLsWithIndices(text)) {
            urls.add(entity.value);
        }
        return urls;
    }

    public List<Entity> extractURLsWithIndices(String text) {
        if (text != null && text.length() != 0) {
            if ((this.extractURLWithoutProtocol ? text.indexOf(46) : text.indexOf(58)) != -1) {
                List<Entity> urls = new ArrayList<>();
                Matcher matcher = Regex.VALID_URL.matcher(text);
                while (matcher.find()) {
                    if (matcher.group(4) != null || (this.extractURLWithoutProtocol && !Regex.INVALID_URL_WITHOUT_PROTOCOL_MATCH_BEGIN.matcher(matcher.group(2)).matches())) {
                        String url = matcher.group(3);
                        int start = matcher.start(3);
                        int end = matcher.end(3);
                        Matcher tco_matcher = Regex.VALID_TCO_URL.matcher(url);
                        if (tco_matcher.find()) {
                            url = tco_matcher.group();
                            end = start + url.length();
                        }
                        urls.add(new Entity(start, end, url, Entity.Type.URL));
                    }
                }
                return urls;
            }
        }
        return Collections.emptyList();
    }

    public List<String> extractHashtags(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        List<String> extracted = new ArrayList<>();
        for (Entity entity : extractHashtagsWithIndices(text)) {
            extracted.add(entity.value);
        }
        return extracted;
    }

    public List<Entity> extractHashtagsWithIndices(String text) {
        return extractHashtagsWithIndices(text, true);
    }

    private List<Entity> extractHashtagsWithIndices(String text, boolean checkUrlOverlap) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        boolean found = false;
        char[] arr$ = text.toCharArray();
        for (char c : arr$) {
            if (c == '#' || c == 65283) {
                found = true;
                break;
            }
        }
        if (!found) {
            return Collections.emptyList();
        }
        List<Entity> extracted = new ArrayList<>();
        Matcher matcher = Regex.VALID_HASHTAG.matcher(text);
        while (matcher.find()) {
            String after = text.substring(matcher.end());
            if (!Regex.INVALID_HASHTAG_MATCH_END.matcher(after).find()) {
                extracted.add(new Entity(matcher, Entity.Type.HASHTAG, 3));
            }
        }
        if (checkUrlOverlap) {
            List<Entity> urls = extractURLsWithIndices(text);
            if (!urls.isEmpty()) {
                extracted.addAll(urls);
                removeOverlappingEntities(extracted);
                Iterator<Entity> it = extracted.iterator();
                while (it.hasNext()) {
                    Entity entity = it.next();
                    if (entity.getType() != Entity.Type.HASHTAG) {
                        it.remove();
                    }
                }
                return extracted;
            }
            return extracted;
        }
        return extracted;
    }

    public List<String> extractCashtags(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        List<String> extracted = new ArrayList<>();
        for (Entity entity : extractCashtagsWithIndices(text)) {
            extracted.add(entity.value);
        }
        return extracted;
    }

    public List<Entity> extractCashtagsWithIndices(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        if (text.indexOf(36) == -1) {
            return Collections.emptyList();
        }
        List<Entity> extracted = new ArrayList<>();
        Matcher matcher = Regex.VALID_CASHTAG.matcher(text);
        while (matcher.find()) {
            extracted.add(new Entity(matcher, Entity.Type.CASHTAG, 3));
        }
        return extracted;
    }

    public void setExtractURLWithoutProtocol(boolean extractURLWithoutProtocol) {
        this.extractURLWithoutProtocol = extractURLWithoutProtocol;
    }

    public boolean isExtractURLWithoutProtocol() {
        return this.extractURLWithoutProtocol;
    }

    public void modifyIndicesFromUnicodeToUTF16(String text, List<Entity> entities) {
        IndexConverter convert = new IndexConverter(text);
        for (Entity entity : entities) {
            entity.start = convert.codePointsToCodeUnits(entity.start);
            entity.end = convert.codePointsToCodeUnits(entity.end);
        }
    }

    public void modifyIndicesFromUTF16ToToUnicode(String text, List<Entity> entities) {
        IndexConverter convert = new IndexConverter(text);
        for (Entity entity : entities) {
            entity.start = convert.codeUnitsToCodePoints(entity.start);
            entity.end = convert.codeUnitsToCodePoints(entity.end);
        }
    }

    private static final class IndexConverter {
        protected final String text;
        protected int codePointIndex = 0;
        protected int charIndex = 0;

        IndexConverter(String text) {
            this.text = text;
        }

        int codeUnitsToCodePoints(int charIndex) {
            if (charIndex < this.charIndex) {
                this.codePointIndex -= this.text.codePointCount(charIndex, this.charIndex);
            } else {
                this.codePointIndex += this.text.codePointCount(this.charIndex, charIndex);
            }
            this.charIndex = charIndex;
            if (charIndex > 0 && Character.isSupplementaryCodePoint(this.text.codePointAt(charIndex - 1))) {
                this.charIndex--;
            }
            return this.codePointIndex;
        }

        int codePointsToCodeUnits(int codePointIndex) {
            this.charIndex = this.text.offsetByCodePoints(this.charIndex, codePointIndex - this.codePointIndex);
            this.codePointIndex = codePointIndex;
            return this.charIndex;
        }
    }
}
