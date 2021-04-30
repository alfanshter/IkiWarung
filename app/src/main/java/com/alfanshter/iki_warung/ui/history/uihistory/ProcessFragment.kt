package com.alfanshter.iki_warung.ui.history.uihistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.Model.BookingWarungmodels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.adapter.ProsesPesananAdapter
import com.alfanshter.iki_warung.databinding.FragmentProcessBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger


class ProcessFragment : Fragment(), AnkoLogger {
    lateinit var auth: FirebaseAuth
    var userID: String? = null

    //database
    lateinit var firestore: FirebaseFirestore

    //binding
    lateinit var binding: FragmentProcessBinding

    //adapter
    private var ProsesAdapter: ProsesPesananAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_process, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid

        binding.adapesanan.visibility = View.INVISIBLE
        binding.belumAdaPesanan.visibility = View.VISIBLE

        ambilprosespesanan()

        return binding.root
    }

    private fun ambilprosespesanan() {
        val mLayoutManager = LinearLayoutManager(context!!.applicationContext)
        binding.rvProsespesanan.layoutManager = mLayoutManager
        binding.rvProsespesanan.itemAnimator = DefaultItemAnimator()

        val docref =
            firestore.collection(Constant.booking).whereEqualTo("idtoko", userID.toString())
                .whereEqualTo("status", "Koneksi").get().addOnSuccessListener { doc ->
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
                        ProsesAdapter =
                            ProsesPesananAdapter(notesList, context!!.applicationContext, firestore)
                        ProsesAdapter!!.notifyDataSetChanged()
                        binding.rvProsespesanan.adapter = ProsesAdapter
                    }
                }

    }


}