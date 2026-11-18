package com.quick.freshfoodgo.login.vm

import androidx.lifecycle.viewModelScope
import com.quick.freshfoodgo.base.BaseViewModel
import com.quick.freshfoodgo.network.repository.AuthRepo
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    object RegisterSuccessNeedVerify : AuthEvent()
    object RegisterSuccessAutoLogin : AuthEvent()
    data class Failed(val msg: String) : AuthEvent()
    object LogoutSuccess : AuthEvent()
}

class AuthViewModel : BaseViewModel() {

    private val repository = AuthRepo()
    override fun initViewModel() {}

    // ✅ UI状态改用 StateFlow，支持 .value
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ✅ 一次性事件 SharedFlow，专门处理登录/注册成功回调
    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent: SharedFlow<AuthEvent> = _authEvent.asSharedFlow()

    fun register(email: String, password: String) {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                message = null
            )

            repository.register(email, password)
                .onSuccess { _: UserInfo? ->
                    val userId = repository.getUserId()
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        isLoggedIn = userId != null,
                        userId = userId,
                        message = if (userId == null) {
                            "注册成功，请验证邮箱后登录"
                        } else {
                            "注册成功"
                        }
                    )
                    // 发送事件
                    if (userId == null) {
                        _authEvent.emit(AuthEvent.RegisterSuccessNeedVerify)
                    } else {
                        _authEvent.emit(AuthEvent.RegisterSuccessAutoLogin)
                    }
                }
                .onFailure { error ->
                    val errMsg = error.message ?: "注册失败"
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = errMsg
                    )
                    _authEvent.emit(AuthEvent.Failed(errMsg))
                }
        }
    }

    /**
     * 登录
     */
    fun login(email: String, password: String) {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                message = null
            )

            repository.login(email, password)
                .onSuccess {
                    val userId = repository.getUserId()
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        isLoggedIn = userId != null,
                        userId = userId,
                        message = "登录成功"
                    )
                    if (userId != null) {
                        _authEvent.emit(AuthEvent.LoginSuccess(userId))
                    } else {
                        _authEvent.emit(AuthEvent.Failed("获取用户ID失败"))
                    }
                }
                .onFailure { error ->
                    val errMsg = error.message ?: "登录失败"
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = errMsg
                    )
                    _authEvent.emit(AuthEvent.Failed(errMsg))
                }
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
                    _authEvent.emit(AuthEvent.LogoutSuccess)
                }
                .onFailure { error ->
                    val errMsg = error.message ?: "退出登录失败"
                    _uiState.value = _uiState.value.copy(
                        error = errMsg
                    )
                    _authEvent.emit(AuthEvent.Failed(errMsg))
                }
        }
    }
}
