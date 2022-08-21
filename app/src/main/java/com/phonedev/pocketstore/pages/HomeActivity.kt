package com.phonedev.pocketstore.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ActivityHomeBinding
import com.phonedev.pocketstore.detail.DetailHomeFragment
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.product.MainAux

class HomeActivity : AppCompatActivity(), onProductListenner, MainAux {

    private lateinit var binding: ActivityHomeBinding

    //variables for Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestoreListener: ListenerRegistration
    private val productCartList = mutableListOf<Product>()
    private var productSelected: Product? = null
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val response = IdpResponse.fromResultIntent(it.data)

            when (it.resultCode) {
                RESULT_OK -> {
                }
                else -> {
                    if (response == null) {
                        configAuth()
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
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configAuth()
        getUserName()
        setClick()
    }

    //
    private fun configAuth() {
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                supportActionBar?.title = auth.currentUser?.displayName
            } else {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                this.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
        getUserName()
    }

    override fun onClick(product: Product) {
        productSelected = product
        val fragment = DetailHomeFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.homeMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun getProductsCart(): MutableList<Product> {
        TODO("Not yet implemented")
    }

    override fun updateTotal() {
        TODO("Not yet implemented")
    }

    override fun clearCart() {
        TODO("Not yet implemented")
    }

    override fun getProductSelected(): Product? = productSelected

    override fun showButton(isVisible: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addProductToCart(product: Product) {
        TODO("Not yet implemented")
    }

    private fun getUserName() {
        binding.tvUser.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        if (firebaseAuth.currentUser?.displayName == null) {
            val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val database = Firebase.database.getReference(Constants.PATH_USERS).child(userID).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val nameUser = it.child("name").value
                        binding.tvUser.text = nameUser.toString()
                    }
                }
        }
    }

    private fun setClick() {
        binding.imbMenu.setOnClickListener {
            val intent = Intent(this, MemuActivity::class.java)
            startActivity(intent)
        }
        binding.btnPhone.setOnClickListener {
            val intent = Intent(this, PhoneActivity::class.java)
            startActivity(intent)
        }
        binding.btnTablet.setOnClickListener {
            val intent = Intent(this, TabletsActivity::class.java)
            startActivity(intent)
        }
        binding.btnAcc.setOnClickListener {
            val intent = Intent(this, AccActivity::class.java)
            startActivity(intent)
        }
        binding.btnArte.setOnClickListener {
            val intent = Intent(this, ArtActivity::class.java)
            startActivity(intent)
        }
        binding.btnKiki.setOnClickListener {
            val intent = Intent(this, KikiActivity::class.java)
            startActivity(intent)
        }
        binding.btnServices.setOnClickListener {
            val intent = Intent(this, ServiciosActivity::class.java)
            startActivity(intent)
        }
    }
}