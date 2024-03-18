package com.guhun.locatorapplication07.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.guhun.locatorapplication07.R;
import com.guhun.locatorapplication07.data.MyAppGlobal;
import com.guhun.locatorapplication07.server.AxiosGH;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {


    private static final String BASE_URL = "http://127.0.0.1:8888/locator_server";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }// 初始化

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.btn_login);
        final Button register = view.findViewById(R.id.btn_register);

//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId = usernameEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//                if (TextUtils.isEmpty(userId)) {
//                    Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                MyAppGlobal global = (MyAppGlobal) getActivity().getApplication();
//                String serverUrl = global.getServerUrl();
//
//                // 创建 Retrofit 实例
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(serverUrl)
//                        .addConverterFactory(GsonConverterFactory.create()) // 使用 ScalarsConverterFactory 处理简单的字符串响应
//                        .build();
//
//                // 创建 API 接口
//                ApiService apiService = retrofit.create(ApiService.class);
//
//                // 构建请求体
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("userId", userId)
//                        .add("userPwd", password)
//                        .build();
//
//                // 发起登录请求
//                Call<String> call = apiService.login(requestBody);
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.isSuccessful()) {
//                            String res = response.body();
//                            TextView headText;
//                            switch (res) {
//                                case "1":
//                                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//                                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_siteFragment);
//                                    global.setUserId(userId);
//                                    headText = getActivity().findViewById(R.id.headText);
//                                    headText.setText("欢迎に:\n" + userId);
//                                    global.setRight("1");
//                                    break;
//                                case "10":
//                                    Toast.makeText(getActivity(), "身份验证：管理员", Toast.LENGTH_SHORT).show();
//                                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_siteFragment);
//                                    global.setUserId(userId);
//                                    headText = getActivity().findViewById(R.id.headText);
//                                    headText.setText("管理员:\n" + userId);
//                                    global.setRight("10");
//                                    break;
//                                case "-1":
//                                    Toast.makeText(getActivity(), "用户名错误", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case "-2":
//                                    Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();
//                                    break;
//                                default:
//                                    throw new IllegalStateException("Unexpected value: " + res);
//                            }
//                        } else {
//                            // 处理请求失败的情况
//                            // 可以根据 response.code() 获取 HTTP 状态码等信息
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        // 处理请求失败的情况
//                        // 可以打印错误日志或者进行其他处理
//                        t.printStackTrace();
//                    }
//                });
//            }
//        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(textIsEmpty(userId,"用户名不能为空")) return;
                if(textIsEmpty(password,"密码不能为空")) return;

                MyAppGlobal global = (MyAppGlobal) getActivity().getApplication();
                String serverUrl = global.getServerUrl();
                Map<String,Object> params = new HashMap<String, Object>();
                params.put("userId", userId);
                params.put("userPwd", password);
                new AxiosGH().post(serverUrl + "/user/login", params, new AxiosGH.Callback() {
                    @Override
                    public void onSuccess(String res) {
                        TextView headText;
                        switch (res){
                            case "1":Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_siteFragment);
                                global.setUserId(userId);
                                headText = getActivity().findViewById(R.id.headText);
                                headText.setText("欢迎に:\n" + userId);
                                global.setRight("1");
                                break;
                            case "10":Toast.makeText(getActivity(),"身份验证：管理员",Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_siteFragment);
                                global.setUserId(userId);
                                headText = getActivity().findViewById(R.id.headText);
                                headText.setText("管理员:\n" + userId);
                                global.setRight("10");
                                break;
                            case "-1":Toast.makeText(getActivity(),"用户名错误",Toast.LENGTH_SHORT).show();break;
                            case "-2":Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + res);
                        }
                    }
                    @Override
                    public void onFailed(String err) {
                        System.out.println(err);
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
    }

    // 判断字符串是否为空及进行提示——————GuHun
    public boolean textIsEmpty(String text, String tip) {
        if (text.equals("") || text == null) {
            Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}