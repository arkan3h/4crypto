package com.arkan.a4crypto.data.source.network.model

import com.google.gson.annotations.SerializedName

data class CoinDetailResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("image")
    val image: Image?,
    @SerializedName("description")
    val description: Description?,
    @SerializedName("market_data")
    val market: Market?,
)

data class Image(
    @SerializedName("large")
    var large: String? = null,
    @SerializedName("small")
    var small: String? = null,
    @SerializedName("thumb")
    var thumb: String? = null
)

data class Description(
    @SerializedName("en")
    var en: String? = null
)

data class Market(
    @SerializedName("current_price")
    var price: Price? = null
)

data class Price(
    @SerializedName("usd")
    var usd: Double? = null
)
