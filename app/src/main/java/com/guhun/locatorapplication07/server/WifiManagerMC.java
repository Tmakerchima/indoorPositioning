package com.guhun.locatorapplication07.server;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.guhun.locatorapplication07.data.model.WifiSignalModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WifiManagerMC extends ContextWrapper {
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private boolean isScanning = false;

    private ArrayList<WifiSignalModel> signalList; //信号列表


    public WifiManagerMC(Context context) {
        super(context);
        // 初始化Wi-Fi管理器
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // 创建一个ScanSettings对象，用于配置扫描选项
        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // 设置为低延迟模式
                .build();

        // 检查是否有足够的权限进行Wi-Fi扫描
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，请求权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // 开始第一次扫描
        startHighFrequencyScan();
    }

    // 方法用于启动高频率扫描
    public void startHighFrequencyScan() {

        if (!isScanning) {
            isScanning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isScanning) {
                        wifiManager.startScan(); // 启动扫描，这里不需要传入ScanSettings，因为默认就是低延迟模式
                        try {
                            Thread.sleep(1000); // 等待1秒（或其他您希望的间隔时间）
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 获取并处理扫描结果
                        wifiList = wifiManager.getScanResults();
                        int index = 1;
                        for (ScanResult item : wifiList){
                            WifiSignalModel wifiSignal =
                                    new WifiSignalModel(index,0,item.SSID,item.BSSID,item.level);
                            index++;
                            // 只存前num个
                            if(signalList.size() < 50){
                                signalList.add(wifiSignal);
                            }else {
                                break;
                            }
                        }
                    }
                }
            }).start();
        }
    }

    // 停止高频率扫描的方法
    public void stopHighFrequencyScan() {
        isScanning = false;
    }

    // 获取最新的Wi-Fi扫描结果
    public List<ScanResult> getWifiScanResults() {
        return wifiList;
    }
    // 获取信号列表信息
    public ArrayList<WifiSignalModel> getSignalList() {
        return signalList;
    }



//    public ArrayList<WifiSignalModel> convertToWifiSignalModel(int size, int wifiId,List<ScanResult> wifiList){
//        ArrayList<WifiSignalModel> signalList = new ArrayList<>();
//        int index = 1;
//        for (ScanResult item : wifiList){
//            WifiSignalModel wifiSignal =
//                    new WifiSignalModel(index,wifiId,item.SSID,item.BSSID,item.level);
//            index++;
//            // 只存前num个
//            if(signalList.size() < size){
//                signalList.add(wifiSignal);
//            }else {
//                break;
//            }
//        }
//        // 进行筛选 测试用途
//        Iterator<WifiSignalModel> iterator = signalList.iterator();
//        while (iterator.hasNext()) {
//            WifiSignalModel wifiSignalModel = iterator.next();
//            String mac = wifiSignalModel.getSignalMac();
//            if(!mac.equals("48:57:02:18:df:42")&&!mac.equals("48:57:02:18:df:43")&&!mac
//                    .equals("48:57:02:18:df:44")){
//                iterator.remove();
//            }
//        }
//        return signalList;
//    }
}
