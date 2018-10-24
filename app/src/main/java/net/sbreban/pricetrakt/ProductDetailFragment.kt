package net.sbreban.pricetrakt

import GetProductByNameQuery
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_product_detail.*
import net.sbreban.pricetrakt.model.Product
import java.util.logging.Logger

class ProductDetailFragment : Fragment() {

    private var item: Product? = null
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
                client.query(GetProductByNameQuery.builder().name(it.getString(PRODUCT_NAME)).build()).enqueue(object : ApolloCall.Callback<GetProductByNameQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<GetProductByNameQuery.Data>) {
                        val productByName = response.data()?.productByName
                        Log.info(productByName.toString())
                        item = Product(id = 1, name = productByName.toString())
                        activity?.runOnUiThread {
                            val textView = activity?.findViewById<TextView>(R.id.product_detail)

                            textView!!.text = item!!.name
                        }
                    }
                })
                activity?.toolbar_layout?.title = item?.name
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_detail, container, false)
    }
}
