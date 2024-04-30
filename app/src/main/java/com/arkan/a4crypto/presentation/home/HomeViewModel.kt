package com.arkan.a4crypto.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.a4crypto.data.repository.CoinRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val coinRepository: CoinRepository,
) : ViewModel() {
    fun getCoinList() = coinRepository.getCoinList().asLiveData(Dispatchers.IO)
}
