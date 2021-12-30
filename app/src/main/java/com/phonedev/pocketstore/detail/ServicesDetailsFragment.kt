package com.phonedev.pocketstore.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import coil.request.CachePolicy
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentServicesDetailsBinding
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.pages.ServiciosActivity
import com.phonedev.pocketstore.product.MainAux

class ServicesDetailsFragment : Fragment() {

    private var binding: FragmentServicesDetailsBinding? = null
    private var product: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServicesDetailsBinding.inflate(inflater, container, false)
        binding?.let {
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? ServiciosActivity)?.supportActionBar?.hide()

        getProduct()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? ServiciosActivity)?.supportActionBar?.show()
    }

    private fun getProduct() {
        product = (activity as? MainAux)?.getProductSelected()
        product?.let { product ->
            binding?.let {
                it.tvName.text = product.name
                it.tvDescription.text = product.description
                it.tvTotalPrice.text = product.phone.toString()

                it.imgBackground.load(product.imgUrl) {
                    crossfade(true)
                    transformations(BlurTransformation(requireActivity(), 20f))
                    diskCachePolicy(CachePolicy.ENABLED)
                    build()
                }

                Glide.with(this)
                    .load(product.imgMap)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_timelapse)
                    .error(R.drawable.ic_broken_image)
                    .centerCrop()
                    .into(it.imgMap)

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

}