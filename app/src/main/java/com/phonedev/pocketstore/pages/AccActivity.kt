package com.phonedev.pocketstore.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.phonedev.pocketstore.adapter.ProductsMainAdapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.databinding.ActivityAccBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("DEPRECATION")
class AccActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccBinding

    private var productList: List<ProductosModeloItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        getProductos()
    }

    private fun getProductos() {
        val bundle = intent.extras
        val id = bundle?.getString("catId")

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)


        //Solution temporal a dynamicURL
        val retrofitData =
            retrofitBuilder.getPorCategoria(url = BASE_URL + "categorias.php?fk_categoria=$id")
        retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
            override fun onResponse(
                call: Call<List<ProductosModeloItem>?>,
                response: Response<List<ProductosModeloItem>?>
            ) {
                val responseBody = response.body()!!
                val adapter = ProductsMainAdapter(responseBody)

                productList = responseBody

                binding.recyclerView.layoutManager = GridLayoutManager(this@AccActivity, 2)
                binding.recyclerView.adapter = adapter
                binding.llProgress.visibility = View.GONE
                binding.nsvProductos.visibility = View.VISIBLE


                adapter.onClick = {
                    val i = Intent(this@AccActivity, DetailActivity::class.java)
                    i.putExtra("producto", it)
                    startActivity(i)
                }
            }

            override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                Toast.makeText(this@AccActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}