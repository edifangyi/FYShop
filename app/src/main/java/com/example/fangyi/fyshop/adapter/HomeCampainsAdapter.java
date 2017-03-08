package com.example.fangyi.fyshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.bean.home.local.HomeCategory;

import java.util.List;

/**
 * Created by fangy on 2017/3/7.
 */

public class HomeCampainsAdapter extends RecyclerView.Adapter<HomeCampainsAdapter.ViewHolder> {
    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;

    private LayoutInflater mInflater;



    private List<HomeCategory> mDatas;

    public HomeCampainsAdapter(List<HomeCategory> datas){
        mDatas = datas;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {


        mInflater = LayoutInflater.from(viewGroup.getContext());
        if(type == VIEW_TYPE_R){

            return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,viewGroup,false));
        }

        return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        HomeCategory category = mDatas.get(i);
        viewHolder.textTitle.setText(category.getName());
        viewHolder.imageViewBig.setImageResource(category.getImgBig());
        viewHolder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
        viewHolder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {

        if(position % 2==0){
            return  VIEW_TYPE_R;
        }
        else return VIEW_TYPE_L;


    }

    static  class ViewHolder extends RecyclerView.ViewHolder{


        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
        }

    }
}