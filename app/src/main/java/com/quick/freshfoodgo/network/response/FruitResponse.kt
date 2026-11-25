package com.quick.freshfoodgo.network.response

import com.quick.freshfoodgo.bean.FruitBean

data class FruitResponse(
    val code: Int,
    val msg: String,
    val data: List<FruitBean>
)