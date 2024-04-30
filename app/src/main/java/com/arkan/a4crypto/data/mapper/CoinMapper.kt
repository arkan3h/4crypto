package com.arkan.a4crypto.data.mapper

import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.data.source.network.model.CoinResponse

fun CoinResponse.toCoin() =
    Coin(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        desc = this.desc.orEmpty(),
        image = this.image.orEmpty(),
        price = this.price ?: 0.0,
    )

fun Collection<CoinResponse>.toCoin() =
    this.map {
        it.toCoin()
    }
