package com.phonedev.pocketstore.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}