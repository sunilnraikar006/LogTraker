package com.sgs.mylibrary.Presenter;

import android.util.Log;

import com.sgs.mylibrary.log_interface.AddDeviceI;
import com.sgs.mylibrary.log_interface.ApiInterface;
import com.sgs.mylibrary.model.AddDeviceM;
import com.sgs.mylibrary.model.response.GeneralResponse;
import com.sgs.mylibrary.util.ApiInit;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AddDeviceP to submit deviceInfo to server using AddDeviceI interface
 */
public class AddDeviceP implements AddDeviceI {

    private final static String TAG = "AddDeviceP";

    /**
     * method is used for adding the device information to server
     * @param addDeviceM
     */
    @Override
    public void addDevice(AddDeviceM addDeviceM) {
       ApiInterface service = ApiInit.getRetrofit().create(ApiInterface.class);
        Call<GeneralResponse> call = service.addDevice(addDeviceM);

        Log.d(TAG, "addDevice: body "+ addDeviceM);
        Log.d(TAG, "addDevice: url "+ call.request().url());

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if(response.isSuccessful()) {

                }
                else {
                    try {
                        Log.d(TAG, "onResponse: failure " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
