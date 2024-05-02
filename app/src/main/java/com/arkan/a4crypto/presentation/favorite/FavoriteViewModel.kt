package com.arkan.a4crypto.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arkan.a4crypto.data.model.Favorite
import com.arkan.a4crypto.data.repository.FavoriteRepository
import com.arkan.a4crypto.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repo: FavoriteRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    fun getAllFavorites() = repo.getUserFavoriteData().asLiveData(Dispatchers.IO)

    fun removeFavorite(item: Favorite) {
        viewModelScope.launch(Dispatchers.IO) { repo.deleteFavorite(item).collect() }
    }

    fun isUserLoggedIn() = userRepository.isLoggedIn()
}
