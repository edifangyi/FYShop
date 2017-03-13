package com.example.fangyi.fyshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.fangyi.fyshop.MainActivity;
import com.example.fangyi.fyshop.NewOrderActivity;
import com.example.fangyi.fyshop.R;
import com.example.fangyi.fyshop.adapter.CartAdapter;
import com.example.fangyi.fyshop.bean.cart.ShoppingCart;
import com.example.fangyi.fyshop.utils.CartProvider;
import com.example.fangyi.fyshop.view.DividerItemDecoration;
import com.example.fangyi.fyshop.widget.FYToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.fangyi.fyshop.R.id.btn_del;

public class CartFragment extends BaseFragment implements View.OnClickListener {

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkbox_all)
    CheckBox mCheckBox;
    @BindView(R.id.txt_total)
    TextView mTextTotal;
    @BindView(R.id.btn_order)
    Button mBtnOrder;
    @BindView(btn_del)
    Button mBtnDel;


    private CartAdapter mAdapter;
    private CartProvider cartProvider;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        cartProvider = new CartProvider(getContext());
        initView();
        showData();
        return view;
    }

    @Override
    public void init() {

    }


    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();
        setCheckList(carts);
        mAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setCheckList(List<ShoppingCart> carts) {
        if (carts.size() == 0) {
            mCheckBox.setChecked(false);
        } else {
            mCheckBox.setChecked(true);
        }
    }

    /**
     * 重新加载数据
     */
    public void refData() {
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        setCheckList(carts);
        hideDelControl();
        mAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.showTotalPrice();
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mBtnOrder.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
    }

    private FYToolbar mToolbar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;

            mToolbar = (FYToolbar) mainActivity.findViewById(R.id.tooblbar);

//            mToolbar.hideSearchView();
//            mToolbar.setTitle(R.string.cart);
//            mToolbar.getRightButton().setText("编辑");
//            mToolbar.getRightButton().setTextColor(getResources().getColor(R.color.white));
//            mToolbar.getRightButton().setVisibility(View.VISIBLE);


            mToolbar.getRightButton().setOnClickListener(this);
            mToolbar.getRightButton().setTag(ACTION_EDIT);


        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_order:
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent,true);
                break;
            case R.id.btn_del:
                mAdapter.delCart();
                break;
            default:
                setToolbarRightButton(v);
                break;
        }
    }

    private void setToolbarRightButton(View v) {
        int action = (int) v.getTag();
        if (ACTION_EDIT == action) {

            showDelControl();
        } else if (ACTION_CAMPLATE == action) {

            hideDelControl();
        }
    }

    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);//v.getTag()

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    private void hideDelControl() {

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);//v.getTag()

        mAdapter.checkAll_None(true);
        mCheckBox.setChecked(true);
        mAdapter.showTotalPrice();
    }

}
