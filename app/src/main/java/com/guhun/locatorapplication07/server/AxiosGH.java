package com.guhun.locatorapplication07.server;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// 仿造axios书写的http请求——————GuHun
public class AxiosGH {
    private String requestMethod;
    private boolean useCaches;
    private String serverUrl;
    private int connectTimeout;
    private Map<String,String> header;
    // 初始化实例
    public AxiosGH(){
        init();
    }
    // 初始化实例
    public AxiosGH(Map<String,String> header){
        init();
        this.header = header;
    }
    // 默认为get请求(地址,回调函数)
    public AxiosGH(String url,Callback callback){
        init();
        serverUrl = url;
        handleData(null,callback);
    }
    // get请求(地址,回调函数)
    public void get(String url,Callback callback){
        serverUrl = url;
        handleData(null,callback);
    }
    // get请求(地址,map参数,回调函数)
    public void get(String url,Map<String,Object> params,Callback callback){
        serverUrl = url;

        handleData(paramsMapToString(params),callback);
    }
    // get请求(地址,object参数,回调函数)
    public void get(String url,Object params,Callback callback) {
        serverUrl = url;
        handleData(paramsJavaToString(params),callback);
    }
    // get请求(地址,字符串参数,回调函数)不用加?了
    public void get(String url,String params,Callback callback) {
        serverUrl = url;
        handleData(params,callback);
    }
    // post请求(地址,回调函数)
    public void post(String url,Callback callback){
        requestMethod = "POST";
        serverUrl = url;
        handleData(null,callback);
    }
    // post请求(地址,map参数,回调函数)
    public void post(String url,Map<String,Object> params,Callback callback){
        requestMethod = "POST";
        serverUrl = url;
        handleData(paramsMapToString(params),callback);
    }
    // post请求(地址,javaObject参数,回调函数)
    public void post(String url,Object params,Callback callback) {
        requestMethod = "POST";
        serverUrl = url;
        handleData(paramsJavaToString(params),callback);
    }
    /**
    * @Description: params是json字符串
    * @Param: post请求(地址,jsonArray参数,回调函数)
    * @return: void
    * @Author: GuHun
    * @Date: 2021/3/19
    */
    public void post(String url, String params, Callback callback) {
        requestMethod = "POST";
        serverUrl = url;
        handleData(params,callback);
    }
    // 初始化参数
    private void init(){
        requestMethod = "GET";
        useCaches = false;
        connectTimeout = 3000;
        header = new HashMap<>();
    }

    //handle中操作页面，以及创建线程
    private void handleData(String params, Callback callback){
        @SuppressLint("HandlerLeak")
        android.os.Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                callback.onSuccess((String) msg.obj);
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run() {
                httpRequest(params,callback,handler);
            }
        }).start();
    }
    // paramsobjectToMap
    public static Map<String, Object> paramsJavaToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            map.put(fieldName, value);
        }
        return map;
    }
    // paramsMapToString
    public static String paramsMapToString(Map<String, Object> params){
        String data = null;
        if (params!=null) {
            StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
            try {
                for(Map.Entry<String, Object> entry : params.entrySet()) {
                    stringBuffer.append(entry.getKey())
                            .append("=")
                            .append(Uri.encode(entry.getValue().toString(), "utf-8"))
                            .append("&");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
            } catch (Exception e) {
                e.printStackTrace();
            }
             data = stringBuffer.toString();
        }
        return data;
    }
    // paramsJavaToString
    public static String paramsJavaToString(Object params){
        String data = null;
        if(params!=null){
            data = paramsMapToString(paramsJavaToMap(params));
        }
        return data;
    }

    //HttpUtils.submitPostData(params, "utf-8")
    private void httpRequest(String data, Callback callback, Handler handler){
        try {
            if(requestMethod.equals("GET") && data != null){
                serverUrl += '?'+data;
            }
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);//方式
            connection.setConnectTimeout(connectTimeout);//超时
            if(requestMethod.equals("POST")){
                connection.setDoInput(true);//向服务器输入
                connection.setDoOutput(true);//向服务器输出
                connection.setUseCaches(useCaches);//缓存
                //设置请求体的类型是文本类型
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                connection.setRequestProperty("Content-Type", "application/json");
                //设置请求体的长度
                connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                if (header.get("Content-Type")!=null){
                    connection.setRequestProperty("Content-Type",header.get("Content-Type"));
                }
                if (header.get("accept")!=null){
                    connection.setRequestProperty("accept",header.get("accept"));
                }
                //获得输出流，向服务器写入数据
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data.getBytes());
            }
            int response = connection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = connection.getInputStream();
                Message message = new Message();
                message.obj = dealResponseResult(inptStream);       //处理服务器的响应结果
                handler.sendMessage(message);
            }else {
                callback.onFailed("Error: response is" + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 博客园-依旧淡然
    private static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
    // 回调函数
    public interface Callback{
        void onSuccess(String res);
        void onFailed(String err);
    }

    // 请求方式
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    // 使用缓存
    public void setUseCaches(boolean useCaches) {
        this.useCaches = useCaches;
    }
    // 请求地址
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    // 请求参数
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
