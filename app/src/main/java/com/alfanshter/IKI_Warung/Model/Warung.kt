package com.alfanshter.IKI_Warung.Model

import com.google.gson.annotations.SerializedName

data class Warung(

	@field:SerializedName("nama_pemilik")
	val namaPemilik: String? = null,

	@field:SerializedName("jumlah_pembeli")
	val jumlahPembeli: Int? = null,

	@field:SerializedName("no_hp_warung")
	val noHpWarung: String? = null,

	@field:SerializedName("nama_warung")
	val namaWarung: String? = null,

	@field:SerializedName("nib")
	val nib: String? = null,

	@field:SerializedName("no_hp_pemilik")
	val noHpPemilik: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("owner_address")
	val ownerAddress: String? = null,

	@field:SerializedName("foto_ktp")
	val fotoKtp: String? = null,

	@field:SerializedName("foto_awal")
	val fotoAwal: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: Lokasi? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("food_address")
	val foodAddress: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Lokasi(

	@field:SerializedName("coordinates")
	val coordinates: List<Double?>? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
