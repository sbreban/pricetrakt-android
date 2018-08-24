package net.sbreban.pricetrakt.model

data class ShopEntry(

		val id: Int = 0,

		val shop: Shop = Shop(),

		val product: Product = Product(),

		val url: String = ""
)