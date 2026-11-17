package com.quick.freshfoodgo.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.quick.freshfoodgo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GuideActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContentView(R.layout.activity_guide)
        waitEnter()
    }

    private fun waitEnter() {
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@GuideActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}