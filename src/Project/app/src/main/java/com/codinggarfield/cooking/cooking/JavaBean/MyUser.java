package com.codinggarfield.cooking.cooking.JavaBean;

import cn.bmob.v3.BmobUser;

/**
 * Created by IAMWicker on 2016/11/25.
 */

public class MyUser extends BmobUser {
    private String nickurl;
    private String usertype;
    private Integer age;

    public String getNickurl() {
        return nickurl;
    }

    public void setNickurl(String nickurl) {
        this.nickurl = nickurl;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
