package com.alfanshter.gofoodresto.Ui.History

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.gofoodresto.Model.ModelOrder
import com.alfanshter.gofoodresto.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var root : View
    lateinit var refinfo: DatabaseReference
    lateinit var auth: FirebaseAuth
    var userID : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_history, container, false)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        foodpopular()

        return  root

    }

    private fun foodpopular() {
        recyclerView = root.find(R.id.rv_history)
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("History_Resto").child(userID.toString())
        val option =
            FirebaseRecyclerOptions.Builder<ModelOrder>().setQuery(refinfo, ModelOrder::class.java)
                .build()

        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<ModelOrder, MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val itemView = LayoutInflater.from(context?.applicationContext)
                        .inflate(R.layout.history_item, parent, false)
                    return MyViewHolder(
                        itemView
                    )
                }

                override fun onBindViewHolder(
                    holder: MyViewHolder,
                    position: Int,
                    model: ModelOrder
                ) {
                    val refid = getRef(position).key.toString()
                    refinfo.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            holder.mnamatoko.text = model.namatoko
                            holder.mtanggal.text = model.calender
                            holder.mharga.text = model.harga
                            holder.mname.text = model.namatoko
                            Picasso.get().load(model.gambar).fit().centerCrop().into(holder.mgambar)
                            holder.mpesan.setOnClickListener {
/*
                                startActivity<DetailFoodActivity>("firebase_idMakanan" to model.idtoko,
                                    "Firebase_gambarMakanan" to model.gambar
                                )
*/
                            }
                            holder.itemView.setOnClickListener {
                            }
                        }

                    })
                }
            }


        recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mnamatoko: TextView = itemView.findViewById(R.id.txt_itemtoko)
        var mtanggal: TextView = itemView.findViewById(R.id.txt_itemtanggal)
        var mdeskripsi: TextView = itemView.findViewById(R.id.txt_itemdeskripsi)
        var mharga: TextView = itemView.findViewById(R.id.txt_itemharga)
        var mgambar: ImageView = itemView.findViewById(R.id.img_itemmakanan)
        var mpesan: Button = itemView.findViewById(R.id.btn_itempesan)
        var mname: TextView = itemView.findViewById(R.id.name)
    }


}