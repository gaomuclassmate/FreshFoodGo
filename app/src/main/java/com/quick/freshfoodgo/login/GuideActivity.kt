package com.quick.freshfoodgo.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityGuideBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GuideActivity: BaseActivity<ActivityGuideBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContentView(R.layout.activity_guide)
        waitEnter()
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityGuideBinding {
        return ActivityGuideBinding.inflate(layoutInflater)
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