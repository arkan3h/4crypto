package com.arkan.a4crypto.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "catalog_id")
    var catalogId: String? = null,
    @ColumnInfo(name = "catalog_name")
    var catalogName: String,
    @ColumnInfo(name = "catalog_img_url")
    var catalogImgUrl: String,
    @ColumnInfo(name = "catalog_price")
    var catalogPrice: Double,
)
