package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.phonedev.pocketstore.adapter.CategoriasApadapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.databinding.ActivityCategoriasBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.CategoriasModelo
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriasBinding

    private var categList: List<CategoriasModelo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        showCategorias()
    }

    private fun showCategorias() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)

        val retrofitData = retrofitBuilder.getCategoriesLIst()
        retrofitData.enqueue(object : Callback<List<CategoriasModelo>?> {
            override fun onResponse(
                call: Call<List<CategoriasModelo>?>,
                response: Response<List<CategoriasModelo>?>
            ) {
                val responseBody = response.body()!!
                val adapter = CategoriasApadapter(responseBody)

                categList = responseBody
                binding.recyclerViewMain.layoutManager =
                    GridLayoutManager(this@CategoriasActivity, 1)
                binding.recyclerViewMain.adapter = adapter

                adapter.onItemClick = {
                    val i = Intent(this@CategoriasActivity, AccActivity::class.java)
                    val id = it.id_categoria.toString()
                    i.putExtra("catId", id)
                    startActivity(i)
                }
            }

            override fun onFailure(call: Call<List<CategoriasModelo>?>, t: Throwable) {
                Toast.makeText(this@CategoriasActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}