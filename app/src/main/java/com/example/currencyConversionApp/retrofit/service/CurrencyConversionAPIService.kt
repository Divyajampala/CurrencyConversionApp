package com.nmc.myapplication.api.retrofit.service

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConversionAPIService {

    //https://openexchangerates.org/api/latest.json?app_id=YOUR_APP_ID
    @GET("/api/latest.json")
    fun getCurrencySupportedListInfo(@Query("app_id") appId: String): Single<Response<CurrencySupportedList>>

}

data class CurrencySupportedList(
    val base: String?,
    val disclaimer: String?,
    val license: String?,
    val rates: Map<String, Double>?,
    val timestamp: Int?,
    override val error: Boolean,
    override val status: Int,
    override val message: String,
    override val description: String,
) : Error

interface Error {
    val error: Boolean
    val status: Int
    val message: String
    val description: String
}

