package net.sbreban.pricetrakt

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.product_detail.view.*
import net.sbreban.pricetrakt.model.Product

/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a [ProductListActivity]
 * in two-pane mode (on tablets) or a [ProductDetailActivity]
 * on handsets.
 */
class ProductDetailFragment : Fragment() {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private var item: Product? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			if (it.containsKey(ARG_ITEM_ID)) {
				// Load the dummy content specified by the fragment
				// arguments. In a real-world scenario, use a Loader
				// to load content from a content provider.
				item = Product(id = 1, name = "product")
				activity?.toolbar_layout?.title = item?.name
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
														savedInstanceState: Bundle?): View? {
		val rootView = inflater.inflate(R.layout.product_detail, container, false)

		// Show the dummy content as text in a TextView.
		item?.let {
			rootView.product_detail.text = it.name
		}

		return rootView
	}

	companion object {
		/**
		 * The fragment argument representing the item ID that this fragment
		 * represents.
		 */
		const val ARG_ITEM_ID = "item_id"
	}
}
