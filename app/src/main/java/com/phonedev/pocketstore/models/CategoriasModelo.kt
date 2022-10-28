package com.phonedev.pocketstore.models

import android.os.Parcel
import android.os.Parcelable

data class CategoriasModelo(
    val id_categoria: Int? = 0,
    val descripcion: String? = null,
    val imagen: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_categoria)
        parcel.writeString(descripcion)
        parcel.writeString(imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoriasModelo> {
        override fun createFromParcel(parcel: Parcel): CategoriasModelo {
            return CategoriasModelo(parcel)
        }

        override fun newArray(size: Int): Array<CategoriasModelo?> {
            return arrayOfNulls(size)
        }
    }
}