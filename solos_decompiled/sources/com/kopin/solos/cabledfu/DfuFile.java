package com.kopin.solos.cabledfu;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/* JADX INFO: loaded from: classes38.dex */
public class DfuFile {
    private File mBinFile;
    protected final Context mContext;
    protected String mFlashDataFile;
    protected byte[] mInitData;
    protected DfuManifest mManifest;

    public static class DfuManifest {
        public DfuManifestAppDefinition application;
        public String dfu_version;
    }

    public static class DfuManifestAppDefinition {
        public String bin_file;
        public String dat_file;
        public String[] dat_file_content;
        public DfuManifestAppParameters init_packet_data;
    }

    public static class DfuManifestAppParameters {
        public long application_version;
        public int device_revision;
        public int device_type;
        public int firmware_crc16;
        public int[] softdevice_req;
    }

    public static class DfuManifestFile {
        public DfuManifest manifest;
    }

    private DfuFile(Context context, String path) {
        this.mContext = context;
    }

    private DfuFile(Context context) {
        this.mContext = context;
    }

    public static DfuFile fromAssets(Context context, String manifestPath, String imagePath) {
        DfuFile self = new DFuAssetFile(context, imagePath);
        try {
            Gson gson = new Gson();
            InputStream in = context.getAssets().open(manifestPath);
            DfuManifestFile manifest = (DfuManifestFile) gson.fromJson((Reader) new InputStreamReader(in), DfuManifestFile.class);
            if (manifest != null) {
                self.mManifest = manifest.manifest;
                self.mFlashDataFile = imagePath.replace(".bin", ".dat");
                if (manifest.manifest.application.dat_file_content != null && manifest.manifest.application.dat_file_content.length > 0) {
                    self.mInitData = new byte[manifest.manifest.application.dat_file_content.length];
                    for (int i = 0; i < manifest.manifest.application.dat_file_content.length; i++) {
                        self.mInitData[i] = (byte) Integer.parseInt(manifest.manifest.application.dat_file_content[i], 16);
                    }
                    return self;
                }
                return self;
            }
            return self;
        } catch (IOException e) {
            return null;
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static DfuFile load(Context context, String manifestPath, String imagePath) {
        File manifestFile = CableFlash.updateFile(manifestPath);
        if (!manifestFile.canRead()) {
            return null;
        }
        try {
            Gson gson = new Gson();
            DfuManifestFile manifest = (DfuManifestFile) gson.fromJson((Reader) new InputStreamReader(new FileInputStream(manifestFile)), DfuManifestFile.class);
            DfuFile self = new DfuFile(context);
            if (manifest != null) {
                self.mManifest = manifest.manifest;
                if (manifest.manifest.application.dat_file_content.length > 0) {
                    self.mInitData = new byte[manifest.manifest.application.dat_file_content.length];
                    for (int i = 0; i < manifest.manifest.application.dat_file_content.length; i++) {
                        self.mInitData[i] = (byte) Integer.parseInt(manifest.manifest.application.dat_file_content[i], 16);
                    }
                }
                self.mBinFile = CableFlash.updateFile(imagePath);
                return self;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public String getVersion() {
        return this.mManifest.dfu_version;
    }

    public boolean exists() {
        return this.mBinFile != null && this.mBinFile.exists();
    }

    public int getCRC16() {
        if (this.mManifest != null) {
            return this.mManifest.application.init_packet_data.firmware_crc16;
        }
        return 0;
    }

    public byte[] getFlashArgs() {
        return this.mInitData;
    }

    public DfuChunker openBinary() {
        if (this.mManifest == null || this.mBinFile == null) {
            return null;
        }
        try {
            return new DfuChunker(new FileInputStream(this.mBinFile));
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] readBytes() {
        if (this.mManifest == null || this.mBinFile == null) {
            return null;
        }
        try {
            return DfuChunker.readAllBytes(new FileInputStream(this.mBinFile));
        } catch (IOException e) {
            return null;
        }
    }

    public static class DFuAssetFile extends DfuFile {
        private String mAssetFile;

        private DFuAssetFile(Context context, String path) {
            super(context, path);
            this.mAssetFile = path;
        }

        private DFuAssetFile(Context context) {
            super(context);
        }

        @Override // com.kopin.solos.cabledfu.DfuFile
        public byte[] getFlashArgs() {
            if (this.mInitData != null) {
                return this.mInitData;
            }
            if (this.mFlashDataFile != null) {
                try {
                    InputStream in = this.mContext.getAssets().open(this.mFlashDataFile);
                    byte[] buf = new byte[in.available()];
                    in.read(buf);
                    in.close();
                    return buf;
                } catch (IOException e) {
                }
            }
            return null;
        }

        @Override // com.kopin.solos.cabledfu.DfuFile
        public DfuChunker openBinary() {
            if (this.mManifest == null || this.mAssetFile == null) {
                return null;
            }
            try {
                return new DfuChunker(this.mContext.getAssets().open(this.mAssetFile));
            } catch (IOException e) {
                return null;
            }
        }

        @Override // com.kopin.solos.cabledfu.DfuFile
        public byte[] readBytes() {
            if (this.mManifest == null || this.mAssetFile == null) {
                return null;
            }
            try {
                return DfuChunker.readAllBytes(this.mContext.getAssets().open(this.mAssetFile));
            } catch (IOException e) {
                return null;
            }
        }

        @Override // com.kopin.solos.cabledfu.DfuFile
        public boolean exists() {
            return true;
        }
    }
}
