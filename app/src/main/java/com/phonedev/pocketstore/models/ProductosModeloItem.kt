package com.phonedev.pocketstore.models

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelImpl
import com.google.firebase.database.Exclude

data class ProductosModeloItem(
    val id_categoria: String? = null,
    val cantidad: String? = null,
    val descripcion: String? = null,
    val estado: String? = null,
    val facebook_link: String? = null,
    val fecha: String? = null,
    val fk_categoria: String? = null,
    val id_producto: String? = null,
    val imagen: String? = null,
    val instagram_link: String? = null,
    val nombre: String? = null,
    val precio: String? = null,
    val telefono: String? = null,
    val tiempor_entrega: String? = null,
    val usuario: String? = null,
    @get:Exclude var nuevaCantidad: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_categoria)
        parcel.writeString(cantidad)
        parcel.writeString(descripcion)
        parcel.writeString(estado)
        parcel.writeString(facebook_link)
        parcel.writeString(fecha)
        parcel.writeString(fk_categoria)
        parcel.writeString(id_producto)
        parcel.writeString(imagen)
        parcel.writeString(instagram_link)
        parcel.writeString(nombre)
        parcel.writeString(precio)
        parcel.writeString(telefono)
        parcel.writeString(tiempor_entrega)
        parcel.writeString(usuario)
    }

    fun totalPrice(): Double = nuevaCantidad * precio!!.toDouble()

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductosModeloItem> {
        override fun createFromParcel(parcel: Parcel): ProductosModeloItem {
            return ProductosModeloItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductosModeloItem?> {
            return arrayOfNulls(size)
        }
    }
}