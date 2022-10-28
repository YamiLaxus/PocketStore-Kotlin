package com.phonedev.pocketstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemViewMainBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

class ProductsMainAdapter(private val productosList: List<ProductosModeloItem>) :
    RecyclerView.Adapter<ProductsMainAdapter.ViewHolder>() {

    var onClick: ((ProductosModeloItem) -> Unit)? = null

    class ViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        fun render(producto: ProductosModeloItem) {
            val binding = ItemViewMainBinding.bind(itemView)
            binding.tvProductName.text = producto.nombre
            val img = itemView.findViewById<ImageView>(R.id.imgProduct)
            Glide.with(img)
                .load(producto.imagen)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_view_main, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productosList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = productosList.size
}