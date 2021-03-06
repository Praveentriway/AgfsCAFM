package com.daemon.emco_android.utils.meterReading.Helpers;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BitmapWriter {

    private static final String LOG_TAG = "BitmapWriter";

    public static File write(Bitmap bmp, String suffix) {

        /**
         * Creating a png file on disk
         */
        File pictureFile = MediaHelper.getOutputMediaFile(suffix);
        if (pictureFile == null) {
            Log.d(LOG_TAG, "Error creating media file, check storage permissions: ");
            return null;
        }

        /**
         * Compressing bitmap into file as PNG
         */
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return pictureFile;
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Error accessing file: " + e.getMessage());
        }
        return null;
    }
}
