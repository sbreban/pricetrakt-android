package net.sbreban.pricetrakt

import GetProductByNameQuery
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import java.util.logging.Logger
import java.util.stream.Collectors


class ProductDetailFragment : Fragment() {

    private lateinit var client: ApolloClient

    companion object {
        val Log = Logger.getLogger(ProductDetailFragment::class.java.name)
        const val PRODUCT_NAME = "item_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = ProductListActivity.setupApollo()

        arguments?.let {
            if (it.containsKey(PRODUCT_NAME)) {
                val productName = it.getString(PRODUCT_NAME)
                client.query(GetProductByNameQuery.builder().name(productName).build()).enqueue(object : ApolloCall.Callback<GetProductByNameQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<GetProductByNameQuery.Data>) {
                        val prices = response.data()?.productByName?.prices()?.stream()?.collect(Collectors.toList())
                        Log.info(prices.toString())
                    }
                })
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_detail, container, false)
    }
}
