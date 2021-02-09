package com.alfanshter.IKI_Warung.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.IKI_Warung.*
import com.alfanshter.IKI_Warung.Adapter.MakananAdapter
import com.alfanshter.IKI_Warung.Adapter.MinumanAdapter
import com.alfanshter.IKI_Warung.R
import com.alfanshter.IKI_Warung.Utils.WarungResponse
import com.alfanshter.IKI_Warung.ViewModels.MakananState
import com.alfanshter.IKI_Warung.ViewModels.MakananViewModel
import com.alfanshter.IKI_Warung.ViewModels.MinumanViewModel
import com.alfanshter.IKI_Warung.ViewModels.WarungViewModel
import com.alfanshter.IKI_Warung.auth.UserModel
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_insert_food.*
import kotlinx.android.synthetic.main.fragment_menu.view.*
import kotlinx.android.synthetic.main.list_item.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import kotlin.math.roundToInt

class MenuFragment : Fragment(), AnkoLogger {
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    var dialog_bidding: Dialog? = null
    private lateinit var rvMakanan: RecyclerView
    private lateinit var rvMinuman: RecyclerView
     var refinfo: DatabaseReference? = null
    var image_list: HashMap<String, String>? = null
    lateinit var reference: DatabaseReference
    lateinit var root: View
    lateinit var tambahbarang: RelativeLayout
    lateinit var dialog: AlertDialog
    lateinit var switch : SwitchMaterial

    private lateinit var makananViewModel: MakananViewModel
    private lateinit var minumanViewModel: MinumanViewModel
    private lateinit var warungViewModel: WarungViewModel
    lateinit var sessionManager: SessionManager

    lateinit var getswitchlistener : ValueEventListener
    var refid: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_menu, container, false)
        dialog_bidding = Dialog(context!!)
        dialog_bidding!!.setContentView(R.layout.layout_editharga)
        switch = root.find(R.id.swt_buka)
        rvMakanan = root.find(R.id.rv_makanan)
        rvMinuman = root.find(R.id.rv_minuman)

        sessionManager = SessionManager(this@MenuFragment.requireContext())
//        auth = FirebaseAuth.getInstance()
//        userID = auth.currentUser!!.uid
        tambahbarang = root.find(R.id.rv_menu1)

        tambahbarang.setOnClickListener {
            startActivity<InsertFoodActivity>()
        }
//
//        root.rv_menu2.setOnClickListener {
//            startActivity<EditRestoActivity>()
//        }

//        if (auth==null){
//            userID = null
//            startActivity<SplashScreen>()
//            activity!!.finish()
//        }
//
//
//        makanan()
//        minuman()
//        switch()
//        getswitch()

        setupRecyler()
        makananViewModel = ViewModelProviders.of(this).get(MakananViewModel::class.java)
        minumanViewModel = ViewModelProviders.of(this).get(MinumanViewModel::class.java)
        warungViewModel = ViewModelProviders.of(this).get(WarungViewModel::class.java)

        warungViewModel.getDataWarungById(sessionManager.getAuthToken().toString())
        warungViewModel.getDataWarung().observe(this, Observer {
                getswitch(it)
        })

        makananViewModel.fetchAllMakanan(sessionManager.getAuthToken().toString())
        makananViewModel.getMakanan().observe(this, Observer {
            rvMakanan.adapter?.let { adapter ->
                if (adapter is MakananAdapter){
                    adapter.setMakanan(it)
                }
            }
        })

        minumanViewModel.fetchAllMinuman(sessionManager.getAuthToken().toString())
        minumanViewModel.getMinuman().observe(this, Observer {
            rvMinuman.adapter?.let { adapter ->
                if (adapter is MinumanAdapter){
                    adapter.setMinuman(it)
                }
            }
        })

        switch(sessionManager.getAuthToken().toString())

        return root
    }

    private fun setupRecyler(){
        rvMakanan.apply {
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = MakananAdapter(mutableListOf(),this@MenuFragment.requireContext())
        }

        rvMinuman.apply {
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = MinumanAdapter(mutableListOf(),this@MenuFragment.requireContext())
        }
    }

//    private fun handleUIState(it : MakananState){
//        when(it){
////            is MakananState.IsLoading -> isLoading(it.state)
//            is MakananState.Error -> {
//                toast(it.err)
////                isLoading(false)
//            }
//        }
//    }

    private fun toast(message : String?) = Toast.makeText(this@MenuFragment.requireContext(), message, Toast.LENGTH_LONG).show()

    private fun switch(token : String){
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                switch.text = "Buka"
                warungViewModel.updateStateWarung(token,true)
            }else{
                switch.text = "Tutup"
                warungViewModel.updateStateWarung(token,false)
            }
        }

    }
