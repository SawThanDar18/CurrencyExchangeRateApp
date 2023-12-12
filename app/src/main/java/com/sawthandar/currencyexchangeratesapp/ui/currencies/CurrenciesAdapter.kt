package com.sawthandar.currencyexchangeratesapp.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sawthandar.currencyexchangeratesapp.R
import com.sawthandar.currencyexchangeratesapp.data.offline.currencies.CurrencyItem
import com.sawthandar.currencyexchangeratesapp.utils.CurrenciesSearchFilter
import javax.inject.Inject

class CurrenciesAdapter @Inject constructor() :
    RecyclerView.Adapter<CurrenciesAdapter.ExchangeRateViewHolder>(),
    Filterable {

    class ExchangeRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var currenciesFiltered: ArrayList<CurrencyItem> = ArrayList()

    private var currencyItems: ArrayList<CurrencyItem> = ArrayList()

    fun submitList(currencyItems: List<CurrencyItem>) {
        currenciesFiltered.clear()
        currenciesFiltered.addAll(currencyItems)
        this.currencyItems.clear()
        this.currencyItems.addAll(currencyItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        return ExchangeRateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_currencies,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = currenciesFiltered.size

    var onCurrencyClicked: ((CurrencyItem) -> Unit)? = null

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        val currency = currenciesFiltered[position]

        var tvCurrency = holder.itemView.findViewById<TextView>(R.id.tvCurrency)
        var tvCurrencyLabel = holder.itemView.findViewById<TextView>(R.id.tvCurrencyLabel)

        tvCurrency.text = String.format("%s", currency.currency)
        tvCurrencyLabel.text = String.format("%s", currency.currencyLabel)

        holder.itemView.setOnClickListener {
            onCurrencyClicked?.invoke(currency)
        }
    }

    fun updateSearchResults(items: List<CurrencyItem>) {
        currenciesFiltered.clear()
        currenciesFiltered.addAll(items)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return CurrenciesSearchFilter(this, currencyItems)
    }
}