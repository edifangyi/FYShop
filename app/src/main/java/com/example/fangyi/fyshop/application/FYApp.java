package com.example.fangyi.fyshop.application;


import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.bean.home.local.HomeCategory;
import com.example.fangyi.fyshop.bean.login.User;
import com.example.fangyi.fyshop.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fangy on 2017/3/7.
 */

public class FYApp extends Application {

    public static FYApp app;

    public static List<String> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static List<HomeCategory> datas = new ArrayList<>(15);

    private User user;

    private static FYApp instance;


    public static FYApp getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        app = this;

        initUser();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())//渐进式JPEG 只支持网络图片
                .build();
        Fresco.initialize(this,config);


//        initLocalData();

    }
    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }



    public User getUser() {
        return user;
    }

    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private Intent intent;


    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        this.intent =null;
    }




    private void initLocalData() {
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        images = new ArrayList<>(Arrays.asList(urls));
        titles = new ArrayList<>(Arrays.asList(tips));

        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
        datas.add(category);
        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
        datas.add(category);
    }
}
