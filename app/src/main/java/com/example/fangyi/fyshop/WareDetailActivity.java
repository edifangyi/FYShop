package com.example.fangyi.fyshop;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.fangyi.fyshop.bean.hot.Wares;
import com.example.fangyi.fyshop.http.Contants;
import com.example.fangyi.fyshop.utils.CartProvider;
import com.example.fangyi.fyshop.utils.ToastUtils;
import com.example.fangyi.fyshop.widget.FYToolbar;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

public class WareDetailActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    FYToolbar mToolBar;
    @BindView(R.id.webView)
    WebView mWebView;

    private Wares mWares;
    private WebAppInterface mAppInterface;

    private CartProvider cartProvider;

    private SpotsDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ButterKnife.bind(this);

        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);

        if (serializable == null) {
            this.finish();
        } else {
            mWares = (Wares) serializable;
            cartProvider = new CartProvider(this);

            mDialog = new SpotsDialog(this, "loading···");
            mDialog.show();
        }

        ShareSDK.initSDK(this);

        initToolBar();
        initWebView();

    }


    private void initWebView() {

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//改成false ，才能显示图片
        settings.setAppCacheEnabled(true);//启动缓存

        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        mAppInterface = new WebAppInterface(this);

        /**
         window.appInterface.addToCart(id);
         */
        mWebView.addJavascriptInterface(mAppInterface, "appInterface");//名字一定要和页面中的保持一样

        mWebView.setWebViewClient(new Wc());//添加监听器

    }


    private void initToolBar() {


        mToolBar.setNavigationOnClickListener(this);
        mToolBar.setRightButtonText("分享");

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showShare();


            }
        });

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");


        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWares.getName());


        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.setImageUrl(mWares.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本" + mWares.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 监听器
     */
    class Wc extends WebViewClient {

        /*
            页面加载完
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            mAppInterface.showDetall();
        }
    }

    class WebAppInterface {

        private Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        /*
            function showDetail(id){


                    $("#txtWareId").val(id);

                    $.ajax({

                        url:"get?id="+id,
                        success:function(ware){


                            $("#ware_name").html(ware.name);
                            $("#ware_img").attr("src",ware.imgUrl);
                            $("#ware_price").html("￥ "+ware.price);
                            $("#ware_sale_quantity").html(ware.sale);

                        }


                    })
                }

         */

        @JavascriptInterface
        public void showDetall() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("Javascript:showDetail(" + mWares.getId() + ")");
                }
            });
        }

        /*
                $(function(){
                   $("#btnBuy").click(function(){

                       var id = $("#txtWareId").val();
                       window.appInterface.buy(id);

                   });



                    $("#btnAddToCart").click(function(){

                        var id = $("#txtWareId").val();
                        window.appInterface.addToCart(id);

                    });
                })

         */
        @JavascriptInterface
        public void buy(long id) {

        }

        @JavascriptInterface
        public void addToCart(long id) {
            cartProvider.put(mWares);
            ToastUtils.show(context, "已经添加到购物车");
        }

    }
}
