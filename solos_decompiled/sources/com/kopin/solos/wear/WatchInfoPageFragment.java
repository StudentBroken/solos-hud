package com.kopin.solos.wear;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.BaseSupportFragment;

/* JADX INFO: loaded from: classes24.dex */
public class WatchInfoPageFragment extends BaseSupportFragment {

    public enum WatchInfoPages {
        Page1(com.kopin.solos.R.string.watch_info_header1, com.kopin.solos.R.attr.strWatchInfoSubHeader1, com.kopin.solos.R.attr.strWatchInfoMessage1, com.kopin.solos.R.drawable.ic_watches),
        Page2(com.kopin.solos.R.string.watch_info_header_install, 0, com.kopin.solos.R.string.watch_info_message_install, com.kopin.solos.R.drawable.ic_install),
        Page3(com.kopin.solos.R.string.watch_info_header2, 0, com.kopin.solos.R.string.watch_info_message2, com.kopin.solos.R.drawable.ic_sensor_watch),
        Page4(com.kopin.solos.R.string.watch_info_header3, 0, com.kopin.solos.R.string.watch_info_message3, com.kopin.solos.R.drawable.ic_headset_watch),
        Page5(com.kopin.solos.R.string.watch_info_header4, 0, com.kopin.solos.R.attr.strWatchInfoMessage4, com.kopin.solos.R.drawable.ic_cloud_watch);

        int imageResId;
        int textHeaderResId;
        int textMessageResId;
        int textSubHeaderResId;

        WatchInfoPages(int headerResId, int subHeaderResId, int messageResId, int imgResId) {
            this.textHeaderResId = headerResId;
            this.textSubHeaderResId = subHeaderResId;
            this.textMessageResId = messageResId;
            this.imageResId = imgResId;
        }
    }

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int ordinal = getArguments().getInt("ordinal", 0);
        WatchInfoPages watchInfoPages = WatchInfoPages.values()[ordinal];
        View view = inflater.inflate(com.kopin.solos.R.layout.fragment_watch_info, container, false);
        ((TextView) view.findViewById(com.kopin.solos.R.id.textHeader)).setText(watchInfoPages.textHeaderResId);
        if (watchInfoPages.textSubHeaderResId > 0) {
            int resolvedRes = ThemeUtil.getResourceId(getActivity(), watchInfoPages.textSubHeaderResId);
            if (resolvedRes <= 0) {
                resolvedRes = watchInfoPages.textSubHeaderResId;
            }
            ((TextView) view.findViewById(com.kopin.solos.R.id.textSubHeader)).setText(resolvedRes);
        }
        int resolvedRes2 = ThemeUtil.getResourceId(getActivity(), watchInfoPages.textMessageResId);
        if (resolvedRes2 <= 0) {
            resolvedRes2 = watchInfoPages.textMessageResId;
        }
        ((TextView) view.findViewById(com.kopin.solos.R.id.textMessage)).setText(resolvedRes2);
        ImageView imageView = (ImageView) view.findViewById(com.kopin.solos.R.id.imageView);
        if (watchInfoPages.imageResId == com.kopin.solos.R.drawable.ic_watches) {
            imageView.setColorFilter(getResources().getColor(com.kopin.solos.R.color.black), PorterDuff.Mode.SRC_ATOP);
        }
        imageView.setImageResource(watchInfoPages.imageResId);
        return view;
    }
}
