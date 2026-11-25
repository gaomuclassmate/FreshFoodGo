package com.quick.freshfoodgo.viewmodel

import androidx.lifecycle.viewModelScope
import com.quick.freshfoodgo.base.BaseViewModel
import com.quick.freshfoodgo.bean.ProductClassify
import com.quick.freshfoodgo.network.repository.CategoryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FoundPageUiState(
    val loading: Boolean = false,
    val categories: List<ProductClassify> = emptyList(),
    val error: String? = null
)

class FoundPageViewModel : BaseViewModel() {

    private val repository = CategoryRepo()
    private val _uiState = MutableStateFlow(FoundPageUiState())
    val uiState: StateFlow<FoundPageUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    override fun initViewModel() {}

    fun loadCategories() {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            repository.getCategories().fold(
                onSuccess = { categories ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        categories = categories,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = error.message ?: "商品分类加载失败"
                    )
                }
            )
        }
    }
}
