package com.sawthandar.currencyexchangeratesapp.data.offline.currencies

import androidx.room.*

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyItems(currencyItems: List<CurrencyItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyItem(currencyItem: CurrencyItem)

    @Delete
    suspend fun deleteCurrencyItem(currencyItem: CurrencyItem)

    @Query("DELETE FROM currency_items")
    suspend fun deleteAllCurrencyItems()

    @Query("SELECT * FROM currency_items")
    fun getAllCurrencyItems(): List<CurrencyItem>

}