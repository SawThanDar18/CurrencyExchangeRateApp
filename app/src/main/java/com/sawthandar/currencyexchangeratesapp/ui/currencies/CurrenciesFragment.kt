package com.sawthandar.currencyexchangeratesapp.ui.currencies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sawthandar.currencyexchangeratesapp.ui.MainActivity
import com.sawthandar.currencyexchangeratesapp.R
import com.sawthandar.currencyexchangeratesapp.utils.Constants
import com.sawthandar.currencyexchangeratesapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrenciesFragment @Inject constructor(
    private val currenciesAdapter: CurrenciesAdapter?,
    private var viewModel: CurrenciesViewModel? = null
) : Fragment(R.layout.fragment_currencies) {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvCurrencies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        rvCurrencies = view.findViewById(R.id.rvCurrencies)

        viewModel = ViewModelProvider(requireActivity())[CurrenciesViewModel::class.java]
        setupRecyclerView()
        subscribeToObservers()
        listeners()
    }

    private fun subscribeToObservers() {
        viewModel?.currencies?.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        currenciesAdapter?.submitList(result.data ?: emptyList())
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

        // Get currencies
        viewModel?.getCurrencies()
    }

    private fun setupRecyclerView() {
        rvCurrencies.apply {
            adapter = currenciesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        currenciesAdapter?.onCurrencyClicked = { currency ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                Constants.KEY_SELECTED_CURRENCY,
                currency.currency
            )
            findNavController().popBackStack()

        }
    }

    private fun listeners() {
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            // Get currencies
            viewModel?.getCurrencies()
        }
        swipeRefreshLayout.setOnRefreshListener(refreshListener)
    }

    fun searchQueryRequest(query: String) {
        currenciesAdapter?.filter?.filter(query)
    }
}