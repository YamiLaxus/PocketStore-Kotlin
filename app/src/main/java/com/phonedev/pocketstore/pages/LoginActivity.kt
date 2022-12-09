package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.phonedev.pocketstore.databinding.ActivityLoginBinding
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.models.Usuario
import org.json.JSONException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var auth = FirebaseAuth.getInstance()

    //Define Object type User Class
    val usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        val pref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val user = pref.getString("email", "")
        val pass = pref.getString("pass", "")

        if (user!!.isNotEmpty() and pass!!.isNotEmpty()) {
            goHome()
            this.finish()
        } else {
            binding.etUser.requestFocus()
        }

        click()
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

    private fun click() {
        binding.tvNoAccount.setOnClickListener {
            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
        }
        binding.btnLogin.setOnClickListener {
            desableUI()
            val user = binding.etUser.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val url = Constants.BASE_URL + "sesion.php?email=$user&pass=$pass"
            val queue = Volley.newRequestQueue(this)
            if (user.isEmpty() || pass.isEmpty()) {
                showAlert()
                enableUI()
            } else {
                val sharedPreference = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                val jsonObjectRequest =
                    JsonObjectRequest(Request.Method.GET, url, null, { response ->

                        try {
                            val jsonArray = response.getJSONArray("datos")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                usuario.nombre = (jsonObject.optString("nombre"))
                                usuario.apellido = (jsonObject.optString("apellido"))
                                usuario.telefono = (jsonObject.optString("telefono"))
                                usuario.direccion = (jsonObject.optString("direccion"))
                                usuario.usuario = (jsonObject.optString("usuario"))
                                usuario.email = (jsonObject.optString("email"))
                                usuario.pass = (jsonObject.optString("pass"))
                                usuario.imagen = (jsonObject.optString("imagen"))
                                usuario.tipo = (jsonObject.optString("tipo"))

                                //Save into SharedPreferences
                                editor.putString("nombre", usuario.nombre)
                                editor.putString("apellido", usuario.apellido)
                                editor.putString("telefono", usuario.telefono)
                                editor.putString("direccion", usuario.direccion)
                                editor.putString("email", usuario.email)
                                editor.putString("pass", usuario.pass)
                                editor.putString("imagen", usuario.imagen)
                                editor.putString("tipo", usuario.tipo)
                                editor.apply()

                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        goHome()
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
}