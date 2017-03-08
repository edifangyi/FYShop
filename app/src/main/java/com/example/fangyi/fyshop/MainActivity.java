package com.example.fangyi.fyshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.fangyi.fyshop.bean.tab.Tab;
import com.example.fangyi.fyshop.fragment.CartFragment;
import com.example.fangyi.fyshop.fragment.CategoryFragment;
import com.example.fangyi.fyshop.fragment.HomeFragment;
import com.example.fangyi.fyshop.fragment.HotFragment;
import com.example.fangyi.fyshop.fragment.MineFragment;
import com.example.fangyi.fyshop.widget.FYToolbar;
import com.example.fangyi.fyshop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater mInflater;
    private FragmentTabHost mTabhost;
    private List<Tab> mTabs = new ArrayList<>();

    private FYToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


//        mInflater = LayoutInflater.from(this);
//        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//        TabHost.TabSpec tabSpec = mTabhost.newTabSpec("home");
//        View view = mInflater.inflate(R.layout.tab_indicator, null);
//        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
//        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
//        img.setBackgroundResource(R.mipmap.icon_home);
//        text.setText("主页");
//        tabSpec.setIndicator(view);
//        mTabhost.addTab(tabSpec, HomeFragment.class, null);
//        mTabhost.addTab(mTabhost.newTabSpec("home").setIndicator());


        initTab();
        initToolbr();
    }

    private void initToolbr() {
        mToolbar = (FYToolbar) findViewById(R.id.tooblbar);

    }

    private void initTab() {
        Tab tab_home = new Tab(HomeFragment.class, R.string.home, R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotFragment.class, R.string.hot, R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class, R.string.catagory, R.drawable.selector_icon_category);
        Tab tab_cart = new Tab(CartFragment.class, R.string.cart, R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class, R.string.mine, R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(), null);

        }
        //去掉分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //默认第一个
        mTabhost.setCurrentTab(0);


    }

    private View buildIndicator(Tab tab) {

        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }


}
