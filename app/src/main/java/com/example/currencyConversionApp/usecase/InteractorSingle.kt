package com.nmc.myapplication.usecase

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface BaseInteractor<in PARAMS> {
    fun buildUseCase(params: PARAMS): Any

    fun run(params: PARAMS): Any {
        return this.buildUseCase(params)
    }
}

abstract class InteractorSingle<in PARAMS, RESULT> : BaseInteractor<PARAMS> {

    abstract override fun buildUseCase(params: PARAMS): Single<RESULT>

    override fun run(params: PARAMS): Single<RESULT> {
        return this.buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}