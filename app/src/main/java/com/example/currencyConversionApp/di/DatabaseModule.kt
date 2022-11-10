package com.nmc.myapplication.di

import android.content.Context
import com.nmc.myapplication.db.AppDatabase
import com.nmc.myapplication.db.AppDatabaseFactory
import com.nmc.myapplication.db.dao.CurrencyCacheDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideDatabase(context: Context): AppDatabase = AppDatabaseFactory.getDBInstance(context)

    @Provides
    fun provideApiCacheDao(db: AppDatabase): CurrencyCacheDao = db.apiCacheDao()
}
