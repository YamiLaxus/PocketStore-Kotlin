package com.phonedev.pocketstore.pages

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.phonedev.pocketstore.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        val pref = getSharedPreferences("usuario", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            desableUI()
            val email = binding.etUser.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                showAlert()
                enableUI()
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    goHome()
                    finish()
                    enableUI()
                    Log.d(TAG, "LOG OK")
                    val user = auth.currentUser
                } else {
                    Log.w(TAG, "Log Faild", task.exception)
                    showAlert()
                    enableUI()
                    Toast.makeText(this, "Error $task", Toast.LENGTH_SHORT).show()
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


    //    private fun click() {
//        binding.tvNoAccount.setOnClickListener {
//            val i = Intent(this, RegistroActivity::class.java)
//            startActivity(i)
//        }
//        binding.btnLogin.setOnClickListener {
//            desableUI()
//            val user = binding.etUser.text.toString().trim()
//            val pass = binding.etPassword.text.toString().trim()
//            val url = Constants.BASE_URL + "login.php?email=$user&pass=$pass"
//            val queue = Volley.newRequestQueue(this)
//            if (user.isEmpty() || pass.isEmpty()) {
//                showAlert()
//                enableUI()
//            } else {
//                val jsonObjectRequest =
//                    JsonObjectRequest(Request.Method.GET, url, null, { response ->
//
//                        try {
//                            val jsonArray = response.getJSONArray("data")
//                            for (i in 0 until jsonArray.length()) {
//                                val jsonObject = jsonArray.getJSONObject(i)
//                                nombre = jsonObject.getString("nombre")
//                                correo = jsonObject.getString("correo")
//                                Toast.makeText(this, this.nombre.toString(), Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//
//                        goHome()
//                        saveSharedPreferences()
//                        finish()
//                    }, { error ->
//                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
//                        enableUI()
//                    }
//                    )
//                queue.add(jsonObjectRequest)
//            }
//        }
//    }
}