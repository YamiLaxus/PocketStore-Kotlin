package com.phonedev.pocketstore.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phonedev.pocketstore.databinding.ActivityNotFoundBinding

class NotFoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}