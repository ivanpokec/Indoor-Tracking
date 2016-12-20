package com.example.dbaccess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Paula on 19.12.2016..
 */

public interface ApiEndpoint {

    @POST("IndoorTracking/api/login")
    @FormUrlEncoded
    Call<UserModel> getUser(@Field("userName") String username, @Field("passWord") String password);

    @POST("IndoorTracking/api/history")
    @FormUrlEncoded
    Call<List<HistoryModel>> getHistory(@Field("UserId") int id);

    @POST("IndoorTracking/api/location")
    @FormUrlEncoded
    Call<LocationModel> getLocation(@Field("MacAddress") String MacAddress, @Field("UsrId") int userId);




}
