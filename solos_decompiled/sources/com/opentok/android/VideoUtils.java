package com.opentok.android;

/* JADX INFO: loaded from: classes15.dex */
public class VideoUtils {

    public static class Size {
        public int height;
        public int width;

        public Size() {
        }

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public Size(Size size) {
            this.width = size.width;
            this.height = size.height;
        }

        public final void set(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public final void set(Size size) {
            this.width = size.width;
            this.height = size.height;
        }

        public final boolean equals(int width, int height) {
            return this.width == width && this.height == height;
        }

        public final boolean equals(Object obj) {
            return (obj instanceof Size) && (obj == this || equals(((Size) obj).width, ((Size) obj).height));
        }
    }
}
