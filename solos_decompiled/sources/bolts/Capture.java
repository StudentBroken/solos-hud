package bolts;

/* JADX INFO: loaded from: classes23.dex */
public class Capture<T> {
    private T value;

    public Capture() {
    }

    public Capture(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }
}
