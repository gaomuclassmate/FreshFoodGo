package com.quick.freshfoodgo.network.cloudbase

import com.quick.freshfoodgo.BuildConfig

object CloudBaseConfig {
    val envId: String = BuildConfig.CLOUDBASE_ENV_ID
    val publishableKey: String = BuildConfig.CLOUDBASE_PUBLISHABLE_KEY
    val baseUrl: String = "https://$envId.api.tcloudbasegateway.com/"
}
