package com.example.fangyi.fyshop.adapter;

import android.content.Context;

import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.base.BaseViewHolder;
import com.example.fangyi.fyshop.adapter.base.SimpleAdapter;
import com.example.fangyi.fyshop.bean.category.CategoryList;

import java.util.List;

/**
 * Created by fangy on 2017/3/8.
 */

public class CategoryListAdapter extends SimpleAdapter<CategoryList> {

    public CategoryListAdapter(Context context, List<CategoryList> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, CategoryList item, int position) {
        viewHoder.getTextView(R.id.textView).setText(item.getName());
    }
}
