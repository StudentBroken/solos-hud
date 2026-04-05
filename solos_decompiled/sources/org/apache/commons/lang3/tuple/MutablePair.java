package org.apache.commons.lang3.tuple;

/* JADX INFO: loaded from: classes51.dex */
public class MutablePair<L, R> extends Pair<L, R> {
    private static final long serialVersionUID = 4954918890077093841L;
    public L left;
    public R right;

    public static <L, R> MutablePair<L, R> of(L left, R right) {
        return new MutablePair<>(left, right);
    }

    public MutablePair() {
    }

    public MutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override // org.apache.commons.lang3.tuple.Pair
    public L getLeft() {
        return this.left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    @Override // org.apache.commons.lang3.tuple.Pair
    public R getRight() {
        return this.right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    @Override // java.util.Map.Entry
    public R setValue(R value) {
        R result = getRight();
        setRight(value);
        return result;
    }
}
