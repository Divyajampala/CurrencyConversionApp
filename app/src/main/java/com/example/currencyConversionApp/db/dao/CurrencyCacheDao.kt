package com.nmc.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nmc.myapplication.db.entity.CurrencyCache

@Dao
interface CurrencyCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyCache: CurrencyCache)

    @Query("SELECT * FROM CurrencyCache WHERE id = :key")
    fun getByKey(key: String): CurrencyCache?
}