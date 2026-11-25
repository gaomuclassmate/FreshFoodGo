package com.quick.freshfoodgo.login

import android.content.Intent
import android.os.CountDownTimer
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
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
import com.quick.freshfoodgo.databinding.ActivityEmailRegisBinding
import com.quick.freshfoodgo.login.vm.AuthEvent
import com.quick.freshfoodgo.login.vm.AuthUiState
import com.quick.freshfoodgo.login.vm.AuthViewModel
import com.quick.freshfoodgo.util.KeyBoardUtil
import kotlinx.coroutines.launch
import kotlin.math.ceil

class EmailRegisrerActivity : BaseActivity<ActivityEmailRegisBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    private var countDownTimer: CountDownTimer? = null
    private var countdownActive = false

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityEmailRegisBinding {
        return ActivityEmailRegisBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val keyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            binding.sureRegis.isVisible = !keyboardVisible
            insets
        }

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

    override fun initAction() {
        super.initAction()
        binding.btnSendCode.setOnClickListener {
            val email = binding.edEmailCode.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            KeyBoardUtil.hideKeyboard(this, binding.btnSendCode)
            viewModel.sendEmailVerification(email)
        }

        binding.sureRegis.setOnClickListener {
            val email = binding.edEmailCode.text.toString().trim()
            val password = binding.edEmailPwd.text.toString()
            val code = binding.edVerifyCode.text.toString().trim()

            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show()
                }
                password.isBlank() -> {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show()
                }
                !code.matches(Regex("\\d{6}")) -> {
                    Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    KeyBoardUtil.hideKeyboard(this, binding.sureRegis)
                    viewModel.registerWithEmailCode(email, password, code)
                }
            }
        }
    }

    private fun render(state: AuthUiState) {
        binding.pgBar.isVisible = state.loading
        binding.sureRegis.isEnabled = !state.loading
        binding.btnSendCode.isEnabled = !state.loading && !countdownActive
    }

    private fun handleAuthEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.VerificationCodeSent -> {
                val message = if (event.emailExists) {
                    "该邮箱已注册，请返回登录"
                } else {
                    "验证码已发送，请查收邮箱"
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                startCountdown()
            }
            is AuthEvent.RegisterSuccessAutoLogin -> {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            is AuthEvent.Failed -> {
                Toast.makeText(this, event.msg, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    private fun startCountdown() {
        countDownTimer?.cancel()
        countdownActive = true
        binding.btnSendCode.isEnabled = false

        countDownTimer = object : CountDownTimer(60_000L, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = ceil(millisUntilFinished / 1000.0).toInt()
                binding.btnSendCode.text = getString(R.string.resend_code_format, seconds)
            }

            override fun onFinish() {
                countdownActive = false
                binding.btnSendCode.text = getString(R.string.send_code)
                binding.btnSendCode.isEnabled = !viewModel.uiState.value.loading
            }
        }.start()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer = null
        super.onDestroy()
    }
}
