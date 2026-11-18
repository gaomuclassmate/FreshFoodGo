package com.quick.freshfoodgo.network.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseManager {


    private const val SUPABASE_URL =
        "https://tjayvthjutczkqaknrkc.supabase.co"

    private const val SUPABASE_KEY =
        "sb_publishable_BOjTOh03IV-GfwzGtJRFpw_pgQxpQBo"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }

}