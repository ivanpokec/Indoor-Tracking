package com.example.dbaccess;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Paula on 14.11.2016..
 */

public interface RetrofitConnection {
    String BASE_URL = "http://development.mobilisis.hr/";

    class Factory{
        private static ApiEndpoint service;
        public static ApiEndpoint getInstance(){
            if(service==null){
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(ApiEndpoint.class);
                return service;
            }
            else{
                return service;
            }
        }
    }
}
