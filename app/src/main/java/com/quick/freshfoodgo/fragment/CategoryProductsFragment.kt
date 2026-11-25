package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.adapter.CategoryProductAdapter
import com.quick.freshfoodgo.adapter.decoration.GridSpacingDecoration
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentCategoryProductsBinding
import com.quick.freshfoodgo.viewmodel.CategoryProductsUiState
import com.quick.freshfoodgo.viewmodel.CategoryProductsViewModel
import com.quick.freshfoodgo.viewmodel.VmFactory
import kotlinx.coroutines.launch

class CategoryProductsFragment : BaseFragment<FragmentCategoryProductsBinding>() {

    private val categoryId: String
        get() = requireArguments().getString(ARG_CATEGORY_ID).orEmpty()


    private val categoryName: String
        get() = requireArguments().getString(ARG_CATEGORY_NAME).orEmpty()

    private val viewModel: CategoryProductsViewModel by viewModels {
        VmFactory { CategoryProductsViewModel(categoryId) }
    }

    private val productAdapter = CategoryProductAdapter()
    private var lastError: String? = null

    override fun inflateBinding(inflater: LayoutInflater): FragmentCategoryProductsBinding {
        return FragmentCategoryProductsBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        binding.toolbar.title = categoryName
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.productList.adapter = productAdapter
        binding.productList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productList.addItemDecoration(
            GridSpacingDecoration(
                spanCount = 2,
                spacing = 12,
                includeEdge = false
            )
        )
    }

    override fun initAction() {
        super.initAction()
        productAdapter.setOnProductClickListener { foodId ->
            findNavController().navigate(
                R.id.action_categoryProductsFragment_to_foodDetailFragment,
                Bundle().apply {
                    putString(FoodDetailFragment.ARG_FOOD_ID, foodId)
                }
            )
        }
    }

    override fun initListener() {
        super.initListener()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: CategoryProductsUiState) {
        binding.progress.isVisible = state.loading
        productAdapter.setData(state.products)

        if (!state.error.isNullOrBlank() && state.error != lastError) {
            lastError = state.error
            Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ARG_CATEGORY_ID = "category_id"
        const val ARG_CATEGORY_NAME = "category_name"
    }
}
