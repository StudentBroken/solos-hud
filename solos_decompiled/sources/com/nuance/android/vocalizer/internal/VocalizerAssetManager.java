package com.nuance.android.vocalizer.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;
import com.nuance.android.vocalizer.util.APKExpansionSupport;
import com.nuance.android.vocalizer.util.ZipResourceFile;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;

/* JADX INFO: loaded from: classes16.dex */
public class VocalizerAssetManager {
    private static final String TAG = "NUANCE";
    private static final String VOCALIZER_DATA_INTENT = "com.nuance.vocalizer.VOCALIZER_DATA";
    private int externalPackageMainVersion;
    private int externalPackagePatchVersion;
    private Context mContext;
    private PackageManager mPkgManager;
    private String mPipelineHeaders = "";
    private VocalizerInstallationListener mInstallationListener = null;
    private BroadcastReceiver mInstallationReceiver = null;
    private boolean bInitialized = false;
    private Hashtable<String, ActivityInfo> mInstalledVoicePackages = null;
    private Hashtable<Integer, AssetFileDescriptor> mOpenAssetFileList = null;
    private final String VOCALIZER_DATA_EXTENSION = "xmf";
    private final String VOCALIZER_PIPELINE_EXTENSION = "jet";
    private final String VOCALIZER_FOLDER_NAME = "vocalizer";
    private Hashtable<String, AssetFileInfo> mAssetFiles = null;
    private String externalPackageName = null;

    private class AssetFileInfo {
        AssetManager mAssetMgr;
        Context mContext;
        String mFileName;
        String mPackageName;

        private AssetFileInfo() {
        }

        AssetFileInfo copy() {
            AssetFileInfo assetFileInfo = VocalizerAssetManager.this.new AssetFileInfo();
            assetFileInfo.mPackageName = this.mPackageName;
            assetFileInfo.mContext = this.mContext;
            assetFileInfo.mFileName = this.mFileName;
            assetFileInfo.mAssetMgr = this.mAssetMgr;
            return assetFileInfo;
        }
    }

    public VocalizerAssetManager(Context context) {
        this.mContext = null;
        this.mPkgManager = null;
        this.mContext = context;
        this.mPkgManager = this.mContext.getPackageManager();
    }

    public void initialize() {
        this.mPipelineHeaders = "";
        this.mAssetFiles = new Hashtable<>();
        boolean z = false;
        for (ResolveInfo resolveInfo : this.mPkgManager.queryIntentActivities(new Intent(VOCALIZER_DATA_INTENT), 0)) {
            if (resolveInfo.activityInfo.packageName.equalsIgnoreCase(this.mContext.getPackageName())) {
                z = true;
            }
            scanPackage(resolveInfo.activityInfo.packageName);
            scanExternalPackage(resolveInfo.activityInfo.packageName);
        }
        if (!z) {
            scanPackage(this.mContext.getPackageName());
        }
        this.bInitialized = true;
        if (this.mInstallationListener != null) {
            registerInstallReceiver();
        }
    }

    private void scanPackage(String str) {
        try {
            Context contextCreatePackageContext = this.mContext.createPackageContext(str, 0);
            AssetFileInfo assetFileInfo = new AssetFileInfo();
            assetFileInfo.mPackageName = str;
            assetFileInfo.mContext = contextCreatePackageContext;
            assetFileInfo.mAssetMgr = contextCreatePackageContext.getAssets();
            scanFolder("vocalizer", assetFileInfo);
        } catch (Exception e) {
            Log.e(TAG, "scanPackage EXCEPTION: " + e);
        }
    }

    private void scanExternalPackage(String str) {
        try {
            Context contextCreatePackageContext = this.mContext.createPackageContext(str, 0);
            int i = contextCreatePackageContext.getPackageManager().getPackageInfo(contextCreatePackageContext.getPackageName(), 0).versionCode;
            this.externalPackagePatchVersion = i;
            this.externalPackageMainVersion = i;
            this.externalPackageName = str;
            AssetFileInfo assetFileInfo = new AssetFileInfo();
            assetFileInfo.mPackageName = str;
            assetFileInfo.mContext = null;
            assetFileInfo.mAssetMgr = null;
            scanFolder("vocalizer", assetFileInfo);
        } catch (Exception e) {
            Log.e(TAG, "scanExternalPackage EXCEPTION: " + e);
        }
    }

