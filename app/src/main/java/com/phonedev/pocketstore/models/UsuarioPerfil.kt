package com.phonedev.pocketstore.models

import android.os.Parcel
import android.os.Parcelable

data class UsuarioPerfil(
    val id_usuario :Int,
    val nombre: String? = null,
    val apellido: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val usuario: String? = null,
    val correo: String? = null,
    val pass: String? = null,
    val imagen: String? = null,
    val tipo: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_usuario)
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(telefono)
        parcel.writeString(direccion)
        parcel.writeString(usuario)
        parcel.writeString(correo)
        parcel.writeString(pass)
        parcel.writeString(imagen)
        parcel.writeString(tipo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UsuarioPerfil> {
        override fun createFromParcel(parcel: Parcel): UsuarioPerfil {
            return UsuarioPerfil(parcel)
        }

        override fun newArray(size: Int): Array<UsuarioPerfil?> {
            return arrayOfNulls(size)
        }
    }
}
