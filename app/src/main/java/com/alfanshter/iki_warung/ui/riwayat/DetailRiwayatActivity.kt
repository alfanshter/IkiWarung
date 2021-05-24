package com.alfanshter.iki_warung.ui.riwayat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alfanshter.iki_warung.Model.DataDriverModels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.databinding.ActivityDetailRiwayatBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_riwayat.*

class DetailRiwayatActivity : AppCompatActivity() {

    //bundle
    var kode_order: String? = null
    var nama_warung: String? = null
    var harga_pesanan_resto: Int? = null
    var uid_driver: String? = null

    var namadriver: String? = null
    var platnomor: String? = null

    //database
    lateinit var firestore: FirebaseFirestore

    //binding
    lateinit var binding: ActivityDetailRiwayatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_riwayat)
        binding.lifecycleOwner = this

        //database inisialisasi
        firestore = FirebaseFirestore.getInstance()


        val bundle: Bundle? = intent.extras
        kode_order = bundle!!.getString("kode_order")
        nama_warung = bundle.getString("nama_warung")
        harga_pesanan_resto = bundle.getInt("harga_pesanan_resto")
        uid_driver = bundle.getString("uid_driver")

        binding.kodeorder.text = "Kode Order : ${kode_order.toString()}"
        binding.txtStatus.text = "Order IKI Warung"
        binding.txtNamawarung.text = "Nama Warung :  ${nama_warung.toString()}"
        binding.txtHargamakanan.text = "Rp. ${harga_pesanan_resto.toString()}"
        binding.txtHarga.text = "Rp. ${harga_pesanan_resto.toString()}"

        binding.txtNamadriver.text = "Nama Ojol : ${namadriver.toString()}"
        binding.txtPlatnomor.text = "${platnomor.toString()}"

        ambildataojek()

        binding.linearLayout2.setOnClickListener {
            finish()
        }


    }

    private fun ambildataojek() {
        val docref = firestore.collection(Constant.data_driver).document(uid_driver.toString()).get().addOnSuccessListener { doc->
            if (doc.exists()){
                try {
                    val data = doc.toObject(DataDriverModels::class.java)
                    binding.txtNamadriver.text = "Nama Ojol : ${data!!.nama}"
                    binding.txtPlatnomor.text = "Plat Nomor: ${data!!.platnomor}"
                    Picasso.get().load(data.foto).into(binding.fotoDriver)
                }catch (e :Exception){
                    return@addOnSuccessListener
                }
            }
        }
    }
}