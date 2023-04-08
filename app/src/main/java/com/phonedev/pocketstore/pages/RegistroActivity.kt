package com.phonedev.pocketstore.pages

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request.Method.POST
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.phonedev.pocketstore.databinding.ActivityRegistroBinding
import com.phonedev.pocketstore.entities.Constants

@Suppress("DEPRECATION")
class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        click()
    }

    private fun click() {
        binding.btnCancelar.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.btnLogin.setOnClickListener {
            crearUsuario()
        }
    }

    private fun crearUsuario() {
        desableUI()
        val url = Constants.BASE_URL
        val queue = Volley.newRequestQueue(this)
        if (binding.etNombre.text.isNotEmpty() && binding.etApellido.text.isNotEmpty() && binding.etTelefono.text.isNotEmpty() && binding.etDireccion.text.isNotEmpty() && binding.etUsuario.text.isNotEmpty() && binding.etCorreo.text.isNotEmpty() && binding.etPassword.text.isNotEmpty() && binding.etPassword2.text.isNotEmpty()) {
            if (binding.etPassword.text.toString().trim() == binding.etPassword2.text.toString()
                    .trim()
            ) {
                val resultadoPost =
                    object : StringRequest(
                        POST,
                        url + "crear_usuarios.php",
                        { response ->
                            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                            goBack()
                            saveSharedPreferences()
                            finish()
                        },
                        { error ->
                            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
                            showAlert()
                            enableUi()
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val parameters = HashMap<String, String>()
                            parameters["nombre"] = binding.etNombre.text.toString().trim()
                            parameters["apellido"] = binding.etApellido.text.toString().trim()
                            parameters["telefono"] = binding.etTelefono.text.toString().trim()
                            parameters["direccion"] = binding.etDireccion.text.toString().trim()
                            parameters["usuario"] = binding.etUsuario.text.toString().trim()
                            parameters["email"] = binding.etCorreo.text.toString().trim()
                            parameters["pass"] = binding.etPassword.text.toString().trim()
                            parameters["imagen"] = ""
                            parameters["tipo"] = "usuario"
                            return parameters
                        }
                    }
                saveSharedPreferences()
                queue.add(resultadoPost)
            } else {
                Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show()
                enableUi()
            }
        } else {
            showAlert()
            enableUi()
        }
    }

    private fun saveSharedPreferences() {
        val pref = getSharedPreferences("usuario", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("usuario", binding.etCorreo.text.toString().trim())
        editor.putString("pass", binding.etPassword.text.toString().trim())
        editor.apply()
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al registrar usuario revisa todos los datos.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goBack() {
        onBackPressed()
        this.finish()
        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
    }

    private fun enableUi() {
        binding.btnCancelar.isEnabled = true
        binding.btnLogin.isEnabled = true
        binding.etNombre.isEnabled = true
        binding.etApellido.isEnabled = true
        binding.etTelefono.isEnabled = true
        binding.etDireccion.isEnabled = true
        binding.etUsuario.isEnabled = true
        binding.etCorreo.isEnabled = true
        binding.etPassword.isEnabled = true
        binding.etPassword2.isEnabled = true
    }

    private fun desableUI() {
        binding.btnCancelar.isEnabled = false
        binding.btnLogin.isEnabled = false
        binding.etNombre.isEnabled = false
        binding.etApellido.isEnabled = false
        binding.etTelefono.isEnabled = false
        binding.etDireccion.isEnabled = false
        binding.etUsuario.isEnabled = false
        binding.etCorreo.isEnabled = false
        binding.etPassword.isEnabled = false
        binding.etPassword2.isEnabled = false
    }
}

//Firebase

//    private fun setup() {
//        binding.btnLogin.setOnClickListener {
//            disableUi()
//            if (binding.etUser.text.isNotEmpty() && binding.etPassword.text.isNotEmpty() && binding.etName.text.isNotEmpty()) {
//                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
//                    binding.etUser.text.toString().trim(),
//                    binding.etPassword.text.toString().trim()
//                ).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        val user = FirebaseAuth.getInstance().currentUser
//                        val profileUpdates =
//                            UserProfileChangeRequest.Builder()
//                                .setDisplayName(binding.etName.text.toString().trim()).build()
//
//                        user?.updateProfile(profileUpdates)
//
//                        val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
//                        val database = Firebase.database
//
//                        val map: Map<String, String> = mapOf(
//                            Pair("name", binding.etName.text.toString().trim()),
//                            Pair("user", binding.etUser.text.toString().trim())
//                        )
//
//                        val userRef =
//                            database.getReference(Constants.PATH_USERS).child(userID).setValue(map)
//                                .addOnCompleteListener { userAdd ->
//                                    if (userAdd.isSuccessful) {
//                                        showHome()
//                                        finish()
//                                    } else {
//                                        showAlert()
//                                        enableUi()
//                                    }
//                                }
//                    } else {
//                        showAlert()
//                        enableUi()
//                    }
//                }
//            } else {
//                showAlert()
//                enableUi()
//            }
//        }
//        binding.btnCancel.setOnClickListener {
//            val i = Intent(this, LoginActivity::class.java)
//            startActivity(i)
//            this.finish()
//        }
//    }