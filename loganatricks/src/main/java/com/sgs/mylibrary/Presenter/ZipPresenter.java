package com.sgs.mylibrary.Presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sgs.mylibrary.SessionHelper;
import com.sgs.mylibrary.log_interface.ApiInterface;
import com.sgs.mylibrary.log_interface.UploadZipI;
import com.sgs.mylibrary.log_interface.ZipI;
import com.sgs.mylibrary.model.ResponseVedioUpload;
import com.sgs.mylibrary.model.response.GeneralResponse;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.util.ApiInit;
import com.sgs.mylibrary.util.LibConstants;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ZipPresenter to submit zip file to the server using
 */
public class ZipPresenter implements UploadZipI {

    private static final String TAG = ZipPresenter.class.getSimpleName();
    private RequestBody rbPrjId;
    private RequestBody rbSessionId;
    private RequestBody rbErrorId;
    private RequestBody rbWidth;
    private RequestBody rbHeight;
    private RequestBody rbFps;
    private MultipartBody.Part serverFile;
    private static final String MULTIPART_FORMDATA = "multipart/form-data";
    private static final String MULTIPART_FILE = "/*";
    private ZipI zipI;

    /**
     * method is used for uploading zip files to server
     *
     * @param prjId
     * @param sessionId
     * @param errId
     * @param fps
     * @param zipFile
     * @param context
     * @param errorVideoAsset
     */
    public void uploadZipFile(String prjId, String sessionId, final String errId
            , int fps, File zipFile, final Context context,
                              final LAErrorVideoAsset errorVideoAsset) {
        try {
            // update to db that file is uploading
            SugarRecord.updEVAisUploaded(errId, errorVideoAsset, 1);
            Toast.makeText(context, "before", Toast.LENGTH_SHORT).show();
            Log.d("CheckServ", "3rd step");

            SessionHelper sessionHelper = SessionHelper.getInstance(context);
            int w = sessionHelper.getScreenWidthInPixels();
            int h = sessionHelper.getScreenHeightInPixels();

            rbPrjId = RequestBody.create(MediaType.parse(MULTIPART_FORMDATA), prjId);
            rbSessionId = RequestBody.create(MediaType.parse(MULTIPART_FORMDATA), sessionId);
            Log.d(TAG, "uploadZipFile: sessionId " + sessionId);
            Log.d(TAG, "uploadZipFile: errId " + errId);
            Log.d(TAG, "uploadZipFile: w " + w);
            Log.d(TAG, "uploadZipFile: h " + h);
            Log.d(TAG, "uploadZipFile: fps " + fps);
            rbErrorId = RequestBody.create(MediaType.parse(MULTIPART_FORMDATA), errId);
            rbWidth = RequestBody.create(MediaType.parse(MULTIPART_FORMDATA), String.valueOf(w));
            rbHeight = RequestBody.create(MediaType.parse(MULTIPART_FORMDATA), String.valueOf(h));
            rbFps = RequestBody.create(MediaType.parse(MULTIPART_FORMDATA), String.valueOf(fps));
        /*zipI = (ZipI) context;
        zipI.vedioUploading();*/

            if (zipFile != null) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse(MULTIPART_FILE), zipFile);

                serverFile =
                        MultipartBody.Part.createFormData("file", zipFile.getName(), requestFile);
                ApiInit.changePath("http://loganalyticssession.shrigowri.com:6501/");

                ApiInterface service = ApiInit.changeApiBaseUrl("http://loganalyticssession.shrigowri.com:6501/").create(ApiInterface.class);
                Call<GeneralResponse> call = service.uploadZipFile(rbPrjId, rbSessionId, serverFile, rbErrorId, rbFps, rbWidth, rbHeight);

                call.enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful()) {
                            File file = new File(context.getFilesDir(), LibConstants.ZIPDIRECTORY + File.separator + errId + ".zip");
                            deleteZip(file);
                            SugarRecord.updEVAisUploaded(errId, errorVideoAsset, 2);
                            errorVideoAsset.delete();

                        } else {
                            SugarRecord.updEVAisUploaded(errId, errorVideoAsset, 0);
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                    zipI.vedioUploadedFailed(t);
                        SugarRecord.updEVAisUploaded(errId, errorVideoAsset, 0);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method is used for deleting zip directory
     *
     * @param file
     */
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
