package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.databinding.ActivityProfileBinding
import com.phonedev.pocketstore.entities.Constants
import org.json.JSONArray

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val nombre = pref.getString("nombre", "")
        val apellido = pref.getString("apellido", "")
        val telefono = pref.getString("telefono", "")
        val direccion = pref.getString("direccion", "")
        val usuario = pref.getString("usuario", "")
        val email = pref.getString("email", "")
        val imageUser = pref.getString("imagen", "")
        val tipo = pref.getString("tipo", "")

        binding.tvName.text = nombre
        binding.tvFullName.text = apellido
        binding.userEmail.text = email
        binding.userPhone.text = telefono
        binding.userAddress.text = direccion
        binding.tvUsuario.text = tipo

        Glide.with(this)
            .load(imageUser.toString())
            .centerCrop().circleCrop().into(binding.imgProfile)


        //Function not working
        binding.btnEdit.isEnabled = false

        click()
    }

    private fun click() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            this.finish()
        }
    }
}