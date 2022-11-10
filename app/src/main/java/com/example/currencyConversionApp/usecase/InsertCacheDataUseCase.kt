package com.example.currencyConversionApp.usecase

import com.nmc.myapplication.db.dao.CurrencyCacheDao
import com.nmc.myapplication.db.entity.CurrencyCache
import com.nmc.myapplication.usecase.InteractorSingle
import com.nmc.myapplication.usecase.Resource
import io.reactivex.Single
import javax.inject.Inject

class InsertCacheDataUseCase @Inject constructor(val dao: CurrencyCacheDao) :
    InteractorSingle<CurrencyCache, Resource<Unit>>() {

    override fun buildUseCase(params: CurrencyCache): Single<Resource<Unit>> {
        return Single.just(Resource.success(dao.insert(params)))
    }
}