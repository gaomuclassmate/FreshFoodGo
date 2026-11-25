package com.quick.freshfoodgo.network.repository

import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.network.ApiException
import com.quick.freshfoodgo.network.ApiService
import com.quick.freshfoodgo.network.MyRetrofit
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class CategoryProductRepo(
    private val api: ApiService = MyRetrofit.apiService
) {

    suspend fun getProducts(categoryId: String): Result<List<FruitBean>> {
        return try {
            val products = api.getCategoryProducts(categoryId = "eq.$categoryId")
                .sortedBy { it.sortOrder }
                .map { product ->
                    FruitBean(
                        type = product.name,
                        des = product.description,
                        price = String.format(Locale.CHINA, "%.2f", product.price),
                        id = product.foodId,
                        imageKey = product.imageKey,
                        imageUrl = product.imageUrl,
                        originalPrice = product.originalPrice?.let {
                            String.format(Locale.CHINA, "%.2f", it)
                        },
                        unit = product.unit,
                        badge = product.badge,
                        stock = product.stock
                    )
                }
            Result.success(products)
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401 -> "登录已过期，请重新登录"
                403 -> "当前账号无权读取分类商品"
                else -> "分类商品请求失败（HTTP ${e.code()}）"
            }
            Result.failure(ApiException(statusCode = e.code(), message = message, cause = e))
        } catch (e: IOException) {
            Result.failure(ApiException(message = "网络连接失败，请检查网络后重试", cause = e))
        } catch (e: Exception) {
            Result.failure(ApiException(message = e.message ?: "分类商品加载失败", cause = e))
        }
    }
}
