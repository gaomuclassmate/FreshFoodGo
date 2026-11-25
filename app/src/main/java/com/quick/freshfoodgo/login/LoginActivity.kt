package com.quick.freshfoodgo.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quick.freshfoodgo.MainActivity
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityLoginBinding
import com.quick.freshfoodgo.login.vm.AuthEvent
import com.quick.freshfoodgo.login.vm.AuthUiState
import com.quick.freshfoodgo.login.vm.AuthViewModel
import com.quick.freshfoodgo.util.KeyBoardUtil
import kotlinx.coroutines.launch

class LoginActivity: BaseActivity<ActivityLoginBinding>() {
    private val viewModel: AuthViewModel by viewModels()

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

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        checkInput()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect(::render)
                }
                launch {
                    viewModel.authEvent.collect(::handleAuthEvent)
                }
            }
        }
    }

    private fun render(state: AuthUiState) {
        binding.pgBar.isVisible = state.loading
        binding.userLogin.isEnabled = !state.loading &&
                binding.inputUser.text?.isNotBlank() == true &&
                binding.inputPwd.text?.isNotBlank() == true
    }

    private fun handleAuthEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginSuccess -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            is AuthEvent.Failed -> {
                Toast.makeText(this, event.msg, Toast.LENGTH_LONG).show()
            }
            is AuthEvent.RegisterSuccessAutoLogin -> {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
            }
            is AuthEvent.VerificationCodeSent -> Unit
            AuthEvent.LogoutSuccess -> Unit
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
            val username = binding.inputUser.text.toString().trim()
            val pwd = binding.inputPwd.text.toString().trim()
            //登录并且关闭键盘
            KeyBoardUtil.hideKeyboard(this,binding.userLogin)
            viewModel.login(
                username,
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
