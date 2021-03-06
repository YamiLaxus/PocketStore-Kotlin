package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.cart.CartFragment
import com.phonedev.pocketstore.databinding.ActivityServiciosBinding
import com.phonedev.pocketstore.detail.DetailFragment
import com.phonedev.pocketstore.detail.ServicesDetailsFragment
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.order.OrderActivity
import com.phonedev.pocketstore.product.ArtAdapter
import com.phonedev.pocketstore.product.MainAux
import com.phonedev.pocketstore.product.ServiciosAdapter

class ServiciosActivity : AppCompatActivity(), onProductListenner, MainAux {

    private lateinit var binding: ActivityServiciosBinding

    private lateinit var firestoreListener: ListenerRegistration

    private lateinit var adapter: ServiciosAdapter

    private var productCartList = mutableListOf<Product>()

    private var productSelected: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        configBottoms()
        configRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        configFirestoreRealTime()
    }

    private fun configRecyclerView() {
        binding.llProgress.visibility = View.GONE
        binding.nsvProductos.visibility = View.VISIBLE

        adapter = ServiciosAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                this@ServiciosActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = this@ServiciosActivity.adapter
        }
    }

    private fun configFirestoreRealTime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_SERVICIOS)

        firestoreListener = productRef.addSnapshotListener { snapshot, error ->
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

    private fun configBottoms() {
//        binding.btnViewCart.setOnClickListener {
//            val fragment = CartFragment()
//            fragment.show(
//                supportFragmentManager.beginTransaction(),
//                CartFragment::class.java.simpleName
//            )
//        }
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
                        Toast.makeText(this, "Sesi??n Cerrada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.nsvProductos.visibility = View.GONE
                            binding.llProgress.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(this, "Sesi??n no Cerrada", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
//            R.id.action_order_history -> startActivity(Intent(this, OrderActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(product: Product) {
        val index = productCartList.indexOf(product)

        productSelected = if (index != -1) {
            productCartList[index]
        } else {
            product
        }

        val fragment = ServicesDetailsFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
        showButton(false)
    }

    override fun getProductsCart(): MutableList<Product> = productCartList

    override fun updateTotal() {
//        var total = 0.0
//        productCartList.forEach { product ->
//            total += product.totalPrice()
//        }
//
//        if (total == 0.0) {
//            binding.tvTotal.text = getString(R.string.product_empty_cart)
//        } else {
//            binding.tvTotal.text = getString(R.string.product_full_cart, total)
//        }
    }

    override fun clearCart() {
        productCartList.clear()
    }

    override fun getProductSelected(): Product? = productSelected

    override fun showButton(isVisible: Boolean) {
//        binding.btnViewCart.visibility = if (isVisible) View.VISIBLE else View.GONE
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
}