package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.phonedev.pocketstore.Product
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

    private lateinit var firestoreListener: ListenerRegistration

    private lateinit var adapter: ProductosDestacadosAdapter
    private lateinit var adapterExplorer: ProductExplorerAdapter

    private val productCartList = mutableListOf<Product>()

    private var productSelected: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClick()
        configRecyclerView()
        configFirestoreRealTime()
        configFirestoreRealTimeExplorer()
    }

    override fun onResume() {
        super.onResume()
        configFirestoreRealTime()
        configFirestoreRealTimeExplorer()
    }

    override fun onPause() {
        super.onPause()
        configFirestoreRealTime()
        configFirestoreRealTimeExplorer()
    }

    private fun setClick() {
        binding.ibCategoriesPhone.setOnClickListener {
            val intent = Intent(this, Phone_Activity::class.java)
            startActivity(intent)
        }
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
        binding.recyclerViewExplorar.apply {
            layoutManager = GridLayoutManager(
                this@HomeActivity, 1,
                GridLayoutManager.HORIZONTAL, false
            )
            adapterExplorer = adapterExplorer
        }
    }

    private fun configFirestoreRealTime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_DESTACADOS)

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

    //For Explorer Page
//    private fun configRecyclerViewExplorar() {
//        adapterExplorer = ProductExplorerAdapter(mutableListOf(), this)
//        binding.recyclerViewExplorar.apply {
//            layoutManager = GridLayoutManager(
//                this@HomeActivity, 1,
//                GridLayoutManager.HORIZONTAL, false
//            )
//            adapterExplorer = adapterExplorer
//        }
//    }

    private fun configFirestoreRealTimeExplorer() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(Constants.COLL_EXPLORAR)

        firestoreListener = productRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
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

}