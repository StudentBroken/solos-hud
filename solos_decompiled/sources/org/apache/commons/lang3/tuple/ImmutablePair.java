package org.apache.commons.lang3.tuple;

/* JADX INFO: loaded from: classes51.dex */
public final class ImmutablePair<L, R> extends Pair<L, R> {
    private static final long serialVersionUID = 4954918890077093841L;
    public final L left;
    public final R right;

    public static <L, R> ImmutablePair<L, R> of(L left, R right) {
        return new ImmutablePair<>(left, right);
    }

    public ImmutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override // org.apache.commons.lang3.tuple.Pair
    public L getLeft() {
        return this.left;
    }

    @Override // org.apache.commons.lang3.tuple.Pair
    public R getRight() {
        return this.right;
    }

    @Override // java.util.Map.Entry
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }
}
