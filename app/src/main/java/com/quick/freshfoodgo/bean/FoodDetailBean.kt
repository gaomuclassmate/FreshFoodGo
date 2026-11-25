package com.quick.freshfoodgo.bean

data class FoodDetailBean(
    val foodId: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val count: Int = 0,
    val isLike: Boolean = false
)
