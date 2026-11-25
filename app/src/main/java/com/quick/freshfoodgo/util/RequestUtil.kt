package com.quick.freshfoodgo.util

object RequestUtil {
    private var lastRequestTime: Long = 0L
    private val requestInterval = 2000L // 间隔，单位ms，可自行改成500/1500

    fun canRepeatRequest(currentTime: Long): Boolean {
        return if (currentTime - lastRequestTime < requestInterval) {
            // 间隔不够 → 判定重复，拦截请求
            false
        } else {
            // 间隔足够，更新时间戳，放行
            lastRequestTime = currentTime
            true
        }
    }

}