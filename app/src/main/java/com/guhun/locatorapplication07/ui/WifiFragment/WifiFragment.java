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
import com.google.gson.Gson;
import com.guhun.locatorapplication07.R;
import com.guhun.locatorapplication07.data.MyAppGlobal;
import com.guhun.locatorapplication07.data.model.UserSiteModel;
import com.guhun.locatorapplication07.data.model.WifiDbmModel;
import com.guhun.locatorapplication07.data.model.WifiSignalModel;
import com.guhun.locatorapplication07.databinding.FragmentWifiBinding;
import com.guhun.locatorapplication07.server.WifiManagerGH;

import java.util.ArrayList;
import java.util.List;

public class WifiFragment extends Fragment {

    private FragmentWifiBinding fragmentWifiBinding;

    private WifiManagerGH wifiManagerGH;

    private WifiManager wifiManager;
    private TextView wifiInfoTextView;


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

        // 获取wifi信息
        getWifiInfo();


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
                Toast.makeText(getActivity(), "录入数据成功", Toast.LENGTH_SHORT).show();
            }
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
        wifiManagerGH.initSignalList(30, 0);
        // wifilist绑定适配器
        RecyclerView wifiListView = getActivity().findViewById(R.id.wifiListView);
        wifiListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        wifiListView.setAdapter(new WifiAdapter(wifiManagerGH.getSignalList()));
    }

    private void generateWifiInfo(int mapId,int siteId,EditText x,EditText y){

        String linex = x.getText().toString();
        String liney = y.getText().toString();



        // 创建wifi对象
        wifiManagerGH = new WifiManagerGH(getContext());
        // 获取wifi信息
        wifiManagerGH.initSignalList(30, 0);
        // wifilist绑定适配器
        RecyclerView wifiListView = getActivity().findViewById(R.id.wifiListView);
        wifiListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        wifiListView.setAdapter(new WifiAdapter(wifiManagerGH.getSignalList()));


        //组装参数 其中siteId为输入
        ArrayList<WifiSignalModel> wifilist = wifiManagerGH.getSignalList();



        List<WifiDbmModel> wifiDbmModelList = new ArrayList<>();
        for(WifiSignalModel wifiSignalModel:wifilist){
            WifiDbmModel wifiDbmModel = new WifiDbmModel(); // 在每次迭代中创建一个新的对象
            wifiDbmModel.setSiteId(siteId);
            wifiDbmModel.setMapId(mapId);
            wifiDbmModel.setWifiName(wifiSignalModel.getSignalName());
            wifiDbmModel.setMacAddress(wifiSignalModel.getSignalMac());
            wifiDbmModel.setWifiDbm((double)wifiSignalModel.getSignalPower());
            wifiDbmModel.setLinex(Integer.valueOf(linex));
            wifiDbmModel.setLiney(Integer.valueOf(liney));
            wifiDbmModelList.add(wifiDbmModel);
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






    }
}