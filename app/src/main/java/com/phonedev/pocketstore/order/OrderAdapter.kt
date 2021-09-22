package com.phonedev.pocketstore.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.ItemOrderBinding
import com.phonedev.pocketstore.entities.Order

class OrderAdapter(
    private val orderList: MutableList<Order>,
    private val listener: OnOrderListener
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        var view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        holder.setListener(order)

        holder.binding.tvId.text = order.id

        var names = ""
        order.products.forEach {
            names += "${it.value.name}, "
        }
        holder.binding.tvProductName.text = names.dropLast(2)

        holder.binding.tvTotalPrice.text = order.totalPrice.toString()
    }

    override fun getItemCount(): Int = orderList.size

    fun add(order: Order){
        orderList.add(order)
        notifyItemInserted(orderList.size -1)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrderBinding.bind(view)

        fun setListener(order: Order) {
            binding.btnTrack.setOnClickListener {
                listener.onStartChat(order)
            }

            binding.chipChat.setOnClickListener {
                listener.onStartChat(order)
            }
        }
    }

}