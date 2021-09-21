package com.phonedev.pocketstore.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemProductCartBinding

class ProductCartAdapter(private val productList: MutableList<Product>,
                         private val listener: OnCartListenner) :
    RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_cart, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.setListenner(product)

        holder.binding.tvName.text = product.name
        holder.binding.tvQuantity.text = product.newQuantity.toString()

        Glide.with(context)
            .load(product.imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_timelapse)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .circleCrop()
            .into(holder.binding.imgProduct)
    }

    override fun getItemCount(): Int = productList.size

    fun add(product: Product){
        if (!productList.contains(product)){
            productList.add(product)
            notifyItemInserted(productList.size - 1)
            calcTotal()
        } else{
            update(product)
        }
    }

    fun update(product: Product){
        val index = productList.indexOf(product)
        if (index != -1){
            productList.set(index, product)
            notifyItemChanged(index)
            calcTotal()
        }
    }

    fun delete(product: Product){
        val index = productList.indexOf(product)
        if (index != -1){
            productList.removeAt(index)
            notifyItemRemoved(index)
            calcTotal()
        }
    }

    private fun calcTotal(){
        var result = 0.0
        for (product in productList){
            result += product.totalPrice()
        }
        listener.showTotal(result)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemProductCartBinding.bind(view)

        fun setListenner(product: Product){
            binding.ibSum.setOnClickListener {
                product.newQuantity += 1
                listener.setQuantity(product)
            }
            binding.ibSub.setOnClickListener {
                product.newQuantity -= 1
                listener.setQuantity(product)
            }
        }
    }
}