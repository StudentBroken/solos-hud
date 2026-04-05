package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes51.dex */
final class CharRange implements Iterable<Character>, Serializable {
    private static final long serialVersionUID = 8270183163158333422L;
    private final char end;
    private transient String iToString;
    private final boolean negated;
    private final char start;

    private CharRange(char start, char end, boolean negated) {
        if (start > end) {
            start = end;
            end = start;
        }
        this.start = start;
        this.end = end;
        this.negated = negated;
    }

    public static CharRange is(char ch) {
        return new CharRange(ch, ch, false);
    }

    public static CharRange isNot(char ch) {
        return new CharRange(ch, ch, true);
    }

    public static CharRange isIn(char start, char end) {
        return new CharRange(start, end, false);
    }

    public static CharRange isNotIn(char start, char end) {
        return new CharRange(start, end, true);
    }

    public char getStart() {
        return this.start;
    }

    public char getEnd() {
        return this.end;
    }

    public boolean isNegated() {
        return this.negated;
    }

    public boolean contains(char ch) {
        return (ch >= this.start && ch <= this.end) != this.negated;
    }

    public boolean contains(CharRange range) {
        if (range == null) {
            throw new IllegalArgumentException("The Range must not be null");
        }
        if (!this.negated) {
            return range.negated ? this.start == 0 && this.end == 65535 : this.start <= range.start && this.end >= range.end;
        }
        if (range.negated) {
            return this.start >= range.start && this.end <= range.end;
        }
        return range.end < this.start || range.start > this.end;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharRange)) {
            return false;
        }
        CharRange other = (CharRange) obj;
        return this.start == other.start && this.end == other.end && this.negated == other.negated;
    }

    public int hashCode() {
        return (this.negated ? 1 : 0) + (this.end * 7) + this.start + 'S';
    }

    public String toString() {
        if (this.iToString == null) {
            StringBuilder buf = new StringBuilder(4);
            if (isNegated()) {
                buf.append('^');
            }
            buf.append(this.start);
            if (this.start != this.end) {
                buf.append('-');
                buf.append(this.end);
            }
            this.iToString = buf.toString();
        }
        return this.iToString;
    }

    @Override // java.lang.Iterable
    public Iterator<Character> iterator() {
        return new CharacterIterator();
    }

    private static class CharacterIterator implements Iterator<Character> {
        private char current;
        private boolean hasNext;
        private final CharRange range;

        private CharacterIterator(CharRange r) {
            this.range = r;
            this.hasNext = true;
            if (this.range.negated) {
                if (this.range.start == 0) {
                    if (this.range.end != 65535) {
                        this.current = (char) (this.range.end + 1);
                        return;
                    } else {
                        this.hasNext = false;
                        return;
                    }
                }
                this.current = (char) 0;
                return;
            }
            this.current = this.range.start;
        }

        private void prepareNext() {
            if (!this.range.negated) {
                if (this.current < this.range.end) {
                    this.current = (char) (this.current + 1);
                    return;
                } else {
                    this.hasNext = false;
                    return;
                }
            }
            if (this.current != 65535) {
                if (this.current + 1 == this.range.start) {
                    if (this.range.end != 65535) {
                        this.current = (char) (this.range.end + 1);
                        return;
                    } else {
                        this.hasNext = false;
                        return;
                    }
                }
                this.current = (char) (this.current + 1);
                return;
            }
            this.hasNext = false;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasNext;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Character next() {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            char cur = this.current;
            prepareNext();
            return Character.valueOf(cur);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
