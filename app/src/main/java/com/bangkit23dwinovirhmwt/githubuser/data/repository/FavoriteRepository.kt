package com.bangkit23dwinovirhmwt.githubuser.data.repository

import androidx.lifecycle.LiveData
import com.bangkit23dwinovirhmwt.githubuser.data.database.FavoriteDao
import com.bangkit23dwinovirhmwt.githubuser.data.database.FavoriteEntity
import com.bangkit23dwinovirhmwt.githubuser.ui.AppExecutors

class FavoriteRepository private constructor(
    private val favoriteDao: FavoriteDao,
    private val appExecutors: AppExecutors
) {

    fun getAllFavoriteUser(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getAllFavoriteUsers()
    }

    fun setFavoriteUser(favoriteEntity: FavoriteEntity, isFavorite: Boolean) {
        appExecutors.diskIO.execute {
            favoriteEntity.isFavorite = isFavorite
            favoriteDao.update(favoriteEntity)
        }
    }

    fun insertFavoriteUser(favoriteEntity: FavoriteEntity) {
        appExecutors.diskIO.execute {
            favoriteDao.insert(favoriteEntity)
        }
    }

    fun deleteAllUsersFavorite() = favoriteDao.deleteAllUsers()

    fun isFavorite(id: String): Boolean = favoriteDao.isFavorite(id)

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            favoriteDao: FavoriteDao,
            appExecutors: AppExecutors,
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(favoriteDao, appExecutors)
            }.also { instance = it }
    }
}