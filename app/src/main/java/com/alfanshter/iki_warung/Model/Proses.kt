package com.alfanshter.iki_warung.Model

class Proses {
    var calendar : String? = null
    var harga: String? = null
    var idtoko: String? = null
    var kodeorder : String? = null
    var namaToko: String? = null
    var namadriver: String? = null
    var ongkir: String? = null
    var penumpang: String? = null
    var platnomor: String? = null
    var status : String? = null
    var uiddriver : String? = null

    constructor(){

    }

    constructor(
        calendar: String?,
        harga: String?,
        idtoko: String?,
        kodeorder: String?,
        namaToko: String?,
        namadriver: String?,
        ongkir: String?,
        penumpang: String?,
        platnomor: String?,
        status: String?,
        uiddriver: String?
    ) {
        this.calendar = calendar
        this.harga = harga
        this.idtoko = idtoko
        this.kodeorder = kodeorder
        this.namaToko = namaToko
        this.namadriver = namadriver
        this.ongkir = ongkir
        this.penumpang = penumpang
        this.platnomor = platnomor
        this.status = status
        this.uiddriver = uiddriver
    }


}