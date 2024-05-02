package com.arkan.a4crypto.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.a4crypto.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val repo: UserRepository) : ViewModel() {
    val isEditMode = MutableLiveData(false)

    fun changeEditMode() {
        val currentValue = isEditMode.value ?: false
        isEditMode.postValue(!currentValue)
    }

    fun changeProfile(fullName: String) = repo.updateProfile(fullName).asLiveData(Dispatchers.IO)

    fun changePassword() {
        repo.requestChangePasswordByEmail()
    }

    fun getCurrentUser() = repo.getCurrentUser()

    fun doLogout() {
        repo.doLogout()
    }
}
