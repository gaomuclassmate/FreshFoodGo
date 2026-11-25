package com.quick.freshfoodgo.network.repository

import com.quick.freshfoodgo.bean.ProductClassify
import com.quick.freshfoodgo.network.ApiException
import com.quick.freshfoodgo.network.ApiService
import com.quick.freshfoodgo.network.MyRetrofit
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

class CategoryRepo(
    private val api: ApiService = MyRetrofit.apiService
) {

    suspend fun getCategories(): Result<List<ProductClassify>> {
        return try {
            val categories = api.getProductCategories()
                .sortedBy { it.sortOrder }
                .map { category ->
                    //map把list都存放着ProductClassify
                    ProductClassify(
                        categoryId = category.categoryId,
                        name = category.name,
                        imageKey = category.imageKey,
                        imageUrl = category.imageUrl,
                        bgColor = category.bgColor
                    )
                }
            Result.success(categories)
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401 -> "登录已过期，请重新登录"
                403 -> "当前账号无权读取商品分类"
                else -> "商品分类请求失败（HTTP ${e.code()}）"
            }
            Result.failure(ApiException(statusCode = e.code(), message = message, cause = e))
        } catch (e: IOException) {
            Result.failure(ApiException(message = "网络连接失败，请检查网络后重试", cause = e))
        } catch (e: Exception) {
            Result.failure(ApiException(message = e.message ?: "商品分类加载失败", cause = e))
        }
    }
}
