package com.kopin.pupil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

/* JADX INFO: loaded from: classes25.dex */
public class VCContext {
    private static final Rect SCREEN_BOUNDS = new Rect(0, 0, 428, 240);
    public static final int SCREEN_HEIGHT = 240;
    public static final int SCREEN_WIDTH = 428;
    private ConnectionManager mConnectionManager;
    private Context mContext;

    public VCContext(ConnectionManager connectionManager, Context context) {
        this.mConnectionManager = connectionManager;
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Bitmap getImageFromCache(String id) {
        return this.mConnectionManager.getImageFromCache(id);
    }

    public void refresh(boolean full) {
        this.mConnectionManager.refresh(full);
    }

    public Rect getBounds() {
        return SCREEN_BOUNDS;
    }

    public Point getScreenSize() {
        return new Point(428, 240);
    }

    public Bitmap getImage(int id) {
        Resources resources = this.mContext.getResources();
        if (resources == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = 1;
        options.inScaled = false;
        return BitmapFactory.decodeResource(resources, id, options);
    }

    public String getString(int id) {
        return this.mContext.getString(id);
    }

    public String[] getStringArray(int id) {
        return this.mContext.getResources().getStringArray(id);
    }
}
