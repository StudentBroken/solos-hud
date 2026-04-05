package org.webrtc;

import com.facebook.internal.NativeProtocol;

/* JADX INFO: loaded from: classes15.dex */
public class Size {
    public int height;
    public int width;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return this.width + "x" + this.height;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Size)) {
            return false;
        }
        Size otherSize = (Size) other;
        return this.width == otherSize.width && this.height == otherSize.height;
    }

    public int hashCode() {
        return (NativeProtocol.MESSAGE_GET_ACCESS_TOKEN_REPLY * this.width) + 1 + this.height;
    }
}
