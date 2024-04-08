package com.guhun.locatorapplication07.ui.WifiFragment.MapFragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.guhun.locatorapplication07.R;
import com.guhun.locatorapplication07.data.model.PositionEntity;
import com.guhun.locatorapplication07.data.model.WifiDbmModel;
import com.guhun.locatorapplication07.data.model.WifiSignalModel;
import com.guhun.locatorapplication07.databinding.FragmentWifiInstituteBinding;
import com.guhun.locatorapplication07.server.WifiManagerGH;
import com.guhun.locatorapplication07.ui.WifiFragment.WifiAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InstituteMapFragment extends Fragment {

    private FragmentWifiInstituteBinding fragmentWifiBinding;

    private WifiManagerGH wifiManagerGH;

    // 梯度阈值需要自行设置
    private final double THRESHOLD = -2;

    private final int siteId = 0;

    private final int mapId = 11;


    public static com.guhun.locatorapplication07.ui.WifiFragment.MapFragment.InstituteMapFragment newInstance() {
        return new com.guhun.locatorapplication07.ui.WifiFragment.MapFragment.InstituteMapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // DataBinding
        fragmentWifiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_institute, container, false);

        return fragmentWifiBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText xPositionText = getView().findViewById(R.id.tx_positionX);
        EditText yPositionText = getView().findViewById(R.id.tx_positionY);

        getWifiInfo();

        fragmentWifiBinding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateWifiInfo(mapId, siteId, xPositionText, yPositionText);
            }
        });

//        fragmentWifiBinding.button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getWifiInfo(xPositionText, yPositionText);
//            }
//        });

