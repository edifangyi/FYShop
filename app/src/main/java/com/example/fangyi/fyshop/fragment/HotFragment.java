package com.example.fangyi.fyshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.WareDetailActivity;
import com.example.fangyi.fyshop.adapter.HotWares2Adapter;
import com.example.fangyi.fyshop.adapter.base.BaseAdapter;
import com.example.fangyi.fyshop.bean.hot.Page;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.utils.Pager;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotFragment extends BaseFragment implements Pager.OnPageListener {


    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_view)
    MaterialRefreshLayout mRefreshLaout;
    private HotWares2Adapter mAdatper;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {

        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLaout)
                .build(getContext(), new TypeToken<Page<Wares>>() {
                }.getType());


        pager.request();
    }


    @Override
    public void load(List datas, int totalPage, int totalCount) {
        mAdatper = new HotWares2Adapter(getContext(), datas);

        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Wares wares = mAdatper.getItem(position);

                Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                intent.putExtra(Contants.WARE, wares);
                startActivity(intent);


            }
        });


        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mAdatper.refreshData(datas);

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mAdatper.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
    }
}
