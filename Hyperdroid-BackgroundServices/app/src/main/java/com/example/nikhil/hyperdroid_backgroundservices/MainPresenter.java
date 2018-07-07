package com.example.nikhil.hyperdroid_backgroundservices;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by archish on 8/3/18.
 */

public class MainPresenter implements MainContract.MainPresenter {

    RestService restService;
    MainContract.MainView mainView;

    public MainPresenter(RestService restService, MainContract.MainView mainView) {
        this.restService = restService;
        this.mainView = mainView;
    }

    @Override
    public void getPublicIp(String format) {
        restService.getPublicIP(format)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<PublicIPResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mainView!=null)
                            mainView.onNetworkException(e);
                    }

                    @Override
                    public void onNext(PublicIPResponse publicIPResponse) {
                        if(mainView!=null)
                            mainView.onPublicIp(publicIPResponse);
                    }
                });
    }
}
