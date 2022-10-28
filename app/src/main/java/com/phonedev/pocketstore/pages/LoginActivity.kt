package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.databinding.ActivityLoginBinding
import com.phonedev.pocketstore.entities.Constants

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        val pref = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        val user = pref.getString("usuario", "")
        val pass = pref.getString("pass", "")

        if (user!!.isNotEmpty() and pass!!.isNotEmpty()) {
            goHome()
            finish()
        }
        click()
    }

    private fun click() {
        binding.tvNoAccount.setOnClickListener {
            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
        }
        binding.btnLogin.setOnClickListener {
            desableUI()
            val user = binding.etUser.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val url = Constants.BASE_URL + "login.php?email=$user&pass=$pass"
            val queue = Volley.newRequestQueue(this)
            if (user.isEmpty() || pass.isEmpty()) {
                showAlert()
                enableUI()
            } else {
                val jsonObjectRequest =
                    JsonObjectRequest(Request.Method.GET, url, null, { response ->
                        goHome()
                        saveSharedPreferences()
                        finish()
                    }, { error ->
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                        enableUI()
                    }
                    )
                queue.add(jsonObjectRequest)
            }
        }
    }

    private fun saveSharedPreferences() {
        val pref = getSharedPreferences("usuario", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("usuario", binding.etUser.text.toString().trim())
        editor.putString("pass", binding.etPassword.text.toString().trim())
        editor.apply()
    }

    private fun goHome() {
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se a producido un error al autenticar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun desableUI() {
        binding.tvNoAccount.isEnabled = false
        binding.btnLogin.isEnabled = false
        binding.etUser.isEnabled = false
        binding.etPassword.isEnabled = false
    }

    private fun enableUI() {
        binding.tvNoAccount.isEnabled = true
        binding.btnLogin.isEnabled = true
        binding.etUser.isEnabled = true
        binding.etPassword.isEnabled = true
    }
}