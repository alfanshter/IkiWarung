package com.alfanshter.iki_warung.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.iki_warung.*
import com.alfanshter.iki_warung.Model.MakananModels
import com.alfanshter.iki_warung.R
import com.alfanshter.iki_warung.Utils.Constant
import com.alfanshter.iki_warung.Utils.CustomProgressDialog
import com.alfanshter.iki_warung.adapter.MakananAdapter
import com.alfanshter.iki_warung.viewmodel.FoodViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import java.util.*
import kotlin.collections.HashMap

class MenuFragment : Fragment(), AnkoLogger {
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    var dialog_bidding: Dialog? = null
    private lateinit var rvMakanan: RecyclerView
    private lateinit var rvMinuman: RecyclerView
    var refinfo: DatabaseReference? = null
    var image_list: HashMap<String, String>? = null
    lateinit var reference: DatabaseReference
    lateinit var tambahbarang: RelativeLayout
    lateinit var dialog: AlertDialog
    lateinit var switch: SwitchMaterial
    lateinit var getswitchlistener: ValueEventListener
    var refid: String? = null

    lateinit var foodViewModel: FoodViewModel
    private var progressdialog = CustomProgressDialog()

    private lateinit var adapter: MakananAdapter
    private lateinit var adapterminuman: MakananAdapter
    lateinit var root: View

    var arrayList = ArrayList<MakananModels>()
    var arrayListMinuman = ArrayList<MakananModels>()

    companion object {
        var status_switch: Boolean? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_menu, container, false)
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)


//        foodViewModel.ambilDataMakanan()
//        foodViewModel.getMakanan().observe(this, Observer {
//            binding.rvMakanan.adapter?.let { adapter ->
//                when (adapter) {
//                    is MakananAdapter -> adapter.setMakanan(it)
//                }
//            }
//        })
//        foodViewModel.ambilDataSwitchWarung()
//        foodViewModel.getState().observer(this, Observer {
//            handleUiState(it)
//        })
//        setswitch()

        dialog_bidding = Dialog(context!!)
        dialog_bidding!!.setContentView(R.layout.layout_editharga)
        switch = root.find(R.id.swt_buka)
        rvMakanan = root.find(R.id.rv_makanan)
        rvMinuman = root.find(R.id.rv_minuman)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        tambahbarang = root.find(R.id.rv_menu1)
        getmakanan()
        getminuman()
        tambahbarang.setOnClickListener {
            startActivity<InsertFoodActivity>()
        }

//        rvMenu2.setOnClickListener {
//            startActivity<EditRestoActivity>()
//        }

        if (auth == null) {
            userID = null
            startActivity<SplashScreen>()
            activity!!.finish()
        }


//        switch()


        return root
    }

//    private fun handleUiState(it: FoodViewModel.FoodState?) {
//        when (it) {
//            is FoodViewModel.FoodState.IsSuksesMenu -> getswitch(it.sukses!!)
//            is FoodViewModel.FoodState.IsLoading -> loading(it.loading)
//        }
//    }

//    fun getswitch(status: Int) {
//        binding.swtBuka.isChecked = status == 2
//        binding.swtBuka.text = "Buka"
//
//
//    }

