package com.phonedev.pocketstore.cart

import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.models.ProductosModeloItem

interface OnCartListenner {

    fun setQuantity(product: Product)
    fun showTotal(total: Double)

    fun setCantidad(p: ProductosModeloItem)

}