package com.alfanshter.IKI_Warung.Ui.History.uiHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.IKI_Warung.Model.Pesanan
import com.alfanshter.IKI_Warung.Model.Proses
import com.alfanshter.IKI_Warung.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.startActivity


class ProcessFragment : Fragment(),AnkoLogger {
    private lateinit var recyclerView: RecyclerView
    lateinit var refinfo: DatabaseReference
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    lateinit var relativeLayout: RelativeLayout
    lateinit var constraint: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_process, container, false)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        relativeLayout = root.find(R.id.adapesanan)
        constraint = root.find(R.id.belum_ada_pesanan)

        constraint.visibility = View.INVISIBLE
        relativeLayout.visibility = View.VISIBLE

        //Pesanan//
        recyclerView = root.find(R.id.rv_prosespesanan)
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("BookingResto").child("Proses")
            .child(userID.toString())
        //=======================

        //reclerview komentar
        val option =
            FirebaseRecyclerOptions.Builder<Proses>().setQuery(refinfo, Proses::class.java)
                .build()
        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<Proses, MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val itemView = LayoutInflater.from(context?.applicationContext)
                        .inflate(R.layout.daftarpesanan, parent, false)
                    return MyViewHolder(itemView)
                }

                override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Proses) {
                    val refid = getRef(position).key.toString()

                    refinfo.addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            holder.mstatus.text = model.harga.toString()
                        }

                    })

                }
            }

        recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

        return root
    }




    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mkodeorder: TextView = itemView.findViewById(R.id.kodeorder)
        var mstatus: TextView = itemView.findViewById(R.id.status)
    }
}