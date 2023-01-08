package com.phonedev.pocketstore.pages

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.databinding.ActivityDetailBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val product = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("producto")
        } else {
            intent.getParcelableExtra<ProductosModeloItem>("producto")
        }
        if (product != null) {
            binding.tvName.text = product.nombre
            binding.tvDescription.text = product.descripcion
            binding.tvTotalPrice.text = product.precio
            binding.tvDisponible.text = product.tiempor_entrega
            Glide.with(binding.imgProduct).load(product.imagen).into(binding.imgProduct)
        }

        binding.imgProduct.setOnClickListener {
            val i = Intent(this, ImageActivity::class.java)
            i.putExtra("producto", product)
            startActivity(i)
        }

    }
}