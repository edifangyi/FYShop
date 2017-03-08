package com.example.fangyi.fyshop.http;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fangy on 2017/3/8.
 */

public abstract class SpotsCallBack<T> extends BaseCallback<T> {


    SpotsDialog dialog;

    public SpotsCallBack(Context context) {
        dialog = new SpotsDialog(context);
    }

    public void showDialog() {

        dialog.show();

    }

    public void setMessage(String message) {
        dialog.setMessage(message);
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }


    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

}
