package com.phonedev.pocketstore.order

import com.phonedev.pocketstore.entities.Order

interface OnOrderListener {
    fun onTrack(order: Order)
    fun onStartChat(order: Order)
}