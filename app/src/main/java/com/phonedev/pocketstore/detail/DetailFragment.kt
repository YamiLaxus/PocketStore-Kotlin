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
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import coil.api.load
import coil.request.CachePolicy
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentDetailBinding
import com.phonedev.pocketstore.product.MainAux

class DetailFragment: Fragment() {

    private var binding: FragmentDetailBinding? = null
    private var product: Product? = null
    var number = 41642429

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        //(activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding?.let {
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProduct()
        product?.let { clickToAddCart(it) }
    }

    @SuppressLint("StringFormatMatches")
    private fun getProduct(){
        product = (activity as? MainAux)?.getProductSelected()
        product?.let { product ->
            binding?.let {
                it.tvName.text = product.name
                it.tvDescription.text = product.description
                it.tvDisponible.text = product.disponible
                binding?.etNewQuantity?.setText("1")
                setNewQuantity(product)

                it.imgBackground.load(product.imgUrl){
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

    private fun setNewQuantity(product: Product){
        binding?.let {
            it.etNewQuantity.setText(product.newQuantity.toString())
            it.tvTotalPrice.text = product.price.toString()
        }
    }

    private fun clickToAddCart(product: Product){
        binding?.btnAddCart?.setOnClickListener {
            addToCart(product)
        }
        binding?.btnBuyIt?.setOnClickListener {
            sendOrder()
        }
    }

    private fun addToCart(product: Product) {
        (activity as? MainAux)?.let {
            it.addProductToCart(product)
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun sendOrder() {
        if (binding?.etNewQuantity?.text?.isNotEmpty() == true) {
            sendMessage()
        } else {
            Toast.makeText(
                (activity as AppCompatActivity?)!!,
                "Ingresa la cantidad :)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun sendMessage() {

        var user = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        var cantidad = binding?.etNewQuantity?.text.toString().toInt()
        var total: Double = product?.price.toString().toDouble() * cantidad
        var cantidadCero = 0

        var pedido = ""
        pedido = pedido + "\n"
        pedido = pedido + "Pocket Store" + "\n"
        pedido = pedido + "CLIENTE: $user"
        pedido = pedido + "\n"
        pedido = pedido + "___________________________"

        binding?.let {
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

        val url = "https://wa.me/$number?text=$pedido"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}