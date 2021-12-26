package com.phonedev.pocketstore.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phonedev.pocketstore.databinding.ActivityStartUnoBinding

class StartActivityUno : AppCompatActivity() {

    private lateinit var binding: ActivityStartUnoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartUnoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}