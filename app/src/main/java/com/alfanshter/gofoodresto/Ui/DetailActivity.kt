package com.alfanshter.gofoodresto.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alfanshter.gofoodresto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity(), AnkoLogger {
    var gambar: String? = null
    var harga: String? = null
    var nama: String? = null
    var latitude : String? = null
    var longitude : String? = null
    var penjual : String? = null

    lateinit var progressDialog: ProgressDialog
    lateinit var dialog: AlertDialog

    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        progressDialog = ProgressDialog(this)

        database = FirebaseDatabase.getInstance().getReference("Pandaan")
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        val bundle: Bundle? = intent.extras
        gambar = bundle!!.getString("firebase_gambar")
        harga = bundle.getString("firebase_harga")
        nama = bundle.getString("firebase_nama")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                latitude =
                    snapshot.child("Akun_Resto").child(userID.toString())
                        .child("latitude").value.toString()
                longitude = snapshot.child("Akun_Resto").child(userID.toString()).child("longitude").value.toString()
                penjual = snapshot.child("Akun_Resto").child(userID.toString()).child("namatoko").value.toString()


            }

        })


        Picasso.get().load(gambar).into(gambar_detail)
        name.text = nama.toString()
        price.text = harga.toString()
        btn_icon.setOnClickListener {
            if (latitude != null && longitude != null) {
                showHome(gambar,nama.toString(),harga.toString(),userID.toString(),penjual.toString())
            }


        }

    }
    fun showHome(gambar : String?,nama : String?,harga : String?, id: String?,penjual:String?) {
        val builder = AlertDialog.Builder(this)
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
                    usermap["latitude"] = latitude
                    usermap["longitude"] = longitude




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


}


