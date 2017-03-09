package com.example.fangyi.fyshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.HotWares2Adapter;
import com.example.fangyi.fyshop.adapter.base.BaseAdapter;
import com.example.fangyi.fyshop.bean.hot.Page;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.http.OKHttpHelper;
import com.example.fangyi.fyshop.http.SpotsCallBack;
import com.example.fangyi.fyshop.view.DividerItemDecoration;

import java.util.List;

import okhttp3.Response;

public class HotFragment extends Fragment {

    private OKHttpHelper httpHelper = OKHttpHelper.getInstance();
    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private List<Wares> datas;


    private HotWares2Adapter mAdatper;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout mRefreshLaout;

    private static final int STATE_NORMAL = 0;//正常状态
    private static final int STATE_REFREH = 1;//下拉刷新状态
    private static final int STATE_MORE = 2;//加载更多状态

    private int state = STATE_NORMAL;//当前状态

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        initView(view);
        getData();
        return view;

    }

    private void initRefreshLayout() {
        mRefreshLaout.setLoadMore(true);
        //下拉刷新监听
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            //在刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            //刷新加载更多
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currPage <= totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getContext(), "已经没有了", Toast.LENGTH_SHORT).show();
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRefreshLaout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        initRefreshLayout();
    }

    private void getData() {
        String url = Contants.API.WARES_HOT + "?curPage=" + currPage + "&pageSize=" + pageSize;
        httpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    private void refreshData() {
        currPage = 1;
        state = STATE_REFREH;
        getData();
    }

    private void loadMoreData() {
        currPage = ++currPage;
        state = STATE_MORE;
        getData();
    }


    private void showData() {
        switch (state) {

            case STATE_NORMAL:
//                mAdatper = new HotWaresAdapter(datas);
//                mRecyclerView.setAdapter(mAdatper);

                mAdatper = new HotWares2Adapter(getContext(), datas);

                mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });




                mRecyclerView.setAdapter(mAdatper);
                break;

            case STATE_REFREH:
                mAdatper.clearData();
                mAdatper.addData(datas);

                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mAdatper.addData(mAdatper.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

    }
}
