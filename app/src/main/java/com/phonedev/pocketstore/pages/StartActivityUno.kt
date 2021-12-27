package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phonedev.pocketstore.databinding.ActivityStartUnoBinding

class StartActivityUno : AppCompatActivity() {

    private lateinit var binding: ActivityStartUnoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartUnoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            Toast.makeText(this, "BIENVENIDO", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }
}