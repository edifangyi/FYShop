package com.example.fangyi.fyshop;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.paysdk.ui.PayResultActivity;
import com.example.fangyi.fyshop.adapter.WareOrderAdapter;
import com.example.fangyi.fyshop.application.FYApp;
import com.example.fangyi.fyshop.bean.Charge;
import com.example.fangyi.fyshop.bean.cart.ShoppingCart;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.http.OkHttpHelper;
import com.example.fangyi.fyshop.http.SpotsCallBack;
import com.example.fangyi.fyshop.msg.BaseRespMsg;
import com.example.fangyi.fyshop.msg.CreateOrderRespMsg;
import com.example.fangyi.fyshop.utils.CartProvider;
import com.example.fangyi.fyshop.utils.JSONUtil;
import com.example.fangyi.fyshop.view.layoutmanager.FullyLinearLayoutManager;
import com.example.fangyi.fyshop.widget.FYToolbar;
import com.pingplusplus.android.PaymentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;


public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {


    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
    @BindView(R.id.toolbar)
    FYToolbar toolbar;
    @BindView(R.id.rl_addr)
    RelativeLayout rlAddr;
    @BindView(R.id.txt_order)
    TextView txtOrder;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_items)
    LinearLayout llItems;
    @BindView(R.id.rb_alipay)
    RadioButton mRbAlipay;
    @BindView(R.id.rl_alipay)
    RelativeLayout mLayoutAlipay;
    @BindView(R.id.rb_webchat)
    RadioButton mRbWechat;
    @BindView(R.id.rl_wechat)
    RelativeLayout mLayoutWechat;
    @BindView(R.id.icon_bd)
    ImageView iconBd;
    @BindView(R.id.rb_bd)
    RadioButton mRbBd;
    @BindView(R.id.rl_bd)
    RelativeLayout mLayoutBd;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.txt_total)
    TextView mTxtTotal;
    @BindView(R.id.btn_createOrder)
    Button mBtnCreateOrder;


    private CartProvider cartProvider;

    private WareOrderAdapter mAdapter;


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private String orderNum;
    private String payChannel = CHANNEL_ALIPAY;
    private float amount;


    private HashMap<String, RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ButterKnife.bind(this);


        showData();

        init();

    }


    private void init() {


        channels.put(CHANNEL_ALIPAY, mRbAlipay);
        channels.put(CHANNEL_WECHAT, mRbWechat);
        channels.put(CHANNEL_BFB, mRbBd);

        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);


        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款： ￥" + amount);
    }


    public void showData() {

        cartProvider = new CartProvider(this);
        mAdapter = new WareOrderAdapter(this, cartProvider.getAll());

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {

        selectPayChannle(v.getTag().toString());
    }


    public void selectPayChannle(String paychannel) {


        for (Map.Entry<String, RadioButton> entry : channels.entrySet()) {

            payChannel = paychannel;
            RadioButton rb = entry.getValue();
            if (entry.getKey().equals(paychannel)) {

                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);

            } else
                rb.setChecked(false);
        }


    }


    @OnClick(R.id.btn_createOrder)
    public void createNewOrder(View view) {

        postNewOrder();
    }


    private void postNewOrder() {


        final List<ShoppingCart> carts = mAdapter.getDatas();

        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c : carts) {

            WareItem item = new WareItem(c.getId(), c.getPrice().intValue());
            items.add(item);

        }

        String item_json = JSONUtil.toJSON(items);

        Map<String, Object> params = new HashMap<>(5);
        params.put("user_id", FYApp.getInstance().getUser().getId() + "");
        params.put("item_json", item_json);
        params.put("pay_channel", payChannel);
        params.put("amount", (int) amount + "");
        params.put("addr_id", 1 + "");


        mBtnCreateOrder.setEnabled(false);

        okHttpHelper.post(Contants.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {


//                cartProvider.

                mBtnCreateOrder.setEnabled(true);
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();

                openPaymentActivity(JSONUtil.toJSON(charge));

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnCreateOrder.setEnabled(true);
            }
        });


    }


    private void openPaymentActivity(String charge) {

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Contants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                else
                    changeOrderStatus(0);

            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            }
        }
    }


    private void changeOrderStatus(final int status) {

        Map<String, Object> params = new HashMap<>(5);
        params.put("order_num", orderNum);
        params.put("status", status + "");


        okHttpHelper.post(Contants.API.ORDER_COMPLEPE, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {

                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);
            }
        });

    }


    private void toPayResultActivity(int status) {

        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status", status);

        startActivity(intent);
        this.finish();

    }


    class WareItem {
        private Long ware_id;
        private int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


}
