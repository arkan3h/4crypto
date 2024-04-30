package com.arkan.a4crypto.data.source.network.model

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("current_price")
    val price: Double?,
)
