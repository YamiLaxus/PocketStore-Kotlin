package com.phonedev.pocketstore.pages

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.phonedev.pocketstore.adapter.CarritoAdapter
import com.phonedev.pocketstore.apis.WebServices
import com.phonedev.pocketstore.databinding.ActivityCarritoBinding
import com.phonedev.pocketstore.entities.Constants.BASE_URL
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private var cartList: List<ProductosModeloItem>? = null
    private var totalToPay: Double = 0.0
    val user: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        //Get user id
        val pref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val user = pref.getInt("id_usuario", 0)

        getCart(user.toString())
        clicks()
    }

    private fun getCart(user: String) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WebServices::class.java)

        val retrofitData = retrofitBuilder.getCart(BASE_URL + "get_cart.php/?customer_id=${user}")

        retrofitData.enqueue(object : Callback<List<ProductosModeloItem>?> {
            override fun onResponse(
                call: Call<List<ProductosModeloItem>?>,
                response: Response<List<ProductosModeloItem>?>
            ) {
                val responseBody = response.body()!!
                val adapter = CarritoAdapter(responseBody)
                cartList = responseBody
                binding.recyclerView.layoutManager = LinearLayoutManager(this@CarritoActivity)
                binding.recyclerView.adapter = adapter
                calculateTotal()

            }

            override fun onFailure(call: Call<List<ProductosModeloItem>?>, t: Throwable) {
                Log.d("Error Search", t.toString())
            }
        })
    }

    private fun calculateTotal() {
        for (i in cartList!!) {
            totalToPay += i.totalPrice()
            binding.tvTotal.text = totalToPay.toString()
        }
    }

    private fun deleteItem(p: ProductosModeloItem) {
        val index = cartList?.indexOf(p)
        if (index != -1) {
            calculateTotal()
        }
    }

    private fun clicks() {
        binding.btnClose.setOnClickListener {
            onBackPressed()
            this.finish()
        }
    }
}