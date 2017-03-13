package com.example.fangyi.fyshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fangyi.fyshop.LoginActivity;
import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.login.User;

/**
 * Created by fangy on 2017/3/11.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater,container,savedInstanceState);

        initToolBar();
        init();

        return view;

    }


    public void  initToolBar(){

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();


    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = FYApp.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                FYApp.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }


}
