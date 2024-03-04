package com.example.cryptoapp.loaddata

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.api.ApiFactory
import com.example.cryptoapp.database.AppDataBase
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.example.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDataBase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    init {  //Автоматическая загрузка данных при запуске приложения и создания ViewModel
        loadData()
    }


    private fun loadData() {
        val disposable =
            ApiFactory.apiService.getTopCoinsInfo(limit = 50)// Получаем популярные валюты
                .map {
                    it.data?.map { it.coinInfo?.name }?.joinToString(",").toString()
                } //Список валют превращаем в 1 строку
                .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) } //Загрузка всей инфо о валютах
                .map { getPriceListFromRawData(it) } //Мапим RawData в CoinPriceInfo
                .delaySubscription(10, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .subscribeOn(Schedulers.io()) //Переключаем поток
                .subscribe({
                    db.coinPriceInfoDao().insertPriceList(it)
                    Log.d("TEST_OF_LOADING_DATA", it.toString())
                }, {
                    Log.d("TEST_OF_LOADING_DATA", it.message.toString())
                })
        compositeDisposable.add(disposable)
    }


    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData //на вход приходит объект,содержащий Json объект
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result //Получаем объект Raw
        val coinKeySet = jsonObject.keySet() //Берем у нашего Raw все ключи (BTC, ETH) и т.д.
        for (coinKey in coinKeySet) { //Перебираем все ключи
            val currencyJson =
                jsonObject.getAsJsonObject(coinKey) //У каждого ключа перебираем вложенные ключи (USD)
            val currencyKeySet = currencyJson.keySet() //Получаем все ключи из вложенного Jsona
            for (currencyKey in currencyKeySet) { //Проходимся по этим ключам
                val priceInfo = Gson().fromJson( //Создаем объект CoinPriceInfo и заполняем его
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo) //Добавляем полученный объект в нашу коллекцию
            }
        }
        return result
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
