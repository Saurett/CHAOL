package com.indev.chaol.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.indev.chaol.BaseAlbumDirFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jvier on 13/07/2017.
 */

public class FileServices {

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File albumF = getAlbumDir();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                albumF      /* directory */
        );
    }

    private static File getAlbumDir() {

        BaseAlbumDirFactory mAlbumStorageDirFactory = new BaseAlbumDirFactory();

        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (null != storageDir) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null;
                    }
                }
            }

        }
        return storageDir;
    }

    /* Photo album for this application */
    private static String getAlbumName() {
        return "CHAOL";
    }

    public static Boolean setPic(BootstrapCircleThumbnail picture, String currentPath) {
        picture.setBackground(null);

        // Get the dimensions of the View
        int targetW = picture.getWidth();
        int targetH = picture.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        File f = new File(currentPath);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(currentPath, bmOptions);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                Bitmap bMapScaled90 = Bitmap.createScaledBitmap(FileServices.rotateBmp(bitmap, 90), 140, 140, true);
                picture.setImageBitmap(bMapScaled90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                Bitmap bMapScaled180 = Bitmap.createScaledBitmap(FileServices.rotateBmp(bitmap, 180), 140, 140, true);
                picture.setImageBitmap(bMapScaled180);
                break;
            default:
                picture.setImageBitmap(bitmap);
                break;
        }

        return (null != bitmap);
    }

    public static Bitmap rotateBmp(Bitmap bmp, float angle) {
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(angle);
        //supply the original width and height, if you don't want to change the height and width of bitmap.
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }
}
