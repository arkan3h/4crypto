package com.arkan.a4crypto.data.mapper

import com.arkan.a4crypto.data.model.CoinDetail
import com.arkan.a4crypto.data.source.network.model.CoinDetailResponse

fun CoinDetailResponse.toCoinDetail() =
    CoinDetail(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        symbol = this.symbol.orEmpty(),
        image = this.image?.large.orEmpty(),
        desc = this.description?.en.orEmpty(),
        price = this.market?.price?.usd ?: 0.0,
    )
