package com.hyperdroidclient.data.local.remote.api;

import com.hyperdroidclient.data.local.remote.PublicIPResponse;

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
