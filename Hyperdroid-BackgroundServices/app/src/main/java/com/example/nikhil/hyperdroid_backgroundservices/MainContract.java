package com.example.nikhil.hyperdroid_backgroundservices;

/**
 * Created by archish on 8/3/18.
 */

public interface MainContract {
    interface MainView{
        void onPublicIp(PublicIPResponse publicIPResponse);

        void onNetworkException(Throwable e);
    }
    interface MainPresenter{
        void getPublicIp(String format);
    }
}
