package com.alfanshter.IKI_Warung.Ui.Riwayat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alfanshter.IKI_Warung.R
import kotlinx.android.synthetic.main.activity_detail_riwayat.*

class DetailRiwayatActivity : AppCompatActivity() {

    var kode_order : String? = null
    var status : String? = null
    var namatoko : String? = null
    var harga : String? = null
    var namadriver : String? = null
    var platnomor : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_riwayat)

        val bundle: Bundle? = intent.extras
        kode_order = bundle!!.getString("kode_order")
        status = bundle.getString("status")
        namatoko = bundle.getString("namatoko")
        harga = bundle.getString("harga")
        namadriver = bundle.getString("namadriver")
        platnomor = bundle.getString("platnomor")

        kodeorder.text = "Kode Order : ${kode_order.toString()}"
        txt_status.text = "Order IKI ${status.toString()}"
        txt_status.text = "Order IKI ${status.toString()}"
        txt_namawarung.text = "Nama Warung :  ${namatoko.toString()}"
        txt_hargamakanan.text = "Rp. ${harga.toString()}"
        txt_harga.text = "Rp. ${harga.toString()}"
        txt_namadriver.text = "Nama Ojol : ${namadriver.toString()}"
        txt_platnomor.text = "${platnomor.toString()}"

        linearLayout2.setOnClickListener {
            finish()
        }




    }
}