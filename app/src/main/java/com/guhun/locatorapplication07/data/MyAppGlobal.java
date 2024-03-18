package com.guhun.locatorapplication07.data;

import android.app.Application;

import com.guhun.locatorapplication07.data.model.UserModel;

public class MyAppGlobal extends Application {

    private String serverUrl;
    private UserModel userModel;
    private String userId;
    private String right;
    private String imgUrl;
    public final int FINDSignalSize = 4; // 指纹匹配时采集尺寸
    public final int INSERTSignalSize = 10; // 指纹采集时上传尺寸
    public final int REFRESHTIME = 10; // 扫描时间间隔(秒)
    public final int ERROR_RATE = 10; // 误差率，单位%
    @Override
    public void onCreate()
    {
        super.onCreate();
        imgUrl = "https://wifilocation-1302313431.cos.ap-shanghai.myqcloud.com/"; // 图片服务器地址
//        serverUrl = "http://121.4.217.63:8080/locator_server"; // 服务器地址
//        serverUrl = "http://127.0.0.1:80/locator_server"; // 服务器地址
//        serverUrl = "http://192.168.128.1:8888/locator_server"; // 服务器地址
//        serverUrl = "http://192.168.169.1:8888/locator_server"; // 服务器地址
//        serverUrl = "http://172.24.128.1:8888/locator_server"; // 服务器地址
//        serverUrl = "http://10.17.4.154:8888/locator_server"; // 服务器地址
        serverUrl = "https://wifilocation-release.lecangs.com/locator_server"; // 服务器地址
    }
    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
