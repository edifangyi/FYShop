package com.example.fangyi.fyshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.CategoryListAdapter;
import com.example.fangyi.fyshop.adapter.CategoryWaresListAdapter;
import com.example.fangyi.fyshop.adapter.base.BaseAdapter;
import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.category.CategoryList;
import com.example.fangyi.fyshop.bean.home.BannerOnline;
import com.example.fangyi.fyshop.bean.hot.Page;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.example.fangyi.fyshop.http.BaseCallback;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.http.OkHttpHelper;
import com.example.fangyi.fyshop.http.SpotsCallBack;
import com.example.fangyi.fyshop.loader.GlideImageLoader;
import com.example.fangyi.fyshop.view.DividerGridItemDecoration;
import com.example.fangyi.fyshop.view.DividerItemDecoration;
import com.socks.library.KLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class CategoryFragment extends Fragment {

    private Banner banner;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerviewWares;
    private MaterialRefreshLayout mRefreshLaout;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private CategoryListAdapter mCategoryListAdapter;
    private CategoryWaresListAdapter mCategoryWaresListAdapter;

    private List<Wares> mWares;

    private List<String> images = FYApp.images;
    private List<String> titles = FYApp.titles;
    private List<String> images2 = new ArrayList<>();
    private List<String> titles2 = new ArrayList<>();

    private int curPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long category_id = 0;
    private int totalCount = 10;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);


        initBanner(view);
        initRecyclerView(view);
        initRecyclerviewWares(view);
        initRefreshLaout(view);

        requestCategoryBannerData();
        requestCategoryListData();

        return view;
    }


    private void requestCategoryBannerData() {
        okHttpHelper.get(Contants.API.BANNER_CATEGORY + "?type=1",new SpotsCallBack<List<BannerOnline>>(getContext()) {


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

    private void requestCategoryListData() {

        okHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<CategoryList>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<CategoryList> categoryLists) {

                showCategoryList(categoryLists);

                if (categoryLists != null && categoryLists.size() > 0) {
                    category_id = categoryLists.get(0).getId();
                }
                requestCategoryWaresData(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void requestCategoryWaresData(long categoryListId) {

        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryListId + "&curPage=" + curPage + "&pageSize=" + pageSize;

        KLog.e("===" + url);

        okHttpHelper.get(url, new BaseCallback<Page<Wares>>() {


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
            public void onSuccess(Response response, Page<Wares> waresPage) {

                curPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                totalCount = waresPage.getTotalCount();
                mWares = waresPage.getList();
                showWaresListData(waresPage.getList());

            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void showCategoryList(List<CategoryList> categoryLists) {
        mCategoryListAdapter = new CategoryListAdapter(getContext(), categoryLists);
        mCategoryListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                CategoryList categoryList = mCategoryListAdapter.getItem(position);
                category_id = categoryList.getId();
                curPage = 1;
                state = STATE_NORMAL;
                requestCategoryWaresData(category_id);
            }
        });
        mRecyclerView.setAdapter(mCategoryListAdapter);
    }

    private void showWaresListData(List<Wares> wares) {
        switch (state) {

            case STATE_NORMAL:

                if (mCategoryWaresListAdapter == null) {
                    mCategoryWaresListAdapter = new CategoryWaresListAdapter(getContext(), wares);
                    mRecyclerviewWares.setAdapter(mCategoryWaresListAdapter);
                } else {
                    mCategoryWaresListAdapter.clear();
                    mCategoryWaresListAdapter.addData(wares);
                }

                break;

            case STATE_REFREH:
                mCategoryWaresListAdapter.clear();
                mCategoryWaresListAdapter.addData(wares);

                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mCategoryWaresListAdapter.addData(mCategoryWaresListAdapter.getDatas().size(), wares);
                mRecyclerviewWares.scrollToPosition(mCategoryWaresListAdapter.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

    }


    private void initBanner(View view) {
        banner = (Banner) view.findViewById(R.id.banner_category);

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setBannerTitles(titles);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.isAutoPlay(true);
        banner.setDelayTime(1000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.start();
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_category);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    private void initRecyclerviewWares(View view) {
        mRecyclerviewWares = (RecyclerView) view.findViewById(R.id.recyclerview_wares);
        mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
        mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }

    private void initRefreshLaout(View view) {
        mRefreshLaout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                KLog.e("====" + curPage + " ========= " + totalPage);

                if (mWares.size() <= totalCount)
                    loadMoreData();
                else {
//                    Toast.makeText()
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {
        curPage = 1;
        state = STATE_REFREH;
        requestCategoryWaresData(category_id);
    }

    private void loadMoreData() {
        curPage = ++curPage;
        state = STATE_MORE;

        KLog.e("====" + category_id + " ========= " + curPage);
        requestCategoryWaresData(category_id);
    }

}
