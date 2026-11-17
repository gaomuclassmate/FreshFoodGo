package com.quick.freshfoodgo.login


import android.content.Intent
import android.view.LayoutInflater
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityPhoneRegisBinding

class PhoneRegisrerActivity(): BaseActivity<ActivityPhoneRegisBinding>(){


    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityPhoneRegisBinding {
        return ActivityPhoneRegisBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            // 获取软键盘的inset高度（键盘高度，单位px）
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val keyboardHeight = imeInsets.bottom
            val isKeyboardShow = insets.isVisible(WindowInsetsCompat.Type.ime())

            if (isKeyboardShow) {
                // 键盘弹出：向上平移 keyboardHeight，加一点偏移量防止贴键盘
                binding.sureNum.animate()
                    .translationY(-keyboardHeight.toFloat())
                    .setDuration(2000)
                    .start()
            } else {
                // 键盘收起：复位回到原来位置
                binding.sureNum.animate()
                    .translationY(0f)
                    .setDuration(2000)
                    .start()
            }
            insets
        }

    }

}
