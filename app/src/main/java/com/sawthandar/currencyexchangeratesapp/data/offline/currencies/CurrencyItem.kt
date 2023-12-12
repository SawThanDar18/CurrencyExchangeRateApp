package com.sawthandar.currencyexchangeratesapp.data.offline.currencies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_items")
data class CurrencyItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var currency: String = "",
    var currencyLabel: String = ""
)