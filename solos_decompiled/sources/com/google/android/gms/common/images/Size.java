package com.google.android.gms.common.images;

import com.twitter.sdk.android.core.TwitterApiErrorConstants;

/* JADX INFO: loaded from: classes3.dex */
public final class Size {
    private final int zzrZ;
    private final int zzsa;

    public Size(int i, int i2) {
        this.zzrZ = i;
        this.zzsa = i2;
    }

    public static Size parseSize(String str) throws NumberFormatException {
        if (str == null) {
            throw new IllegalArgumentException("string must not be null");
        }
        int iIndexOf = str.indexOf(42);
        if (iIndexOf < 0) {
            iIndexOf = str.indexOf(TwitterApiErrorConstants.EMAIL_ALREADY_REGISTERED);
        }
        if (iIndexOf < 0) {
            throw zzcy(str);
        }
        try {
            return new Size(Integer.parseInt(str.substring(0, iIndexOf)), Integer.parseInt(str.substring(iIndexOf + 1)));
        } catch (NumberFormatException e) {
            throw zzcy(str);
        }
    }

    private static NumberFormatException zzcy(String str) {
        throw new NumberFormatException(new StringBuilder(String.valueOf(str).length() + 16).append("Invalid Size: \"").append(str).append("\"").toString());
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size)) {
            return false;
        }
        Size size = (Size) obj;
        return this.zzrZ == size.zzrZ && this.zzsa == size.zzsa;
    }

    public final int getHeight() {
        return this.zzsa;
    }

    public final int getWidth() {
        return this.zzrZ;
    }

    public final int hashCode() {
        return this.zzsa ^ ((this.zzrZ << 16) | (this.zzrZ >>> 16));
    }

    public final String toString() {
        int i = this.zzrZ;
        return new StringBuilder(23).append(i).append("x").append(this.zzsa).toString();
    }
}
