package com.arkan.a4crypto.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.a4crypto.data.repository.CoinRepository
import com.arkan.a4crypto.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val coinRepository: CoinRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    fun getCoinList() = coinRepository.getCoinList().asLiveData(Dispatchers.IO)

    fun getCurrentUser() = userRepository.getCurrentUser()
}
