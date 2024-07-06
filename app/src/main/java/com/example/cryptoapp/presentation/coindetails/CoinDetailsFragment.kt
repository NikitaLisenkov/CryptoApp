package com.example.cryptoapp.presentation.coindetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentCoinDetailsBinding
import com.example.cryptoapp.presentation.CoinViewModel
import com.squareup.picasso.Picasso

class CoinDetailsFragment : Fragment(R.layout.fragment_coin_details) {

    private val binding by viewBinding(FragmentCoinDetailsBinding::bind)

    private val viewModel: CoinViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fromSymbol = getSymbol()
        fromSymbol?.let {
            viewModel.getDetailInfo(it).observe(viewLifecycleOwner) { coinInfo ->
                with(binding) {
                    with(coinInfo) {
                        tvPrice.text = price.toString()
                        tvMinPrice.text = lowDay.toString()
                        tvMaxPrice.text = highDay.toString()
                        tvLastMarket.text = lastMarket
                        tvLastUpdate.text = lastUpdate
                        tvFromSymbol.text = fromSymbol
                        tvToSymbol.text = toSymbol
                        Picasso
                            .get()
                            .load(imageUrl)
                            .into(ivLogoCoin)
                    }
                }
            }
        }
    }

    private fun getSymbol(): String? {
        return requireArguments().getString(EXTRA_FROM_SYMBOL, EMPTY_SYMBOL)
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "EXTRA_FROM_SYMBOL"
        private const val EMPTY_SYMBOL = ""

        fun newInstance(fromSymbol: String): CoinDetailsFragment {
            return CoinDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_FROM_SYMBOL, fromSymbol)
                }
            }
        }
    }
}