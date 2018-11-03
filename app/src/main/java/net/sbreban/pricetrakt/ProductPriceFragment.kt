package net.sbreban.pricetrakt

import GetProductByNameQuery.Price
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.price_fragment.*
import kotlinx.android.synthetic.main.prices_list_content.view.*
import net.sbreban.pricetrakt.viewmodel.PriceListViewModel
import java.util.logging.Logger
import java.util.stream.Collectors


class ProductPriceFragment : Fragment() {

    companion object {
        fun newInstance() = ProductPriceFragment()
        const val PRODUCT_NAME = "product_name"
        val Log = Logger.getLogger(ProductPriceFragment::class.java.name)
    }

    private lateinit var viewModel: PriceListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.price_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ProductPriceFragment.PRODUCT_NAME)) {
                val productName = it.getString(ProductPriceFragment.PRODUCT_NAME)
                viewModel = ViewModelProviders.of(this).get(PriceListViewModel::class.java)

                viewModel.getPrices(productName).observe(this, Observer<List<Price>> { prices ->
                    // update UI
                    val pricesText = prices?.stream()?.map(Price::price)?.collect(Collectors.toList()).toString()
                    Log.info(pricesText)
                    prices_list.adapter = PriceRecyclerViewAdapter(prices!!)
                })
            }
        }
    }

    class PriceRecyclerViewAdapter(private val values: List<Price>) :
            RecyclerView.Adapter<PriceRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.prices_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.priceText.text = item.price().toString()
            holder.currencyText.text = item.currency()
            holder.stateText.text = item.state().rawValue()
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val priceText: TextView = view.price_text
            val currencyText: TextView = view.currency_text
            val stateText: TextView = view.state_text
        }
    }


}
