package com.guhun.locatorapplication07.data.model;


import lombok.Builder;
import lombok.Data;

public class WifiDbmModel {

    private Long id;

    private Integer mapId;
    private Integer siteId;
    private Double positionx;
    private Double positiony;
    private Integer linex;
    private Integer liney;
    private String wifiName;
    private Double wifiDbm;
    private String macAddress;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Double getPositionx() {
        return positionx;
    }

    public void setPositionx(Double positionx) {
        this.positionx = positionx;
    }

    public Double getPositiony() {
        return positiony;
    }

    public void setPositiony(Double positiony) {
        this.positiony = positiony;
    }

    public Integer getLinex() {
        return linex;
    }

    public void setLinex(Integer linex) {
        this.linex = linex;
    }

    public Integer getLiney() {
        return liney;
    }

    public void setLiney(Integer liney) {
        this.liney = liney;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public Double getWifiDbm() {
        return wifiDbm;
    }

    public void setWifiDbm(Double wifiDbm) {
        this.wifiDbm = wifiDbm;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }
}
