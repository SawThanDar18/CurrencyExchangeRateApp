package com.sawthandar.currencyexchangeratesapp.ui.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sawthandar.currencyexchangeratesapp.data.offline.exchange.ExchangeItem
import com.sawthandar.currencyexchangeratesapp.data.online.response.ExchangeRatesResponse
import com.sawthandar.currencyexchangeratesapp.repositories.exchangerate.ExchangeRateRepository
import com.sawthandar.currencyexchangeratesapp.utils.Constants
import com.sawthandar.currencyexchangeratesapp.utils.Event
import com.sawthandar.currencyexchangeratesapp.utils.Resource
import com.sawthandar.currencyexchangeratesapp.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val repository: ExchangeRateRepository
) : ViewModel() {

    private val _exchangeRates = MutableLiveData<Event<Resource<ArrayList<ExchangeItem>>>>()
    val exchangeRates: LiveData<Event<Resource<ArrayList<ExchangeItem>>>> = _exchangeRates

    fun updateExchangeRatesLiveData(response: Resource<ArrayList<ExchangeItem>>) {
        _exchangeRates.postValue(Event(response))
    }

    fun deleteAllExchangeRates(sourceCurrency: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllExchangeItems(sourceCurrency)
    }

    fun insertExchangeRatesIntoDb(exchangeItems: List<ExchangeItem>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAllExchangeItems(exchangeItems)
        }

    fun getExchangeRates(source: String) {
        _exchangeRates.value = Event(Resource.loading(null))

        viewModelScope.launch(Dispatchers.IO) {
            val dbResponse = ArrayList(repository.getAllExchangeItems(source))
            viewModelScope.launch {
                updateExchangeRatesLiveData(Resource.success(dbResponse))
                if (dbResponse.isEmpty()) {
                    val apiResponse = repository.getExchangeRates(source)
                    validateResponse(apiResponse, source)
                }
            }
        }

    }

    private fun validateResponse(
        response: Resource<ExchangeRatesResponse>,
        sourceCurrency: String
    ) {
        val exchangeRates: ArrayList<ExchangeItem> = ArrayList()
        when (response.status) {
            Status.SUCCESS -> {
                response.data?.quotes?.forEach { (currency, amount) ->
                    val toCurrency = currency.replace(sourceCurrency, "")
                    if (toCurrency.isNotEmpty())
                        exchangeRates.add(
                            ExchangeItem(
                                sourceCurrency = sourceCurrency,
                                currency = toCurrency, amount = amount
                            )
                        )
                }
                if (exchangeRates.isNotEmpty()) {
                    deleteAllExchangeRates(sourceCurrency)
                    insertExchangeRatesIntoDb(exchangeRates)
                }
                updateExchangeRatesLiveData(Resource.success(exchangeRates))
            }
            else -> {
                updateExchangeRatesLiveData(
                    Resource.error(
                        response.message ?: Constants.NO_INTERNET, exchangeRates
                    )
                )
            }
        }
    }

    init {
        getExchangeRates(Constants.DEFAULT_SOURCE_CURRENCY)
    }
}






