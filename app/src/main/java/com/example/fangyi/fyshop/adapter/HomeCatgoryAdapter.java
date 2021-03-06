package com.example.fangyi.fyshop.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.bean.home.Campaign;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fangy on 2017/3/7.
 */

public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder> {
    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    private LayoutInflater mInflater;


    private List<Campaign> mDatas;
    private Context mContext;


    private OnCampaignClickListener mListener;

    public HomeCatgoryAdapter(List<Campaign> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }


    public void setOnCampaignClickListener(OnCampaignClickListener listener) {
        this.mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {


        mInflater = LayoutInflater.from(viewGroup.getContext());
        if (type == VIEW_TYPE_R) {

            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2, viewGroup, false));
        }

        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        Campaign campaign = mDatas.get(i);
        viewHolder.textTitle.setText(campaign.getTitle());

        Picasso.with(mContext).load(campaign.getCpOne().getImgUrl()).into(viewHolder.imageViewBig);
        Picasso.with(mContext).load(campaign.getCpTwo().getImgUrl()).into(viewHolder.imageViewSmallTop);
        Picasso.with(mContext).load(campaign.getCpThree().getImgUrl()).into(viewHolder.imageViewSmallBottom);


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else return VIEW_TYPE_L;


    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }

        @Override
        public void onClick(final View v) {


            final Campaign campaign = mDatas.get(getLayoutPosition());
            if (mListener != null) {

                anim(v, campaign);


            }
        }

        private void anim(final View v, final Campaign campaign) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {


                    switch (v.getId()) {
                        case R.id.imgview_big:
                            mListener.onClick(v, campaign.getCpOne());
                            break;
                        case R.id.imgview_small_top:
                            mListener.onClick(v, campaign.getCpTwo());
                            break;
                        case R.id.imgview_small_bottom:
                            mListener.onClick(v, campaign.getCpThree());
                            break;
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }


            });

            animator.start();
        }
    }


    public interface OnCampaignClickListener {
        void onClick(View view, Campaign.CpOneBean campaign);

        void onClick(View view, Campaign.CpTwoBean campaign);

        void onClick(View view, Campaign.CpThreeBean campaign);
    }
}