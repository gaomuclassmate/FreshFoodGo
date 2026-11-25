package com.quick.freshfoodgo.network.cloudbase

import com.google.gson.annotations.SerializedName

data class HomeFeedData(
    val updatedAt: String? = null,
    val todayOffers: List<HomeOfferDto> = emptyList(),
    val bestSellers: List<HomeOfferDto> = emptyList()
)

data class HomeOfferDto(
    @SerializedName("food_id")
    val foodId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    @SerializedName("original_price")
    val originalPrice: Double? = null,
    val unit: String = "",
    @SerializedName("image_key")
    val imageKey: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    val badge: String = "",
    val stock: Int = 0,
    @SerializedName("sales_count")
    val salesCount: Int = 0,
    @SerializedName("sort_order")
    val sortOrder: Int = 0,
    @SerializedName("is_active")
    val active: Boolean = true
)

data class HomeBannerDto(
    @SerializedName("banner_id")
    val bannerId: String = "",
    val title: String = "",
    val subtitle: String = "",
    @SerializedName("image_key")
    val imageKey: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("target_type")
    val targetType: String = "NONE",
    @SerializedName("target_value")
    val targetValue: String = "",
    @SerializedName("sort_order")
    val sortOrder: Int = 0
)

data class ProductCategoryDto(
    @SerializedName("category_id")
    val categoryId: String = "",
    val name: String = "",
    @SerializedName("image_key")
    val imageKey: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("bg_color")
    val bgColor: String = "#F2F3F2",
    @SerializedName("sort_order")
    val sortOrder: Int = 0
)
