package com.arkan.a4crypto.data.datasource

import com.arkan.a4crypto.data.source.network.model.CoinResponse
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices

interface CoinDataSource {
    suspend fun getCoinList(): List<CoinResponse>
}

class CoinDataSourceImpl(private val service: FourCryptoApiServices) : CoinDataSource {
    override suspend fun getCoinList(): List<CoinResponse> {
        return service.getCoinList()
    }
}
