package com.phonedev.pocketstore.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemExplorarBinding
import com.phonedev.pocketstore.pages.HomeActivity

class ProductExplorerAdapter(
    private val productList: MutableList<Product>,
    private val listener: HomeActivity
) :
    RecyclerView.Adapter<ProductExplorerAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_explorar, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.setListener(product)

        Glide.with(context)
            .load(product.imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_timelapse)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .into(holder.binding.imageProductDestacados)
    }

    override fun getItemCount(): Int = productList.size

    fun add(product: Product) {
        if (!productList.contains(product)) {
            productList.add(product)
            notifyItemInserted(productList.size - 1)
        } else {
            update(product)
        }
    }

    fun update(product: Product) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.set(index, product)
            notifyItemChanged(index)
        }
    }

    fun delete(product: Product) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemExplorarBinding.bind(view)

        fun setListener(product: Product) {
            binding.root.setOnClickListener {
                listener.onClick(product)
            }
        }
    }

}