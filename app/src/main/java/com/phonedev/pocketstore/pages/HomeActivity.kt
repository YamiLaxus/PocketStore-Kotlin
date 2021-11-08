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
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.entities.ProductosDestacados
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.product.MainAux
import com.phonedev.pocketstore.product.ProductAdapter
import com.phonedev.pocketstore.product.ProductosDestacadosAdapter


class HomeActivity : AppCompatActivity(), onProductListenner, MainAux{

    private lateinit var binding: ActivityHomeBinding

    private lateinit var firestoreListenner: ListenerRegistration

    lateinit var adapter: ProductosDestacadosAdapter

    private var productSelected: ProductosDestacadosAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClick()
        configRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        configFirestoreRealTime()
    }

    override fun onPause() {
        super.onPause()
        configFirestoreRealTime()
    }

    fun setClick(){
        binding.ibCategoriesPhone.setOnClickListener {
            val intent = Intent(this, Phone_Activity::class.java)
            startActivity(intent)
        }
    }

    private fun configRecyclerView() {
        adapter = ProductosDestacadosAdapter(mutableListOf(), this)
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
        TODO("Not yet implemented")
    }

    override fun onClickDestacado(product: ProductosDestacados) {
        TODO("Not yet implemented")
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

    override fun getProductSelected(): Product? {
        TODO("Not yet implemented")
    }

    override fun showButton(isVisible: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addProductToCart(product: Product) {
        TODO("Not yet implemented")
    }
}