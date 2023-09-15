package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.databinding.ActivityConfirmationBinding
import com.phonedev.pocketstore.models.ProductosModeloItem

@Suppress("INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding

    private var product: ProductosModeloItem? = null
    private var user: Int? = 0
    private var cantidad: Int? = 0
    private var ncantidad: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()


        //GetDetails of Order
        val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        user = pref.getInt("id_usuario", 0)
        user.toString()

        this.product = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("producto")

        } else {
            intent.getParcelableExtra("producto")
        }

        if (product != null) {
            binding.tvName.text = product!!.nombre
            binding.tvTotalPrice.text = product!!.precio
            Glide.with(binding.imgProduct).load(product!!.imagen).into(binding.imgProduct)
        }

        cantidad = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("cantidad")
        } else {
            intent.getParcelableExtra("cantidad")
        }
        if (cantidad != null) {
            ncantidad = cantidad.toString().toInt()
        }
        clicks()
        confirmOrder()
    }


    private fun confirmOrder() {
        binding.radioCard.isClickable = false

    }

    private fun clicks() {
        binding.btnClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            this.finish()
        }
        binding.footerButton.setOnClickListener {
            buyNow()
        }
    }

    fun onRadioButtonClicked(view: View) {}

    private fun buyNow() {

      var pedido = ""
        pedido = pedido + "Pocket Store" + "\n"
        pedido += "CLIENTE: $user"
        pedido += "\n"
        pedido += "___________________________"

        val total: Double = product?.precio.toString().toDouble() * ncantidad.toString().toInt()
        binding.let {
            pedido = pedido +
                    "\n" +
                    "ID: ${product?.id_producto.toString()}" +
                    "\n" +
                    "Producto: ${product?.nombre.toString()}" +
                    "\n" +
                    "Cantidad: ${ncantidad}" +
                    "\n" +
                    "Precio: Q. ${product?.precio.toString()}" +
                    "\n" +
                    "___________________________" +
                    "\n" +
                    "TOTAL: Q.${total}"
        }

        val url = "https://wa.me/50241642429?text=$pedido"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}