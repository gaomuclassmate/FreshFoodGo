package com.quick.freshfoodgo.network

/**
 * 已转换过的网络异常，message 可以直接展示给用户。
 */
class ApiException(
    val statusCode: Int? = null,
    override val message: String,
    cause: Throwable? = null
) : Exception(message, cause)
