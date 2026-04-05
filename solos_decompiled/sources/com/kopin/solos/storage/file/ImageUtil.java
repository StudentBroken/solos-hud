package com.kopin.solos.storage.file;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

/* JADX INFO: loaded from: classes54.dex */
public class ImageUtil {
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        if (bitmap.getWidth() != bitmap.getHeight()) {
            int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int crop = (Math.max(bitmap.getWidth(), bitmap.getHeight()) - dimension) / 2;
            int i = bitmap.getWidth() > dimension ? crop : 0;
            if (bitmap.getHeight() <= dimension) {
                crop = 0;
            }
            return Bitmap.createBitmap(bitmap, i, crop, dimension, dimension);
        }
        return bitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap, int rotation) {
        if (rotation != 0) {
            bitmap = RotateBitmap(bitmap, rotation);
        }
        if (bitmap.getWidth() != bitmap.getHeight()) {
            int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int crop = (Math.max(bitmap.getWidth(), bitmap.getHeight()) - dimension) / 2;
            int i = bitmap.getWidth() > dimension ? crop : 0;
            if (bitmap.getHeight() <= dimension) {
                crop = 0;
            }
            return Bitmap.createBitmap(bitmap, i, crop, dimension, dimension);
        }
        return bitmap;
    }

    public static Bitmap getCroppedImageFromGallery(Context context, Uri selectedImage) {
        try {
            String[] filePathColumn = {"_data", "orientation"};
            Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            int orientationIndex = cursor.getColumnIndex(filePathColumn[1]);
            int rotated = cursor.getInt(orientationIndex);
            cursor.close();
            if (imgDecodableString != null && !imgDecodableString.isEmpty()) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgDecodableString);
                if (imageBitmap != null) {
                    return cropToSquare(BitmapFactory.decodeFile(imgDecodableString), rotated);
                }
                return imageBitmap;
            }
        } catch (Exception e) {
            Log.e("ProfileEditFr", "Problem retrieving profile photo from gallery: " + e.getMessage());
        }
        return null;
    }
}
