package com.quick.freshfoodgo

import android.app.Application
import android.content.Context
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class AppContext: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext

        initSuperbase()

    }

    private fun initSuperbase() {

        val supabase = createSupabaseClient(
            supabaseUrl = "https://tjayvthjutczkqaknrkc.supabase.co",
            supabaseKey = "sb_publishable_BOjTOh03IV-GfwzGtJRFpw_pgQxpQBo"
        ) {
            install(Postgrest)
        }
    }

    companion object {
        lateinit var instance: Context


    }
}