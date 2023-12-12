package com.sawthandar.currencyexchangeratesapp.repositories.currencies

import com.sawthandar.currencyexchangeratesapp.data.offline.currencies.CurrencyDao
import com.sawthandar.currencyexchangeratesapp.data.offline.currencies.CurrencyItem
import com.sawthandar.currencyexchangeratesapp.data.online.APIs
import com.sawthandar.currencyexchangeratesapp.data.online.response.CurrenciesResponse
import com.sawthandar.currencyexchangeratesapp.utils.Constants.NO_INTERNET
import com.sawthandar.currencyexchangeratesapp.utils.Constants.SOMETHING_WENT_WRONG
import com.sawthandar.currencyexchangeratesapp.utils.Resource
import javax.inject.Inject

class DefaultCurrenciesRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val APIs: APIs
) : CurrenciesRepository {

    override suspend fun insertAllCurrencyItems(currencyItems: List<CurrencyItem>) {
        currencyDao.insertCurrencyItems(currencyItems)
    }

    override suspend fun insertCurrencyItem(currencyItem: CurrencyItem) {
        currencyDao.insertCurrencyItem(currencyItem)
    }

    override suspend fun deleteCurrencyItem(currencyItem: CurrencyItem) {
        currencyDao.deleteCurrencyItem(currencyItem)
    }

    override suspend fun deleteAllCurrencyItems() {
        currencyDao.deleteAllCurrencyItems()
    }

    override suspend fun getAllCurrencyItems(): List<CurrencyItem> {
        return currencyDao.getAllCurrencyItems()
    }

    override suspend fun getCurrencies(): Resource<CurrenciesResponse> {
        return try {
            val response = APIs.getCurrencies()
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












