package com.example.fangyi.fyshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.base.BaseViewHolder;
import com.example.fangyi.fyshop.adapter.base.SimpleAdapter;
import com.example.fangyi.fyshop.bean.cart.ShoppingCart;
import com.example.fangyi.fyshop.bean.hot.Wares;
import com.example.fangyi.fyshop.utils.CartProvider;
import com.example.fangyi.fyshop.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by fangy on 2017/3/8.
 */

public class HotWares2Adapter extends SimpleAdapter<Wares> {

    CartProvider provider;

    public HotWares2Adapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
        provider = new CartProvider(context);
    }


    @Override
    protected void convert(BaseViewHolder viewHoder, final Wares wares, final int position) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHoder.getTextView(R.id.text_title).setText(wares.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());
        Button button =viewHoder.getButtonView(R.id.btn_buy);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    provider.put(convertData(wares));
                    ToastUtils.show(context, "已添加到购物车");

                }
            });

        }

    }

    public ShoppingCart convertData(Wares item) {

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

    public void resetLayout(int layoutId) {


        this.layoutResId = layoutId;

        notifyItemRangeChanged(0, getDatas().size());


    }

}
