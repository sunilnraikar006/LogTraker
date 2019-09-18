package com.sgs.mylibrary.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.log_interface.ApiInterface;
import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.model.UploadDataResponse;
import com.sgs.mylibrary.model.UploadPacketData;
import com.sgs.mylibrary.util.ApiInit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * IntentService Class to used to upload the data ,
 * in terms of packet data to server using retrofit library
 */
public class UploadDataService extends IntentService {
    private static final String TAG = UploadDataService.class.getSimpleName();

    /**
     * UploadDataService no arg-Constructor
     */
    public UploadDataService() {
        super("UploadDataService");
    }

    /**
     * This method is called first ,Only one Intent is processed at a time,
     * but the processing happens on a worker thread that runs independently from application logic.
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            List<PacketData> packetData = intent.getParcelableArrayListExtra("packetData");
            UploadPacketData uploadPacketData = new UploadPacketData();
            uploadPacketData.setAppVersion(intent.getStringExtra("AppVersion"));
            uploadPacketData.setDeviceId(intent.getStringExtra("deviceid"));
            uploadPacketData.setPacketTime(intent.getStringExtra("packagetime"));
            uploadPacketData.setPlatform(intent.getStringExtra("platform"));
            uploadPacketData.setSessionId(intent.getStringExtra("sessionId"));
            uploadPacketData.setProjectId(intent.getStringExtra("projectId"));
            uploadPacketData.setPacketData(packetData);


            ApiInterface apiInterface = ApiInit.getRetrofit().create(ApiInterface.class);


            Call<UploadDataResponse> uploadData = apiInterface.uploadPacketData(uploadPacketData);

            uploadData.enqueue(new Callback<UploadDataResponse>() {


                @Override
                public void onResponse(Call<UploadDataResponse> call, Response<UploadDataResponse> response) {
                    if (response.isSuccessful()) {

                    }

                }

                @Override
                public void onFailure(Call<UploadDataResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
