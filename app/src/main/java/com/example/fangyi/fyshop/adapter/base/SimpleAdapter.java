package com.example.fangyi.fyshop.adapter.base;

import android.content.Context;

import java.util.List;

/**
 * Created by fangy on 2017/3/8.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }



}
