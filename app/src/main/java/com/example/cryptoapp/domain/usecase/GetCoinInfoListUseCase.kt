package com.example.cryptoapp.domain.usecase

import androidx.lifecycle.LiveData
import com.example.cryptoapp.domain.model.CoinInfo
import com.example.cryptoapp.domain.repository.CoinRepository

class GetCoinInfoListUseCase(private val repository: CoinRepository) {

    operator fun invoke(): LiveData<List<CoinInfo>> = repository.getCoinInfoList()
}