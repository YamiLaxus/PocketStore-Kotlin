package com.phonedev.pocketstore.cart

import com.phonedev.pocketstore.Product

interface OnCartListenner {

    fun setQuantity(product: Product)
    fun showTotal(total: Double)

}