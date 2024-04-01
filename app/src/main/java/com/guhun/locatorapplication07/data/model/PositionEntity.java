package com.guhun.locatorapplication07.data.model;


import lombok.Data;

public class PositionEntity {

    /**
     * AP点特征值
     */
    private String feature;

    /**
     * AP点位置 x,y,z
     */
    private double x;
    private double y;
    private double z;

    public PositionEntity(String feature, double x, double y, double z) {
        this.feature = feature;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
