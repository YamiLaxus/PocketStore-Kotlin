package com.phonedev.pocketstore.apis

import com.phonedev.pocketstore.models.CategoriasModelo
import com.phonedev.pocketstore.models.ComentariosModel
import com.phonedev.pocketstore.models.ProductosModeloItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WebServices {
    @GET("productos.php")
    fun getAllProducts(): Call<List<ProductosModeloItem>>

    //Usar @Query para hacer dinamico el URL
    @GET("categorias_list.php")
    fun getCategoriaSelected(@Query("?fk_categoria=") id_cat: String): Call<List<ProductosModeloItem>>

    @GET
    fun getPorCategoria(@Url url: String): Call<List<ProductosModeloItem>>

    @GET
    fun getBySearch(@Url url: String): Call<List<ProductosModeloItem>>

    @GET
    fun getByFavorites(@Url url: String): Call<List<ProductosModeloItem>>

    @GET
    fun getComments(@Url url: String): Call<List<ComentariosModel>>

    @GET
    fun getCart(@Url url: String): Call<List<ProductosModeloItem>>

    @GET("categorias_list.php")
    fun getCategoriesLIst(): Call<List<CategoriasModelo>>

    @GET("categorias.php?fk_categoria=1")
    fun getAccesoriesPhone(): Call<List<ProductosModeloItem>>

    @GET("categorias.php?fk_categoria=2")
    fun getPhone(): Call<List<ProductosModeloItem>>

    @GET("busar.php?nombre=")
    fun getSearch(item: String): Call<List<ProductosModeloItem>>

}