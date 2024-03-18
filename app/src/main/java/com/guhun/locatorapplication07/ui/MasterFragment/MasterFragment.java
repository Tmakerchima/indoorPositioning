package com.guhun.locatorapplication07.ui.MasterFragment;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guhun.locatorapplication07.data.MyAppGlobal;
import com.guhun.locatorapplication07.R;
import com.guhun.locatorapplication07.data.model.MapModel;
import com.guhun.locatorapplication07.data.model.SiteModel;
import com.guhun.locatorapplication07.data.model.WifiSignalModel;
import com.guhun.locatorapplication07.databinding.FragmentMasterBinding;
import com.guhun.locatorapplication07.server.AxiosGH;
import com.guhun.locatorapplication07.server.GetBitMap;
import com.guhun.locatorapplication07.server.WifiManagerGH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MasterFragment extends Fragment {

    private FragmentMasterBinding binding;// 数据绑定
    private MyAppGlobal global;// 全局变量
    private MapModel selectedMap;// 当前选择的地图信息
    private SiteModel selectedSite;// 当前选择的位置
    private WifiManagerGH wifiManagerGH;// 扫描wifi信号

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        global = (MyAppGlobal) getActivity().getApplication();
        if (global.getUserId() == null) {
            Toast.makeText(getActivity(), "暂未登录", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(MasterFragment.this).navigateUp();
            return null;
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_master, container, false);
        binding.setShowView(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
        if (global.getRight() != "10") {
            Toast.makeText(getActivity(), "权限不足，无法访问", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(MasterFragment.this).navigateUp();
            return;
        }
        getMapList();
        binding.btnGetSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 后台查询该坐标在指纹中是否存在，存在返回更新成功2，不存在返回创建成功1
                Map<String,Object> params = new HashMap<>();
                params.put("wifiSiteId",selectedSite.getSiteId());
                params.put("wifiGridX",binding.getGridX());
                params.put("wifiGridY",binding.getGridY());
                new AxiosGH().get(global.getServerUrl() + "/wifi/update",params, new AxiosGH.Callback() {
                    @Override
                    public void onSuccess(String res) {
                        JSONObject json = JSONObject.parseObject(res);
                        if (!json.getBoolean("success")){
                            Toast.makeText(getContext(),"采集失败！",Toast.LENGTH_SHORT);
                            return;
                        }
                        int wifiId = json.getIntValue("wifiId");

                        wifiManagerGH = new WifiManagerGH(getContext());
                        wifiManagerGH.initSignalList(10, wifiId);
                        ArrayList<WifiSignalModel> signalModels = wifiManagerGH.getSignalList();
                        String array = JSONArray.toJSON(signalModels).toString();

                        Map<String,String> header = new HashMap<>();
                        header.put("Content-Type","application/json; charset=UTF-8");
                        header.put("accept","application/json");
                        new AxiosGH(header).post(global.getServerUrl() + "/signal/insert",array, new AxiosGH.Callback() {
                            @Override
                            public void onSuccess(String res) {
                                if(json.getBoolean("isUpdata")){
                                    Toast.makeText(getContext(),"更新成功！",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(),"创建成功！",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailed(String err) {
                                Toast.makeText(getContext(),"采集失败！",Toast.LENGTH_SHORT);
                            }
                        });



                    }

                    @Override
                    public void onFailed(String err) {
                        System.out.println(err);
                    }
                });
            }
        });
    }

    private void getMapList() {
        new AxiosGH().get(global.getServerUrl() + "/map/query", new AxiosGH.Callback() {
            @Override
            public void onSuccess(String res) {
                JSONObject json = JSONObject.parseObject(res);
                JSONArray jmodels = json.getJSONArray("list");
                ArrayList<MapModel> mapModels = new ArrayList<>();
                ArrayList<String> listText = new ArrayList<>();
                for (int i = 0; i < jmodels.size(); i++) {
                    MapModel model = JSONObject.toJavaObject(jmodels.getJSONObject(i), MapModel.class);
                    mapModels.add(model);
                    listText.add(model.getMapName());
                }
                // 选择地图下拉框
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, listText);
//                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                binding.mapsList.setAdapter(adapter);
//                binding.mapsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        selectedMap = mapModels.get(position);
//                        if (selectedMap.getMapUrl() != null) {
//                            GetBitMap.getHttpBitmap(selectedMap.getMapUrl(), new GetBitMap.Callback() {
//                                @Override
//                                public void onSuccess(Bitmap bitmap) {
//                                    binding.siteImg.setImageBitmap(bitmap);
//                                    binding.setShowView(View.VISIBLE);
//                                    getSiteList(selectedMap.getMapId());
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//                });
            }

            @Override
            public void onFailed(String err) {
                System.out.println(err);
            }
        });
    }

    private void getSiteList(int mapId) {
        new AxiosGH().get(global.getServerUrl() + "/site/select?siteMapId=" + mapId, new AxiosGH.Callback() {
            @Override
            public void onSuccess(String res) {
                JSONObject jsonObject = JSONObject.parseObject(res);
                JSONArray objects = jsonObject.getJSONArray("list");
                ArrayList<SiteModel> siteModels = new ArrayList<>();
                ArrayList<String> strings = new ArrayList<>();
                for (int i=0;i<objects.size();i++){
                    SiteModel model = JSONObject.toJavaObject(objects.getJSONObject(i),SiteModel.class);
                    siteModels.add(model);
                    strings.add(model.getSiteName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,strings);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                binding.siteList.setAdapter(adapter);
                binding.siteList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSite = siteModels.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailed(String err) {
                System.out.println(err);
            }
        });
    }
}