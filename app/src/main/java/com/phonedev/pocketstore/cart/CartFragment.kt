package com.phonedev.pocketstore.cart

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.phonedev.pocketstore.Product
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentCartBinding
import com.phonedev.pocketstore.order.OrderActivity
import com.phonedev.pocketstore.product.MainAux

class CartFragment : BottomSheetDialogFragment(), OnCartListenner {

    private var binding: FragmentCartBinding? = null

    private lateinit var bottomSheetBehaivor: BottomSheetBehavior<*>

    private lateinit var adapter: ProductCartAdapter

    private var totalPrice = 0.0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(activity))
        binding?.let {
            val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)

            bottomSheetBehaivor = BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBehaivor.state = BottomSheetBehavior.STATE_EXPANDED

            setupRecyclerView()
            setupButtoms()

            getProducts()

            return bottomSheetDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setupRecyclerView() {
        binding?.let {
            adapter = ProductCartAdapter(mutableListOf(), this)

            it.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@CartFragment.adapter
            }

            /*(1..5).forEach {
                val product = Product(it.toString(), "Product $it", "This Product is $it", "", it, 2.0 * it)
                adapter.add(product)
            }*/
        }
    }

    private fun setupButtoms(){
        binding?.let {
            it.ibCancel.setOnClickListener {
                bottomSheetBehaivor.state = BottomSheetBehavior.STATE_HIDDEN
            }
            it.efab.setOnClickListener {
                requestOrder()
            }
        }
    }

    private fun requestOrder(){
        dismiss()
        (activity as? MainAux)?.clearCart()
        startActivity(Intent(context, OrderActivity::class.java))
    }

    private fun getProducts(){
        (activity as? MainAux)?.getProductsCart()?.forEach {
            adapter.add(it)
        }
    }

    override fun onDestroyView() {
        (activity as? MainAux)?.updateTotal()
        super.onDestroyView()
        binding = null
    }

    override fun setQuantity(product: Product) {
        adapter.update(product)
    }

    override fun showTotal(total: Double) {
        totalPrice = total
        binding?.let {
            it.tvTotal.text = getString(R.string.product_full_cart, total)
        }
    }
}