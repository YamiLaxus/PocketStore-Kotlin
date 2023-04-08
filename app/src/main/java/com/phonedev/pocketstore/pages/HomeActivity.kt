package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.adapter.ProductosAdapter
import com.phonedev.pocketstore.adapter.ProductsMainAdapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.cart.CartFragment
import com.phonedev.pocketstore.databinding.ActivityHomeBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.ProductosModeloItem
import com.phonedev.pocketstore.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private var productosList: List<ProductosModeloItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val usuario = pref.getString("nombre", "")
        val imageUser = pref.getString("imagen", "")

        Glide.with(this)
            .load(imageUser.toString())
            .centerCrop().circleCrop().into(binding.imgPerfil)

        binding.progressBar.visibility = View.VISIBLE
        showProductsMain()
        clicChips()
        navigationDrawer()
        getProfile()
    }

    private fun navigationDrawer() {
        binding.imgCategorias.setOnClickListener {
            val i = Intent(this, CategoriasActivity::class.java)
            startActivity(i)
        }
        binding.imgBuscar.setOnClickListener {
            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)
        }
        binding.imgConfiguracion.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            startActivity(i)
        }
        binding.imgCarrito.setOnClickListener {
            val i = Intent(this, CarritoActivity::class.java)
            startActivity(i)
        }
        binding.imgFavoritos.setOnClickListener {
            val i = Intent(this, FavoritosActivity::class.java)
            startActivity(i)
        }
    }

    private fun showProducts() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)

        val retrofitData = retrofitBuilder.getAllProducts()
        retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
            override fun onResponse(
                call: Call<List<ProductosModeloItem>?>,
                response: Response<List<ProductosModeloItem>?>
            ) {
                val responseBody = response.body()!!
                val adapter = ProductosAdapter(responseBody)

//                productosList = responseBody
//                binding.recyclerViewHome.layoutManager =
//                    LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
//                binding.recyclerViewHome.adapter = adapter
//                binding.recyclerViewHome.setHasFixedSize(true)
//                binding.progressBar.visibility = View.GONE

                adapter.onItemClick = {
                    val i = Intent(this@HomeActivity, DetailActivity::class.java)
                    i.putExtra("producto", it)
                    startActivity(i)
                }
            }

            override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showProductsMain() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)

        val retrofitData = retrofitBuilder.getAllProducts()
        retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
            override fun onResponse(
                call: Call<List<ProductosModeloItem>?>,
                response: Response<List<ProductosModeloItem>?>
            ) {
                val responseBody = response.body()!!
                val adapter = ProductsMainAdapter(responseBody)

                productosList = responseBody
                binding.recyclerViewMain.layoutManager =
                    GridLayoutManager(this@HomeActivity, 2)
                binding.recyclerViewMain.adapter = adapter
                binding.progressBar.visibility = View.GONE

                adapter.onClick = {
                    val i = Intent(this@HomeActivity, DetailActivity::class.java)
                    i.putExtra("producto", it)
                    startActivity(i)
                }
            }

            override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clicChips() {
        binding.btnTodo.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            it.background.setTint(Color.BLACK)
            showProductsMain()
        }
        binding.btnAccesorios.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(WebServices::class.java)

            val retrofitData = retrofitBuilder.getAccesoriesPhone()
            retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
                override fun onResponse(
                    call: Call<List<ProductosModeloItem>?>,
                    response: Response<List<ProductosModeloItem>?>
                ) {
                    val responseBody = response.body()!!
                    val adapter = ProductsMainAdapter(responseBody)

                    productosList = responseBody
                    binding.recyclerViewMain.layoutManager =
                        GridLayoutManager(this@HomeActivity, 2)
                    binding.recyclerViewMain.adapter = adapter
                    binding.progressBar.visibility = View.GONE

                    adapter.onClick = {
                        val i = Intent(this@HomeActivity, DetailActivity::class.java)
                        i.putExtra("producto", it)
                        startActivity(i)
                    }
                }

                override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.btnTelefonos.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(WebServices::class.java)

            val retrofitData = retrofitBuilder.getPhone()
            retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
                override fun onResponse(
                    call: Call<List<ProductosModeloItem>?>,
                    response: Response<List<ProductosModeloItem>?>
                ) {
                    val responseBody = response.body()!!
                    val adapter = ProductsMainAdapter(responseBody)

                    productosList = responseBody
                    binding.recyclerViewMain.layoutManager =
                        GridLayoutManager(this@HomeActivity, 2)
                    binding.recyclerViewMain.adapter = adapter
                    binding.progressBar.visibility = View.GONE

                    adapter.onClick = {
                        val i = Intent(this@HomeActivity, DetailActivity::class.java)
                        i.putExtra("producto", it)
                        startActivity(i)
                    }
                }

                override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getProfile() {
        binding.imgPerfil.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            startActivity(i)
        }
    }
}