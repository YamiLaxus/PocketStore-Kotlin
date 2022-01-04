package com.phonedev.pocketstore.product

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemProductBinding
import com.phonedev.pocketstore.entities.Product
import com.phonedev.pocketstore.pages.TabletsActivity
import java.util.stream.Collectors

class TabletsAdapter (
    private val productList: MutableList<Product>,
    private val listener: TabletsActivity): RecyclerView.Adapter<TabletsAdapter.ViewHolder>(){

    private lateinit var context: Context

    private val productListOriginal = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.setListener(product)

        holder.binding.tvName.text = product.name
        holder.binding.tvPrice.text = product.price.toString()
        holder.binding.tvDescription.text = product.description

        Glide.with(context)
            .load(product.imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_timelapse)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .into(holder.binding.imageProduct)
    }

    override fun getItemCount(): Int = productList.size

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.N)
    fun filtrado(newText: String) {
        var longitud: Int = newText.length
        if (longitud != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                var coleccion: MutableList<Product>? = productList.stream()
                    .filter { it.name?.toLowerCase()!!.contains(newText.toLowerCase()) }
                    .collect(Collectors.toList())
                productList.clear()
                productList.addAll(coleccion!!)
            }
        } else {
            productList.addAll(productListOriginal)
        }
        notifyDataSetChanged()
    }

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
        val binding = ItemProductBinding.bind(view)

        fun setListener(product: Product) {
            binding.root.setOnClickListener {
                listener.onClick(product)
            }
        }
    }
}