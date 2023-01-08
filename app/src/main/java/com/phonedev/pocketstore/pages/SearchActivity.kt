package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.GridLayoutManager
import com.phonedev.pocketstore.adapter.ProductosAdapter
import com.phonedev.pocketstore.adapter.ProductsMainAdapter
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

    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnCancelar.setOnClickListener {
            onBackPressed()
        }

        search()
    }

    private fun search() {
        binding.btnBuscar.setOnClickListener {


            val item = binding.etBuscar.text.toString().trim()

            progressBar?.visibility = View.VISIBLE
            binding.tvNoResult.visibility = View.GONE

            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(WebServices::class.java)

            val retrofitData =
                retrofitBuilder.getBySearch(url = BASE_URL + "buscar.php?nombre=$item")

            retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
                override fun onResponse(
                    call: Call<List<ProductosModeloItem>?>,
                    response: Response<List<ProductosModeloItem>?>
                ) {
                    val responseBody = response.body()!!
                    val adapter = ProductsMainAdapter(responseBody)
                    productList = responseBody
                    binding.recyclerViewMain.layoutManager =
                        GridLayoutManager(this@SearchActivity, 2)
                    binding.recyclerViewMain.adapter = adapter
                    progressBar?.visibility = View.GONE
                    if (productList!!.isEmpty()) {
                        binding.tvNoResult.visibility = View.VISIBLE
                    } else {
                        binding.tvNoResult.visibility = View.GONE
                    }
                    adapter.onClick = {
                        val i = Intent(this@SearchActivity, DetailActivity::class.java)
                        i.putExtra("producto", i)
                        startActivity(i)
                    }
                }

                override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                    productList = null
                    progressBar?.visibility = View.GONE
                    binding.tvNoResult.visibility = View.VISIBLE
                    Log.d("Error Search", t.toString())
                }
            })
        }
    }
}