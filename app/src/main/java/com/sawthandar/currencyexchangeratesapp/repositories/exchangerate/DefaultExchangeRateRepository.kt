package com.sawthandar.currencyexchangeratesapp.repositories.exchangerate

import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeItem
import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeRatesDao
import com.sawthandar.currencyexchangeratesapp.data.online.APIs
import com.sawthandar.currencyexchangeratesapp.data.online.response.ExchangeRatesResponse
import com.sawthandar.currencyexchangeratesapp.utils.Constants.NO_INTERNET
import com.sawthandar.currencyexchangeratesapp.utils.Constants.SOMETHING_WENT_WRONG
import com.sawthandar.currencyexchangeratesapp.utils.Resource
import javax.inject.Inject

class DefaultExchangeRateRepository @Inject constructor(
    private val exchangeRatesDao: ExchangeRatesDao,
    private val APIs: APIs
) : ExchangeRateRepository {


    override suspend fun insertExchangeItem(exchangeItem: ExchangeItem) {
        exchangeRatesDao.insertExchangeItem(exchangeItem)
    }

    override suspend fun insertAllExchangeItems(exchangeItems: List<ExchangeItem>) {
        exchangeRatesDao.insertCurrencyItems(exchangeItems)
    }

    override suspend fun deleteExchangeItem(exchangeItem: ExchangeItem) {
        exchangeRatesDao.deleteExchangeItem(exchangeItem)
    }

    override suspend fun deleteAllExchangeItems() {
        exchangeRatesDao.getAllExchangeItems()
    }

    override suspend fun deleteAllExchangeItems(selectedSourceCurrency: String) {
        exchangeRatesDao.deleteAllCurrencyItems(selectedSourceCurrency)
    }

    override suspend fun getAllExchangeItems(): List<ExchangeItem> {
        return exchangeRatesDao.getAllExchangeItems()
    }

    override suspend fun getAllExchangeItems(selectedSourceCurrency: String): List<ExchangeItem> {
        return exchangeRatesDao.getAllExchangeItems(selectedSourceCurrency)
    }

    override suspend fun getExchangeRates(selectedSourceCurrency: String): Resource<ExchangeRatesResponse> {
        return try {
            val response = APIs.getExchangeRates(selectedSourceCurrency)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(SOMETHING_WENT_WRONG, null)
            } else {
                Resource.error(SOMETHING_WENT_WRONG, null)
            }
        } catch (e: Exception) {
            Resource.error(NO_INTERNET, null)
        }
    }
}














