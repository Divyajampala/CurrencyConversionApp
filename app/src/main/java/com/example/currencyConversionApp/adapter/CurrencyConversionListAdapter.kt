package com.example.currencyConversionApp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyConversionApp.R
import com.example.currencyConversionApp.databinding.CurrencyConvertedRvItemNewBinding

/**
 * Currency Conversion Recycler View Adapter
 */
class CurrencyConversionListAdapter(results: Map<String, Double>?) :
    RecyclerView.Adapter<CurrencyConversionListAdapter.CurrencyConversionViewHolder>() {

    var results: Map<String, Double>? = null

    init {
        results.also { this.results = it }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyConversionViewHolder {
        val mBinding: CurrencyConvertedRvItemNewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.currency_converted_rv_item_new,
            parent,
            false
        )
        return CurrencyConversionViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: CurrencyConversionViewHolder, position: Int) {
        val firstKey: String? = results?.keys?.toTypedArray()?.get(position)
        val valueForFirstKey: Double? = results?.get(firstKey)

        holder.itemBinding.currencyValue = "$firstKey  $valueForFirstKey"
    }

    override fun getItemCount(): Int {
        return results?.size ?: 0
    }

    inner class CurrencyConversionViewHolder(val itemBinding: CurrencyConvertedRvItemNewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    fun setCurrencyConversionList(classList: Map<String, Double>) {
        results = classList
        notifyDataSetChanged()
    }
}