package com.example.cryptoapp.domain.model

data class CoinInfo(
    val fromSymbol: String,
    val toSymbol: String?,
    val lastMarket: String?,
    val price: String?,
    val lastUpdate: String,
    val highDay: String?,
    val lowDay: String?,
    val imageUrl: String
)
