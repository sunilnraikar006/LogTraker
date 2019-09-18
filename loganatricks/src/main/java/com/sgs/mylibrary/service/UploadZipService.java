package com.sgs.mylibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.Presenter.ZipPresenter;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.util.LibConstants;

import java.io.File;

/**
 * IntentService Class to used to upload the data ,
 * in terms of zip file to server using retrofit library
 */
public class UploadZipService extends IntentService {

    private static final String TAG = "UploadZipService";

    /**
     * UploadZipService no arg-Constructor
     */
    public UploadZipService() {
        super(UploadZipService.class.getSimpleName());
    }

    /**
     * This method is called first ,Only one Intent is processed at a time,
     * but the processing happens on a worker thread that runs independently from application logic.
     *
     * @param intent fps, errorId, projectId,w,h are the parameters required to generate zip file
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            if (intent != null) {

                LAErrorVideoAsset eva = intent.getParcelableExtra(LibConstants.EVA);
                String sessiodId = intent.getStringExtra("sessionId");
                String errorId = intent.getStringExtra("errorId");
                String projectId = intent.getStringExtra("projectId");
                int fps = intent.getIntExtra("fps", -1);
                File file = new File(getFilesDir(), LibConstants.ZIPDIRECTORY + File.separator + errorId + ".zip");

                long fileSizeInBytes = file.length();
                long fileSizeInKB = fileSizeInBytes / 1024;


                if (fileSizeInKB == 0) {
                    deleteZip(file);  // delete the file
                    delEvaRecord(eva);
                } else {
                    new ZipPresenter().uploadZipFile(projectId,
                            sessiodId,
                            errorId,
                            fps, file, this, eva);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delEvaRecord(LAErrorVideoAsset eva) {
        eva.delete();
    }

    private void deleteZip(File file) {
        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteZip(f);
                }
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


