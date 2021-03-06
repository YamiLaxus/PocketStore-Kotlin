package com.phonedev.pocketstore.pages

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.phonedev.pocketstore.databinding.ActivityNotFoundBinding

class NotFoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setClicks()
    }

    fun setClicks() {
        binding.btnWhatsApp.setOnClickListener {
            val number = 41642429
            val user = FirebaseAuth.getInstance().currentUser?.displayName.toString()

            Toast.makeText(this, "Abriendo WhatsApp...", Toast.LENGTH_SHORT).show()

            var pedido = ""
            pedido = pedido + "\n"
            pedido = pedido + "Pocket Store Support" + "\n"
            pedido = pedido + "CLIENTE: $user"
            pedido = pedido + "\n"
            pedido = pedido + "___________________________"
            pedido = pedido + "\n"
            pedido = pedido + "Hola PhoneDev, necesito información..."


            val url = "https://wa.me/502$number?text=$pedido"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.btnFacebook.setOnClickListener {
            val url = "https://www.facebook.com/def.alt.00101"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.btnInstagram.setOnClickListener {
            val url = "https://www.instagram.com/osvaldo_ez/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.btnWeb.setOnClickListener {
            val url = "http://www.phone-dev.tk/#inicio"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}