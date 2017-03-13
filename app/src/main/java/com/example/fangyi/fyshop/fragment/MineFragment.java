package com.example.fangyi.fyshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fangyi.fyshop.LoginActivity;
import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.login.User;
import com.example.fangyi.fyshop.http.Contants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment {


    @BindView(R.id.img_head)
    CircleImageView mImageHead;
    @BindView(R.id.txt_username)
    TextView mTxtUserName;
    @BindView(R.id.txt_my_orders)
    TextView mbtnLogout;
    @BindView(R.id.btn_logout)
    Button btnLogout;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void init() {

    }

    @OnClick(value = {R.id.img_head, R.id.txt_username})
    public void toLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @OnClick(R.id.btn_logout)
    public void logout(View view){

        FYApp.getInstance().clearUser();
        showUser(null);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        User user = FYApp.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user) {

        if (user != null) {

            if (!TextUtils.isEmpty(user.getLogo_url()))
                showHeadImage(user.getLogo_url());

            mTxtUserName.setText(user.getUsername());

            mbtnLogout.setVisibility(View.VISIBLE);
        } else {
            mTxtUserName.setText(R.string.to_login);
            mbtnLogout.setVisibility(View.GONE);
        }
    }


    private void showHeadImage(String url) {

        Picasso.with(getActivity()).load(url).into(mImageHead);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
