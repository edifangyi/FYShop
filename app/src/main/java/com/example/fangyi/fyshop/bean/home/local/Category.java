package com.example.fangyi.fyshop.bean.home.local;

import com.example.fangyi.fyshop.bean.BaseBean;

/**
 * Created by Ivan on 15/9/24.
 */
public class Category extends BaseBean {


    public Category() {
    }

    public Category(String name) {

        this.name = name;
    }

    public Category(long id ,String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
