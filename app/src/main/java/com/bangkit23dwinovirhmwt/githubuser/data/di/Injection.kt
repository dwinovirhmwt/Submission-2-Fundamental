package com.bangkit23dwinovirhmwt.githubuser.data.di

import android.content.Context
import com.bangkit23dwinovirhmwt.githubuser.data.database.FavoriteRoomDatabase
import com.bangkit23dwinovirhmwt.githubuser.data.repository.FavoriteRepository
import com.bangkit23dwinovirhmwt.githubuser.ui.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val database = FavoriteRoomDatabase.getInstance(context)
        val dao = database.favoriteDao()
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance(dao, appExecutors)
    }
}