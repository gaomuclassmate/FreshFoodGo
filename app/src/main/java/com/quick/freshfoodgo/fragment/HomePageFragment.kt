package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.adapter.FruitAdapter
import com.quick.freshfoodgo.adapter.ImageAdapter
import com.quick.freshfoodgo.adapter.decoration.HorizontalSpaceDecoration
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentHomePageBinding
import com.quick.freshfoodgo.viewmodel.HomePageUiState
import com.quick.freshfoodgo.viewmodel.HomePageViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.launch

class HomePageFragment : BaseFragment<FragmentHomePageBinding>() {

    private val viewModel: HomePageViewModel by viewModels()
    private val bannerAdapter = ImageAdapter()
    private var todayOffersAdapter: FruitAdapter? = null
    private var bestSellersAdapter: FruitAdapter? = null
    private var lastError: String? = null
    private var bannerSignature: String = ""

    override fun inflateBinding(inflater: LayoutInflater): FragmentHomePageBinding {
        return FragmentHomePageBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        useBanner()

        todayOffersAdapter = createAdapter()
        bestSellersAdapter = createAdapter()

        binding.recycleFruit.adapter = todayOffersAdapter
        binding.recycleFruit.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleFruit.addItemDecoration(HorizontalSpaceDecoration(8))

        binding.recycleFruit2.adapter = bestSellersAdapter
        binding.recycleFruit2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleFruit2.addItemDecoration(HorizontalSpaceDecoration(8))
        //refresh
        binding.refreshLayout.setRefreshHeader(ClassicsHeader(requireContext()))
//        binding.refreshLayout.setRefreshFooter(ClassicsFooter(requireContext()))
        binding.refreshLayout.setOnRefreshListener { it: RefreshLayout ->
            lifecycleScope.launch {
                viewModel.loadHomeFeed()
            }

        }
//        binding.refreshLayout.setOnLoadMoreListener {
//
//        }
    }

    override fun initListener() {
        super.initListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: HomePageUiState) {
        val newBannerSignature = state.banners.joinToString("|") {
            "${it.bannerId}:${it.imageUrl}"
        }
        if (newBannerSignature.isNotBlank() && newBannerSignature != bannerSignature) {
            bannerSignature = newBannerSignature
            bannerAdapter.setDatas(state.banners.toMutableList())
            binding.banner.start()
        }

        todayOffersAdapter?.setData(state.todayOffers)
        bestSellersAdapter?.setData(state.bestSellers)
        binding.refreshLayout.finishRefresh(true)

        if (!state.error.isNullOrBlank() && state.error != lastError) {
            lastError = state.error
            binding.refreshLayout.finishRefresh(false)
            Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
        }
    }

    private fun createAdapter(): FruitAdapter {
        return FruitAdapter().apply {
            setOnClickListener(object : FruitAdapter.OnClickListener {
                override fun clickEnter(foodId: String) {
                    findNavController().navigate(
                        R.id.action_home_page_to_foodDetailFragment,
                        Bundle().apply {
                            putString(FoodDetailFragment.ARG_FOOD_ID, foodId)
                        }
                    )
                }
            })
        }
    }

    private fun useBanner() {
        binding.banner.addBannerLifecycleObserver(this)
            .setAdapter(bannerAdapter)
            .indicator = CircleIndicator(binding.root.context)
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
        fun instance(): HomePageFragment = HomePageFragment()
    }
}
