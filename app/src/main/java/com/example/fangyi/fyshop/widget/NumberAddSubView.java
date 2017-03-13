package com.example.fangyi.fyshop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fangyi.fyshop.R;

/**
 * Created by fangy on 2017/3/9.
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {


    public static final int DEFUALT_MAX = 1000;

    private LayoutInflater mInflater;
    private Button subButton;
    private Button addButton;
    private TextView mEtxtNum;

    private int value;
    private int minValue;
    private int maxValue = DEFUALT_MAX;

    private OnButtonClickListener onButtonClickListener;
    private Drawable editTextBackground;
    private Drawable buttonAddBackgroud;
    private Drawable buttonSubBackgroud;


    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);

        initView();

        getAttrs(context, attrs);

    }

    private void getAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TintTypedArray typedArray = TintTypedArray
                    .obtainStyledAttributes(context, attrs, R.styleable.NumberAddSubView);

            int val = typedArray.getInt(R.styleable.NumberAddSubView_value, 0);
            setValue(val);

            int maxVal = typedArray.getInt(R.styleable.NumberAddSubView_maxValue, 0);
            if (maxVal != 0)
                setMaxValue(maxVal);

            int minVal = typedArray.getInt(R.styleable.NumberAddSubView_minValue, 0);
            setMinValue(minVal);

            Drawable etBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_editBackground);
            if (etBackground != null)
                setEditTextBackground(etBackground);


            Drawable buttonAddBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_buttonAddBackgroud);
            if (buttonAddBackground != null)
                setButtonAddBackgroud(buttonAddBackground);

            Drawable buttonSubBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_buttonSubBackgroud);
            if (buttonSubBackground != null)
                setButtonSubBackgroud(buttonSubBackground);
        }
    }

    private void initView() {

        View view = mInflater.inflate(R.layout.widet_num_add_sub, this, true);
        subButton = (Button) view.findViewById(R.id.btn_sub);
        addButton = (Button) view.findViewById(R.id.btn_add);
        mEtxtNum = (TextView) view.findViewById(R.id.etxt_num);


        subButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            numAdd();
            if (onButtonClickListener != null) {
                onButtonClickListener.onButtonAddClick(v, this.value);
            }
        } else if (v.getId() == R.id.btn_sub) {
            numSub();
            if (onButtonClickListener != null) {
                onButtonClickListener.onButtonSubClick(v, this.value);
            }

        }
    }


    private void numAdd() {

        getValue();

        if (this.value < maxValue)
            this.value = +this.value + 1;

        mEtxtNum.setText(value + "");
    }

    private void numSub() {

        getValue();

        if (this.value > minValue)
            this.value = this.value - 1;

        mEtxtNum.setText(value + "");
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public void setEditTextBackground(Drawable editTextBackground) {
        this.editTextBackground = editTextBackground;
    }

    public void setButtonAddBackgroud(Drawable buttonAddBackgroud) {
        this.buttonAddBackgroud = buttonAddBackgroud;
    }

    public void setButtonSubBackgroud(Drawable buttonSubBackgroud) {
        this.buttonSubBackgroud = buttonSubBackgroud;
    }

    public interface OnButtonClickListener {

        public void onButtonAddClick(View view, int value);

        public void onButtonSubClick(View view, int value);

    }

    public int getValue() {

        String value = mEtxtNum.getText().toString();

        if (!TextUtils.isEmpty(value))
            this.value = Integer.parseInt(value);

        return this.value;
    }

    public void setValue(int value) {
        mEtxtNum.setText(value+"");
        this.value = value;
    }
    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
