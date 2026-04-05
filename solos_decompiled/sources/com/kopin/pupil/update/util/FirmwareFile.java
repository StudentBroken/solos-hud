package com.kopin.pupil.update.util;

import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import com.kopin.pupil.PupilMaintenance;
import com.kopin.pupil.update.util.FirmwareManifest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes13.dex */
public class FirmwareFile {
    private File mBinFile;
    protected Context mContext;
    protected FirmwareManifest.FirmwareSignature mManifest;

    private FirmwareFile(Context ctx, FirmwareManifest.FirmwareSignature manifest) {
        this.mContext = ctx;
        this.mManifest = manifest;
    }

    private FirmwareFile(Context ctx, FirmwareManifest.FirmwareSignature manifest, String imgPath) {
        this(ctx, manifest);
        this.mBinFile = FirmwareFlash.updateFile(imgPath);
    }

    public boolean exists() {
        return this.mBinFile != null && this.mBinFile.exists();
    }

    public String getVersion() {
        return String.format("%d.%d.%d", Byte.valueOf(this.mManifest.mVersion[0]), Byte.valueOf(this.mManifest.mVersion[1]), Byte.valueOf(this.mManifest.mVersion[2]));
    }

    public static FirmwareFile load(Context ctx, String manifestPath, String imgPath) {
        File manifest = FirmwareFlash.updateFile(manifestPath);
        if (!manifest.canRead()) {
            return null;
        }
        try {
            FileInputStream in = new FileInputStream(manifest);
            FirmwareManifest.FirmwareSignature fwManifest = FirmwareManifest.fromInputStream(in);
            if (fwManifest != null) {
                return new FirmwareFile(ctx, fwManifest, imgPath);
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FirmwareFile loadFromAssets(Context ctx, String manifestPath, String imgPath) {
        try {
            InputStream in = ctx.getAssets().open(manifestPath);
            FirmwareManifest.FirmwareSignature fwManifest = FirmwareManifest.fromInputStream(in);
            if (fwManifest != null) {
                return new FirmwareAssetFile(ctx, fwManifest, imgPath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public FlashChunker openBinary() {
        try {
            FileInputStream input = new FileInputStream(this.mBinFile);
            return new FlashChunker(input, this.mManifest.mSegments);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class FirmwareAssetFile extends FirmwareFile {
        private String mImageAssetFile;

        public FirmwareAssetFile(Context ctx, FirmwareManifest.FirmwareSignature manifest, String imgPath) {
            super(ctx, manifest);
            this.mImageAssetFile = imgPath;
        }

        @Override // com.kopin.pupil.update.util.FirmwareFile
        public FlashChunker openBinary() {
            try {
                InputStream input = this.mContext.getAssets().open(this.mImageAssetFile);
                return new FlashChunker(input, this.mManifest.mSegments);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override // com.kopin.pupil.update.util.FirmwareFile
        public boolean exists() {
            return true;
        }
    }

    public static class FlashChunker {
        private byte[] chunk;
        private InputStream input;
        private FirmwareManifest.FirmwareSegment[] segments;
        private int segIdx = -1;
        private long nextAddr = 0;
        private int bytesLeft = 0;

        FlashChunker(InputStream in, FirmwareManifest.FirmwareSegment[] segArray) throws FileNotFoundException {
            this.input = in;
            this.segments = segArray;
        }

        public long getBinarySize() {
            long sz = 0;
            for (FirmwareManifest.FirmwareSegment seg : this.segments) {
                sz += (long) seg.mDataLength;
            }
            return sz;
        }

        public boolean hasMoreToSend() {
            return this.bytesLeft != 0 || this.segIdx < this.segments.length + (-1);
        }

        private void nextSegment() {
            this.segIdx++;
            if (this.segIdx < this.segments.length) {
                long curPos = this.nextAddr;
                this.nextAddr = this.segments[this.segIdx].mStartAddress;
                this.bytesLeft = this.segments[this.segIdx].mDataLength;
                if (this.input != null) {
                    try {
                        this.input.skip(this.nextAddr - curPos);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }

        public void flashNextBytes(int count) {
            if (this.bytesLeft == 0) {
                boolean waitForVerify = verifySegment();
                nextSegment();
                if (waitForVerify) {
                    return;
                }
            }
            if (count > this.bytesLeft) {
                count = this.bytesLeft;
            }
            this.chunk = new byte[count];
            try {
                int len = this.input.read(this.chunk);
                if (len != count) {
                    throw new IOException("Byte count read from file, didn't match expected or requested count.");
                }
                reflashLastBytes();
                this.nextAddr += (long) count;
                this.bytesLeft -= count;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void reflashLastBytes() {
            if (this.chunk != null) {
                int chksum = 0;
                for (byte b : this.chunk) {
                    chksum += b & 255;
                }
                PupilMaintenance.flashBytes((int) this.nextAddr, this.chunk, (short) (chksum & SupportMenu.USER_MASK));
            }
        }

        private boolean verifySegment() {
            if (this.segIdx < 0 || this.segIdx >= this.segments.length) {
                return false;
            }
            PupilMaintenance.checkSignature((int) this.segments[this.segIdx].mStartAddress, this.segments[this.segIdx].mDataLength, this.segments[this.segIdx].mSignature);
            return true;
        }

        public boolean closeAndVerify() {
            if (this.bytesLeft != 0) {
                return false;
            }
            try {
                this.input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return verifySegment();
        }
    }
}
