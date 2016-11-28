package com.codinggarfield.cooking.cooking.JavaBean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by IAMWicker on 2016/11/25.
 */

public class MyUser extends BmobUser {
    private BmobFile nick;
    private String usertype;
    private Integer age;

    public BmobFile getNick() {
        return nick;
    }

    public void setNick(BmobFile nick) {
        this.nick = nick;
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
