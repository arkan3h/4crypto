package com.arkan.a4crypto.data.datasource

import com.arkan.a4crypto.data.source.network.model.CoinDetailResponse
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices

interface CoinDetailDataSource {
    suspend fun getCoinDetail(id : String) : List<CoinDetailResponse>
}

class CoinDetailDataSourceImpl(private val service: FourCryptoApiServices) : CoinDetailDataSource {
    override suspend fun getCoinDetail(id : String): List<CoinDetailResponse> {
        return service.getCoinDetail(id = id)
    }
}