package com.dilipsuthar.smssendapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("sendhttp.php")
    fun sendSMS(@Query("mobiles") mobiles: String,
                @Query("authkey") authkey: String,
                @Query("route") route: String,
                @Query("sender") sender: String,
                @Query("message") message: String,
                @Query("country") country: String): Call<String>

}