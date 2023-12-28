package com.bangkit23dwinovirhmwt.githubuser.ui.detailUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit23dwinovirhmwt.githubuser.data.database.FavoriteEntity
import com.bangkit23dwinovirhmwt.githubuser.data.repository.FavoriteRepository
import com.bangkit23dwinovirhmwt.githubuser.data.response.DetailUserResponse
import com.bangkit23dwinovirhmwt.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    val userGithubDetail = MutableLiveData<DetailUserResponse>()

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun setUserGithubDetail(username: String) {
        ApiConfig.getApiService()
            .getUserGithubDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful)
                        userGithubDetail.postValue(response.body())
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    val errorMessage = t.message.toString()
                    _errorLiveData.postValue(errorMessage)
                }

            })
    }

    fun getUserGithubDetail(): LiveData<DetailUserResponse> {
        return userGithubDetail
    }

    fun saveUserToFavorite(favoriteEntity: FavoriteEntity) {
        favoriteRepository.setFavoriteUser(favoriteEntity, true)
    }

    fun deleteUserFromFavorite(userEntity: FavoriteEntity) {
        favoriteRepository.setFavoriteUser(userEntity, false)
    }

    fun insertUserToFavorite(userEntity: FavoriteEntity) {
        favoriteRepository.insertFavoriteUser(userEntity)
    }

    suspend fun deleteAllUsersFavorite() = withContext(Dispatchers.IO) {
        favoriteRepository.deleteAllUsersFavorite()
    }

    suspend fun isFavorite(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            favoriteRepository.isFavorite(id)
        }
    }
}