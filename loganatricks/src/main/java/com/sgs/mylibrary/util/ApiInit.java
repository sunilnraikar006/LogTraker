package com.sgs.mylibrary.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * ApiInit class for invoking the api calls
 */
public class ApiInit {

    private static Retrofit mRetrofit;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constant.BASE_URL);

    /**
     * method used for intialising the retrofit library
     *
     * @return retrofit callback
     */
    private static Retrofit initializeRetrofit() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return mRetrofit;
    }

    public static Retrofit changeApiBaseUrl(String newApiBaseUrl) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(newApiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return mRetrofit;
    }

    /**
     * method is used for getting the retrofit instance
     *
     * @return
     */
    public static Retrofit getRetrofit() {

        if (mRetrofit != null) {
            return mRetrofit;
        } else {
            return initializeRetrofit();
        }
    }

    public static Retrofit changePath(String url) {
        if (mRetrofit != null)
            return mRetrofit;
        else
            return
                    changeApiBaseUrl(url);

    }
}