//    private fun switch(){
//        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan")
//        refinfo!!.child("Resto").child(userID.toString()).addValueEventListener(object :ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    switch.setOnCheckedChangeListener { buttonView, isChecked ->
//                        if (isChecked) {
//                            switch.text = "Buka"
//                            // The switch is enabled/checked
//                            refinfo!!.child("Resto").child(userID.toString()).child("status")
//                                .setValue("Buka")
//                            // Change the app background color
//                        } else {
//                            switch.text = "Tutup"
//                            refinfo!!.child("Resto").child(userID.toString()).child("status")
//                                .setValue("Tutup")
//                        }
//                    }
//                }
//                else {
//                    switch.isChecked = false
//                    switch.isClickable = false
//                     switch()
//                }
//
//
//            }
//
//        })
//
//    }

    private fun getswitch(warung : WarungResponse){
        var status = warung.status
        if(status == true){
            switch.apply {
                text = "Buka"
                isChecked = true
            }
        }else{
            switch.text = "Tutup"
        }
    }
//
//    private fun getswitch(){
//        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan")
//
//        getswitchlistener = object : ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                var data = snapshot.child("status").value.toString()
//                switch.isChecked = data == "Buka"
//                if (data.equals("Buka")){
//                    switch.text = "Buka"
//
//                }
//            }
//
//        }
//        refinfo!!.child("Resto").child(userID.toString()).addValueEventListener(getswitchlistener)
//
//    }
//
//    private fun makanan() {
//        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
//        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        rvMakanan.layoutManager = LayoutManager
//        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
//            .child(userID.toString())
//
//        val query3 =
//            FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
//                .child(userID.toString())
//                .orderByChild("kategori")
//                .equalTo("Makanan")
//
//        val newOptions = FirebaseRecyclerOptions.Builder<UserModel>()
//            .setQuery(query3, UserModel::class.java)
//            .build()
//
//        val firebaseRecyclerAdapter =
//            object : FirebaseRecyclerAdapter<UserModel, MenuFragment.MyViewHolder>(newOptions) {
//                override fun onCreateViewHolder(
//                    parent: ViewGroup,
//                    viewType: Int
//                ): MenuFragment.MyViewHolder {
//                    val itemView = LayoutInflater.from(context!!.applicationContext)
//                        .inflate(R.layout.list_item, parent, false)
//                    return MyViewHolder(
//                        itemView
//                    )
//                }
//
//                override fun onBindViewHolder(
//                    holder: MyViewHolder,
//                    position: Int,
//                    model: UserModel
//                ) {
//
//                    refid = getRef(position).key.toString()
//                    refinfo!!.child(refid!!).addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onCancelled(p0: DatabaseError) {
//
//                        }
//
//                        override fun onDataChange(p0: DataSnapshot) {
//                            holder.mtitle.text = model.nama
//                            holder.mharga.text = model.harga
//                            if (model.status.equals("Ready")){
//                                Picasso.get().load(model.gambar).fit().centerCrop().into(holder.mimage)
//
//                            }
//                            else{
//                                Picasso.get().load(R.drawable.makanan_habis).fit().centerCrop().into(holder.mimage)
//
//                            }
//                            val status = p0.child("status").value.toString()
//
//                            holder.itemView.setOnClickListener {
//                                startActivity<DetailActivity>(
//                                    "firebase_gambar" to model.gambar,
//                                    "firebase_harga" to model.harga,
//                                    "firebase_nama" to model.nama,
//                                    "firebase_keywarung" to model.id)
//
//                            }
//                        }
//
//                    })
//                }
//            }
//
//
//        rvMakanan.adapter = firebaseRecyclerAdapter
//        firebaseRecyclerAdapter.startListening()
//
//    }
//
//    private fun minuman() {
//        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
//        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        rvMinuman.layoutManager = LayoutManager
//        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
//            .child(userID.toString())
//
//        val query3 =
//            FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
//                .child(userID.toString())
//                .orderByChild("kategori")
//                .equalTo("Minuman")
//
//        val newOptions = FirebaseRecyclerOptions.Builder<UserModel>()
//            .setQuery(query3, UserModel::class.java)
//            .build()
//
//        val firebaseRecyclerAdapter =
//            object : FirebaseRecyclerAdapter<UserModel, MenuFragment.MyViewHolder>(newOptions) {
//                override fun onCreateViewHolder(
//                    parent: ViewGroup,
//                    viewType: Int
//                ): MenuFragment.MyViewHolder {
//                    val itemView = LayoutInflater.from(context!!.applicationContext)
//                        .inflate(R.layout.list_item, parent, false)
//                    return MyViewHolder(
//                        itemView
//                    )
//                }
//
//                override fun onBindViewHolder(
//                    holder: MyViewHolder,
//                    position: Int,
//                    model: UserModel
//                ) {
//
//                    refid = getRef(position).key.toString()
//                    refinfo!!.child(refid!!).addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onCancelled(p0: DatabaseError) {
//
//                        }
//
//                        override fun onDataChange(p0: DataSnapshot) {
//                            holder.mtitle.text = model.nama
//                            holder.mharga.text = model.harga
//                            if (model.status.equals("Ready")){
//                                Picasso.get().load(model.gambar).fit().centerCrop().into(holder.mimage)
//
//                            }
//                            else{
//                                Picasso.get().load(R.drawable.warung_habis).fit().centerCrop().into(holder.mimage)
//
//                            }
//                            val status = p0.child("status").value.toString()
//
//                            holder.itemView.setOnClickListener {
//                                startActivity<DetailActivity>(
//                                    "firebase_gambar" to model.gambar,
//                                    "firebase_harga" to model.harga,
//                                    "firebase_nama" to model.nama,
//                                    "firebase_keywarung" to model.id)
//
//                            }
//                        }
//
//                    })
//                }
//            }
//
//
//        rvMinuman.adapter = firebaseRecyclerAdapter
//        firebaseRecyclerAdapter.startListening()
//
//    }
//
//    fun showHome(gambar : String?,nama : String?,harga : String?, id: String?,penjual:String?) {
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Apakah anda ingin menambahkan food ini ? ")
//        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
//            when (which) {
//                DialogInterface.BUTTON_POSITIVE -> {
//                    var usermap : HashMap<String,Any?> = HashMap()
//                    usermap["gambar"] = gambar
//                    usermap["harga"] = harga
//                    usermap["nama"] = nama
//                    usermap["id"] = id
//                    usermap["penjual"] = penjual
//
//
//
//                    var database =
//                        FirebaseDatabase.getInstance().reference
//                            .child("Pandaan").child("Resto").child(userID.toString()).setValue(usermap)
//                }
//                DialogInterface.BUTTON_NEGATIVE -> {
//                }
//                DialogInterface.BUTTON_NEUTRAL -> {
//                }
//            }
//        }
//
//
//        // Set the alert dialog positive/yes button
//        builder.setPositiveButton("YES", dialogClickListener)
//
//        // Set the alert dialog negative/no button
//        builder.setNegativeButton("NO", dialogClickListener)
//
//        // Set the alert dialog neutral/cancel button
//        builder.setNeutralButton("CANCEL", dialogClickListener)
//
//
//        // Initialize the AlertDialog using builder object
//        dialog = builder.create()
//
//        // Finally, display the alert dialog
//        dialog.show()
//    }
//
//    private fun showPopUp() {
//        val textClose: TextView
//        val amount_bidding: EditText
//        val btnBidding: Button
//        amount_bidding = dialog_bidding!!.findViewById(R.id.edt_harga)
//        textClose = dialog_bidding!!.findViewById(R.id.txtclose)
//        btnBidding = dialog_bidding!!.findViewById(R.id.btn_lelang)
//
//
//        btnBidding.setOnClickListener {
//            amount_bidding.error = null
//            var cancel = false
//            var focusView: View? = null
//
//            if (TextUtils.isEmpty(amount_bidding.text.toString())) {
//                amount_bidding.error = getString(R.string.error_field_required)
//                focusView = amount_bidding
//                cancel = true
//            }
//
//            if (cancel) {
//                // There was an error; don't attempt login and focus the first
//                // form field with an error.
//                focusView!!.requestFocus()
//            } else {
//                var database =
//                    FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
//                        .child(userID.toString()).child(refid.toString())
//                database.child("harga").setValue(amount_bidding.text.toString())
//                // Check for a valid password, if the user entered one.
//                dialog_bidding!!.dismiss()
//
//            }
//
//
//        }
//
//        textClose.setOnClickListener { dialog_bidding!!.dismiss() }
//        dialog_bidding!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog_bidding!!.show()
//
//    }
//
//    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var mtitle: TextView = itemView.findViewById(R.id.name)
//        var mimage: ImageView = itemView.findViewById(R.id.gambar_makanan)
//        var mharga: TextView = itemView.findViewById(R.id.harga)
//       }
//

    override fun onDestroy() {
        super.onDestroy()
//        if (refinfo!=null){
//            refinfo!!.removeEventListener(getswitchlistener)
//        }

    }
}