package com.quick.freshfoodgo.network.repository

import com.quick.freshfoodgo.bean.FoodDetailBean
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.network.ApiException
import com.quick.freshfoodgo.network.ApiService
import com.quick.freshfoodgo.network.MyRetrofit
import com.quick.freshfoodgo.network.cloudbase.HomeFeedData
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class FruitRepo(
    private val api: ApiService = MyRetrofit.apiService
) {

    suspend fun getHomeFeed(): Result<HomeFeedData> {
        return try {
            val offers = api.getHomeOffers()
            val bestSellers = api.getBestSellers()
            Result.success(
                HomeFeedData(
                    updatedAt = null,
                    todayOffers = offers,
                    bestSellers = bestSellers
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiException) {
            Result.failure(e)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401 -> "登录已过期，请重新登录"
                403 -> "当前账号无权读取首页数据"
                else -> "首页接口请求失败（HTTP ${e.code()}）"
            }
            Result.failure(ApiException(statusCode = e.code(), message = message, cause = e))
        } catch (e: IOException) {
            Result.failure(ApiException(message = "网络连接失败，请检查网络后重试", cause = e))
        } catch (e: Exception) {
            Result.failure(ApiException(message = e.message ?: "首页数据加载失败", cause = e))
        }
    }

    fun toFruitBeans(data: HomeFeedData, todayOffers: Boolean): List<FruitBean> {
        val source = if (todayOffers) data.todayOffers else data.bestSellers
        return source
            .filter { it.active }
            .sortedBy { it.sortOrder }
            .map { offer ->
                FruitBean(
                    type = offer.name,
                    des = offer.description,
                    price = String.format(Locale.CHINA, "%.2f", offer.price),
                    id = offer.foodId,
                    imageKey = offer.imageKey,
                    imageUrl = offer.imageUrl,
                    originalPrice = offer.originalPrice?.let {
                        String.format(Locale.CHINA, "%.2f", it)
                    },
                    unit = offer.unit,
                    badge = offer.badge,
                    stock = offer.stock
                )
            }
    }

    suspend fun getFoodDetail(foodId: String): Result<FoodDetailBean> {
        return try {
            val offer = api.getFoodOffer(foodId = "eq.$foodId")
                .firstOrNull()
                ?: api.getBestSellerOffer(foodId = "eq.$foodId").firstOrNull()
                ?: api.getCategoryProductOffer(foodId = "eq.$foodId").firstOrNull()
                ?: throw ApiException(message = "商品不存在或已下架")

            Result.success(
                FoodDetailBean(
                    foodId = offer.foodId,
                    name = offer.name,
                    description = offer.description,
                    price = String.format(Locale.CHINA, "%.2f", offer.price),
                    imageUrl = offer.imageUrl
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiException) {
            Result.failure(e)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401 -> "登录已过期，请重新登录"
                403 -> "当前账号无权读取商品详情"
                404 -> "商品不存在或已下架"
                else -> "商品详情加载失败（HTTP ${e.code()}）"
            }
            Result.failure(ApiException(statusCode = e.code(), message = message, cause = e))
        } catch (e: IOException) {
            Result.failure(ApiException(message = "网络连接失败，请检查网络后重试", cause = e))
        } catch (e: Exception) {
            Result.failure(ApiException(message = e.message ?: "商品详情加载失败", cause = e))
        }
    }
}
