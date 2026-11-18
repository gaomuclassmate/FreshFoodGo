package com.quick.freshfoodgo.network

import com.quick.freshfoodgo.database.FruitResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    suspend fun login()

    // 示例：获取水果列表
    @GET("fruit/list")
    suspend fun getFruitList(): Response<FruitResponse>

    // 带参数示例
    @GET("fruit/search")
    suspend fun searchFruit(@Query("keyword") keyword: String): Response<FruitResponse>
}