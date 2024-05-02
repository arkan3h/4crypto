package com.arkan.a4crypto.presentation.detail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.data.repository.CoinDetailRepository
import kotlinx.coroutines.Dispatchers

class DetailViewModel(
    extras: Bundle?,
    private val coinDetailRepository: CoinDetailRepository,
) : ViewModel() {
    private val product = extras?.getParcelable<Coin>(DetailActivity.EXTRAS_ITEM_ACT)

    fun getDetailInfo() = coinDetailRepository.getCoinDetail(product?.id).asLiveData(Dispatchers.IO)
}
