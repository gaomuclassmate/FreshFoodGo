package com.quick.freshfoodgo.network.repository

import com.quick.freshfoodgo.model.AuthSession
import com.quick.freshfoodgo.network.ApiException
import com.quick.freshfoodgo.network.ApiService
import com.quick.freshfoodgo.network.MyRetrofit
import com.quick.freshfoodgo.network.cloudbase.CloudBaseAuthResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseConfig
import com.quick.freshfoodgo.network.cloudbase.CloudBaseRefreshTokenRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseSignInRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseSignUpRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseUser
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerificationRequest
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerificationResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerificationTokenResponse
import com.quick.freshfoodgo.network.cloudbase.CloudBaseVerifyCodeRequest
import com.quick.freshfoodgo.util.TokenUtil
import kotlinx.coroutines.CancellationException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class AuthRepo(
    private val api: ApiService = MyRetrofit.apiService
) {

    suspend fun login(
        username: String,
        password: String
    ): Result<AuthSession> = execute {
        val response = api.signIn(
            CloudBaseSignInRequest(
                username = username.trim(),
                password = password
            )
        )
        val session = response.toSession(username.trim())
        TokenUtil.saveToken(response)
        session
    }

    suspend fun refreshSession(): Result<AuthSession> {
        val refreshToken = TokenUtil.getRefreshToken()
            ?: return Result.failure(ApiException(message = "登录已过期，请重新登录"))

        return execute {
            val response = api.refreshToken(
                CloudBaseRefreshTokenRequest(
                    refreshToken = refreshToken,
                    clientId = CloudBaseConfig.envId
                )
            )
            val session = response.toSession()
            TokenUtil.saveToken(response)
            session
        }
    }

    suspend fun registerWithVerificationCode(
        email: String,
        password: String,
        verificationToken: String,
        username: String? = null
    ): Result<AuthSession> {
        return execute {
            val response = api.signUp(
                CloudBaseSignUpRequest(
                    email = email.trim(),
                    username = username?.trim()?.takeIf { it.isNotBlank() },
                    password = password,
                    verificationToken = verificationToken
                )
            )
            val session = response.toSession(username?.trim())
            TokenUtil.saveToken(response)
            session
        }
    }

    suspend fun sendEmailVerification(
        email: String
    ): Result<CloudBaseVerificationResponse> = execute {
        val response = api.sendEmailVerification(
            CloudBaseVerificationRequest(email = email.trim())
        )
        if (response.verificationId.isNullOrBlank()) {
            throw ApiException(message = "验证码发送成功，但服务器未返回验证码 ID")
        }
        response
    }

    suspend fun verifyEmailCode(
        verificationId: String,
        code: String
    ): Result<CloudBaseVerificationTokenResponse> = execute {
        val response = api.verifyEmailCode(
            CloudBaseVerifyCodeRequest(
                verificationId = verificationId,
                verificationCode = code.trim()
            )
        )
        if (response.verificationToken.isNullOrBlank()) {
            throw ApiException(message = "验证码校验成功，但服务器未返回验证 Token")
        }
        response
    }

    suspend fun registerApi(
        email: String,
        password: String
    ): Result<AuthSession> {
        return Result.failure(
            ApiException(
                message = "CloudBase 注册需要邮箱或手机号验证码，当前注册页尚未接入验证码流程"
            )
        )
    }

    suspend fun getCurrentUser(): Result<CloudBaseUser> {
        if (TokenUtil.isAccessTokenExpired()) {
            val refreshResult = refreshSession()
            if (refreshResult.isFailure) {
                return Result.failure(
                    refreshResult.exceptionOrNull()
                        ?: ApiException(message = "登录已过期，请重新登录")
                )
            }
        }

        return execute { api.getCurrentUser() }
    }

    suspend fun logout(): Result<Unit> {
        if (TokenUtil.hasSession()) {
            try {
                api.signOut(emptyMap())
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                // Local session cleanup must still complete when the network is unavailable.
            }
        }
        TokenUtil.clear()
        return Result.success(Unit)
    }

    fun getUserId(): String? = TokenUtil.getUserId()

    fun isLoggedIn(): Boolean = TokenUtil.hasSession()

    private fun CloudBaseAuthResponse.toSession(username: String? = null): AuthSession {
        if (accessToken.isNullOrBlank()) {
            throw ApiException(message = "登录响应缺少 access token")
        }

        val userId = sub?.takeIf { it.isNotBlank() }
            ?: throw ApiException(message = "登录响应缺少用户 ID")

        return AuthSession(
            userId = userId,
            username = username
        )
    }

    private suspend fun <T> execute(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e.toApiException())
        } catch (e: IOException) {
            Result.failure(
                ApiException(
                    message = "网络连接失败，请检查网络后重试",
                    cause = e
                )
            )
        } catch (e: Exception) {
            Result.failure(
                ApiException(
                    message = e.message ?: "请求失败，请稍后重试",
                    cause = e
                )
            )
        }
    }

    private fun HttpException.toApiException(): ApiException {
        val body = response()?.errorBody()?.string()
        val message = body?.let(::parseErrorMessage)
            ?: "请求失败（HTTP ${code()}）"

        return ApiException(
            statusCode = code(),
            message = message,
            cause = this
        )
    }

    private fun parseErrorMessage(body: String): String? {
        return runCatching {
            val json = JSONObject(body)
            when (json.optString("error").lowercase()) {
                "invalid_username_or_password" -> "账号或密码错误"
                "captcha_required" -> "登录失败次数过多，请完成验证码后重试"
                "password_not_set" -> "该账号尚未设置密码"
                "invalid_status" -> "账号暂时不可用，请稍后重试"
                "username login is disabled by admin" -> "当前环境未开启用户名密码登录"
                "invalid_email" -> "邮箱格式不正确"
                "invalid_argument" -> "请求参数格式不正确"
                "rate_limit_exceeded" -> "验证码发送过于频繁，请稍后重试"
                "invalid_verification_code" -> "验证码错误，请重新输入"
                "invalid_verification_id",
                "verification_not_found" -> "验证码已失效，请重新获取"
                "user_already_exists" -> "该邮箱已经注册，请直接登录"
                else -> {
                    listOf("error_description", "message", "error")
                        .map { key -> json.optString(key) }
                        .firstOrNull { it.isNotBlank() }
                }
            }
        }.getOrNull()
    }
}
