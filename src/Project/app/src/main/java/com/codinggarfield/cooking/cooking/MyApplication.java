package com.codinggarfield.cooking.cooking;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by IAMWicker on 2016/11/23.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //第一：默认初始化
        Bmob.initialize(this, "0feef3f477ca3be89237354a758c65f6");

    }
}
