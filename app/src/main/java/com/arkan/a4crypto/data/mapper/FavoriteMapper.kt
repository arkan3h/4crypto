package com.arkan.a4crypto.data.mapper
import com.arkan.a4crypto.data.model.Favorite
import com.arkan.a4crypto.data.source.entity.FavoriteEntity

fun Favorite?.toFavoriteEntity() =
    FavoriteEntity(
        id = this?.id,
        catalogId = this?.catalogId.orEmpty(),
        catalogName = this?.catalogName.orEmpty(),
        catalogPrice = this?.catalogPrice ?: 0.0,
        catalogImgUrl = this?.catalogImgUrl.orEmpty(),
    )

fun FavoriteEntity?.toFavorite() =
    Favorite(
        id = this?.id,
        catalogId = this?.catalogId.orEmpty(),
        catalogName = this?.catalogName.orEmpty(),
        catalogPrice = this?.catalogPrice ?: 0.0,
        catalogImgUrl = this?.catalogImgUrl.orEmpty(),
    )

fun List<FavoriteEntity?>.toFavoriteList() = this.map { it.toFavorite() }
