package com.quick.freshfoodgo.viewmodel

import androidx.lifecycle.viewModelScope
import com.quick.freshfoodgo.base.BaseViewModel
import com.quick.freshfoodgo.bean.FoodDetailBean
import com.quick.freshfoodgo.network.repository.FruitRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FoodDetailUiState(
    val loading: Boolean = false,
    val detail: FoodDetailBean = FoodDetailBean(),
    val error: String? = null,
    var addCartSuccess: Boolean? = null
)

class FoodDetailViewModel(
    private val foodId: String,
    private val repository: FruitRepo = FruitRepo()
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(FoodDetailUiState())
    val uiState: StateFlow<FoodDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    override fun initViewModel() {}

    fun loadDetail() {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            val result = repository.getFoodDetail(foodId)
            _uiState.update { state ->
                result.fold(
                    onSuccess = { remoteDetail ->
                        state.copy(
                            loading = false,
                            detail = remoteDetail.copy(
                                count = state.detail.count,
                                isLike = state.detail.isLike
                            ),
                            error = null
                        )
                    },
                    onFailure = { error ->
                        state.copy(
                            loading = false,
                            error = error.message ?: "商品详情加载失败"
                        )
                    }
                )
            }
        }
    }

    fun addCount() {
        _uiState.update { state ->
            state.copy(
                detail = state.detail.copy(count = state.detail.count + 1)
            )
        }
    }

    fun reduceCount() {
        _uiState.update { state ->
            if (state.detail.count > 0) {
                state.copy(
                    detail = state.detail.copy(count = state.detail.count - 1)
                )
            } else {
                state
            }
        }
    }

    fun toggleLike() {
        _uiState.update { state ->
            state.copy(
                detail = state.detail.copy(isLike = !state.detail.isLike)
            )
        }
    }

    fun addProductCar(foodId: String, quantity: Int = 1) {
        _uiState.update { it.copy(addCartSuccess = null, error = null) }
        viewModelScope.launch {
            repository.addBuyCar(foodId, quantity).fold(
                onSuccess = {
                    _uiState.update { it.copy(addCartSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            addCartSuccess = false,
                            error = error.message ?: "添加购物车失败"
                        )
                    }
                }
            )
        }
    }
}
