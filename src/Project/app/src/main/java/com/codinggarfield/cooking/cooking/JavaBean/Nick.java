package com.codinggarfield.cooking.cooking.JavaBean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by IAMWicker on 2016/11/26.
 */

public class Nick {
    private BmobFile file;
    private String name;
    private String width;
    private String high;
    private String filesize;

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }
}
