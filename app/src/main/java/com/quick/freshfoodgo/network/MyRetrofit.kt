package com.quick.freshfoodgo.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * 提高请求接口实例
 */
class MyRetrofit {

    // 你的后端基础地址，末尾必须带 /
    private  val BASE_URL = "https://xxx.xxx.com/api/"

    // OkHttp
    private val okHttpClient: OkHttpClient by lazy {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            // 这里可以加统一请求头拦截器（token）后面给你写
            .build()
    }

    // Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 对外获取ApiService
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}