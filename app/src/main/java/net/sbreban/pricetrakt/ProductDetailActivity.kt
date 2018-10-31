package net.sbreban.pricetrakt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_product_detail)
		setSupportActionBar(detail_toolbar)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		if (savedInstanceState == null) {
			val fragment = PriceFragment().apply {
				arguments = Bundle().apply {
					putString(PriceFragment.PRODUCT_NAME,
							intent.getStringExtra(PriceFragment.PRODUCT_NAME))
				}
			}

			supportFragmentManager.beginTransaction()
					.add(R.id.product_detail_container, fragment)
					.commit()
		}
	}

	override fun onOptionsItemSelected(item: MenuItem) =
			when (item.itemId) {
				android.R.id.home -> {
					navigateUpTo(Intent(this, ProductListActivity::class.java))
					true
				}
				else -> super.onOptionsItemSelected(item)
			}
}
