package com.sawthandar.currencyexchangeratesapp.data.offline.exchange

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_items")
data class ExchangeItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var sourceCurrency: String = "",
    var currency: String = "",
    var amount: Double = 0.0
)