package com.alfanshter.IKI_Warung.Model

import com.google.gson.annotations.SerializedName

data class ProdukMinuman(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("harga_ppn")
	val hargaPpn: Int? = null,

	@field:SerializedName("harga_pelanggan")
	val hargaPelanggan: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("terjual")
	val terjual: Int? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
