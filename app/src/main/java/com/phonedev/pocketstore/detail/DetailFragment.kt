package com.phonedev.pocketstore.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import coil.api.load
import coil.request.CachePolicy
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentDetailBinding
import com.phonedev.pocketstore.product.MainAux

class DetailFragment: Fragment() {

    private var binding: FragmentDetailBinding? = null
    private var product: Product? = null


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

            var newQuantityStr = getString(R.string.detail_total_price, product.totalPrice(),
                product.newQuantity, product.price)

            it.tvTotalPrice.text = product.price.toString()
        }
    }

    private fun clickToAddCart(product: Product){
        binding?.btnAddCart?.setOnClickListener {
            addToCart(product)
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
}