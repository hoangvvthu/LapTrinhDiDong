package com.example.baitap06;

import java.io.Serializable;

public class IconModel implements Serializable {
    private int imgId;
    private String desc;

    public IconModel(int imgId, String desc) {
        this.imgId = imgId;
        this.desc = desc;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
