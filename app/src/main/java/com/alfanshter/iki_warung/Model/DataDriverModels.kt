package com.alfanshter.iki_warung.Model

import com.google.firebase.Timestamp

class DataDriverModels {
    var email: String? = null
    var username: String? = null
    var foto: String? = null
    var motor: String? = null
    var nama: String? = null
    var password: String? = null
    var platnomor: String? = null
    var uidtoko: String? = null
    var notelp: String? = null
    var desa: String? = null
    var kecamatan: String? = null
    var kabupaten: String? = null
    var provinsi: String? = null
    var alamat_detail: String? = null
    var jenis_kelamin: String? = null
    var kodeorder: String? = null
    var nik: String? = null
    var id_booking: String? = null
    var ojek_sales: String? = null
    var point: Float? = null
    var rating: Float? = null
    var saldo: Int? = null
    var saldohariini: Int? = null
    var saldosetor: Int? = null
    var hati: Int? = null
    var status: Int? = null
    var uid_pelanggan: String? = null
    var tipe_order: String? = null
    var uid_ojek: String? = null
    var tanggal_register: Timestamp? = null
    var tanggal_update: Timestamp? = null

    constructor()
    constructor(
        email: String?,
        username: String?,
        foto: String?,
        motor: String?,
        nama: String?,
        password: String?,
        platnomor: String?,
        uidtoko: String?,
        notelp: String?,
        desa: String?,
        kecamatan: String?,
        kabupaten: String?,
        provinsi: String?,
        alamat_detail: String?,
        jenis_kelamin: String?,
        kodeorder: String?,
        nik: String?,
        id_booking: String?,
        ojek_sales: String?,
        point: Float?,
        rating: Float?,
        saldo: Int?,
        saldohariini: Int?,
        saldosetor: Int?,
        hati: Int?,
        status: Int?,
        uid_pelanggan: String?,
        tipe_order: String?,
        uid_ojek: String?,
        tanggal_register: Timestamp?,
        tanggal_update: Timestamp?
    ) {
        this.email = email
        this.username = username
        this.foto = foto
        this.motor = motor
        this.nama = nama
        this.password = password
        this.platnomor = platnomor
        this.uidtoko = uidtoko
        this.notelp = notelp
        this.desa = desa
        this.kecamatan = kecamatan
        this.kabupaten = kabupaten
        this.provinsi = provinsi
        this.alamat_detail = alamat_detail
        this.jenis_kelamin = jenis_kelamin
        this.kodeorder = kodeorder
        this.nik = nik
        this.id_booking = id_booking
        this.ojek_sales = ojek_sales
        this.point = point
        this.rating = rating
        this.saldo = saldo
        this.saldohariini = saldohariini
        this.saldosetor = saldosetor
        this.hati = hati
        this.status = status
        this.uid_pelanggan = uid_pelanggan
        this.tipe_order = tipe_order
        this.uid_ojek = uid_ojek
        this.tanggal_register = tanggal_register
        this.tanggal_update = tanggal_update
    }

}
