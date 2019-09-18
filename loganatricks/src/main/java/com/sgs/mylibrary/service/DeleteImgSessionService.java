package com.sgs.mylibrary.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.util.DBHelper;
import com.sgs.mylibrary.util.LibConstants;

import java.io.File;
import java.util.List;

/**
 * IntentService Class to used to delete the sceenshots which is used to create zip fie
 * after successful creation of zip file this class will be called to delete the unwanted screen shots.
 */
public class DeleteImgSessionService extends IntentService {

    private File inputDirectory;

    /**
     * DeleteImgSessionService no arg-Constructor
     */
    public DeleteImgSessionService ( ) {
        super(DeleteImgSessionService.class.getSimpleName());
    }

    /**
     * This method is called first ,Only one Intent is processed at a time,
     * but the processing happens on a worker thread that runs independently from application logic.
     * @param intent
     */
    @Override
    protected void onHandleIntent ( @Nullable Intent intent ) {
        List<LAErrorVideoAsset> eva = DBHelper.getListEVA();
        for(LAErrorVideoAsset idx : eva) {
            String errId = idx.identifier;
            String sessionId = idx.sessionIdentifier;
            int isCreated = idx.isCreated;
            if(isCreated == 1 || errId != null) {
                // delete the images and clear the db

                DBHelper.deleteScreenshots(errId);

                File baseInputDirectory = new File(this.getFilesDir(), LibConstants.SNAP_SHOT_DIRECTORY);
                inputDirectory = new File(baseInputDirectory + "/" + sessionId + "/" + errId);
                if(inputDirectory.isDirectory())
                    deleteImgs(inputDirectory);
            }
        }
    }

    private void deleteImgs(File file) {

        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteImgs(f);
            }
        }
        file.delete();
    }
}
