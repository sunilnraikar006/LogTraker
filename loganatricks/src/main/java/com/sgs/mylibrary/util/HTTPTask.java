package com.sgs.mylibrary.util;



import com.sgs.mylibrary.log_interface.HTTP;
import com.sgs.mylibrary.request_body.BinaryDataRequestBody;
import com.sgs.mylibrary.request_body.ProgressRequestBody;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HTTPTask implements HTTP {

    private OkHttpClient client;
    public static HTTPTask instance;

    private HTTPTask() {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
                .build();
    }

    public static HTTPTask getInstance() {
        if(instance == null)
            instance = new HTTPTask();
        return instance;
    }

    @Override
    public void get(String url, Object headers, Object callback) {
        Request request = new Request.Builder()
                .headers((Headers) headers)
                .url(Constant.RequestBuilder.buildURL(url))
                .build();
        client.newCall(request).enqueue((Callback) callback);
    }

    public void rawPost(String url, MediaType mediaType, InputStream inputStream,
                        Headers headers, Callback callback) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.MINUTES);
        builder.readTimeout(15, TimeUnit.MINUTES);
        OkHttpClient rawPostClient = builder.build();
        RequestBody requestBody = new BinaryDataRequestBody(mediaType, inputStream);
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(requestBody)
                .build();
        rawPostClient.newCall(request).enqueue(callback);
    }

    public void rawPost(String url, RequestBody requestBody,
                        Headers headers, Callback callback) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient rawPostClient = builder.build();
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(requestBody)
                .build();
        rawPostClient.newCall(request).enqueue(callback);
    }

    public void multipartPost(String url, Headers headers, File file, MediaType mediaType,
                              Callback callback, Object progressCallback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", file.getName(), RequestBody.create(mediaType, file));
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .post(new ProgressRequestBody(builder.build(),
                        (ProgressRequestBody.Listener) progressCallback))
                .build();
        client.newCall(request).enqueue(callback);
    }

}
