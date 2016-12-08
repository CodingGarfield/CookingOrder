package com.codinggarfield.cooking.cooking.JavaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by IAMWicker on 2016/11/30.
 */

public class goods extends BmobObject {
    String goodname;
    String goodprice;
    String goodintrodution;
    int surplusgoods;
    String goodImageUrl;

    public int getSurplusgoods() {
        return surplusgoods;
    }

    public void setSurplusgoods(int surplusgoods) {
        this.surplusgoods = surplusgoods;
    }

    public String getGoodImageUrl() {
        return goodImageUrl;
    }

    public void setGoodImageUrl(String goodImageUrl) {
        this.goodImageUrl = goodImageUrl;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getGoodprice() {
        return goodprice;
    }

    public void setGoodprice(String goodprice) {
        this.goodprice = goodprice;
    }

    public String getGoodintrodution() {
        return goodintrodution;
    }

    public void setGoodintrodution(String goodintrodution) {
        this.goodintrodution = goodintrodution;
    }
}
