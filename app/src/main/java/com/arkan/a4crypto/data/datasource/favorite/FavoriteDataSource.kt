package com.arkan.a4crypto.data.datasource.favorite

import com.arkan.a4crypto.data.source.dao.FavoriteDao
import com.arkan.a4crypto.data.source.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteDataSource {
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    suspend fun insertFavorite(favorite: FavoriteEntity): Long

    suspend fun deleteFavorite(favorite: FavoriteEntity): Int

    suspend fun deleteAll()
}

class FavoriteDatabaseDataSource(
    private val dao: FavoriteDao,
) : FavoriteDataSource {
    override fun getAllFavorites(): Flow<List<FavoriteEntity>> = dao.getAllFavorites()

    override suspend fun insertFavorite(favorite: FavoriteEntity): Long = dao.insertFavorite(favorite)

    override suspend fun deleteFavorite(favorite: FavoriteEntity): Int = dao.deleteFavorite(favorite)

    override suspend fun deleteAll() = dao.deleteAll()
}
