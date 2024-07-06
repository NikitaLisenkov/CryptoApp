package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.entity.CoinInfoEntity
import com.example.cryptoapp.data.network.api.ApiFactory
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.domain.model.CoinInfo
import com.example.cryptoapp.utils.convertTimestampToTime
import com.google.gson.Gson

class CoinMapper {

    fun mapDtoToEntity(dto: CoinInfoDto): CoinInfoEntity {
        return CoinInfoEntity(
            fromSymbol = dto.fromSymbol,
            toSymbol = dto.toSymbol,
            lastMarket = dto.lastMarket,
            price = dto.price.toString(),
            lastUpdate = dto.lastUpdate?.toLong(),
            highDay = dto.highDay.toString(),
            lowDay = dto.lowDay.toString(),
            imageUrl = ApiFactory.BASE_IMAGE_URL + dto.imageUrl
        )
    }

    fun mapJsonToListCoinInfo(json: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = json.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson =
                jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(listDto: CoinNamesListDto): String {
        return listDto.names?.map {
            it.coinNameDto?.name
        }?.joinToString(",").toString() ?: ""
    }

    fun mapEntityToDomain(entity: CoinInfoEntity): CoinInfo {
        return CoinInfo(
            fromSymbol = entity.fromSymbol,
            toSymbol = entity.toSymbol,
            lastMarket = entity.lastMarket,
            price = entity.price,
            lastUpdate = convertTimestampToTime(entity.lastUpdate?.toInt()),
            highDay = entity.highDay,
            lowDay = entity.lowDay,
            imageUrl = entity.imageUrl
        )
    }
}