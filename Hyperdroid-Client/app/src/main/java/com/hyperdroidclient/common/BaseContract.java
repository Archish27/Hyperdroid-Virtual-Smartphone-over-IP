package com.hyperdroidclient.common;

/**
 * Created by Archish on 10/06/2016.
 */

public interface BaseContract {
    interface BaseView {
        void onNetworkException(Throwable e);
    }
}
