package com.guhun.locatorapplication07.ui.WifiFragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.guhun.locatorapplication07.R;
import com.guhun.locatorapplication07.data.MyAppGlobal;
import com.guhun.locatorapplication07.data.model.PositionEntity;
import com.guhun.locatorapplication07.data.model.UserSiteModel;
import com.guhun.locatorapplication07.data.model.WifiDbmModel;
import com.guhun.locatorapplication07.data.model.WifiSignalModel;
import com.guhun.locatorapplication07.databinding.FragmentWifiBinding;
import com.guhun.locatorapplication07.server.WifiManagerGH;
import com.guhun.locatorapplication07.server.WifiManagerMC;
import com.guhun.locatorapplication07.server.WifiScanner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WifiFragment extends Fragment {

    private FragmentWifiBinding fragmentWifiBinding;

    private WifiManagerGH wifiManagerGH;

    private WifiManagerMC wifiManagerMC;


    private WifiManager wifiManager;
    private TextView wifiInfoTextView;

    private int secondsProcessed = 0;

    private boolean processFinished;

    private int key = 1;

    private final double THRESHOLD = -2;


    private MyAppGlobal global;

    public static WifiFragment newInstance() {
        return new WifiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // DataBinding
        fragmentWifiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi, container, false);



        return fragmentWifiBinding.getRoot();
    }



//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText xPositionText = getView().findViewById(R.id.tx_positionX);
        EditText yPositionText = getView().findViewById(R.id.tx_positionY);

        getWifiInfo();


        // TODO: Use the ViewModel
        // 格式化刷新时间
//        int time = global.REFRESHTIME;
//        String timeText = time + "s";
//        if(time >= 60){
//            int mm = time / 60;
//            int ss = time % 60;
//            timeText = mm + "m" + ss + "s";
//            if(mm >= 60){
//                int hh = mm / 60;
//                mm = mm % 60;
//                timeText = hh + "h" + mm + "m" + ss + "s";
//            }
//        }
//        fragmentWifiBinding.textView11.setText("扫描时间间隔："+timeText);




        // 点击获取指纹信息按钮时刷新扫描
//        fragmentWifiBinding.button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getWifiInfo();
//                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//




        fragmentWifiBinding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int siteId = 0;
                int mapId = 8;
                generateWifiInfo(mapId,siteId,xPositionText,yPositionText);
            }
        });
