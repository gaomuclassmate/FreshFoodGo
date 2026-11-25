package com.quick.freshfoodgo.viewmodel

import androidx.lifecycle.viewModelScope
import com.quick.freshfoodgo.base.BaseViewModel
import com.quick.freshfoodgo.bean.BannerDataBean
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.network.MyRetrofit
import com.quick.freshfoodgo.network.cloudbase.AddProductCarRequest
import com.quick.freshfoodgo.network.repository.BannerRepo
import com.quick.freshfoodgo.network.repository.FruitRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomePageUiState(
    val loading: Boolean = false,
    val banners: List<BannerDataBean> = emptyList(),
    val todayOffers: List<FruitBean> = emptyList(),
    val bestSellers: List<FruitBean> = emptyList(),
    val error: String? = null
)

class HomePageViewModel : BaseViewModel() {

    private val repository = FruitRepo()
    private val bannerRepository = BannerRepo()
    private val apiService = MyRetrofit.apiService
    private val _uiState = MutableStateFlow(HomePageUiState())
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    init {
        loadHomeFeed()
    }

    override fun initViewModel() {}

    fun loadHomeFeed() {
        if (_uiState.value.loading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            val feedResult = repository.getHomeFeed()
            val bannerResult = bannerRepository.getHomeBanners()
            val feedData = feedResult.getOrNull()
            val banners = bannerResult.getOrNull()
            val error = feedResult.exceptionOrNull()?.message
                ?: bannerResult.exceptionOrNull()?.message

            _uiState.value = _uiState.value.copy(
                loading = false,
                banners = banners ?: _uiState.value.banners,
                todayOffers = feedData?.let {
                    repository.toFruitBeans(it, todayOffers = true)
                } ?: _uiState.value.todayOffers,
                bestSellers = feedData?.let {
                    repository.toFruitBeans(it, todayOffers = false)
                } ?: _uiState.value.bestSellers,
                error = error
            )
        }
    }



}
