package com.quick.freshfoodgo.fragment

import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.bean.FoodDetailBean
import com.quick.freshfoodgo.databinding.FragmentFoodDetailBinding
import com.quick.freshfoodgo.util.CommonUtil
import com.quick.freshfoodgo.util.RequestUtil
import com.quick.freshfoodgo.util.TokenUtil
import com.quick.freshfoodgo.viewmodel.FoodDetailViewModel
import com.quick.freshfoodgo.viewmodel.FoodDetailUiState
import com.quick.freshfoodgo.viewmodel.VmFactory
import kotlinx.coroutines.launch

class FoodDetailFragment : BaseFragment<FragmentFoodDetailBinding>() {

    private val foodId: String
        get() = requireArguments().getString(ARG_FOOD_ID).orEmpty()

    private val viewModel: FoodDetailViewModel by viewModels {
        VmFactory { FoodDetailViewModel(foodId) }
    }
    private var lastError: String? = null

    override fun inflateBinding(inflater: LayoutInflater): FragmentFoodDetailBinding {
        return FragmentFoodDetailBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        binding.toolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.addBuyCar.isEnabled = binding.count.text.toString().toInt() != 0

    }

    override fun initAction() {
        super.initAction()
        binding.add.setOnClickListener {
            viewModel.addCount()
        }
        binding.reduce.setOnClickListener {
            viewModel.reduceCount()
        }
        binding.like.setOnClickListener { viewModel.toggleLike() }

        binding.addBuyCar.setOnClickListener {
            RequestUtil.canRepeatRequest(System.currentTimeMillis()).let { it: Boolean ->
                if (!it){
                    return@setOnClickListener
                }
                viewModel.addProductCar(foodId,binding.count.text.toString().toInt())


            }


        }


    }

    override fun initListener() {
        super.initListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(state: FoodDetailUiState) {
        if (!state.error.isNullOrBlank() && state.error != lastError) {
            lastError = state.error
            Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
        }
        if (state.loading || state.detail.name.isBlank()) return

        renderDetail(state)
    }

    private fun renderDetail(state: FoodDetailUiState) {
        val (_,detail,_,addCartSuccess) = state
        binding.productImg.load(detail.imageUrl) {
            placeholder(R.mipmap.imgloading)
            error(R.mipmap.img_load_fail)
            crossfade(200)
        }
        binding.name.text = detail.name
        val total = detail.count * detail.price.toDouble()
        binding.price.text = "￥%.2f".format(total)
        binding.tvDetail.text = detail.description
        binding.count.text = detail.count.toString()
        binding.like.setImageResource(
            if (detail.isLike) R.mipmap.like else R.mipmap.not_like
        )
        binding.toolbar.toolbar.title = detail.name
        binding.addBuyCar.isEnabled = detail.count != 0
        addCartSuccess?.let {
            if (it){
                CommonUtil.showToast(requireContext(),"添加购物车成功")
            }
        }
    }

    companion object {
        const val ARG_FOOD_ID = "food_id"
    }
}
