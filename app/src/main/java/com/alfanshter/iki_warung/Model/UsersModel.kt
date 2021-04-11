package com.alfanshter.iki_warung.Model

import android.util.EventLog

data class UsersModel(
    var alamat_pemilik: String? = null,
    var alamat_warung: String? = null,
    var fotoktp: String? = null,
    var fotowarung: String? = null,
    var kecamatan: String? = null,
    var kelurahan: String? = null,
    var kode_sales: String? = null,
    var kota: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var namapemilik: String? = null,
    var namatoko: String? = null,
    var nib: String? = null,
    var nik: String? = null,
    var no_hp_pemilik: String? = null,
    var no_hp_warung: String? = null,
    var password: String? = null,
    var provinsi: String? = null,
    var username: String? = null,
    var status_aktivasi: Boolean? = null,
    var uid: String? = null,
    var tipe_akun: String? = null,
    var tanggal_pendaftaran: String? = null,
    var email: String? = null,
    var foto: String? = null,
    var nama: String? = null,
    var no_telp: String? = null
)