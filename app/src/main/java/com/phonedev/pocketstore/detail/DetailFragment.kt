package com.phonedev.pocketstore.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.api.load
import coil.request.CachePolicy
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentDetailBinding
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.pages.*
import com.phonedev.pocketstore.product.MainAux

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var product: Product? = null
    private var number: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding.let {
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClick()
        getProduct()
        product?.let { clickToAddCart(it) }
    }

    @SuppressLint("StringFormatMatches")
    private fun getProduct() {
        product = (activity as? MainAux)?.getProductSelected()
        product?.let { product ->
            binding.let {
                it.tvName.text = product.name
                it.tvDescription.text = product.description
                it.tvDisponible.text = product.disponible
                binding.etNewQuantity.setText("1")
                setNewQuantity(product)

                it.imgBackground.load(product.imgUrl) {
                    crossfade(true)
                    transformations(BlurTransformation(requireActivity(), 20f))
                    diskCachePolicy(CachePolicy.ENABLED)
                    build()
                }

                Glide.with(this)
                    .load(product.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_timelapse)
                    .error(R.drawable.ic_broken_image)
                    .fitCenter()
                    .into(it.imgProduct)
                //.into(it.imgBackground)
            }
        }
    }

    private fun setNewQuantity(product: Product) {
        binding.let {
            it.etNewQuantity.setText(product.newQuantity.toString())
            it.tvTotalPrice.text = product.price.toString()
        }
    }

    //toBuy and go to facebook
    private fun clickToAddCart(product: Product) {
        binding.btnAddCart.setOnClickListener {
            addToCart(product)
        }
        binding.btnBuyIt.setOnClickListener {
            if (product.phone != null) {
                number = product.phone.toString()
            }

            if (product.phone == null) {
                number = "41642429"
            }

            sendOrder()
        }
        binding.imbFacebook.setOnClickListener {
            if (product.facebook == null) {
                Toast.makeText(
                    (activity as AppCompatActivity?)!!,
                    "Lo sientimos no puedes ir a Facebook :/",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val url = "${product.facebook}"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    private fun addToCart(product: Product) {
        (activity as? MainAux)?.let {
            it.addProductToCart(product)
            activity?.onBackPressed()
        }
    }

    private fun sendOrder() {
        if (binding.etNewQuantity.text?.isNotEmpty() == true) {
            sendMessage()
        } else {
            Toast.makeText(
                (activity as AppCompatActivity?)!!,
                "Ingresa la cantidad :)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun sendMessage() {
        val firebaseAuth = FirebaseAuth.getInstance()
        var user = firebaseAuth.currentUser?.displayName.toString()
        val cantidad = binding.etNewQuantity.text.toString().toInt()
        val total: Double = product?.price.toString().toDouble() * cantidad

        if (firebaseAuth.currentUser?.displayName != null) {
            user = firebaseAuth.currentUser?.displayName.toString()
        } else {
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
        pedido = pedido + "\n"
        pedido = pedido + "Pocket Store" + "\n"
        pedido = pedido + "CLIENTE: $user"
        pedido = pedido + "\n"
        pedido = pedido + "___________________________"

        binding.let {
            pedido = pedido +
                    "\n" +
                    "Producto: ${product?.name.toString()}" +
                    "\n" +
                    "Cantidad: ${cantidad.toString()}" +
                    "\n" +
                    "Precio: Q. ${product?.price.toString()}" +
                    "\n" +
                    "___________________________" +
                    "\n" +
                    "TOTAL: Q. ${total.toString()}"
        }

        val url = "https://wa.me/502$number?text=$pedido"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun setClick() {
        binding.ibCategoriesAcc.setOnClickListener {
            val intent = Intent((activity as AppCompatActivity), AccActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                (activity as AppCompatActivity),
                "Vamos, Revisa los accesorios.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.ibCategoriesPhone.setOnClickListener {
            val intent = Intent((activity as AppCompatActivity), PhoneActivity::class.java)
            Toast.makeText(
                (activity as AppCompatActivity),
                "Bien, Ahora verás teléfonos.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }
        binding.ibCategoriesTablet.setOnClickListener {
            val intent = Intent((activity as AppCompatActivity), TabletsActivity::class.java)
            Toast.makeText(
                (activity as AppCompatActivity),
                "Excelente, ¿Quieres una tablet?",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }
        binding.ibCategoriesArt.setOnClickListener {
            val intent = Intent((activity as AppCompatActivity), ArtActivity::class.java)
            Toast.makeText(
                (activity as AppCompatActivity),
                "El arte es una garantía de cordura.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }
        binding.ibCategoriesKiki.setOnClickListener {
            val intent = Intent((activity as AppCompatActivity), KikiActivity::class.java)
            Toast.makeText(
                (activity as AppCompatActivity),
                "Personaliza tu vida",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }
        binding.ibCategoriesServices.setOnClickListener {
            val intent = Intent((activity as AppCompatActivity), ServiciosActivity::class.java)
            Toast.makeText(
                (activity as AppCompatActivity),
                "Reparemos un par de cosas.",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(intent)
        }
    }
}