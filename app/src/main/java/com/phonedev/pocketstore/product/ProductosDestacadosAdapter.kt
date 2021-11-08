package com.phonedev.pocketstore.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemDestacadosBinding
import com.phonedev.pocketstore.entities.ProductosDestacados
import com.phonedev.pocketstore.pages.HomeActivity

class ProductosDestacadosAdapter(
    private val productsList: MutableList<ProductosDestacados>,
    private val listener: HomeActivity
) : RecyclerView.Adapter<ProductosDestacadosAdapter.ViewHolder>() {


    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_destacados, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductosDestacadosAdapter.ViewHolder, position: Int) {
        val product = productsList[position]

        holder.setListener(product)
        //holder.binding.tvQuantity.text = product.quantity.toString()

        Glide.with(context)
            .load(product.imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_timelapse)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .into(holder.binding.imageProductDestacados)
    }

    override fun getItemCount(): Int = productsList.size

    fun add(product: ProductosDestacados) {
        if (!productsList.contains(product)) {
            productsList.add(product)
            notifyItemInserted(productsList.size - 1)
        } else {
            update(product)
        }
    }

    fun update(product: ProductosDestacados) {
        val index = productsList.indexOf(product)
        if (index != -1) {
            productsList.set(index, product)
            notifyItemChanged(index)
        }
    }

    fun delete(product: ProductosDestacados) {
        val index = productsList.indexOf(product)
        if (index != -1) {
            productsList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDestacadosBinding.bind(view)

        fun setListener(product: ProductosDestacados) {
            binding.root.setOnClickListener {
                listener.onClickDestacado(product)
            }
        }
    }
}
