package com.guhun.locatorapplication07.data.model;

import java.util.Date;

public class UserPositionModel {

    private int id;
    private String userId;
    private String userName;
    private int siteId;
    private int locationLinex;
    private int locationLiney;
    private Double locationPositionx;
    private Double locationPositiony;
    private String updateTime;

    private int mapId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getLocationLinex() {
        return locationLinex;
    }

    public void setLocationLinex(int locationLinex) {
        this.locationLinex = locationLinex;
    }

    public int getLocationLiney() {
        return locationLiney;
    }

    public void setLocationLiney(int locationLiney) {
        this.locationLiney = locationLiney;
    }

    public Double getLocationPositionx() {
        return locationPositionx;
    }

    public void setLocationPositionx(Double locationPositionx) {
        this.locationPositionx = locationPositionx;
    }

    public Double getLocationPositiony() {
        return locationPositiony;
    }

    public void setLocationPositiony(Double locationPositiony) {
        this.locationPositiony = locationPositiony;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
