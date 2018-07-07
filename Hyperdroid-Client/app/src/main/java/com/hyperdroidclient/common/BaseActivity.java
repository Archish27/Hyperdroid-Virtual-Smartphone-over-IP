package com.hyperdroidclient.common;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {

    protected ProgressDialog progressDialog;

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
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
    public void onNetworkException(Throwable e) throws ClassCastException {
//        try {
//            RetrofitException error = (RetrofitException) e;
//            try {
//                if (error.getKind().equals(RetrofitException.Kind.HTTP)) {
//                    handleHTTPError(error);
//                } else if (error.getKind().equals(RetrofitException.Kind.NETWORK)) {
//                    handleNetworkError(getString(R.string.network_error));
//                } else {
//                    handleUnknownError(getString(R.string.unknown_error));
//                }
//            } catch (IOException exception) {
//                handleUnknownError(getString(R.string.unknown_error));
//            }
//        } catch (ClassCastException exception) {
//            handleUnknownError(getString(R.string.system_error));
//        } finally {
//            dismissProgressDialog();
//        }
    }
//
//    protected void handleHTTPError(RetrofitException e) throws IOException {
//        ErrorResponse errorResponse = e.getErrorBodyAs(ErrorResponse.class);
//        handleHTTPErrorByStatusCode(errorResponse);
//    }
//
//    protected void handleHTTPErrorByStatusCode(ErrorResponse errorResponse) {
//        switch (errorResponse.status_code) {
//            default:
//                Toast.makeText(this, errorResponse.message, Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    protected void appUpgradeError() {
//        AppUpgradeDialogBuilder appUpgradeDialogBuilder = new AppUpgradeDialogBuilder(this);
//        appUpgradeDialogBuilder.show();
//
    }

    protected void accessTokenInvalidError() {
        /*SharedPreferencesManager.getInstance(this).setLoginStatus(false);
        SharedPreferencesManager.getInstance(this).setToken(null);
        Intent in = new Intent(this, LoginActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);*/
    }

    protected void handleNetworkError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void handleUnknownError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
