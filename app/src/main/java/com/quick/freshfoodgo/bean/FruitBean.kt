package com.quick.freshfoodgo.bean

data class FruitBean(
    val type: String = "",
    val des: String = "",
    val price: String = "",
    val imgRes: Int = 0,
    val id: String = "",
    val imageKey: String = "",
    val imageUrl: String = "",
    val originalPrice: String? = null,
    val unit: String = "",
    val badge: String = "",
    val stock: Int = 0
)
