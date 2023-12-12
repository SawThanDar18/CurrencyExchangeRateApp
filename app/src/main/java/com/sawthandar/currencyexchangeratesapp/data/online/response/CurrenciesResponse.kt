package com.sawthandar.currencyexchangeratesapp.data.online.response


import com.google.gson.annotations.SerializedName

data class CurrenciesResponse(
    @SerializedName("currencies")
    var currencies: HashMap<String, String> = HashMap(),
    @SerializedName("success")
    val success: Boolean,
)