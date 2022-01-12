package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ActivityHomeBinding
import com.phonedev.pocketstore.detail.DetailHomeFragment
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.product.MainAux
import com.phonedev.pocketstore.product.ProductExplorerAdapter
import com.phonedev.pocketstore.product.ProductosDestacadosAdapter


class HomeActivity : AppCompatActivity(), onProductListenner, MainAux {

    private lateinit var binding: ActivityHomeBinding

    //variables for Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestoreListener: ListenerRegistration

    private lateinit var adapter: ProductosDestacadosAdapter
    private lateinit var adapterExplorer: ProductExplorerAdapter

    private val productCartList = mutableListOf<Product>()

    private var productSelected: Product? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val response = IdpResponse.fromResultIntent(it.data)

            if (it.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Toast.makeText(this, "Hola Bienvenido", Toast.LENGTH_SHORT).show()
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
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClick()
        configRecyclerView()
        configFirestoreRealTime()
        configAuth()
        configFirestoreRealTimeExplorer()
        getUserName()
    }

    private fun configAuth() {

        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                supportActionBar?.title = auth.currentUser?.displayName
            } else {
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )

                val loginView = AuthMethodPickerLayout
                    .Builder(R.layout.login_view)
                    .setEmailButtonId(R.id.btnEmail)
                    .setGoogleButtonId(R.id.btnGoogle)
                    .build()

                resultLauncher.launch(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(loginView)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.ThemeUICustom)
                        .build()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
        configFirestoreRealTime()
        configFirestoreRealTimeExplorer()
        getUserName()
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
        getUserName()
        firestoreListener.remove()
    }

    private fun configRecyclerView() {
        adapter = ProductosDestacadosAdapter(mutableListOf(), this)
        adapterExplorer = ProductExplorerAdapter(mutableListOf(), this)
        binding.recyclerViewDestacados.apply {
            layoutManager = GridLayoutManager(
                this@HomeActivity, 1,
                GridLayoutManager.HORIZONTAL, false
            )
            adapter = this@HomeActivity.adapter
        }
    }

    private fun configFirestoreRealTime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_DESTACADOS)

        firestoreListener = productRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
//                Toast.makeText(this, "Error al consultar datos 404", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            for (snapshot in snapshot!!.documentChanges) {
                val product = snapshot.document.toObject(Product::class.java)
                product.id = snapshot.document.id
                when (snapshot.type) {
                    DocumentChange.Type.ADDED -> adapter.add(product)
                    DocumentChange.Type.MODIFIED -> adapter.update(product)
                    DocumentChange.Type.REMOVED -> adapter.delete(product)
                }
            }
        }
    }

    private fun configFirestoreRealTimeExplorer() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_EXPLORAR)

        firestoreListener = productRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Inicia sesión primero!", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            for (snapshot in snapshot!!.documentChanges) {
                val product = snapshot.document.toObject(Product::class.java)
                product.id = snapshot.document.id
                when (snapshot.type) {
                    DocumentChange.Type.ADDED -> adapterExplorer.add(product)
                    DocumentChange.Type.MODIFIED -> adapterExplorer.update(product)
                    DocumentChange.Type.REMOVED -> adapterExplorer.delete(product)
                }
            }
        }
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
    }

    private fun setClick() {
        binding.imbMenu.setOnClickListener {
            val intent = Intent(this, MemuActivity::class.java)
            startActivity(intent)
        }

        binding.ibCategoriesAcc.setOnClickListener {
            val intent = Intent(this, AccActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Vamos, Revisa los accesorios.", Toast.LENGTH_SHORT).show()
        }
        binding.ibCategoriesPhone.setOnClickListener {
            val intent = Intent(this, PhoneActivity::class.java)
            Toast.makeText(this, "Bien, Ahora verás teléfonos.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.ibCategoriesTablet.setOnClickListener {
            val intent = Intent(this, TabletsActivity::class.java)
            Toast.makeText(this, "Excelente, ¿Quieres una tablet?", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.ibCategoriesArt.setOnClickListener {
            val intent = Intent(this, ArtActivity::class.java)
            Toast.makeText(this, "El arte es una garantía de cordura.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.ibCategoriesKiki.setOnClickListener {
            val intent = Intent(this, KikiActivity::class.java)
            Toast.makeText(this, "Personaliza tu vida", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.ibCategoriesServices.setOnClickListener {
            val intent = Intent(this, ServiciosActivity::class.java)
            Toast.makeText(this, "Reparemos un par de cosas.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.btnPhone.setOnClickListener {
            val intent = Intent(this, PhoneActivity::class.java)
            Toast.makeText(this, "Bien, Ahora verás teléfonos.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.btnTablet.setOnClickListener {
            val intent = Intent(this, TabletsActivity::class.java)
            Toast.makeText(this, "Excelente, ¿Quieres una tablet?", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.btnAcc.setOnClickListener {
            val intent = Intent(this, AccActivity::class.java)
            Toast.makeText(this, "Vamos, Revisa los accesorios.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.btnArte.setOnClickListener {
            val intent = Intent(this, ArtActivity::class.java)
            Toast.makeText(this, "El arte es una garantía de cordura.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

}