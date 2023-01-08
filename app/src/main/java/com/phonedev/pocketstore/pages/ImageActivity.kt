package com.phonedev.pocketstore.pages

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.databinding.ActivityImageBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

@Suppress("DEPRECATION")
class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val product = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("producto")
        } else {
            intent.getParcelableExtra<ProductosModeloItem>("producto")
        }
        if (product != null) {
            Glide.with(this).load(product.imagen).into(binding.imageFull)
        }
    }
}