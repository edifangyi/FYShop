package com.example.fangyi.fyshop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fangyi.fyshop.R;

/**
 * Created by fangy on 2017/3/6.
 */

public class FYToolbar extends Toolbar {

    private LayoutInflater mInflater;
    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;


    public FYToolbar(Context context) {
        this(context, null);
    }

    public FYToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FYToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10, 10);//设置边距

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.FYToolbar, defStyleAttr, 0);


            final Drawable rightIcon = a.getDrawable(R.styleable.FYToolbar_rightButtnIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }


            boolean isShowSearchView = a.getBoolean(R.styleable.FYToolbar_isShowSearchView, false);
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
            }


            String isChangeHint = a.getString(R.styleable.FYToolbar_isChangeHint);
            if (!TextUtils.isEmpty(isChangeHint)) {
                mSearchView.setHint(isChangeHint);
            }


            String isChangeText = a.getString(R.styleable.FYToolbar_isChangeText);
            if (!TextUtils.isEmpty(isChangeText)) {
                mSearchView.setText(isChangeText);
            }

            a.recycle();

        }


    }


    public void setShowSearchView(boolean isShowSearchView) {

        if (isShowSearchView) {
            showSearchView();
            hideTitleView();
        } else {
            showTitleView();
            hideSearchView();
        }
    }



    public void setRightButtonIcon(Drawable icon) {

        if (mRightButton != null) {
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    public void setChangeHint(String isChangeHint) {
        mSearchView.setHint(isChangeHint);
    }

    public void setChangeText(String isChangeText) {
        mSearchView.setText(isChangeText);
    }


    public void setRightButtonOnClickListener(OnClickListener li) {
        mRightButton.setOnClickListener(li);
    }

    public Button getRightButton(){

        return this.mRightButton;
    }


    private void initView() {

        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);


            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            //长、宽、对齐方式
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);

        }


    }


    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }


    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView() {

        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);

    }


    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }

}
