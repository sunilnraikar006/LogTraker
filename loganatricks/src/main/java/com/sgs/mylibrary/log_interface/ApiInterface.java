package com.sgs.mylibrary.log_interface;

import com.sgs.mylibrary.model.AddDeviceM;
import com.sgs.mylibrary.model.ConfigModel;
import com.sgs.mylibrary.model.ConfigModelResponse;
import com.sgs.mylibrary.model.Response;
import com.sgs.mylibrary.model.ResponseVedioUpload;
import com.sgs.mylibrary.model.UploadDataResponse;
import com.sgs.mylibrary.model.UploadPacketData;
import com.sgs.mylibrary.model.response.GeneralResponse;
import com.sgs.mylibrary.util.Constant;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * ApiInterface  contains all the endpoints used in the sdk
 */
public interface ApiInterface {

    /**
     * configUrl is used to send the below parameter to server
     * @param deviceId
     * @param token
     * @param devcetype
     * ConfigUrl is GET method
     */
    @GET(Constant.ConfigURl)
    Call<ConfigModelResponse> getConfiguration(@Path("deviceID") String deviceId, @Path("token") String token,
                                               @Path("devicetype") String devcetype);

    /**
     * AddPacketData url is used to send uploadData obj to server
     *   @param uploadData;
     *     addPacketData is POST method
     */
    @POST(Constant.AddPacketData)
    Call<UploadDataResponse> uploadPacketData(@Body UploadPacketData uploadData);

    /**
     * addDeviceUrl is used to send addDeviceM obj to server;
     *  param addDeviceM;
     *   addDevice is @POST method
     */
    @POST(Constant.ADD_DEVICE)
    Call<GeneralResponse> addDevice(@Body AddDeviceM addDeviceM);

    /**
     * UPLOAD_ZIPFILE url will send the below parameter to server
     * @param rbPrjId
     * @param rbSessionId
     * @param file
     * @param errBody
     * @param fpsBody
     * @param widthBody
     * @param heightBody
     * UPLOAD_ZIPFILE url is POST method
     */
    @Multipart
    @POST(Constant.UPLOAD_ZIPFILE)
    Call<GeneralResponse> uploadZipFile(
                                            @Part("projectId") RequestBody rbPrjId,
                                            @Part("sessionId") RequestBody rbSessionId,
                                            @Part MultipartBody.Part file,
                                            @Part("errorId") RequestBody errBody,
                                            @Part("fps") RequestBody fpsBody,
                                            @Part("width") RequestBody widthBody,
                                            @Part("height") RequestBody heightBody
    );
}
