package com.arkan.a4crypto.data.repository

import com.arkan.a4crypto.data.datasource.CoinDetailDataSource
import com.arkan.a4crypto.data.mapper.toCoinDetail
import com.arkan.a4crypto.data.model.CoinDetail
import com.arkan.aresto.utils.ResultWrapper
import com.arkan.aresto.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface CoinDetailRepository {
    fun getCoinDetail(id: String): Flow<ResultWrapper<List<CoinDetail>>>
}

class CoinDetailRepositoryImpl(
    private val dataSource: CoinDetailDataSource,
) : CoinDetailRepository {
    override fun getCoinDetail(id: String): Flow<ResultWrapper<List<CoinDetail>>> {
        return proceedFlow { dataSource.getCoinDetail(id = id).toCoinDetail() }
    }
}
