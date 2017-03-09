package com.example.fangyi.fyshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.base.BaseViewHolder;
import com.example.fangyi.fyshop.adapter.base.SimpleAdapter;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by fangy on 2017/3/8.
 */

public class HotWares2Adapter extends SimpleAdapter<Wares> {

    public HotWares2Adapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
    }


    @Override
    protected void convert(BaseViewHolder viewHoder, Wares wares, final int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHoder.getTextView(R.id.text_title).setText(wares.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());
        viewHoder.getTextView(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "position button:" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
