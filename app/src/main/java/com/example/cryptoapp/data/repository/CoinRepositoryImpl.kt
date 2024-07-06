package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.database.AppDataBase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.workers.RefreshDataWorker
import com.example.cryptoapp.domain.model.CoinInfo
import com.example.cryptoapp.domain.repository.CoinRepository

class CoinRepositoryImpl(private val application: Application) : CoinRepository {

    private val dao = AppDataBase.getInstance(application).coinInfoDao()

    private val mapper = CoinMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        val coinInfoList = dao.getPriceList()
        return coinInfoList.map { coinInfoEntities ->
            coinInfoEntities.map {
                mapper.mapEntityToDomain(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return dao.getPriceInfoAboutCoin(fromSymbol).map { coinInfoEntity ->
            mapper.mapEntityToDomain(coinInfoEntity)
        }
    }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }
}