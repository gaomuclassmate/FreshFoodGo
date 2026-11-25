package com.quick.freshfoodgo.login.vm

import androidx.lifecycle.viewModelScope
import com.quick.freshfoodgo.base.BaseViewModel
import com.quick.freshfoodgo.network.ApiException
import com.quick.freshfoodgo.network.repository.AuthRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// UI状态：常驻状态，用于页面渲染 loading / error
data class AuthUiState(
    val loading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userId: String? = null,
    val error: String? = null,
    val message: String? = null
)

// 一次性事件：只推送一次，用于跳转、弹窗，不会粘性回放
sealed class AuthEvent {
    data class LoginSuccess(val userId: String) : AuthEvent()
    data class VerificationCodeSent(
        val expiresInSeconds: Long,
        val emailExists: Boolean
    ) : AuthEvent()
    data class RegisterSuccessAutoLogin(val userId: String) : AuthEvent()
    data class Failed(val msg: String) : AuthEvent()
    object LogoutSuccess : AuthEvent()
}

class AuthViewModel : BaseViewModel() {

    private val repository = AuthRepo()
    override fun initViewModel() {}
    private var verificationId: String? = null
    private var verificationEmail: String? = null

    // ✅ UI状态改用 StateFlow，支持 .value
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // 一次性事件使用 Channel，页面暂停或旋转时不会直接丢失
    private val _authEvent = Channel<AuthEvent>(Channel.BUFFERED)
    val authEvent: Flow<AuthEvent> = _authEvent.receiveAsFlow()

    fun sendEmailVerification(email: String) {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                message = null
            )
            val event = repository.sendEmailVerification(email)
                .fold(
                    onSuccess = { response ->
                        verificationId = response.verificationId
                        verificationEmail = email.trim()
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            message = "验证码已发送"
                        )
                        AuthEvent.VerificationCodeSent(
                            expiresInSeconds = response.expiresIn ?: 600L,
                            emailExists = response.isUser == true
                        )
                    },
                    onFailure = { error ->
                        val message = error.message ?: "验证码发送失败"
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            error = message
                        )
                        AuthEvent.Failed(message)
                    }
                )
            _authEvent.send(event)
        }
    }

    fun registerWithEmailCode(
        email: String,
        password: String,
        code: String
    ) {
        if (_uiState.value.loading) return

        val normalizedEmail = email.trim()
        val currentVerificationId = verificationId
        if (currentVerificationId == null || verificationEmail != normalizedEmail) {
            viewModelScope.launch {
                _authEvent.send(AuthEvent.Failed("请先获取并验证当前邮箱的验证码"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                message = null
            )

            val verificationResult = repository.verifyEmailCode(
                verificationId = currentVerificationId,
                code = code
            )
            val registrationResult = verificationResult.fold(
                onSuccess = { verification ->
                    val verificationToken = verification.verificationToken
                    if (verificationToken.isNullOrBlank()) {
                        Result.failure(ApiException(message = "验证码校验失败，请重新获取"))
                    } else {
                        repository.registerWithVerificationCode(
                            email = normalizedEmail,
                            password = password,
                            verificationToken = verificationToken
                        )
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )

            val event = registrationResult.fold(
                onSuccess = { session ->
                    verificationId = null
                    verificationEmail = null
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        isLoggedIn = true,
                        userId = session.userId,
                        message = "注册成功"
                    )
                    AuthEvent.RegisterSuccessAutoLogin(session.userId)
                },
                onFailure = { error ->
                    val message = error.message ?: "注册失败"
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = message
                    )
                    AuthEvent.Failed(message)
                }
            )
            _authEvent.send(event)
        }
    }

    /**
     * 登录
     */
    fun login(username: String, password: String) {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                message = null
            )

            val event = repository.login(username, password)
                .fold(
                    onSuccess = { session ->
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            isLoggedIn = true,
                            userId = session.userId,
                            error = null,
                            message = "登录成功"
                        )
                        AuthEvent.LoginSuccess(session.userId)
                    },
                    onFailure = { error ->
                        val errMsg = error.message ?: "登录失败"
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            isLoggedIn = false,
                            userId = null,
                            error = errMsg
                        )
                        AuthEvent.Failed(errMsg)
                    }
                )
            _authEvent.send(event)
        }
    }

    /**
     * 退出登录
     */
    fun logout() {
        viewModelScope.launch {
            repository.logout()
                .onSuccess {
                    _uiState.value = AuthUiState()
                    _authEvent.send(AuthEvent.LogoutSuccess)
                }
                .onFailure { error ->
                    val errMsg = error.message ?: "退出登录失败"
                    _uiState.value = _uiState.value.copy(
                        error = errMsg
                    )
                    _authEvent.send(AuthEvent.Failed(errMsg))
                }
        }
    }
}
