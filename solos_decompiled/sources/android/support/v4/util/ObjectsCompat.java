package android.support.v4.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.Objects;

/* JADX INFO: loaded from: classes27.dex */
public class ObjectsCompat {
    private static final ImplBase IMPL;

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new ImplApi19();
        } else {
            IMPL = new ImplBase();
        }
    }

    private ObjectsCompat() {
    }

    public static boolean equals(Object a, Object b) {
        return IMPL.equals(a, b);
    }

    private static class ImplBase {
        private ImplBase() {
        }

        public boolean equals(Object a, Object b) {
            return a == b || (a != null && a.equals(b));
        }
    }

    @RequiresApi(19)
    private static class ImplApi19 extends ImplBase {
        private ImplApi19() {
            super();
        }

        @Override // android.support.v4.util.ObjectsCompat.ImplBase
        public boolean equals(Object a, Object b) {
            return Objects.equals(a, b);
        }
    }
}
