package com.alfanshter.iki_warung.ui.history

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfanshter.iki_warung.Model.DataDriverModels
import com.alfanshter.iki_warung.Model.KeranjangModels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.adapter.RincianProsesAdapter
import com.alfanshter.iki_warung.databinding.ActivityRincianProsesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_rincian__proses.*

class Rincian_Proses : AppCompatActivity() {

    //bundle
    var nama_customer: String? = null
    var kode_order: String? = null
    var uid_pelanggan: String? = null
    var uid_driver: String? = null

    //binding
    lateinit var binding: ActivityRincianProsesBinding

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null

    //adapter
    private var AdapterRincian: RincianProsesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rincian__proses)
        binding.lifecycleOwner = this

        //inisialisasi database
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        val bundle: Bundle? = intent.extras
        nama_customer = bundle!!.getString("nama_customer")
        kode_order = bundle.getString("kode_order")
        uid_pelanggan = bundle.getString("uid_pelanggan")
        uid_driver = bundle.getString("uid_driver")

        binding.txtNamaCustomer.text = nama_customer.toString()
        binding.txtKodeOrder.text = kode_order.toString()
        binding.btnBack.setOnClickListener {
            finish()
        }


        daftarpesanan()
        datadriver()


    }

    private fun datadriver() {
        val datadriverref =
            firestore.collection(Constant.data_driver).document(uid_driver.toString()).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        try {
                            val data = doc.toObject(DataDriverModels::class.java)
                            binding.txtNamaDriver.text = data!!.nama.toString()
                            binding.txtPlatnomor.text = data.platnomor.toString()
                            Picasso.get().load(data.foto).into(binding.fotoDriver)
                            binding.btnWa.setOnClickListener {
                                val url =
                                    "https://api.whatsapp.com/send?phone=${data.notelp}"
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(url)
                                startActivity(i)
                            }
                        } catch (e: Exception) {
                            return@addOnSuccessListener
                        }
                    }
                }
    }

    private fun daftarpesanan() {
        val mLayoutManager = LinearLayoutManager(this)
        rv_rincian_proses.layoutManager = mLayoutManager
        rv_rincian_proses.itemAnimator = DefaultItemAnimator()
        val docref =
            firestore.collection(Constant.keranjang).whereEqualTo("id_warung", userId.toString())
                .whereEqualTo("id_pembeli", uid_pelanggan.toString()).get()
                .addOnSuccessListener { doc ->
                    if (doc.isEmpty) {

                    } else {
                        var harga_pesanan = 0
                        val notesList = mutableListOf<KeranjangModels>()
                        for (document in doc.documents) {
                            try {
                                val data = document.toObject(KeranjangModels::class.java)
                                data!!.id_makanan = document.id
                                notesList.add(data)
                                var harga_pesanan_database = document["harga_resto"].toString().toInt()
                                harga_pesanan += harga_pesanan_database
                                binding.txtHargaTotal.text = harga_pesanan.toString()

                            } catch (e: Exception) {
                                return@addOnSuccessListener
                            }
                        }
                        AdapterRincian = RincianProsesAdapter(
                            notesList,
                            this,
                            firestore
                        )
                        AdapterRincian!!.notifyDataSetChanged()
                        rv_rincian_proses.adapter = AdapterRincian
                    }
                }

    }
}