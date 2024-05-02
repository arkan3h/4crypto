package com.arkan.a4crypto.presentation.detail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.data.repository.CoinDetailRepository
import com.arkan.a4crypto.data.repository.FavoriteRepository
import com.arkan.aresto.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalStateException

class DetailViewModel(
    extras: Bundle?,
    private val coinDetailRepository: CoinDetailRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {
    private val product = extras?.getParcelable<Coin>(DetailActivity.EXTRAS_ITEM_ACT)

    fun getDetailInfo() = coinDetailRepository.getCoinDetail(product?.id).asLiveData(Dispatchers.IO)

    fun addToFavorite(): LiveData<ResultWrapper<Boolean>> {
        return product?.let {
            favoriteRepository.createFavorite(it).asLiveData(Dispatchers.IO)
        } ?: liveData { emit(ResultWrapper.Error(IllegalStateException("Catalog not found"))) }
    }
}
