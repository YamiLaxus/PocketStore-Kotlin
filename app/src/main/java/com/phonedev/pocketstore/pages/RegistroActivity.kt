package com.phonedev.pocketstore.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.phonedev.pocketstore.databinding.ActivityRegistroBinding
import com.phonedev.pocketstore.entities.Constants


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        setup()
    }

    private fun setup() {
        binding.btnLogin.setOnClickListener {
            disableUi()
            if (binding.etUser.text.isNotEmpty() && binding.etPassword.text.isNotEmpty() && binding.etName.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etUser.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val profileUpdates =
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(binding.etName.text.toString().trim()).build()

                        user?.updateProfile(profileUpdates)

                        val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        val database = Firebase.database

                        val map: Map<String, String> = mapOf(
                            Pair("name", binding.etName.text.toString().trim()),
                            Pair("user", binding.etUser.text.toString().trim())
                        )

                        val userRef =
                            database.getReference(Constants.PATH_USERS).child(userID).setValue(map)
                                .addOnCompleteListener { userAdd ->
                                    if (userAdd.isSuccessful) {
                                        showHome()
                                        finish()
                                    } else {
                                        showAlert()
                                        enableUi()
                                    }
                                }
                    } else {
                        showAlert()
                        enableUi()
                    }
                }
            } else {
                showAlert()
                enableUi()
            }
        }
        binding.btnCancel.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            this.finish()
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al registrar usuario.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

    private fun enableUi() {
        binding.btnCancel.isEnabled = true
        binding.btnLogin.isEnabled = true
        binding.etName.isEnabled = true
        binding.etUser.isEnabled = true
        binding.etPassword.isEnabled = true
    }

    private fun disableUi() {
        binding.btnCancel.isEnabled = false
        binding.btnLogin.isEnabled = false
        binding.etName.isEnabled = false
        binding.etUser.isEnabled = false
        binding.etPassword.isEnabled = false
    }
}