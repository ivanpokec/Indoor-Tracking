package com.example.dbaccess;

import java.util.List;

import retrofit2.Call;
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

    @POST("IndoorTracking/api/userpassupdate")
    @FormUrlEncoded
    Call<UserModel> changePassword(@Field("passWord") String passWord, @Field("usrId") int id);

    @POST("IndoorTracking/api/UsersOnLocation")
    @FormUrlEncoded
    Call<List<UserLocationModel>> getCurrentUsersOnLocation(@Field("locationId") int locationId);

    @POST("IndoorTracking/api/UserLocation")
    @FormUrlEncoded
    Call<List<UserLocationModel>> getUsersOnLocation(@Field("locationId") int locationId);

    @GET("IndoorTracking/api/User")
    Call<List<UserModel>> listUsers();

    @POST("IndoorTracking/api/User")
    @FormUrlEncoded
    Call<UserModel> getUser(@Field("UserId") int UserId);



    @POST("IndoorTracking/api/History/GetDateForUser")
    @FormUrlEncoded
    Call<List<HistoryModel>> getHistory(@Field("UserId") int userId);

    @POST("IndoorTracking/api/History/GetHistoryForDateAndUser")
    @FormUrlEncoded
    Call<List<HistoryModel>> getHistoryDetails(@Field("UserId") int userId, @Field("date") String date);

    @POST("IndoorTracking/api/History/GetHistoryFromTo")
    @FormUrlEncoded
    Call<List<HistoryModel>> getHistoryByDate(@Field("UserId") int userId, @Field("dateFrom") String dateFrom, @Field("dateTo") String dateTo);

    @POST("IndoorTracking/api/History/GetHistoryForUserLocation")
    @FormUrlEncoded
    Call<List<HistoryModel>> getHistoryByLocation(@Field("UserId") int userId, @Field("locationId") int locationId);



    @POST("IndoorTracking/api/location")
    @FormUrlEncoded
    Call<LocationModel> getLocation(@Field("MacAddress") String MacAddress, @Field("UsrId") int userId);

    @GET("IndoorTracking/api/Location")
    Call<List<LocationModel>> listAllLocations();

    @POST("IndoorTracking/api/LocationInCategori")
    @FormUrlEncoded
    Call<List<LocationModel>> getLocationInCategory(@Field("catId") int catId);



    @GET("IndoorTracking/api/Category")
    Call<List<CategoryModel>> listCategories();




}
