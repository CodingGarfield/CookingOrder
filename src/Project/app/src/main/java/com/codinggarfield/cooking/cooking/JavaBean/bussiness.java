package com.codinggarfield.cooking.cooking.JavaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by IAMWicker on 2016/12/15.
 */

public class bussiness extends BmobObject {
    String name;
    String local;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
