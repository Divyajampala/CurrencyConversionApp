package com.example.currencyConversionApp.usecase

import com.nmc.myapplication.db.dao.CurrencyCacheDao
import com.nmc.myapplication.db.entity.CurrencyCache
import com.nmc.myapplication.usecase.InteractorSingle
import com.nmc.myapplication.usecase.Resource
import io.reactivex.Single
import javax.inject.Inject

class FetchCacheDataUseCase @Inject constructor(val dao: CurrencyCacheDao) :
    InteractorSingle<String, Resource<CurrencyCache>>() {

    override fun buildUseCase(params: String): Single<Resource<CurrencyCache>> {
        return Single.just(Resource.success(dao.getByKey(params)))
    }
}