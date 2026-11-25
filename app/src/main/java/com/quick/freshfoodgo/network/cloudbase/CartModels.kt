package com.quick.freshfoodgo.network.cloudbase

import com.google.gson.annotations.SerializedName

data class AddProductCarRequest(
    @SerializedName("food_id")
    val foodId: String,
    val quantity: Int = 1
)
