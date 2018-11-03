package net.sbreban.pricetrakt

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apollographql.apollo.ApolloClient
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.product_list.*
import kotlinx.android.synthetic.main.product_list_content.view.*
import net.sbreban.pricetrakt.model.Product
import net.sbreban.pricetrakt.viewmodel.ProductListViewModel
import okhttp3.OkHttpClient
import java.util.logging.Logger

class ProductListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    private lateinit var viewModel: ProductListViewModel

    companion object {

        private const val BASE_URL = "http://sergiu-pc:8080/graphql"
        val Log = Logger.getLogger(ProductListActivity::class.java.name)!!

        fun setupApollo(): ApolloClient {
            val okHttp = OkHttpClient
                    .Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val builder = original.newBuilder().method(original.method(),
                                original.body())
                        chain.proceed(builder.build())
                    }
                    .build()
            return ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttp)
                    .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (product_detail_container != null) {
            twoPane = true
        }

        viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        viewModel.getProducts().observe(this, Observer<List<Product>> { products ->
            // update UI
            setupRecyclerView(product_list, products?.toMutableList()!!)

        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: MutableList<Product>) {
        recyclerView.adapter = PriceRecyclerViewAdapter(this, items, twoPane)
    }

    class PriceRecyclerViewAdapter(private val parentActivity: ProductListActivity,
                                   private val values: List<Product>,
                                   private val twoPane: Boolean) :
            RecyclerView.Adapter<PriceRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Product
                if (twoPane) {
                    val fragment = ProductPriceFragment().apply {
                        arguments = Bundle().apply {
                            putString(ProductPriceFragment.PRODUCT_NAME, item.name)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.product_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ProductPricesActivity::class.java).apply {
                        putExtra(ProductPriceFragment.PRODUCT_NAME, item.name)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id.toString()
            holder.contentView.text = item.name

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
