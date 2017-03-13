package com.example.fangyi.fyshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.fangyi.fyshop.adapter.HotWares2Adapter;
import com.example.fangyi.fyshop.bean.hot.Page;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.utils.Pager;
import com.example.fangyi.fyshop.view.DividerItemDecoration;
import com.example.fangyi.fyshop.widget.FYToolbar;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fangy on 2017/3/10.
 */

public class WareListActivity extends BaseActivity implements Pager.OnPageListener, TabLayout.OnTabSelectedListener, View.OnClickListener {

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;


    @BindView(R.id.tooblbar)
    FYToolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.txt_summary)
    TextView mTxtSummary;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview_wares;
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout refreshLayout;

    private HotWares2Adapter mWaresAdapter;

    private int orderBy = 0;
    private long campaignId = 0;

    private Pager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_warelist);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        campaignId = bundle.getLong(Contants.COMPAINGAIN_ID);
        KLog.e("=========" + campaignId + "+======" + orderBy);


        initToolBar();
        initTab();
        getData();
    }

    private void initToolBar() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });


        mToolbar.setRightButtonIcon(getDrawable(R.drawable.icon_grid_32));
        mToolbar.getRightButton().setTag(ACTION_LIST);


        mToolbar.setRightButtonOnClickListener(this);


    }


    private void initTab() {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_SALE);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_PRICE);
        tabLayout.addTab(tab);

        tabLayout.setOnTabSelectedListener(this);
    }

    public void getData() {

        pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(refreshLayout).setLoadMore(true).setOnPageListener(this).build(this, new TypeToken<Page<Wares>>() {
                }.getType());

        pager.request();
    }

    @Override
    public void load(List datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有" + totalCount + "件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HotWares2Adapter(this, datas);
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();

        if(ACTION_LIST == action){

            mToolbar.setRightButtonIcon(getDrawable(R.drawable.icon_list_32));
            mToolbar.getRightButton().setTag(ACTION_GIRD);

            mWaresAdapter.resetLayout(R.layout.template_grid_wares);


            mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this,2));

        }
        else if(ACTION_GIRD == action){


            mToolbar.setRightButtonIcon(getDrawable(R.drawable.icon_grid_32));
            mToolbar.getRightButton().setTag(ACTION_LIST);

            mWaresAdapter.resetLayout(R.layout.template_hot_wares);

            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
