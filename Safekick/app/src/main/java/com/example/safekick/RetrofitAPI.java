package com.example.safekick;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RetrofitAPI {
    @POST("/verify")
    Call<registerDATA> register(@Body JsonObject body);

//    @POST("/riding")
//    Call<registerDATA> register2(@Body JsonObject body);

}