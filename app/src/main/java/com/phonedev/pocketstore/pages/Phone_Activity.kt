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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.Product
import com.phonedev.pocketstore.product.ProductAdapter
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.cart.CartFragment
import com.phonedev.pocketstore.databinding.ActivityMainBinding.*
import com.phonedev.pocketstore.databinding.ActivityPhoneBinding
import com.phonedev.pocketstore.detail.DetailFragment
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.order.OrderActivity
import com.phonedev.pocketstore.product.MainAux


class Phone_Activity : AppCompatActivity(), onProductListenner, MainAux {

    private lateinit var binding: ActivityPhoneBinding

    //variables for Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestoreListenner: ListenerRegistration

    lateinit var adapter: ProductAdapter

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
        setContentView(R.layout.activity_phone)

        binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configAuth()
        configRecyclerView()
        configBottoms()
    }

    private fun configAuth() {

        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                supportActionBar?.title = auth.currentUser?.displayName
                binding.llProgress.visibility = View.GONE
                binding.nsvProductos.visibility = View.VISIBLE
            } else {
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )

                resultLauncher.launch(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
        configFirestoreRealTime()
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
        firestoreListenner.remove()
    }

    //fix for PhoneAdapter
    private fun configRecyclerView() {
//        adapter = ProductAdapter(mutableListOf(), this)
//        binding.recyclerView.apply {
//            layoutManager = GridLayoutManager(
//                this@Phone_Activity, 1,
//                GridLayoutManager.VERTICAL, false
//            )
//            adapter = this@Phone_Activity.adapter
//        }
    }

    private fun configBottoms() {
        binding.btnViewCart.setOnClickListener {
            val fragment = CartFragment()
            fragment.show(
                supportFragmentManager.beginTransaction(),
                CartFragment::class.java.simpleName
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                AuthUI.getInstance().signOut(this)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sesión Cerrada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.nsvProductos.visibility = View.GONE
                            binding.llProgress.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(this, "Sesión no Cerrada", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            R.id.action_order_history -> startActivity(Intent(this, OrderActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configFirestoreRealTime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_PRODUCTS)

        firestoreListenner = productRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
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

    override fun onClick(product: Product) {

        val index = productCartList.indexOf(product)

        if (index != -1) {
            productSelected = productCartList[index]
        } else {
            productSelected = product
        }

        val fragment = DetailFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
        showButton(false)
    }

    override fun getProductsCart(): MutableList<Product> = productCartList

    override fun getProductSelected(): Product? = productSelected

    override fun showButton(isVisible: Boolean) {
        binding.btnViewCart.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun addProductToCart(product: Product) {
        val index = productCartList.indexOf(product)

        if (index != -1) {
            productCartList.set(index, product)
        } else {
            productCartList.add(product)
        }

        updateTotal()
    }

    override fun updateTotal() {
        var total = 0.0
        productCartList.forEach { product ->
            total += product.totalPrice()
        }

        if(total == 0.0){
            binding.tvTotal.text = getString(R.string.product_empty_cart)
        } else{
            binding.tvTotal.text = getString(R.string.product_full_cart, total)
        }
    }

    override fun clearCart() {
        productCartList.clear()
    }
}