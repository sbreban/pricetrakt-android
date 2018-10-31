package net.sbreban.pricetrakt

import GetProductByNameQuery.Price
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.price_fragment.*
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
                    prices_text.text = pricesText
                })
            }
        }


    }

}
