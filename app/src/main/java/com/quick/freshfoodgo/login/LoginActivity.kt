package com.quick.freshfoodgo.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityLoginBinding
import com.quick.freshfoodgo.login.vm.AuthEvent
import com.quick.freshfoodgo.login.vm.AuthUiState
import com.quick.freshfoodgo.login.vm.AuthViewModel
import com.quick.freshfoodgo.util.KeyBoardUtil
import kotlinx.coroutines.launch

class LoginActivity: BaseActivity<ActivityLoginBinding>() {
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val root = findViewById<View>(R.id.root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, bars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


    }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        checkInput()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.authEvent.collect { it: AuthEvent ->
                    when (it) {
                        is AuthEvent.LoginSuccess -> {
                            Log.d("ly", "登录成功: ${it.userId}")
                            // 跳转主页
//                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                        finish()
                        }
                        is AuthEvent.Failed -> {
                            Log.d("ly", "失败: ${it.msg}")
                            // Toast
                        }
                        AuthEvent.RegisterSuccessNeedVerify -> {
                            Log.d("ly", "注册成功，需要邮箱验证")
                        }
                        AuthEvent.RegisterSuccessAutoLogin -> {
                            Log.d("ly", "注册成功并且自动登录")
                        }
                        AuthEvent.LogoutSuccess -> {}
                    }
                }

                }
            }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                     binding.pgBar.isVisible = state.loading
                }
            }
        }
        }



    override fun initAction() {
        super.initAction()
        binding.inputUser.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }
            override fun onTextChanged(
                p0: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                checkInput()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.inputPwd.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }
            override fun onTextChanged(
                p0: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                checkInput()
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.userLogin.setOnClickListener {
            val email = binding.inputUser.text.toString().trim()
            val pwd = binding.inputPwd.text.toString().trim()
            //登录并且关闭键盘
            KeyBoardUtil.hideKeyboard(this,binding.userLogin)
            viewModel.login(
                email,
                pwd
            )

        }

        binding.regisUser.setOnClickListener {
            startActivity(Intent(this, EmailRegisrerActivity::class.java))
        }

    }

    private fun checkInput() {
        val user = binding.inputUser.text.toString().trim()
        val pwd = binding.inputPwd.text.toString().trim()
        binding.userLogin.isEnabled = user.isNotEmpty() and pwd.isNotEmpty()
    }


}