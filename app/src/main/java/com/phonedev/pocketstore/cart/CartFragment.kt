package com.phonedev.pocketstore.cart

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentCartBinding
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.entities.Order
import com.phonedev.pocketstore.entities.ProductOrder
import com.phonedev.pocketstore.order.OrderActivity
import com.phonedev.pocketstore.product.MainAux

class CartFragment : BottomSheetDialogFragment(), OnCartListenner {

    private var binding: FragmentCartBinding? = null

    private lateinit var bottomSheetBehaivor: BottomSheetBehavior<*>
    private lateinit var adapter: ProductCartAdapter
    private var product: Product? = null
    private var totalPrice = 0.0

    private var number: String = ""

    private val listOnCart: MutableList<Product> = mutableListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(activity))
        binding?.let {
            val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)

            bottomSheetBehaivor = BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBehaivor.state = BottomSheetBehavior.STATE_EXPANDED

            setupRecyclerView()
            setupButtoms()

            getProducts()

            return bottomSheetDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setupRecyclerView() {
        binding?.let {
            adapter = ProductCartAdapter(mutableListOf(), this)

            it.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@CartFragment.adapter
            }
        }
    }

    private fun setupButtoms() {
        binding?.let {
            it.ibCancel.setOnClickListener {
                bottomSheetBehaivor.state = BottomSheetBehavior.STATE_HIDDEN
            }
            it.efab.setOnClickListener {
                if (product?.phone != null) {
                    number = product?.phone.toString()
                }

                if (product?.phone == null) {
                    number = "41642429"
                }

                sendMessage()
            }
        }
    }

    private fun requestOrder() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { myUser ->
            enableUI(false)

            val products = hashMapOf<String, ProductOrder>()
            adapter.getProducts().forEach { product ->
                products.put(
                    product.id!!,
                    ProductOrder(product.id!!, product.name!!, product.newQuantity)
                )
            }

            val order = Order(
                clientID = myUser.uid,
                products = products,
                totalPrice = totalPrice,
                status = 1
            )

            val db = FirebaseFirestore.getInstance()
            db.collection(Constants.COLL_REQUEST)
                .add(order)
                .addOnSuccessListener {
                    dismiss()
                    (activity as? MainAux)?.clearCart()
                    startActivity(Intent(context, OrderActivity::class.java))
                    Toast.makeText(activity, "Orden realizada.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Ocurrió un error en la orden.", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnCompleteListener {
                    enableUI(true)
                }
        }
    }

    private fun getProducts() {
        (activity as? MainAux)?.getProductsCart()?.forEach {
            adapter.add(it)
            listOnCart.addAll(listOf(it))
        }
    }

    private fun enableUI(enable: Boolean){
        binding?.let {
            it.ibCancel.isEnabled = enable
            it.efab.isEnabled = enable
        }
    }

    override fun onDestroyView() {
        (activity as? MainAux)?.updateTotal()
        super.onDestroyView()
        binding = null
    }

    override fun setQuantity(product: Product) {
        adapter.update(product)
    }

    override fun showTotal(total: Double) {
        totalPrice = total
        binding?.let {
            it.tvTotal.text = getString(R.string.product_full_cart, total)
        }
    }

    //Cart
    private fun sendMessage() {
        val firebaseAuth = FirebaseAuth.getInstance()
        var user = FirebaseAuth.getInstance().currentUser?.displayName.toString()

        if (firebaseAuth.currentUser?.displayName == null) {
            val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val database = Firebase.database.getReference(Constants.PATH_USERS).child(userID).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val nameUser = it.child("name").value
                        user = nameUser.toString()
                    }
                }
        }

        var pedido = ""

        pedido = pedido + "Pocket Store"
        pedido = pedido + "\n"
        pedido = pedido + "CLIENTE: $user"
        pedido = pedido + "\n"
        pedido = pedido + "___________________________"

        Toast.makeText(
            (activity as AppCompatActivity?)!!,
            "Total en lista: ${adapter.itemCount}",
            Toast.LENGTH_SHORT
        ).show()

        var index = 0
        while (index < listOnCart.size) {
            pedido = "$pedido" +
                    "\n" +
                    "\n" +
                    "Producto: ${listOnCart[index].name}" +
                    "\n" +
                    "Precios: ${listOnCart[index].price}" +
                    "\n" +
                    "Cantidad: ${listOnCart[index].newQuantity}" +
                    "\n" +
                    "___________________________\n"
            index++
        }
        pedido = pedido + "Total: Q.$totalPrice"

        val url = "https://wa.me/502$number?text=$pedido"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}