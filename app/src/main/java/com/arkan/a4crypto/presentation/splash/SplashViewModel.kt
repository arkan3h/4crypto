package com.arkan.a4crypto.presentation.splash

import androidx.lifecycle.ViewModel
import com.arkan.a4crypto.data.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repository.isLoggedIn()
}
