package com.sgs.mylibrary.screenshot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;


import com.sgs.mylibrary.util.LibConstants;

import java.io.File;
import java.io.FileOutputStream;

/**
 * this helper class is used for capturing the screenshots from the device
 * by getting the screen width and height pixels
 */
public class ScreenRecordingHelper {

    public static ScreenRecordingHelper instance;
    private Context context;

    /**
     * Private Constructor is used for initializing Singleton
     * the helper class with respect to context
     * @param context
     */
    private ScreenRecordingHelper(Context context) {
        this.context = context;
    }

    /**
     * helper class with sigleton model so only one time will be created
     * and multiple time will use the instance of it
     * @param context
     * @return
     */
    public static ScreenRecordingHelper getInstance(Context context) {
        if(instance == null)
            instance = new ScreenRecordingHelper(context);
        return instance;
    }

    // Parent directory where screen shots are stored

    /**
     * method used returns base snapshot directory as a file
     * @return
     */
    public File getBaseSnapshotStorageDirectory() {
        File internalStorageDirectory = context.getFilesDir();
        File directory = new File(internalStorageDirectory, "SDK_SGS_Snapshots");
        if(!directory.exists()) {
            directory.mkdirs();   // creating the directory if not exits
        }
        return directory;
    }

    /**
     * method returns sub directory as file
     * @param sessionId
     * @return
     */
    public File getSubDirectory(String sessionId) {
        File storageDirectory = context.getFilesDir();
        File directory = new File(storageDirectory,sessionId);

        if(!directory.exists())
            directory.mkdirs();

        return directory;
    }

    /**
     * method returns the file once the directory is created
     * @param context
     * @return
     */
    public static File createDirectory(Context context) {
        String pathToExternalStorage = Environment.getExternalStorageDirectory().toString();
        File internalStorageDirectory = context.getFilesDir();
        File appDirectory = new File(pathToExternalStorage + "/" + LibConstants.ZIPDIRECTORY);

       if(!appDirectory.exists())
        appDirectory.mkdirs();

        return appDirectory;
    }

    /**
     * method for creating directory of logs
     * @return
     */
    public File getBaseLogStorageDirectory() {
        File internalStorageDirectory = context.getFilesDir();
        File directory = new File(internalStorageDirectory, "Logs");
        if(!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * method for creating snapshot directory
     * @param sessionId
     * @return
     */
    public File getSnapshotStorageDirectory(String sessionId) {
        File directory = new File(getBaseSnapshotStorageDirectory(),
                "/" + getSubDirectory(sessionId));
        if(!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }


    /**
     * method returns the boolean flag as screenshot is taken or not
     * mehtod returns true means screen shot is successfully took,else it will retuns the false
     * @param activity
     * @param filePath
     * @param scale
     * @return
     */
    public boolean takeScreenShot(Activity activity, String filePath,int scale) {
        try {
            // Check For landscape
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int rotationAngle = activity.getWindowManager().getDefaultDisplay().getRotation();
            int deviceHeight = displayMetrics.heightPixels;
            int deviceWidth = displayMetrics.widthPixels;
            Bitmap bitmap = ScreenShotHelper.takeScreenshotBitmap(activity,deviceWidth,deviceHeight);
//            Timber.d("Rotation Angle :" + rotationAngle);

            if(bitmap.getWidth() > bitmap.getHeight()) {
                // rotate bitmap by 90 degree
                bitmap = rotateBitmap(bitmap, 90.0f);
            }

            File imageFile = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 50;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            return true;
        }
        catch(Throwable e) {
            Log.d(ScreenRecordingHelper.class.getSimpleName(), "takeScreenShot: " + e.getMessage());
        }
        return false;
    }

    /**
     * method returns the bitmap as output ,after the rotation of the bitmap
     * @param source
     * @param angle
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



}

