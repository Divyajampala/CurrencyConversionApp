package com.nmc.myapplication.di

import com.example.currencyConversionApp.MainActivity
import com.example.currencyConversionApp.OpenExchangeApplication
import dagger.Component

@PerApplication
@Component(
    modules = arrayOf(
        OpenExchangeModule::class,
        RetrofitModule::class,
        DatabaseModule::class
    )
)
interface OpenExchangeComponent {
    fun inject(takshilaApplication: OpenExchangeApplication)
    fun inject(activity: MainActivity)
}
