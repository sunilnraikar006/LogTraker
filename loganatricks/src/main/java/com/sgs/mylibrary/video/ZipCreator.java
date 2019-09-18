package com.sgs.mylibrary.video;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.screenshot.ScreenRecordingHelper;
import com.sgs.mylibrary.util.LibConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * ZipCreator is an IntentService for creating zipfile
 */
public final class ZipCreator extends IntentService {

    private static final String TAG = ZipCreator.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;
    Handler delayHandler;
    ScreenRecordingHelper screenRecordingHelper;
    File replayFile;
    private boolean flag = false;
    private String errorId;
    private String sesId;
    private LAErrorVideoAsset eva;
    private File inputDirectory;

    public ZipCreator() {
        super(ZipCreator.class.getSimpleName());
    }


    /**
     * method will convert Images to zip format using the below parameters
     *
     * @param destinationPath
     * @param destinationFileName
     * @param eva
     */
    private void convertToZip(String destinationPath, String destinationFileName, LAErrorVideoAsset eva) {

        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream = null;
        errorId = eva.identifier;
        sesId = eva.sessionIdentifier;
        try {
            if (!destinationPath.endsWith("/")) destinationPath += "/";
            String destination = destinationPath + destinationFileName;
            File file = new File(destination);
            if (!file.exists()) file.createNewFile();

            fileOutputStream = new FileOutputStream(file);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));


            boolean fileCreated = zipIt(zipOutputStream, destinationFileName + errorId);
            if (fileCreated) {
                updateDBSndToserver();
            }
        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage());

        } finally {
            if (zipOutputStream != null)
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                }
        }
    }

    /**
     * method will update ErrorVedioAsset isCreated to 1 in localDb
     */
    private void updateDBSndToserver() {
//        DBHelper.updateEVA(eva.identifier,eva.sessionIdentifier);
        try {

            boolean updated = eva.updEvaErrId(eva.getIdentifier(), eva);
//        DBHelper.updateEVAErrid();

           /* if (updated)
                deleteImgs(inputDirectory);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method will delete all the files in a directory
     *
     * @param file deleting the particular directory after creating zipFile successFully
     */
    private void deleteImgs(File file) {

        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteImgs(f);
                }
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * method will zip the set of images
     *
     * @param zipOutputStream
     * @param sourcePath
     * @return
     */
    private boolean zipIt(ZipOutputStream zipOutputStream, String sourcePath) {
        try {
            File baseInputDirectory = new File(this.getFilesDir(), LibConstants.SNAP_SHOT_DIRECTORY);

            inputDirectory = new File(baseInputDirectory + "/" + sesId + "/" + errorId);
            File[] inputFiles = inputDirectory.listFiles();

            if (inputFiles != null && inputFiles.length > 0) {
                String entryPath = "";
                BufferedInputStream input;
                for (File file : inputFiles) {
                    if (file.isDirectory()) {
                        zipIt(zipOutputStream, file.getPath());
                    } else {
                        byte data[] = new byte[BUFFER_SIZE];
                        FileInputStream fileInputStream = new FileInputStream(file.getPath());
                        input = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
//                    entryPath = file.getAbsolutePath().replace(sourcePath, "");

                        ZipEntry entry = new ZipEntry(file.getName());
                        zipOutputStream.putNextEntry(entry);
                        int count;
                        while ((count = input.read(data, 0, BUFFER_SIZE)) != -1) {
                            zipOutputStream.write(data, 0, count);
                        }
                        input.close();
                    }
                }
            } else {
                deleteImgs(inputDirectory);
                return false;
            }
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * service life cycle call back
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        eva = intent.getParcelableExtra(LibConstants.EVA);
        startZippingLog(eva);
    }

    /**
     * method will start zipping
     *
     * @param eva
     */
    private void startZippingLog(LAErrorVideoAsset eva) {
        try {
            File baseInputDirectory = new File(getFilesDir(), LibConstants.SNAP_SHOT_DIRECTORY);
            // File inputDirectory = new File(baseInputDirectory + sessionId);
            File[] inputFiles = baseInputDirectory.listFiles();
            String destinationPath = getFilesDir() + File.separator + LibConstants.ZIPDIRECTORY;

            new File(destinationPath).mkdirs();
            convertToZip(destinationPath, eva.identifier + ".zip", eva);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * service call back method
     * lowMemory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * trimMemory
     * service call back
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * service call back method
     *
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // Get The Sessions
        flag = true;
    }

    /**
     * Service call back method
     * Service will be Killed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // this method will create the video and saves to application directory with errorId.mp4 as name
    public void createVideo(LAErrorVideoAsset eva) {
        // 1 - screensCount =  check endIndex - startIndex
        // 2 - if screensCount > 100 then set startIndex = endIndex - (endIndex - 100
        // 3 - set eva.isCreated = 1 and save
        // 4 - get all screenshots in an array
        // 5 - create file path
        // 6 - convert screenshots to video and save at filepath
        // 7 - if success don't do anything
        // 8 - if failed eva.iscreated = 0 and save
    }

}