//        fragmentWifiBinding.button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getWifiInfo();
//            }
//        });


    }




    /**
     * 获取wifi信息
     * 无参无返回值
     * 作者咕魂
     * 2021年4月8日10:31:12
     */
    private void getWifiInfo(){
//        Context context = getContext();
        // 创建wifi对象
        wifiManagerGH = new WifiManagerGH(getContext());
        // 获取wifi信息
        wifiManagerGH.initSignalList(50, 0);


//        WifiScanner wifiScanner = (WifiScanner) context.getSystemService(Context.WIFI_SCANNER_SERVICE);




//        // 创建WifiManagerMC实例
//        WifiManagerMC wifiManagerMC = new WifiManagerMC(getContext());
//
//        // 启动高频率Wi-Fi扫描
//        wifiManagerMC.startHighFrequencyScan();
//
//
//        // 检查是否有可用的扫描结果
//        List<ScanResult> scanResults = wifiManagerMC.getWifiScanResults();
//        if (scanResults != null && !scanResults.isEmpty()) {
//            // 更新UI在主线程中运行
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    // 绑定适配器到RecyclerView
//                    RecyclerView wifiListView = getActivity().findViewById(R.id.wifiListView);
//                    wifiListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//                    WifiAdapter wifiAdapter = new WifiAdapter(wifiManagerMC.getSignalList());
//                    wifiListView.setAdapter(wifiAdapter);
//                }
//            });
//        } else {
//            // 处理扫描结果为空的情况，例如显示错误消息或重新尝试扫描
//            // ...
//            // 创建一个 Handler
//            Handler handler = new Handler(Looper.getMainLooper());
//            View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
//
//            handler.post(() -> Snackbar.make(rootView, "程序获取wifi数据集为null", Snackbar.LENGTH_SHORT).show());

//        }

        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("df",4,3,2.5));// df
        positionEntities.add(new PositionEntity("e3",4,7,2.5));// e3
        positionEntities.add(new PositionEntity("7a",4,11,2.5));// 7a
        positionEntities.add(new PositionEntity("5e",-2,7,2.5));// 5e

        wifiManagerGH.stHandle(wifiManagerGH.getSignalList(),positionEntities);

        // wifilist绑定适配器
        RecyclerView wifiListView = getActivity().findViewById(R.id.wifiListView);
        wifiListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        wifiListView.setAdapter(new WifiAdapter(wifiManagerGH.getSignalList()));
    }

    private void generateWifiInfo(int mapId, int siteId, EditText x, EditText y) {
        // Ap位置
        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("df",4,3,2.5));// df
        positionEntities.add(new PositionEntity("e3",4,7,2.5));// e3
        positionEntities.add(new PositionEntity("7a",4,11,2.5));// 7a
        positionEntities.add(new PositionEntity("5e",-2,7,2.5));// 5e


        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        String linex = x.getText().toString();
        String liney = y.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Map<String, List<Double>> wifiSignalMap = new HashMap<>();
        Map<String, List<Double>> wifiSignalAveMap = new HashMap<>();
        Map<String, Double> wifiSignalAverageMap = new HashMap<>(); // 用于存储每个wifiName_macAddress类别的平均值
        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();

        fragmentWifiBinding.button3.setEnabled(false); // 禁用按钮

        // 创建一个 Handler
        Handler handler = new Handler(Looper.getMainLooper());

        // 创建一个 Runnable 来执行定时任务
        Runnable task = new Runnable() {
            int secondsProcessed = 0; // 用于计算处理的秒数

            @Override
            public void run() {

                if (secondsProcessed >= 20) {
                    // 如果已经处理了20秒以上，则取消定时任务
                    handler.removeCallbacks(this);
                    return;
                }


                // 创建wifi对象
                wifiManagerGH = new WifiManagerGH(getContext());

                // 获取wifi信息
                wifiManagerGH.initSignalList(50, 0);

                // 组装参数
                ArrayList<WifiSignalModel> wifilist = wifiManagerGH.getSignalList();

                // 遍历wifi信号，计算平均值
                for (WifiSignalModel wifiSignalModel : wifilist) {
                    String key = wifiSignalModel.getSignalName() + "_" + wifiSignalModel.getSignalMac() + "_" + secondsProcessed;
                    double wifiDbm = wifiSignalModel.getSignalPower();

                    if (!wifiSignalMap.containsKey(key)) {
                        wifiSignalMap.put(key, new ArrayList<>());
                    }
                    wifiSignalMap.get(key).add(wifiDbm);
                }

                // 显示录入指纹数据的消息
                handler.post(() -> Snackbar.make(rootView, "录入指纹数据中: " + (secondsProcessed) + "/20", Snackbar.LENGTH_SHORT).show());

                // 如果已经处理了20秒，则取消定时器
                if (secondsProcessed == 19) {
                    fragmentWifiBinding.button3.setEnabled(true); // 禁用按钮
                    Snackbar.make(rootView, "录入指纹数据完成", Snackbar.LENGTH_SHORT).show();

                    // 遍历原始的 wifiSignalMap
                    for (Map.Entry<String, List<Double>> entry : wifiSignalMap.entrySet()) {
                        String key = entry.getKey(); // 获取原始键
                        List<Double> values = entry.getValue(); // 获取对应的值列表

                        // 解析 wifi 名称和 MAC 地址
                        String[] parts = key.split("_");
                        String wifiNameMac = parts[0] + "_" + parts[1];

                        // 将前缀相同的值列表加起来
                        if (!wifiSignalAveMap.containsKey(wifiNameMac)) {
                            wifiSignalAveMap.put(wifiNameMac, new ArrayList<>());
                        }
                        wifiSignalAveMap.get(wifiNameMac).addAll(values);
                    }

                    // 计算每个前缀对应的平均值
                    for (Map.Entry<String, List<Double>> entry : wifiSignalAveMap.entrySet()) {
                        String wifiNameMac = entry.getKey();
                        List<Double> values = entry.getValue();

                        // 计算平均值
                        double sum = 0;
                        for (Double value : values) {
                            sum += value;
                        }
                        double average = sum / values.size();
                        // 使用 DecimalFormat 格式化平均值
                        DecimalFormat decimalFormat = new DecimalFormat("#.#");
                        double formattedAverage = Double.parseDouble(decimalFormat.format(average));

                        // 存储平均值
                        wifiSignalAverageMap.put(wifiNameMac, formattedAverage);
                    }

                    // 在定时任务结束后执行你的逻辑
                    // 遍历wifiSignalAverageMap，并生成WifiDbmModel对象
                    for (Map.Entry<String, Double> entry : wifiSignalAverageMap.entrySet()) {
                        String[] keyParts = entry.getKey().split("_");
                        String wifiName = keyParts[0];
                        String macAddress = keyParts[1];
                        double wifiDbm = entry.getValue();

                        Date currentDate = new Date();
                        String formattedDate = dateFormat.format(currentDate);


                        WifiDbmModel wifiDbmModel = new WifiDbmModel();
                        wifiDbmModel.setSiteId(siteId);
                        wifiDbmModel.setMapId(mapId);
                        wifiDbmModel.setWifiName(wifiName);
                        wifiDbmModel.setMacAddress(macAddress);
                        wifiDbmModel.setWifiDbm(wifiDbm);
                        wifiDbmModel.setLinex(Integer.parseInt(linex));
                        wifiDbmModel.setLiney(Integer.parseInt(liney));
                        wifiDbmModel.setUpdateTime(formattedDate);
                        wifiDbmModelList.add(wifiDbmModel);
                    }

                    wifiDbmModelList.sort(Comparator.comparingDouble(WifiDbmModel::getWifiDbm));
                    // 当前地图只筛选df 5e e3 7a的值
                    Iterator<WifiDbmModel> iteratorModel = wifiDbmModelList.iterator();
                    while (iteratorModel.hasNext()) {
                        WifiDbmModel wifiDbmModel = iteratorModel.next();
                        String macAddress = wifiDbmModel.getMacAddress();
                        if (null == macAddress) {
                            continue;
                        }
                        String[] parts = macAddress.split(":");
                        String secondLastPart;
                        if (parts.length >= 2) {
                            secondLastPart = parts[parts.length - 2];
                            boolean shouldRemove = true;
                            for (PositionEntity entity : positionEntities) {
                                if (entity.getFeature().equals(secondLastPart)) {
                                    shouldRemove = false;
                                    break;
                                }
                            }
                            if (shouldRemove) {
                                iteratorModel.remove();
                            }
                        } else {
                            iteratorModel.remove();
                        }
                    }
                    double signalX = Double.parseDouble(linex);
                    double signalY = Double.parseDouble(liney);
                    double signalZ = 1.2;
                    double distance;

                    // 梯度筛选
                    Iterator<WifiDbmModel> iterator = wifiDbmModelList.iterator();
                    while (iterator.hasNext()) {
                        WifiDbmModel wifiDbmModel = iterator.next();
                        String macAddress = wifiDbmModel.getMacAddress();
                        String[] parts = macAddress.split(":");
                        String secondLastPart;

                        if (parts.length >= 2) {
                            secondLastPart = parts[parts.length - 2];
                            for(PositionEntity entity:positionEntities){
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
                        }else{
                            iterator.remove();
                        }
                    }
                    if(wifiDbmModelList.isEmpty()){
                        handler.post(() -> Snackbar.make(rootView, "无活跃AP", Snackbar.LENGTH_SHORT).show());
                        return;
                    }

                    // top50强度逻辑筛选
//                    List<WifiDbmModel> top50WifiDbmList = wifiDbmModelList.subList(0, Math.min(50, wifiDbmModelList.size()));


                    //调用http接口 补全代码
                    // 将参数转换为JSON格式
                    Gson gson = new Gson();
                    String jsonParams = gson.toJson(wifiDbmModelList);

                    // 发起HTTP请求
                    String apiUrl = "https://wifilocation-release.lecangs.com/locator_server/wifi/sync"; // 替换为实际的API URL
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,

                            response -> {
                                // 处理响应
                                Log.d("Response", response);
                            },
                            error -> {
                                // 处理错误
                                Log.e("Error", error.toString());
                            }) {
                        @Override
                        public byte[] getBody() {
                            return jsonParams.getBytes();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    requestQueue.add(stringRequest);

                    // 取消定时任务
                    handler.removeCallbacks(this);
                }
                secondsProcessed++;
                // 每隔1秒执行一次任务
                handler.postDelayed(this, 1000);
            }
        };

        // 开始执行定时任务
        handler.post(task);
    }


//    private void generateWifiInfo(int mapId,int siteId,EditText x,EditText y) {
//
//        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
//
//
//        String linex = x.getText().toString();
//        String liney = y.getText().toString();
//
//        Map<String, List<Double>> wifiSignalMap = new HashMap<>();
//        Map<String, List<Double>> wifiSignalAveMap = new HashMap<>();
//        Map<String, Double> wifiSignalAverageMap = new HashMap<>(); // 用于存储每个wifiName_macAddress类别的平均值
//        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();
//
//        fragmentWifiBinding.button3.setEnabled(false); // 禁用按钮
//
//        // 创建一个定时器
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            int secondsProcessed = 0; // 用于计算处理的秒数
//
//            @Override
//            public void run() {
//                // 每隔1秒执行一次
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 创建wifi对象
//                        wifiManagerGH = new WifiManagerGH(getContext());
//
//                        // 获取wifi信息
//                        wifiManagerGH.initSignalList(30, 0);
//
//                        // 组装参数
//                        ArrayList<WifiSignalModel> wifilist = wifiManagerGH.getSignalList();
//
//                        // 遍历wifi信号，计算平均值
//                        for (WifiSignalModel wifiSignalModel : wifilist) {
//                            String key = wifiSignalModel.getSignalName() + "_" + wifiSignalModel.getSignalMac() + "_" + secondsProcessed;
//                            double wifiDbm = wifiSignalModel.getSignalPower();
//
//                            if (!wifiSignalMap.containsKey(key)) {
//                                wifiSignalMap.put(key, new ArrayList<>());
//                            }
//                            wifiSignalMap.get(key).add(wifiDbm);
//                        }
//
//                        // 显示录入指纹数据的消息
//                        Snackbar.make(rootView, "录入指纹数据中: " + (secondsProcessed + 1) + "/20", Snackbar.LENGTH_SHORT).show();
//
//
//                        // 如果已经处理了20秒，则取消定时器
//                        if (secondsProcessed == 19) {
//
//                            fragmentWifiBinding.button3.setEnabled(true); // 禁用按钮
//
//                            Snackbar.make(rootView, "录入指纹数据完成", Snackbar.LENGTH_SHORT).show();
//
//
//                            // 遍历原始的 wifiSignalMap
//                            for (Map.Entry<String, List<Double>> entry : wifiSignalMap.entrySet()) {
//                                String key = entry.getKey(); // 获取原始键
//                                List<Double> values = entry.getValue(); // 获取对应的值列表
//
//                                // 解析 wifi 名称和 MAC 地址
//                                String[] parts = key.split("_");
//                                String wifiNameMac = parts[0] + "_" + parts[1];
//
//                                // 将前缀相同的值列表加起来
//                                if (!wifiSignalAveMap.containsKey(wifiNameMac)) {
//                                    wifiSignalAveMap.put(wifiNameMac, new ArrayList<>());
//                                }
//                                wifiSignalAveMap.get(wifiNameMac).addAll(values);
//                            }
//
//                            // 计算每个前缀对应的平均值
//                            for (Map.Entry<String, List<Double>> entry : wifiSignalAveMap.entrySet()) {
//                                String wifiNameMac = entry.getKey();
//                                List<Double> values = entry.getValue();
//
//                                // 计算平均值
//                                double sum = 0;
//                                for (Double value : values) {
//                                    sum += value;
//                                }
//                                double average = sum / values.size();
//
//                                // 存储平均值
//                                wifiSignalAverageMap.put(wifiNameMac, average);
//                            }
//
//                            // 在定时任务结束后执行你的逻辑
//                            // 遍历wifiSignalAverageMap，并生成WifiDbmModel对象
//                            for (Map.Entry<String, Double> entry : wifiSignalAverageMap.entrySet()) {
//                                String[] keyParts = entry.getKey().split("_");
//                                String wifiName = keyParts[0];
//                                String macAddress = keyParts[1];
//                                double wifiDbm = entry.getValue();
//
//                                WifiDbmModel wifiDbmModel = new WifiDbmModel();
//                                wifiDbmModel.setSiteId(siteId);
//                                wifiDbmModel.setMapId(mapId);
//                                wifiDbmModel.setWifiName(wifiName);
//                                wifiDbmModel.setMacAddress(macAddress);
//                                wifiDbmModel.setWifiDbm(wifiDbm);
//                                wifiDbmModel.setLinex(Integer.parseInt(linex));
//                                wifiDbmModel.setLiney(Integer.parseInt(liney));
//                                wifiDbmModelList.add(wifiDbmModel);
//                            }
//
//                            //调用http接口 补全代码
//                            // 将参数转换为JSON格式
//                            Gson gson = new Gson();
//                            String jsonParams = gson.toJson(wifiDbmModelList);
//
//                            // 发起HTTP请求
//                            String apiUrl = "https://wifilocation-release.lecangs.com/locator_server/wifi/sync"; // 替换为实际的API URL
//                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
//
//                                    response -> {
//                                        // 处理响应
//                                        Log.d("Response", response);
//                                    },
//                                    error -> {
//                                        // 处理错误
//                                        Log.e("Error", error.toString());
//                                    }) {
//                                @Override
//                                public byte[] getBody() {
//                                    return jsonParams.getBytes();
//                                }
//
//                                @Override
//                                public String getBodyContentType() {
//                                    return "application/json";
//                                }
//                            };
//                            requestQueue.add(stringRequest);
//
//                            // 取消定时器
//                            timer.cancel();
//                        }
//                        secondsProcessed++;
//                    }
//                });
//            }
//        };
//
//        // 每隔1秒执行一次任务
//        timer.scheduleAtFixedRate(task, 0, 1000);
//    }

    private static double calculateGradient(WifiDbmModel wifiDbmModel) {
        // 根据你的路径损耗模型公式计算梯度
        // 这里只是一个示例，你需要根据实际情况调整
        double rssi = wifiDbmModel.getWifiDbm();
        double distance = Math.pow(10, (-rssi / 20)); // 假设路径损耗为-20dBm/decade
        return -rssi / distance;
    }
}