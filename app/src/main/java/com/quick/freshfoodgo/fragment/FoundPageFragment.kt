package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.adapter.ProductClassifyAdapter
import com.quick.freshfoodgo.adapter.decoration.GridSpacingDecoration
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentFoundPageBinding
import com.quick.freshfoodgo.viewmodel.FoundPageUiState
import com.quick.freshfoodgo.viewmodel.FoundPageViewModel
import kotlinx.coroutines.launch

class FoundPageFragment : BaseFragment<FragmentFoundPageBinding>() {

    private val viewModel: FoundPageViewModel by viewModels()
    private val categoryAdapter = ProductClassifyAdapter()
    private var lastError: String? = null

    override fun inflateBinding(inflater: LayoutInflater): FragmentFoundPageBinding {
        return FragmentFoundPageBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        binding.list.adapter = categoryAdapter
        binding.list.layoutManager = GridLayoutManager(context, 2)
        binding.list.addItemDecoration(
            GridSpacingDecoration(
                spanCount = 2,
                spacing = 16,
                includeEdge = false
            )
        )
    }

    override fun initAction() {
        super.initAction()
        categoryAdapter.setOnClickListener { category ->
            findNavController().navigate(
                R.id.action_found_page_to_categoryProductsFragment,
                Bundle().apply {
                    putString(
                        CategoryProductsFragment.ARG_CATEGORY_ID,
                        category.categoryId
                    )
                    putString(
                        CategoryProductsFragment.ARG_CATEGORY_NAME,
                        category.name
                    )
                }
            )
        }
        binding.search.setOnClickListener {
            // Search will be connected to the product API in a later step.
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

    private fun render(state: FoundPageUiState) {
        categoryAdapter.setData(state.categories)
        if (!state.error.isNullOrBlank() && state.error != lastError) {
            lastError = state.error
            Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun instance(): FoundPageFragment = FoundPageFragment()
    }
}
