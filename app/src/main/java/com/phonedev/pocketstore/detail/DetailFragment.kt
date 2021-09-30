package com.phonedev.pocketstore.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.Product
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding?.let {
            return it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProduct()
        setupButtons()
    }

    @SuppressLint("StringFormatMatches")
    private fun getProduct(){
        product = (activity as? MainAux)?.getProductSelected()
        product?.let { product ->
            binding?.let {
                it.tvName.text = product.name
                it.tvDescription.text = product.description
                //it.tvQuantity.text = getString(R.string.detail_quantity, product.quantity)
                setNewQuantity(product)


                Glide.with(this)
                    .load(product.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_timelapse)
                    .error(R.drawable.ic_broken_image)
                    .fitCenter()
                    //.into(it.imgProduct)
                    .into(it.imgBackground)
            }
        }
    }

    private fun setNewQuantity(product: Product){
        binding?.let {
            it.etNewQuantity.setText(product.newQuantity.toString())

            var newQuantityStr = getString(R.string.detail_total_price, product.totalPrice(),
                product.newQuantity, product.price)

            it.tvTotalPrice.text = HtmlCompat.fromHtml(newQuantityStr, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }


    private fun setupButtons(){
        product?.let { product ->
            binding?.let { binding ->
                binding.ibSub.setOnClickListener {
                    if(product.newQuantity > 1){
                        product.newQuantity -= 1
                        setNewQuantity(product)
                    }
                }
                binding.ibSum.setOnClickListener {
                    if(product.newQuantity < product.quantity){
                        product.newQuantity += 1
                        setNewQuantity(product)
                    }
                }
                binding.efab.setOnClickListener {
                    product.newQuantity = binding.etNewQuantity.text.toString().toInt()
                    addToCart(product)
                }
            }
        }
    }

    private fun addToCart(product: Product) {
        (activity as? MainAux)?.let {
            it.addProductToCart(product)
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        (activity as? MainAux)?.showButton(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        super.onDestroyView()
        binding = null
    }
}