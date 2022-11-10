package com.example.currencyConversionApp

import android.app.Application
import com.nmc.myapplication.di.*

/**
 * App Application Class.
 */
class OpenExchangeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        var appModule = OpenExchangeModule(this)
        //Dagger
        appComponent = DaggerOpenExchangeComponent.builder()
            .openExchangeModule(appModule)
            .databaseModule(DatabaseModule())
            .retrofitModule(RetrofitModule()).build()
    }

    companion object {
        private var appComponent: OpenExchangeComponent? = null
        fun getApplicationComponent(): OpenExchangeComponent? {
            return appComponent
        }
    }
}