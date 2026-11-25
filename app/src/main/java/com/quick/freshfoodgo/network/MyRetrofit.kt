package com.quick.freshfoodgo.network

import com.quick.freshfoodgo.BuildConfig
import com.quick.freshfoodgo.network.cloudbase.CloudBaseConfig
import com.quick.freshfoodgo.util.TokenUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MyRetrofit {

    private val publicAuthPaths = setOf(
        "/auth/v1/signin",
        "/auth/v1/signup",
        "/auth/v1/token",
        "/auth/v1/verification",
        "/auth/v1/verification/verify"
    )

    private val publicReadPaths = setOf(
        "/v1/rdb/rest/home_offers",
        "/v1/rdb/rest/best_sellers",
        "/v1/rdb/rest/home_banners",
        "/v1/rdb/rest/product_categories",
        "/v1/rdb/rest/category_products"
    )

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = if (
                    originalRequest.url.encodedPath in publicAuthPaths ||
                    originalRequest.url.encodedPath in publicReadPaths
                ) {
                    CloudBaseConfig.publishableKey
                } else {
                    TokenUtil.getAccessToken()
                        ?.takeIf { it.isNotBlank() }
                        ?: CloudBaseConfig.publishableKey
                }

                val request = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .header("x-device-id", TokenUtil.getDeviceId())
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CloudBaseConfig.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
