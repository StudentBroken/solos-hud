package com.kopin.solos.menu;

import android.content.Context;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.ShareMenuAdapter;
import com.kopin.solos.share.Platforms;

/* JADX INFO: loaded from: classes24.dex */
public class ShareMenu extends CustomActionProvider<ShareMenuAdapter.ShareMenuItem> {
    private ChoiceListener mCallback;
    private final CustomActionProvider.OnItemClickListener mItemClick;

    public interface ChoiceListener {
        void doShareRide(Platforms platforms, boolean z);

        boolean isShared(Platforms platforms);
    }

    public ShareMenu(Context context, ChoiceListener cb) {
        super(context);
        this.mItemClick = new CustomActionProvider.OnItemClickListener() { // from class: com.kopin.solos.menu.ShareMenu.2
            @Override // com.kopin.solos.menu.CustomActionProvider.OnItemClickListener
            public void onItemClick(int position, CustomMenuItem menuItem) {
                int id = menuItem.getId();
                for (Platforms p : Platforms.values()) {
                    if (p.getRequestCode() == id) {
                        ShareMenu.this.mCallback.doShareRide(p, false);
                    }
                }
            }
        };
        this.mCallback = cb;
        setMenuAdapter(new ShareMenuAdapter(context));
        for (final Platforms p : Platforms.values()) {
            ShareMenuAdapter.ShareMenuItem item = new ShareMenuAdapter.ShareMenuItem(context.getString(p.getMenuTitleResId()), p.getMenuIconId(), p.getRequestCode()) { // from class: com.kopin.solos.menu.ShareMenu.1
                @Override // com.kopin.solos.menu.ShareMenuAdapter.ShareMenuItem
                public boolean isShared() {
                    return ShareMenu.this.mCallback.isShared(p);
                }
            };
            addMenuItem(item);
        }
        setOnItemClickListener(this.mItemClick);
    }

    public static CustomActionProvider<ShareMenuAdapter.ShareMenuItem> createActionProvider(Context context, ChoiceListener cb) {
        return new ShareMenu(context, cb);
    }
}
