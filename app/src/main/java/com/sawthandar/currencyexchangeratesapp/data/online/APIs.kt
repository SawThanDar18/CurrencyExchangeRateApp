package com.sawthandar.currencyexchangeratesapp.data.online

import com.sawthandar.currencyexchangeratesapp.data.online.response.CurrenciesResponse
import com.sawthandar.currencyexchangeratesapp.data.online.response.ExchangeRatesResponse
import com.sawthandar.currencyexchangeratesapp.utils.Constants.API_KEY
import com.sawthandar.currencyexchangeratesapp.utils.Constants.CURRENCIES_END_POINT
import com.sawthandar.currencyexchangeratesapp.utils.Constants.DEFAULT_SOURCE_CURRENCY
import com.sawthandar.currencyexchangeratesapp.utils.Constants.LIVE_END_POINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIs {

    @GET(LIVE_END_POINT)
    suspend fun getExchangeRates(
        @Query("source") source: String = DEFAULT_SOURCE_CURRENCY,
        @Query("apikey") apiKey: String = API_KEY
    ): Response<ExchangeRatesResponse>


    @GET(CURRENCIES_END_POINT)
    suspend fun getCurrencies(
        @Query("apikey") apiKey: String = API_KEY
    ): Response<CurrenciesResponse>

}