package com.guhun.locatorapplication07.data.model;

import com.alibaba.fastjson.annotation.JSONField;

public class PositionModel {
    @JSONField(name = "positionx")
    private String positionx;
    @JSONField(name = "positiony")
    private String positiony;
    @JSONField(name = "linex")
    private String linex;
    @JSONField(name = "liney")
    private String liney;


    public String getPositionx() {
        return positionx;
    }

    public void setPositionx(String positionx) {
        this.positionx = positionx;
    }

    public String getPositiony() {
        return positiony;
    }

    public void setPositiony(String positiony) {
        this.positiony = positiony;
    }

    public String getLinex() {
        return linex;
    }

    public void setLinex(String linex) {
        this.linex = linex;
    }

    public String getLiney() {
        return liney;
    }

    public void setLiney(String liney) {
        this.liney = liney;
    }
}
