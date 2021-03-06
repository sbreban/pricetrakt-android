package net.sbreban.pricetrakt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductPricesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(prices_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = ProductPriceFragment().apply {
                arguments = Bundle().apply {
                    putString(ProductPriceFragment.PRODUCT_NAME,
                            intent.getStringExtra(ProductPriceFragment.PRODUCT_NAME))
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
