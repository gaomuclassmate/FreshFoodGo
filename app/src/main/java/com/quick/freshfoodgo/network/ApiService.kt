package com.quick.freshfoodgo.network

import com.quick.freshfoodgo.network.cloudbase.CloudBaseAuthResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseRefreshTokenRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseSignInRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseSignUpRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseSignOutResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseUser
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerificationRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerificationResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerificationTokenResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerifyCodeRequest
import com.quick.freshfoodgo.network.cloudbase.HomeBannerDto
import com.quick.freshfoodgo.network.cloudbase.HomeOfferDto
import com.quick.freshfoodgo.network.cloudbase.ProductCategoryDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/v1/signin")
    suspend fun signIn(
        @Body request: CloudBaseSignInRequest
    ): CloudBaseAuthResponse

    @POST("auth/v1/signup")
    suspend fun signUp(
        @Body request: CloudBaseSignUpRequest
    ): CloudBaseAuthResponse

    @POST("auth/v1/token")
    suspend fun refreshToken(
        @Body request: CloudBaseRefreshTokenRequest
    ): CloudBaseAuthResponse

    @POST("auth/v1/user/signout")
    suspend fun signOut(
        @Body request: Map<String, String> = emptyMap()
    ): Response<CloudBaseSignOutResponse>

    @GET("auth/v1/user/me")
    suspend fun getCurrentUser(): CloudBaseUser

    @POST("auth/v1/verification")
    suspend fun sendEmailVerification(
        @Body request: CloudBaseVerificationRequest
    ): CloudBaseVerificationResponse

    @POST("auth/v1/verification/verify")
    suspend fun verifyEmailCode(
        @Body request: CloudBaseVerifyCodeRequest
    ): CloudBaseVerificationTokenResponse

    @GET("v1/rdb/rest/home_offers")
    suspend fun getHomeOffers(
        @Query("select") select: String = "*",
        @Query("is_active") isActive: String = "eq.true",
        @Query("order") order: String = "sort_order.asc"
    ): List<HomeOfferDto>

    @GET("v1/rdb/rest/home_offers")
    suspend fun getFoodOffer(
        @Query("select") select: String = "*",
        @Query("food_id") foodId: String,
        @Query("is_active") isActive: String = "eq.true",
        @Query("limit") limit: Int = 1
    ): List<HomeOfferDto>

    @GET("v1/rdb/rest/best_sellers")
    suspend fun getBestSellers(
        @Query("select") select: String = "*",
        @Query("is_active") isActive: String = "eq.true",
        @Query("order") order: String = "sales_count.desc,sort_order.asc"
    ): List<HomeOfferDto>

    @GET("v1/rdb/rest/best_sellers")
    suspend fun getBestSellerOffer(
        @Query("select") select: String = "*",
        @Query("food_id") foodId: String,
        @Query("is_active") isActive: String = "eq.true",
        @Query("limit") limit: Int = 1
    ): List<HomeOfferDto>

    @GET("v1/rdb/rest/home_banners")
    suspend fun getHomeBanners(
        @Query("select") select: String = "*",
        @Query("order") order: String = "sort_order.asc"
    ): List<HomeBannerDto>

    @GET("v1/rdb/rest/product_categories")
    suspend fun getProductCategories(
        @Query("select") select: String = "*",
        @Query("order") order: String = "sort_order.asc"
    ): List<ProductCategoryDto>

    @GET("v1/rdb/rest/category_products")
    suspend fun getCategoryProducts(
        @Query("select") select: String = "*",
        @Query("category_id") categoryId: String,
        @Query("is_active") isActive: String = "eq.true",
        @Query("order") order: String = "sort_order.asc"
    ): List<HomeOfferDto>

    @GET("v1/rdb/rest/category_products")
    suspend fun getCategoryProductOffer(
        @Query("select") select: String = "*",
        @Query("food_id") foodId: String,
        @Query("is_active") isActive: String = "eq.true",
        @Query("limit") limit: Int = 1
    ): List<HomeOfferDto>
}
