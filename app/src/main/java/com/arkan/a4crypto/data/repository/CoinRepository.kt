package com.arkan.a4crypto.data.repository

import com.arkan.a4crypto.data.datasource.CoinDataSource
import com.arkan.a4crypto.data.mapper.toCoin
import com.arkan.a4crypto.data.model.Coin
import com.arkan.aresto.utils.ResultWrapper
import com.arkan.aresto.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoinList(): Flow<ResultWrapper<List<Coin>>>
}

class CoinRepositoryImpl(private val dataSource: CoinDataSource) : CoinRepository {
    override fun getCoinList(): Flow<ResultWrapper<List<Coin>>> {
        return proceedFlow { dataSource.getCoinList().toCoin() }
    }
}
