package com.example.nikhil.hyperdroid_backgroundservices;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by archish on 8/3/18.
 */

public interface RestService {
    @GET("/")
    Observable<PublicIPResponse> getPublicIP(@Query("format") String format);
}
