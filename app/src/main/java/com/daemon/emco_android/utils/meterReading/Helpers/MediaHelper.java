package com.daemon.emco_android.utils.meterReading.Helpers;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.daemon.emco_android.utils.meterReading.Utils.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MediaHelper {

    private static final String LOG_TAG = "MediaHelper";

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(String suffix) {
        return Uri.fromFile(getOutputMediaFile(suffix));
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(String suffix) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.CAPTURE_PATH);

        /**
         * Create directory if it does not exist
         */
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(LOG_TAG, "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "SHAILI_" + timeStamp + "_" + suffix + ".png");
    }
}
