package com.quick.freshfoodgo.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityLoginBinding

class LoginActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val root = findViewById<View>(R.id.root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, bars.bottom)
            insets
        }
    }

    override fun initView() {
        super.initView()
        val binding = ActivityLoginBinding.bind(findViewById(R.id.root))
        Log.d("ly", "initView: ")
        binding.phoneLogin.setOnClickListener {
            Log.d("ly", "initView: ")
            startActivity(Intent(this,PhoneRegisrerActivity::class.java))
        }
        binding.qqLogin.setOnClickListener {
            Log.d("ly", "initView: ")
            startActivity(Intent(this,PhoneRegisrerActivity::class.java))
        }
        binding.vxLogin.setOnClickListener {
            Log.d("ly", "initView: ")
            startActivity(Intent(this,PhoneRegisrerActivity::class.java))
        }

    }

    override fun getLayoutId(): Int {
        return  R.layout.activity_login
    }
}