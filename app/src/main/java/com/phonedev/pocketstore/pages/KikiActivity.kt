package com.phonedev.pocketstore.pages

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.cart.CartFragment
import com.phonedev.pocketstore.databinding.ActivityKikiBinding
import com.phonedev.pocketstore.detail.DetailFragment
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.order.OrderActivity
import com.phonedev.pocketstore.product.ArtAdapter
import com.phonedev.pocketstore.product.KikiAdapter
import com.phonedev.pocketstore.product.MainAux

class KikiActivity : AppCompatActivity(), onProductListenner, MainAux,
    SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityKikiBinding

    private lateinit var firestoreListener: ListenerRegistration

    private lateinit var adapter: KikiAdapter

    private var productCartList = mutableListOf<Product>()

    private var productSelected: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKikiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        binding.searchView.setOnQueryTextListener(this)

        configBottoms()
        configRecyclerView()
        reloadData()
    }

    override fun onResume() {
        super.onResume()
        configFirestoreRealTime()
    }

    private fun configRecyclerView() {
        binding.llProgress.visibility = View.GONE
        binding.nsvProductos.visibility = View.VISIBLE

        adapter = KikiAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                this@KikiActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = this@KikiActivity.adapter
        }
    }

    private fun configFirestoreRealTime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_KIKISTORE)

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

        val fragment = DetailFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
//        showButton(false)
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

    //Busqueda
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            adapter.filtrado(query)
        } else {
            configFirestoreRealTime()
        }
        return false
    }

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.N)
    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }


    private fun reloadData() {
        binding?.let {
            it.imbReload.setOnClickListener {
                configRecyclerView()
                configFirestoreRealTime()
                Toast.makeText(this, "Recargando datos...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}