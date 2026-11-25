package com.quick.freshfoodgo.network.cloudbase

import com.google.gson.annotations.SerializedName

data class CloudBaseSignInRequest(
    val username: String,
    val password: String
)

data class CloudBaseRefreshTokenRequest(
    @SerializedName("grant_type")
    val grantType: String = "refresh_token",
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("client_id")
    val clientId: String? = null
)

data class CloudBaseSignUpRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    @SerializedName("verification_token")
    val verificationToken: String? = null
)

data class CloudBaseAuthResponse(
    @SerializedName("token_type")
    val tokenType: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long? = null,
    val sub: String? = null
)

data class CloudBaseUser(
    val sub: String? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    val status: String? = null
)

data class CloudBaseSignOutResponse(
    @SerializedName("redirect_uri")
    val redirectUri: String? = null
)

data class CloudBaseVerificationRequest(
    val target: String = "ANY",
    val email: String
)

data class CloudBaseVerificationResponse(
    @SerializedName("verification_id")
    val verificationId: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long? = null,
    @SerializedName("is_user")
    val isUser: Boolean? = null
)

data class CloudBaseVerifyCodeRequest(
    @SerializedName("verification_id")
    val verificationId: String,
    @SerializedName("verification_code")
    val verificationCode: String
)

data class CloudBaseVerificationTokenResponse(
    @SerializedName("verification_token")
    val verificationToken: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long? = null
)
