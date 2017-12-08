package com.example.mladen.masterradandroid.retrofit;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    //public static final String BASE_URL = "http://opendata.mpn.gov.rs";
    public static final String BASE_URL = "http://192.168.0.14:61615";

    private static RestApi requestInterface = null;

    public static RestApi getClient() {
        if (requestInterface == null) {
             requestInterface = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RestApi.class);
        }
        return requestInterface;
    }
}