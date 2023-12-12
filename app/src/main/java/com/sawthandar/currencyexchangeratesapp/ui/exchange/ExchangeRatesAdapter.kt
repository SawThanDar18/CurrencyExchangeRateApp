package com.sawthandar.currencyexchangeratesapp.ui.exchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sawthandar.currencyexchangeratesapp.R
import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeItem
import com.sawthandar.currencyexchangeratesapp.utils.ExchangeRateSearchFilter
import javax.inject.Inject

class ExchangeRatesAdapter @Inject constructor() :
    RecyclerView.Adapter<ExchangeRatesAdapter.ExchangeRateViewHolder>(),
    Filterable {

    class ExchangeRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var conversionsFiltered: ArrayList<ExchangeItem> = ArrayList()
    private var conversions: ArrayList<ExchangeItem> = ArrayList()
    private var inputAmount: Double = 1.0

    fun submitList(conversions: List<ExchangeItem>, inputAmount: Double) {
        this.inputAmount = inputAmount
        this.conversions.clear()
        this.conversions.addAll(conversions)
        conversionsFiltered.clear()
        conversionsFiltered.addAll(conversions)
        notifyDataSetChanged()
    }

    fun updateExchangeRate(inputAmount: Double) {
        this.inputAmount = inputAmount
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        return ExchangeRateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_exchange_rate,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = conversionsFiltered.size

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        val currency = conversionsFiltered[position]

        var tvToCurrency = holder.itemView.findViewById<TextView>(R.id.tvToCurrency)
        var tvConversionRate = holder.itemView.findViewById<TextView>(R.id.tvConversionRate)

        holder.itemView.apply {
            tvToCurrency.text = String.format("%s", currency.currency)
            tvConversionRate.text = String.format("%,.2f", (currency.amount * inputAmount))
        }
    }

    fun updateSearchResults(items: List<ExchangeItem>) {
        conversionsFiltered.clear()
        conversionsFiltered.addAll(items)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return ExchangeRateSearchFilter(this, conversions)
    }
}