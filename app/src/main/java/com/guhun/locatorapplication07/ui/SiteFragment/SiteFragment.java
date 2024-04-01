package com.guhun.locatorapplication07.ui.SiteFragment;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.guhun.locatorapplication07.R;
import com.guhun.locatorapplication07.data.MyAppGlobal;
import com.guhun.locatorapplication07.data.model.PositionModel;
import com.guhun.locatorapplication07.data.model.UserPositionModel;
import com.guhun.locatorapplication07.data.model.UserSiteModel;
import com.guhun.locatorapplication07.data.model.WifiDbmModel;
import com.guhun.locatorapplication07.data.model.WifiSignalModel;
import com.guhun.locatorapplication07.databinding.FragmentSiteBinding;
import com.guhun.locatorapplication07.databinding.FragmentUserBinding;
import com.guhun.locatorapplication07.server.AxiosGH;
import com.guhun.locatorapplication07.server.GetBitMap;
import com.guhun.locatorapplication07.server.MyWebSocketClient;
import com.guhun.locatorapplication07.server.WifiManagerGH;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class SiteFragment extends Fragment {

    private FragmentSiteBinding binding;
    private MyAppGlobal global;
    private WifiManagerGH wifiManagerGH;

    private FragmentUserBinding userBinding;

    public static SiteFragment newInstance() {
        return new SiteFragment();
    }

    // 声明一个成员变量用于保存当前标记对象
    private Bitmap originalBitmapGlobal;

    private int lineX;
    private int lineY;

    private boolean isTimerRunning = false;

    private int mapId = 10;


    private static final String WS_URL = "wss://wifilocation-release.lecangs.com/locator_server/data-websocket";
    private WebSocketClient webSocketClient;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_site, container, false);
        global = (MyAppGlobal) getActivity().getApplication();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgInit();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    stopTimer();
                    Toast.makeText(getContext(), "定位结束", Toast.LENGTH_SHORT).show();
                } else {
                    startTimer();
                    Toast.makeText(getContext(), "定位开始", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void startTimer() {
        isTimerRunning = true;
        handler.postDelayed(runnable, 1000); // 延迟3秒后执行
    }

    private void stopTimer() {
        isTimerRunning = false;
        handler.removeCallbacks(runnable); // 移除定时任务
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imgInit();
            location(mapId,0);
            handler.postDelayed(this, 1000); // 3秒后再次执行
        }
    };



    public void location(int mapId,int siteId){
        wifiManagerGH = new WifiManagerGH(getContext());
        wifiManagerGH.initSignalList(50,0);

        ArrayList<WifiSignalModel> wifilist = wifiManagerGH.getSignalList();
//        List<WifiSignalModel> filteredMacAddresses = filterWifiSignalModels(wifilist);


        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();
        for(WifiSignalModel wifiSignalModel:wifilist){
            WifiDbmModel wifiDbmModel = new WifiDbmModel(); // 在每次迭代中创建一个新的对象
            wifiDbmModel.setSiteId(siteId);
            wifiDbmModel.setMapId(mapId);
            wifiDbmModel.setWifiName(wifiSignalModel.getSignalName());
            wifiDbmModel.setMacAddress(wifiSignalModel.getSignalMac());
            wifiDbmModel.setWifiDbm((double)wifiSignalModel.getSignalPower());
            wifiDbmModelList.add(wifiDbmModel);
        }

        //调用http接口 补全代码


        // 将参数转换为JSON格式
        Gson gson = new Gson();
        String jsonParams = gson.toJson(wifiDbmModelList);

        // 发起 HTTP 请求
        String apiUrl = "https://wifilocation-release.lecangs.com/locator_server/wifi/localize/imp"; // 替换为实际的 API URL
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,

                response -> {
                    // 处理响应
                    Log.d("Response", response);
                    String[] parts = response.substring(1, response.length() - 1).split(",");
                    if (parts.length == 2) {
                        try {
                            double x = Double.parseDouble(parts[0].trim());
                            double y = Double.parseDouble(parts[1].trim());
                            // 在图片上显示坐标
                            showLocationOnImage(originalBitmapGlobal,x,y);
                            PositionModel p = new PositionModel();
                            p.setLinex(String.valueOf(x));
                            p.setLiney(String.valueOf(y));

                            lineX = (int) x;
                            lineY = (int) y;
                            binding.setPositionInfo(p);


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "解析响应时出错", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "响应格式不正确", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // 处理错误
                    Log.e("Error", error.toString());
                    Toast.makeText(getContext(), "算法接口发生错误", Toast.LENGTH_SHORT).show();
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


        /**
         * 用户同步接口
         */

        UserPositionModel userPositionModel = new UserPositionModel();
        userPositionModel.setLocationLinex(lineX);
        userPositionModel.setLocationLiney(lineY);
        userPositionModel.setUpdateTime(new Date().toString());
        userPositionModel.setSiteId(0);
        userPositionModel.setUserId(global.getUserId());
        userPositionModel.setMapId(mapId);

        //调用http接口 补全代码


        // 将参数转换为JSON格式
        Gson gsonUser = new Gson();
        String jsonParamsUser = gsonUser.toJson(userPositionModel);


        String apiUrlUser = "https://wifilocation-release.lecangs.com/locator_server/user/sync"; // 替换为实际的 API URL
        RequestQueue requestQueueUser = Volley.newRequestQueue(getContext());
        StringRequest stringRequestUser = new StringRequest(Request.Method.POST, apiUrlUser,

                response -> {
                    // 处理响应
                    Log.d("Response", response);
                },
                error -> {
                    // 处理错误
                    Log.e("Error", error.toString());
                    Toast.makeText(getContext(), "用户同步接口发生错误", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                return jsonParamsUser.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        requestQueueUser.add(stringRequestUser);



    }


    public void imgInit() {
        String imgUrl = global.getImgUrl() + "img/ywy.jpg";
        GetBitMap.getHttpBitmap(imgUrl, new GetBitMap.Callback() {
            @Override
            public void onSuccess(Bitmap originalBitmap) {
                // 获取原始图片的宽度和高度
                int originalWidth = originalBitmap.getWidth();
                int originalHeight = originalBitmap.getHeight();

                // 计算缩放后的宽度和高度
                int scaledWidth = originalWidth * 2; // 三倍缩放
                int scaledHeight = originalHeight * 2; // 三倍缩放

                // 使用三倍缩放创建新的 Bitmap
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, true);

                // 创建一个新的 Bitmap 对象，用于绘制坐标信息
                Bitmap bitmapWithCoordinates = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

                // 创建一个 Canvas 对象，并将新的 Bitmap 设置给它
                Canvas canvas = new Canvas(bitmapWithCoordinates);

                // 绘制缩放后的图片
                canvas.drawBitmap(scaledBitmap, 0, 0, null);

                // 绘制坐标信息
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setTextSize(20);

                // 计算单元格的宽度和高度
                int cellWidth = scaledWidth / 7;  // 假设横向最大值为7
                int cellHeight = scaledHeight / 15;  // 假设纵向最大值为15

                // 绘制坐标信息
                for (int x = 1; x <= 7; x++) { // 从1开始
                    for (int y = 1; y <= 15; y++) { // 从1开始
                        String coordinate = "(" + x + ", " + y + ")";
                        float textWidth = paint.measureText(coordinate);
                        float textHeight = paint.getTextSize();
                        float startX = (x - 1) * cellWidth + (cellWidth - textWidth) / 2;
                        float startY = (y - 1) * cellHeight + (cellHeight + textHeight) / 2;
                        canvas.drawText(coordinate, startX, startY, paint);
                    }
                }
                originalBitmapGlobal = bitmapWithCoordinates;
                // 将带有坐标信息的 Bitmap 设置给 ImageView
                binding.imageView.setImageBitmap(bitmapWithCoordinates);

            }
        });
    }

    // 在图片上显示坐标
    // 在图片格子中间显示黄色点标记
    private void showLocationOnImage(Bitmap originalBitmap,double x, double y) {
        // 获取 ImageView 的宽度和高度
        int imageViewWidth = binding.imageView.getWidth();
        int imageViewHeight = binding.imageView.getHeight();

        // 创建一个没有标记的副本
        Bitmap bitmapWithoutMarker = originalBitmap.copy(originalBitmap.getConfig(), true);

        // 创建 Canvas 对象，并将新的 Bitmap 设置给它
        Canvas canvas = new Canvas(bitmapWithoutMarker);

        // 计算标记点的中心位置
        int imageX = (int) ((x - 1) * imageViewWidth / 7); // 假设横向最大值为7
        int imageY = (int) ((y - 1) * imageViewHeight / 15); // 假设纵向最大值为15
        int centerX = imageX + imageViewWidth / 14; // 在格子中心绘制标记
        int centerY = imageY + imageViewHeight / 30; // 在格子中心绘制标记

        // 绘制原始图片
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        // 绘制标记点
        drawMarker(canvas, centerX, centerY);

        // 将新的 Bitmap 设置给 ImageView，显示标记
        binding.imageView.setImageBitmap(bitmapWithoutMarker);
    }

    // 绘制标记点的方法
    private void drawMarker(Canvas canvas, int centerX, int centerY) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, 10, paint); // 绘制一个半径为 10 的黄色圆形
    }


    public static List<WifiSignalModel> filterWifiSignalModels(List<WifiSignalModel> wifilist) {
        List<WifiSignalModel> filteredList = new ArrayList<>();

        for (WifiSignalModel wifiSignalModel : wifilist) {
            String macAddress = wifiSignalModel.getSignalMac();
            // 获取倒数第二个十六进制字符
            String secondLastChar = macAddress.substring(macAddress.length() - 5, macAddress.length() - 3).toLowerCase();
            // 检查是否符合条件
            if (secondLastChar.equals("df") || secondLastChar.equals("e3") || secondLastChar.equals("7a") || secondLastChar.equals("5e")) {
                filteredList.add(wifiSignalModel);
            }
        }

        return filteredList;
    }


}