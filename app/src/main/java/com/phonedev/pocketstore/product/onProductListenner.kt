package com.phonedev.pocketstore

import com.phonedev.pocketstore.entities.ProductosDestacados

interface onProductListenner {

    fun onClick(product: Product)
    fun onClickDestacado(product: ProductosDestacados)
}