package net.sbreban.pricetrakt.viewmodel

import GetAllProductsQuery
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import net.sbreban.pricetrakt.ProductListActivity
import net.sbreban.pricetrakt.model.Product
import java.util.logging.Logger

class ProductListViewModel : ViewModel() {

    companion object {
        val Log = Logger.getLogger(ProductListViewModel::class.java.name)
    }

    private lateinit var products: MutableLiveData<List<Product>>

    fun getProducts(): LiveData<List<Product>> {
        if (!::products.isInitialized) {
            products = MutableLiveData()
            loadProducts()
        }
        return products
    }

    private fun loadProducts() {
        // Do an asynchronous operation to fetch products.
        val client = ProductListActivity.setupApollo()
        client.query(GetAllProductsQuery.builder().build()).enqueue(object : ApolloCall.Callback<GetAllProductsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.info(e.message.toString())
            }

            override fun onResponse(response: Response<GetAllProductsQuery.Data>) {
                val loadedProducts: MutableList<Product> = mutableListOf()
                response.data()?.allProducts?.forEach {
                    loadedProducts.add(Product(id = it.id().toInt(), name = it.name()))
                }
                Log.info(loadedProducts.toString())
                products.postValue(loadedProducts)
            }
        })
    }

}