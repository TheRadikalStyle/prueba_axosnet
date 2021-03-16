package com.davidochoa.retrofit.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RecibosServices {
    @GET("getbyid?")
    Call<String> getByID(@Query("id") int id);

    @GET("getall")
    Call<String> GetAll();

    @POST("insert?")
    Call<String> insert(
            @Query("provider") String provider,
            @Query("amount") float amonut,
            @Query("comment") String comment,
            @Query("emission_date") String emission_date,
            @Query("currency_code") String currency_code
    );


    @POST("delete?")
    Call<String> deleteByID(@Query("id") int id);


    @POST("update?")
    Call<String> update(
            @Query("id") int id,
            @Query("provider") String provider,
            @Query("amount") float amonut,
            @Query("comment") String comment,
            @Query("emission_date") String emission_date,
            @Query("currency_code") String currency_code
    );
}
