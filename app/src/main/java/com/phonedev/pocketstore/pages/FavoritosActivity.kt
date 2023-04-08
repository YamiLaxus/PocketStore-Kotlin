package com.phonedev.pocketstore.pages

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.phonedev.pocketstore.adapter.ProductsMainAdapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.databinding.ActivityFavoritosBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding

    private var productList: List<ProductosModeloItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //getUserId
        val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val user = pref.getInt("id_usuario", 0)

        getFav(user)
    }

    private fun getFav(user: Int?) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)

        val retrofitData =
            retrofitBuilder.getByFavorites(url = BASE_URL + "get_fav.php?id_usuario=${user}")

        retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
            override fun onResponse(
                call: Call<List<ProductosModeloItem>?>,
                response: Response<List<ProductosModeloItem>?>
            ) {
                val responseBody = response.body()!!
                val adapter = ProductsMainAdapter(responseBody)
                productList = responseBody
                binding.recyclerViewMain.layoutManager =
                    GridLayoutManager(this@FavoritosActivity, 2)
                binding.recyclerViewMain.adapter = adapter
                adapter.onClick = { producto ->
                    val i = Intent(this@FavoritosActivity, DetailActivity::class.java)
                    i.putExtra("producto", producto)
                    startActivity(i)
                }

                binding.progressBar.visibility = View.GONE
                if (productList!!.isEmpty()) {
                    binding.tvNoResult.visibility = View.VISIBLE
                } else {
                    binding.tvNoResult.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                productList = null
                binding.progressBar.visibility = View.GONE
                binding.tvNoResult.visibility = View.VISIBLE
                Log.d("Error Search", t.toString())
            }
        })

    }
}