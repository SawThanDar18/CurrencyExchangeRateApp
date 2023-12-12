package com.sawthandar.currencyexchangeratesapp.repositories.exchangerate

import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeItem
import com.sawthandar.currencyexchangeratesapp.data.online.response.ExchangeRatesResponse
import com.sawthandar.currencyexchangeratesapp.utils.Resource

interface ExchangeRateRepository {

    suspend fun insertAllExchangeItems(exchangeItems: List<ExchangeItem>)

    suspend fun insertExchangeItem(exchangeItem: ExchangeItem)

    suspend fun deleteExchangeItem(exchangeItem: ExchangeItem)

    suspend fun deleteAllExchangeItems()

    suspend fun deleteAllExchangeItems(selectedSourceCurrency: String)

    suspend fun getAllExchangeItems(): List<ExchangeItem>

    suspend fun getAllExchangeItems(selectedSourceCurrency: String): List<ExchangeItem>

    suspend fun getExchangeRates(selectedSourceCurrency: String): Resource<ExchangeRatesResponse>
}