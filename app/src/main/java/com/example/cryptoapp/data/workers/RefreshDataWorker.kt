package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.AppDataBase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.api.ApiFactory
import kotlinx.coroutines.delay

class RefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val dao = AppDataBase.getInstance(context).coinInfoDao()

    private val api = ApiFactory.apiService

    private val mapper = CoinMapper()

    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoins = api.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = api.getFullPriceList(fSyms = fSyms)
                val coinDtoList = mapper.mapJsonToListCoinInfo(jsonContainer)
                val entityList = coinDtoList.map { mapper.mapDtoToEntity(it) }
                dao.insertPriceList(entityList)
            } catch (e: Exception) {
                throw e
            }
            delay(10000)
        }
    }

    companion object {

        const val NAME = "RefreshDataWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<RefreshDataWorker>().build()
        }
    }
}