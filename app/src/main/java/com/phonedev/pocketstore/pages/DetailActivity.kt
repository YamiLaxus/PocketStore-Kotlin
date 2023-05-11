package com.phonedev.pocketstore.pages

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request.Method.POST
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.adapter.ComentariosAdapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.databinding.ActivityDetailBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.ComentariosModel
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var product: ProductosModeloItem? = null
    private var user: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //getUserId
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
            binding.tvDescription.text = product!!.descripcion
            binding.tvTotalPrice.text = product!!.precio
            binding.tvDisponible.text = product!!.tiempor_entrega
            Glide.with(binding.imgProduct).load(product!!.imagen).into(binding.imgProduct)
        }

        binding.imgProduct.setOnClickListener {
            val i = Intent(this, ImageActivity::class.java)
            i.putExtra("producto", product)
            startActivity(i)
        }

        click()
        getComments()
    }

    private fun click() {
        binding.btnBuyIt.setOnClickListener {
            buyNow()
        }
        binding.btnAddFav.setOnClickListener {
            addFav()
        }
    }

    private fun setNuevaCantidad(cantidad: Int) {
        if (binding.etNewQuantity.text.isNullOrEmpty()) {
            binding.etNewQuantity.setText("1").toString().toInt()
        } else {
            binding.etNewQuantity.setText("$cantidad")
        }
    }

    private fun addFav() {
        val url = BASE_URL
        val queue = Volley.newRequestQueue(this)

        val resultPost = object : StringRequest(POST, url + "insert_fav.php", { response ->
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        }, { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val parameter = HashMap<String, String>()
                parameter["id_producto"] = product!!.id_producto.toString()
                parameter["id_usuario"] = user!!.toInt().toString()
                return parameter
            }
        }
        queue.add(resultPost)
    }

    private fun insertComment() {
        //TODO comments insert's
    }

    private fun getComments() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)

        val retrofitData =
            retrofitBuilder.getComments(url = BASE_URL + "get_comments.php?id_producto=${product?.id_producto.toString()}")

        retrofitData.enqueue(object : Callback<List<ComentariosModel>?> {
            override fun onResponse(
                call: Call<List<ComentariosModel>?>,
                response: Response<List<ComentariosModel>?>
            ) {
                val responseBody = response.body()!!
                val adapter = ComentariosAdapter(responseBody)
                binding.recyclerViewComments.layoutManager =
                    GridLayoutManager(this@DetailActivity, 1)
                binding.recyclerViewComments.adapter = adapter
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<List<ComentariosModel>?>, t: Throwable) {
                Log.d("Error Comments no comments", t.toString())
            }
        })
    }

    private fun buyNow() {
        //Fix quantity default 1
        var nuevaCantidad = binding.etNewQuantity.text.toString().toInt()
        if (nuevaCantidad.equals(null)) {
            nuevaCantidad = "1".toInt()
        } else {
            setNuevaCantidad(nuevaCantidad)
        }

        product = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("producto")
        } else {
            intent.getParcelableExtra("producto")
        }
        if (product != null) {
            binding.tvName.text = product!!.nombre
            binding.tvDescription.text = product!!.descripcion
            binding.tvTotalPrice.text = product!!.precio
            binding.tvDisponible.text = product!!.tiempor_entrega
            Glide.with(binding.imgProduct).load(product!!.imagen).into(binding.imgProduct)


        }

        var pedido = ""
        pedido = pedido + "Pocket Store" + "\n"
        pedido += "CLIENTE: $user"
        pedido += "\n"
        pedido += "___________________________"

        val total: Double = product?.precio.toString().toDouble() * nuevaCantidad
        binding.let {
            pedido = pedido +
                    "\n" +
                    "ID: ${product?.id_producto.toString()}" +
                    "\n" +
                    "Producto: ${product?.nombre.toString()}" +
                    "\n" +
                    "Cantidad: $nuevaCantidad" +
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