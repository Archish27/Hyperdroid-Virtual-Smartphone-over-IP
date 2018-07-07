package com.example.nikhil.hyperdroid_backgroundservices;



import com.example.nikhil.hyperdroid_backgroundservices.network.RxErrorHandlingCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Archish on 1/14/2017.
 */

public class RetrofitObj {
    public static Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
    }

}
