package com.sgs.mylibrary.util;

import android.app.IntentService;
import android.content.Intent;

import android.os.Build;

import android.util.Log;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.LogManager;
import com.sgs.mylibrary.Presenter.AddDeviceP;
import com.sgs.mylibrary.SessionHelper;
import com.sgs.mylibrary.log_interface.ApiInterface;
import com.sgs.mylibrary.model.AddDeviceM;
import com.sgs.mylibrary.model.AndroidSettingModel;
import com.sgs.mylibrary.model.ConfigModel;
import com.sgs.mylibrary.model.ConfigModelResponse;
import com.sgs.mylibrary.ormmodel.LAConfiguration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ConfigurationHelper is IntentService to send device details
 * like device id ,api level, version , width and height of the device in pixels to server
 */
public class ConfigurationHelper extends IntentService {


    private static final String TAG = ConfigurationHelper.class.getSimpleName();
    private LAConfiguration configuration;
    private ConfigurationHelper context = this;
    private SessionHelper sessionHelper;
    private ConfigurationHelper helper;

    /**
     * Constructor
     */
    public ConfigurationHelper() {
        super("ConfigurationHelper");
    }

    /**
     * OnCreate Method
     */
    @Override
    public void onCreate() {
        super.onCreate();
        helper = this;
    }

    /**
     * This method is called first ,Only one Intent is processed at a time,
     * but the processing happens on a worker thread that runs independently from application logic.
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String deviceId = intent.getStringExtra("deviceId");
        callApiforConfiguration(deviceId);
    }

    /**
     * method to get the configuration values with respect to the token,
     * the token which will get once we register the app in portal
     *
     * @param deviceId
     */
    private void callApiforConfiguration(String deviceId) {
        sessionHelper = SessionHelper.getInstance(helper);
        ApiInterface apiInterface = ApiInit.getRetrofit().create(ApiInterface.class);
        Call<ConfigModelResponse> configModelCall = apiInterface.getConfiguration(deviceId,
                SharedPref.getInstance().get(LibConstants.DEV_TOKEN, "nullValue"), "android");

        configModelCall.enqueue(new Callback<ConfigModelResponse>() {
            @Override
            public void onResponse(Call<ConfigModelResponse> call, Response<ConfigModelResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        ConfigModelResponse configModelResponse = response.body();
                        ConfigModel configModel = configModelResponse.getResult();
                        List<AndroidSettingModel> androidSettingModels = configModel.getAndroidSettings();
                        AndroidSettingModel settingModel = androidSettingModels.get(0);
                        configuration = new LAConfiguration(String.valueOf(configModel.getId()), configModel.getKey(), configModel.getSchemaName(),
                               true, settingModel.isInfoLog(), settingModel.isCrashLog(), settingModel.isErrorLog(), settingModel.isWarningLog(),
                                settingModel.getSessionTime(), settingModel.getVideoQuality(), settingModel.isEventsEnabled(), settingModel.getEventsDuration(),
                                settingModel.isHideSensitiveInfo(), settingModel.getMaxPacketUploadCount(),true,true);

                        if (!configuration.getKey().isEmpty()) {
                            configuration.update();

                            // add the device here to server
                            AddDeviceM addDeviceM = new AddDeviceM();
                            addDeviceM.setDeviceId(SessionHelper.getInstance(context).getDeviceID());
                            addDeviceM.setdM(Build.MODEL);
                            addDeviceM.setdV(Build.VERSION.RELEASE);
                            addDeviceM.setOs("android");
                            addDeviceM.setProjectId(configuration.getProjectId());
                            new AddDeviceP().addDevice(addDeviceM);
                        } else {
                            configuration.save();
                        }
                    }
                    else {
                        // assuming that it is 400 and token is invalid stop all the sdk functionality

                        if(!sessionHelper.getProjectId().equals("")){
                           //stop all the functionality of sdk
                            sessionHelper.stopSession();
                            sessionHelper.stopRecording();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ConfigModelResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                // dont start the service it self
            }
        });


    }

    private void insertDefault ( ) {
        configuration = new LAConfiguration("abcdefghijkl1235","value",
                "defaultSchema",true, true, true, true, true,
                3, "3", true, 3,true,
                3, true,true);
        configuration.save();
    }
}

