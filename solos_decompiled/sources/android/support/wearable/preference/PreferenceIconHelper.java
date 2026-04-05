package android.support.wearable.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.support.wearable.R;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public final class PreferenceIconHelper {
    public static void wrapAllIconsInGroup(PreferenceGroup group) {
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference p = group.getPreference(i);
            wrapIcon(p);
            if (p instanceof PreferenceGroup) {
                wrapAllIconsInGroup((PreferenceGroup) p);
            }
        }
    }

    public static void wrapIcon(Preference p) {
        Drawable icon = p.getIcon();
        if (icon != null) {
            p.setIcon(wrapIcon(p.getContext(), icon));
        }
    }

    public static Drawable wrapIcon(Context context, Drawable icon) {
        if (!(icon instanceof LayerDrawable) || ((LayerDrawable) icon).findDrawableByLayerId(R.id.nested_icon) == null) {
            LayerDrawable wrappedDrawable = (LayerDrawable) context.getDrawable(R.drawable.preference_wrapped_icon);
            wrappedDrawable.setDrawableByLayerId(R.id.nested_icon, icon);
            return wrappedDrawable;
        }
        return icon;
    }

    private PreferenceIconHelper() {
        throw new IllegalStateException("cannot instantiate utility class");
    }
}
