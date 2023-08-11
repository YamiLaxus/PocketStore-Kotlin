package com.phonedev.pocketstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.cart.OnCartListenner
import com.phonedev.pocketstore.databinding.ItemProductCartBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

class CarritoAdapter(
    private val cartList: List<ProductosModeloItem>
) :
    RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    var onItemClick: ((ProductosModeloItem) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemProductCartBinding.bind(itemView)
        fun render(product: ProductosModeloItem) {
            val binding = ItemProductCartBinding.bind(itemView)
            binding.tvName.text = product.nombre
            Glide.with(binding.imgProduct)
                .load(product.imagen).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_timelapse)
                .error(R.drawable.ic_broken_image)
                .circleCrop()
                .centerCrop().into(binding.imgProduct)
        }

        fun setListener(p: ProductosModeloItem) {
            binding.ibSum.setOnClickListener {
                p.nuevaCantidad += 1
            }
            binding.ibSub.setOnClickListener {
                p.nuevaCantidad -= 1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_product_cart, parent, false))
    }

    override fun getItemCount(): Int = cartList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    //Calcular el precio total del carrito
    private fun calcularPrecioTotal() {
        var result = 0.0
        for (i in cartList) {
            result += i.totalPrice()
        }
    }

    //Delete Cart item
    fun delete(p: ProductosModeloItem) {
        val index = cartList.indexOf(p)
        if (index != -1) {
            notifyItemRemoved(index)
            calcularPrecioTotal()
        }
    }

    //Update cart item
    fun update(p: ProductosModeloItem) {
        val index = cartList.indexOf(p)
        if (index != -1) {
            notifyItemChanged(index)
            calcularPrecioTotal()
        }
    }
}
