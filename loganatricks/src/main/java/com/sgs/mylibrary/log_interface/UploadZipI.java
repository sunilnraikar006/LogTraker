package com.sgs.mylibrary.log_interface;
import android.content.Context;

import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import java.io.File;

/**
 * UploadZipI interface to submit file to server
 */
public interface UploadZipI {
    void uploadZipFile( String prjId, String sessionId, String errId,
                        int fps, File file, Context context, LAErrorVideoAsset eva
    );
}
