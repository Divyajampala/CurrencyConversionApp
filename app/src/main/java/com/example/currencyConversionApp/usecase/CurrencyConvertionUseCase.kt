package com.nmc.myapplication.usecase

import com.nmc.myapplication.api.retrofit.service.CurrencyConversionAPIService
import com.nmc.myapplication.api.retrofit.service.CurrencySupportedList
import com.nmc.myapplication.extension.validateResponse
import io.reactivex.Single
import javax.inject.Inject

class CurrencyConversionUseCase @Inject constructor(val service: CurrencyConversionAPIService) :
    InteractorSingle<String, Resource<CurrencySupportedList>>() {

    override fun buildUseCase(params: String): Single<Resource<CurrencySupportedList>> {
        return getCurrencySupportedListInfo(params)
    }

    private fun getCurrencySupportedListInfo(params: String): Single<Resource<CurrencySupportedList>> {
        return service.getCurrencySupportedListInfo(params).map { it.validateResponse() }
    }
}