package com.quick.freshfoodgo.network.repository

import com.quick.freshfoodgo.bean.BannerDataBean
import com.quick.freshfoodgo.network.ApiException
import com.quick.freshfoodgo.network.ApiService
import com.quick.freshfoodgo.network.MyRetrofit
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

class BannerRepo(
    private val api: ApiService = MyRetrofit.apiService
) {

    suspend fun getHomeBanners(): Result<List<BannerDataBean>> {
        return try {
            val banners = api.getHomeBanners()
                .sortedBy { it.sortOrder }
                .map { banner ->
                    BannerDataBean(
                        bannerId = banner.bannerId,
                        title = banner.title,
                        subtitle = banner.subtitle,
                        imageUrl = banner.imageUrl,
                        targetType = banner.targetType,
                        targetValue = banner.targetValue
                    )
                }
            Result.success(banners)
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401 -> "登录已过期，请重新登录"
                403 -> "当前账号无权读取 Banner"
                else -> "Banner 请求失败（HTTP ${e.code()}）"
            }
            Result.failure(ApiException(statusCode = e.code(), message = message, cause = e))
        } catch (e: IOException) {
            Result.failure(ApiException(message = "网络连接失败，请检查网络后重试", cause = e))
        } catch (e: Exception) {
            Result.failure(ApiException(message = e.message ?: "Banner 加载失败", cause = e))
        }
    }
}
