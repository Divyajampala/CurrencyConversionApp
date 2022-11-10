package com.nmc.myapplication.di

import android.content.Context
import com.example.currencyConversionApp.OpenExchangeApplication
import dagger.Module
import dagger.Provides

@Module
class OpenExchangeModule(private val app: OpenExchangeApplication) {

    @Provides
    @PerApplication
    fun provideApplication() = app

    @Provides
    fun provideContext(): Context = app.applicationContext

}
