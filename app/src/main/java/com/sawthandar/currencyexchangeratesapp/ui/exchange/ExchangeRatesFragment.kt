package com.sawthandar.currencyexchangeratesapp.ui.exchange

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sawthandar.currencyexchangeratesapp.R
import com.sawthandar.currencyexchangeratesapp.utils.Constants
import com.sawthandar.currencyexchangeratesapp.utils.Constants.SEARCH_TIME_DELAY
import com.sawthandar.currencyexchangeratesapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ExchangeRatesFragment @Inject constructor(
    private val exchangeRatesAdapter: ExchangeRatesAdapter,
    var viewModel: ExchangeRateViewModel? = null
) : Fragment(R.layout.fragment_exchange_rate) {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var llSelectedCurrency: LinearLayout
    private lateinit var etAmount: EditText
    private lateinit var rvExchangeRates: RecyclerView
    private lateinit var tvSelectedCurrency: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        llSelectedCurrency = view.findViewById(R.id.llSelectedCurrency)
        etAmount = view.findViewById(R.id.etAmount)
        rvExchangeRates = view.findViewById(R.id.rvExchangeRates)
        tvSelectedCurrency = view.findViewById(R.id.tvSelectedCurrency)

        viewModel =
            viewModel ?: ViewModelProvider(requireActivity()).get(ExchangeRateViewModel::class.java)

        subscribeToObservers()

        setupRecyclerView()

        listeners()
    }


    private fun listeners() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(Constants.KEY_SELECTED_CURRENCY)
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                updateSelectedCurrency(result)
            }

        llSelectedCurrency.setOnClickListener {
            navigateToCurrenciesFragment()
        }

        var job: Job? = null
        etAmount.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        exchangeRatesAdapter.updateExchangeRate(editable.toString().toDouble())
                    } else {
                        exchangeRatesAdapter.updateExchangeRate(1.0)
                    }
                }
            }
        }

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            viewModel?.getExchangeRates(getSelectedCurrency())
        }
        swipeRefreshLayout.setOnRefreshListener(refreshListener)
    }

    private fun subscribeToObservers() {
        viewModel?.exchangeRates?.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        exchangeRatesAdapter.submitList(
                            result.data ?: emptyList(),
                            getInputAmount()
                        )
                        swipeRefreshLayout.isRefreshing = false
                    }

                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            result.message ?: Constants.SOMETHING_WENT_WRONG,
                            Toast.LENGTH_LONG
                        ).show()
                        swipeRefreshLayout.isRefreshing = false
                    }

                    Status.LOADING -> {
                        swipeRefreshLayout.isRefreshing = true
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        rvExchangeRates.apply {
            adapter = exchangeRatesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun getInputAmount(): Double {
        val amount = etAmount.text.toString()
        return if (amount.isEmpty()) 1.0 else amount.toDouble()
    }

    private fun navigateToCurrenciesFragment() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.action_exchangeRatesFragment_to_currenciesFragment)
    }

    private fun getSelectedCurrency(): String = tvSelectedCurrency.text.toString()

    private fun updateSelectedCurrency(selectedCurrency: String) {
        tvSelectedCurrency.text = selectedCurrency
        viewModel?.getExchangeRates(selectedCurrency)
    }

    fun searchQueryRequest(query: String) {
        exchangeRatesAdapter.filter.filter(query)
    }
}
















