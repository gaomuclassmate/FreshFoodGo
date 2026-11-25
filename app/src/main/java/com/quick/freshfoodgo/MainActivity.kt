package com.quick.freshfoodgo

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.quick.freshfoodgo.base.BaseActivity
import com.quick.freshfoodgo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private val mainDestinations = setOf(
        R.id.home_page,
        R.id.found_page,
        R.id.buy_car_page,
        R.id.collect_page,
        R.id.me_page
    )

    override fun initView() {
        super.initView()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        //bottomNav设置颜色为我自己设置的
        binding.bottomNav.itemIconTintList = null

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id in mainDestinations) View.VISIBLE else View.GONE


        }
    }
}