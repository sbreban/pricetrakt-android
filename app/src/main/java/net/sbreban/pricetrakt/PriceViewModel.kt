package net.sbreban.pricetrakt

import GetProductByNameQuery
import GetProductByNameQuery.Price
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import java.util.logging.Logger
import java.util.stream.Collectors

class PriceViewModel : ViewModel() {

    companion object {
        val Log = Logger.getLogger(PriceViewModel::class.java.name)
    }

    private lateinit var prices: MutableLiveData<List<Price>>

    fun getPrices(productName: String): LiveData<List<Price>> {
        if (!::prices.isInitialized) {
            prices = MutableLiveData()
            loadPrices(productName)
        }
        return prices
    }

    private fun loadPrices(productName: String) {
        // Do an asynchronous operation to fetch prices.
        val client = ProductListActivity.setupApollo()
        client.query(GetProductByNameQuery.builder().name(productName).build()).enqueue(object : ApolloCall.Callback<GetProductByNameQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.info(e.message.toString())
            }

            override fun onResponse(response: Response<GetProductByNameQuery.Data>) {
                val loadedPrices = response.data()?.productByName?.prices()?.stream()?.collect(Collectors.toList())
                prices.postValue(loadedPrices)
                Log.info(loadedPrices.toString())
            }
        })
    }

}
