package com.kopin.solos.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;

/* JADX INFO: loaded from: classes24.dex */
public class ShareMenuAdapter extends CustomMenuAdapter<ShareMenuItem> {
    private LayoutInflater mInflater;

    public ShareMenuAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void addMenuItem(ShareMenuItem menuItem) {
        menuItem.mMenuAdapter = this;
        super.addMenuItem(menuItem);
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter
    public void doCleanUp() {
        super.doCleanUp();
        for (int i = 0; i < getCount(); i++) {
            getMenuItem(i).mMenuAdapter = null;
        }
    }

    @Override // com.kopin.solos.menu.CustomMenuAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ShareMenuItem item = getMenuItem(position);
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.share_menu_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIcon = (ImageView) convertView.findViewById(R.id.menu_item_image);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.menu_item_text);
            viewHolder.mStatusIcon = (ImageView) convertView.findViewById(R.id.menu_item_image_progress);
            viewHolder.mStatusBar = (ProgressBar) convertView.findViewById(R.id.menu_item_image_progress_bar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mIcon.setImageResource(item.getDrawableId());
        viewHolder.mTitle.setText(item.getTitle());
        item.checkShareStatus();
        if (item.mShareProgress != null) {
            viewHolder.mStatusBar.setVisibility(4);
            viewHolder.mStatusIcon.setVisibility(0);
            switch (item.mShareProgress.status) {
                case ALREADY_SHARED:
                case DONE:
                    viewHolder.mStatusIcon.setImageResource(R.drawable.ic_tick_icon);
                    break;
                case NETWORK_ERROR:
                case AUTH_FAIL:
                case UNKNOWN:
                case CANCELED:
                    viewHolder.mStatusIcon.setImageResource(R.drawable.ic_error_icon);
                    break;
                default:
                    viewHolder.mStatusIcon.setVisibility(4);
                    viewHolder.mStatusBar.setVisibility(0);
                    break;
            }
        } else {
            viewHolder.mStatusIcon.setVisibility(4);
            viewHolder.mStatusBar.setVisibility(4);
        }
        viewHolder.setEnabled(item.isEnabled());
        return convertView;
    }

    private static class ViewHolder {
        ImageView mIcon;
        ProgressBar mStatusBar;
        ImageView mStatusIcon;
        TextView mTitle;

        private ViewHolder() {
        }

        public void setEnabled(boolean enabled) {
            this.mIcon.setEnabled(enabled);
            this.mTitle.setEnabled(enabled);
            this.mStatusIcon.setEnabled(enabled);
            this.mStatusBar.setEnabled(enabled);
        }
    }

    public static abstract class ShareMenuItem extends CustomMenuItem implements ShareHelper.ShareProgressListener {
        private ShareHelper.UploadListener mListener;
        private ShareMenuAdapter mMenuAdapter;
        private ShareHelper.ShareProgress mShareProgress;

        public abstract boolean isShared();

        public ShareMenuItem(String title, int drawableId, int id) {
            super(title, drawableId, id);
            this.mListener = new ShareHelper.UploadListener() { // from class: com.kopin.solos.menu.ShareMenuAdapter.ShareMenuItem.1
                @Override // com.kopin.solos.share.ShareHelper.UploadListener, com.kopin.solos.share.ShareHelper.ShareProgressListener
                public void onProgress(Platforms which, ShareHelper.ShareProgress progress) {
                    ShareMenuItem.this.onProgress(which, progress);
                }
            };
        }

        public ShareHelper.UploadListener getListener() {
            return this.mListener;
        }

        @Override // com.kopin.solos.share.ShareHelper.ShareProgressListener
        public void onProgress(Platforms which, ShareHelper.ShareProgress progress) {
            this.mShareProgress = progress;
            onDataChanged();
        }

        @Override // com.kopin.solos.menu.CustomMenuItem
        public void onDataChanged() {
            if (this.mMenuAdapter != null) {
                this.mMenuAdapter.notifyDataSetChanged();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkShareStatus() {
            if (this.mShareProgress == null && isShared()) {
                this.mShareProgress = new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, "");
            }
        }
    }
}
