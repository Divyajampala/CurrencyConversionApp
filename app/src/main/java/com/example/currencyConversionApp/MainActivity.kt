package com.example.currencyConversionApp

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.coroutineScope
import com.example.currencyConversionApp.adapter.CurrencyConversionListAdapter
import com.example.currencyConversionApp.databinding.ActivityMainBinding
import com.nmc.myapplication.api.retrofit.service.CurrencySupportedList
import com.nmc.myapplication.extension.UsdBaseToOtherCurrency
import com.nmc.myapplication.usecase.Resource
import com.nmc.myapplication.usecase.Status
import com.nmc.myapplication.viewmodel.CurrencyConversionViewModel
import com.nmc.myapplication.viewmodel.CurrencyConversionViewModelFactory
import javax.inject.Inject
import androidx.recyclerview.widget.GridLayoutManager
import com.nmc.myapplication.db.entity.CurrencyCache
import com.nmc.myapplication.extension.isConnectionAvailable
import com.nmc.myapplication.extension.trackNetworkConnection
import com.nmc.myapplication.viewmodel.CacheViewDataModel
import com.nmc.myapplication.viewmodel.CacheViewDataModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var list: Map<String, Double>? = null

    private lateinit var binding: ActivityMainBinding

    private lateinit var cacheViewDataModel: CacheViewDataModel

    private lateinit var currencyConversionViewModel: CurrencyConversionViewModel

    @Inject
    lateinit var currencyConversionViewModelFactory: CurrencyConversionViewModelFactory

    @Inject
    lateinit var cacheViewDataModelFactory: CacheViewDataModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OpenExchangeApplication.getApplicationComponent()?.inject(this)
        initView()
        initViewModel()
        initObservers()
        cacheViewDataModel.fetchCurrencyListResponse(!this.isConnectionAvailable(), "CurrencyList")
    }

    /**
     * initialize the views.
     */
    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.contentMain.listCurrencyConversion.layoutManager = GridLayoutManager(this, 2)
        binding.contentMain.enterCurrencyValue.doAfterTextChanged { currentConversion() }
        if (!isConnectionAvailable()) {
            this.trackNetworkConnection(networkCallback)
        }
    }

    /**
     * Convert base currency to all other currency.
     */
    private fun currentConversion() {
        if (!TextUtils.isEmpty(binding.contentMain.enterCurrencyValue.getText())
        ) {
            binding.contentMain.listCurrencyConversion.visibility = VISIBLE
            list?.let {
                var convertedCurrencyList = UsdBaseToOtherCurrency(
                    binding.contentMain.enterCurrencyValue.getText().toString().toDouble(),
                    it,
                    binding.contentMain.baseCurrencyType.selectedItem.toString()
                )

                binding.contentMain.listCurrencyConversion.let {
                    if (it.adapter == null) {
                        it.adapter = CurrencyConversionListAdapter(convertedCurrencyList)
                    } else {
                        (it.adapter as CurrencyConversionListAdapter).setCurrencyConversionList(
                            convertedCurrencyList
                        )
                    }
                }
            }
        } else {
            binding.contentMain.listCurrencyConversion.visibility = GONE
        }
    }

    /**
     * initialize the Observers.
     */
    private fun initObservers() {
        // api fetch observer
        currencyConversionViewModel.currencyConversionUseCaseResponse.observe(
            this,
            Observer { response ->
                handleSupportedCurrencyResponse(response)
            })

        // cache data observer
        cacheViewDataModel.fetchCurrencyListResponse.observe(this, Observer { response ->
            if (response == null) {
                fetchCurrencyData()
                return@Observer
            }
            response.map { it.key }.let {
                list = response
                setSpinner(it)
            }
        })
    }

    /**
     * Sets the ui with Api fetched currency list.
     */
    private fun handleSupportedCurrencyResponse(response: Resource<CurrencySupportedList>?) {
        if (response?.status == Status.SUCCESS) {
            response.data?.rates?.map { it.key }?.let {
                list = response.data.rates
                setSpinner(it)
                list?.let {
                    if (it.size > 0) {
                        cacheViewDataModel.cacheCurrencyData(
                            CurrencyCache(
                                "CurrencyList",
                                list.toString(),
                                System.currentTimeMillis().toString()
                            )
                        )
                    }
                }
                binding.messageView.visibility = GONE
            }
        } else {
            //show error message
            binding.messageView.visibility = VISIBLE
            binding.messageView.text = response?.errorMessage
        }
        hideProgressBar()
    }

    /**
     * Hide Progress Bar.
     */
    private fun hideProgressBar() {
        binding.progressBar.visibility = GONE
    }

    /**
     * Show Progress Bar.
     */
    private fun showProgressBar() {
        binding.progressBar.visibility = VISIBLE
    }

    /**
     * Sets the spinner data.
     */
    private fun setSpinner(list: List<String>) {
        binding.contentMain.baseCurrencyType.onItemSelectedListener = this
        object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                var view = super.getDropDownView(position, convertView, parent) as TextView
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return if (position == 0) false else super.isEnabled(position)
            }
        }.also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.contentMain.baseCurrencyType.adapter = adapter
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, parent: View?, position: Int, p3: Long) {
        currentConversion()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //nothing to do
    }

    /**
     * initialize the ViewModel.
     */
    private fun initViewModel() {
        currencyConversionViewModel =
            ViewModelProviders.of(this, currencyConversionViewModelFactory)
                .get(CurrencyConversionViewModel::class.java)
        cacheViewDataModel =
            ViewModelProviders.of(this, cacheViewDataModelFactory)
                .get(CacheViewDataModel::class.java)
    }

    /**
     * Network Callack : When Internet turned on and UI has not data, fetch the data.
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            uiScope.launch {
                // updating in UI thread.
                if (binding.contentMain.baseCurrencyType.selectedItem == null) {
                    fetchCurrencyData()
                }
            }
        }
    }

    /**
     * API call to fetch Currency Data.
     */
    private fun fetchCurrencyData() {
        showProgressBar()
        currencyConversionViewModel.currencyConversionUseCase(BuildConfig.OPEN_EXCHANGE_RATES_ID)
    }
}