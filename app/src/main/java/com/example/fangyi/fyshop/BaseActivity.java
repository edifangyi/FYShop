package com.example.fangyi.fyshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.login.User;

/**
 * Created by fangy on 2017/3/11.
 */

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = FYApp.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                FYApp.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(intent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }
}
