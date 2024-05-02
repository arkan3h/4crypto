package com.arkan.a4crypto.data.datasource

import com.arkan.a4crypto.data.source.network.model.CoinDetailResponse
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices

interface CoinDetailDataSource {
    suspend fun getCoinDetail(id: String?): CoinDetailResponse
}

class CoinDetailDataSourceImpl(private val service: FourCryptoApiServices) : CoinDetailDataSource {
    override suspend fun getCoinDetail(id: String?): CoinDetailResponse {
        return service.getCoinDetail(id = id)
    }
}
