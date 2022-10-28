package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.phonedev.pocketstore.adapter.ProductosAdapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.databinding.ActivitySearchBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var productList: List<ProductosModeloItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.progressBar.visibility = View.GONE

        binding.btnCancelar.setOnClickListener {
            onBackPressed()
        }

        search()
    }

    private fun search() {
        binding.btnBuscar.setOnClickListener {

            val item = binding.etBuscar

            binding.progressBar.visibility = View.VISIBLE
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(WebServices::class.java)

            val retrofitData = retrofitBuilder.getBySearch(item.text.toString())
            retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
                override fun onResponse(
                    call: Call<List<ProductosModeloItem>?>,
                    response: Response<List<ProductosModeloItem>?>
                ) {
                    val responseBody = response.body()!!
                    val adapter = ProductosAdapter(responseBody)

                    productList = responseBody
                    binding.recyclerViewSearch.layoutManager =
                        GridLayoutManager(this@SearchActivity, 2)
                    binding.recyclerViewSearch.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                    if (binding.tvNoResult.visibility == View.VISIBLE) {
                        binding.tvNoResult.visibility = View.GONE
                    }

                    adapter.onItemClick = {
                        val i = Intent(this@SearchActivity, DetailActivity::class.java)
                        i.putExtra("producto", i)
                        startActivity(i)
                    }
                }

                override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                    binding.tvNoResult.visibility = View.VISIBLE
                }
            })
        }
    }
}