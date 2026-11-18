package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.adapter.FruitAdapter
import com.quick.freshfoodgo.adapter.HorizontalSpaceDecoration
import com.quick.freshfoodgo.adapter.ImageAdapter
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.bean.BannerDataBean
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.databinding.FragmentHomePageBinding
import com.quick.freshfoodgo.viewmodel.HomePageViewModel
import com.youth.banner.indicator.CircleIndicator

class HomePageFragment : BaseFragment<FragmentHomePageBinding>() {
    var viewModel: ViewModel? = null


    override fun inflateBinding(inflater: LayoutInflater): FragmentHomePageBinding {
        return FragmentHomePageBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

        useBanner()
        val adapter = FruitAdapter()
        val list = mutableListOf<FruitBean>()
        list.addAll(
            listOf(
                FruitBean("有机香蕉", "1kg,价格", "3.99", R.mipmap.bannaner),
                FruitBean("红苹果", "1kg,价格", "8.99", R.mipmap.pingguo),
                FruitBean("有机香蕉", "1kg,价格", "7.99", R.mipmap.lajiao),
                FruitBean("有机香蕉", "1kg,价格", "6.99", R.mipmap.shengjiang),
            )
        )
        adapter.setData(list)
        binding.recycleFruit.adapter = adapter
        binding.recycleFruit.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        binding.recycleFruit.addItemDecoration(HorizontalSpaceDecoration(8))

        binding.recycleFruit2.adapter = adapter
        binding.recycleFruit2.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        binding.recycleFruit2.addItemDecoration(HorizontalSpaceDecoration(8))

    }

    private fun useBanner() {
        binding.banner.addBannerLifecycleObserver(this)//添加生命周期观察者
            .setAdapter(ImageAdapter(BannerDataBean.getTestData()))
            .indicator = CircleIndicator(binding.root.context);
        binding.banner.start()
    }

    override fun onStop() {
        super.onStop()
        binding.banner.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.banner.destroy()
    }

    companion object {
        fun instance(): HomePageFragment {
            val fragment = HomePageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }


}