package com.arkan.a4crypto.presentation.home

import androidx.lifecycle.ViewModel
import com.arkan.a4crypto.data.repository.UserRepository

class HomeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun isUserLoggedIn() = userRepository.isLoggedIn()
}
