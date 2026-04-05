package retrofit.client;

/* JADX INFO: loaded from: classes53.dex */
public final class Header {
    private final String name;
    private final String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Header header = (Header) o;
        if (this.name == null ? header.name != null : !this.name.equals(header.name)) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(header.value)) {
                return true;
            }
        } else if (header.value == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        return (result * 31) + (this.value != null ? this.value.hashCode() : 0);
    }

    public String toString() {
        return (this.name != null ? this.name : "") + ": " + (this.value != null ? this.value : "");
    }
}
