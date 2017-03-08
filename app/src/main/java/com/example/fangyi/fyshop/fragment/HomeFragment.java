package com.example.fangyi.fyshop.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.HomeCatgoryAdapter;
import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.home.BannerOnline;
import com.example.fangyi.fyshop.bean.home.Campaign;
import com.example.fangyi.fyshop.http.BaseCallback;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.http.OKHttpHelper;
import com.example.fangyi.fyshop.http.SpotsCallBack;
import com.example.fangyi.fyshop.loader.GlideImageLoader;
import com.example.fangyi.fyshop.view.DividerItemDecortion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeFragment extends Fragment implements OnBannerListener {


    private OKHttpHelper httpHelper = OKHttpHelper.getInstance();
    private Banner banner;
    private RecyclerView mRecyclerView;
    private HomeCatgoryAdapter mAdatper;
    private Gson mGson = new Gson();
    private List<BannerOnline> mBannerOnlines;

    private List<String> images = FYApp.images;
    private List<String> titles = FYApp.titles;


    private List<String> images2 = new ArrayList<>();
    private List<String> titles2 = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    banner.update(images2, titles2);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        initBanner(view);
        initRecyclerView(view);

//        requestImages();//默认
        requestImages2();//封装后


        return view;


    }



    private void requestImages2() {
        String url = Contants.API.BANNER_HOME;

        httpHelper.get(url + "?type=1", new SpotsCallBack<List<BannerOnline>>(this.getContext()) {


            @Override
            public void onSuccess(Response response, List<BannerOnline> bannerOnlines) {

                for (BannerOnline mBannerOnline : bannerOnlines) {
                    images2.add(mBannerOnline.getImgUrl());
                    titles2.add(mBannerOnline.getName());
                }
                banner.update(images2, titles2);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void requestImages() {
        String url = Contants.API.BANNER_HOME;
        ;
        OkHttpClient client = new OkHttpClient();


        RequestBody body = new FormBody.Builder().add("type", "1").build();

        Request request = new Request.Builder().url(url).post(body).build();


        client.newCall(request).enqueue(new Callback() {//异步

            //请求网络时出现不可恢复的错误时调用该方法
            @Override
            public void onFailure(Call call, IOException e) {

            }

            //请求网络成功时调用该方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();

                KLog.e("=====" + response.isSuccessful());

                //http 状态码 大于200 并且小于300
                if (response.isSuccessful()) {//成功
                    String json = response.body().string();

                    KLog.json(json);

                    Type type = new TypeToken<List<BannerOnline>>() {
                    }.getType();

                    mBannerOnlines = mGson.fromJson(json, type);

                    if (mBannerOnlines != null) {
                        for (BannerOnline mBannerOnline : mBannerOnlines) {
                            images2.add(mBannerOnline.getImgUrl());
                            titles2.add(mBannerOnline.getName());
                        }

                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }
            }
        });

    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<Campaign>>() {

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<Campaign> campaigns) {
                inidData(campaigns);
            }



            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void inidData(List<Campaign> campaigns) {
//        mAdatper = new HomeCatgoryAdapter(FYApp.datas);//本地

        mAdatper = new HomeCatgoryAdapter(campaigns,this.getContext());

       mAdatper.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
           @Override
           public void onClick(View view, Campaign.CpOneBean campaign) {
               Toast.makeText(getContext(), "campaign:" + campaign.getTitle(), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onClick(View view, Campaign.CpTwoBean campaign) {
               Toast.makeText(getContext(), "campaign:" + campaign.getTitle(), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onClick(View view, Campaign.CpThreeBean campaign) {
               Toast.makeText(getContext(), "campaign:" + campaign.getTitle(), Toast.LENGTH_SHORT).show();
           }
       });

        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.addItemDecoration(new DividerItemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }


    private void initBanner(View view) {
        banner = (Banner) view.findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合


        banner.setImages(images);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);

        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        //设置监听
        banner.setOnBannerListener(this);
    }


    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getContext(), "第" + position + "个", Toast.LENGTH_SHORT).show();
    }
}
