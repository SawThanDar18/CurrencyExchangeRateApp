package com.sawthandar.currencyexchangeratesapp.repositories.currencies

import com.sawthandar.currencyexchangeratesapp.data.offline.currencies.CurrencyItem
import com.sawthandar.currencyexchangeratesapp.data.online.response.CurrenciesResponse
import com.sawthandar.currencyexchangeratesapp.utils.Resource

interface CurrenciesRepository {

    suspend fun insertAllCurrencyItems(currencyItems: List<CurrencyItem>)

    suspend fun insertCurrencyItem(currencyItem: CurrencyItem)

    suspend fun deleteCurrencyItem(currencyItem: CurrencyItem)

    suspend fun deleteAllCurrencyItems()

    suspend fun getAllCurrencyItems(): List<CurrencyItem>

    suspend fun getCurrencies(): Resource<CurrenciesResponse>
}