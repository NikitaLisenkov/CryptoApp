package com.example.cryptoapp.domain.usecase

import androidx.lifecycle.LiveData
import com.example.cryptoapp.domain.model.CoinInfo
import com.example.cryptoapp.domain.repository.CoinRepository

class GetCoinInfoUseCase(private val repository: CoinRepository) {

    operator fun invoke(fromSymbol: String): LiveData<CoinInfo> = repository.getCoinInfo(fromSymbol)
}