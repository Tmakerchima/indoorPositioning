package com.guhun.locatorapplication07.server;

import static java.security.AccessController.getContext;

import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.EditText;

import com.guhun.locatorapplication07.data.model.PositionEntity;
import com.guhun.locatorapplication07.data.model.WifiDbmModel;
import com.guhun.locatorapplication07.data.model.WifiSignalModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WifiManagerGH {
//    private WifiManager wifiManager;    // manager对象
//    private WifiInfo wifiInfo;          // wifi信息
    private List<ScanResult> wifiList;  // 网络列表，使用时用signalList代替
    private ArrayList<WifiSignalModel> signalList; //信号列表
    private final double THRESHOLD = -5;

    public WifiManagerGH(Context context){
        // 获取 WifiManager 实例
        // 初始化信号信息
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.getConnectionInfo();
        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
    }

    /**
    * @Description: 初始化信号列表
    * @Param: [size]列表大小
    * @return: java.util.ArrayList<com.guhun.locatorapplication07.data.model.WifiSignalModel>
    * @Author: GuHun
    * @Date: 2021/3/14
    */
    public ArrayList<WifiSignalModel> initSignalList(int size, int wifiId) {
        signalList = new ArrayList<>();
        int index = 1;
        for (ScanResult item : wifiList){
            WifiSignalModel wifiSignal =
                    new WifiSignalModel(index,wifiId,item.SSID,item.BSSID,item.level);
            index++;
            // 只存前num个
            if(signalList.size() < size){
                signalList.add(wifiSignal);
            }else {
                break;
            }
        }
        return signalList;

    }

    /**
     * 梯度判断是否可以放入参数集
     * @return
     */
    public ArrayList<WifiSignalModel> stHandle(ArrayList<WifiSignalModel> sSignalList, List<PositionEntity> list) {
        // 梯度筛选
        Iterator<WifiSignalModel> iterator = sSignalList.iterator();

        // 由于只是测试所以传入坐标点为(1,1,1)
        int signalX = 1;
        int signalY = 1;
        int signalZ = 1;
        double distance;

        while (iterator.hasNext()) {
            WifiSignalModel wifiDbmModel = iterator.next();
            String macAddress = wifiDbmModel.getSignalMac();
            String[] parts = macAddress.split(":");
            String secondLastPart = null;

            if (parts.length >= 2) {
                secondLastPart = parts[parts.length - 2];
            }
            for(PositionEntity entity:list){
                if(entity.getFeature().equals(secondLastPart)){
                    // 计算欧几里得距离
                    distance = Math.sqrt(Math.pow(entity.getX() - signalX, 2) +
                            Math.pow(entity.getY() - signalY, 2) +
                            Math.pow(entity.getZ() - signalZ, 2));
                    // n代表电磁波消耗率
                    double n = 3.5;
                    //斜率
                    double px = -10 * (n / distance) / Math.log(10);
                    if(px>THRESHOLD){
                        iterator.remove();
                    }
                }
            }
        }
        signalList = sSignalList;
        return signalList;
    }

    public ArrayList<WifiSignalModel> stHandle(ArrayList<WifiSignalModel> sSignalList, List<PositionEntity> list, int x,int y) {
        // 梯度筛选
        Iterator<WifiSignalModel> iterator = sSignalList.iterator();

        // 由于只是测试所以传入坐标点为(1,1,1)
        int signalX = x;
        int signalY = y;
        int signalZ = 1;
        double distance;

        while (iterator.hasNext()) {
            WifiSignalModel wifiDbmModel = iterator.next();
            String macAddress = wifiDbmModel.getSignalMac();
            String[] parts = macAddress.split(":");
            String secondLastPart = null;

            if (parts.length >= 2) {
                secondLastPart = parts[parts.length - 2];
            }
            for(PositionEntity entity:list){
                if(entity.getFeature().equals(secondLastPart)){
                    // 计算欧几里得距离
                    distance = Math.sqrt(Math.pow(entity.getX() - signalX, 2) +
                            Math.pow(entity.getY() - signalY, 2) +
                            Math.pow(entity.getZ() - signalZ, 2));
                    // n代表电磁波消耗率
                    double n = 3.5;
                    //斜率
                    double px = -10 * (n / distance) / Math.log(10);
                    if(px>THRESHOLD){
                        iterator.remove();
                    }
                }
            }
        }
        signalList = sSignalList;
        return signalList;
    }


    private static double calculateGradient(WifiSignalModel wifiSignalModel) {
        // 根据你的路径损耗模型公式计算梯度
        // 这里只是一个示例，你需要根据实际情况调整
        int rssi = wifiSignalModel.getSignalPower();
        double distance = Math.pow(10, ((double) -rssi / 20)); // 假设路径损耗为-20dBm/decade
        return -rssi / distance;
    }


    // 获取信号列表信息
    public ArrayList<WifiSignalModel> getSignalList() {
        return signalList;
    }
}
