package com.phonedev.pocketstore.pages

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ActivityHomeBinding
import com.phonedev.pocketstore.detail.DetailHomeFragment
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.onProductListenner
import com.phonedev.pocketstore.product.MainAux
import com.phonedev.pocketstore.product.ProductExplorerAdapter
import com.phonedev.pocketstore.product.ProductosDestacadosAdapter


class HomeActivity : AppCompatActivity(), onProductListenner, MainAux {

    private lateinit var binding: ActivityHomeBinding

    //variables for Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var realTimeDatabase: FirebaseDatabase
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var firestoreListener: ListenerRegistration

    private lateinit var adapter: ProductosDestacadosAdapter
    private lateinit var adapterExplorer: ProductExplorerAdapter

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

        setClick()
        configRecyclerView()
        configFirestoreRealTime()
        configAuth()
        configFirestoreRealTimeExplorer()
        getUserName()
        configStackImages()
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
        configFirestoreRealTime()
        configFirestoreRealTimeExplorer()
        getUserName()
    }

//    override fun onBackPressed() {
//        AlertDialog.Builder(this)
//            .setMessage("Are you sure you want to exit?")
//            .setCancelable(false)
//            .setPositiveButton("Yes",
//                DialogInterface.OnClickListener { dialog, id -> super@HomeActivity.onBackPressed() })
//            .setNegativeButton("No", null)
//            .show()
//    }

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
        binding.btnKiki.setOnClickListener {
            val intent = Intent(this, KikiActivity::class.java)
            Toast.makeText(this, "Personaliza tu vida.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        binding.btnServices.setOnClickListener {
            val intent = Intent(this, ServiciosActivity::class.java)
            Toast.makeText(this, "Los mejores servicios.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

    private fun configStackImages() {
        val imageList = ArrayList<SlideModel>()

        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/fire-base-e4be5.appspot.com/o/banners%2Fpocket%20banner.jpg?alt=media&token=a41c7f7c-7f05-4028-a06b-5eb1ad4b8884",
                "Que es Pocket?"
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/fire-base-e4be5.appspot.com/o/banners%2Fange%20model.JPEG?alt=media&token=4d344aba-4e98-443d-8a5a-658fc4c6537a",
                "Los mejores productos"
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/fire-base-e4be5.appspot.com/o/banners%2Fkiki%20banner.jpg?alt=media&token=1ef03453-7a9a-48d8-9d8f-6b7412093bcd",
                "Lo mejor lo imaginas tú"
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/fire-base-e4be5.appspot.com/o/banners%2Fconecta2%20banner.jpg?alt=media&token=e36627cf-fc49-4540-aae7-c556b9dcc086",
                "El mejor Internet y Velocidad"
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/fire-base-e4be5.appspot.com/o/banners%2Fnetflix%20banner.jpg?alt=media&token=482a0102-70b5-477e-975c-37bd5949f43c",
                "Quieres ver una serie?"
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/fire-base-e4be5.appspot.com/o/banners%2Fdigitalgames%20banner.jpg?alt=media&token=415c5cf6-9e47-4e22-91ff-b57551f5dd7f",
                "Recarga ahora y se un Pro."
            )
        )
        binding.imgSlider.setImageList(imageList, ScaleTypes.FIT)
    }

}