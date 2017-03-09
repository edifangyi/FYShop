package com.example.fangyi.fyshop.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fangy on 2017/3/7.
 */

public class OKHttpHelper {

    private static OkHttpClient okHttpClient;


    private Gson gson;

    private Handler handler;

    private OKHttpHelper() {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OKHttpHelper getInstance() {
        return new OKHttpHelper();
    }

    public void get(String url, BaseCallback callback) {
        Request request = builRequest(url, null, HttpMethodType.GET);
        doRequest(request, callback);

    }

    public void post(String url, Map<String, String> params, BaseCallback callback) {
        Request request = builRequest(url, params, HttpMethodType.POST);
        doRequest(request, callback);

    }


    public void doRequest(final Request request, final BaseCallback callback) {

        callback.onBeforeRequest(request);//弹出log对话框之类的

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                callback.onResponse(response);

                if (response.isSuccessful()) {

                    String resultStr = response.body().string();




                    if (callback.mType == String.class) {

                        callbackSuccess(callback, response, resultStr);

                    } else {
                        try {

                            Object object = gson.fromJson(resultStr, callback.mType);

                            callbackSuccess(callback, response, object);

                        } catch (JsonSyntaxException e) {

                            callbackError(callback, response, response.code(), e);// Json解析的错误
                        }
                    }

                } else {
                    callback.onError(response, response.code(), null);
                }

            }
        });
    }

    private Request builRequest(String url, Map<String, String> params, HttpMethodType methodType) {

        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = builFormData(params);
            builder.post(body);
        }

        return builder.build();
    }

    private RequestBody builFormData(Map<String, String> params) {

        FormBody.Builder body = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                body.add(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        return body.build();
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Object object, final Exception e
    ) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


    enum HttpMethodType {
        GET, POST
    }
}
