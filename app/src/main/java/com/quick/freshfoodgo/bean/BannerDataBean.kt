package com.quick.freshfoodgo.bean

data class BannerDataBean(
    val bannerId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val imageUrl: String = "",
    val targetType: String = "NONE",
    val targetValue: String = ""
)
