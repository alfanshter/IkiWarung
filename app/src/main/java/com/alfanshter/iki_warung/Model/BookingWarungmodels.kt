package com.alfanshter.iki_warung.Model

import com.google.firebase.Timestamp
import com.google.type.Date
import com.google.type.DateTime

data class BookingWarungmodels(
    var latitude_warung : String? = null,
    var longitude_warung : String? = null,
    var nama_warung : String? = null,
    var nama_customer : String? = null,
    var latitude_customer : String? = null,
    var longitude_customer : String? = null,
    var latitude_tujuan : String? = null,
    var longitude_tujuan : String? = null,
    var total_belanja : Int? = null,
    var ongkir : Int? = null,
    var harga_pesanan : Int? = null,
    var harga_pesanan_resto : Int? = null,
    var notelpon : String? = null,
    var idtoko : String? = null,
    var uidpelanggan : String? = null,
    var kode_order : String? = null,
    var id_booking_warung : String? = null,
    var id_booking : String? = null,
    var alamat : String? = null,
    var status : String? = null,
    var status_perjalanan : String? = null,
    var tipe_order : String? = null,
    var alamat_warung : String? = null,
    var uid_driver : String? = null,
    var alamat_customer : String? = null,
    var jarak : Float? = null,
    var tanggal_order : Timestamp? = null


)