    private void scanFolder(String str, AssetFileInfo assetFileInfo) {
        ZipResourceFile aPKExpansionZipFile;
        String[] list;
        BufferedInputStream bufferedInputStream;
        try {
            if (assetFileInfo.mAssetMgr != null) {
                list = assetFileInfo.mAssetMgr.list(str);
                aPKExpansionZipFile = null;
            } else {
                aPKExpansionZipFile = APKExpansionSupport.getAPKExpansionZipFile(this.externalPackageName, this.externalPackageMainVersion, this.externalPackagePatchVersion);
                if (aPKExpansionZipFile != null) {
                    ZipResourceFile.ZipEntryRO[] allEntries = aPKExpansionZipFile.getAllEntries();
                    String[] strArr = new String[allEntries.length];
                    int length = allEntries.length;
                    int i = 0;
                    int i2 = 0;
                    while (i < length) {
                        ZipResourceFile.ZipEntryRO zipEntryRO = allEntries[i];
                        strArr[i2] = zipEntryRO.mFileName.substring(zipEntryRO.mFileName.lastIndexOf("/") + 1);
                        i++;
                        i2++;
                    }
                    list = strArr;
                } else {
                    Log.i(TAG, "There is no expansion obb file found at location " + this.externalPackageName);
                    list = null;
                }
            }
            if (list != null && list.length > 0) {
                for (String str2 : list) {
                    if (str2.contains(".")) {
                        AssetFileInfo assetFileInfoCopy = assetFileInfo.copy();
                        assetFileInfoCopy.mFileName = str + "/" + str2;
                        AssetFileInfo assetFileInfoPut = this.mAssetFiles.put(assetFileInfoCopy.mFileName, assetFileInfoCopy);
                        if (assetFileInfoPut != null) {
                            Log.w(TAG, "File " + assetFileInfoPut.mFileName + " already found in package " + assetFileInfoPut.mPackageName);
                        }
                        if (assetFileInfoCopy.mFileName.toLowerCase().endsWith(".jet")) {
                            if (assetFileInfo.mAssetMgr != null) {
                                bufferedInputStream = new BufferedInputStream(assetFileInfo.mAssetMgr.open(assetFileInfoCopy.mFileName));
                            } else {
                                bufferedInputStream = new BufferedInputStream(aPKExpansionZipFile.getInputStream(assetFileInfoCopy.mFileName));
                            }
                            while (bufferedInputStream.available() > 0) {
                                byte[] bArr = new byte[bufferedInputStream.available()];
                                bufferedInputStream.read(bArr);
                                this.mPipelineHeaders += new String(bArr);
                            }
                            bufferedInputStream.close();
                        }
                    } else {
                        scanFolder(str + "/" + str2, assetFileInfo);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "scanFolder EXCEPTION: " + e);
        } catch (Exception e2) {
            Log.e(TAG, "scanFolder EXCEPTION: " + e2);
        }
    }

    public VocalizerFileInfo openAssetFile(String str) {
        VocalizerFileInfo vocalizerFileInfo;
        AssetFileInfo assetFileInfo;
        AssetFileDescriptor assetFileDescriptor;
        FileDescriptor fileDescriptor;
        try {
            if (str.indexOf("vocalizer/") == -1) {
                int length = str.length();
                String str2 = "vocalizer/";
                for (int i = 0; i < length; i++) {
                    char cCharAt = str.charAt(i);
                    if (cCharAt == '.') {
                        break;
                    }
                    if (cCharAt == '/') {
                        str2 = str2 + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
                    } else if (!Character.isLetterOrDigit(cCharAt)) {
                        str2 = str2 + "-";
                    } else {
                        str2 = str2 + cCharAt;
                    }
                }
                str = str2 + ".xmf";
            }
            Log.i(TAG, "openAssetFile: " + str);
            assetFileInfo = this.mAssetFiles.get(str);
        } catch (Exception e) {
            Log.v(TAG, "openAssetFile EXCEPTION: " + e);
            vocalizerFileInfo = null;
        }
        if (assetFileInfo == null) {
            return null;
        }
        if (assetFileInfo.mAssetMgr != null) {
            assetFileDescriptor = assetFileInfo.mAssetMgr.openFd(str);
            fileDescriptor = assetFileDescriptor.getFileDescriptor();
        } else {
            assetFileDescriptor = APKExpansionSupport.getAPKExpansionZipFile(this.externalPackageName, this.externalPackageMainVersion, this.externalPackagePatchVersion).getAssetFileDescriptor(str);
            fileDescriptor = assetFileDescriptor.getFileDescriptor();
        }
        Field declaredField = FileDescriptor.class.getDeclaredField("descriptor");
        declaredField.setAccessible(true);
        int i2 = declaredField.getInt(fileDescriptor);
        if (i2 > 0) {
            vocalizerFileInfo = new VocalizerFileInfo();
            vocalizerFileInfo.descriptor = i2;
            vocalizerFileInfo.startOffset = assetFileDescriptor.getStartOffset();
            vocalizerFileInfo.length = assetFileDescriptor.getLength();
            if (this.mOpenAssetFileList == null) {
                this.mOpenAssetFileList = new Hashtable<>();
                if (this.mOpenAssetFileList == null) {
                    throw new Exception("Not Enough Memory");
                }
            }
            this.mOpenAssetFileList.put(Integer.valueOf(i2), assetFileDescriptor);
        } else {
            assetFileDescriptor.close();
            vocalizerFileInfo = null;
        }
        return vocalizerFileInfo;
    }

    public void closeAssetFile(int i) {
        AssetFileDescriptor assetFileDescriptor = this.mOpenAssetFileList.get(Integer.valueOf(i));
        if (assetFileDescriptor != null) {
            try {
                assetFileDescriptor.close();
            } catch (Exception e) {
                Log.e(TAG, "closeAssetFile EXCEPTION: " + e);
            }
            this.mOpenAssetFileList.remove(Integer.valueOf(i));
        }
    }

    public byte[] readFileContents(String str) {
        if (str == null) {
            return null;
        }
        String lowerCase = str.toLowerCase();
        if (!lowerCase.startsWith("vocalizer/")) {
            lowerCase = "vocalizer/" + lowerCase;
        }
        String str2 = lowerCase.indexOf(".xmf") == -1 ? lowerCase + ".xmf" : lowerCase;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(this.mContext.getAssets().open(str2));
            while (bufferedInputStream.available() > 0) {
                byte[] bArr = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(bArr);
                byteArrayOutputStream.write(bArr);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "readFileContents EXCEPTION reading file: " + str2 + " " + e);
            return null;
        }
    }

    public void release() {
        if (this.mOpenAssetFileList != null && this.mOpenAssetFileList.size() > 0) {
            Log.e(TAG, "NUMBER OF UNCLOSED ASSET FILES: " + this.mOpenAssetFileList.size());
        }
        unregisterInstallReceiver();
        this.bInitialized = false;
    }

    public String getPipelineHeaders() {
        return this.mPipelineHeaders;
    }

    private void registerInstallReceiver() {
        if (this.mInstallationReceiver == null) {
            this.mInstallationReceiver = new BroadcastReceiver() { // from class: com.nuance.android.vocalizer.internal.VocalizerAssetManager.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action != null) {
                        try {
                            if (VocalizerAssetManager.this.mInstallationListener != null) {
                                String dataString = intent.getDataString();
                                if (dataString != null && dataString.startsWith("package:")) {
                                    dataString = dataString.substring("package:".length()).trim();
                                }
                                if (action.equals("android.intent.action.PACKAGE_ADDED") && !intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                                    VocalizerAssetManager.this.updateInstalledVoicePackagesList();
                                    Log.i(VocalizerAssetManager.TAG, "Package added: " + dataString + " " + VocalizerAssetManager.this.isVocalizerVoicePackage(dataString));
                                    if (VocalizerAssetManager.this.isVocalizerVoicePackage(dataString)) {
                                        VocalizerAssetManager.this.mInstallationListener.onInstallationEvent(1, dataString);
                                        return;
                                    }
                                    return;
                                }
                                if (action.equals("android.intent.action.PACKAGE_REPLACED")) {
                                    Log.i(VocalizerAssetManager.TAG, "Package replaced: " + dataString + " " + VocalizerAssetManager.this.isVocalizerVoicePackage(dataString));
                                    if (VocalizerAssetManager.this.isVocalizerVoicePackage(dataString)) {
                                        VocalizerAssetManager.this.mInstallationListener.onInstallationEvent(3, dataString);
                                        return;
                                    }
                                    return;
                                }
                                if (action.equals("android.intent.action.PACKAGE_REMOVED") && !intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                                    Log.i(VocalizerAssetManager.TAG, "Package removed: " + dataString + " " + VocalizerAssetManager.this.isVocalizerVoicePackage(dataString));
                                    if (VocalizerAssetManager.this.isVocalizerVoicePackage(dataString)) {
                                        VocalizerAssetManager.this.mInstallationListener.onInstallationEvent(2, dataString);
                                    }
                                    VocalizerAssetManager.this.updateInstalledVoicePackagesList();
                                }
                            }
                        } catch (Exception e) {
                            Log.e(VocalizerAssetManager.TAG, "Installation Receiver EXCEPTION " + e);
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            this.mContext.registerReceiver(this.mInstallationReceiver, intentFilter);
            updateInstalledVoicePackagesList();
        }
    }

    private void unregisterInstallReceiver() {
        if (this.mInstallationReceiver != null) {
            this.mContext.unregisterReceiver(this.mInstallationReceiver);
        }
        this.mInstalledVoicePackages = null;
        this.mInstallationReceiver = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInstalledVoicePackagesList() {
        this.mInstalledVoicePackages = new Hashtable<>();
        for (ResolveInfo resolveInfo : this.mPkgManager.queryIntentActivities(new Intent(VOCALIZER_DATA_INTENT), 0)) {
            if (!resolveInfo.activityInfo.packageName.equalsIgnoreCase(this.mContext.getPackageName())) {
                this.mInstalledVoicePackages.put(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVocalizerVoicePackage(String str) {
        return (str == null || this.mInstalledVoicePackages == null || this.mInstalledVoicePackages.get(str) == null) ? false : true;
    }

    public void setInstallationListener(VocalizerInstallationListener vocalizerInstallationListener) {
        this.mInstallationListener = vocalizerInstallationListener;
        if (this.bInitialized) {
            if (this.mInstallationListener != null) {
                registerInstallReceiver();
            } else {
                unregisterInstallReceiver();
            }
        }
    }
}
