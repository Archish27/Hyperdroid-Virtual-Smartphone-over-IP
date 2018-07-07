package com.hyperdroidclient.dashboard;

import com.hyperdroidclient.common.BaseContract;
import com.hyperdroidclient.data.local.remote.PublicIPResponse;

/**
 * Created by archish on 8/3/18.
 */

public interface MainContract {
    interface MainView extends BaseContract.BaseView{
        void onPublicIp(PublicIPResponse publicIPResponse);
    }
    interface MainPresenter{
        void getPublicIp(String format);
    }
}