/*
    fun setswitch() {
        swt_buka.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                foodViewModel.setDataSwitchWarung()
                status_switch = true
                binding.swtBuka.text = "Buka"
            } else {
                foodViewModel.setDataSwitchWarung()
                status_switch = false
                binding.swtBuka.text = "Tutup"
            }
        }
    }
*/



    private fun getmakanan() {
        rvMakanan.layoutManager = LinearLayoutManager(context!!.applicationContext)
        rvMakanan.setHasFixedSize(true)
        (rvMakanan.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref = firestore.collection("Warung_Resep").whereEqualTo("uid", userId.toString())
            .whereEqualTo("kategori", "Makanan").get().addOnSuccessListener { doc ->
                for (document in doc) {
                    val data = document.toObject(MakananModels::class.java)
                    val mylist = MakananModels()
                    mylist.setgambar(data.gambar_makanan.toString())
                    mylist.setname(data.nama.toString())
                    mylist.setprice(data.harga.toString())
                    arrayList.add(mylist)
                    adapter = MakananAdapter(arrayList, context!!.applicationContext)

                    rvMakanan.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
            }.addOnFailureListener {

            }
    }

    private fun getminuman(){
        rvMinuman.layoutManager = LinearLayoutManager(context!!.applicationContext)
        rvMinuman.setHasFixedSize(true)
        (rvMinuman.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL


        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val docref = firestore.collection("Warung_Resep").whereEqualTo("uid", userId.toString())
            .whereEqualTo("kategori", "Minuman").get().addOnSuccessListener { doc ->
                for (document in doc) {
                    val data = document.toObject(MakananModels::class.java)
                    val mylist = MakananModels()
                    mylist.setgambar(data.gambar_makanan.toString())
                    mylist.setname(data.nama.toString())
                    mylist.setprice(data.harga.toString())
                    mylist.setidmakanan(data.id_makanan.toString())
                    arrayListMinuman.add(mylist)
                    adapterminuman = MakananAdapter(arrayListMinuman, context!!.applicationContext)

                    rvMinuman.adapter = adapterminuman
                    adapterminuman.notifyDataSetChanged()

                }
            }.addOnFailureListener {

            }
    }

/*
    private fun switch(){
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan")
        refinfo!!.child("Resto").child(userID.toString()).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    switch.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            switch.text = "Buka"
                            // The switch is enabled/checked
                            refinfo!!.child("Resto").child(userID.toString()).child("status")
                                .setValue("Buka")
                            // Change the app background color
                        } else {
                            switch.text = "Tutup"
                            refinfo!!.child("Resto").child(userID.toString()).child("status")
                                .setValue("Tutup")
                        }
                    }
                }
                else {
                    switch.isChecked = false
                    switch.isClickable = false
                     switch()
                }


            }

        })

    }
*/

/*
    private fun getswitch(){
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan")

        getswitchlistener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.child("status").value.toString()
                switch.isChecked = data == "Buka"
                if (data.equals("Buka")){
                    switch.text = "Buka"

                }
            }

        }
        refinfo!!.child("Resto").child(userID.toString()).addValueEventListener(getswitchlistener)

    }
*/

 /*   private fun makanan() {
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvMakanan.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
            .child(userID.toString())

        val query3 =
            FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
                .child(userID.toString())
                .orderByChild("kategori")
                .equalTo("Makanan")

        val newOptions = FirebaseRecyclerOptions.Builder<UserModel>()
            .setQuery(query3, UserModel::class.java)
            .build()

        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<UserModel, MenuFragment.MyViewHolder>(newOptions) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): MenuFragment.MyViewHolder {
                    val itemView = LayoutInflater.from(context!!.applicationContext)
                        .inflate(R.layout.list_item, parent, false)
                    return MyViewHolder(
                        itemView
                    )
                }

                override fun onBindViewHolder(
                    holder: MyViewHolder,
                    position: Int,
                    model: UserModel
                ) {

                    refid = getRef(position).key.toString()
                    refinfo!!.child(refid!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                holder.mtitle.text = model.nama
                                holder.mharga.text = model.harga
                                if (model.status.equals("Ready")) {
                                    Picasso.get().load(model.gambar).fit().centerCrop()
                                        .into(holder.mimage)

                                } else {
                                    Picasso.get().load(R.drawable.makanan_habis).fit().centerCrop()
                                        .into(holder.mimage)

                                }
                                val status = p0.child("status").value.toString()

                                holder.itemView.setOnClickListener {
                                    startActivity<DetailActivity>(
                                        "firebase_gambar" to model.gambar,
                                        "firebase_harga" to model.harga,
                                        "firebase_nama" to model.nama,
                                        "firebase_keywarung" to model.id
                                    )

                                }
                            }

                        })
                }
            }


        rvMakanan.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

    }

    private fun minuman() {
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvMinuman.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
            .child(userID.toString())

        val query3 =
            FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
                .child(userID.toString())
                .orderByChild("kategori")
                .equalTo("Minuman")

        val newOptions = FirebaseRecyclerOptions.Builder<UserModel>()
            .setQuery(query3, UserModel::class.java)
            .build()

        val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<UserModel, MenuFragment.MyViewHolder>(newOptions) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): MenuFragment.MyViewHolder {
                    val itemView = LayoutInflater.from(context!!.applicationContext)
                        .inflate(R.layout.list_item, parent, false)
                    return MyViewHolder(
                        itemView
                    )
                }

                override fun onBindViewHolder(
                    holder: MyViewHolder,
                    position: Int,
                    model: UserModel
                ) {

                    refid = getRef(position).key.toString()
                    refinfo!!.child(refid!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                holder.mtitle.text = model.nama
                                holder.mharga.text = model.harga
                                if (model.status.equals("Ready")) {
                                    Picasso.get().load(model.gambar).fit().centerCrop()
                                        .into(holder.mimage)

                                } else {
                                    Picasso.get().load(R.drawable.warung_habis).fit().centerCrop()
                                        .into(holder.mimage)

                                }
                                val status = p0.child("status").value.toString()

                                holder.itemView.setOnClickListener {
                                    startActivity<DetailActivity>(
                                        "firebase_gambar" to model.gambar,
                                        "firebase_harga" to model.harga,
                                        "firebase_nama" to model.nama,
                                        "firebase_keywarung" to model.id
                                    )

                                }
                            }

                        })
                }
            }


        rvMinuman.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

    }
*/
/*
    fun showHome(gambar: String?, nama: String?, harga: String?, id: String?, penjual: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Apakah anda ingin menambahkan food ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var usermap: HashMap<String, Any?> = HashMap()
                    usermap["gambar"] = gambar
                    usermap["harga"] = harga
                    usermap["nama"] = nama
                    usermap["id"] = id
                    usermap["penjual"] = penjual


                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("Pandaan").child("Resto").child(userID.toString())
                            .setValue(usermap)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                }
                DialogInterface.BUTTON_NEUTRAL -> {
                }
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES", dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO", dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL", dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }
*/

/*
    private fun showPopUp() {
        val textClose: TextView
        val amount_bidding: EditText
        val btnBidding: Button
        amount_bidding = dialog_bidding!!.findViewById(R.id.edt_harga)
        textClose = dialog_bidding!!.findViewById(R.id.txtclose)
        btnBidding = dialog_bidding!!.findViewById(R.id.btn_lelang)


        btnBidding.setOnClickListener {
            amount_bidding.error = null
            var cancel = false
            var focusView: View? = null

            if (TextUtils.isEmpty(amount_bidding.text.toString())) {
                amount_bidding.error = getString(R.string.error_field_required)
                focusView = amount_bidding
                cancel = true
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView!!.requestFocus()
            } else {
                var database =
                    FirebaseDatabase.getInstance().reference.child("Pandaan").child("Resto_Detail")
                        .child(userID.toString()).child(refid.toString())
                database.child("harga").setValue(amount_bidding.text.toString())
                // Check for a valid password, if the user entered one.
                dialog_bidding!!.dismiss()

            }


        }

        textClose.setOnClickListener { dialog_bidding!!.dismiss() }
        dialog_bidding!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_bidding!!.show()

    }
*/
/*

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mtitle: TextView = itemView.findViewById(R.id.name)
        var mimage: ImageView = itemView.findViewById(R.id.gambar_makanan)
        var mharga: TextView = itemView.findViewById(R.id.harga)
    }
*/


    override fun onDestroy() {
        super.onDestroy()

    }

    fun loading(state: Boolean) {
        if (state) {
            progressdialog.show(activity!!, Constant.tunggu)
        } else {
            progressdialog.dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        foodViewModel.ambilDataMakanan()
    }

}