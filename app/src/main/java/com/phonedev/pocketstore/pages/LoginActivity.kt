package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestoreListener: ListenerRegistration
    private lateinit var firebaseAuth: FirebaseAuth

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val response = IdpResponse.fromResultIntent(it.data)

            if (it.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                }
            } else {
                if (response == null) {
                    Toast.makeText(this, "Hasta Pronto", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    response.error?.let {
                        if (it.errorCode == ErrorCodes.NO_NETWORK) {
                            Toast.makeText(this, "Sin Coneccion", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Codigo de error: ${it.errorCode}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        setup()
        configAuth()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)

    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.onStop()
    }

    private fun configAuth() {
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                supportActionBar?.title = auth.currentUser?.displayName
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                this.finish()
            }
        }
    }

    private fun setup() {
        binding.btnSigUp.setOnClickListener {
            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
            this.finish()
        }
        binding.btnLogin.setOnClickListener {
            if (binding.etUser.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.etUser.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val homeIntent = Intent(this, HomeActivity::class.java)
                        startActivity(homeIntent)
                        this.finish()
                    } else {
                        showAlert()
                    }
                }
            } else {
                showAlert()
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se a producido un error al autenticar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertExit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir")
        builder.setMessage("Deseas Salir?")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        finish()
    }
}