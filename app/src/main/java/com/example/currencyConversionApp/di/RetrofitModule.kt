package com.nmc.myapplication.di

import com.example.currencyConversionApp.retrofit.APIClient
import com.nmc.myapplication.api.retrofit.service.*
import dagger.Module
import dagger.Provides

@Module
class RetrofitModule {

    @Provides
    @PerApplication
    fun provideClassApiService(): CurrencyConversionAPIService =
        APIClient.getRetrofitClient().create(CurrencyConversionAPIService::class.java)
}
