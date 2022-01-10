package com.phonedev.pocketstore.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import coil.request.CachePolicy
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentDetailHomeBinding
import com.phonedev.pocketstore.product.MainAux

class DetailHomeFragment: Fragment() {
    private var binding: FragmentDetailHomeBinding? = null
    private var product: Product? = null
    private var number: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailHomeBinding.inflate(inflater, container, false)
        binding?.let {
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProduct()
        sendOrder()
    }

    private fun getProduct() {
        product = (activity as? MainAux)?.getProductSelected()
        product?.let { product ->
            binding?.let {
                it.tvName.text = product.name
                it.tvDescription.text = product.description
                it.tvTotalPrice.text = product.price.toString()
                it.tvDisponible.text = product.disponible
                binding?.etNewQuantity?.setText("1")

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
                    .centerCrop()
                    .into(it.imgProduct)
            }
        }
    }

    private fun sendOrder() {
        binding?.btnBuyIt?.setOnClickListener {
            if (product?.phone != null) {
                number = product?.phone.toString()
            }

            if (product?.phone == null) {
                number = "41642429"
            }

            sendMessage()
        }
    }

    fun sendMessage() {

        var user = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        var cantidad = binding?.etNewQuantity?.text.toString().toInt()
        var total: Double = product?.price.toString().toDouble() * cantidad


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

        val url = "https://wa.me/502$number?text=$pedido"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}