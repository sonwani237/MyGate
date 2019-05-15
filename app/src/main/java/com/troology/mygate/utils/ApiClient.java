package com.troology.mygate.utils;

import com.troology.mygate.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(ApplicationConstant.INSTANCE.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }else {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(ApplicationConstant.INSTANCE.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }


        }
        return retrofit;

    }
}
