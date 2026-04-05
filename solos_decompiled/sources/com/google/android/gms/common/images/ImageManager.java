package com.google.android.gms.common.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.internal.zzbgy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes3.dex */
public final class ImageManager {
    private static final Object zzaFT = new Object();
    private static HashSet<Uri> zzaFU = new HashSet<>();
    private static ImageManager zzaFV;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService zzaFW = Executors.newFixedThreadPool(4);
    private final zza zzaFX = null;
    private final zzbgy zzaFY = new zzbgy();
    private final Map<com.google.android.gms.common.images.zza, ImageReceiver> zzaFZ = new HashMap();
    private final Map<Uri, ImageReceiver> zzaGa = new HashMap();
    private final Map<Uri, Long> zzaGb = new HashMap();

    @KeepName
    final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<com.google.android.gms.common.images.zza> zzaGc;

        ImageReceiver(Uri uri) {
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
            this.zzaGc = new ArrayList<>();
        }

        @Override // android.os.ResultReceiver
        public final void onReceiveResult(int i, Bundle bundle) {
            ImageManager.this.zzaFW.execute(ImageManager.this.new zzb(this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public final void zzb(com.google.android.gms.common.images.zza zzaVar) {
            com.google.android.gms.common.internal.zzc.zzcz("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzaGc.add(zzaVar);
        }

        public final void zzc(com.google.android.gms.common.images.zza zzaVar) {
            com.google.android.gms.common.internal.zzc.zzcz("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzaGc.remove(zzaVar);
        }

        public final void zzqT() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            ImageManager.this.mContext.sendBroadcast(intent);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    static final class zza extends LruCache<com.google.android.gms.common.images.zzb, Bitmap> {
        @Override // android.support.v4.util.LruCache
        protected final /* synthetic */ void entryRemoved(boolean z, com.google.android.gms.common.images.zzb zzbVar, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, zzbVar, bitmap, bitmap2);
        }

        @Override // android.support.v4.util.LruCache
        protected final /* synthetic */ int sizeOf(com.google.android.gms.common.images.zzb zzbVar, Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            return bitmap2.getHeight() * bitmap2.getRowBytes();
        }
    }

    final class zzb implements Runnable {
        private final Uri mUri;
        private final ParcelFileDescriptor zzaGe;

        public zzb(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.zzaGe = parcelFileDescriptor;
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                String strValueOf = String.valueOf(Thread.currentThread());
                String strValueOf2 = String.valueOf(Looper.getMainLooper().getThread());
                Log.e("Asserts", new StringBuilder(String.valueOf(strValueOf).length() + 56 + String.valueOf(strValueOf2).length()).append("checkNotMainThread: current thread ").append(strValueOf).append(" IS the main thread ").append(strValueOf2).append("!").toString());
                throw new IllegalStateException("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            }
            boolean z = false;
            Bitmap bitmapDecodeFileDescriptor = null;
            if (this.zzaGe != null) {
                try {
                    bitmapDecodeFileDescriptor = BitmapFactory.decodeFileDescriptor(this.zzaGe.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    String strValueOf3 = String.valueOf(this.mUri);
                    Log.e("ImageManager", new StringBuilder(String.valueOf(strValueOf3).length() + 34).append("OOM while loading bitmap for uri: ").append(strValueOf3).toString(), e);
                    z = true;
                }
                try {
                    this.zzaGe.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ImageManager.this.mHandler.post(ImageManager.this.new zzd(this.mUri, bitmapDecodeFileDescriptor, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                String strValueOf4 = String.valueOf(this.mUri);
                Log.w("ImageManager", new StringBuilder(String.valueOf(strValueOf4).length() + 32).append("Latch interrupted while posting ").append(strValueOf4).toString());
            }
        }
    }

    final class zzc implements Runnable {
        private final com.google.android.gms.common.images.zza zzaGf;

        public zzc(com.google.android.gms.common.images.zza zzaVar) {
            this.zzaGf = zzaVar;
        }

        @Override // java.lang.Runnable
        public final void run() {
            com.google.android.gms.common.internal.zzc.zzcz("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzaFZ.get(this.zzaGf);
            if (imageReceiver != null) {
                ImageManager.this.zzaFZ.remove(this.zzaGf);
                imageReceiver.zzc(this.zzaGf);
            }
            com.google.android.gms.common.images.zzb zzbVar = this.zzaGf.zzaGh;
            if (zzbVar.uri == null) {
                this.zzaGf.zza(ImageManager.this.mContext, ImageManager.this.zzaFY, true);
                return;
            }
            Bitmap bitmapZza = ImageManager.this.zza(zzbVar);
            if (bitmapZza != null) {
                this.zzaGf.zza(ImageManager.this.mContext, bitmapZza, true);
                return;
            }
            Long l = (Long) ImageManager.this.zzaGb.get(zzbVar.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < 3600000) {
                    this.zzaGf.zza(ImageManager.this.mContext, ImageManager.this.zzaFY, true);
                    return;
                }
                ImageManager.this.zzaGb.remove(zzbVar.uri);
            }
            this.zzaGf.zza(ImageManager.this.mContext, ImageManager.this.zzaFY);
            ImageReceiver imageReceiver2 = (ImageReceiver) ImageManager.this.zzaGa.get(zzbVar.uri);
            if (imageReceiver2 == null) {
                imageReceiver2 = ImageManager.this.new ImageReceiver(zzbVar.uri);
                ImageManager.this.zzaGa.put(zzbVar.uri, imageReceiver2);
            }
            imageReceiver2.zzb(this.zzaGf);
            if (!(this.zzaGf instanceof com.google.android.gms.common.images.zzd)) {
                ImageManager.this.zzaFZ.put(this.zzaGf, imageReceiver2);
            }
            synchronized (ImageManager.zzaFT) {
                if (!ImageManager.zzaFU.contains(zzbVar.uri)) {
                    ImageManager.zzaFU.add(zzbVar.uri);
                    imageReceiver2.zzqT();
                }
            }
        }
    }

    final class zzd implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private boolean zzaGg;
        private final CountDownLatch zztM;

        public zzd(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzaGg = z;
            this.zztM = countDownLatch;
        }

        @Override // java.lang.Runnable
        public final void run() {
            com.google.android.gms.common.internal.zzc.zzcz("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (ImageManager.this.zzaFX != null) {
                if (this.zzaGg) {
                    ImageManager.this.zzaFX.evictAll();
                    System.gc();
                    this.zzaGg = false;
                    ImageManager.this.mHandler.post(this);
                    return;
                }
                if (z) {
                    ImageManager.this.zzaFX.put(new com.google.android.gms.common.images.zzb(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzaGa.remove(this.mUri);
            if (imageReceiver != null) {
                ArrayList arrayList = imageReceiver.zzaGc;
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    com.google.android.gms.common.images.zza zzaVar = (com.google.android.gms.common.images.zza) arrayList.get(i);
                    if (z) {
                        zzaVar.zza(ImageManager.this.mContext, this.mBitmap, false);
                    } else {
                        ImageManager.this.zzaGb.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                        zzaVar.zza(ImageManager.this.mContext, ImageManager.this.zzaFY, false);
                    }
                    if (!(zzaVar instanceof com.google.android.gms.common.images.zzd)) {
                        ImageManager.this.zzaFZ.remove(zzaVar);
                    }
                }
            }
            this.zztM.countDown();
            synchronized (ImageManager.zzaFT) {
                ImageManager.zzaFU.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
    }

    public static ImageManager create(Context context) {
        if (zzaFV == null) {
            zzaFV = new ImageManager(context, false);
        }
        return zzaFV;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Bitmap zza(com.google.android.gms.common.images.zzb zzbVar) {
        if (this.zzaFX == null) {
            return null;
        }
        return this.zzaFX.get(zzbVar);
    }

    private final void zza(com.google.android.gms.common.images.zza zzaVar) {
        com.google.android.gms.common.internal.zzc.zzcz("ImageManager.loadImage() must be called in the main thread");
        new zzc(zzaVar).run();
    }

    public final void loadImage(ImageView imageView, int i) {
        zza(new com.google.android.gms.common.images.zzc(imageView, i));
    }

    public final void loadImage(ImageView imageView, Uri uri) {
        zza(new com.google.android.gms.common.images.zzc(imageView, uri));
    }

    public final void loadImage(ImageView imageView, Uri uri, int i) {
        com.google.android.gms.common.images.zzc zzcVar = new com.google.android.gms.common.images.zzc(imageView, uri);
        zzcVar.zzaGj = i;
        zza(zzcVar);
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        zza(new com.google.android.gms.common.images.zzd(onImageLoadedListener, uri));
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        com.google.android.gms.common.images.zzd zzdVar = new com.google.android.gms.common.images.zzd(onImageLoadedListener, uri);
        zzdVar.zzaGj = i;
        zza(zzdVar);
    }
}
