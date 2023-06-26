package com.phonedev.pocketstore.models

import android.os.Parcel
import android.os.Parcelable

data class ComentariosModel(
    val id_comentario: Int? = 0,
    val id_producto: Int? = 0,
    val id_usuario: Int = 0,
    val nombre: String? = null,
    val imagen: String? = null,
    val text: String? = null,
    val fecha: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id_comentario)
        parcel.writeValue(id_producto)
        parcel.writeInt(id_usuario)
        parcel.writeString(text)
        parcel.writeString(fecha)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComentariosModel> {
        override fun createFromParcel(parcel: Parcel): ComentariosModel {
            return ComentariosModel(parcel)
        }

        override fun newArray(size: Int): Array<ComentariosModel?> {
            return arrayOfNulls(size)
        }
    }

}
