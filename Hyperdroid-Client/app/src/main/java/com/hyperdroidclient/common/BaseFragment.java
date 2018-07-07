package com.hyperdroidclient.common;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**
 * Created by Archish on 10/06/2016.
 */

public class BaseFragment extends Fragment implements BaseContract.BaseView {
    protected ProgressDialog progressDialog;

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onNetworkException(Throwable e) {
        try {
            ((BaseActivity) getActivity()).onNetworkException(e);
        } catch (ClassCastException cce) {
            throw new ClassCastException("Fragments extending BaseFragment must be placed in a Activity" +
                    "extending BaseActivity");
        }
    }
}
