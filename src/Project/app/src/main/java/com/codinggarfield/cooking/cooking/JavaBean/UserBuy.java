package com.codinggarfield.cooking.cooking.JavaBean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by IAMWicker on 2016/12/2.
 */

@Table(name="user_buy")
public class UserBuy extends BmobObject{
    @Id(column="ordercode")
    String Username;
    String ordercode;
    String Useraddress;
    Date sendfoodTime;
    int orderprice;
    List<String> foodlist;
    List<String> foodlistUrl;

    public List<String> getFoodlistUrl() {
        return foodlistUrl;
    }

    public void setFoodlistUrl(List<String> foodlistUrl) {
        this.foodlistUrl = foodlistUrl;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getUseraddress() {
        return Useraddress;
    }

    public void setUseraddress(String useraddress) {
        Useraddress = useraddress;
    }

    public Date getSendfoodTime() {
        return sendfoodTime;
    }

    public void setSendfoodTime(Date sendfoodTime) {
        this.sendfoodTime = sendfoodTime;
    }

    public int getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(int orderprice) {
        this.orderprice = orderprice;
    }

    public List<String> getFoodlist() {
        return foodlist;
    }

    public void setFoodlist(List<String> foodlist) {
        this.foodlist = foodlist;
    }
}
