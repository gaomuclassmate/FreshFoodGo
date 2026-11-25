package com.quick.freshfoodgo.model

/**
 * 登录成功后，Repository 向业务层返回的数据。
 * token 属于数据层细节，由 Repository 保存，不向 ViewModel 暴露。
 */
data class AuthSession(
    val userId: String,
    val email: String? = null,
    val username: String? = null
)
