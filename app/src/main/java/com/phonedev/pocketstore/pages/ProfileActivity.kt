package com.phonedev.pocketstore.pages

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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

        getProfile()

    }

    private fun getProfile() {
        val pref = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        val user = pref.getString("usuario", "")
        val pass = pref.getString("pass", "")
        val url = Constants.BASE_URL + "login.php?email=$user&pass=$pass"
        val queue = Volley.newRequestQueue(this)

        binding.tvName.text = user


        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->

            }, { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
            )
        queue.add(jsonObjectRequest)
    }
}