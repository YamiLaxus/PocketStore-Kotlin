package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.databinding.ActivityProfileBinding

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var homeActivity: HomeActivity


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

        binding.imgProfile.setOnClickListener {
            val i = Intent(this, ImageActivity::class.java)
            i.putExtra("producto", imageUser)
            startActivity(i)
        }

        //Function not working
        binding.btnEdit.isEnabled = false

        click()
    }

    private fun click() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
            this.finish()
        }
        binding.btnLogOut.setOnClickListener {
            val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
            pref.edit().clear().apply()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}