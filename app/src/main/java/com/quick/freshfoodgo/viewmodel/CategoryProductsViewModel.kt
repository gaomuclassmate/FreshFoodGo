package com.quick.freshfoodgo.viewmodel

import androidx.lifecycle.viewModelScope
import com.quick.freshfoodgo.base.BaseViewModel
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.network.repository.CategoryProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CategoryProductsUiState(
    val loading: Boolean = false,
    val products: List<FruitBean> = emptyList(),
    val error: String? = null
)

class CategoryProductsViewModel(
    private val categoryId: String,
    private val repository: CategoryProductRepo = CategoryProductRepo()
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CategoryProductsUiState())
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    override fun initViewModel() {}

    fun loadProducts() {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            repository.getProducts(categoryId).fold(
                onSuccess = { products ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        products = products,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = error.message ?: "分类商品加载失败"
                    )
                }
            )
        }
    }
}
