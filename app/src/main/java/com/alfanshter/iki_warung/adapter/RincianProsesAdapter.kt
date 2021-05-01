package com.alfanshter.iki_warung.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.Model.BookingWarungmodels
import com.alfanshter.iki_warung.Model.KeranjangModels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.ui.history.Rincian_Proses
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.text.SimpleDateFormat
import java.util.*

class RincianProsesAdapter(

    private val notesList: MutableList<KeranjangModels>,
    private val context: Context,
    private val firestore: FirebaseFirestore

) : RecyclerView.Adapter<RincianProsesAdapter.ViewHolder>(), AnkoLogger {

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama_makanan : TextView
        internal var harga : TextView
        internal var harga_total : TextView
        init {
        nama_makanan = view.find(R.id.txt_nama_makanan)
        harga = view.find(R.id.txt_harga)
        harga_total = view.find(R.id.txt_hargatotal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.daftar_proses, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note =notesList[position]
        holder.nama_makanan.text = note.nama
        holder.harga.text = "${note.jumlah_makanan} x ${note.harga_resto}"
        holder.harga_total.text = "Rp. ${note.harga_resto!! * note.jumlah_makanan!!}"

    }

    override fun getItemCount(): Int {
        return notesList.size
    }


}