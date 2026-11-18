package com.quick.freshfoodgo.login


import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityEmailRegisBinding
import com.quick.freshfoodgo.databinding.ActivityPhoneRegisBinding
import com.quick.freshfoodgo.login.vm.AuthViewModel
import kotlinx.coroutines.launch

class EmailRegisrerActivity(): BaseActivity<ActivityEmailRegisBinding>(){
    lateinit var viewModel: AuthViewModel

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityEmailRegisBinding {
        return ActivityEmailRegisBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            // 获取软键盘的inset高度（键盘高度，单位px）
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val keyboardHeight = imeInsets.bottom
            val isKeyboardShow = insets.isVisible(WindowInsetsCompat.Type.ime())

            if (isKeyboardShow) {
                // 键盘弹出：向上平移 keyboardHeight，加一点偏移量防止贴键盘
                binding.sureRegis.animate()
                    .translationY(-keyboardHeight.toFloat())
                    .setDuration(2000)
                    .start()
            } else {
                // 键盘收起：复位回到原来位置
                binding.sureRegis.animate()
                    .translationY(0f)
                    .setDuration(2000)
                    .start()
            }
            insets
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state ->
                    when{
                        state.error != null -> {
                            Toast.makeText(this@EmailRegisrerActivity, state.error, Toast.LENGTH_LONG).show()
                        }
                        state.message != null -> {
                            Toast.makeText(this@EmailRegisrerActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                    }



                }
            }

        }

    }

    override fun initVarible() {
        super.initVarible()
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun initAction() {
        super.initAction()
        binding.sureRegis.setOnClickListener {
            val email = binding.edEmailCode.text.toString().trim()
            val pwd = binding.edEmailPwd.text.toString().trim()
            viewModel.register(email,pwd)
        }



    }

}
