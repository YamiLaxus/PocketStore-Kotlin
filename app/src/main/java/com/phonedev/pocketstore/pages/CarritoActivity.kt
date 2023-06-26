package com.phonedev.pocketstore.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.phonedev.pocketstore.databinding.ActivityCarritoBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private var cartList: List<ProductosModeloItem>? = null
    private var totalToPay: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        getCart()
        clicks()
    }

    private fun getCart() {

    }

    private fun clicks() {
        binding.btnClose.setOnClickListener {
            onBackPressed()
            this.finish()
        }
    }
}