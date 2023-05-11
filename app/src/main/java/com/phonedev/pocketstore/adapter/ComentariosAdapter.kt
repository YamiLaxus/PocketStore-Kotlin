package com.phonedev.pocketstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.CommentsViewBinding
import com.phonedev.pocketstore.models.ComentariosModel

class ComentariosAdapter(private val comentList: List<ComentariosModel>) :
    RecyclerView.Adapter<ComentariosAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(comment: ComentariosModel) {
            val binding = CommentsViewBinding.bind(itemView)
            binding.tvName.text = comment.nombre
            binding.tvComment.text = comment.text
            Glide.with(binding.imageProfile)
                .load(comment.imagen)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.comments_view, parent, false))
    }

    override fun getItemCount(): Int = comentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = comentList[position]
        holder.render(item)
        
    }
}