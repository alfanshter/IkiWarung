package com.alfanshter.iki_warung.Model

import com.google.firebase.Timestamp

 class MakananModels {
     var gambar_makanan: String? = null
     var harga: Int? = null
     var harga_ppn: Int? = null
     var harga_total: Int? = null
     var jumlah_makanan: Int? = null
     var id_makanan: String? = null
     var kategori: String? = null
     var keterangan: String? = null
     var kode_makanan: String? = null
     var nama: String? = null
     var tanggal_tambah: Timestamp? = null
     var uid: String? = null
     var status_makanan : String? = null

     constructor() {}
     constructor(
         gambar_makanan: String?,
         harga: Int?,
         harga_ppn: Int?,
         harga_total: Int?,
         jumlah_makanan: Int?,
         id_makanan: String?,
         kategori: String?,
         keterangan: String?,
         kode_makanan: String?,
         nama: String?,
         tanggal_tambah: Timestamp?,
         uid: String?,
         status_makanan: String?
     ) {
         this.gambar_makanan = gambar_makanan
         this.harga = harga
         this.harga_ppn = harga_ppn
         this.harga_total = harga_total
         this.jumlah_makanan = jumlah_makanan
         this.id_makanan = id_makanan
         this.kategori = kategori
         this.keterangan = keterangan
         this.kode_makanan = kode_makanan
         this.nama = nama
         this.tanggal_tambah = tanggal_tambah
         this.uid = uid
         this.status_makanan = status_makanan
     }

     fun getgambar(): String? {
         return gambar_makanan
     }

     fun setgambar(gambar: String?) {
         this.gambar_makanan = gambar
     }

     fun getname(): String? {
         return nama
     }

     fun setname(name: String?) {
         this.nama = name
     }

     fun getprice(): Int? {
         return harga
     }

     fun setprice(price: Int?) {
         this.harga = price
     }

     fun getidmakanan(): String? {
         return id_makanan
     }

     fun setidmakanan(idmakanan: String?) {
         this.id_makanan = idmakanan
     }





 }