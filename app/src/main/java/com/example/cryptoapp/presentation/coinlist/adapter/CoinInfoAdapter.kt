package com.example.cryptoapp.presentation.coinlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ItemCoinInfoBinding
import com.example.cryptoapp.domain.model.CoinInfo
import com.squareup.picasso.Picasso

class CoinInfoAdapter() :
    ListAdapter<CoinInfo, CoinInfoAdapter.CoinInfoViewHolder>(CoinDiffCallback()) {

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val binding =
            ItemCoinInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CoinInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin, onCoinClickListener)
    }

    class CoinInfoViewHolder(private val binding: ItemCoinInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: CoinInfo, onCoinClickListener: OnCoinClickListener?) {
            with(binding) {
                with(coin) {
                    val symbolsTemplate = itemView.context.getString(R.string.symbols_template)
                    val lastUpdateTemplate =
                        itemView.context.getString(R.string.last_update_template)
                    textViewSymbols.text =
                        String.format(symbolsTemplate, fromSymbol, toSymbol)
                    textViewPrice.text = price.toString()
                    textViewLastUpdate.text = String.format(lastUpdateTemplate, lastUpdate)
                    Picasso.get().load(imageUrl).into(imageViewLogoCoin)
                    root.setOnClickListener {
                        onCoinClickListener?.onCoinClick(this)
                    }
                }
            }
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinInfo: CoinInfo)
    }
}