package com.arkan.a4crypto.data.repository

import com.arkan.a4crypto.data.datasource.favorite.FavoriteDataSource
import com.arkan.a4crypto.data.mapper.toFavoriteEntity
import com.arkan.a4crypto.data.mapper.toFavoriteList
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.data.model.Favorite
import com.arkan.a4crypto.data.source.entity.FavoriteEntity
import com.arkan.aresto.utils.ResultWrapper
import com.arkan.aresto.utils.proceed
import com.arkan.aresto.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.lang.IllegalStateException

interface FavoriteRepository {
    fun getUserFavoriteData(): Flow<ResultWrapper<Pair<List<Favorite>, Double>>>

    fun createFavorite(catalog: Coin): Flow<ResultWrapper<Boolean>>

    fun deleteFavorite(item: Favorite): Flow<ResultWrapper<Boolean>>

    fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class FavoriteRepositoryImpl(private val favoriteDataSource: FavoriteDataSource) : FavoriteRepository {
    override fun getUserFavoriteData(): Flow<ResultWrapper<Pair<List<Favorite>, Double>>> {
        return favoriteDataSource.getAllFavorites()
            .map {
                // mapping into Favorite list and sum the total price
                proceed {
                    val result = it.toFavoriteList()
                    val totalPrice = result.sumOf { it.catalogPrice }
                    Pair(result, totalPrice)
                }
            }.map {
                // map to check when list is empty
                if (it.payload?.first?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.onStart {
                emit(ResultWrapper.Loading())
                delay(1000)
            }
    }

    override fun deleteFavorite(item: Favorite): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { favoriteDataSource.deleteFavorite(item.toFavoriteEntity()) > 0 }
    }

    override fun deleteAll(): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            favoriteDataSource.deleteAll()
            true
        }
    }

    override fun createFavorite(catalog: Coin): Flow<ResultWrapper<Boolean>> {
        return catalog.id?.let { catalogId ->
            // when id is not null
            proceedFlow {
                val affectedRow =
                    favoriteDataSource.insertFavorite(
                        FavoriteEntity(
                            catalogId = catalogId,
                            catalogName = catalog.name,
                            catalogImgUrl = catalog.image,
                            catalogPrice = catalog.price,
                        ),
                    )
                delay(2000)
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("catalog ID not found")))
        }
    }
}
