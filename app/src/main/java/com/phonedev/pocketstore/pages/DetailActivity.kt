package com.phonedev.pocketstore.pages

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request.Method.POST
import com.android.volley.RequestQueue
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var product: ProductosModeloItem? = null
    private var user: Int? = 0
    private var estadoAgotado: String = "AGOTADO"
    private var fecha: String? = null
    var cantidadI: Int? = 0

    @RequiresApi(Build.VERSION_CODES.O)
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
        getTime()
        refreshComments()
        paintFlagText()
    }

    private fun paintFlagText() {
        if (binding.tvTotalPrice.text == estadoAgotado) {
            binding.tvTotalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTime() {
        val time = LocalTime.now()
        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        val current = time.hour
        if (current >= 18) {
            if (binding.tvDisponible.text != estadoAgotado) {
                binding.tvDisponible.text = "Recibe Mañana"
            }
        }
        fecha = dateTime.toString()
    }

    private fun click() {
        binding.btnBuyIt.setOnClickListener {
            //buyNow()
            if (binding.tvDisponible.text == estadoAgotado) {
                paintFlagText()
                showAlert()
            } else {
                confirmOrder()
            }
        }
        binding.btnAddFav.setOnClickListener {
            addFav()
        }
        binding.btnComment.setOnClickListener {
            insertComment()
            binding.btnComment.isEnabled = false
            startTimer()
        }
        binding.txtDeleteComments.setOnClickListener {
            deleteComment()
            binding.txtDeleteComments.isEnabled = false
        }
    }

    private fun confirmOrder() {
        val i = Intent(this, ConfirmationActivity::class.java)
        i.putExtra("producto", product)
        i.putExtra("cantidad", binding.etNewQuantity.text.toString())
        startActivity(i)
    }

    private fun setNuevaCantidad(cantidad: Int) {
        if (binding.etNewQuantity.text.isNullOrEmpty()) {
            binding.etNewQuantity.setText("1").toString().toInt()
        } else {
            binding.etNewQuantity.setText("$cantidad")
            cantidadI = cantidad
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
        val url = BASE_URL
        val queue = Volley.newRequestQueue(this)

        if (binding.commentText.text!!.isNotEmpty()) {
            val resultPost = object : StringRequest(POST, url + "insert_comment.php", { response ->
                Toast.makeText(this, "Publicando comentario... exitoso!.", Toast.LENGTH_SHORT)
                    .show()
                binding.commentText.text = null
                getComments()
            },
                { error ->
                    Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val parameters = HashMap<String, String>()
                    parameters["id_producto"] = product?.id_producto.toString()
                    parameters["id_usuario"] = user.toString()
                    parameters["text"] = binding.commentText.text.toString().trim()
                    return parameters
                }
            }
            queue.add(resultPost)
        } else {
            Toast.makeText(
                this,
                "Error, debes escribir tu comentario o intenta de nuevo mas tarde.",
                Toast.LENGTH_SHORT
            ).show()
        }
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
                binding.commentEmpty.visibility = View.GONE
                binding.commentTotal.text = adapter.itemCount.toString()
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<List<ComentariosModel>?>, t: Throwable) {
                Log.d("Error Comments", t.toString())
            }
        })
    }

    private fun deleteComment() {
        val url = BASE_URL
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val resultPost = object : StringRequest(POST, url + "delete_comments.php",
            com.android.volley.Response.Listener { response ->
                Toast.makeText(this, "Comentario eliminado con éxito.", Toast.LENGTH_SHORT).show()
                getComments()
            },
            com.android.volley.Response.ErrorListener { error ->
                Toast.makeText(this, "Ups! Al parecer no hay comentarios.", Toast.LENGTH_SHORT)
                    .show()
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String, String>()
                parametros.put("id_usuario", user.toString())
                parametros.put("id_producto", product!!.id_producto.toString())
                return parametros
            }
        }
        queue.add(resultPost)
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

    //To Refresh an activity
    private fun refreshComments() {
        binding.refreshLayout.setOnRefreshListener {
            // Your code goes here
            binding.refreshLayout.isRefreshing = true
            this.getComments()
            Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show()
            binding.recyclerViewComments.refreshDrawableState()

            // This line is important as it explicitly refreshes only once
            binding.refreshLayout.isRefreshing = false
        }
    }

    //Desabilitar temporalmente bonton comentar
    private fun startTimer() {
        object : CountDownTimer(2000, 500) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                binding.btnComment.isEnabled = true
                binding.txtDeleteComments.isEnabled = true
            }
        }.start()
    }

    //Show Alert's
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Lo sentimos :(")
        builder.setMessage("De momento no hay stock de este producto.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}