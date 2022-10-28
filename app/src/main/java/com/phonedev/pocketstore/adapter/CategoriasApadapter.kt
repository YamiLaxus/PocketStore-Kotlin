package com.phonedev.pocketstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.CategoriasListViewBinding
import com.phonedev.pocketstore.models.CategoriasModelo
import com.phonedev.pocketstore.models.ProductosModeloItem
import kotlinx.coroutines.awaitAll

class CategoriasApadapter(private val categoeriasList: List<CategoriasModelo>) :
    RecyclerView.Adapter<CategoriasApadapter.ViewHolder>() {

    var onItemClick: ((CategoriasModelo) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(cat: CategoriasModelo) {
            val binding = CategoriasListViewBinding.bind(itemView)
            binding.tvCategoria.text = cat.descripcion
            Glide.with(binding.imgCategoria)
                .load(cat.imagen)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgCategoria)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.categorias_list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoeriasList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = categoeriasList.size
}