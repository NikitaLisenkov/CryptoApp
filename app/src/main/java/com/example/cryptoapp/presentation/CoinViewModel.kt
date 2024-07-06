package com.example.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cryptoapp.data.repository.CoinRepositoryImpl
import com.example.cryptoapp.domain.usecase.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.usecase.GetCoinInfoUseCase
import com.example.cryptoapp.domain.usecase.LoadDataUseCase

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = CoinRepositoryImpl(application)

    private val getCoinInfoListUseCase = GetCoinInfoListUseCase(repo)
    private val getCoinInfoUseCase = GetCoinInfoUseCase(repo)
    private val loadDataUseCase = LoadDataUseCase(repo)

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)

    init {
        loadDataUseCase()
    }
}
