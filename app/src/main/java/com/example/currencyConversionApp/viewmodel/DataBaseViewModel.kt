package com.nmc.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyConversionApp.usecase.FetchCacheDataUseCase
import com.example.currencyConversionApp.usecase.InsertCacheDataUseCase
import com.nmc.myapplication.usecase.*
import javax.inject.Inject
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import com.nmc.myapplication.db.entity.CurrencyCache
import java.lang.reflect.Type

class CacheViewDataModel @Inject constructor(
    private val insertCacheViewDataUseCase: InsertCacheDataUseCase,
    private val fetchCacheDataUseCase: FetchCacheDataUseCase
) : RxViewModel() {

    val cacheCurrencyListResponse = MutableLiveData<Resource<Unit>>()

    val fetchCurrencyListResponse = MutableLiveData<Map<String, Double>?>()

    /**
     * cache the currency data in Database.
     */
    fun cacheCurrencyData(key: CurrencyCache) {
        compositeDisposable.add(insertCacheViewDataUseCase.run(key).subscribe({
            cacheCurrencyListResponse.value = it
        }, {
            cacheCurrencyListResponse.value = Resource.error(ACCESS_RESTRICTED)
        }))
    }

    /**
     * fetch the cached currency data from Database,
     * if offline or last Api fetch data time duration is more then 30 mins .
     */
    fun fetchCurrencyListResponse(isAppOffline : Boolean, key: String) {
        compositeDisposable.add(fetchCacheDataUseCase.run(key).subscribe({ response ->
            response.data?.timeStamp?.toLong()?.let {
                if (isAppOffline || it.plus(30 * 60000) > System.currentTimeMillis()) {
                    // fetch - if offline or last Api fetch data time duration is more then 30 mins .
                    fetchCurrencyListResponse.value = getObjectFromString(response.data?.value)
                } else {
                    fetchCurrencyListResponse.value = null
                }
                return@subscribe
            }
            fetchCurrencyListResponse.value = null
        }, {
            fetchCurrencyListResponse.value = null
        }))
    }


    fun getObjectFromString(jsonString: String?): Map<String, Double>? {
        val listType: Type = object : TypeToken<Map<String, Double>?>() {}.type
        return Gson().fromJson<Map<String, Double>?>(jsonString, listType)
    }
}

/**
 * ViewModel Factory
 */
class CacheViewDataModelFactory
@Inject constructor(
    private val insertCacheViewDataUseCase: InsertCacheDataUseCase,
    private val fetchCacheDataUseCase: FetchCacheDataUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CacheViewDataModel(
            insertCacheViewDataUseCase,
            fetchCacheDataUseCase
        ) as T
    }
}