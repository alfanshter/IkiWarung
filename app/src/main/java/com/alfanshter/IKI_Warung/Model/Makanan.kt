package com.alfanshter.IKI_Warung.Model

import com.google.gson.annotations.SerializedName

data class Makanan(
    @SerializedName("id") var id : String? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("kategori") var kategori : String? = null,
    @SerializedName("harga") var harga : Double? = null,
    @SerializedName("keterangan") var keterangan : String? = null,
    @SerializedName("status") var status : Boolean
)