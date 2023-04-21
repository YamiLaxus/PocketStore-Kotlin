package com.phonedev.pocketstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemRecienteViewBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

class FavoritosAdapter(private val productosList: List<ProductosModeloItem>) :
    RecyclerView.Adapter<FavoritosAdapter.ViewHolder>() {

    var onItemClick: ((ProductosModeloItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_reciente_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productosList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = productosList.size
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(producto: ProductosModeloItem) {
            val binding = ItemRecienteViewBinding.bind(itemView)
            binding.txtNombre.text = producto.nombre
            val img = itemView.findViewById<ImageView>(R.id.imgProduct)
            Glide.with(img)
                .load(producto.imagen)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().into(img)
        }
    }
}