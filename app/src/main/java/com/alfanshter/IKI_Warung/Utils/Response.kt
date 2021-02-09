package com.alfanshter.IKI_Warung.Utils

import com.google.gson.annotations.SerializedName

data class LoginResponse(
      @SerializedName("status") var status: Int? = null,
      @SerializedName("token") var token : String? = null
)

data class WarungResponse(

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

data class MakananResponse(
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

data class MinumanResponse(

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

data class BukaWarungResponse(
      @SerializedName("status") var status : Int? = null,
      @SerializedName("message") var message : String? = null
)

data class TambahMakananResponse(
      @SerializedName("status") var status : Int? = null,
      @SerializedName("message") var message : String? = null
)
