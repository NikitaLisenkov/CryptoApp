package com.example.cryptoapp.presentation.coinlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.cryptoapp.domain.model.CoinInfo

class CoinDiffCallback : DiffUtil.ItemCallback<CoinInfo>() {

    override fun areItemsTheSame(oldItem: CoinInfo, newItem: CoinInfo): Boolean {
        return oldItem.fromSymbol == newItem.fromSymbol
    }

    override fun areContentsTheSame(oldItem: CoinInfo, newItem: CoinInfo): Boolean {
        return oldItem == newItem
    }
}