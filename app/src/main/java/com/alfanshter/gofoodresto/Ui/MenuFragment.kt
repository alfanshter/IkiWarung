package com.alfanshter.gofoodresto.Ui

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.gofoodresto.R
import com.alfanshter.gofoodresto.auth.UserModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class MenuFragment : Fragment(), AnkoLogger {
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    var dialog_bidding: Dialog? = null
    private lateinit var rvMakanan: RecyclerView
    private lateinit var rvMinuman: RecyclerView
    lateinit var refinfo: DatabaseReference
    var image_list: HashMap<String, String>? = null
    lateinit var reference: DatabaseReference
    lateinit var root: View
    lateinit var tambahbarang: RelativeLayout
    lateinit var dialog: AlertDialog

    var refid: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_menu, container, false)


        dialog_bidding = Dialog(context!!)
        dialog_bidding!!.setContentView(R.layout.layout_editharga)

        rvMakanan = root.find(R.id.rv_makanan)
        rvMinuman = root.find(R.id.rv_minuman)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        tambahbarang = root.find(R.id.rv_menu1)
        tambahbarang.setOnClickListener {
            startActivity<InsertFoodActivity>()
        }
        makanan()
        minuman()
        return root
    }

    private fun makanan() {
        val LayoutManager = LinearLayoutManager(context!!.applicationContext)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvMakanan.layoutManager = LayoutManager
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("User_Resto")
            .child(userID.toString())

        val query3 =
            FirebaseDatabase.getInstance().reference.child("Pandaan").child("User_Resto")
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
                    holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            // The switch is enabled/checked
                            refinfo.child(refid!!).child("status").setValue("Ready")
                            // Change the app background color
                        } else {
                            refinfo.child(refid!!).child("status").setValue("habis")

                        }
                    }

                    holder.delete.setOnClickListener {
                        showdialog()
                    }
                    refinfo.child(refid!!).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            holder.mtitle.text = model.nama
                            Picasso.get().load(model.gambar).fit().centerCrop().into(holder.mimage)
                            holder.mharga.text = model.harga
                            val status = p0.child("status").value.toString()
                            holder.switch.isChecked = status == "Ready"

                            holder.itemView.setOnClickListener {
                                startActivity<DetailActivity>(
                                    "firebase_gambar" to model.gambar,
                                    "firebase_harga" to model.harga,
                                    "firebase_nama" to model.nama)

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
        refinfo = FirebaseDatabase.getInstance().reference.child("Pandaan").child("User_Resto")
            .child(userID.toString())

        val query3 =
            FirebaseDatabase.getInstance().reference.child("Pandaan").child("User_Resto")
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


                    refinfo.child(refid!!).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (isChecked) {
                                    // The switch is enabled/checked
                                    refinfo.child(refid.toString()).child("status")
                                        .setValue("Ready")
                                    // Change the app background color
                                } else {
                                    refinfo.child(refid.toString()).child("status")
                                        .setValue("Habis")

                                }
                            }
                            holder.mtitle.text = model.nama
                            Picasso.get().load(model.gambar).fit().centerCrop().into(holder.mimage)
                            holder.mharga.text = model.harga
                            val status = p0.child("status").value.toString()
                            holder.switch.isChecked = status == "Ready"

                            holder.delete.setOnClickListener {
                                showdialog()
                            }
                            holder.mharga.setOnClickListener {
                                showPopUp()
                            }
                            holder.itemView.setOnClickListener {
                                startActivity<DetailActivity>(
                                    "firebase_gambar" to model.gambar,
                                    "firebase_harga" to model.harga,
                                    "firebase_nama" to model.nama,
                                    "firebaes_userid" to userID
                                    )

                            }
                        }

                    })
                }
            }


        rvMinuman.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

    }
    fun showHome(gambar : String?,nama : String?,harga : String?, id: String?,penjual:String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Apakah anda ingin menambahkan food ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var usermap : HashMap<String,Any?> = HashMap()
                    usermap["gambar"] = gambar
                    usermap["harga"] = harga
                    usermap["nama"] = nama
                    usermap["id"] = id
                    usermap["penjual"] = penjual



                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("Pandaan").child("Resto").child(userID.toString()).setValue(usermap)
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
    fun showdialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Apakah anda ingin menghapus food ini ? ")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    var database =
                        FirebaseDatabase.getInstance().reference
                            .child("Pandaan").child("User_Resto").child(userID.toString())
                    database.child(refid!!).removeValue().addOnCompleteListener {
                        if (it.isSuccessful) {
                            toast("berhasil terhapus")
                        } else {
                            toast("coba ulangi lagi")
                        }
                    }
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
                    FirebaseDatabase.getInstance().reference.child("Pandaan").child("User_Resto")
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

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mtitle: TextView = itemView.findViewById(R.id.name)
        var mimage: ImageView = itemView.findViewById(R.id.gambar_makanan)
        var mharga: TextView = itemView.findViewById(R.id.harga)
        var switch: Switch = itemView.findViewById(R.id.switch1)
        var delete: ImageView = itemView.findViewById(R.id.btn_delete)
    }


}