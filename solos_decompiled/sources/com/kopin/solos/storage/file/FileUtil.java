package com.kopin.solos.storage.file;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.kopin.solos.common.CommonFileUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class FileUtil {
    private static final String HASH_FILE = "hash.txt";
    private static final String SUB_FOLDER = "";

    public static Bitmap readBitmapInternal(Context context, String filename) {
        try {
            File root = context.getApplicationContext().getFilesDir();
            File bmp = new File(root, filename);
            if (bmp.canRead()) {
                return BitmapFactory.decodeFile(root + File.separator + filename);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static File getInternalFile(Context context, String filename) {
        return new File(context.getApplicationContext().getFilesDir(), filename);
    }

    public static void saveBitmapInternal(Context context, Bitmap bitmap, String filename) throws Throwable {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(context.getApplicationContext().getFilesDir() + File.separator + filename);
            if (bitmap == null) {
                if (file.exists()) {
                    file.delete();
                }
            } else {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                file.createNewFile();
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(bytes.toByteArray());
                    fileOutputStream = fileOutputStream2;
                } catch (IOException e) {
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                            return;
                        } catch (IOException e2) {
                            return;
                        }
                    }
                    return;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e4) {
                }
            }
        } catch (IOException e5) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static void saveBitmapExternal(Context context, Bitmap bitmap, String filename) throws Throwable {
        saveBitmapExternal(context, "", bitmap, filename);
    }

    private static void saveBitmapExternal(Context context, String folder, Bitmap bitmap, String filename) throws Throwable {
        FileOutputStream fileOutputStream = null;
        try {
            File sdCard = CommonFileUtil.getExternalPath(context);
            File dir = sdCard;
            if (folder != null && !folder.isEmpty()) {
                if (!folder.startsWith("/")) {
                    folder = File.separator + folder;
                }
                dir = new File(sdCard.getAbsolutePath() + folder);
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            if (bitmap == null) {
                if (file.exists()) {
                    file.delete();
                }
            } else {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                file.createNewFile();
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(bytes.toByteArray());
                    fileOutputStream = fileOutputStream2;
                } catch (IOException e) {
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                            return;
                        } catch (IOException e2) {
                            return;
                        }
                    }
                    return;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e4) {
                }
            }
        } catch (IOException e5) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static void saveTextExternal(Context context, String folder, String filename, List<String> lines) {
        if (lines != null) {
            try {
                if (lines.size() > 0) {
                    File sdCard = CommonFileUtil.getExternalPath(context);
                    File dir = sdCard;
                    if (folder != null && !folder.isEmpty()) {
                        if (!folder.startsWith("/")) {
                            folder = File.separator + folder;
                        }
                        dir = new File(sdCard.getAbsolutePath() + folder);
                        dir.mkdirs();
                    }
                    File file = new File(dir, filename);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOutputStream);
                    for (String line : lines) {
                        myOutWriter.append((CharSequence) line);
                        myOutWriter.append((CharSequence) "\n");
                    }
                    myOutWriter.close();
                    fileOutputStream.close();
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e2) {
            }
        }
    }

    public static void saveHashFile(Context context) {
        try {
            List<String> hashList = new ArrayList<>();
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 64);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash = Base64.encodeToString(md.digest(), 0);
                if (hash != null && !hash.isEmpty()) {
                    hashList.add(hash);
                }
            }
            if (hashList.size() > 0) {
                saveTextExternal(context, "", HASH_FILE, hashList);
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e2) {
        }
    }
}
