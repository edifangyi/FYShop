package com.example.fangyi.fyshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.login.User;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.http.OkHttpHelper;
import com.example.fangyi.fyshop.http.SpotsCallBack;
import com.example.fangyi.fyshop.msg.LoginRespMsg;
import com.example.fangyi.fyshop.utils.DESUtil;
import com.example.fangyi.fyshop.utils.ToastUtils;
import com.example.fangyi.fyshop.widget.FYEditText;
import com.example.fangyi.fyshop.widget.FYToolbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;


public class LoginActivity extends BaseActivity {


    @BindView(R.id.etxt_phone)
    FYEditText mEtxtPhone;
    @BindView(R.id.etxt_pwd)
    FYEditText mEtxtPwd;
    @BindView(R.id.toolbar)
    FYToolbar mToolBar;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        initToolBar();
    }


    private void initToolBar() {


        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @OnClick(R.id.txt_toReg)
    public void toReg(){
        Intent intent = new Intent(this,RegActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void login(View view) {


        String phone = mEtxtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }


        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));//对称加密

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {


                FYApp application = FYApp.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if (application.getIntent() == null) {
                    setResult(RESULT_OK);
                    finish();
                } else {

                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();

                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }


}
