package com.nmc.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nmc.myapplication.api.retrofit.service.CurrencySupportedList
import com.nmc.myapplication.usecase.CurrencyConversionUseCase
import com.nmc.myapplication.usecase.Resource
import com.nmc.myapplication.usecase.ACCESS_RESTRICTED
import javax.inject.Inject

class CurrencyConversionViewModel @Inject constructor(
    private val currencyConversionUseCase: CurrencyConversionUseCase) : RxViewModel() {

    val currencyConversionUseCaseResponse = MutableLiveData<Resource<CurrencySupportedList>>()

    fun currencyConversionUseCase(arg: String) {
        compositeDisposable.add(currencyConversionUseCase.run(arg).subscribe({
            currencyConversionUseCaseResponse.value = it
        }, {
            currencyConversionUseCaseResponse.value = Resource.error(ACCESS_RESTRICTED)
        }))
    }
}

class CurrencyConversionViewModelFactory
@Inject constructor(
    private val currencyConversionUseCase: CurrencyConversionUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrencyConversionViewModel(
            currencyConversionUseCase
        ) as T
    }
}