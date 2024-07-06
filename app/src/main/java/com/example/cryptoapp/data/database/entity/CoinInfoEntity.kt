package com.example.cryptoapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "full_price_list")
data class CoinInfoEntity(
    @PrimaryKey
    val fromSymbol: String,
    val toSymbol: String?,
    val lastMarket: String?,
    val price: String?,
    val lastUpdate: Long?,
    val highDay: String?,
    val lowDay: String?,
    val imageUrl: String
)