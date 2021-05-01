package com.alfanshter.iki_warung.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.Model.BookingWarungmodels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.ui.history.Rincian_Proses
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class ProsesPesananAdapter(
    private val notesList: MutableList<BookingWarungmodels>,
    private val context: Context,
    private val firestore: FirebaseFirestore
) : RecyclerView.Adapter<ProsesPesananAdapter.ViewHolder>(), AnkoLogger {

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama_customer: TextView
        internal var harga_pesanan: TextView
        internal var tanggal_order: TextView

        init {

            nama_customer = view.findViewById(R.id.txt_namacustomer)
            harga_pesanan = view.findViewById(R.id.txt_harga_pesanan)
            tanggal_order = view.findViewById(R.id.txt_tanggalorder)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.proses_pesanan, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note =notesList[position]
        val timestamp = note.tanggal_order
        val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()

        holder.nama_customer.text = note.nama_customer.toString()
        holder.harga_pesanan.text = note.harga_pesanan_resto.toString()
        holder.tanggal_order.text = date

        holder.itemView.setOnClickListener {
            context.startActivity(context.intentFor<Rincian_Proses>(
                "nama_customer" to note.nama_customer.toString(),
                "kode_order" to note.kode_order.toString(),
                "uid_pelanggan" to note.uidpelanggan.toString(),
                "uid_driver" to note.uid_driver.toString()

            ).newTask())
        }



    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}