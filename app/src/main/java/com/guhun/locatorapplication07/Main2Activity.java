package com.guhun.locatorapplication07;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class Main2Activity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private Button scanWifiButton;

    private AppBarConfiguration mAppBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // 初始化导航栏
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // 初始化头部
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.loginFragment,R.id.nav_user, R.id.nav_site)
                .setDrawerLayout(drawer)
                .build();
        // 初始化导航界面
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // 界面和头部绑定
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        // 导航栏和界面绑定
        NavigationUI.setupWithNavController(navigationView, navController);

        Toast.makeText(getApplicationContext(),"暂未登录",Toast.LENGTH_SHORT).show();

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);

        // 初始化按钮
        scanWifiButton = findViewById(R.id.scan_wifi_button);
        scanWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查权限
                if (checkPermissions()) {
                    // 权限已授予，开始获取 WiFi 信息
                    getWifiInfo();
                } else {
                    // 请求权限
                    requestPermissions();
                }
            }
        });
    }

    // 检查权限
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    // 请求权限
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, PERMISSIONS_REQUEST_CODE);
    }

    // 获取 WiFi 信息
    private void getWifiInfo() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            String ssid = wifiInfo.getSSID();
            int signalStrength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
            // 在这里将 WiFi 信息存入数据库或进行其他操作
            Toast.makeText(getApplicationContext(), "WiFi SSID: " + ssid + ", Signal Strength: " + signalStrength, Toast.LENGTH_SHORT).show();
        }
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，开始获取 WiFi 信息
                getWifiInfo();
            } else {
                // 用户拒绝了权限请求，提示用户并进行适当处理
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
