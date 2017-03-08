package com.example.fangyi.fyshop.bean.home;

/**
 * Created by fangy on 2017/3/7.
 */

public class BannerOnline {

    /**
     * id : 1
     * name : 音箱狂欢
     * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/5608f3b5Nc8d90151.jpg
     * type : 1
     */

    private int id;
    private String name;
    private String imgUrl;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
