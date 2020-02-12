package com.dilipsuthar.smssendapi

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service(val context: Context) {

    private val BASE_URL = "https://api.msg91.com/api/"
    private val TAG = "debug"

    fun requestSms(mobileNum: String, message: String, route: String, senderId: String) {
        // Route >> 1 for Promotional, 4 for Transactional
        val smsRequest = buildApi(buildClient(), Api::class.java)
            .sendSMS(mobileNum, BuildConfig.AUTH_API, route, senderId, message, "91")

        smsRequest.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d(TAG, response.body().toString())
                    Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private fun <T> buildApi(client: OkHttpClient, api: Class<T>): T {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(api)
    }

    companion object {

        fun getService(context: Context): Service {
            return Service(context)
        }
    }

}