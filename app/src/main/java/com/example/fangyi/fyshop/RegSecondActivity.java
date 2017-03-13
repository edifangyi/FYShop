package com.example.fangyi.fyshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.login.User;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.http.OkHttpHelper;
import com.example.fangyi.fyshop.http.SpotsCallBack;
import com.example.fangyi.fyshop.msg.LoginRespMsg;
import com.example.fangyi.fyshop.utils.CountTimerView;
import com.example.fangyi.fyshop.utils.DESUtil;
import com.example.fangyi.fyshop.utils.ManifestUtil;
import com.example.fangyi.fyshop.utils.ToastUtils;
import com.example.fangyi.fyshop.widget.FYEditText;
import com.example.fangyi.fyshop.widget.FYToolbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

public class RegSecondActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    FYToolbar mToolBar;
    @BindView(R.id.txtTip)
    TextView mTxtTip;
    @BindView(R.id.edittxt_code)
    FYEditText mEtCode;
    @BindView(R.id.btn_reSend)
    Button mBtnResend;



    private String phone;
    private String pwd;
    private String countryCode;


    private CountTimerView countTimerView;


    private SpotsDialog dialog;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private SMSEvenHanlder evenHanlder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ButterKnife.bind(this);


        initToolBar();

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);


        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));


        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();



        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"));

        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this, "正在校验验证码");


    }


    private void initToolBar() {

        mToolBar.setRightButtonText("完成");

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submitCode();

            }
        });


    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view) {

        SMSSDK.getVerificationCode("+" + countryCode, phone);
        countTimerView = new CountTimerView(mBtnResend, R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }

    /**
     * 分割电话号码
     */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }


    private void submitCode() {

        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        dialog.show();
    }


    private void doReg() {

        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        okHttpHelper.post(Contants.API.REG, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    ToastUtils.show(RegSecondActivity.this, "注册失败:" + userLoginRespMsg.getMessage());
                    return;
                }
                FYApp application = FYApp.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this, MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);


            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    class SMSEvenHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {


//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");

//                            ToastUtils.show(RegSecondActivity.this,"验证成功："+phone+",country:"+country);


                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                        }
                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }


}
