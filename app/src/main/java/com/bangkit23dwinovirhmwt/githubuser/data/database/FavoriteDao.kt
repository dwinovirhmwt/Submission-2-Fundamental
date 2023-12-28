package com.bangkit23dwinovirhmwt.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: FavoriteEntity)

    @Update
    fun update(favorite: FavoriteEntity)

    @Delete
    fun delete(favorite: FavoriteEntity)

    @Query("SELECT * from favorite_user ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<FavoriteEntity>>

    @Query("DELETE FROM favorite_user WHERE is_favorite = 0")
    fun deleteAllUsers()

    @Query("SELECT * FROM favorite_user WHERE is_favorite = 1")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_user WHERE id = :id AND is_favorite = 1)")
    fun isFavorite(id: String): Boolean

}