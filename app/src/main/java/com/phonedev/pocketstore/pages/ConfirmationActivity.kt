package com.phonedev.pocketstore.pages

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.databinding.ActivityConfirmationBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding

    private var product: ProductosModeloItem? = null
    private var user: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()


        //GetDetails of Order
        val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        user = pref.getInt("id_usuario", 0)
        user.toString()

        this.product = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("producto")
        } else {
            intent.getParcelableExtra("producto")
        }
        if (product != null) {
            binding.tvName.text = product!!.nombre
            binding.tvTotalPrice.text = product!!.precio
            Glide.with(binding.imgProduct).load(product!!.imagen).into(binding.imgProduct)
        }

        clicks()
        confirmOrder()
    }


    private fun confirmOrder() {
        binding.radioCard.isClickable = false

    }

    private fun clicks() {
        binding.btnClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            this.finish()
        }
    }

    fun onRadioButtonClicked(view: View) {}
}