//        fragmentWifiBinding.button5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateWifiInfoOnes(mapId, siteId,xPositionText, yPositionText);
//            }
//        });


        fragmentWifiBinding.button6.setOnClickListener(v -> {
            WiFiFingerPointAnalysis(xPositionText, yPositionText);
        });


    }


    /**
     * 获取wifi信息
     * 无参无返回值
     * 作者咕魂
     * 2021年4月8日10:31:12
     */
    private void getWifiInfo() {
        // 创建wifi对象
        wifiManagerGH = new WifiManagerGH(getContext());
        // 获取wifi信息
        wifiManagerGH.initSignalList(50, 0);

        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("48:57:02:18:df", 5, 2, 2.8));// 48:57:02:18:df
        positionEntities.add(new PositionEntity("48:57:02:18:e3", 5, 6, 2.8));// 48:57:02:18:e3
        positionEntities.add(new PositionEntity("ac:75:1d:e7:7a", 5, 12, 2.8));// ac:75:1d:e7:7a
        positionEntities.add(new PositionEntity("ac:75:1d:e7:5e", -3, 6, 2.8));// ac:75:1d:e7:5e

        wifiManagerGH.stHandle(wifiManagerGH.getSignalList(), positionEntities);

        // wifilist绑定适配器
        RecyclerView wifiListView = getActivity().findViewById(R.id.wifiListView);
        wifiListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        wifiListView.setAdapter(new WifiAdapter(wifiManagerGH.getSignalList()));
    }

    private void getWifiInfo(EditText x, EditText y) {
        // 创建一个 Handler
        Handler handler = new Handler(Looper.getMainLooper());
        // 获取root view
        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);


        // 创建wifi对象
        wifiManagerGH = new WifiManagerGH(getContext());
        // 获取wifi信息
        wifiManagerGH.initSignalList(50, 0);
        if (x.getText().toString().isEmpty() || y.getText().toString().isEmpty()) {
            handler.post(() -> Snackbar.make(rootView, "没有填坐标信息,无法入库指纹信息", Snackbar.LENGTH_SHORT).show());
            return;
        }
        String linex = x.getText().toString();
        String liney = y.getText().toString();
        int xx = Integer.parseInt(linex);
        int yy = Integer.parseInt(liney);


        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("48:57:02:18:df", 5, 2, 2.8));// 48:57:02:18:df
        positionEntities.add(new PositionEntity("48:57:02:18:e3", 5, 6, 2.8));// 48:57:02:18:e3
        positionEntities.add(new PositionEntity("ac:75:1d:e7:7a", 5, 12, 2.8));// ac:75:1d:e7:7a
        positionEntities.add(new PositionEntity("ac:75:1d:e7:5e", -3, 6, 2.8));// ac:75:1d:e7:5e

        wifiManagerGH.stHandle(wifiManagerGH.getSignalList(), positionEntities, xx, yy);

        // wifilist绑定适配器
        RecyclerView wifiListView = getActivity().findViewById(R.id.wifiListView);
        wifiListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        wifiListView.setAdapter(new WifiAdapter(wifiManagerGH.getSignalList()));
    }

    /**
     * 离线阶段读取wifi信息存入数据库
     * @param mapId 地图编号
     * @param siteId 站点编号默认为0
     * @param x 文本框x坐标
     * @param y 文本框y坐标
     */
    private void generateWifiInfo(int mapId, int siteId, EditText x, EditText y) {

        // 创建一个 Handler
        Handler handler = new Handler(Looper.getMainLooper());


        // Ap位置
        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("48:57:02:18:df", 5, 2, 2.8));// 48:57:02:18:df
        positionEntities.add(new PositionEntity("48:57:02:18:e3", 5, 6, 2.8));// 48:57:02:18:e3
        positionEntities.add(new PositionEntity("ac:75:1d:e7:7a", 5, 12, 2.8));// ac:75:1d:e7:7a
        positionEntities.add(new PositionEntity("ac:75:1d:e7:5e", -3, 6, 2.8));// ac:75:1d:e7:5e


        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        if (x.getText().toString().isEmpty() || y.getText().toString().isEmpty()) {
            handler.post(() -> Snackbar.make(rootView, "没有填坐标信息,无法入库指纹信息", Snackbar.LENGTH_SHORT).show());
            return;
        }

        String linex = x.getText().toString();
        String liney = y.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Map<String, List<Double>> wifiSignalMap = new HashMap<>();
        Map<String, List<Double>> wifiSignalAveMap = new HashMap<>();
        Map<String, Double> wifiSignalAverageMap = new HashMap<>(); // 用于存储每个wifiName_macAddress类别的平均值
        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();

        fragmentWifiBinding.button3.setEnabled(false); // 禁用按钮


        // 创建一个 Runnable 来执行定时任务
        Runnable task = new Runnable() {
            int secondsProcessed = 0; // 用于计算处理的秒数

            @Override
            public void run() {

                if (secondsProcessed >= 15) {
                    // 如果已经处理了15秒以上，则取消定时任务
                    handler.removeCallbacks(this);
                    return;
                }


                // 创建wifi对象
                wifiManagerGH = new WifiManagerGH(getContext());

                // 获取wifi信息
                wifiManagerGH.initSignalList(200, 0);

                // 组装参数
                ArrayList<WifiSignalModel> wifiList = wifiManagerGH.getSignalList();

                // 遍历wifi信号，计算平均值
                for (WifiSignalModel wifiSignalModel : wifiList) {
                    String key = wifiSignalModel.getSignalName() + "_" + wifiSignalModel.getSignalMac() + "_" + secondsProcessed;
                    double wifiDbm = wifiSignalModel.getSignalPower();

                    if (!wifiSignalMap.containsKey(key)) {
                        wifiSignalMap.put(key, new ArrayList<>());
                    }
                    wifiSignalMap.get(key).add(wifiDbm);
                }

                // 显示录入指纹数据的消息
                handler.post(() -> Snackbar.make(rootView, "录入指纹数据中: " + (secondsProcessed) + "/15", Snackbar.LENGTH_SHORT).show());

                // 如果已经处理了20秒，则取消定时器
                if (secondsProcessed == 14) {
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
                        String firstFiveParts = extractFirstFiveParts(macAddress);

                        if (null != firstFiveParts) {
                            boolean shouldRemove = true;
                            for (PositionEntity entity : positionEntities) {
                                if (entity.getFeature().equals(firstFiveParts)) {
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
//                    double signalX = Double.parseDouble(linex);
//                    double signalY = Double.parseDouble(liney);
//                    double signalZ = 1;
//                    double distance;

//                    // 梯度筛选
//                    Iterator<WifiDbmModel> iterator = wifiDbmModelList.iterator();
//                    while (iterator.hasNext()) {
//                        WifiDbmModel wifiDbmModel = iterator.next();
//                        String macAddress = wifiDbmModel.getMacAddress();
//                        String[] parts = macAddress.split(":");
//                        String secondLastPart;
//
//                        if (parts.length >= 2) {
//                            secondLastPart = parts[parts.length - 2];
//                            for (PositionEntity entity : positionEntities) {
//                                if (entity.getFeature().equals(secondLastPart)) {
//                                    // 计算欧几里得距离
//                                    distance = Math.sqrt(Math.pow(entity.getX() - signalX, 2) +
//                                            Math.pow(entity.getY() - signalY, 2) +
//                                            Math.pow(entity.getZ() - signalZ, 2));
//                                    // n代表电磁波消耗率
//                                    double n = 3.5;
//                                    //斜率
//                                    double px = -10 * (n / distance) / Math.log(10);
//                                    if (px > THRESHOLD) {
//                                        iterator.remove();
//                                    }
//                                }
//                            }
//                        } else {
//                            iterator.remove();
//                        }
//                    }
                    if (wifiDbmModelList.isEmpty()) {
                        handler.post(() -> Snackbar.make(rootView, "无活跃AP", Snackbar.LENGTH_SHORT).show());
                        return;
                    }


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

    /**
     * 录一次wifi信息
     * @param mapId 地图编号
     * @param siteId 站点编号
     * @param x 文本框的值
     * @param y 文本框的值
     */
    private void generateWifiInfoOnes(int mapId, int siteId, EditText x, EditText y) {
        // 创建一个 Handler
        Handler handler = new Handler(Looper.getMainLooper());

        // Ap位置
        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("48:57:02:18:df", 5, 2, 2.8));// 48:57:02:18:df
        positionEntities.add(new PositionEntity("48:57:02:18:e3", 5, 6, 2.8));// 48:57:02:18:e3
        positionEntities.add(new PositionEntity("ac:75:1d:e7:7a", 5, 12, 2.8));// ac:75:1d:e7:7a
        positionEntities.add(new PositionEntity("ac:75:1d:e7:5e", -3, 6, 2.8));// ac:75:1d:e7:5e


        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        if (x.getText().toString().isEmpty() || y.getText().toString().isEmpty()) {
            handler.post(() -> Snackbar.make(rootView, "没有填坐标信息,无法入库指纹信息", Snackbar.LENGTH_SHORT).show());
            return;
        }
        String linex = x.getText().toString();
        String liney = y.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();

        // 创建wifi对象
        wifiManagerGH = new WifiManagerGH(getContext());

        // 获取wifi信息
        wifiManagerGH.initSignalList(50, 0);

        // 组装参数
        ArrayList<WifiSignalModel> wifiList = wifiManagerGH.getSignalList();


        for (WifiSignalModel wifiSignalModel : wifiList) {
            Date currentDate = new Date();
            String formattedDate = dateFormat.format(currentDate);


            WifiDbmModel wifiDbmModel = new WifiDbmModel();
            wifiDbmModel.setSiteId(siteId);
            wifiDbmModel.setMapId(mapId);
            wifiDbmModel.setWifiName(wifiSignalModel.getSignalName());
            wifiDbmModel.setMacAddress(wifiSignalModel.getSignalMac());
            wifiDbmModel.setWifiDbm((double) wifiSignalModel.getSignalPower());
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
            String firstFiveParts = extractFirstFiveParts(macAddress);

            if (null != firstFiveParts) {
                boolean shouldRemove = true;
                for (PositionEntity entity : positionEntities) {
                    if (entity.getFeature().equals(firstFiveParts)) {
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
        double signalZ = 1;
        double distance;

        // 梯度筛选
        Iterator<WifiDbmModel> iterator = wifiDbmModelList.iterator();
        while (iterator.hasNext()) {
            WifiDbmModel wifiDbmModel = iterator.next();
            String macAddress = wifiDbmModel.getMacAddress();

            String firstFiveParts = extractFirstFiveParts(macAddress);

            if (null!=firstFiveParts) {
                for (PositionEntity entity : positionEntities) {
                    if (entity.getFeature().equals(firstFiveParts)) {
                        // 计算欧几里得距离
                        distance = Math.sqrt(Math.pow(entity.getX() - signalX, 2) +
                                Math.pow(entity.getY() - signalY, 2) +
                                Math.pow(entity.getZ() - signalZ, 2));
                        // n代表电磁波消耗率
                        double n = 3.5;
                        //斜率
                        double px = -10 * (n / distance) / Math.log(10);
                        if (px > THRESHOLD) {
                            iterator.remove();
                        }
                    }
                }
            } else {
                iterator.remove();
            }
        }
        if (wifiDbmModelList.isEmpty()) {
            handler.post(() -> Snackbar.make(rootView, "无活跃AP", Snackbar.LENGTH_SHORT).show());
            return;
        }

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
                    handler.post(() -> Snackbar.make(rootView, "完成采集", Snackbar.LENGTH_SHORT).show());
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


    }

    /**
     * 分析wifi信息
     * @param x 文本框的值
     * @param y 文本框的值
     */
    private void WiFiFingerPointAnalysis(EditText x, EditText y) {
        // 创建一个 Handler
        Handler handler = new Handler(Looper.getMainLooper());

        // Ap位置
        List<PositionEntity> positionEntities = new ArrayList<>();

        // 实验室AP点位置信息
        positionEntities.add(new PositionEntity("48:57:02:18:df", 5, 2, 2.8));// 48:57:02:18:df
        positionEntities.add(new PositionEntity("48:57:02:18:e3", 5, 6, 2.8));// 48:57:02:18:e3
        positionEntities.add(new PositionEntity("ac:75:1d:e7:7a", 5, 12, 2.8));// ac:75:1d:e7:7a
        positionEntities.add(new PositionEntity("ac:75:1d:e7:5e", -3, 6, 2.8));// ac:75:1d:e7:5e

        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        if (x.getText().toString().isEmpty() || y.getText().toString().isEmpty()) {
            handler.post(() -> Snackbar.make(rootView, "没有填坐标信息,无法入库指纹信息", Snackbar.LENGTH_SHORT).show());
            return;
        }

//        fragmentWifiBinding.button6.setEnabled(false); // 禁用按钮

        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();

        // 创建一个 Runnable 来执行定时任务
        Runnable task = new Runnable() {
            int secondsProcessed = 0; // 用于计算处理的秒数

            @Override
            public void run() {
                // 90s之后直接取消
                if (secondsProcessed >= 90) {
                    // 如果已经处理了90秒以上，则取消定时任务
                    handler.removeCallbacks(this);
                    return;
                }
                // 前60s获取数据
                if (secondsProcessed < 59) {

                    // 显示录入指纹数据的消息
                    handler.post(() -> Snackbar.make(rootView, "分析指纹数据中: " + (secondsProcessed) + "/60", Snackbar.LENGTH_SHORT).show());

                    // 创建wifi对象
                    wifiManagerGH = new WifiManagerGH(getContext());

                    // 获取wifi信息
                    wifiManagerGH.initSignalList(50, 0);

                    // 组装参数
                    ArrayList<WifiSignalModel> wifiList = wifiManagerGH.getSignalList();

                    for (WifiSignalModel wifiSignalModel : wifiList) {
                        WifiDbmModel wifiDbmModel = new WifiDbmModel();
                        wifiDbmModel.setUpdateTime(String.valueOf(secondsProcessed));
                        wifiDbmModel.setMacAddress(wifiSignalModel.getSignalMac());
                        wifiDbmModel.setWifiDbm((double) wifiSignalModel.getSignalPower());
                        wifiDbmModelList.add(wifiDbmModel);
                    }


//                    // 当前地图只筛选df 5e e3 7a的值
//                    Iterator<WifiDbmModel> iteratorModel = wifiDbmModelList.iterator();
//                    while (iteratorModel.hasNext()) {
//                        WifiDbmModel wifiDbmModel = iteratorModel.next();
//                        String macAddress = wifiDbmModel.getMacAddress();
//                        if (null == macAddress) {
//                            continue;
//                        }
//                        String firstFiveParts = extractFirstFiveParts(macAddress);
//
//                        if (null != firstFiveParts) {
//                            boolean shouldRemove = true;
//                            for (PositionEntity entity : positionEntities) {
//                                if (entity.getFeature().equals(firstFiveParts)) {
//                                    shouldRemove = false;
//                                    break;
//                                }
//                            }
//                            if (shouldRemove) {
//                                iteratorModel.remove();
//                            }
//                        } else {
//                            iteratorModel.remove();
//                        }
//                    }
                }


                // 60s后将数据写入文件中
                if (secondsProcessed == 59) {
                    //调用http接口 补全代码
                    // 将参数转换为JSON格式
                    Gson gson = new Gson();
                    String jsonParams = gson.toJson(wifiDbmModelList);

                    // 发起HTTP请求
                    String apiUrl = "https://wifilocation-release.lecangs.com/locator_server/wifi/analyze"; // 替换为实际的API URL
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,

                            response -> {
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
                    // 处理响应
//                    fragmentWifiBinding.button6.setEnabled(true); // 启用按钮
                }

                secondsProcessed++;
                // 每隔1秒执行一次任务
                handler.postDelayed(this, 1000);
            }
        };

        // 开始执行定时任务
        handler.post(task);
    }


    public static String extractFirstFiveParts(String macAddress) {
        // 使用冒号分割MAC地址
        String[] parts = macAddress.split(":");
        // 如果部分数量小于五个，则返回null
        if (parts.length < 5) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 将前五个部分拼接起来
        for (int i = 0; i < 5; i++) {
            builder.append(parts[i]);
            if (i < 4) {
                builder.append(":");
            }
        }
        return builder.toString();
    }


}
