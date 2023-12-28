package com.bangkit23dwinovirhmwt.githubuser.ui.favoriteActivity

import androidx.lifecycle.ViewModel
import com.bangkit23dwinovirhmwt.githubuser.data.repository.FavoriteRepository

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    fun getAllFavoriteUser() = favoriteRepository.getAllFavoriteUser()
}