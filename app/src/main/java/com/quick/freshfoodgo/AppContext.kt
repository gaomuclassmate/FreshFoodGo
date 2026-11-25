package com.quick.freshfoodgo

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV


class AppContext: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
        MMKV.initialize(this)

    }


    companion object {
        lateinit var instance: Context

    }
}
