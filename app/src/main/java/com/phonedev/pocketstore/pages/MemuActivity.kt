package com.phonedev.pocketstore.pages

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.phonedev.pocketstore.databinding.ActivityMemuBinding

class MemuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        setClicks()
    }

    private fun setClicks() {
        binding.btnLogOut.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Sesión Cerrada, Vuelve pronto", Toast.LENGTH_SHORT).show()
                        this.finish()
                    } else {
                        Toast.makeText(this, "Sesión no Cerrada", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.btnReportBug.setOnClickListener {
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
            pedido = pedido + "Hola PhoneDev, quiero reportar..."


            val url = "https://wa.me/502$number?text=$pedido"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.btnTrack.setOnClickListener {
            val intent = Intent(this, NotFoundActivity::class.java)
            startActivity(intent)
        }
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
        binding.btnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
    }
}