package com.sawthandar.currencyexchangeratesapp.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.sawthandar.currencyexchangeratesapp.R
import com.sawthandar.currencyexchangeratesapp.ui.FragmentsFactory
import com.sawthandar.currencyexchangeratesapp.ui.currencies.CurrenciesFragment
import com.sawthandar.currencyexchangeratesapp.ui.exchange.ExchangeRatesFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentsFactory

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                passSearchQuery(query ?: "")
                return false
            }
        })
        return true
    }

    private fun passSearchQuery(query: String) {
        val fragment: Fragment? = getCurrentVisibleFragment()
        if (fragment is ExchangeRatesFragment) {
            fragment.searchQueryRequest(query)
        } else if (fragment is CurrenciesFragment) {
            fragment.searchQueryRequest(query)
        }
    }

    private fun getCurrentVisibleFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment as NavHostFragment?
        val fragmentManager: FragmentManager = navHostFragment?.childFragmentManager!!
        return fragmentManager.primaryNavigationFragment
    }

}