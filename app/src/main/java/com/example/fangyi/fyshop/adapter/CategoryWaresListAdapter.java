package com.example.fangyi.fyshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.base.BaseViewHolder;
import com.example.fangyi.fyshop.adapter.base.SimpleAdapter;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by fangy on 2017/3/8.
 */

public class CategoryWaresListAdapter extends SimpleAdapter<Wares> {

    public CategoryWaresListAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }



    @Override
    protected void convert(BaseViewHolder viewHoder, Wares item, int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
    }
}
