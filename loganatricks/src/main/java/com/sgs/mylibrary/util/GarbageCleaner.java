package com.sgs.mylibrary.util;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.ormmodel.LASession;

import java.io.File;
import java.util.List;


/**
 * GarbageCleaner is intentService to delete unwanted session from db
 */
public class GarbageCleaner extends IntentService {


    private static final String TAG = GarbageCleaner.class.getSimpleName();

    public GarbageCleaner() {
        super("GarbageCleaner");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //get all the sessions from the db

        try {
            List<LASession> laSessions = DBHelper.getAllSession();

            if (laSessions != null && laSessions.size() > 0) {
                for (LASession laSession : laSessions) {
                    String sessionId =
                            laSession.getIdentifier();
                    //check whether this sessionid is have any eva or not
                    //get the eva by this session id
                    LAErrorVideoAsset laErrorVideoAsset = DBHelper.getErrorVideoAssetBySessionId(sessionId);
                    //check whether this error video asset as error id is 0 or null
                    if (laErrorVideoAsset != null) {
                        String session = laErrorVideoAsset.getIdentifier();
                        if (session == null) {
                            laSession.delete();
                        } else {
                            int isUploaded = laErrorVideoAsset.getIsUploaded();
                            int isCreated = laErrorVideoAsset.getIsCreated();

                            if (isCreated == 0 || isUploaded == 2) {
                                deleteByFolder(sessionId);
                                deleteSessionData(sessionId);
                            }
                        }
                    } else {
                        //delete the folders
                        deleteByFolder(sessionId);
                        deleteSessionData(sessionId);
                    }
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "onHandleIntent: " + e.getMessage());
        }
        //get the Error Video Asset where error id is o or null


        //then get the sess
        //get the Error Video Asset where error id is o or null


        //then get the session id of that eva and delete that session data

    }

    /**
     * method to delete the session data by sessionId
     * @param sessionId
     */
    private void deleteSessionData(String sessionId) {

        try {
            DBHelper.deleteSessionData(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * method to deleting the files using session id and deleting the directory if directory is empty
     *
     * @param sessionId
     */
    private void deleteByFolder(String sessionId) {

        try {
            File baseInputDirectory = new File(getFilesDir(), LibConstants.SNAP_SHOT_DIRECTORY);
            File sessionDirectory = new File(baseInputDirectory + "/" + sessionId);
            File[] files = sessionDirectory.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    deleteScreenShots(file);
                }
            }

            if (sessionDirectory.isDirectory())
                sessionDirectory.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * deletes the unwanted screenshots
     *
     * @param file
     */
    private void deleteScreenShots(File file) {
        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteScreenShots(f);
                }
            }
            file.delete();
        } catch (Exception e) {
            Log.e(TAG, "deleteScreenShots: " + e.getMessage());
        }
    }
}
