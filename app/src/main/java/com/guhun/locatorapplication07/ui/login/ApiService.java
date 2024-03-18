package com.guhun.locatorapplication07.ui.login;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/user/login") // 这里的路径需要根据你的实际情况进行修改
    Call<String> login(@Body RequestBody params);
}

