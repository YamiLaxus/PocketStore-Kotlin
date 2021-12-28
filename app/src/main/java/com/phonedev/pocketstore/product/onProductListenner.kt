package com.phonedev.pocketstore

import com.phonedev.pocketstore.entities.Product

interface onProductListenner {

    fun onClick(product: Product)
}