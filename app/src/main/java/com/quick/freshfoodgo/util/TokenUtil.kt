package com.quick.freshfoodgo.util

import android.util.Log
import com.quick.freshfoodgo.network.cloudbase.CloudBaseAuthResponse
import com.tencent.mmkv.MMKV
import java.util.UUID

object TokenUtil {

    private val kv = MMKV.defaultMMKV()

    init {
        // A Supabase token left by an older app version must never be sent to CloudBase.
        kv.removeValueForKey("access_token")
        kv.removeValueForKey("refresh_token")
        kv.removeValueForKey("expiresAt")
        kv.removeValueForKey("expires_at")
        kv.removeValueForKey("user_id")
    }

    fun saveToken(response: CloudBaseAuthResponse) {
        val expiresAt = System.currentTimeMillis() +
                (response.expiresIn ?: 0L) * 1000L

        kv.encode("cloudbase_access_token", response.accessToken)
        kv.encode("cloudbase_refresh_token", response.refreshToken)
        kv.encode("cloudbase_expires_at", expiresAt)
        kv.encode("cloudbase_user_id", response.sub)
    }

    fun isAccessTokenExpired(): Boolean {
        val token = getAccessToken()
        return token.isNullOrBlank() ||
                System.currentTimeMillis() >= kv.decodeLong("cloudbase_expires_at")
    }

    fun hasSession(): Boolean {
        return !getAccessToken().isNullOrBlank() ||
                !getRefreshToken().isNullOrBlank()
    }

    fun getAccessToken(): String? = kv.decodeString("cloudbase_access_token")

    fun getRefreshToken(): String? = kv.decodeString("cloudbase_refresh_token")

    fun getUserId(): String? = kv.decodeString("cloudbase_user_id")

    fun getDeviceId(): String {
        val existing = kv.decodeString("cloudbase_device_id")
        if (!existing.isNullOrBlank()) {
            return existing
        }

        val newDeviceId = UUID.randomUUID().toString()
        kv.encode("cloudbase_device_id", newDeviceId)

        return newDeviceId
    }

    fun clear() {
        kv.removeValueForKey("cloudbase_access_token")
        kv.removeValueForKey("cloudbase_refresh_token")
        kv.removeValueForKey("cloudbase_expires_at")
        kv.removeValueForKey("cloudbase_user_id")
    }
}
