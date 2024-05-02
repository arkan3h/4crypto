package com.arkan.a4crypto.data.model

data class Favorite(
    var id: Int? = null,
    var catalogId: String? = null,
    var catalogName: String,
    var catalogImgUrl: String,
    var catalogPrice: Double,
)
