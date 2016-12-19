package com.example.dbaccess;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<HistoryModel> getHistory(@Field("UserId") int id); //spremiti svaki dohvaćeni objekt u listu!




}
