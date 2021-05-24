package com.alfanshter.iki_warung.ui.riwayat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.Model.BookingWarungmodels
import com.alfanshter.iki_warung.Model.Proses
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.adapter.ProsesPesananAdapter
import com.alfanshter.iki_warung.adapter.RiwayatAdapter
import com.alfanshter.iki_warung.databinding.FragmentRiwayatBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity

class RiwayatFragment : Fragment() {

    //database
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    lateinit var firestore: FirebaseFirestore

    //binding
    lateinit var binding : FragmentRiwayatBinding

    //adapter
    private var RiwayatAdapter : RiwayatAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_riwayat,container,false)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()

        binding.adapesanan.visibility = View.INVISIBLE
        binding.belumAdaPesanan.visibility = View.VISIBLE

        //Pesanan//


        tampilriwayat()
        return binding.root
    }

    private fun tampilriwayat() {
        val mLayoutManager = LinearLayoutManager(context!!.applicationContext)
        binding.rvRiwayatpesanan.layoutManager = mLayoutManager
        binding.rvRiwayatpesanan.itemAnimator = DefaultItemAnimator()

        val docref =
            firestore.collection(Constant.booking).whereEqualTo("idtoko", userID.toString())
                .whereEqualTo("status", "selesai").get().addOnSuccessListener { doc ->
                    if (doc.isEmpty) {
                    } else {
                        binding.adapesanan.visibility = View.VISIBLE
                        binding.belumAdaPesanan.visibility = View.INVISIBLE

                        val notesList = mutableListOf<BookingWarungmodels>()
                        for (document in doc.documents) {
                            try {
                                val data = document.toObject(BookingWarungmodels::class.java)
                                data!!.id_booking_warung = document.id
                                notesList.add(data)
                            } catch (e: Exception) {
                                return@addOnSuccessListener
                            }

                        }
                        RiwayatAdapter =
                            RiwayatAdapter(
                                notesList, context!!.applicationContext, firestore)
                        RiwayatAdapter!!.notifyDataSetChanged()
                        binding.rvRiwayatpesanan.adapter = RiwayatAdapter
                    }
                }
    }



}