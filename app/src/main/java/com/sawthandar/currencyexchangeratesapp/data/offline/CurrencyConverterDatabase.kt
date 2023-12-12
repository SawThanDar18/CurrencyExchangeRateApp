package com.sawthandar.currencyexchangeratesapp.data.offline

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sawthandar.currencyexchangeratesapp.data.offline.currencies.CurrencyDao
import com.sawthandar.currencyexchangeratesapp.data.offline.currencies.CurrencyItem
import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeItem
import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeRatesDao

@Database(
    entities = [CurrencyItem::class, ExchangeItem::class],
    version = 1
)
abstract class CurrencyConverterDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun exchangeRatesDao(): ExchangeRatesDao
}