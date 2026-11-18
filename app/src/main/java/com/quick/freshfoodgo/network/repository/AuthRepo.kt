package com.quick.freshfoodgo.network.repository

import com.quick.freshfoodgo.network.supabase.SupabaseManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepo {

    private val auth = SupabaseManager.client.auth

    /**
     * 邮箱注册
     */
    suspend fun register(
        email: String,
        password: String
    ) = runCatching {

        auth.signUpWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    /**
     * 邮箱登录
     */
    suspend fun login(
        email: String,
        password: String
    ) = runCatching {

        auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    /**
     * 退出登录
     */
    suspend fun logout() = runCatching {
        auth.signOut()
    }

    /**
     * 获取当前用户ID
     */
    fun getUserId(): String? {
        return auth.currentUserOrNull()?.id
    }

    /**
     * 判断是否已经登录
     */
    fun isLoggedIn(): Boolean {
        return auth.currentSessionOrNull() != null
    }
}